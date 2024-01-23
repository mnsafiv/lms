package ru.safonoviv.lms.service;

import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import ru.safonoviv.lms.entities.BookFeedback;
import ru.safonoviv.lms.entities.RegisterBookReserve;
import ru.safonoviv.lms.exceptions.ExceptionCustomRollback;
import ru.safonoviv.lms.exceptions.NotFoundException;

public interface RegisterService {

    @Transactional(rollbackOn = ExceptionCustomRollback.class)
    ResponseEntity<?> registerReservationBook(RegisterBookReserve registerBookReserve, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback;


    ResponseEntity<?> myAvailableBook(UsernamePasswordAuthenticationToken token) throws NotFoundException;

    @Transactional(rollbackOn = ExceptionCustomRollback.class)
    ResponseEntity<?> registerReturnBook(long registerId, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback;

    @Transactional(rollbackOn = ExceptionCustomRollback.class)
    ResponseEntity<?> registerConfirmBook(long registerId, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback;

    ResponseEntity<?> availableBook(int pageNumber, UsernamePasswordAuthenticationToken token) throws NotFoundException;

    @Transactional(rollbackOn = ExceptionCustomRollback.class)
    ResponseEntity<?> registerClosedBook(Long registerId, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback;

    @Transactional(rollbackOn = ExceptionCustomRollback.class)
    ResponseEntity<?> registerFeedback(BookFeedback registerId, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback;



}
