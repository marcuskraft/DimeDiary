package com.template.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.template.api.LikeApi;
import com.template.demo.port.in.LikeUseCase;
import com.template.model.Like;

import io.swagger.annotations.ApiOperation;

@RequestMapping("/api")
@Controller
public class LikeController implements LikeApi {

	private final LikeUseCase likeUseCase;

	@Autowired
	LikeController(LikeUseCase likeUseCase) {
		this.likeUseCase = likeUseCase;

	}

	@Override
	@GetMapping("/like/count")
	@ApiOperation("Gets Likes number from backend")
	public ResponseEntity<Object> likeCountGet() {
		try {
			return new ResponseEntity<>(this.likeUseCase.getLikeNumbers(), HttpStatus.OK);
		} catch (final Exception e) {
			return new ResponseEntity<Object>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Override
	@PostMapping("/like")
	@ApiOperation("Persists Likes in the backend")
	public ResponseEntity<Like> likePost() {
		final Like like = LikeTransform.INSTANCE.likeDomainToLikeModel(this.likeUseCase.createLike());
		return ResponseEntity.status(HttpStatus.CREATED).body(like);
	}

}
