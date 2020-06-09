package com.dimediary.jpa.repo;

import com.dimediary.domain.Like;
import com.dimediary.jpa.entities.LikeEntity;
import org.springframework.stereotype.Service;

@Service
public class Liketransformator {

  public Like from(LikeEntity jpaLikes) {
    final Like likes = new Like();
    likes.setId(jpaLikes.getId());
    return likes;
  }

  public LikeEntity to(Like likes) {
    LikeEntity jpaLikes = new LikeEntity();
    jpaLikes.setId(likes.getId());
    return jpaLikes;
  }

}
