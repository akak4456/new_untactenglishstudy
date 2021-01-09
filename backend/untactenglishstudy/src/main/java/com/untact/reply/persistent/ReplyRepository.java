package com.untact.reply.persistent;

import org.springframework.data.jpa.repository.JpaRepository;

import com.untact.reply.domain.Reply;

public interface ReplyRepository extends JpaRepository<Reply, Long>, ReplyCustomRepository {

}
