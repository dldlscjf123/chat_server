/*package com.example.chatserver.chat.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final SimpleWebSockterHandler simpleWebSockterHandler;
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // /connect url로 websocket연결이 들어오면, 핸들러 클래스가 처리한다
        registry.addHandler(simpleWebSockterHandler, "/connect")
        // securityconfig에서의 cors예외는 http요청에 대한 예외. 따라서 websocket프로토콜에 대한 예외는 별도의 cors설정이 필요하다
                .setAllowedOrigins("http://localhost:3000");
    }
}

 */
