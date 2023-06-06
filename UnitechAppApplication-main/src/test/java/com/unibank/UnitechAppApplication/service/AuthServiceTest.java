package com.unibank.UnitechAppApplication.service;

import com.unibank.UnitechAppApplication.config.jwt.JwtTokenProvider;
import com.unibank.UnitechAppApplication.model.User;
import com.unibank.UnitechAppApplication.repository.UserRepository;
import com.unibank.UnitechAppApplication.model.dto.request.AuthorRequest;
import com.unibank.UnitechAppApplication.model.dto.response.LoginSuccessResponse;
import com.unibank.UnitechAppApplication.model.dto.response.RegisterSuccessResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private UserDetailsServiceImpl userService;

    @InjectMocks
    private AuthService authService;

    @Test
    void testRegisterUser_SuccessfulRegistration() throws Exception {
        AuthorRequest authorRequest = new AuthorRequest();
        authorRequest.setPin("1234");
        authorRequest.setPassword("password");

        when(userRepository.findByPin("1234")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        RegisterSuccessResponse response = authService.registerUser(authorRequest);

        assertEquals("Successfully Registered", response.getSuccess());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_UserAlreadyExists() {
        AuthorRequest authorRequest = new AuthorRequest();
        authorRequest.setPin("1234");
        authorRequest.setPassword("password");

        when(userRepository.findByPin("1234")).thenReturn(Optional.of(new User()));

        assertThrows(Exception.class,
            () -> authService.registerUser(authorRequest),
            "User with the provided PIN already exists"
        );
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void testLoginUser_SuccessfulLogin() {
        AuthorRequest authorRequest = new AuthorRequest();
        authorRequest.setPin("1234");
        authorRequest.setPassword("password");

        when(userService.loadUserByUsername("1234")).thenReturn(mock(UserDetails.class));
        when(jwtTokenProvider.generateToken(any(UserDetails.class))).thenReturn("sampleToken");

        LoginSuccessResponse response = authService.loginUser(authorRequest);

        assertEquals("Successfully logged in", response.getSuccess());
        assertEquals("sampleToken", response.getToken());
        verify(authenticationManager, times(1)).authenticate(
            new UsernamePasswordAuthenticationToken("1234", "password")
        );
    }
}