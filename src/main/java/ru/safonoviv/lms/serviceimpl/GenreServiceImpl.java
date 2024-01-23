package ru.safonoviv.lms.serviceimpl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.safonoviv.lms.dto.GenreResponse;
import ru.safonoviv.lms.entities.*;
import ru.safonoviv.lms.exceptions.ExceptionCustomRollback;
import ru.safonoviv.lms.exceptions.NotFoundException;
import ru.safonoviv.lms.repository.GenreRepository;
import ru.safonoviv.lms.service.GenreService;
import ru.safonoviv.lms.util.PrivilegeUtil;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GenreServiceImpl implements GenreService {

    @PersistenceContext
    private EntityManager entityManager;
    private final GenreRepository genreRepo;
    private final PrivilegeUtil privilegeUtil;


    @Override
    @Transactional
    public ResponseEntity<?> saveGenre(Genre genre, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback {
        if (privilegeUtil.isAdminRole(token)) {
            genre = genreRepo.save(genre);
            if (genre.getId() != null) {
                return ResponseEntity.ok(genre.getId() + " " + genre.getName());
            }
        }
        throw new ExceptionCustomRollback("Не удалось сохранить", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> updateGenre(Long id, Genre genre, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback {
        if (privilegeUtil.isAdminRole(token)) {
            CriteriaBuilder cb = entityManager.getCriteriaBuilder();
            CriteriaUpdate<Genre> criteriaUpdateBook = cb.createCriteriaUpdate(Genre.class);
            Root<Genre> bookRootUpdate = criteriaUpdateBook.from(Genre.class);
            criteriaUpdateBook.where(cb.equal(bookRootUpdate.get("id"), id));
            criteriaUpdateBook.set("name", genre.getName());
            if (entityManager.createQuery(criteriaUpdateBook).executeUpdate() == 1) {
                return ResponseEntity.ok("Успешно сохранено");
            }
        }
        throw new NotFoundException("Не удалось обновить", HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<?> findGenre(int pageNumber) throws NotFoundException {
        if (--pageNumber < 0) {
            throw new NotFoundException("Bad request!", HttpStatus.BAD_REQUEST);
        }
        int pageSize = 5;

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GenreResponse> cr = cb.createQuery(GenreResponse.class);
        Root<Genre> root = cr.from(Genre.class);
        CriteriaQuery<GenreResponse> select = cr.multiselect(root.get("id"), root.get("name"));

        TypedQuery<GenreResponse> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(pageNumber * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<GenreResponse> results = typedQuery.getResultList();
        if(results.isEmpty()){
            throw new NotFoundException("No genre!", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(results);
    }


}
