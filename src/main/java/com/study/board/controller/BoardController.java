package com.study.board.controller;

import ch.qos.logback.core.net.SyslogOutputStream;
import com.study.board.dto.BoardDTO;
import com.study.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

}
