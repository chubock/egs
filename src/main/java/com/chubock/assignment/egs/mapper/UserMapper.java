package com.chubock.assignment.egs.mapper;

import com.chubock.assignment.egs.entity.User;
import com.chubock.assignment.egs.model.UserModel;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper extends AbstractMapper<User, UserModel> {

    @Override
    UserModel toModel(User entity);

}
