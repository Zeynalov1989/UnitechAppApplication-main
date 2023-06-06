package com.unibank.UnitechAppApplication.service;

import com.unibank.UnitechAppApplication.config.jwt.JwtTokenProvider;
import com.unibank.UnitechAppApplication.model.User;
import com.unibank.UnitechAppApplication.repository.UserRepository;
import com.unibank.UnitechAppApplication.model.dto.request.AuthorRequest;
import com.unibank.UnitechAppApplication.model.dto.response.LoginSuccessResponse;
import com.unibank.UnitechAppApplication.model.dto.response.RegisterSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserDetailsServiceImpl userService;


    public RegisterSuccessResponse registerUser(AuthorRequest authorRequest) throws Exception {
        // Check if a user with the given PIN already exists
        if (userRepository.findByPin(authorRequest.getPin()).isPresent()) {
            throw new BadRequestException("User with the provided PIN already exists");
        }

        // Create a new User entity
        User user = User.builder()
            .pin(authorRequest.getPin())
            .password(passwordEncoder.encode(authorRequest.getPassword()))
            .build();

        // Save the user in the repository
        userRepository.save(user);
        return new RegisterSuccessResponse("Successfully Registered");
    }

    public LoginSuccessResponse loginUser(AuthorRequest authorRequest) {
        String pin = authorRequest.getPin();
        String password = authorRequest.getPassword();
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(pin, password));
        UserDetails userDetails = userService.loadUserByUsername(pin);
        String token = jwtTokenProvider.generateToken(userDetails);
        return new LoginSuccessResponse("Successfully logged in", token);
    }
}
