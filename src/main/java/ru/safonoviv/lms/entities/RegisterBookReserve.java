package ru.safonoviv.lms.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_register_book_reserve")
public class RegisterBookReserve {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "register_id")
    private Long id;


    @ManyToOne
    @JoinColumn(name="register_book_id")
    private Book book;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name="register_user_id")
    private User user;

    @Column(name = "register_book_date_start")
    private LocalDate dateTakeBookStart;

    @Column(name = "register_book_date_end")
    private LocalDate dateTakeBookEnd;

    @Column(name = "last_update")
    private Date timeCreated;

    @Column(name = "valid")
    private Boolean valid;

    public RegisterBookReserve(Long id) {
        this.id = id;
    }
}
