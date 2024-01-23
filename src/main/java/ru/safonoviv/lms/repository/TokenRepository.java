package ru.safonoviv.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.safonoviv.lms.entities.Author;
import ru.safonoviv.lms.entities.Token;
import ru.safonoviv.lms.entities.User;

import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {
    Optional<Token> findByUser(User user);
}
