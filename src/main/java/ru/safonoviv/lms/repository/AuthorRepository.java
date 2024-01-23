package ru.safonoviv.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.safonoviv.lms.entities.Author;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
