package com.badnarrators.celos.message.repository;

import com.badnarrators.celos.message.entity.Message;
import com.badnarrators.celos.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
    int countByReceiverAndRead(User receiver, boolean b);

    List<Message> findByReceiver(User receiver);

    List<Message> findBySender(User sender);

}
