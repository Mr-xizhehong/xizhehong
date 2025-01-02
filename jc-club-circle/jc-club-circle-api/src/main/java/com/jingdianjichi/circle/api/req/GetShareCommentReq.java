package com.jingdianjichi.circle.api.req;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class GetShareCommentReq implements Serializable {
    
    /**
     * 动态id
     */
    private Long id;

}
