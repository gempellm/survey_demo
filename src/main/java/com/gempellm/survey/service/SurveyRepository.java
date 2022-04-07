package com.gempellm.survey.service;

import com.gempellm.survey.models.Survey;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Optional<Survey> findById(Long id);
    List<Survey> findByStartIsNotNullAndEndIsNull();
}
