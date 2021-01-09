package com.untact.socket;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.untact.member.domain.MemberEntity;

import lombok.extern.java.Log;

@Controller
@Log
public class ChatHandler {
	@MessageMapping("/send/{groupid}")
	@SendTo("/sub/group/{groupid}")
	public ChatResponse broadcasting(ChatRequest vo) throws Exception{
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Date time = new Date();
		String time1 = format1.format(time);
		return new ChatResponse(vo.getMno(),vo.getFrom(),vo.getMsg(),time1,null);
	}
	@MessageMapping("/hello/{groupid}")
	@SendTo("/sub/group/{groupid}")
	public ChatResponse hello(@DestinationVariable String groupid, SimpMessageHeaderAccessor headerAccessor) throws Exception{
		String sessionId = headerAccessor.getSessionId();
		Map<String,MemberEntity> members = SocketDataStructure.memberMap.get("/sub/group/"+groupid);
		MemberEntity member = members.get(sessionId);
		List<MemberEntity> list = new ArrayList<>(members.values());
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Date time = new Date();
		String time1 = format1.format(time);
		return new ChatResponse(0L,"시스템",member.getName()+"님이 입장하십니다.",time1,list);
	}
	
	@MessageMapping("/send/en/{groupid}")
	@SendTo("/sub/group/en/{groupid}")
	public ChatResponse broadcastingEnglish(ChatRequest vo) throws Exception{
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Date time = new Date();
		String time1 = format1.format(time);
		if(Pattern.matches("^[ㄱ-ㅎ가-힣]*$", vo.getMsg())) {
			//문자에 한글이 있다면
			//그전에 클라이언트 쪽에서 확인을 해야만 한다.
			return new ChatResponse(0L,"시스템","한글은 입력할 수 없습니다.",time1,null);
		}else {
			return new ChatResponse(vo.getMno(),vo.getFrom(),vo.getMsg(),time1,null);
		}
		
	}
	
	@MessageMapping("/hello/en/{groupid}")
	@SendTo("/sub/group/en/{groupid}")
	public ChatResponse helloEnglish(@DestinationVariable String groupid, SimpMessageHeaderAccessor headerAccessor) throws Exception{
		String sessionId = headerAccessor.getSessionId();
		Map<String,MemberEntity> members = SocketDataStructure.memberMap.get("/sub/group/en/"+groupid);
		MemberEntity member = members.get(sessionId);
		List<MemberEntity> list = new ArrayList<>(members.values());
		SimpleDateFormat format1 = new SimpleDateFormat ( "yyyy-MM-dd HH:mm:ss");
		Date time = new Date();
		String time1 = format1.format(time);
		return new ChatResponse(0L,"시스템",member.getName()+"님이 입장하십니다.",time1,list);
	}
}
