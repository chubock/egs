package com.chubock.assignment.egs.mapper;

import com.chubock.assignment.egs.entity.Rate;
import com.chubock.assignment.egs.model.RateModel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RateMapper extends AbstractMapper<Rate, RateModel> {
    @Override
    Rate toEntity(RateModel model);

    @Override
    @Mapping(target = "product.category", ignore = true)
    @Mapping(target = "user.credentialExpired", ignore = true)
    @Mapping(target = "user.accountLocked", ignore = true)
    @Mapping(target = "user.accountExpired", ignore = true)
    @Mapping(target = "user.enabled", ignore = true)
    RateModel toModel(Rate entity);

    @Mapping(target = "user", ignore = true)
    @Mapping(target = "product.category", ignore = true)
    RateModel toModelWithoutUser(Rate entity);

}
