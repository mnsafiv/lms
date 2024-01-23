package ru.safonoviv.lms.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import ru.safonoviv.lms.entities.User;

import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {
    private Long authorId;
    private String authorName;
    @JsonIgnore
    private User userCreated;
    private Set<BookDto> books;

    public AuthorDto(Long authorId,String authorName) {
        this.authorId = authorId;
        this.authorName = authorName;
    }


}
