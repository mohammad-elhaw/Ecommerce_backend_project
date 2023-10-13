package com.backend.ecommerce.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "privileges")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long privilegeId;

    private String privilegeName;

    @ManyToMany(mappedBy = "privileges")
    private List<Role> roles;

    public Privilege(String privilegeName) {
        this.privilegeName = privilegeName;
    }
}
