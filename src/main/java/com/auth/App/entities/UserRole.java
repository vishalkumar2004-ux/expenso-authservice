package com.auth.App.entities;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@NoArgsConstructor
@Data
@Entity
@Table(name= "roles")
public class UserRole {

    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleID;

    @Column(name = "role_name")
    private String roleName;

    public UserRole(String roleName) {
        this.roleName = roleName;
    }

}
