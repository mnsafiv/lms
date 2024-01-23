package ru.safonoviv.lms.service;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import ru.safonoviv.lms.dto.BookDto;
import ru.safonoviv.lms.entities.User;
import ru.safonoviv.lms.exceptions.ExceptionCustomRollback;
import ru.safonoviv.lms.exceptions.NotFoundException;

import java.security.Principal;
import java.util.Collection;

public interface BookService {
    @Transactional(rollbackOn = ExceptionCustomRollback.class)
    ResponseEntity<?> saveBook(BookDto bookDto, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback;

    @Transactional(rollbackOn = ExceptionCustomRollback.class)
    ResponseEntity<?> updateBook(Long id, BookDto bookDto, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback;

    ResponseEntity<?> findBookPage(int pageNumber) throws NotFoundException;

    ResponseEntity<?> findBookPageAvailable(int pageNumber) throws NotFoundException;

    ResponseEntity<?> findBookById(Long id) throws NotFoundException;

    ResponseEntity<?> findBookByAuthorId(Long authorId, int pageNumber) throws NotFoundException;

    ResponseEntity<?> findBookByAuthorIdAvailable(Long authorId, int pageNumber) throws NotFoundException;

    ResponseEntity<?> findBookByGenreId(Long genreId, int pageNumber);

    ResponseEntity<?> findBookByGenreIdAvailable(Long genreId, int pageNumber);
}
