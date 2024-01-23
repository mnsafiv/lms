package ru.safonoviv.lms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.safonoviv.lms.dto.JwtRequest;
import ru.safonoviv.lms.dto.RegistrationUserDto;
import ru.safonoviv.lms.service.AuthService;

@RestController
@RequiredArgsConstructor
@Tag(name = "auth")
public class AuthController {
	private final AuthService authService;

	@Operation(description = "Sign in service", responses = {
			@ApiResponse(responseCode = "200", ref = "successResponse"),
			@ApiResponse(responseCode = "401", ref = "unauthorized") })
	@PostMapping("/auth")
	public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
		return authService.createAuthToken(authRequest);
	}

	@Operation(description = "Register in service", responses = {
			@ApiResponse(responseCode = "200", ref = "successResponse"),
			@ApiResponse(responseCode = "400", ref = "badRequest") })
	@PostMapping("/registration")
	public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
		return authService.createNewUser(registrationUserDto);
	}
}