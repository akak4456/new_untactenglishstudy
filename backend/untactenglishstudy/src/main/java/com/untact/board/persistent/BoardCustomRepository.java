package com.untact.board.persistent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.untact.board.domain.Board;
import com.untact.board.domain.BoardKind;

public interface BoardCustomRepository {
	public Page<Board> getPageWithGroupNumber(Pageable pageable, Long gno);
	public Page<Board> getPageWithGroupNumberAndKind(Pageable pageable,Long gno,BoardKind kind);
}
