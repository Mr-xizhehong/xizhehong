package com.jingdianjichi.circle.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jingdianjichi.circle.api.common.PageInfo;
import com.jingdianjichi.circle.api.common.PageResult;
import com.jingdianjichi.circle.api.enums.IsDeletedFlagEnum;
import com.jingdianjichi.circle.api.req.GetShareMomentReq;
import com.jingdianjichi.circle.api.req.RemoveShareMomentReq;
import com.jingdianjichi.circle.api.req.SaveMomentCircleReq;
import com.jingdianjichi.circle.api.vo.ShareMomentVO;
import com.jingdianjichi.circle.server.dao.ShareCommentReplyMapper;
import com.jingdianjichi.circle.server.dao.ShareMomentMapper;
import com.jingdianjichi.circle.server.entity.dto.UserInfo;
import com.jingdianjichi.circle.server.entity.po.ShareCommentReply;
import com.jingdianjichi.circle.server.entity.po.ShareMoment;
import com.jingdianjichi.circle.server.rpc.UserRpc;
import com.jingdianjichi.circle.server.service.ShareMomentService;
import com.jingdianjichi.circle.server.util.LoginUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 动态信息 服务实现类
 */
@Service
@Slf4j
public class ShareMomentServiceImpl extends ServiceImpl<ShareMomentMapper, ShareMoment> implements ShareMomentService {

    @Resource
    private ShareCommentReplyMapper shareCommentReplyMapper;
    @Resource
    private UserRpc userRpc;

    @Override
    public Boolean saveMoment(SaveMomentCircleReq req) {

        ShareMoment moment = new ShareMoment();
        moment.setCircleId(req.getCircleId());
        moment.setContent(req.getContent());
        if (!CollectionUtils.isEmpty(req.getPicUrlList())) {
            moment.setPicUrls(JSON.toJSONString(req.getPicUrlList()));
        }
        moment.setReplyCount(0);
        moment.setCreatedBy(LoginUtil.getLoginId());
        moment.setCreatedTime(new Date());
        moment.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        return super.save(moment);

    }

    @Override
    public PageResult<ShareMomentVO> getMoments(GetShareMomentReq req) {

        LambdaQueryWrapper<ShareMoment> query = Wrappers.<ShareMoment>lambdaQuery()
                .eq(Objects.nonNull(req.getCircleId()), ShareMoment::getCircleId, req.getCircleId())
                .eq(ShareMoment::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getCode())
                .orderByDesc(ShareMoment::getCircleId);
        PageInfo pageInfo = req.getPageInfo();
        Page<ShareMoment> page = new Page<>(pageInfo.getPageNo(), pageInfo.getPageSize());
        Page<ShareMoment> pageRes = super.page(page, query);
        PageResult<ShareMomentVO> result = new PageResult<>();
        List<ShareMoment> records = pageRes.getRecords();
        List<String> userNameList = records.stream().map(ShareMoment::getCreatedBy).distinct().collect(Collectors.toList());
        Map<String, UserInfo> userInfoMap = userRpc.batchGetUserInfo(userNameList);
        UserInfo defaultUser = new UserInfo();
        //通过createBy（LoginId）得到nickName及用户头像并转成VO
        List<ShareMomentVO> list = records.stream().map(item -> {
            ShareMomentVO vo = new ShareMomentVO();
            vo.setId(item.getId());
            vo.setCircleId(item.getCircleId());
            vo.setContent(item.getContent());
            if (Objects.nonNull(item.getPicUrls())) {
                List<String> picList = JSONArray.parseArray(item.getPicUrls(), String.class);
                vo.setPicUrlList(picList);
            }
            vo.setReplyCount(item.getReplyCount());
            vo.setCreatedTime(item.getCreatedTime().getTime());
            UserInfo user = userInfoMap.getOrDefault(item.getCreatedBy(), defaultUser);
            vo.setUserName(user.getNickName());
            vo.setUserAvatar(user.getAvatar());
            return vo;
        }).collect(Collectors.toList());
        result.setRecords(list);
        result.setTotal((int) pageRes.getTotal());
        result.setPageSize(pageInfo.getPageSize());
        result.setPageNo(pageInfo.getPageNo());
        return result;
    }
    
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeMoment(RemoveShareMomentReq req) {
        // 级联删除相关评论和回复
        try {
            ShareCommentReply updateEntity = new ShareCommentReply();
            updateEntity.setIsDeleted(IsDeletedFlagEnum.DELETED.getCode());
            LambdaUpdateWrapper<ShareCommentReply> updateWrapper = Wrappers.<ShareCommentReply>lambdaUpdate()
                    .eq(ShareCommentReply::getMomentId, req.getId());
            int updatedCount = shareCommentReplyMapper.update(updateEntity, updateWrapper);
            log.info("Removed {} comment(s) and reply(s) for moment id: {}", updatedCount, req.getId());
        } catch (Exception e) {
            log.error("Failed to remove comments and replies for moment id: {}", req.getId(), e);
            throw new RuntimeException("Failed to remove associated comments and replies", e);
        }
        
        // 删除动态
        boolean momentRemoved = super.update(Wrappers.<ShareMoment>lambdaUpdate()
                .eq(ShareMoment::getId, req.getId())
                .eq(ShareMoment::getIsDeleted, IsDeletedFlagEnum.UN_DELETED.getCode())
                .set(ShareMoment::getIsDeleted, IsDeletedFlagEnum.DELETED.getCode()));
        
        if (!momentRemoved) {
            log.error("Failed to remove ShareMoment with id: {}", req.getId());
            throw new RuntimeException("Failed to remove ShareMoment");
        }
        
        return true;
    }

    @Override
    public void incrReplyCount(Long id, int count) {
        getBaseMapper().incrReplyCount(id, count);
    }

}
