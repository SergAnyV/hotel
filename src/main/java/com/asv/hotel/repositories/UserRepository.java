package com.asv.hotel.repositories;

import com.asv.hotel.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

@Query(value = "SELECT * FROM users WHERE last_name ILIKE :last_name AND first_name ILIKE :first_name",nativeQuery = true)
List<User> findUserByLastNameAndFirstName(@Param("last_name") String lastName
        , @Param("first_name") String firstName);

}
