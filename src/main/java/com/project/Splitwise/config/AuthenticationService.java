package com.project.Splitwise.config;

import com.project.Splitwise.model.AuthenticationResponse;
import com.project.Splitwise.model.Role;
import com.project.Splitwise.model.User;
import com.project.Splitwise.repositroy.UserRepository;
import com.project.Splitwise.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;
    public AuthenticationResponse register(User request) {
        User savedUser=userRepository.findByEmail(request.getEmail());
        if (savedUser == null) {
            var user=User.builder()
                    .name(request.getName())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .phoneNumber(request.getPhoneNumber())
                    .role(Role.USER)
                    .build();
            userRepository.save(user);
            String body="Hi "+user.getName()+",\n\nWelcome to Splitcoin! Manage shared expenses effortlessly with friends, roommates, or partners. Organize, track, and settle up with ease. Let's get started!\n\n" +
                    "Thank you,\n" +
                    "SplitCoin Team";
            emailService.sendEmail(user.getEmail(), "Welcome to Splitcoin",body);
            var jwtToken=jwtService.generateToken(user);
            return AuthenticationResponse.builder().token(jwtToken).build();
        }
        else if(savedUser.getPassword()==null){
            savedUser.setName(request.getName());
            savedUser.setPassword(passwordEncoder.encode(request.getPassword()));
            savedUser.setPhoneNumber(request.getPhoneNumber());
            userRepository.save(savedUser);
            var jwtToken=jwtService.generateToken(savedUser);
            return AuthenticationResponse.builder().token(jwtToken).build();
        }
        return AuthenticationResponse.builder().build();
    }

    public AuthenticationResponse authenticate(User request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(),request.getPassword()));
        var user =userRepository.findByEmail(request.getEmail());
        var jwtToken=jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();

    }
}
