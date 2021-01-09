package com.untact.reply.persistent;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.untact.reply.domain.Reply;

public interface ReplyCustomRepository {
	public Page<Reply> getPageWithBoardNumber(Pageable pageable,Long bno);
}
