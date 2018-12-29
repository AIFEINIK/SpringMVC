package com.controller;

import com.bean.CustomerMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageExceptionHandler;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.annotation.SendToUser;
import org.springframework.messaging.simp.annotation.SubscribeMapping;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Feinik
 * @Discription
 * @Data 2018/12/25
 * @Version 1.0.0
 */
@RestController
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @SubscribeMapping("/init")
    public CustomerMessage init() {
        System.out.println("WebSocketController.init");
        CustomerMessage response = new CustomerMessage();
        response.setMsg("系统初始化消息");
        response.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        return response;
    }

    @MessageMapping("/getNotifications")
    @SendTo(value = "/topic/notification")
    public String notification() {
        System.out.println("WebSocketController.notification");
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        return new String("系统广播消息：号外、号外..." + time);
    }

    @MessageMapping("/getUserInfo")
    public void getUserInfo(CustomerMessage customerMessage) {
        System.out.println("WebSocketController.getUserInfo");
        Map<String, Object> map = new HashMap<>(1);
        map.put(MessageHeaders.CONTENT_TYPE, MimeTypeUtils.APPLICATION_JSON);

        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        CustomerMessage resp = new CustomerMessage(customerMessage.getUserName(), "用户详情信息", time);
        messagingTemplate.convertAndSendToUser(customerMessage.getUserName(), "/queue/userInfo", resp, map);
    }

    @MessageMapping("/errorTest")
    public void errorTest(CustomerMessage message) {
        System.out.println("WebSocketController.errorTest:" + message.getUserName());
        System.out.println(1/0);
    }

    /**
     * 可将@MessageMapping、@SubscribeMapping等注解的方法中的参数
     * 带入@MessageExceptionHandler注解的方法中
     * @param exception
     * @param message
     * @return
     */
    @MessageExceptionHandler
    public void error(Throwable exception, CustomerMessage message) {
        //如果提供了身份认证实现，可以通过在此方法上注解@SendToUser("/queue/error")即可
        messagingTemplate.convertAndSendToUser(message.getUserName(), "/queue/error", exception.getMessage());
    }
}
