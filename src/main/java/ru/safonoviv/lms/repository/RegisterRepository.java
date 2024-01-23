package ru.safonoviv.lms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.safonoviv.lms.entities.RegisterBookReserve;

@Repository
public interface RegisterRepository extends JpaRepository<RegisterBookReserve, Long> {
}
