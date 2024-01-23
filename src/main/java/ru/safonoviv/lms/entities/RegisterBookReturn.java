package ru.safonoviv.lms.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "_register_book_return")
public class RegisterBookReturn {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "register_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="register_reserve_id")
    private RegisterBookReserve bookReserve;

    @Column(name = "last_update")
    private Date timeCreated;

    @Column(name = "valid")
    private Boolean valid;


}
