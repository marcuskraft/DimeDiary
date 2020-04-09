package com.template.demo.port.out;

import com.template.demo.domain.Like;

public interface LikeRepository {
	
    Like store(Like likes);

    Long count();


}
