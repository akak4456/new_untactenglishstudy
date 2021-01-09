package com.untact.socket;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.untact.member.domain.MemberEntity;

public class SocketDataStructure {
	public static Map<String,Map<String,MemberEntity>> memberMap = new ConcurrentHashMap<>();
	//destination, member
	public static Map<String,String> sessionIdToDestination = new ConcurrentHashMap<>();
	
	public static Map<String,List<Long>> prizeMap = new ConcurrentHashMap<>();//상금액수
	
	public static Map<String,ProblemSet> groupProblemSet = new ConcurrentHashMap<>();//게임이 진행중인 그룹이 가지고 있는 문제들
	
	public static Map<String,Map<String,Long>> gameResult = new ConcurrentHashMap<>();
	//destination,(sessionId,correct count)
}
