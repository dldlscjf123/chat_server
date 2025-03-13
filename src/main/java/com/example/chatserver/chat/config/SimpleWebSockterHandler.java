//package com.example.chatserver.chat.config;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.web.socket.*;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.Set;
//import java.util.concurrent.ConcurrentHashMap;
//
//// /connect로 웹소켓 연결 요청이 들어왔을 때 이를 처리할 클래스
//@Slf4j
//@Component
//public class SimpleWebSockterHandler extends TextWebSocketHandler {
//    // 연결된 세션 관리 : 스레드 safe한 set을 사용한다
//    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
//    //연결되면 어떻게?
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        sessions.add(session);
//        log.info("Connect session {}", session.getId());
//    }
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
//        String payLoad = message.getPayload();
//        log.info("receive message {}", payLoad);
//        for (WebSocketSession s : sessions) {
//            if(s.isOpen()){
//                s.sendMessage(new TextMessage(payLoad));
//            }
//        }
//    }
//    //연결끝나면 어떻게?
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        sessions.remove(session);
//        log.info("Disconnect session {}", session.getId());
//    }
//
//}
