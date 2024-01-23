package ru.safonoviv.lms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "authors_books")
public class AuthorBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private AuthorBookId id;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    public AuthorBook(Author author, Book book) {
        this.author = author;
        this.book = book;
        this.id=new AuthorBookId(author.getId(),book.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorBook that = (AuthorBook) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
