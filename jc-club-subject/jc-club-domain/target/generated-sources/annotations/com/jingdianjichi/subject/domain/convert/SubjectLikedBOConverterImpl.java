package com.jingdianjichi.subject.domain.convert;

import com.jingdianjichi.subject.domain.entity.SubjectLikedBO;
import com.jingdianjichi.subject.infra.basic.entity.SubjectLiked;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Generated;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-12-26T15:57:01+0800",
    comments = "version: 1.4.2.Final, compiler: Eclipse JDT (IDE) 3.40.0.z20241112-1021, environment: Java 17.0.13 (Eclipse Adoptium)"
)
public class SubjectLikedBOConverterImpl implements SubjectLikedBOConverter {

    @Override
    public SubjectLiked convertBOToEntity(SubjectLikedBO subjectLikedBO) {
        if ( subjectLikedBO == null ) {
            return null;
        }

        SubjectLiked subjectLiked = new SubjectLiked();

        subjectLiked.setCreatedBy( subjectLikedBO.getCreatedBy() );
        subjectLiked.setCreatedTime( subjectLikedBO.getCreatedTime() );
        subjectLiked.setId( subjectLikedBO.getId() );
        subjectLiked.setIsDeleted( subjectLikedBO.getIsDeleted() );
        subjectLiked.setLikeUserId( subjectLikedBO.getLikeUserId() );
        subjectLiked.setStatus( subjectLikedBO.getStatus() );
        subjectLiked.setSubjectId( subjectLikedBO.getSubjectId() );
        subjectLiked.setUpdateBy( subjectLikedBO.getUpdateBy() );
        subjectLiked.setUpdateTime( subjectLikedBO.getUpdateTime() );

        return subjectLiked;
    }

    @Override
    public List<SubjectLikedBO> convertListInfoToBO(List<SubjectLiked> subjectLikedList) {
        if ( subjectLikedList == null ) {
            return null;
        }

        List<SubjectLikedBO> list = new ArrayList<SubjectLikedBO>( subjectLikedList.size() );
        for ( SubjectLiked subjectLiked : subjectLikedList ) {
            list.add( subjectLikedToSubjectLikedBO( subjectLiked ) );
        }

        return list;
    }

    protected SubjectLikedBO subjectLikedToSubjectLikedBO(SubjectLiked subjectLiked) {
        if ( subjectLiked == null ) {
            return null;
        }

        SubjectLikedBO subjectLikedBO = new SubjectLikedBO();

        subjectLikedBO.setCreatedBy( subjectLiked.getCreatedBy() );
        subjectLikedBO.setCreatedTime( subjectLiked.getCreatedTime() );
        subjectLikedBO.setId( subjectLiked.getId() );
        subjectLikedBO.setIsDeleted( subjectLiked.getIsDeleted() );
        subjectLikedBO.setLikeUserId( subjectLiked.getLikeUserId() );
        subjectLikedBO.setStatus( subjectLiked.getStatus() );
        subjectLikedBO.setSubjectId( subjectLiked.getSubjectId() );
        subjectLikedBO.setUpdateBy( subjectLiked.getUpdateBy() );
        subjectLikedBO.setUpdateTime( subjectLiked.getUpdateTime() );

        return subjectLikedBO;
    }
}
