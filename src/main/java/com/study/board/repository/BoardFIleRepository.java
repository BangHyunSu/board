package com.study.board.repository;

import com.study.board.entity.BoardFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardFIleRepository extends JpaRepository<BoardFileEntity, Long> {

}
