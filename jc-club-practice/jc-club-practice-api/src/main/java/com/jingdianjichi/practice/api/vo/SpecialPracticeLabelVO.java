package com.jingdianjichi.practice.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

//标签
@Data
public class SpecialPracticeLabelVO implements Serializable {

    private Long id;

    /**
     * 分类id-标签ID
     */
    private String assembleId;

    private String labelName;

}
