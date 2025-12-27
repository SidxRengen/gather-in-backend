package com.example.getherinjava.repository;

import com.example.getherinjava.entry.Message;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MessageRepository extends JpaRepository<Message,Long> {

}
