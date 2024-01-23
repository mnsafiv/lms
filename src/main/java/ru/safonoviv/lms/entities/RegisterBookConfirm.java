package ru.safonoviv.lms.entities;

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
@Table(name = "_register_book_confirm")
public class RegisterBookConfirm {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "register_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="register_reserve_id")
    private RegisterBookReserve bookReserve;

    @Column(name = "valid")
    private Boolean valid;

    @Column(name = "last_update")
    private Date timeCreated;


}
