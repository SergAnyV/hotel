package com.asv.hotel.repositories;

import com.asv.hotel.entities.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Long> {

    @Query(value = "SELECT * FROM user_types WHERE role ILIKE :role", nativeQuery = true)
    Optional<UserType> findUserTypeByRoleLikeIgnoreCase(@Param("role") String role);


    @Modifying
    @Query(value = "DELETE FROM user_types WHERE role ILIKE :role",nativeQuery = true)
    int deleteByRole(@Param("role") String role);

    @Modifying
    @Query(value = """
            UPDATE user_types SET role = :role, description = :description, is_active = :isActive
            WHERE id = :id
            """, nativeQuery = true)
    int updateUserType(
            @Param("id") Long id,
            @Param("role") String role,
            @Param("description") String description,
            @Param("isActive") boolean isActive);

    @Query(value = """
    SELECT ut.* 
    FROM user_types ut
    INNER JOIN user_type_job_type utjt ON ut.id = utjt.user_type_id
    WHERE utjt.job_type_id = :jobTypeId
    """, nativeQuery = true)
    List<UserType> findUserTypesByJobTypeId(@Param("jobTypeId") Long jobTypeId);
}
