package com.dimediary.port.in;

import com.dimediary.domain.Like;

public interface LikeUseCase {
	
    Like createLike();

    Long getLikeNumbers();

}
