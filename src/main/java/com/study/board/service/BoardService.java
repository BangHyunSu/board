package com.study.board.service;

import com.study.board.dto.BoardDTO;
import com.study.board.entity.BoardEntity;
import com.study.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

// DTO -> Entity (Entity Class 에서 바꿔줄꺼다)
//Entity -> DTO (DTO Class 에서 바꿔줄꺼다)
//service 에서는 위처럼 변환하는 작업을 많이 한다
//Entity 클래스는 db 와 직접적으로 연관이 있기때문에 view 에 직접적으로 노출을 꺼려한다 때문에 service 클래스 까지만 오도록 설계했다

@Service
@RequiredArgsConstructor // ???
public class BoardService {
    private final BoardRepository boardRepository;

    public void save(BoardDTO boardDTO) {
        // dto 객체를 entity 에 담아주는 과정????
        BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO); //repository 에 보내려면? entity 타입이여야 하기 때문에
        boardRepository.save(boardEntity); // repository 는 entity 만 받아준다 return 도 동일
    }

    public List<BoardDTO> findAll() {
        // repository 에서 가져올때는 entity 로 온다 list 형태의 entity 가 넘어온것
        List<BoardEntity> boardEntityList = boardRepository.findAll();
        // entity 로 넘어온 객체를 dto 객체로 옮겨 담아서 컨트롤러로 호출해주는것
        List<BoardDTO> boardDTOList = new ArrayList<>();
        //entity 객체를 dto 로 전환을 하고 boardDTOList 에 담아주는 과정
        for (BoardEntity boardEntity: boardEntityList) { //반복문으로 entity 를 꺼내옴
            boardDTOList.add(BoardDTO.toBoardDTO(boardEntity));
        }
        return boardDTOList;
    }
}
