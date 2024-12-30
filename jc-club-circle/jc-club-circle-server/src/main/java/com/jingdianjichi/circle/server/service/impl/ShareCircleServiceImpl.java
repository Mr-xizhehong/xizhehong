package com.jingdianjichi.circle.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.jingdianjichi.circle.api.enums.IsDeletedFlagEnum;
import com.jingdianjichi.circle.api.req.RemoveShareCircleReq;
import com.jingdianjichi.circle.api.req.SaveShareCircleReq;
import com.jingdianjichi.circle.api.req.UpdateShareCircleReq;
import com.jingdianjichi.circle.api.vo.ShareCircleVO;
import com.jingdianjichi.circle.server.dao.ShareCircleMapper;
import com.jingdianjichi.circle.server.entity.po.ShareCircle;
import com.jingdianjichi.circle.server.service.ShareCircleService;
import com.jingdianjichi.circle.server.util.LoginUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.Duration;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 圈子信息 服务实现类
 */
@Service
@Slf4j
public class ShareCircleServiceImpl extends ServiceImpl<ShareCircleMapper, ShareCircle> implements ShareCircleService {

    private static final Cache<Integer, List<ShareCircleVO>> CACHE = Caffeine.newBuilder().initialCapacity(1)
            .maximumSize(1).expireAfterWrite(Duration.ofSeconds(30)).build();

    /**
     * 获取圈子列表
     */
    @Override
    public List<ShareCircleVO> listResult() {
        List<ShareCircleVO> res = CACHE.getIfPresent(1);
        return Optional.ofNullable(res).orElseGet(() -> {
            //通过mybatis-plus的查询接口获取所有圈子数据
            List<ShareCircle> list = super.list(Wrappers.<ShareCircle>lambdaQuery().eq(ShareCircle::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getCode()));
            //通过stream流过滤出圈子大类
            List<ShareCircle> parentList = list.stream().filter(item -> item.getParentId() == -1L).collect(Collectors.toList());
            //通过stream流,依靠parentId获取分组
            Map<Long, List<ShareCircle>> map = list.stream().collect(Collectors.groupingBy(ShareCircle::getParentId));
            List<ShareCircleVO> collect = parentList.stream().map(item -> {
                ShareCircleVO vo = new ShareCircleVO();
                vo.setId(item.getId());
                vo.setCircleName(item.getCircleName());
                vo.setIcon(item.getIcon());
                List<ShareCircle> shareCircles = map.get(item.getId());
                if (CollectionUtils.isEmpty(shareCircles)) {
                    vo.setChildren(Collections.emptyList());
                } else {
                    List<ShareCircleVO> children = shareCircles.stream().map(cItem -> {
                        ShareCircleVO cVo = new ShareCircleVO();
                        cVo.setId(cItem.getId());
                        cVo.setCircleName(cItem.getCircleName());
                        cVo.setIcon(cItem.getIcon());
                        cVo.setChildren(Collections.emptyList());
                        return cVo;
                    }).collect(Collectors.toList());
                    vo.setChildren(children);
                }
                return vo;
            }).collect(Collectors.toList());
            CACHE.put(1, collect);
            return collect;
        });
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean saveCircle(SaveShareCircleReq req) {
        ShareCircle circle = new ShareCircle();
        circle.setCircleName(req.getCircleName());
        circle.setIcon(req.getIcon());
        circle.setParentId(req.getParentId());
        circle.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        circle.setCreatedTime(new Date());
        circle.setCreatedBy(LoginUtil.getLoginId());
        // 清空缓存数据，确保缓存和数据库的一致性
        try {
            // 在执行数据库操作之前清除缓存，避免缓存不一致
            CACHE.invalidateAll();
        } catch (Exception e) {
            log.error("Failed to invalidate cache", e);
        }
        // 执行数据库保存操作
        boolean saveSuccess = save(circle);
        if (!saveSuccess) {
            log.error("Failed to save ShareCircle");
            // 可以根据需要抛出异常来回滚事务
            throw new RuntimeException("Failed to save ShareCircle");
        }
        
        return saveSuccess;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateCircle(UpdateShareCircleReq req) {
        LambdaUpdateWrapper<ShareCircle> update = Wrappers.<ShareCircle>lambdaUpdate()
                .eq(ShareCircle::getId, req.getId())
                .eq(ShareCircle::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getCode())
                .set(Objects.nonNull(req.getParentId()), ShareCircle::getParentId, req.getParentId())
                .set(Objects.nonNull(req.getIcon()), ShareCircle::getIcon, req.getIcon())
                .set(Objects.nonNull(req.getCircleName()), ShareCircle::getCircleName, req.getCircleName())
                .set(ShareCircle::getUpdateBy, LoginUtil.getLoginId())
                .set(ShareCircle::getUpdateTime, new Date());
        
        try {
            // 清理缓存数据，确保缓存与数据库数据一致性
            CACHE.invalidateAll();
        } catch (Exception e) {
            log.error("Failed to invalidate cache before updating ShareCircle with id: {}", req.getId(), e);
        }
        // 执行数据库更新操作
        boolean updateSuccess = super.update(update);
        
        if (!updateSuccess) {
            log.error("Failed to update ShareCircle with id: {}", req.getId());
            // 抛出异常触发事务回滚
            throw new RuntimeException("Failed to update ShareCircle");
        }
        return true;
    }

    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeCircle(RemoveShareCircleReq req) {
        // 删除自身
        boolean res = super.update(Wrappers.<ShareCircle>lambdaUpdate().eq(ShareCircle::getId, req.getId())
                .eq(ShareCircle::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getCode())
                .set(ShareCircle::getIsDeleted, IsDeletedFlagEnum.DELETED.getCode()));
        
        // 删除子圈子
        boolean childRes = super.update(Wrappers.<ShareCircle>lambdaUpdate().eq(ShareCircle::getParentId, req.getId())
                .eq(ShareCircle::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getCode())
                .set(ShareCircle::getIsDeleted, IsDeletedFlagEnum.DELETED.getCode()));
        
        if (res && childRes) {
            // 如果都删除成功，清空缓存
            CACHE.invalidateAll();
            return true;
        } else {
            log.error("Failed to remove ShareCircle with id:{}", req.getId());
            // 删除失败时，可以抛出异常来回滚事务
            throw new RuntimeException("Failed to remove ShareCircle");
        }
    }

}
