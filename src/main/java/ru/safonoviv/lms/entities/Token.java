package ru.safonoviv.lms.entities;


import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "_token")
public class Token {
    @Id
    @GeneratedValue
    public Long id;

    @Column(unique = true)
    public String token;

    public boolean revoked;

    public boolean expired;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "token_user_id")
    public User user;
}
