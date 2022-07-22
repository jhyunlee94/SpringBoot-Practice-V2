package com.example.mvc.validator;

import com.example.mvc.entity.Board;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class BoardValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Board.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) {
        Board b = (Board) obj;
        if(StringUtils.isEmpty(b.getContent())) {
            //StringUtils Board에 Content가 비어있는지 확인해보겠습니다.
            //isEmpty가 사라져서 대체로
            //ObjectUtils.isEmpty
            e.rejectValue("content", "key","내용을 입력하세요");
        }
    }
}
