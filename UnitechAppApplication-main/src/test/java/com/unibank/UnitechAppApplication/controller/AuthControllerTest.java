package com.unibank.UnitechAppApplication.controller;

import com.unibank.UnitechAppApplication.model.dto.request.AuthorRequest;
import com.unibank.UnitechAppApplication.model.dto.response.LoginSuccessResponse;
import com.unibank.UnitechAppApplication.model.dto.response.RegisterSuccessResponse;
import com.unibank.UnitechAppApplication.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {
    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    void testRegisterUser_SuccessfulRegistration() throws Exception {
        AuthorRequest authorRequest = new AuthorRequest();

        RegisterSuccessResponse expectedResponse = new RegisterSuccessResponse("Registered");
        when(authService.registerUser(authorRequest)).thenReturn(expectedResponse);

        ResponseEntity<RegisterSuccessResponse> response = authController.registerUser(authorRequest);

        assertSame(expectedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(authService, times(1)).registerUser(authorRequest);
    }

    @Test
    void testLogin_SuccessfulLogin() {
        AuthorRequest authorRequest = new AuthorRequest();

        LoginSuccessResponse expectedResponse = new LoginSuccessResponse();
        when(authService.loginUser(authorRequest)).thenReturn(expectedResponse);

        ResponseEntity<LoginSuccessResponse> response = authController.login(authorRequest);

        assertSame(expectedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(authService, times(1)).loginUser(authorRequest);
    }
}
