package com.gempellm.survey.models;

import lombok.NonNull;

public class QuestionRequest {

    private @NonNull Long id;
    private String text;
    private String type;
    private String answers;

    public QuestionRequest() {
    }

    public QuestionRequest(Long id, String text, String type) {
        this.id = id;
        this.text = text;
        this.type = type;
    }

    public QuestionRequest(@NonNull Long id, String text, String type, String answers) {
        this.id = id;
        this.text = text;
        this.type = type;
        this.answers = answers;
    }

    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getType() {
        return type;
    }

    public String getAnswers() {
        return answers;
    }
}
