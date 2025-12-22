package com.garage.management.Entity;

import jakarta.persistence.*;
import lombok.*;


@Setter
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String password;
    private String role; // Example: "ROLE_ADMIN" or "ROLE_USER"

}
