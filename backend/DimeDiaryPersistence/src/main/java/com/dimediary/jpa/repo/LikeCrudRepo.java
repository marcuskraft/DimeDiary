package com.dimediary.jpa.repo;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.dimediary.jpa.entities.LikeEntity;

public interface LikeCrudRepo extends CrudRepository<LikeEntity, UUID> {

}
