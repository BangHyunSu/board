package com.study.board.entity;

import com.fasterxml.jackson.databind.ser.Serializers;
import com.study.board.dto.BoardDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "board_table") //여기서 정의한대로 테이블 생성된다 별도로 db에 테이블 만들지 않아도 된다
public class BoardEntity extends BaseEntity { // DB의 테이블 역할을 하는 클래스 jpa 에서 필수
    @Id //pk 컬럼 지정 필수다
    @GeneratedValue(strategy = GenerationType.IDENTITY) //auto_increment
    private Long id;

    @Column(length = 20, nullable = false) // 길이 20 null 이면 안된다
    private String boardWriter;

    @Column // 디폴트 값이면 크기 255, null 가능
    private String boardPass;

    @Column
    private String boardTitle;

    @Column(length = 500)
    private String boardContents;

    @Column
    private int boardHits;

    public static BoardEntity toSaveEntity(BoardDTO boardDTO) {
        //save.html 에서 입력한 값을 dto 에 담아온 뒤 그 값을 entity 에 담아주는 작업
        BoardEntity boardEntity = new BoardEntity();
        boardEntity.setBoardWriter(boardDTO.getBoardWriter());
        boardEntity.setBoardPass(boardDTO.getBoardPass());
        boardEntity.setBoardTitle(boardDTO.getBoardTitle());
        boardEntity.setBoardContents(boardDTO.getBoardContents());
        boardEntity.setBoardHits(0);
        return boardEntity;
    }

}
