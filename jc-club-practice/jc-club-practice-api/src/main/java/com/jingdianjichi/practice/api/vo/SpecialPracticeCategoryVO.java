package com.jingdianjichi.practice.api.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

//二级分类
@Data
public class SpecialPracticeCategoryVO implements Serializable {

    private String categoryName;

    private Long categoryId;

    private List<SpecialPracticeLabelVO> labelList;

}
