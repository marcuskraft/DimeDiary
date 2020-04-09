package com.template.demo.jpa.repo;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.template.demo.jpa.entities.LikeEntity;

public interface LikeCrudRepo extends CrudRepository<LikeEntity, UUID> {

}
