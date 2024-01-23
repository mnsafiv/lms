package ru.safonoviv.lms.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;


import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class AuthorBookId implements Serializable {

    @Column(name = "author_id")
    private Long authorId;

    @Column(name = "book_id")
    private Long bookId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthorBookId that = (AuthorBookId) o;
        return Objects.equals(authorId, that.authorId) && Objects.equals(bookId, that.bookId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId, bookId);
    }
}
