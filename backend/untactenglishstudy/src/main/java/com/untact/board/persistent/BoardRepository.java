package com.untact.board.persistent;

import org.springframework.data.jpa.repository.JpaRepository;

import com.untact.board.domain.Board;

public interface BoardRepository extends JpaRepository<Board, Long>,BoardCustomRepository {
}
