package com.example.chatserver.chat.config;


import com.example.chatserver.chat.service.ChatService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Component;

import javax.naming.AuthenticationException;


@Component
@Slf4j

public class StompHandler implements ChannelInterceptor {
    private final ChatService chatService;
    public StompHandler(ChatService chatService) {
        this.chatService = chatService;
    }

    @Value("${jwt.secretKey}")
    private String secretKey;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if (StompCommand.CONNECT == accessor.getCommand()) {
            String bearerToken = accessor.getFirstNativeHeader("Authorization");

            if (bearerToken == null || !bearerToken.startsWith("Bearer ")) {
                log.error("Authorization 헤더 없음 또는 형식 불일치");
                throw new SecurityException("Authorization header is missing or malformed");
            }

            String token = bearerToken.substring(7);

            try {
                // JWT 검증
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();

                log.info("token 검증 완료");
            } catch (Exception e) {
                log.error("JWT 검증 실패: {}", e.getMessage());
                throw new SecurityException("Invalid JWT token");
            }
        }
        if(StompCommand.SUBSCRIBE == accessor.getCommand()){
            String bearerToken = accessor.getFirstNativeHeader("Authorization");
            String token = bearerToken.substring(7);
            try {
                // JWT 검증
               Claims claims =  Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token)
                        .getBody();
               String email = claims.getSubject();
               String roomId = accessor.getDestination().split("/")[2];
               if(!chatService.isRoomParticipant(email, Long.parseLong(roomId))){
                   throw new AuthenticationServiceException("해당 room에 권한이 없습니다.");
               }


                log.info("subscribe 검증 완료");
            } catch (Exception e) {
                log.error("JWT 검증 실패: {}", e.getMessage());
                throw new SecurityException("Invalid JWT token");
            }
        }

        return message;
    }



}
