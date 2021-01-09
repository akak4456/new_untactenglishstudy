package com.untact.board.persistent;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.untact.board.domain.Board;
import com.untact.file.domain.FileEntity;

public interface BoardRepository extends JpaRepository<Board, Long>,BoardCustomRepository {
}
