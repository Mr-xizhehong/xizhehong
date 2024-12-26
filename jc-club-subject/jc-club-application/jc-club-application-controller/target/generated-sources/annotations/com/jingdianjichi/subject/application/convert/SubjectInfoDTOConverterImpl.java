package com.jingdianjichi.subject.application.convert;

import com.jingdianjichi.subject.application.dto.SubjectAnswerDTO;
import com.jingdianjichi.subject.application.dto.SubjectInfoDTO;
import com.jingdianjichi.subject.domain.entity.SubjectAnswerBO;
import com.jingdianjichi.subject.domain.entity.SubjectInfoBO;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-26T15:57:02+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.40.0.z20241112-1021, environment: Java 17.0.13 (Eclipse Adoptium)"
)
public class SubjectInfoDTOConverterImpl implements SubjectInfoDTOConverter {

    @Override
    public SubjectInfoBO convertDTOToBO(SubjectInfoDTO subjectInfoDTO) {
        if ( subjectInfoDTO == null ) {
            return null;
        }

        SubjectInfoBO subjectInfoBO = new SubjectInfoBO();

        subjectInfoBO.setPageNo( subjectInfoDTO.getPageNo() );
        subjectInfoBO.setPageSize( subjectInfoDTO.getPageSize() );
        subjectInfoBO.setCategoryId( subjectInfoDTO.getCategoryId() );
        List<Integer> list = subjectInfoDTO.getCategoryIds();
        if ( list != null ) {
            subjectInfoBO.setCategoryIds( new ArrayList<Integer>( list ) );
        }
        subjectInfoBO.setCreateUser( subjectInfoDTO.getCreateUser() );
        subjectInfoBO.setCreateUserAvatar( subjectInfoDTO.getCreateUserAvatar() );
        subjectInfoBO.setId( subjectInfoDTO.getId() );
        subjectInfoBO.setKeyWord( subjectInfoDTO.getKeyWord() );
        subjectInfoBO.setLabelId( subjectInfoDTO.getLabelId() );
        List<Integer> list1 = subjectInfoDTO.getLabelIds();
        if ( list1 != null ) {
            subjectInfoBO.setLabelIds( new ArrayList<Integer>( list1 ) );
        }
        List<String> list2 = subjectInfoDTO.getLabelName();
        if ( list2 != null ) {
            subjectInfoBO.setLabelName( new ArrayList<String>( list2 ) );
        }
        subjectInfoBO.setLastSubjectId( subjectInfoDTO.getLastSubjectId() );
        subjectInfoBO.setLiked( subjectInfoDTO.getLiked() );
        subjectInfoBO.setLikedCount( subjectInfoDTO.getLikedCount() );
        subjectInfoBO.setNextSubjectId( subjectInfoDTO.getNextSubjectId() );
        subjectInfoBO.setOptionList( subjectAnswerDTOListToSubjectAnswerBOList( subjectInfoDTO.getOptionList() ) );
        subjectInfoBO.setSettleName( subjectInfoDTO.getSettleName() );
        subjectInfoBO.setSubjectAnswer( subjectInfoDTO.getSubjectAnswer() );
        subjectInfoBO.setSubjectCount( subjectInfoDTO.getSubjectCount() );
        subjectInfoBO.setSubjectDifficult( subjectInfoDTO.getSubjectDifficult() );
        subjectInfoBO.setSubjectName( subjectInfoDTO.getSubjectName() );
        subjectInfoBO.setSubjectParse( subjectInfoDTO.getSubjectParse() );
        subjectInfoBO.setSubjectScore( subjectInfoDTO.getSubjectScore() );
        subjectInfoBO.setSubjectType( subjectInfoDTO.getSubjectType() );

        return subjectInfoBO;
    }

    @Override
    public SubjectInfoDTO convertBOToDTO(SubjectInfoBO subjectInfoBO) {
        if ( subjectInfoBO == null ) {
            return null;
        }

        SubjectInfoDTO subjectInfoDTO = new SubjectInfoDTO();

        subjectInfoDTO.setPageNo( subjectInfoBO.getPageNo() );
        subjectInfoDTO.setPageSize( subjectInfoBO.getPageSize() );
        subjectInfoDTO.setCategoryId( subjectInfoBO.getCategoryId() );
        List<Integer> list = subjectInfoBO.getCategoryIds();
        if ( list != null ) {
            subjectInfoDTO.setCategoryIds( new ArrayList<Integer>( list ) );
        }
        subjectInfoDTO.setCreateUser( subjectInfoBO.getCreateUser() );
        subjectInfoDTO.setCreateUserAvatar( subjectInfoBO.getCreateUserAvatar() );
        subjectInfoDTO.setId( subjectInfoBO.getId() );
        subjectInfoDTO.setKeyWord( subjectInfoBO.getKeyWord() );
        subjectInfoDTO.setLabelId( subjectInfoBO.getLabelId() );
        List<Integer> list1 = subjectInfoBO.getLabelIds();
        if ( list1 != null ) {
            subjectInfoDTO.setLabelIds( new ArrayList<Integer>( list1 ) );
        }
        List<String> list2 = subjectInfoBO.getLabelName();
        if ( list2 != null ) {
            subjectInfoDTO.setLabelName( new ArrayList<String>( list2 ) );
        }
        subjectInfoDTO.setLastSubjectId( subjectInfoBO.getLastSubjectId() );
        subjectInfoDTO.setLiked( subjectInfoBO.getLiked() );
        subjectInfoDTO.setLikedCount( subjectInfoBO.getLikedCount() );
        subjectInfoDTO.setNextSubjectId( subjectInfoBO.getNextSubjectId() );
        subjectInfoDTO.setOptionList( subjectAnswerBOListToSubjectAnswerDTOList( subjectInfoBO.getOptionList() ) );
        subjectInfoDTO.setSettleName( subjectInfoBO.getSettleName() );
        subjectInfoDTO.setSubjectAnswer( subjectInfoBO.getSubjectAnswer() );
        subjectInfoDTO.setSubjectCount( subjectInfoBO.getSubjectCount() );
        subjectInfoDTO.setSubjectDifficult( subjectInfoBO.getSubjectDifficult() );
        subjectInfoDTO.setSubjectName( subjectInfoBO.getSubjectName() );
        subjectInfoDTO.setSubjectParse( subjectInfoBO.getSubjectParse() );
        subjectInfoDTO.setSubjectScore( subjectInfoBO.getSubjectScore() );
        subjectInfoDTO.setSubjectType( subjectInfoBO.getSubjectType() );

        return subjectInfoDTO;
    }

    @Override
    public List<SubjectInfoDTO> convertBOToDTOList(List<SubjectInfoBO> subjectInfoBO) {
        if ( subjectInfoBO == null ) {
            return null;
        }

        List<SubjectInfoDTO> list = new ArrayList<SubjectInfoDTO>( subjectInfoBO.size() );
        for ( SubjectInfoBO subjectInfoBO1 : subjectInfoBO ) {
            list.add( convertBOToDTO( subjectInfoBO1 ) );
        }

        return list;
    }

    protected SubjectAnswerBO subjectAnswerDTOToSubjectAnswerBO(SubjectAnswerDTO subjectAnswerDTO) {
        if ( subjectAnswerDTO == null ) {
            return null;
        }

        SubjectAnswerBO subjectAnswerBO = new SubjectAnswerBO();

        subjectAnswerBO.setIsCorrect( subjectAnswerDTO.getIsCorrect() );
        subjectAnswerBO.setOptionContent( subjectAnswerDTO.getOptionContent() );
        subjectAnswerBO.setOptionType( subjectAnswerDTO.getOptionType() );

        return subjectAnswerBO;
    }

    protected List<SubjectAnswerBO> subjectAnswerDTOListToSubjectAnswerBOList(List<SubjectAnswerDTO> list) {
        if ( list == null ) {
            return null;
        }

        List<SubjectAnswerBO> list1 = new ArrayList<SubjectAnswerBO>( list.size() );
        for ( SubjectAnswerDTO subjectAnswerDTO : list ) {
            list1.add( subjectAnswerDTOToSubjectAnswerBO( subjectAnswerDTO ) );
        }

        return list1;
    }

    protected SubjectAnswerDTO subjectAnswerBOToSubjectAnswerDTO(SubjectAnswerBO subjectAnswerBO) {
        if ( subjectAnswerBO == null ) {
            return null;
        }

        SubjectAnswerDTO subjectAnswerDTO = new SubjectAnswerDTO();

        subjectAnswerDTO.setIsCorrect( subjectAnswerBO.getIsCorrect() );
        subjectAnswerDTO.setOptionContent( subjectAnswerBO.getOptionContent() );
        subjectAnswerDTO.setOptionType( subjectAnswerBO.getOptionType() );

        return subjectAnswerDTO;
    }

    protected List<SubjectAnswerDTO> subjectAnswerBOListToSubjectAnswerDTOList(List<SubjectAnswerBO> list) {
        if ( list == null ) {
            return null;
        }

        List<SubjectAnswerDTO> list1 = new ArrayList<SubjectAnswerDTO>( list.size() );
        for ( SubjectAnswerBO subjectAnswerBO : list ) {
            list1.add( subjectAnswerBOToSubjectAnswerDTO( subjectAnswerBO ) );
        }

        return list1;
    }
}
