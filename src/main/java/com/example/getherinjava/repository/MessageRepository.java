package com.example.getherinjava.repository;

import com.example.getherinjava.entry.Message;
import com.example.getherinjava.entry.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("""
        SELECT m.receiver.userName,
               m.receiver.email,
               m.receiver.photoUrl,
               MAX(m.timestamp)
        FROM Message m
        WHERE m.sender.email = :email
        GROUP BY m.receiver.userName, m.receiver.email, m.receiver.photoUrl
    """)
    List<Object[]> findSenderUser(@Param("email") String email);

    @Query("""
        SELECT m.sender.userName,
               m.sender.email,
               m.sender.photoUrl,
               MAX(m.timestamp)
        FROM Message m
        WHERE m.receiver.email = :email
        GROUP BY m.sender.userName, m.sender.email, m.sender.photoUrl
    """)
    List<Object[]> findReceiverUser(@Param("email") String email);
}