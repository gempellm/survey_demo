package com.gempellm.survey.models;

import lombok.NonNull;

public class SurveyRequest {
    private @NonNull Long id;
    private String name;
    private String description;

    public SurveyRequest() {
    }

    public SurveyRequest(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}
