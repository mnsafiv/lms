package ru.safonoviv.lms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.safonoviv.lms.dto.AuthorDto;
import ru.safonoviv.lms.entities.User;
import ru.safonoviv.lms.service.AuthorService;
import ru.safonoviv.lms.serviceimpl.AuthorServiceImpl;

import java.security.Principal;

@RestController
@RequestMapping("/v1/author")
@RequiredArgsConstructor
@Tag(name="/v1/author")
public class AuthorController {

    @Autowired
    private AuthorService authorServiceImpl;


    @Operation(description = "Add author", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse") })
    @PostMapping("/create")
    public ResponseEntity<?> createAuthor(@RequestBody AuthorDto authorRequest, final UsernamePasswordAuthenticationToken token) {
        return authorServiceImpl.saveAuthor(authorRequest, token);
    }


    @Operation(description = "Get authors by page", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse") })
    @GetMapping("/page{number}")
    public ResponseEntity<?> findAllAuthor(@PathVariable("number") int pageNumber) {
        return authorServiceImpl.findAllAuthor(pageNumber);
    }


    @Operation(description = "Update author by id", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse") })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateAuthor(@PathVariable("id") Long id, @RequestBody AuthorDto authorDto, final UsernamePasswordAuthenticationToken token) {
        return authorServiceImpl.updateAuthor(id, authorDto, token);
    }


}
