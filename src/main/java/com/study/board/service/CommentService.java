package com.study.board.service;

import com.study.board.dto.CommentDTO;
import com.study.board.entity.BoardEntity;
import com.study.board.entity.CommentEntity;
import com.study.board.repository.BoardRepository;
import com.study.board.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor //?
public class CommentService {
    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;

    public Long save(CommentDTO commentDTO) {
        //부모엔티티(BoardEntity) 조회
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(commentDTO.getBoardId());
        if (optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            CommentEntity commentEntity = CommentEntity.toSaveEntity(commentDTO, boardEntity);
            //entity 객체는 데이터베이스를 다루는 객체기 때문에 외부로 보여지기를 꺼려한다
            //그래서 클래스 메소드 하나로만 보이게? 하는것
            //CommentEntity commentEntity = new CommentEntity(); 이런 형식을 사용하지 않기 위해
            return commentRepository.save(commentEntity).getId();

        } else {
            return null;
        }

    }

    public List<CommentDTO> findAll(Long boardId) {
        // select * from comment_table where board_id=? order by id desc;
        BoardEntity boardEntity = boardRepository.findById(boardId).get();
        List<CommentEntity> commentEntityList = commentRepository.findAllByBoardEntityOrderByIdDesc(boardEntity);
        // EntityList 를 DTOList 로 변환 시켜줘야함
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (CommentEntity commentEntity: commentEntityList) {
            CommentDTO commentDTO = CommentDTO.toCommentDTO(commentEntity, boardId);
            commentDTOList.add(commentDTO);
        }
        return commentDTOList;
    }
}
