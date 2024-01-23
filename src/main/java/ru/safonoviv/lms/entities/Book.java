package ru.safonoviv.lms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_book")
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @Column(name = "book_name")
    private String name;

    @Column(name = "book_description")
    private String description;

    @Column(name = "book_available")
    private boolean isAvailable;

    @Column(name = "book_year_publication")
    private Year year;

    @Column(name = "book_date_created")
    private LocalDate dateCreated;

    @JsonIgnore
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private Set<AuthorBook> authorBooks;

    @JsonIgnore
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<GenreBook> genreBooks;


    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY,cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    @JoinColumn(name = "book_create_user_id")
    private User userCreated;

    public Book(Long id, String name, String description, boolean isAvailable, Year year, LocalDate dateCreated, User userCreated) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.isAvailable = isAvailable;
        this.year = year;
        this.dateCreated = dateCreated;
        this.userCreated = userCreated;
    }

    @SneakyThrows
    public Book(Book book, String authorBooks, String genreBooks) {
        this.id = book.getId();
        this.name = book.getName();
        this.description = book.getDescription();
        this.isAvailable = book.isAvailable;
        this.year = book.getYear();
        this.dateCreated = book.getDateCreated();
        this.userCreated = book.getUserCreated();
        ObjectMapper objMapper = new ObjectMapper();
        List<Genre> genres = objMapper.readValue(genreBooks, new TypeReference<>() {
        });
        List<Author> authors = objMapper.readValue(authorBooks, new TypeReference<>() {
        });
        this.genreBooks = genres.stream().map(t->new GenreBook(t,this)).collect(Collectors.toSet());
        this.authorBooks = authors.stream().map(t->new AuthorBook(t,this)).collect(Collectors.toSet());
    }

    public Book(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return Objects.equals(id, book.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
