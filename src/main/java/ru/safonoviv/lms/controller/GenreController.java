package ru.safonoviv.lms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.safonoviv.lms.dto.GenreRequest;
import ru.safonoviv.lms.entities.Genre;
import ru.safonoviv.lms.service.GenreService;

import java.security.Principal;

@RestController
@RequestMapping("/v1/genre")
@RequiredArgsConstructor
public class GenreController {

    @Autowired
    private GenreService genreService;


    @Operation(description = "Create new genre", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse") })
    @PostMapping("/create")
    public ResponseEntity<?> createGenre(@RequestBody Genre genre, final UsernamePasswordAuthenticationToken token) {
        return genreService.saveGenre(genre, token);
    }

    @Operation(description = "Update genre by id", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse") })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGenre(@PathVariable("id") Long id, @RequestBody Genre genre, final UsernamePasswordAuthenticationToken token) {
        return genreService.updateGenre(id, genre, token);
    }

    @Operation(description = "Get genres per page", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse") })
    @GetMapping("page{number}")
    public ResponseEntity<?> findGenre(@PathVariable("number") int pageNumber) {
        return genreService.findGenre(pageNumber);
    }


}
