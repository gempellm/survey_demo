package com.gempellm.survey.controller;

import com.gempellm.survey.models.*;
import com.gempellm.survey.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/")
public class APIController {

    @Autowired
    private RegistrationService registrationService;

    @Autowired
    private SurveyService surveyService;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;

    @GetMapping("")
    public ResponseEntity<?> home() {
        return ResponseEntity.ok("<h1>Home Page</h1>");
    }

    @GetMapping("/user")
    public ResponseEntity<?> user() {
        return ResponseEntity.ok("<h1>Welcome User</h1>");
    }

    @GetMapping("/admin")
    public ResponseEntity<?> admin() {
        return ResponseEntity.ok("<h1>Welcome Admin</h1>");
    }

    @PostMapping("/user/register")
    public ResponseEntity<?> register(@RequestBody CredentialsRequest credentialsRequest) {
        return ResponseEntity.ok(registrationService.register(credentialsRequest));
    }

    @PostMapping("/user/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthenticationRequest authenticationRequest) {
        return authenticationService.authenticate(authenticationRequest);
    }

    @PostMapping("/survey/create")
    public ResponseEntity<?> createSurvey(@RequestBody NameRequest nameRequest) {
        return ResponseEntity.ok(surveyService.create(nameRequest).toString());
    }

    @GetMapping("/survey/fetch")
    public ResponseEntity<?> fetchSurveys() {
        return ResponseEntity.ok(surveyService.fetchSurveys());
    }

    @GetMapping("/survey/fetch-active")
    public ResponseEntity<?> fetchActiveSurveys() {
        return ResponseEntity.ok(surveyService.fetchActiveSurveys());
    }

    @PutMapping("/survey/modify")
    public ResponseEntity<?> modifySurvey(@RequestBody SurveyRequest surveyRequest) {
        return ResponseEntity.ok(surveyService.modifySurvey(surveyRequest));
    }

    @PutMapping("/survey/start")
    public ResponseEntity<?> startSurvey(@RequestBody IdRequest idRequest) {
        return ResponseEntity.ok(surveyService.startSurvey(idRequest));
    }

    @PutMapping("/survey/end")
    public ResponseEntity<?> endSurvey(@RequestBody IdRequest idRequest) {
        return ResponseEntity.ok(surveyService.endSurvey(idRequest));
    }

    @DeleteMapping("/survey/delete")
    public ResponseEntity<?> deleteSurvey(@RequestBody IdRequest idRequest) {
        return ResponseEntity.ok(surveyService.deleteSurvey(idRequest));
    }

    @GetMapping("/question/fetch")
    public ResponseEntity<?> fetchQuestions() {
        return  ResponseEntity.ok(questionService.fetchQuestions());
    }

    @PostMapping("/question/fetch-by-id")
    public ResponseEntity<?> fetchQuestionsById(@RequestBody IdRequest idRequest) {
        return ResponseEntity.ok(questionService.fetchQuestionsBySurvey(idRequest));
    }

    @PostMapping("/question/create")
    public ResponseEntity<?> createQuestion(@RequestBody QuestionRequest questionRequest) {
        return ResponseEntity.ok(questionService.create(questionRequest));
    }

    @PutMapping("/question/modify")
    public ResponseEntity<?> modifyQuestion(@RequestBody QuestionRequest questionRequest) {
        return ResponseEntity.ok(questionService.modify(questionRequest));
    }

    @DeleteMapping("/question/delete")
    public ResponseEntity<?> deleteQuestion(@RequestBody IdRequest idRequest) {
        return ResponseEntity.ok(questionService.delete(idRequest));
    }

    @PostMapping("/answer/create")
    public ResponseEntity<?> createAnswer(HttpServletRequest request) {
        return answerService.create(request);
    }

    @GetMapping("/answer/complete")
    public  ResponseEntity<?> fetchCompleteSurveys(HttpServletRequest request) {
        return answerService.fetchCompleteSurveys(request);
    }
}
