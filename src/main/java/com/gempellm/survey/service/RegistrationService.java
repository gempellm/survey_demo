package com.gempellm.survey.service;

import com.gempellm.survey.models.CredentialsRequest;
import com.gempellm.survey.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String register(CredentialsRequest credentialsRequest) {
        User user = new User();
        String login = credentialsRequest.getLogin();
        if (userRepository.findByLogin(login).isPresent()) {
            return "Username already exists";
        }
        user.setLogin(login);
        user.setPassword(passwordEncoder.encode(credentialsRequest.getPassword()));
        user.setRoles(credentialsRequest.getRoles());
        userRepository.save(user);
        return "Successfully registered";
    }
}
