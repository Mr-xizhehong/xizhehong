package com.jingdianjichi.practice.server.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.nacos.common.utils.CollectionUtils;
import com.jingdianjichi.practice.api.enums.*;
import com.jingdianjichi.practice.api.req.*;
import com.jingdianjichi.practice.api.vo.*;
import com.jingdianjichi.practice.server.dao.*;
import com.jingdianjichi.practice.server.entity.dto.*;
import com.jingdianjichi.practice.server.entity.po.*;
import com.jingdianjichi.practice.server.rpc.UserRpc;
import com.jingdianjichi.practice.server.service.PracticeDetailService;
import com.jingdianjichi.practice.server.util.DateUtils;
import com.jingdianjichi.practice.server.util.LoginUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PracticeDetailServiceImpl implements PracticeDetailService {

    @Resource
    private PracticeDetailDao practiceDetailDao;

    @Resource
    private PracticeSetDao practiceSetDao;

    @Resource
    private PracticeSetDetailDao practiceSetDetailDao;

    @Resource
    private PracticeDao practiceDao;

    @Resource
    private SubjectDao subjectDao;

    @Resource
    private SubjectRadioDao subjectRadioDao;

    @Resource
    private SubjectMultipleDao subjectMultipleDao;

    @Resource
    private SubjectJudgeDao subjectJudgeDao;

    @Resource
    private SubjectMappingDao subjectMappingDao;

    @Resource
    private SubjectLabelDao subjectLabelDao;

    @Resource
    private UserRpc userRpc;

    @Override
    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public Boolean submit(SubmitPracticeDetailReq req) {
        PracticePO practicePO = new PracticePO();
        Long practiceId = req.getPracticeId();
        Long setId = req.getSetId();
        practicePO.setSetId(setId);
        String timeUse = req.getTimeUse();
        String hour = timeUse.substring(0, 2);
        String minute = timeUse.substring(2, 4);
        String second = timeUse.substring(4, 6);
        practicePO.setTimeUse(hour + ":" + minute + ":" + second);
        practicePO.setSubmitTime(DateUtils.parseStrToDate(req.getSubmitTime()));
        practicePO.setCompleteStatus(CompleteStatusEnum.COMPLETE.getCode());
        practicePO.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        practicePO.setCreatedBy(LoginUtil.getLoginId());
        practicePO.setCreatedTime(new Date());
        
        //计算正确率
        Integer correctCount = practiceDetailDao.selectCorrectCount(practiceId);
        List<PracticeSetDetailPO> practiceSetDetailPOS = practiceSetDetailDao.selectBySetId(setId);
        Integer totalCount = practiceSetDetailPOS.size();
        BigDecimal correctRate = new BigDecimal(correctCount.toString()).divide(new BigDecimal(totalCount.toString()), 4, BigDecimal.ROUND_HALF_UP)
                .multiply(new BigDecimal("100.00"));
        practicePO.setCorrectRate(correctRate);
        
        //查看是否为第一次提交，是则插入新记录，不是则更新记录
        PracticePO po = practiceDao.selectById(practiceId);
        if (Objects.isNull(po)) {
            practiceDao.insert(practicePO);
        } else {
            practicePO.setId(practiceId);
            practiceDao.update(practicePO);
        }
        //练习对应的set套题热度+1
        practiceSetDao.updateHeat(setId);
        
        //通过stream流的filter得到仍然未作答的题目集合
        List<PracticeDetailPO> practiceDetailPOList = practiceDetailDao.selectByPracticeId(practiceId);
        List<PracticeSetDetailPO> minusList = practiceSetDetailPOS.stream()
                .filter(item -> !practiceDetailPOList.stream()
                        .map(e -> e.getSubjectId())
                        .collect(Collectors.toList())
                        .contains(item.getSubjectId()))
                .collect(Collectors.toList());
        if (log.isInfoEnabled()) {
            log.info("仍然未做的题目集合{}", JSON.toJSONString(minusList));
        }
        if (CollectionUtils.isNotEmpty(minusList)) {
            minusList.forEach(e -> {
                PracticeDetailPO practiceDetailPO = new PracticeDetailPO();
                practiceDetailPO.setPracticeId(practiceId);
                practiceDetailPO.setSubjectType(e.getSubjectType());
                practiceDetailPO.setSubjectId(e.getSubjectId());
                practiceDetailPO.setAnswerStatus(AnswerStatusEnum.ERROR.getCode());
                practiceDetailPO.setAnswerContent("");
                practiceDetailPO.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
                practiceDetailPO.setCreatedTime(new Date());
                practiceDetailPO.setCreatedBy(LoginUtil.getLoginId());
                practiceDetailDao.insertSingle(practiceDetailPO);
            });
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean submitSubject(SubmitSubjectDetailReq req) {
        
        //计算用时并存储
        String timeUse = req.getTimeUse();
        if (timeUse.equals("0")) {
            timeUse = "000000";
        }
        String hour = timeUse.substring(0, 2);
        String minute = timeUse.substring(2, 4);
        String second = timeUse.substring(4, 6);
        PracticePO practicePO = new PracticePO();
        practicePO.setId(req.getPracticeId());
        practicePO.setTimeUse(hour + ":" + minute + ":" + second);
        practicePO.setSubmitTime(new Date());
        practiceDao.update(practicePO);

        PracticeDetailPO practiceDetailPO = new PracticeDetailPO();
        practiceDetailPO.setPracticeId(req.getPracticeId());
        practiceDetailPO.setSubjectId(req.getSubjectId());
        practiceDetailPO.setSubjectType(req.getSubjectType());
        
        //存储作答的答案
        String answerContent = "";
        if (CollectionUtils.isNotEmpty(req.getAnswerContents())) {
            List<Integer> answerContents = req.getAnswerContents();
            Collections.sort(answerContents);
            answerContent = StringUtils.join(answerContents, ",");
        }
        practiceDetailPO.setAnswerContent(answerContent);
        
        //获取正确答案，并判断答案是否正确
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setSubjectId(req.getSubjectId());
        subjectDTO.setSubjectType(req.getSubjectType());
        SubjectDetailDTO subjectDetail = this.getSubjectDetail(subjectDTO);
        StringBuffer correctAnswer = new StringBuffer();
        if (req.getSubjectType().equals(SubjectInfoTypeEnum.JUDGE.getCode())) {
            Integer isCorrect = subjectDetail.getIsCorrect();
            correctAnswer.append(isCorrect);
        } else {
            //单选题和多选题组装答案
            subjectDetail.getOptionList().forEach(e -> {
                if (Objects.equals(e.getIsCorrect(), 1)) {
                    correctAnswer.append(e.getOptionType()).append(",");
                }
            });
            //去除最后一个逗号
            if (correctAnswer.length() > 0) {
                correctAnswer.deleteCharAt(correctAnswer.length() - 1);
            }
        }
        if (Objects.equals(correctAnswer.toString(), answerContent)) {
            practiceDetailPO.setAnswerStatus(AnswerStatusEnum.CORRECT.getCode());
        } else {
            practiceDetailPO.setAnswerStatus(AnswerStatusEnum.ERROR.getCode());
        }
        
        practiceDetailPO.setIsDeleted(IsDeletedFlagEnum.UN_DELETED.getCode());
        practiceDetailPO.setCreatedBy(LoginUtil.getLoginId());
        practiceDetailPO.setCreatedTime(new Date());
        
        //判断是否已经存在提交记录，存在则更新，不存在则插入
        PracticeDetailPO existDetail = practiceDetailDao.selectDetail(req.getPracticeId(), req.getSubjectId(), LoginUtil.getLoginId());
        if (Objects.isNull(existDetail)) {
            practiceDetailDao.insertSingle(practiceDetailPO);
        } else {
            practiceDetailPO.setId(existDetail.getId());
            practiceDetailDao.update(practiceDetailPO);
        }
        return true;
    }


    public SubjectDetailDTO getSubjectDetail(SubjectDTO dto) {
        SubjectDetailDTO subjectDetailDTO = new SubjectDetailDTO();
        SubjectPO subjectPO = subjectDao.selectById(dto.getSubjectId());
        if (dto.getSubjectType() == SubjectInfoTypeEnum.RADIO.getCode()) {
            List<SubjectOptionDTO> optionList = new LinkedList<>();
            List<SubjectRadioPO> radioSubjectPOS = subjectRadioDao.selectBySubjectId(subjectPO.getId());
            radioSubjectPOS.forEach(e -> {
                SubjectOptionDTO subjectOptionDTO = new SubjectOptionDTO();
                subjectOptionDTO.setOptionContent(e.getOptionContent());
                subjectOptionDTO.setOptionType(e.getOptionType());
                subjectOptionDTO.setIsCorrect(e.getIsCorrect());
                optionList.add(subjectOptionDTO);
            });
            subjectDetailDTO.setOptionList(optionList);
        }
        if (dto.getSubjectType() == SubjectInfoTypeEnum.MULTIPLE.getCode()) {
            List<SubjectOptionDTO> optionList = new LinkedList<>();
            List<SubjectMultiplePO> multipleSubjectPOS = subjectMultipleDao.selectBySubjectId(subjectPO.getId());
            multipleSubjectPOS.forEach(e -> {
                SubjectOptionDTO subjectOptionDTO = new SubjectOptionDTO();
                subjectOptionDTO.setOptionContent(e.getOptionContent());
                subjectOptionDTO.setOptionType(e.getOptionType());
                subjectOptionDTO.setIsCorrect(e.getIsCorrect());
                optionList.add(subjectOptionDTO);
            });
            subjectDetailDTO.setOptionList(optionList);
        }
        if (dto.getSubjectType() == SubjectInfoTypeEnum.JUDGE.getCode()) {
            SubjectJudgePO judgeSubjectPO = subjectJudgeDao.selectBySubjectId(subjectPO.getId());
            subjectDetailDTO.setIsCorrect(judgeSubjectPO.getIsCorrect());
        }
        subjectDetailDTO.setSubjectParse(subjectPO.getSubjectParse());
        subjectDetailDTO.setSubjectName(subjectPO.getSubjectName());
        return subjectDetailDTO;
    }

    @Override
    public List<ScoreDetailVO> getScoreDetail(GetScoreDetailReq req) {
        Long practiceId = req.getPracticeId();
        List<ScoreDetailVO> list = new LinkedList<>();
        List<PracticeDetailPO> practiceDetailPOList = practiceDetailDao.selectByPracticeId(practiceId);
        if (CollectionUtils.isEmpty(practiceDetailPOList)) {
            return Collections.emptyList();
        }
        practiceDetailPOList.forEach(po -> {
            ScoreDetailVO scoreDetailVO = new ScoreDetailVO();
            scoreDetailVO.setSubjectId(po.getSubjectId());
            scoreDetailVO.setSubjectType(po.getSubjectType());
            scoreDetailVO.setIsCorrect(po.getAnswerStatus());
            list.add(scoreDetailVO);
        });
        return list;
    }

    @Override
    public SubjectDetailVO getSubjectDetail(GetSubjectDetailReq req) {
        SubjectDetailVO subjectDetailVO = new SubjectDetailVO();
        Long subjectId = req.getSubjectId();
        Integer subjectType = req.getSubjectType();
        SubjectDTO subjectDTO = new SubjectDTO();
        subjectDTO.setSubjectId(subjectId);
        subjectDTO.setSubjectType(subjectType);
        SubjectDetailDTO subjectDetail = getSubjectDetail(subjectDTO);
        List<SubjectOptionDTO> optionList = subjectDetail.getOptionList();
        List<PracticeSubjectOptionVO> optionVOList = new LinkedList<>();
        List<Integer> correctAnswer = new LinkedList<>();
        
        //拼接正确答案，判断题题没有optionList,所有可以通过判断optionList是否为空来判断题目类型
        if (CollectionUtils.isNotEmpty(optionList)) {
            optionList.forEach(option -> {
                PracticeSubjectOptionVO optionVO = new PracticeSubjectOptionVO();
                optionVO.setOptionType(option.getOptionType());
                optionVO.setOptionContent(option.getOptionContent());
                optionVO.setIsCorrect(option.getIsCorrect());
                optionVOList.add(optionVO);
                if (option.getIsCorrect() == 1) {
                    correctAnswer.add(option.getOptionType());
                }
            });
        }
        if (subjectType.equals(SubjectInfoTypeEnum.JUDGE.getCode())) {
            Integer isCorrect = subjectDetail.getIsCorrect();
            PracticeSubjectOptionVO correctOption = new PracticeSubjectOptionVO();
            correctOption.setOptionType(1);
            correctOption.setOptionContent("正确");
            correctOption.setIsCorrect(isCorrect == 1 ? 1 : 0);
            PracticeSubjectOptionVO errorOptionVO = new PracticeSubjectOptionVO();
            errorOptionVO.setOptionType(2);
            errorOptionVO.setOptionContent("错误");
            errorOptionVO.setIsCorrect(isCorrect == 0 ? 1 : 0);
            optionVOList.add(correctOption);
            optionVOList.add(errorOptionVO);
            correctAnswer.add(subjectDetail.getIsCorrect());
        }
        subjectDetailVO.setOptionList(optionVOList);
        subjectDetailVO.setSubjectParse(subjectDetail.getSubjectParse());
        subjectDetailVO.setSubjectName(subjectDetail.getSubjectName());
        subjectDetailVO.setCorrectAnswer(correctAnswer);
        
        //自己的答题答案
        List<Integer> respondAnswer = new LinkedList<>();
        PracticeDetailPO practiceDetailPO = practiceDetailDao.selectAnswer(req.getPracticeId(), subjectId);
        String answerContent = practiceDetailPO.getAnswerContent();
        if (StringUtils.isNotBlank(answerContent)) {
            String[] split = answerContent.split(",");
            for (String s : split) {
                respondAnswer.add(Integer.valueOf(s));
            }
        }
        subjectDetailVO.setRespondAnswer(respondAnswer);
        
        //得到题目label
        List<SubjectMappingPO> subjectMappingPOList = subjectMappingDao.getLabelIdsBySubjectId(subjectId);
        List<Long> labelIdList = subjectMappingPOList.stream().map(SubjectMappingPO::getLabelId).collect(Collectors.toList());
        List<String> labelNameList = subjectLabelDao.getLabelNameByIds(labelIdList);
        subjectDetailVO.setLabelNames(labelNameList);
        return subjectDetailVO;
    }

    @Override
    public ReportVO getReport(GetReportReq req) {
        //获取set套题名称
        ReportVO reportVO = new ReportVO();
        Long practiceId = req.getPracticeId();
        PracticePO practicePO = practiceDao.selectById(practiceId);
        Long setId = practicePO.getSetId();
        PracticeSetPO practiceSetPO = practiceSetDao.selectById(setId);
        reportVO.setTitle(practiceSetPO.getSetName());
        
        //获取正确数/总题数
        List<PracticeDetailPO> practiceDetailPOList = practiceDetailDao.selectByPracticeId(practiceId);
        if (CollectionUtils.isEmpty(practiceDetailPOList)) {
            return null;
        }
        int totalCount = practiceDetailPOList.size();
        List<PracticeDetailPO> correctPoList = practiceDetailPOList.stream().filter(e ->
                Objects.equals(e.getAnswerStatus(), AnswerStatusEnum.CORRECT.getCode())).collect(Collectors.toList());
        reportVO.setCorrectSubject(correctPoList.size() + "/" + totalCount);
        
        //获得技能图谱
        List<ReportSkillVO> reportSkillVOS = new LinkedList<>();
        Map<Long, Integer> totalMap = getSubjectLabelMap(practiceDetailPOList);
        Map<Long, Integer> correctMap = getSubjectLabelMap(correctPoList);
        totalMap.forEach((key, val) -> {
            ReportSkillVO skillVO = new ReportSkillVO();
            SubjectLabelPO labelPO = subjectLabelDao.queryById(key);
            String labelName = labelPO.getLabelName();
            Integer correctCount = correctMap.get(key);
            if (Objects.isNull(correctCount)) {
                correctCount = 0;
            }
            skillVO.setName(labelName);
            BigDecimal rate = BigDecimal.ZERO;
            if (!Objects.equals(val, 0)) {
                rate = new BigDecimal(correctCount.toString()).divide(new BigDecimal(val.toString()), 4,
                        BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
            }
            skillVO.setStar(rate);
            reportSkillVOS.add(skillVO);
        });
        if (log.isInfoEnabled()) {
            log.info("获取到的正确率{}", JSON.toJSONString(reportSkillVOS));
        }
        reportVO.setSkill(reportSkillVOS);
        return reportVO;
    }

    /**
     * 获取题目对应的标签map
     */
    private Map<Long, Integer> getSubjectLabelMap(List<PracticeDetailPO> practiceDetailPOList) {
        if (CollectionUtils.isEmpty(practiceDetailPOList)) {
            return Collections.emptyMap();
        }
        Map<Long, Integer> map = new HashMap<>();
        practiceDetailPOList.forEach(detail -> {
            Long subjectId = detail.getSubjectId();
            List<SubjectMappingPO> labelIdPO = subjectMappingDao.getLabelIdsBySubjectId(subjectId);
            labelIdPO.forEach(po -> {
                Long labelId = po.getLabelId();
                if (Objects.isNull(map.get(labelId))) {
                    map.put(labelId, 1);
                    return;
                }
                map.put(labelId, map.get(labelId) + 1);
            });
        });
        if (log.isInfoEnabled()) {
            log.info("获取到的题目对应的标签map{}", JSON.toJSONString(map));
        }
        return map;
    }

    /**
     * 获取练习题目排行榜
     */
    @Override
    public List<RankVO> getPracticeRankList() {
        //考虑用户可能可能一套题会有多次提交，对于使用zset进行排行榜实现会有问题，所以不考虑
        List<RankVO> list = new LinkedList<>();
        List<PracticeRankPO> poList = practiceDetailDao.getPracticeCount();
        if (CollectionUtils.isEmpty(poList)) {
            return list;
        }
        poList.forEach(e -> {
            RankVO rankVO = new RankVO();
            rankVO.setCount(e.getCount());
            UserInfo userInfo = userRpc.getUserInfo(e.getCreatedBy());
            rankVO.setName(userInfo.getNickName());
            rankVO.setAvatar(userInfo.getAvatar());
            list.add(rankVO);
        });
        return list;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean giveUp(Long practiceId) {
        practiceDetailDao.deleteByPracticeId(practiceId);
        practiceDao.deleteById(practiceId);
        return true;
    }


}