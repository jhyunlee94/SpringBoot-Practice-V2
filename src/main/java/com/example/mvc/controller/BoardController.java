package com.example.mvc.controller;

import com.example.mvc.entity.Board;
import com.example.mvc.repository.BoardRepository;
import com.example.mvc.validator.BoardValidator;
import org.springframework.beans.factory.annotation.Autowired;
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
    private BoardValidator boardValidator;

    @GetMapping("/list")
    public String list(Model model) {
        List<Board> boards = boardRepository.findAll();
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
    public String form(@Valid Board board, BindingResult bindingResult){
        boardValidator.validate(board, bindingResult);
        if(bindingResult.hasErrors()) {
            //값이 부합하는지 안하는지 hasErrors()로 처리가능
            //GetMapping 에서 처리한거 넣어줌
            return "board/form";
        }
        //다시 board를 받아오는거
        boardRepository.save(board);
        return "redirect:/board/list";
    }
}
