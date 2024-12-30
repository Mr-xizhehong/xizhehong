package com.jingdianjichi.circle.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jingdianjichi.circle.api.common.PageResult;
import com.jingdianjichi.circle.api.req.GetShareMomentReq;
import com.jingdianjichi.circle.api.req.RemoveShareMomentReq;
import com.jingdianjichi.circle.api.req.SaveMomentCircleReq;
import com.jingdianjichi.circle.api.vo.ShareMomentVO;
import com.jingdianjichi.circle.server.entity.po.ShareMoment;

/**
 * 动态信息 服务类
 */
public interface ShareMomentService extends IService<ShareMoment> {

    Boolean saveMoment(SaveMomentCircleReq req);

    PageResult<ShareMomentVO> getMoments(GetShareMomentReq req);

    Boolean removeMoment(RemoveShareMomentReq req);

    void incrReplyCount(Long id, int count);

}
