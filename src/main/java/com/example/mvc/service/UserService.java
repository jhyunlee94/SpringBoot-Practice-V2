package com.example.mvc.service;

import com.example.mvc.entity.Role;
import com.example.mvc.entity.User;
import com.example.mvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    //@Service하면 비즈니스 로직을 할수있고 하단에 test 폴더 안에있는 테스트를 작성하기 좋겠죠? 이 클래스를 가져다가!!
    // 실제로 하다보면 이 서비스를 인터페이스로 빼기도하고 ServiceImpl 하기도하고 굳이 저는 두개로 빼지 않겠습니다.

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User save(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());// 사용자가 전해준 패스워드를 인코드해서 저장을 해주고
        user.setPassword(encodedPassword); // 값을 저장해주고
        user.setEnabled(true); // 활성화를 해줘야겠죠??

        Role role = new Role();
        role.setId(1l); // 하드 코딩 한 상태
        user.getRoles().add(role); // role을 어떤 권한을 줄건지 할수있는거죠?? 이렇게 roles에 값을 넣어서 하면은 user_role에 알아서 저장됩니다.
        return userRepository.save(user);
    }
}
