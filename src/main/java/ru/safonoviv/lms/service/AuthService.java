package ru.safonoviv.lms.service;


import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import ru.safonoviv.lms.dto.JwtRequest;
import ru.safonoviv.lms.dto.JwtResponse;
import ru.safonoviv.lms.dto.RegistrationUserDto;
import ru.safonoviv.lms.dto.UserDto;
import ru.safonoviv.lms.entities.User;
import ru.safonoviv.lms.exceptions.ExceptionCustomRollback;
import ru.safonoviv.lms.exceptions.NotFoundException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserService userService;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) throws NotFoundException {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new NotFoundException("Ошибка авторизации", HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
        String token = jwtTokenService.getOrGenerateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    public ResponseEntity<?> createNewUser(@RequestBody RegistrationUserDto registrationUserDto) {
        if (userService.loadByUsername(registrationUserDto.getEmail()).isPresent()) {
            return new ResponseEntity<>(new ExceptionCustomRollback("Данная почта существует",HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
        }
        User user = userService.createNewUser(registrationUserDto);
        return ResponseEntity.ok(new UserDto(user.getId(), user.getName(), user.getEmail()));
    }
}
