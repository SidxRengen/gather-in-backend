package com.example.getherinjava.repository;

import com.example.getherinjava.entry.Group;
import com.example.getherinjava.entry.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface GroupRepository extends JpaRepository<Group,Long> {
    @Query("""
               SELECT g.id, g.name, g.photoUrl, MAX(gm.timestamp)
               FROM Group g
               LEFT JOIN GroupMessage gm ON gm.group = g
               LEFT JOIN g.members m
               LEFT JOIN g.admins a
               WHERE m.email = :email OR a.email = :email
               GROUP BY g.id, g.name, g.photoUrl
               ORDER BY MAX(gm.timestamp) DESC
            """)
    List<Object[]> getActiveGroup(@Param("email") String email);
}
