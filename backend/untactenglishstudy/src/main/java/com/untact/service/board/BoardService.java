package com.untact.service.board;

import org.springframework.data.domain.Page;

import com.untact.board.domain.Board;
import com.untact.board.domain.BoardKind;
import com.untact.member.domain.MemberEntity;
import com.untact.vo.PageVO;

public interface BoardService {
	public Page<Board> getListWithPagingAndGroupNumber(PageVO pageVO,Long gno);
	public Page<Board> getListWithPagingAndGroupNumberAndKind(PageVO pageVO,Long gno, BoardKind kind);
	public Board getOne(Long bno);
	public void addBoard(Board board,Long gno,MemberEntity member);
	public void modifyBoard(Board targetBoard,Long bno);
	public void deleteBoard(Long bno);
}
