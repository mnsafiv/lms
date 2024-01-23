package ru.safonoviv.lms.service;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.safonoviv.lms.dto.GenreRequest;
import ru.safonoviv.lms.entities.Genre;
import ru.safonoviv.lms.exceptions.ExceptionCustomRollback;
import ru.safonoviv.lms.exceptions.NotFoundException;

public interface GenreService {

    @Transactional(rollbackOn = ExceptionCustomRollback.class)
    ResponseEntity<?> saveGenre(Genre genre, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback;

    @Transactional(rollbackOn = ExceptionCustomRollback.class)
    ResponseEntity<?> updateGenre(Long id, Genre genre, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback;

    ResponseEntity<?> findGenre(int pageNumber) throws NotFoundException;
}
