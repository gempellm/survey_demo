package com.gempellm.survey.models;

public class AnswerRequest {
    private Long surveyId;
    private Long questionId;
    private String answer;

    public AnswerRequest() {
    }

    public AnswerRequest(Long surveyId, Long questionId, String answer) {
        this.surveyId = surveyId;
        this.questionId = questionId;
        this.answer = answer;
    }

    public Long getSurveyId() {
        return surveyId;
    }

    public Long getQuestionId() {
        return questionId;
    }

    public String getAnswer() {
        return answer;
    }
}
