package com.listener;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

/**
 * @author Feinik
 * @Discription 监听客户端是否断开连接
 * @Data 2018/12/28
 * @Version 1.0.0
 */
@Component
public class WebSocketListener {

    @EventListener
    public void onApplicationEvent(SessionDisconnectEvent event) {
        System.out.println("客户端断开连接了：" + event.getUser().getName());
    }
}
