package com.untact.socket;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

@Configuration
public class SocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {
	@Override 
	protected void configureInbound(
	  MessageSecurityMetadataSourceRegistry messages) { 
	    messages
	    	.anyMessage().permitAll(); 
	}
	
	@Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}
