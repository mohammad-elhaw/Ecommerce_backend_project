package com.backend.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class ResetToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String resetToken;

    @OneToOne
    @JoinColumn(name = "user_id")
    private LocalUser user;

    public ResetToken(String resetToken, LocalUser user) {
        this.resetToken = resetToken;
        this.user = user;
    }
}
