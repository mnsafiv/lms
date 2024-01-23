package ru.safonoviv.lms.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "_book_feedback")
public class BookFeedback {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_feedback_id")
    private Integer id;

    @Column(name = "book_feedback_description")
    private String description;

    @Column(name = "book_feedback_rating")
    private Rating rating;

    @OneToOne
    @JoinColumn(name="book_feedback_book_register_id")
    private RegisterBookReserve bookReserve;


    @Column(name = "book_feedback_status")
    @Enumerated
    private FeedbackStatus status;


}
