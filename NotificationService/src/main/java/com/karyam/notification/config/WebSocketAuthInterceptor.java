package com.karyam.notification.config;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import com.karyam.notification.constant.ICommonConstants;
import com.karyam.notification.util.JwtUtil;

@Component
public class WebSocketAuthInterceptor implements ChannelInterceptor {
	
	@Autowired
	private JwtUtil jwtUtil;
    
	@Override
	public Message<?> preSend(Message<?> message, MessageChannel channel) {
	    StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

	    if (StompCommand.CONNECT.equals(accessor.getCommand())) {
	        String authHeader = accessor.getFirstNativeHeader("Authorization");

	        if (authHeader != null && authHeader.startsWith("Bearer ")) {
	            String token = authHeader.substring(7);
	            String userId = jwtUtil.getClaims(token).get(ICommonConstants.USER_ID).toString(); 
	            
	            UsernamePasswordAuthenticationToken user = 
	                new UsernamePasswordAuthenticationToken(userId, null, Collections.emptyList());
	            accessor.setUser(user);
	        }
	    }
	    return message;
	}
}