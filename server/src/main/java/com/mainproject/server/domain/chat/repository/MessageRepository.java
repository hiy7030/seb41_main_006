package com.mainproject.server.domain.chat.repository;

import com.mainproject.server.domain.chat.entity.ChatMessage;
import com.mainproject.server.domain.chat.entity.ChatRoom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<ChatMessage, Long> {
    Page<ChatMessage> findByChatRoom(Pageable pageable, ChatRoom chatRoom);
    List<ChatMessage> findAllByChatRoom(ChatRoom chatRoom);
}
