package ru.safonoviv.lms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.safonoviv.lms.entities.BookFeedback;
import ru.safonoviv.lms.entities.RegisterBookReserve;
import ru.safonoviv.lms.service.RegisterService;

@RestController
@RequestMapping("/v1/bookreg")
@RequiredArgsConstructor
public class RegisterController {
    @Autowired
    private RegisterService registerServiceImpl;


    @Operation(description = "Create reserve book", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse")})
    @PostMapping("/create")
    public ResponseEntity<?> registerReservationBook(@RequestBody RegisterBookReserve registerBookReserve, final UsernamePasswordAuthenticationToken token) {
        return registerServiceImpl.registerReservationBook(registerBookReserve, token);
    }

    @Operation(description = "Confirm reserve book", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse")})
    @PostMapping("/{id}/confirm")
    public ResponseEntity<?> registerConfirmBook(@PathVariable("id") Long registerId, final UsernamePasswordAuthenticationToken token) {
        return registerServiceImpl.registerConfirmBook(registerId, token);
    }

    @Operation(description = "Return reserve book", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse")})
    @PostMapping("/{id}/return")
    public ResponseEntity<?> registerClosedBook(@PathVariable("id") Long registerId, final UsernamePasswordAuthenticationToken token) {
        return registerServiceImpl.registerReturnBook(registerId, token);
    }

    @Operation(description = "Leave feedback for the book by registerId", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse")})
    @PostMapping("/{id}/feedback")
    public ResponseEntity<?> registerFeedback(@PathVariable("id") Long registerId, @RequestBody BookFeedback bookFeedback, final UsernamePasswordAuthenticationToken token) {
        return registerServiceImpl.registerFeedback(bookFeedback, token);
    }

    @Operation(description = "Get actual reserve book per page", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse")})
    @GetMapping("/page{number}")
    public ResponseEntity<?> getReserveBook(@PathVariable("number") int number, final UsernamePasswordAuthenticationToken token) {
        return registerServiceImpl.availableBook(number, token);
    }


}
