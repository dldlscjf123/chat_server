package com.example.chatserver.chat.controller;

import com.example.chatserver.chat.dto.ChatMessageDto;
import com.example.chatserver.chat.service.ChatService;
import com.example.chatserver.chat.service.RedisPubSubService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Controller;



@Controller
@Slf4j
@RequiredArgsConstructor
public class StompController {
    private final SimpMessageSendingOperations messageTemplate;
    private final ChatService chatService;
    private final RedisPubSubService pubSubService;
//     방법 1: messageMapping(수신)과 sendTo(송신) 한꺼번에 처리
//    @MessageMapping("/{roomId}") //클라이언트에서 특정 public/rooomId형태로 메시지를 발행 시 MessageMapping 수신
//    @SendTo("/topic/{roomId}") //해당 룸아이디에 메시지를 발행하여 구독중인 클라이언트에게 메시지 전송
////    @DestinationVariable : @MessageMapping 어노테이션으로 정의된 WebSocket Controller 내에서만 사용된다
//    public String sendMessage(@DestinationVariable Long roomId, String message) {
//        log.info("message:{}", message);
//        return message;
//    }
//    방법 2 : messageMapping어노테이션만 활용
@MessageMapping("/{roomId}")
public void sendMessage(@DestinationVariable Long roomId, ChatMessageDto chatMessageDto) throws JsonProcessingException {
    log.info("message:{}", chatMessageDto.getMessage());
    chatService.saveMessage(roomId, chatMessageDto);
    chatMessageDto.setRoomId(roomId);
   // messageTemplate.convertAndSend("/topic/"+roomId, chatMessageDto);
    ObjectMapper objectMapper = new ObjectMapper();
   String message = objectMapper.writeValueAsString(chatMessageDto);
    pubSubService.publish("chat", message);

}



}
