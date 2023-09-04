package com.study.board.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Table(name = "board_file_table")
public class BoardFileEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String originalFileName;

    @Column
    private String storedFileName;

    //BoardFileEntity 의 입장에서 n 대 1 관계이므로 ManyToOne
    @ManyToOne(fetch = FetchType.LAZY) //약간 공식같은 느낌
    @JoinColumn(name = "board_id") //테이블에 만들어지는 컬럼 이름
    private BoardEntity boardEntity; //String Long 이 아니라 부모 Entity 타입으로 해줘야함

    public static BoardFileEntity toBoardFileEntity(BoardEntity boardEntity, String originalFileName, String storedFileName) {
        BoardFileEntity boardFileEntity =  new BoardFileEntity();
        boardFileEntity.setOriginalFileName(originalFileName);
        boardFileEntity.setStoredFileName(storedFileName);
        boardFileEntity.setBoardEntity(boardEntity);
        return boardFileEntity;
    }
}
