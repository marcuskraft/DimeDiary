package com.template.demo.domain;

import java.util.UUID;

public class Like {

	private UUID id;

    public Like() {
        this.id = UUID.randomUUID();
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }
	
}
