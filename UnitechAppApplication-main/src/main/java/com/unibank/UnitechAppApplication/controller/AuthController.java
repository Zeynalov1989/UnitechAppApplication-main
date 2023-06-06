package com.unibank.UnitechAppApplication.controller;

import com.unibank.UnitechAppApplication.model.dto.request.AuthorRequest;
import com.unibank.UnitechAppApplication.model.dto.response.LoginSuccessResponse;
import com.unibank.UnitechAppApplication.model.dto.response.RegisterSuccessResponse;
import com.unibank.UnitechAppApplication.service.AuthService;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/author")
@Api( tags = "Auth")
public class AuthController {
	private final AuthService authService;
	
	@PostMapping("/register")
    public ResponseEntity<RegisterSuccessResponse> registerUser(@RequestBody AuthorRequest authorRequest) throws Exception {
        return ResponseEntity.ok(authService.registerUser(authorRequest));
    }


    @PostMapping("/login")
    public ResponseEntity<LoginSuccessResponse> login(@RequestBody AuthorRequest authorRequest) {
        return ResponseEntity.ok(authService.loginUser(authorRequest));
    }
}
