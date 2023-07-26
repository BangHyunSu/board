package com.study.board.repository;

import com.study.board.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

//repository 는 entity 클래스만 받아준다

public interface BoardRepository extends JpaRepository<BoardEntity, Long> { //entity 이름과 entity pk의 타입
    //update board_table set board_hits = board_hits + 1 where id = ?

    //위에꺼는 entity 를 기준으로 쿼리를 작성한것 테이블이 올 자리에 entity 를 써주고 약어를 b라고 지정해준다



    @Modifying //????필수로 붙여주라는데 이게 뭘까
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id") //맨 끝에 id는 파람의 id와 맞춰준다

    void updateHits(@Param("id") Long id);
}
