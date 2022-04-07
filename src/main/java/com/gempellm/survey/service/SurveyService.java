package com.gempellm.survey.service;

import com.gempellm.survey.models.IdRequest;
import com.gempellm.survey.models.NameRequest;
import com.gempellm.survey.models.Survey;
import com.gempellm.survey.models.SurveyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SurveyService {

    @Autowired
    private SurveyRepository surveyRepository;

    private Survey get(Long id) {
        Optional<Survey> surveyOptional = surveyRepository.findById(id);
        if (surveyOptional.isEmpty()) {
            return null;
        }

        Survey survey = surveyOptional.get();
        return survey;
    }

    public Survey create(NameRequest nameRequest) {
        String name = nameRequest.getName();
        if (name == null) {
            return null;
        }

        Survey survey = new Survey();
        survey.setName(name);
        surveyRepository.save(survey);
        return survey;
    }

    public List<Survey> fetchActiveSurveys() {
        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();*/
        List<Survey> surveys = surveyRepository.findByStartIsNotNullAndEndIsNull();
        return surveys;
    }

    public List<Survey> fetchSurveys() {
        List<Survey> surveys = surveyRepository.findAll();
        return surveys;
    }

    public Survey modifySurvey(SurveyRequest surveyRequest) {
        Long id = surveyRequest.getId();
        Survey survey = get(id);
        survey.setId(id);
        survey.setName(surveyRequest.getName());
        survey.setDescription(surveyRequest.getDescription());

        surveyRepository.save(survey);
        return survey;
    }

    public Survey startSurvey(IdRequest idRequest) {
        Long id = idRequest.getId();
        Survey survey = get(id);
        if (survey.getStart() == null) {
            Long date = new Date().getTime();
            survey.setStart(date);
            surveyRepository.save(survey);
        }
        return survey;
    }

    public Survey endSurvey (IdRequest idRequest) {
        Long id = idRequest.getId();
        Survey survey = get(id);
        if (survey.getEnd() == null && survey.getStart() != null) {
            Long date = new Date().getTime();
            survey.setEnd(date);
            surveyRepository.save(survey);
        }
        return survey;
    }

    public List<Survey> deleteSurvey(IdRequest idRequest) {
        Long id = idRequest.getId();
        Survey survey = get(id);
        if (survey != null) {
            surveyRepository.delete(survey);
        }
        return fetchSurveys();
    }
}
