package com.asv.hotel.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @Column(name = "nick_name", unique = true, nullable = false, length = 30)
    private String nickName;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "fathers_name", nullable = true, length = 50)
    private String fathersName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "email", length = 50, unique = true)
    private String email;

    @Column(name = "phone", length = 30, unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "password", nullable = false, length = 120)
    private String password;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    private UserType role;

    @OneToMany(mappedBy ="user",fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<Booking> bookingSet = new HashSet<>();

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY,orphanRemoval = true)
    private Set<Report> reports = new HashSet<>();


}
