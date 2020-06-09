package com.dimediary.jpa.repo;

import com.dimediary.domain.Like;
import com.dimediary.port.out.LikeRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeRepo implements LikeRepository {

  private LikeCrudRepo likeCrudRepository;


  private Liketransformator likeTransform;

  public LikeRepo(LikeCrudRepo likeCrudRepository, Liketransformator likesTransform) {
    this.likeCrudRepository = likeCrudRepository;
    this.likeTransform = likesTransform;
  }

  @Override
  public Like store(Like like) {
    return likeTransform.from(likeCrudRepository.save(likeTransform.to(like)));
  }

  @Override
  public Long count() {
    return likeCrudRepository.count();
  }

}
