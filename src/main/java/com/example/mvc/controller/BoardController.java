package com.example.mvc.controller;

import com.example.mvc.entity.Board;
import com.example.mvc.repository.BoardRepository;
import com.example.mvc.service.BoardService;
import com.example.mvc.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/board")
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardValidator boardValidator;

    @GetMapping("/list")
    public String list(Model model, @PageableDefault(size = 2) Pageable pageable,
                       @RequestParam(required = false, defaultValue = "") String searchText) {
//        Page<Board> boards = boardRepository.findAll(pageable);
        Page<Board> boards = boardRepository.findByTitleContainingOrContentContaining(searchText, searchText, pageable);
        int startPage = Math.max(1,boards.getPageable().getPageNumber() - 4);
        int endPage = Math.min(boards.getTotalPages(), boards.getPageable().getPageNumber() + 4);
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("boards", boards);
        return "board/list";
    }

    @GetMapping("/form")
    public String form(Model model, @RequestParam(required = false) Long id) {
        if(id == null) {
            model.addAttribute("board", new Board());
        }else {
            //조회된 값이 없을 수 있어서 null로 처리
            Board board = boardRepository.findById(id).orElse(null);
            model.addAttribute("board", board);
        }
        return "board/form";
    }

    @PostMapping("/form")
    public String postForm(@Valid Board board, BindingResult bindingResult, Authentication authentication) {
        // 인증정보 가져오는게 Authentication으로 가져옴
        boardValidator.validate(board, bindingResult);
        if(bindingResult.hasErrors()) {
            //값이 부합하는지 안하는지 hasErrors()로 처리가능
            //GetMapping 에서 처리한거 넣어줌
            return "board/form";
        }

        //
        //인증정보 가져오기
        //또는
//        Authentication a = SecurityContextHolder.getContext().getAuthentication(); 이 방법도 있음
        String username = authentication.getName();

        //다시 board를 받아오는거
        boardService.save(username, board);
        boardRepository.save(board);
        return "redirect:/board/list";
    }
}
