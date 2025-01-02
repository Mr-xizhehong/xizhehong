package com.jingdianjichi.circle.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jingdianjichi.circle.api.common.PageResult;
import com.jingdianjichi.circle.api.req.GetShareMessageReq;
import com.jingdianjichi.circle.api.vo.ShareMessageVO;
import com.jingdianjichi.circle.server.entity.po.ShareMessage;

public interface ShareMessageService extends IService<ShareMessage> {

    PageResult<ShareMessageVO> getMessages(GetShareMessageReq req);

    void comment(String fromId, String toId, Long targetId);

    void reply(String fromId, String toId, Long targetId);

    Boolean unRead();

}
