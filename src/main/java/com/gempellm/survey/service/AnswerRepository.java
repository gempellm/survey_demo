package com.gempellm.survey.service;

import com.gempellm.survey.models.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
    Optional<Answer> findById(Long id);
    Optional<Answer> findByUserIdAndQuestionIdAndSurveyId(Long userId, Long questionId, Long surveyId);
    List<Answer> findAllByUserId(Long userId);
    List<Answer> findAllBySurveyIdAndUserId(Long surveyId, Long userId);
}
