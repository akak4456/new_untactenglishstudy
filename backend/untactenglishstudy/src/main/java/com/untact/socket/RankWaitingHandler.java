package com.untact.socket;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

import com.untact.groupinclude.domain.WhichStatus;
import com.untact.groupinclude.persistent.GroupIncludeRepository;
import com.untact.member.domain.MemberEntity;
import com.untact.security.JwtTokenProvider;
import com.untact.service.group.GroupService;
import com.untact.service.groupinclude.GroupIncludeService;

import lombok.extern.java.Log;

@Controller
@Log
public class RankWaitingHandler {
	private final int MINIMUM_PEOPLE = 3;
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	@Autowired
	private GroupService groupService;
	@Autowired
	private GroupIncludeService groupIncludeService;
	@Autowired
	private GroupIncludeRepository groupIncludeRepo;
	@MessageMapping("/hellorank/{groupid}")
	@SendTo("/sub/rank/{groupid}")
	public RankWaitingResponse hellorank(@DestinationVariable String groupid, SimpMessageHeaderAccessor headerAccessor) throws Exception{
		String sessionId = headerAccessor.getSessionId();
		Map<String,MemberEntity> members = SocketDataStructure.memberMap.get("/sub/rank/"+groupid);
		MemberEntity member = members.get(sessionId);
		List<MemberEntity> list = new ArrayList<>(members.values());
		return new RankWaitingResponse("list",list,groupService.getGroupMemberCount(Long.parseLong(groupid)));
	}
	
	@MessageMapping("/rankstarttry/{groupid}")
	@SendTo("/sub/rank/{groupid}")
	public RankWaitingResponse rankstarttry(@DestinationVariable String groupid, SimpMessageHeaderAccessor headerAccessor) throws Exception{
		Authentication authentication = jwtTokenProvider.getAuthentication(headerAccessor.getFirstNativeHeader("X-AUTH-TOKEN"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        MemberEntity member = ((MemberEntity)authentication.getPrincipal()).copy();
        if(canStart(groupid,member)) {
        	return new RankWaitingResponse("try",member.getMno(),"Y");
        }else {
        	return new RankWaitingResponse("try",member.getMno(),"N");
        }
	}
	
	@MessageMapping("/rankstart/{groupid}")
	@SendTo("/sub/rank/{groupid}")
	public RankWaitingResponse rankstart(RankWaitingRequest request,@DestinationVariable String groupid, SimpMessageHeaderAccessor headerAccessor) throws Exception{
		Authentication authentication = jwtTokenProvider.getAuthentication(headerAccessor.getFirstNativeHeader("X-AUTH-TOKEN"));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        MemberEntity member = ((MemberEntity)authentication.getPrincipal()).copy();
        if(canStart(groupid,member)) {
        	if(request.getFirst_prize() == null || request.getFirst_prize() < 0) {
        		return new RankWaitingResponse("start",member.getMno(),"N");
        	}
        	if(request.getSecond_prize() == null ||request.getSecond_prize() < 0) {
        		return new RankWaitingResponse("start",member.getMno(),"N");
        	}
        	if(request.getThird_prize() == null ||request.getThird_prize() < 0) {
        		return new RankWaitingResponse("start",member.getMno(),"N");
        	}
        	Long prizeSum = request.getFirst_prize()+request.getSecond_prize()+request.getThird_prize();
        	Long gno = Long.parseLong(groupid);
        	Long totalFine = groupIncludeRepo.findSumOfFineByGroupNumber(gno, Set.of(WhichStatus.LEADER,WhichStatus.FOLLOWER));
    		Long totalReward = groupIncludeRepo.findSumOfRewardByGroupNumber(gno, Set.of(WhichStatus.LEADER,WhichStatus.FOLLOWER));
    		if(prizeSum > totalFine-totalReward) {
    			return new RankWaitingResponse("start",member.getMno(),"N");
    		}
    		List<Long> priceList = new ArrayList<>();
    		priceList.add(request.getFirst_prize());
    		priceList.add(request.getSecond_prize());
    		priceList.add(request.getThird_prize());
    		SocketDataStructure.prizeMap.putIfAbsent("/sub/rankgame/"+groupid, priceList);
    		return new RankWaitingResponse("start",0L,"Y");
        }else {
        	return new RankWaitingResponse("start",member.getMno(),"N");
        }
	}
	public boolean canStart(String groupid,MemberEntity member) {
		if(SocketDataStructure.memberMap.get("/sub/rank/"+groupid).values().size() < MINIMUM_PEOPLE) {
			//최소인원보다 적으면
			return false;
		}
		if(!groupIncludeService.isLeader(Long.parseLong(groupid), member)) {
			//리더가 아니라면
			return false;
		}
		return true;
	}
}
