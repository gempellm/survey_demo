package com.gempellm.survey.service;

import com.gempellm.survey.models.SurveyUserDetails;
import com.gempellm.survey.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SurveyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByLogin(username);

        user.orElseThrow(() -> new UsernameNotFoundException("Not found " + username));

        return user.map(SurveyUserDetails::new).get();
    }
}
