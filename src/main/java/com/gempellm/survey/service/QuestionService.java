package com.gempellm.survey.service;

import com.gempellm.survey.models.IdRequest;
import com.gempellm.survey.models.Question;
import com.gempellm.survey.models.QuestionRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    public List<Question> fetchQuestions() {
        return questionRepository.findAll();
    }

    public List<Question> fetchQuestionsBySurvey(IdRequest idRequest) {
        Long surveyId = idRequest.getId();
        List<Question> questions = questionRepository.findAllBySurveyId(surveyId);
        return questions;
    }

    public Question create(QuestionRequest questionRequest) {
        Long surveyId = questionRequest.getId();
        if (surveyRepository.findById(surveyId).isEmpty()) {
            return null;
        }

        Question question = new Question();
        question.setSurveyId(surveyId);
        question.setText(questionRequest.getText());
        question.setType(questionRequest.getType());
        if (questionRequest.getAnswers() != null) {
            question.setAnswers(questionRequest.getAnswers());
        }
        questionRepository.save(question);
        return question;
    }

    public Question modify(QuestionRequest questionRequest) {
        Long questionId = questionRequest.getId();
        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (questionOptional.isEmpty()) {
            return null;
        }

        Question question = questionOptional.get();
        question.setText(questionRequest.getText());
        question.setType(questionRequest.getType());
        if (questionRequest.getAnswers() != null) {
            question.setAnswers(questionRequest.getAnswers());
        }
        questionRepository.save(question);
        return question;
    }

    public List<Question> delete(IdRequest idRequest) {
        Long id = idRequest.getId();
        Optional<Question> questionOptional = questionRepository.findById(id);
        if (questionOptional.isEmpty()) {
            return null;
        }
        Question question = questionOptional.get();
        long surveyId = question.getSurveyId();
        questionRepository.delete(question);
        List<Question> questions = questionRepository.findAllBySurveyId(surveyId);
        return questions;
    }

}
