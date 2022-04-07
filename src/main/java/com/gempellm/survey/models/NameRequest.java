package com.gempellm.survey.models;

import lombok.NonNull;

public class NameRequest {
    private @NonNull String name;

    public NameRequest() {
    }

    public NameRequest(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
