package training.ws.handler;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import training.ws.ChatMessage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class MyHandler extends TextWebSocketHandler {
    private Map<String, WebSocketSession> sessions = new HashMap<>();
    private Gson gson = new Gson();

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        super.handleMessage(session, message);
        log.info("Thread: {} - Message: {}", Thread.currentThread().getName(), message.getPayload().toString());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        super.afterConnectionEstablished(session);

        String sessionId = session.getId();
        sessions.put(sessionId, session); // 세션 저장

        log.info("Thread: {} - Session established: {}", Thread.currentThread().getName(), sessionId);
        log.info("sessionList Size: {}", sessions.size());
        for (WebSocketSession s : sessions.values()) {
            try {
                s.sendMessage(new TextMessage("새로운 유저 접속 [현재:" + sessions.size() +"명]"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        super.handleTextMessage(session, message);

        String payload = message.getPayload();
        log.info("Thread: {} - Received text message: {}", Thread.currentThread().getName(), payload);

        ChatMessage chatMessage = gson.fromJson(payload, ChatMessage.class);
        String formattedMessage = chatMessage.getName() + ": " + chatMessage.getContent();

        for (WebSocketSession s : sessions.values()) {
            try {
                s.sendMessage(new TextMessage(formattedMessage));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        super.afterConnectionClosed(session, status);

        String sessionId = session.getId();
        sessions.remove(sessionId);

        log.info("Thread: {} - Session closed: {}", Thread.currentThread().getName(), sessionId);

        for (WebSocketSession s : sessions.values()) {
            try {
                s.sendMessage(new TextMessage("유저 연결 해제 [현재:" + sessions.size() +"명]"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
