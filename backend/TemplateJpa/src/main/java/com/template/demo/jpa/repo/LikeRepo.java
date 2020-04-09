package com.template.demo.jpa.repo;

import org.springframework.stereotype.Service;

import com.template.demo.domain.Like;
import com.template.demo.port.out.LikeRepository;

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
