package com.config;

import com.interceptor.CustomerChannelInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptorAdapter;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketTransportRegistration;
import org.springframework.web.socket.handler.WebSocketHandlerDecoratorFactory;

import java.security.Principal;

/**
 * @author Feinik
 * @Discription WebSocket配置
 * @Data 2018/12/25
 * @Version 1.0.0
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        //注册一个STOMP端点，connectPoint表示客户端在消息发布或订阅前需先建立与该端点的连接，withSockJS 表示启用SockJS
        registry.addEndpoint("connectPoint").withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //启用一个简单的基于内存的消息代理，一般开发测试中会使用该代理，生产中建议配置外部消息代理
        registry.enableSimpleBroker("/queue", "/topic", "/user");

        //基于RabbitMQ的消息代理
        /*registry.enableStompBrokerRelay("/queue", "/topic")
                .setRelayHost("host")
                .setRelayPort(port)
                .setClientLogin("name")
                .setClientPasscode("pwd");*/

        registry.setApplicationDestinationPrefixes("/app");
    }

    /**
     * 身份认证
     * @param registration
     */
    /*@Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        super.configureClientInboundChannel(registration);
        registration.interceptors(new ChannelInterceptorAdapter() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                //1、判断是否首次连接
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    //2、判断用户名和密码,客户端连接时需配置StompHeader参数
                    String username = accessor.getNativeHeader("username").get(0);
                    String password = accessor.getNativeHeader("password").get(0);
                    if ("admin".equals(username) && "admin".equals(password)){
                        Principal principal = new Principal() {
                            @Override
                            public String getName() {
                                return username;
                            }
                        };
                        accessor.setUser(principal);
                        return message;
                    }else {
                        return null;
                    }
                }
                //不是首次连接，已经登陆成功
                return message;
            }
        });
    }*/

    @Override
    public void configureClientOutboundChannel(ChannelRegistration registration) {
        //拦截器配置
        registration.interceptors(channelInterceptor());
    }

    @Bean
    public CustomerChannelInterceptor channelInterceptor() {
        return new CustomerChannelInterceptor();
    }
}
