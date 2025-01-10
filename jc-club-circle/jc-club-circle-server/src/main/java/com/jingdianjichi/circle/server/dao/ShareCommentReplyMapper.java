package com.jingdianjichi.circle.server.dao;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jingdianjichi.circle.server.entity.po.ShareCommentReply;
import org.apache.ibatis.annotations.Param;

public interface ShareCommentReplyMapper extends BaseMapper<ShareCommentReply> {
    ShareCommentReply selectById(@Param("id") Long id);
}
