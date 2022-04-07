package com.gempellm.survey.service;

import com.gempellm.survey.models.*;
import com.gempellm.survey.util.AccessTokenUtil;
import com.google.gson.Gson;
import com.sun.net.httpserver.HttpsServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AnswerService {

    @Autowired
    private AccessTokenUtil accessTokenUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    private Long extractUserId(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null) {
            return null;
        }

        String jwt, login;
        Long userId = null;

        try {
            if (authorizationHeader.startsWith("Bearer ")) {
                jwt = authorizationHeader.substring(7);
                login = accessTokenUtil.extractLogin(jwt);
                Optional<User> userOptional = userRepository.findByLogin(login);
                if (userOptional.isEmpty()) {
                    return null;
                }
                User user = userOptional.get();
                userId = user.getId();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return userId;
    }


    public ResponseEntity<?> create(HttpServletRequest request) {


        String login, jwt;
        Long userId = extractUserId(request);
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        AnswerRequest answerRequest = null;

        try {
            String rawJson = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
            answerRequest = new Gson().fromJson(rawJson, AnswerRequest.class);
        } catch (IOException e) {
            e.printStackTrace();
        }



        Answer answer = new Answer();

        Long surveyId = answerRequest.getSurveyId();
        Optional<Survey> surveyOptional = surveyRepository.findById(surveyId);
        if (surveyOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }

        if (surveyOptional.get().getStart() == null || surveyOptional.get().getEnd() != null) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        Long questionId = answerRequest.getQuestionId();
        Optional<Answer> answerOptional = answerRepository.findByUserIdAndQuestionIdAndSurveyId(userId, questionId, surveyId);

        if (answerOptional.isPresent()) {
            answer = answerOptional.get();
        }

        Optional<Question> questionOptional = questionRepository.findById(questionId);
        if (questionOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.EXPECTATION_FAILED);
        }
        Question question = questionOptional.get();
        String questionType = question.getType();
        String answerContent = answerRequest.getAnswer();
        if (questionType.equals("single")) {
            if (answerContent.contains(",")) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
            String questionAnswers = question.getAnswers();
            String[] possibleAnswers = questionAnswers.split(",");
            if (!Arrays.asList(possibleAnswers).contains(answerContent)) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }

        if (questionType.equals("multiple")) {
            String[] userAnswers = answerContent.split(",");
            String[] questionAnswers = question.getAnswers().split(",");
            if (userAnswers.length > questionAnswers.length) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }

            if (!Arrays.asList(questionAnswers).containsAll(Arrays.asList(userAnswers))) {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        }

        answer.setUserId(userId);
        answer.setSurveyId(surveyId);
        answer.setQuestionId(questionId);
        answer.setAnswer(answerRequest.getAnswer());
        answerRepository.save(answer);
        return ResponseEntity.ok(answer);
    }

    public ResponseEntity<?> fetchCompleteSurveys(HttpServletRequest request) {
        Long userId = extractUserId(request);
        if (userId == null) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        List<Answer> answerList = answerRepository.findAllByUserId(userId);
        HashMap<Long, List<Answer>> distinctAnswers = new HashMap<>();
        for (Answer answer : answerList) {
            Long surveyId = answer.getSurveyId();
            List<Answer> answers = new ArrayList<>();
            if (distinctAnswers.containsKey(surveyId)) {
                answers = distinctAnswers.get(surveyId);
                answers.add(answer);
                distinctAnswers.put(surveyId, answers);
            } else {
                answers.add(answer);
                distinctAnswers.put(surveyId, answers);
            }
        }

        HashMap<Long, List<Answer>> result = new HashMap<>();

        for (Map.Entry<Long, List<Answer>> entry : distinctAnswers.entrySet()) {
            Long surveyId = entry.getKey();
            if (questionRepository.findAllBySurveyId(surveyId).size() == entry.getValue().size()) {
                result.put(surveyId, entry.getValue());
            }
        }

        return ResponseEntity.ok(result);
    }
}
