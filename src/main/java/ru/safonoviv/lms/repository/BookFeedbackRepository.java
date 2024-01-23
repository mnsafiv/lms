package ru.safonoviv.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.safonoviv.lms.entities.Author;
import ru.safonoviv.lms.entities.BookFeedback;

@Repository
public interface BookFeedbackRepository extends JpaRepository<BookFeedback, Long> {
}
