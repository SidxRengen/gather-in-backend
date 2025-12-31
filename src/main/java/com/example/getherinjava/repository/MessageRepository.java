package com.example.getherinjava.repository;

import com.example.getherinjava.entry.Message;
import com.example.getherinjava.entry.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,Long> {
//        @Query("""
//        SELECT
//          CASE
//            WHEN m.sender.email = :email THEN m.receiver
//            ELSE m.sender
//          END
//        FROM Message m
//        WHERE m.sender.email = :email OR m.receiver.email = :email
//        GROUP BY
//          CASE
//            WHEN m.sender.email = :email THEN m.receiver
//            ELSE m.sender
//          END
//        ORDER BY MAX(m.timestamp) DESC
//    """)
//    List<User> findChatUsers(@Param("email") String email);
    @Query("""
               SELECT m.receiver.userName, m.receiver.email, MAX(m.timestamp)
               FROM Message m
               WHERE m.sender.email= :email
               GROUP BY m.receiver
            """)
    List<Object[]> findSenderUser(@Param("email") String email);
    @Query("""
               SELECT m.sender.userName, m.sender.email, MAX(m.timestamp)
               FROM Message m
               WHERE m.receiver.email= :email
               GROUP BY m.sender
            """)
    List<Object[]> findReceiverUser(@Param("email") String email);
}
