package com.asv.hotel.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_types")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserType {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
   private Long id;

    @Column(name = "role",unique = true,nullable = false,length = 30)
  private   String role;

    @Column(name = "description",nullable = false,length = 250)
   private String description;

    @Column(name = "is_active",nullable = false)
    private Boolean isActive;

    @ManyToMany(mappedBy = "userTypes",cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    private Set<JobType> jobTypeList=new HashSet<>();


}
