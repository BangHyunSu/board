package com.study.board.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.study.board.dto.BoardDTO;
import com.study.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequiredArgsConstructor // ???
@RequestMapping("/board") // 먼저 /board 를 호출한다
public class BoardController {
    private final BoardService boardService; // 어캐했누

    @GetMapping("/save")
    public String saveForm() {

        return "save";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute BoardDTO boardDTO) { //dto 의 이름과 save 에 name 이 같으면 자동으로 불러와준다
        System.out.println("boardDTO = " + boardDTO);
        boardService.save(boardDTO);
        return "index";
    }

    @GetMapping("/list") // DB 에서 전체 게시글 데이터를 가져와서 list 에 보여준다
    public String findAll(Model model) {
        //여러개를 가져가니까 list 에 담는다 BoardDTO 가 담겨져 있는 list
        List<BoardDTO> boardDTOList = boardService.findAll();
        model.addAttribute("boardList" , boardDTOList);
        return "list";
    }

    @GetMapping("/{id}") //경로상에 있는 걸 가져오려면 @PathVariable 사용  페이징처리까지 완료
    public String findById(@PathVariable Long id, Model model,
                           @PageableDefault (page=1) Pageable pageable) {
        /*
            해당 게시글의 조회수를 하나 올리고
            게시글 데이터를 가져와서 detail.html 에 출력해야한다
         */
        boardService.updateHits(id);
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("board" , boardDTO);
        model.addAttribute("page" , pageable.getPageNumber());
        return "detail";
    }

    @GetMapping("/update/{id}")
    public String updateForm(@PathVariable Long id, Model model) {
        BoardDTO boardDTO = boardService.findById(id);
        model.addAttribute("boardUpdate" , boardDTO);
        return "update";
    }

    @PostMapping("update") //  update 를 하면 hit 값이 0이 된다 고쳐보고 싶음
    public String update(@ModelAttribute BoardDTO boardDTO, Model model) { //ModelAttribute ???
        BoardDTO board = boardService.update(boardDTO);
        model.addAttribute("board", board);
        return "detail";
        // return "redirect:/board/" + boardDTO.getId(); 이렇게 다시 상세 페이지로 가서 보여줄 수 있지만 이렇게 되면 조회수가 영향을 받아서 그냥 목록으로 가게 해줌

    }

    @GetMapping("delete/{id}")
    public String delete(@PathVariable Long id) {
        boardService.delete(id);
        return "redirect:/board/list";
    }

    // /board/paging?page = 1
    @GetMapping("/paging") //index 에서 페이지 번호를 따로 언급 안해서 처음은 1페이지로 하겠다는 뜻   //spring 에서 지원하는 pageable 로 import 해주어야 한다
    public String paging(@PageableDefault(page = 1)Pageable pageable, Model model) {
        pageable.getPageNumber();
        Page<BoardDTO> boardList = boardService.paging(pageable);
        //하단에 보여지는 페이지 갯수 3개
        int blockLimit = 3;
        //1,4,7,10, ~~
        int startPage = (((int)(Math.ceil((double)pageable.getPageNumber() / blockLimit))) - 1) * blockLimit + 1; // 1 4 7 10 ~~
        //3,6,9, ~~ 만약에 startPage 가 7이면 789가 나와야하지만 8페이지까지 밖에 없으면 9페이지는 보여주지 않는다  삼항연산자 사용
        int endPage = ((startPage + blockLimit - 1) < boardList.getTotalPages()) ? startPage + blockLimit - 1 : boardList.getTotalPages();

        model.addAttribute("boardList" , boardList);
        model.addAttribute("startPage" , startPage);
        model.addAttribute("endPage" , endPage);
        return "paging";

   }

}

