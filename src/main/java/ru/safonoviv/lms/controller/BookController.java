package ru.safonoviv.lms.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import ru.safonoviv.lms.dto.BookDto;
import ru.safonoviv.lms.service.BookService;

@RestController
@RequestMapping("/v1/book")
@RequiredArgsConstructor
public class BookController {
    @Autowired
    private BookService bookServiceImpl;

    @Operation(description = "Add book", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse") })
    @PostMapping("/create")
    public ResponseEntity<?> createBook(@RequestBody BookDto bookDto, final UsernamePasswordAuthenticationToken token) {
        return bookServiceImpl.saveBook(bookDto, token);
    }

    @Operation(description = "Update book by id", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse") })
    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(@PathVariable("id") Long id, @RequestBody BookDto bookDto, final UsernamePasswordAuthenticationToken token) {
        return bookServiceImpl.updateBook(id, bookDto, token);
    }

    @Operation(description = "Get book by id", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse") })
    @GetMapping("/{id}")
    public ResponseEntity<?> getBook(@PathVariable("id") Long id) {
        return bookServiceImpl.findBookById(id);
    }


    @Operation(description = "Get books per page", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse") })
    @GetMapping("/page{number}")
    public ResponseEntity<?> findBook(@PathVariable("number") int number) {
        return bookServiceImpl.findBookPage(number);
    }

    @Operation(description = "Get books per page if available", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse") })
    @GetMapping("/page{number}/available")
    public ResponseEntity<?> findBookAvailable(@PathVariable("number") int number) {
        return bookServiceImpl.findBookPageAvailable(number);
    }


    @Operation(description = "Get books by author per page", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse") })
    @GetMapping("/author/{id}/page{number}")
    public ResponseEntity<?> findBookByAuthorId(@PathVariable("id") Long id, @PathVariable("number") int pageNumber) {
        return bookServiceImpl.findBookByAuthorId(id, pageNumber);
    }


    @Operation(description = "Get books by author per page if available", responses = {
            @ApiResponse(responseCode = "200", ref = "successResponse"),
            @ApiResponse(responseCode = "400", ref = "badRequest"),
            @ApiResponse(responseCode = "403", ref = "forbiddenResponse") })
    @GetMapping("/author/{id}/page{number}/available")
    public ResponseEntity<?> findAuthorBookByIdAvailable(@PathVariable("id") Long id, @PathVariable("number") int pageNumber) {
        return bookServiceImpl.findBookByAuthorIdAvailable(id, pageNumber);
    }


}
