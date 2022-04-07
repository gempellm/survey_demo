package com.gempellm.survey.models;

import lombok.NonNull;

public class IdRequest {
    private @NonNull Long id;

    public IdRequest() {
    }

    public IdRequest(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
