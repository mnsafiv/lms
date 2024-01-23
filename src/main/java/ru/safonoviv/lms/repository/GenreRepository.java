package ru.safonoviv.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.safonoviv.lms.entities.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Long> {
}
