package com.template.demo.rest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.template.model.Like;

@Mapper
public interface LikeTransform {
	
	LikeTransform INSTANCE = Mappers.getMapper(LikeTransform.class);

    @Mapping(target = "uuid", source = "like.id")
    Like likeDomainToLikeModel(com.template.demo.domain.Like like);

}
