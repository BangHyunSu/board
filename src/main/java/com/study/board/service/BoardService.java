package com.study.board.service;

import com.study.board.dto.BoardDTO;
import com.study.board.entity.BoardEntity;
import com.study.board.entity.BoardFileEntity;
import com.study.board.repository.BoardFIleRepository;
import com.study.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

// DTO -> Entity (Entity Class 에서 바꿔줄꺼다)
//Entity -> DTO (DTO Class 에서 바꿔줄꺼다)
//service 에서는 위처럼 변환하는 작업을 많이 한다
//Entity 클래스는 db 와 직접적으로 연관이 있기때문에 view 에 직접적으로 노출을 꺼려한다 때문에 service 클래스 까지만 오도록 설계했다

@Service
@RequiredArgsConstructor // ???
public class BoardService {
    private final BoardRepository boardRepository;

    private final BoardFIleRepository boardFIleRepository;

    public void save(BoardDTO boardDTO) throws IOException {
        // 파일 첨부 여부에 따라 로직 분리

        // dto 객체를 entity 에 담아주는 과정????
        if(boardDTO.getBoardFile().isEmpty()) {
            //첨부 파일 없을때
            BoardEntity boardEntity = BoardEntity.toSaveEntity(boardDTO); //repository 에 보내려면? entity 타입이여야 하기 때문에
            boardRepository.save(boardEntity); // repository 는 entity 만 받아준다 return 도 동일
        } else {
            //첨부 파일 있을때
            /*
                1. DTO 에 담긴 파일을 꺼냄
                2. 파일의 이름을 가져옴
                3. 서버 저장용 이름을 만듦
                //내사진.jpg => 93546543215654_내사진.jpg
                4. 저장 경로 설정
                5. 해당 경로에 파일 저장
                6. board_table 에 해당 데이터 save 처리
                7. board_file_table 에 해당 데이터 save 처리
             */
            BoardEntity boardEntity = BoardEntity.toSaveFileEntity(boardDTO);
            Long savedId = boardRepository.save(boardEntity).getId();
            BoardEntity board = boardRepository.findById(savedId).get();
            for (MultipartFile boardFile: boardDTO.getBoardFile()) {
                //파일 하나만 할때는 밑에꺼가 파일을 꺼내주는? 역할을 했었는데
                //파일이 여러개일 경우에는 for 문으로 돌리기 때문에 밑에 코드가  필요 없어짐
//                MultipartFile boardFile = boardDTO.getBoardFile(); // 1.
                String originalFilename = boardFile.getOriginalFilename(); //2.
                String storedFileName = System.currentTimeMillis() + "_" + originalFilename; //3. 되게 긴 값을 앞에 붙여주는것 겹치면 안되니까
                String savePath = "C:/springboot_img/" + storedFileName; // 4.  C:/springboot_img/981654895_내사진.jpg
                boardFile.transferTo(new File(savePath)); // 5.  //예외처리 필요 controller 쪽도


                BoardFileEntity boardFileEntity = BoardFileEntity.toBoardFileEntity(board, originalFilename, storedFileName);
                boardFIleRepository.save(boardFileEntity);
            }
        }

    }
    @Transactional
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
    @Transactional //수동적인 쿼리를 처리하기 위해 붙여줘야한다 (영속성???)
    public void updateHits(Long id) {
        boardRepository.updateHits(id);
    }

    @Transactional
    //findById 에서 toBoardDTO 를 호출하고 있다 이때 toBoardDTO 안에서 boardEntity 가 boardFileEntity를 접근하고 있기 때문에 붙어줘야함
    public BoardDTO findById(Long id) {
        Optional<BoardEntity> optionalBoardEntity = boardRepository.findById(id);
        //Optional 객체로 넘어왔기 때운에 boardDTO 로 변환해주는것
        if(optionalBoardEntity.isPresent()) {
            BoardEntity boardEntity = optionalBoardEntity.get();
            BoardDTO boardDTO = BoardDTO.toBoardDTO(boardEntity);
            return boardDTO;
        } else {
            return null;
        }
    }

    public BoardDTO update(BoardDTO boardDTO) {
        //entity 로 변환해주는 작업
        BoardEntity boardEntity = BoardEntity.toUpdateEntity(boardDTO);

        //jpa 는 save 를 가지고 insert 와 update 둘 다 가능한데 차이점은 update 는 id 값이 존재한다
        boardRepository.save(boardEntity);
        return findById(boardDTO.getId()); //다시 옵셔널로 까서 막 해줘야하는걸 그냥 findById 가 어차피 해주니까 써줌
    }

    public void delete(Long id) {
        boardRepository.deleteById(id);
    }

    public Page<BoardDTO> paging(Pageable pageable) {
        int page = pageable.getPageNumber() -1;
        // 몇개씩 보여줄지를 사용자의 설정으로 바꾸려면 3이라는 값을 파라미터로 받아온다
        int pageLimit = 3; // 한 페이지에 보여줄 글 갯수
        // 한페이지당 3개씩 글을 보여주고 정렬 기준은 id 기준으로 내림차순 정렬
        // page 위치에 있는 값은 0부터 시작해서 1을 빼준다
        Page<BoardEntity> boardEntities = //findAll 을 호출하고 몇 페이지를 볼건지(Page), 한페이지에 보이는 글(pageLimit), 어떻게 정렬해서 가져올거냐(Sort ~~)
                boardRepository.findAll(PageRequest.of(page , pageLimit, Sort.by(Sort.Direction.DESC,"id"))); // id는 entity 기준

        System.out.println("boardEntities.getContent() = " + boardEntities.getContent()); // 요청 페이지에 해당하는 글
        System.out.println("boardEntities.getTotalElements() = " + boardEntities.getTotalElements()); // 전체 글갯수
        System.out.println("boardEntities.getNumber() = " + boardEntities.getNumber()); // DB로 요청한 페이지 번호
        System.out.println("boardEntities.getTotalPages() = " + boardEntities.getTotalPages()); // 전체 페이지 갯수
        System.out.println("boardEntities.getSize() = " + boardEntities.getSize()); // 한 페이지에 보여지는 글 갯수
        System.out.println("boardEntities.hasPrevious() = " + boardEntities.hasPrevious()); // 이전 페이지 존재 여부
        System.out.println("boardEntities.isFirst() = " + boardEntities.isFirst()); // 첫 페이지 여부
        System.out.println("boardEntities.isLast() = " + boardEntities.isLast()); // 마지막 페이지 여부

        // 목록: id, writer, title, hits, createTime 맵을 이용해서 entity 를 dto 로 바꿔주는 것
        Page<BoardDTO> boardDTOS = boardEntities.map(board -> new BoardDTO(board.getId(), board.getBoardWriter(), board.getBoardTitle(),board.getBoardHits(),board.getCreatedTime()));
        return boardDTOS;
    }
}
