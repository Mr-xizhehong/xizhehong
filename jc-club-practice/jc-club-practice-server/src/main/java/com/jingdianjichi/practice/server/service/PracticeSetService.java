package com.jingdianjichi.practice.server.service;

import com.jingdianjichi.practice.api.common.PageResult;
import com.jingdianjichi.practice.api.req.GetPracticeSubjectsReq;
import com.jingdianjichi.practice.api.req.GetUnCompletePracticeReq;
import com.jingdianjichi.practice.api.vo.*;
import com.jingdianjichi.practice.server.entity.dto.PracticeSetDTO;
import com.jingdianjichi.practice.server.entity.dto.PracticeSubjectDTO;

import java.util.List;

public interface PracticeSetService {

    /**
     * 得到所有的专项练习name
     */
    List<SpecialPracticeVO> getSpecialPracticeContent();

    /**
     * 得到并生成练习题id
     */
    PracticeSetVO getAndAddPractice(PracticeSubjectDTO dto);

    /**
     * 获取练习题
     */
    PracticeSubjectListVO getSubjects(GetPracticeSubjectsReq req);

    /**
     * 获取题目
     */
    PracticeSubjectVO getPracticeSubject(PracticeSubjectDTO dto);

    /**
     * 获取模拟套题内容
     */
    PageResult<PracticeSetVO> getPreSetContent(PracticeSetDTO dto);

    /**
     * 获取未完成练习内容
     */
    PageResult<UnCompletePracticeSetVO> getUnCompletePractice(GetUnCompletePracticeReq req);

}
