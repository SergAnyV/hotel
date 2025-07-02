package com.asv.hotel.repositories;

import com.asv.hotel.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {
}
