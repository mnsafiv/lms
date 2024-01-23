package ru.safonoviv.lms.service;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.safonoviv.lms.dto.AuthorDto;
import ru.safonoviv.lms.exceptions.ExceptionCustomRollback;
import ru.safonoviv.lms.exceptions.NotFoundException;

public interface AuthorService {
    ResponseEntity<?> findAllAuthor(int pageNumber) throws NotFoundException;

    @Transactional(rollbackOn = ExceptionCustomRollback.class)
    ResponseEntity<?> saveAuthor(AuthorDto authorRequest, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback;

    @Transactional(rollbackOn = ExceptionCustomRollback.class)
    ResponseEntity<?> updateAuthor(Long id, AuthorDto authorDto, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback;
}
