package ru.safonoviv.lms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class GenreBookId implements Serializable {
    @Column(name = "genre_id")
    private Long genreId;

    @Column(name = "book_id")
    private Long bookId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GenreBookId that = (GenreBookId) o;
        return Objects.equals(genreId, that.genreId) && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId, bookId);
    }
}
