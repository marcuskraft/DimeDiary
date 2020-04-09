package com.template.demo.services;

import org.springframework.stereotype.Service;

import com.template.demo.domain.Like;
import com.template.demo.port.in.LikeUseCase;
import com.template.demo.port.out.LikeRepository;

@Service
public class LikeService implements LikeUseCase {

	private final LikeRepository likeRepository;

	public LikeService(LikeRepository likeRepository) {
		super();
		this.likeRepository = likeRepository;
	}

	@Override
	public Like createLike() {
		final Like like = new Like();
		return this.likeRepository.store(like);
	}

	@Override
	public Long getLikeNumbers() {
		return this.likeRepository.count();
	}

}
