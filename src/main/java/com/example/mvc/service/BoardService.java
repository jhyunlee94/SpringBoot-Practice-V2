package com.example.mvc.service;

import com.example.mvc.entity.Board;
import com.example.mvc.entity.User;
import com.example.mvc.repository.BoardRepository;
import com.example.mvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    public Board save(String username, Board board) {
       User user =  userRepository.findByUsername(username);
       board.setUser(user);
       return boardRepository.save(board);
    }
}
