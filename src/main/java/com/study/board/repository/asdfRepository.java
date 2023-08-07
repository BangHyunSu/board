package com.study.board.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface asdfRepository {
    @Modifying //????필수로 붙여주라는데 이게 뭘까
    @Query(value = "update BoardEntity b set b.boardHits=b.boardHits+1 where b.id=:id") //맨 끝에 id는 파람의 id와 맞춰준다

    void updateHits(@Param("id") Long id);
}
