package com.jingdianjichi.circle.server.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.jingdianjichi.circle.api.req.RemoveShareCircleReq;
import com.jingdianjichi.circle.api.req.SaveShareCircleReq;
import com.jingdianjichi.circle.api.req.UpdateShareCircleReq;
import com.jingdianjichi.circle.api.vo.ShareCircleVO;
import com.jingdianjichi.circle.server.entity.po.ShareCircle;

import java.util.List;

public interface ShareCircleService extends IService<ShareCircle> {

    List<ShareCircleVO> listResult();

    Boolean saveCircle(SaveShareCircleReq req);

    Boolean updateCircle(UpdateShareCircleReq req);

    Boolean removeCircle(RemoveShareCircleReq req);
}
