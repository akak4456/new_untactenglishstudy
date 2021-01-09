package com.untact.socket;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import com.untact.member.domain.MemberEntity;
import com.untact.security.JwtTokenProvider;
import com.untact.service.quiz.QuizService;
@Component
public class StompHandler implements ChannelInterceptor {
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private SimpMessagingTemplate messagingTemplate;
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
    	StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
    	if(StompCommand.SUBSCRIBE == accessor.getCommand()) {
    		Authentication authentication = jwtTokenProvider.getAuthentication(accessor.getFirstNativeHeader("X-AUTH-TOKEN"));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            accessor.setUser(authentication);
            MemberEntity member = ((MemberEntity)authentication.getPrincipal()).copy();
            String sessionId = accessor.getSessionId();
            String destination = accessor.getDestination();
            SocketDataStructure.memberMap.computeIfAbsent(destination, memSet->new ConcurrentHashMap<>()).put(sessionId,member);
            SocketDataStructure.sessionIdToDestination.put(sessionId, destination);
    	}else if(StompCommand.UNSUBSCRIBE == accessor.getCommand()||StompCommand.DISCONNECT == accessor.getCommand()) {
    		//memberMap.get(accessor.getDestination()).remove(accessor.getSessionId());
    		String sessionId = accessor.getSessionId();
    		String destination = SocketDataStructure.sessionIdToDestination.get(sessionId);
    		//시스템 메시지 전송
    		MemberEntity member = SocketDataStructure.memberMap.get(destination).get(sessionId);
    		SocketDataStructure.memberMap.get(destination).remove(sessionId);
    		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
    		Date time = new Date();
    		String time1 = format1.format(time);
    		if(destination.contains("/sub/group")) {
    			List<MemberEntity> list = new ArrayList<>(SocketDataStructure.memberMap.get(destination).values());
    			messagingTemplate.convertAndSend(destination, new ChatResponse(0L,"시스템",member.getName()+"님이 퇴장하십니다.",time1,list));
    		}else if(destination.contains("/sub/rank")) {
    			List<MemberEntity> list = new ArrayList<>(SocketDataStructure.memberMap.get(destination).values());
    			messagingTemplate.convertAndSend(destination, new RankWaitingResponse("disconnect",list,0L));
    		}
    	}
    	return message;
    }
}
