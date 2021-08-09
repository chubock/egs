package com.chubock.assignment.egs.mapper;

import com.chubock.assignment.egs.entity.Comment;
import com.chubock.assignment.egs.model.CommentModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper extends AbstractMapper<Comment, CommentModel> {

    @Override
    @Mapping(target = "product", ignore = true)
    @Mapping(target = "user.role", ignore = true)
    @Mapping(target = "user.credentialExpired", ignore = true)
    @Mapping(target = "user.accountLocked", ignore = true)
    @Mapping(target = "user.accountExpired", ignore = true)
    @Mapping(target = "user.enabled", ignore = true)
    CommentModel toModel(Comment entity);

}
