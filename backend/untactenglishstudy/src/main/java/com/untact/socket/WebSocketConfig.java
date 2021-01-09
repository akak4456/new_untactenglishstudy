package com.untact.socket;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
/*
 테스트 할때
 URL은 http://localhost:8888/endpoint
 Connect Type은 SockJS, STOMP 둘다 체크
 구독은 /sub/{groupoid}
 헤더는 적절하게 선택
 destination은 /pub/...으로
 */
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
	@Autowired
	private StompHandler stompHandler;
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/endpoint").setAllowedOrigins("*").withSockJS();
	}
	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		/*
		 Configure the message converters to use when extracting the payload of messages in 
		 annotated methods and when sending messages (e.g. through the "broker" SimpMessagingTemplate).
		한 클라이언트에서 다른 클라이언트로 메시지를 라우팅 하는 데 사용될 메시지 브로커를 구성함
		 */
		registry.setApplicationDestinationPrefixes("/pub");
		registry.enableSimpleBroker("/sub");
	}
	
	@Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
    
}
