package com.client;

import javafx.scene.chart.ScatterChart;
import org.springframework.messaging.converter.SimpleMessageConverter;
import org.springframework.messaging.converter.StringMessageConverter;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.WebSocketHttpHeaders;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;
import org.springframework.web.socket.messaging.WebSocketStompClient;
import org.springframework.web.socket.sockjs.client.RestTemplateXhrTransport;
import org.springframework.web.socket.sockjs.client.SockJsClient;
import org.springframework.web.socket.sockjs.client.Transport;
import org.springframework.web.socket.sockjs.client.WebSocketTransport;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Feinik
 * @Discription stomp客户端连接
 * @Data 2018/12/28
 * @Version 1.0.0
 */
public class WebSocketClientTest {
    public static void main(String[] args) throws InterruptedException {
        List<Transport> transports = new ArrayList<>(2);
        //指定WebSocket的初始化请求对象，也可使用JettyWebSocketClient
        transports.add(new WebSocketTransport(new StandardWebSocketClient()));
        //指定使用Spring的RestTemplate来完成HTTP请求
        transports.add(new RestTemplateXhrTransport());
        WebSocketClient socketJsClient = new SockJsClient(transports);

        //用WebSocketStompClient来包装SockJsClient
        WebSocketStompClient stompClient = new WebSocketStompClient(socketJsClient);
        //指定消息转换器
        stompClient.setMessageConverter(new StringMessageConverter());

        //配置线程池，用于客户端与服务器端的心跳检测，默认心跳为10秒，如果该客户端作为模拟大量并发请求来测试
        //建议关闭心跳检测
        ThreadPoolTaskScheduler scheduler = new ThreadPoolTaskScheduler();
        scheduler.setPoolSize(10);
        stompClient.setTaskScheduler(scheduler);
        //关闭客户端心跳检测
        //stompClient.setDefaultHeartbeat(new long[]{0,0});

        WebSocketHttpHeaders httpHeaders = new WebSocketHttpHeaders();
        //配置StompHeader用于WebSocket配置中configureClientInboundChannel连接身份认证，如果未开启身份认证功能，那么不需要该配置
        StompHeaders stompHeaders = new StompHeaders();
        stompHeaders.add("username", "admin");
        stompHeaders.add("password", "admin");

        String url = "http://127.0.0.1:9002/connectPoint";
        stompClient.connect(url, httpHeaders, stompHeaders, new MyStompSessionHandler());

        Thread.currentThread().join();
    }
}
