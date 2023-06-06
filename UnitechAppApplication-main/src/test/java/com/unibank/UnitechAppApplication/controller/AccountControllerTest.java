package com.unibank.UnitechAppApplication.controller;

import com.unibank.UnitechAppApplication.config.jwt.JwtTokenProvider;
import com.unibank.UnitechAppApplication.exception.InvalidJwtAuthenticationException;
import com.unibank.UnitechAppApplication.model.dto.request.TransferRequest;
import com.unibank.UnitechAppApplication.model.dto.response.AccountResponse;
import com.unibank.UnitechAppApplication.model.dto.response.TransferResponse;
import com.unibank.UnitechAppApplication.service.AccountService;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
class AccountControllerTest {
    @Mock
    private AccountService accountService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private HttpServletRequest request;

    @InjectMocks
    private AccountController accountController;

    @Test
    void testGetAllActiveAccounts_ValidToken() {
        List<AccountResponse> expectedResponse = new ArrayList<>();
        when(jwtTokenProvider.resolveToken(request)).thenReturn("sampleToken");
        when(accountService.getAllActiveAccounts("sampleToken")).thenReturn(expectedResponse);

        ResponseEntity<List<AccountResponse>> response = accountController.getAllActiveAccounts(request);

        assertSame(expectedResponse, response.getBody());
        assertEquals(200, response.getStatusCodeValue());
        verify(jwtTokenProvider, times(1)).resolveToken(request);
        verify(accountService, times(1)).getAllActiveAccounts("sampleToken");
    }

    @Test
    void testGetAllActiveAccounts_InvalidToken() {
        when(jwtTokenProvider.resolveToken(request)).thenReturn(null);

        assertThrows(InvalidJwtAuthenticationException.class,
            () -> accountController.getAllActiveAccounts(request),
            "User not authenticated"
        );
        verify(jwtTokenProvider, times(1)).resolveToken(request);
        verify(accountService, never()).getAllActiveAccounts(any());
    }

    @Test
    void testTransferAmount_ValidTokenAndRequest() {
        TransferRequest transferRequest = new TransferRequest();
        when(jwtTokenProvider.resolveToken(request)).thenReturn("sampleToken");
        when(accountService.transferAmountToAccount("sampleToken", transferRequest))
            .thenReturn(new TransferResponse());

        ResponseEntity<?> response = accountController.transferAmount(request, transferRequest);

        assertEquals(200, response.getStatusCodeValue());
        verify(jwtTokenProvider, times(1)).resolveToken(request);
        verify(accountService, times(1)).transferAmountToAccount("sampleToken", transferRequest);
    }

    @Test
    void testTransferAmount_InvalidToken() {
        TransferRequest transferRequest = new TransferRequest();
        when(jwtTokenProvider.resolveToken(request)).thenReturn(null);

        assertThrows(InvalidJwtAuthenticationException.class,
            () -> accountController.transferAmount(request, transferRequest),
            "User not authenticated"
        );
        verify(jwtTokenProvider, times(1)).resolveToken(request);
        verify(accountService, never()).transferAmountToAccount(any(), any());
    }
}
