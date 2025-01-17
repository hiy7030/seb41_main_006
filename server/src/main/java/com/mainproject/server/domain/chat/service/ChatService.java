package com.mainproject.server.domain.chat.service;

import com.mainproject.server.domain.chat.dto.MessageDto;
import com.mainproject.server.domain.chat.entity.ChatMessage;
import com.mainproject.server.domain.chat.entity.ChatRoom;
import com.mainproject.server.domain.chat.repository.MessageRepository;
import com.mainproject.server.domain.chat.repository.RoomRepository;
import com.mainproject.server.domain.member.entity.Member;
import com.mainproject.server.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.cache.Cache;
import org.springframework.data.domain.*;
import org.springframework.data.redis.cache.RedisCache;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ChatService {

    private final MemberService memberService;
    private final RoomService roomService;
    private final RoomRepository roomRepository;
    private final MessageRepository messageRepository;

    @Resource(name = "chatRedisTemplate")
    private final RedisTemplate<String, ChatMessage> redisTemplate;


    private static final String MESSAGE_CACHE_KEY = "messageCacheRoom:";

    public void CachedMessage(MessageDto dto, Long roomId) {
        Member member = memberService.validateVerifyMember(dto.getSenderId());
        ChatRoom chatRoom = roomService.findRoom(roomId);

        ChatMessage chatMessage = ChatMessage.builder()
                .content(dto.getContent())
                .sender(member)
                .chatRoom(chatRoom)
                .sendTime(LocalDateTime.now())
                .build();
        String cacheKey = MESSAGE_CACHE_KEY+roomId;

        redisTemplate.opsForList().rightPush(cacheKey, chatMessage);
    }
    @Scheduled(cron = "0 0 0/1 * * *") // 한시간마다
    @Transactional
    public void saveMessages() {
        // 레디스에 캐싱된 채팅방 아이디만 파싱
        List<Long> roomIdList = redisTemplate.keys(MESSAGE_CACHE_KEY+"*").stream()
                .map(key -> Long.parseLong(key.substring(MESSAGE_CACHE_KEY.length())))
                .collect(Collectors.toList());
        // 각 채팅방의 캐싱된 메세지를 찾아 DB에 저장한 후, 캐싱된 메세지는 삭제
        for(Long id : roomIdList) {
            String cacheKey = MESSAGE_CACHE_KEY + id;
            try{
                List<ChatMessage> messages = redisTemplate.opsForList().range(cacheKey, 0, -1);
                if(messages != null && messages.size() > 0) {
                    messageRepository.saveAll(messages);
                    redisTemplate.opsForList().trim(cacheKey, messages.size(), -1);
                } else {
                    continue;
                }
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
    }

    public Page<ChatMessage> findMessages(long roomId, int page, int size) {
        String cacheKey = MESSAGE_CACHE_KEY+roomId;
        long start = (page -1) * size;
        long end = start + size -1;

        List<ChatMessage> cachedMessages = redisTemplate.opsForList().range(cacheKey, start, end);

        ChatRoom chatRoom = roomService.findRoom(roomId);
        List<ChatMessage> dbMessages = new ArrayList<>();
        // 캐시된 메세지 수가 요청한 페이지 사이즈보다 적을 경우
        if(cachedMessages.size() < size) {
            // DB에서 가져와야 할 페이지 수
            int dbPage = page - cachedMessages.size()/size;
            Pageable pageable = PageRequest.of(dbPage, size - cachedMessages.size());
            dbMessages = messageRepository.findAllByChatRoomOrderBySendTimeDesc(chatRoom, pageable).getContent();
        }

        List<ChatMessage> allMessages = new ArrayList<>();
        allMessages.addAll(cachedMessages);
        allMessages.addAll(dbMessages);
        Collections.sort(allMessages, Comparator.comparing(ChatMessage::getSendTime));

        int totalElements = allMessages.size();
        int totalPage = (int) Math.ceil(totalElements/size);
        int startIndex = (page -1) * size;
        int endIndex = Math.min(startIndex + size, totalElements);

        List<ChatMessage> pageMessages = allMessages.subList(startIndex, endIndex);
        return new PageImpl<>(pageMessages, PageRequest.of(page, size), totalPage);
    }
}
