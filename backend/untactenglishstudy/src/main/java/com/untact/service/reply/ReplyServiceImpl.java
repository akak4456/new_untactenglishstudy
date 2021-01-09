package com.untact.service.reply;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.untact.board.persistent.BoardRepository;
import com.untact.group.persistent.GroupEntityRepository;
import com.untact.member.domain.MemberEntity;
import com.untact.reply.domain.Reply;
import com.untact.reply.persistent.ReplyRepository;
import com.untact.vo.PageVO;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class ReplyServiceImpl implements ReplyService {
	@Autowired
	private ReplyRepository replyRepo;
	
	@Autowired
	private GroupEntityRepository groupEntityRepo;
	
	@Autowired
	private BoardRepository boardRepo;

	@Override
	public Page<Reply> getListWithPagingAndBoardNumber(PageVO pageVO, Long bno) {
		return replyRepo.getPageWithBoardNumber(pageVO.makePageable(1, "rno"), bno);
	}

	@Override
	public void addReply(Reply reply,Long gno,Long bno,MemberEntity member) {
		reply.setGroup(groupEntityRepo.findById(gno).get());
		reply.setBoard(boardRepo.findById(bno).get());
		reply.setMember(member);
		replyRepo.save(reply);
	}

	@Override
	public void modifyReply(Reply targetReply, Long rno) {
		Reply oldReply = replyRepo.findById(rno).get();
		Reply updatedReply = oldReply.modifyThisToTargetReply(targetReply);
		replyRepo.save(updatedReply);
	}

	@Override
	public void deleteReply(Long rno) {
		replyRepo.deleteById(rno);
	}
}
