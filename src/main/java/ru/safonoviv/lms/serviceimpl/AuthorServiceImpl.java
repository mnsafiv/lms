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
import ru.safonoviv.lms.dto.AuthorDto;
import ru.safonoviv.lms.dto.BookDto;
import ru.safonoviv.lms.entities.*;
import ru.safonoviv.lms.exceptions.NotFoundException;
import ru.safonoviv.lms.repository.AuthorRepository;
import ru.safonoviv.lms.exceptions.ExceptionCustomRollback;
import ru.safonoviv.lms.service.AuthorService;
import ru.safonoviv.lms.service.UserService;
import ru.safonoviv.lms.util.PrivilegeUtil;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

    @PersistenceContext
    private EntityManager entityManager;

    private final AuthorRepository authorRepo;
    private final PrivilegeUtil privilegeUtil;

    @Override
    public ResponseEntity<?> findAllAuthor(int pageNumber) throws NotFoundException {
        if (--pageNumber < 0) {
            throw new NotFoundException("Bad request!", HttpStatus.BAD_REQUEST);
        }
        int pageSize = 3;
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Author> cr = cb.createQuery(Author.class);
        Root<Author> bookRoot = cr.from(Author.class);
        CriteriaQuery<Author> select = cr.multiselect(
                bookRoot.get("id"),
                bookRoot.get("name")
        );
        TypedQuery<Author> typedQuery = entityManager.createQuery(select);
        typedQuery.setFirstResult(pageNumber * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<Author> results = typedQuery.getResultList();

        if (results.isEmpty()) {
            throw new NotFoundException("Нет книг или не существует страница", HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(results);
    }

    @Override
    @Transactional(rollbackOn = ExceptionCustomRollback.class)
    public ResponseEntity<?> saveAuthor(AuthorDto authorDto, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback {
        Author author = new Author(null, authorDto.getAuthorName(), (User) token.getPrincipal(), null);
        author = authorRepo.save(author);
        if (author.getId() != null) {
            return ResponseEntity.ok(author.getId() + " " + author.getName());
        }
        throw new ExceptionCustomRollback("Не удалось сохранить",HttpStatus.BAD_REQUEST);
    }

    @Override
    @Transactional(rollbackOn = ExceptionCustomRollback.class)
    public ResponseEntity<?> updateAuthor(Long id, AuthorDto authorDto, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback {
        User user = (User) token.getPrincipal();
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaUpdate<Author> criteriaUpdateBook = cb.createCriteriaUpdate(Author.class);
        Root<Author> rootBook = criteriaUpdateBook.from(Author.class);
        criteriaUpdateBook.set("name", authorDto.getAuthorName());
        if (privilegeUtil.isAdminRole(token)) {
            criteriaUpdateBook.where(cb.equal(rootBook.get("id"), id));
        } else {
            criteriaUpdateBook.where(cb.equal(rootBook.get("id"), id), cb.equal(rootBook.get("userCreated").get("id"), user.getId()));
        }
        if (entityManager.createQuery(criteriaUpdateBook).executeUpdate() == 1) {
            return ResponseEntity.ok("Обновлено");
        }
        throw new ExceptionCustomRollback("Bad request!",HttpStatus.BAD_REQUEST);
    }


}
