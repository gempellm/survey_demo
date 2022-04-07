package com.gempellm.survey.service;

import com.gempellm.survey.models.AuthenticationRequest;
import com.gempellm.survey.models.User;
import com.gempellm.survey.util.AccessTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private SurveyUserDetailsService userDetailsService;

    @Autowired
    private AccessTokenUtil accessTokenUtil;

    public ResponseEntity<?> authenticate(AuthenticationRequest authenticationRequest) {
        String login = authenticationRequest.getLogin();
        Optional<User> userOptional = userRepository.findByLogin(login);
        if (userOptional.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        User user = userOptional.get();
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(login, authenticationRequest.getPassword())
            );
        } catch (AuthenticationException ex) {
            ex.printStackTrace();
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(login);
        final String accessToken = accessTokenUtil.generateToken(userDetails, 10);

        return ResponseEntity.ok(accessToken);
    }
}
