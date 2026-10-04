package com.auth.App.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
@Entity
@Table(name = "users")
public class UserInfo {

    @Id
    @Column(name = "user_id")
    private String userId;

    private String username;

    private  String password;

    @Builder.Default
    @ManyToMany
    @JoinTable(
            name = "user_roles",
        joinColumns = @JoinColumn( name= "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<UserRole> roles = new HashSet<>();

}
