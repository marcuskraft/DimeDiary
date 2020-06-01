package com.dimediary.port.out;

import com.dimediary.domain.Like;

public interface LikeRepository {
	
    Like store(Like likes);

    Long count();


}
