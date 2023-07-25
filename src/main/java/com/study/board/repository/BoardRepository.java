package com.study.board.repository;

import com.study.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;

//repository 는 entity 클래스만 받아준다
public interface BoardRepository extends JpaRepository<BoardEntity, Long> { //entity 이름과 entity pk의 타입
}
