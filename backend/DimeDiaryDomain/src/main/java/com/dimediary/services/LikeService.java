package com.dimediary.services;

import org.springframework.stereotype.Service;

import com.dimediary.domain.Like;
import com.dimediary.port.in.LikeUseCase;
import com.dimediary.port.out.LikeRepository;

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
