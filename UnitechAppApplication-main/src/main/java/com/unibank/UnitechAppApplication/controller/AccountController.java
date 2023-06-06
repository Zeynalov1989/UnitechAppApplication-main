package com.unibank.UnitechAppApplication.controller;

import com.unibank.UnitechAppApplication.config.jwt.JwtTokenProvider;
import com.unibank.UnitechAppApplication.exception.InvalidJwtAuthenticationException;
import com.unibank.UnitechAppApplication.model.dto.request.TransferRequest;
import com.unibank.UnitechAppApplication.model.dto.response.AccountResponse;
import com.unibank.UnitechAppApplication.service.AccountService;
import io.swagger.annotations.Api;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
@Api( tags = "Account")
public class AccountController {
	private final AccountService accountService;
    private final JwtTokenProvider jwtTokenProvider;
	
	@GetMapping("/all")
    public ResponseEntity<List<AccountResponse>> getAllActiveAccounts(HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) {
            throw new InvalidJwtAuthenticationException("User not authenticated");
        }
        return ResponseEntity.ok(accountService.getAllActiveAccounts(token));
    }

    @PostMapping("/transfer")
    public ResponseEntity<?> transferAmount(HttpServletRequest request,  @RequestBody TransferRequest transferRequest) {
        String token = jwtTokenProvider.resolveToken(request);
        if (token == null) {
            throw new InvalidJwtAuthenticationException("User not authenticated");
        }
        return ResponseEntity.ok(accountService.transferAmountToAccount(token, transferRequest));
    }
}
