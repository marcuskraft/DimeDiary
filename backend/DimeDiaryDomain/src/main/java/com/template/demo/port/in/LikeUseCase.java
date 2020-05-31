package com.template.demo.port.in;

import com.template.demo.domain.Like;

public interface LikeUseCase {
	
    Like createLike();

    Long getLikeNumbers();

}
