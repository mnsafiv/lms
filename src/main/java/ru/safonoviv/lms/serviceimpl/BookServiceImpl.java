package ru.safonoviv.lms.serviceimpl;

import jakarta.persistence.*;
import jakarta.persistence.criteria.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import ru.safonoviv.lms.dto.*;
import ru.safonoviv.lms.entities.*;
import ru.safonoviv.lms.exceptions.ExceptionCustomRollback;
import ru.safonoviv.lms.exceptions.NotFoundException;
import ru.safonoviv.lms.repository.BookRepository;
import ru.safonoviv.lms.repository.GenreRepository;
import ru.safonoviv.lms.service.BookService;
import ru.safonoviv.lms.util.PrivilegeUtil;

import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final PrivilegeUtil privilegeUtil;
    private final BookRepository bookRepo;
    private final GenreRepository genreRepo;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(rollbackOn = ExceptionCustomRollback.class)
    public ResponseEntity<?> saveBook(BookDto bookDto, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback {
        Book book = bookDto.convertToBook();
        book.setUserCreated((User) token.getPrincipal());
        book.setDateCreated(LocalDate.now());
        entityManager.persist(book);

        if (book.getId() != null) {
            return ResponseEntity.ok(String.format("Книга сохранена id: %s, название: %s, описание: %s", book.getId(), book.getName(), book.getDescription()));
        }
        throw new ExceptionCustomRollback("Не удалось сохранить", HttpStatus.BAD_REQUEST);
    }


    @Override
    public ResponseEntity<?> findBookPage(int pageNumber) throws NotFoundException {
        if (--pageNumber < 0) {
            throw new NotFoundException("Bad request!", HttpStatus.BAD_REQUEST);
        }
        int pageSize = 3;

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookDto> cr = cb.createQuery(BookDto.class);
        Root<Book> bookRoot = cr.from(Book.class);

        TypedQuery<BookDto> typedQuery = getBooksDto(cb, cr, bookRoot);
        typedQuery.setFirstResult(pageNumber * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<BookDto> results = typedQuery.getResultList();

        if (results.isEmpty()) {
            throw new NotFoundException("Нет книг или не существует страница", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(results);
    }

    @Override
    @Transactional(rollbackOn = ExceptionCustomRollback.class)
    public ResponseEntity<?> updateBook(Long id, BookDto updateBookDto, UsernamePasswordAuthenticationToken token) throws ExceptionCustomRollback, NotFoundException {
        updateBookDto.setBookId(id);
        Book updateBook = updateBookDto.convertToBook();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Book> cr = cb.createQuery(Book.class);
        Root<Book> bookRoot = cr.from(Book.class);
        Join<AuthorBook, Author> author = bookRoot.join("authorBooks", JoinType.LEFT).join("author", JoinType.LEFT);
        Join<GenreBook, Genre> genre = bookRoot.join("genreBooks", JoinType.LEFT).join("genre", JoinType.LEFT);
        cr.where(cb.equal(bookRoot.get("id"), id));

        Expression<String> concatWsAuthor = cb.function("jsonb_build_object", String.class,
                cb.literal("id"),
                author.get("id"));

        Expression<String> concatWsGenre = cb.function("jsonb_build_object", String.class,
                cb.literal("id"),
                genre.get("id"));

        Expression<String> distinctAuthor = cb.function("DISTINCT", String.class, concatWsAuthor);
        Expression<String> distinctGenre = cb.function("DISTINCT", String.class, concatWsGenre);

        CriteriaQuery<Book> select = cr.multiselect(
                        bookRoot,
                        cb.function("jsonb_agg", String.class, distinctAuthor),
                        cb.function("jsonb_agg", String.class, distinctGenre)
                )
                .groupBy(
                        bookRoot.get("id")
                );

        Book existBook;
        try {
            existBook = entityManager.createQuery(select).getSingleResult();
        } catch (NoResultException e) {
            throw new NotFoundException("Not found!", HttpStatus.BAD_REQUEST);
        }

        if (privilegeUtil.isCreatorOrAdmin(token, existBook.getUserCreated())) {
            CriteriaUpdate<Book> criteriaUpdateBook = cb.createCriteriaUpdate(Book.class);
            Root<Book> bookRootUpdate = criteriaUpdateBook.from(Book.class);
            criteriaUpdateBook.where(cb.equal(bookRootUpdate.get("id"), id));
            criteriaUpdateBook.set("name", updateBookDto.getName());
            criteriaUpdateBook.set("description", updateBookDto.getDescription());
            criteriaUpdateBook.set("isAvailable", updateBookDto.isAvailable());
            criteriaUpdateBook.set("year", updateBookDto.getYear());
            try {
                updateBook.getAuthorBooks().stream().filter(t -> !existBook.getAuthorBooks().contains(t)).forEach(t -> entityManager.persist(entityManager.merge(t)));
                existBook.getAuthorBooks().stream().filter(t -> !updateBook.getAuthorBooks().contains(t)).forEach(t -> entityManager.remove(entityManager.merge(t)));
                updateBook.getGenreBooks().stream().filter(t -> !existBook.getGenreBooks().contains(t)).forEach(t -> entityManager.persist(entityManager.merge(t)));
                existBook.getGenreBooks().stream().filter(t -> !updateBook.getGenreBooks().contains(t)).forEach(t -> entityManager.remove(entityManager.merge(t)));
                entityManager.createQuery(criteriaUpdateBook).executeUpdate();
            } catch (IllegalArgumentException | PersistenceException e) {
                throw new ExceptionCustomRollback("No updated!", HttpStatus.BAD_REQUEST);
            }
            return ResponseEntity.ok("Книга обновлена");
        }
        throw new ExceptionCustomRollback("No updated!", HttpStatus.BAD_REQUEST);
    }


    @Override
    public ResponseEntity<?> findBookPageAvailable(int pageNumber) throws NotFoundException {
        if (--pageNumber < 0) {
            throw new NotFoundException("Bad request!", HttpStatus.BAD_REQUEST);
        }
        int pageSize = 3;

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookDto> cr = cb.createQuery(BookDto.class);
        Root<Book> bookRoot = cr.from(Book.class);

        TypedQuery<BookDto> typedQuery = getBooksDto(cb, cr, bookRoot,
                cb.equal(bookRoot.get("isAvailable"), true));

        typedQuery.setFirstResult(pageNumber * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<BookDto> results = typedQuery.getResultList();

        if (results.isEmpty()) {
            throw new NotFoundException("Нет книг или не существует страница", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(results);
    }

    @Override
    public ResponseEntity<?> findBookById(Long id) throws NotFoundException {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookDto> cr = cb.createQuery(BookDto.class);
        Root<Book> bookRoot = cr.from(Book.class);

        TypedQuery<BookDto> typedQuery = getBooksDto(cb, cr, bookRoot,
                cb.equal(bookRoot.get("id"), id));

        try {
            return ResponseEntity.ok(typedQuery.getSingleResult());
        } catch (NoResultException e) {
            throw new NotFoundException("Not found", HttpStatus.BAD_REQUEST);
        }
    }

    @Override
    public ResponseEntity<?> findBookByAuthorId(Long authorId, int pageNumber) throws NotFoundException {
        if (--pageNumber < 0) {
            throw new NotFoundException("Bad request!", HttpStatus.BAD_REQUEST);
        }
        int pageSize = 3;

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookDto> cr = cb.createQuery(BookDto.class);
        Root<Book> bookRoot = cr.from(Book.class);

        Subquery<Long> sub = cr.subquery(Long.class);
        Root<AuthorBook> subRoot = sub.from(AuthorBook.class);
        sub.select(subRoot.get("book").get("id")).where(cb.equal(subRoot.get("author").get("id"), authorId));

        TypedQuery<BookDto> typedQuery = getBooksDto(cb, cr, bookRoot,
                cb.in(bookRoot.get("id")).value(sub));

        typedQuery.setFirstResult(pageNumber * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<BookDto> results = typedQuery.getResultList();

        if (results.isEmpty()) {
            throw new NotFoundException("Автор не найден или не существует страница", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(results);
    }

    @Override
    public ResponseEntity<?> findBookByAuthorIdAvailable(Long authorId, int pageNumber) throws NotFoundException {
        if (--pageNumber < 0) {
            throw new NotFoundException("Bad request!", HttpStatus.BAD_REQUEST);
        }
        int pageSize = 3;

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookDto> cr = cb.createQuery(BookDto.class);
        Root<Book> bookRoot = cr.from(Book.class);

        Subquery<Long> sub = cr.subquery(Long.class);
        Root<AuthorBook> subRoot = sub.from(AuthorBook.class);
        sub.select(subRoot.get("book").get("id")).where(cb.equal(subRoot.get("author").get("id"), authorId));

        TypedQuery<BookDto> typedQuery = getBooksDto(cb, cr, bookRoot,
                cb.in(bookRoot.get("id")).value(sub),
                cb.equal(bookRoot.get("isAvailable"), true));

        typedQuery.setFirstResult(pageNumber * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<BookDto> results = typedQuery.getResultList();

        if (results.isEmpty()) {
            throw new NotFoundException("Автор не найден или не существует страница", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(results);
    }

    @Override
    public ResponseEntity<?> findBookByGenreId(Long genreId, int pageNumber) throws NotFoundException {
        if (--pageNumber < 0) {
            throw new NotFoundException("Bad request!", HttpStatus.BAD_REQUEST);
        }
        int pageSize = 3;

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookDto> cr = cb.createQuery(BookDto.class);
        Root<Book> bookRoot = cr.from(Book.class);

        Subquery<Long> sub = cr.subquery(Long.class);
        Root<GenreBook> subRoot = sub.from(GenreBook.class);
        sub.select(subRoot.get("book").get("id")).where(cb.equal(subRoot.get("genre").get("id"), genreId));

        TypedQuery<BookDto> typedQuery = getBooksDto(cb, cr, bookRoot,
                cb.in(bookRoot.get("id")).value(sub),
                cb.equal(bookRoot.get("isAvailable"), true));

        typedQuery.setFirstResult(pageNumber * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<BookDto> results = typedQuery.getResultList();

        if (results.isEmpty()) {
            throw new NotFoundException("Автор не найден или не существует страница", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(results);
    }

    @Override
    public ResponseEntity<?> findBookByGenreIdAvailable(Long genreId, int pageNumber) throws NotFoundException {
        if (--pageNumber < 0) {
            throw new NotFoundException("Bad request!", HttpStatus.BAD_REQUEST);
        }
        int pageSize = 3;

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<BookDto> cr = cb.createQuery(BookDto.class);
        Root<Book> bookRoot = cr.from(Book.class);

        Subquery<Long> sub = cr.subquery(Long.class);
        Root<GenreBook> subRoot = sub.from(GenreBook.class);
        sub.select(subRoot.get("book").get("id")).where(cb.equal(subRoot.get("genre").get("id"), genreId));

        TypedQuery<BookDto> typedQuery = getBooksDto(cb, cr, bookRoot,
                cb.in(bookRoot.get("id")).value(sub));

        typedQuery.setFirstResult(pageNumber * pageSize);
        typedQuery.setMaxResults(pageSize);

        List<BookDto> results = typedQuery.getResultList();

        if (results.isEmpty()) {
            throw new NotFoundException("Автор не найден или не существует страница", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(results);
    }

    public TypedQuery<BookDto> getBooksDto(CriteriaBuilder cb, CriteriaQuery<BookDto> cr, Root<Book> bookRoot, Predicate... predicates) {
        Join<AuthorBook, Author> author = bookRoot.join("authorBooks", JoinType.LEFT).join("author", JoinType.LEFT);
        Join<GenreBook, Genre> genre = bookRoot.join("genreBooks", JoinType.LEFT).join("genre", JoinType.LEFT);

        Expression<String> concatWsAuthor = cb.function("CONCAT_WS", String.class,
                cb.literal(";"),
                author.get("id"),
                author.get("name"));

        Expression<String> concatWsGenre = cb.function("CONCAT_WS", String.class,
                cb.literal(";"),
                genre.get("id"),
                genre.get("name"));

        Expression<String> distinctAuthor = cb.function("DISTINCT", String.class, concatWsAuthor);
        Expression<String> distinctGenre = cb.function("DISTINCT", String.class, concatWsGenre);

        cr.where(predicates);


        CriteriaQuery<BookDto> select = cr.multiselect(
                        bookRoot.get("id"),
                        bookRoot.get("name"),
                        bookRoot.get("description"),
                        bookRoot.get("isAvailable"),
                        bookRoot.get("year"),
                        bookRoot.get("dateCreated"),
                        bookRoot.get("userCreated").get("id"),
                        cb.function("array_agg", String.class, distinctAuthor),
                        cb.function("array_agg", String.class, distinctGenre)
                )
                .groupBy(
                        bookRoot.get("id")
                );

        return entityManager.createQuery(select);
    }


}
