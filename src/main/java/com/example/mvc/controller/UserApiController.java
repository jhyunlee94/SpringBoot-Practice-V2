package com.example.mvc.controller;

import com.example.mvc.entity.User;
import com.example.mvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
class UserApiController {

    @Autowired
    private UserRepository repository;

    // Aggregate root
    // tag::get-aggregate-root[]
    @GetMapping("/users")
    List<User> all() {
        return repository.findAll();
    }
    // end::get-aggregate-root[]

    @PostMapping("/users")
    User newuser(@RequestBody User newuser) {
        return repository.save(newuser);
    }

    // Single item

    @GetMapping("/users/{id}")
    User one(@PathVariable Long id) {

        return repository.findById(id).orElse(null);
    }

    @PutMapping("/users/{id}")
    User replaceuser(@RequestBody User newuser, @PathVariable Long id) {

        return repository.findById(id)
                .map(user -> {
//                    user.setTitle(newuser.getTitle());
//                    user.setContent(newuser.getContent());
                    return repository.save(user);
                })
                .orElseGet(() -> {
                    newuser.setId(id);
                    return repository.save(newuser);
                });
    }

    @DeleteMapping("/users/{id}")
    void deleteuser(@PathVariable Long id) {
        repository.deleteById(id);
    }
}