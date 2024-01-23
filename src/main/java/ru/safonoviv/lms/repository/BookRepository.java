package ru.safonoviv.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.safonoviv.lms.entities.Book;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {
}
