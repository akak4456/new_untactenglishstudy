package com.untact.socket;

import static java.util.Comparator.comparing;
import static java.util.Comparator.reverseOrder;
import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;

import com.untact.groupinclude.domain.GroupInclude;
import com.untact.groupinclude.persistent.GroupIncludeRepository;
import com.untact.member.domain.MemberEntity;
import com.untact.service.quiz.QuizService;
import com.untact.workbook.domain.Workbook;

import lombok.extern.java.Log;

@Controller
@Log
public class RankGameHandler {
	public static final Long QUIZ_COUNT = 50L;
	@Autowired
	private QuizService quizService;
	@Autowired
	private GroupIncludeRepository groupIncludeRepo;
	
	@MessageMapping("/hellogame/{groupid}")
	@SendTo("/sub/rankgame/{groupid}")
	public RankGameResponse hellogame(@DestinationVariable String groupid, SimpMessageHeaderAccessor headerAccessor) throws Exception{
    	String destination = "/sub/rankgame/"+groupid;
    	if(!SocketDataStructure.groupProblemSet.containsKey(destination)) {
    		List<Workbook> workbook = quizService.generateRandomQuiz(RankGameHandler.QUIZ_COUNT);
    		List<Problem> vo = new ArrayList<>();
    		for(Workbook w:workbook) {
    			vo.add(new Problem(w.getKind().toString(),w.getQuestion(),w.getAns1(),w.getAns2(),w.getAns3(),w.getAns4(),w.getEnglishSpelling().getSpelling()));
    		}
    		ProblemSet set = new ProblemSet(LocalDateTime.now(),vo);
    		SocketDataStructure.groupProblemSet.putIfAbsent(destination, set);//문제생성
    	}
		return new RankGameResponse("init",SocketDataStructure.groupProblemSet.get(destination));
	}
	
	@MessageMapping("/finishgame/{groupid}")
	@SendTo("/sub/rankgame/{groupid}")
	public RankGameResponse finishgame(@DestinationVariable String groupid,RankGameRequest request,SimpMessageHeaderAccessor headerAccessor) {
		String destination = "/sub/rankgame/"+groupid;
		String sessionId = headerAccessor.getSessionId();
		List<Problem> problems = SocketDataStructure.groupProblemSet.get(destination).getProblems();
		Long correctcount = 0L;
		for(int i=0;i<problems.size();i++) {
			if(request.getUserAnswer().get(i).equals(problems.get(i).getAns1())) {
				//유저가 낸 정답과 실제 정답이 같다면
				correctcount++;
			}
		}
		SocketDataStructure.gameResult.computeIfAbsent(destination, map->new ConcurrentHashMap<>()).put(sessionId,correctcount);
		String first = "없음";
		String second = "없음";
		String third = "없음";
		//게임에 남은 사람이 2명이하일 수도 있다. 그럴 때 없음을 표시해야만 한다
		if(SocketDataStructure.gameResult.get(destination).size() >= SocketDataStructure.memberMap.get(destination).size()) {
			//남아 있는 인원하고 결과를 모은 사람의 수가 같다면 결과를 집계할 수 있다.
			Map<String,Long> resultMap =  SocketDataStructure.gameResult.get(destination);
			List<Entry<String,Long>> top3 = resultMap.entrySet().stream()
												.sorted(comparing(Entry::getValue,reverseOrder()))
												.limit(3)
												.collect(toList());
			Map<String,MemberEntity> memberMap = SocketDataStructure.memberMap.get(destination);
			int sz = SocketDataStructure.gameResult.get(destination).size();
			List<MemberEntity> memberList = new ArrayList<>();
			for(int i=0;i<Math.min(sz,3);i++) {
				memberList.add(memberMap.get(top3.get(i).getKey()));
			}
			Long gno = Long.parseLong(groupid);
			List<Long> prizeList = SocketDataStructure.prizeMap.get(destination);
			Long prize1 = prizeList.get(0);
			Long prize2 = prizeList.get(1);
			Long prize3 = prizeList.get(2);
			if(memberList.size() >= 1) {
				first = memberList.get(0).getName();
				GroupInclude include1 = groupIncludeRepo.findByGroupNumberAndMemberNumber(gno, memberList.get(0).getMno()).get();
				include1.addReward(prize1);
				groupIncludeRepo.save(include1);
			}
			if(memberList.size() >= 2) {
				second = memberList.get(1).getName();
				GroupInclude include2 = groupIncludeRepo.findByGroupNumberAndMemberNumber(gno, memberList.get(1).getMno()).get();
				include2.addReward(prize2);
				groupIncludeRepo.save(include2);
			}
			if(memberList.size() >= 3) {
				third = memberList.get(2).getName();
				GroupInclude include3 = groupIncludeRepo.findByGroupNumberAndMemberNumber(gno, memberList.get(2).getMno()).get();
				include3.addReward(prize3);
				groupIncludeRepo.save(include3);
			}
			
			//상금 업데이트 후에는 게임을 종료해야 함
			
			SocketDataStructure.prizeMap.remove(destination);
			SocketDataStructure.groupProblemSet.remove(destination);
			SocketDataStructure.gameResult.remove(destination);
			return new RankGameResponse("finish",first,second,third,prize1,prize2,prize3);
		}else {
			return new RankGameResponse("wait");
		}
	}
}
