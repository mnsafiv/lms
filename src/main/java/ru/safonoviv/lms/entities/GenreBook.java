package ru.safonoviv.lms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cascade;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "books_genres")
public class GenreBook {
    @EmbeddedId
    private GenreBookId genreBookId;

    @ManyToOne
    @MapsId("genreId")
    @JoinColumn(name = "genre_id")
    private Genre genre;

    @ManyToOne
    @MapsId("bookId")
    @JoinColumn(name = "book_id")
    private Book book;

    public GenreBook(Genre genre, Book book) {
        this.genre = genre;
        this.book = book;
        this.genreBookId = new GenreBookId(genre.getId(),book.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreBook genreBook = (GenreBook) o;
        return Objects.equals(genreBookId, genreBook.genreBookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreBookId);
    }
}
