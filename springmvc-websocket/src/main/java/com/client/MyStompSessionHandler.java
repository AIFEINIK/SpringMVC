package com.client;

import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Feinik
 * @Discription 消息处理
 * @Data 2018/12/28
 * @Version 1.0.0
 */
public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        System.out.println("MyStompSessionHandler.afterConnected");
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        session.send("/topic/notification", "来自stomp客户端的通知消息" + time);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        System.out.println("MyStompSessionHandler.getPayloadType");
        return super.getPayloadType(headers);
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        System.out.println("MyStompSessionHandler.handleFrame");
        super.handleFrame(headers, payload);
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        System.err.println("MyStompSessionHandler.handleException:" + exception.getMessage());
    }

    @Override
    public void handleTransportError(StompSession session, Throwable exception) {
        System.err.println("MyStompSessionHandler.handleTransportError:" + exception.getMessage());
    }
}
