package ru.safonoviv.lms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.safonoviv.lms.entities.*;

import java.time.LocalDate;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDto {
    private Long bookId;
    private String name;
    private String description;
    private boolean isAvailable;
    private Year year;
    private LocalDate dateCreated;
    @JsonIgnore
    private User userCreated;
    private Set<Author> authors;
    private Set<Genre> genres;


    public BookDto(Long bookId, String name, String description, boolean isAvailable) {
        this.bookId = bookId;
        this.name = name;
        this.description = description;
        this.isAvailable = isAvailable;
    }

    @SneakyThrows
    public BookDto(Long bookId, String name, String description, boolean isAvailable, Year year, LocalDate dateCreated, Long userCreatedId, String arrAuthor, String arrGenre) {
        this.bookId = bookId;
        this.year = year;
        this.name = name;
        this.userCreated = new User(userCreatedId);
        this.description = description;
        this.isAvailable = isAvailable;
        this.dateCreated = dateCreated;

        String splitArr = ",";
        String splitId = ";";
        arrAuthor = arrAuthor.replaceAll("[{}\"]", "");
        arrGenre = arrGenre.replaceAll("[{}\"]", "");

        authors = new HashSet<>();
        if (!arrAuthor.isEmpty()) {
            String[] dataAuthor = arrAuthor.split(splitArr);
            for (String s : dataAuthor) {
                String[] it = s.split(splitId);
                if(it.length==1) {
                    authors.add(new Author(Long.valueOf(it[0])));
                } if (it.length==2){
                    authors.add(new Author(Long.valueOf(it[0]),it[1]));
                }

            }
        }
        genres = new HashSet<>();
        if (!arrGenre.isEmpty()) {
            String[] dataGenre = arrGenre.split(splitArr);
            for (String s : dataGenre) {
                String[] it = s.split(splitId);
                if(it.length==1) {
                    genres.add(new Genre(Long.valueOf(it[0])));
                } if (it.length==2){
                    genres.add(new Genre(Long.valueOf(it[0]),it[1]));
                }
            }
        }
    }

    public Book convertToBook() {
        Book book = Book.builder()
                .id(bookId)
                .name(name)
                .description(description)
                .isAvailable(isAvailable)
                .dateCreated(dateCreated)
                .userCreated(userCreated)
                .year(year)
                .build();

        book.setGenreBooks(genres.stream().map(t -> new GenreBook(new Genre(t.getId()), book)).collect(Collectors.toSet()));
        book.setAuthorBooks(authors.stream().map(t -> new AuthorBook(new Author(t.getId()), book)).collect(Collectors.toSet()));

        return book;
    }


}
