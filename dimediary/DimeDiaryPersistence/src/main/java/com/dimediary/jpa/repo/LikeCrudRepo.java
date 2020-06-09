package com.dimediary.jpa.repo;

import com.dimediary.jpa.entities.LikeEntity;
import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

public interface LikeCrudRepo extends CrudRepository<LikeEntity, UUID> {

}
