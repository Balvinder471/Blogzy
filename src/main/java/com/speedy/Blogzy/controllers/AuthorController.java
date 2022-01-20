package com.speedy.Blogzy.controllers;

import com.speedy.Blogzy.models.Author;
import com.speedy.Blogzy.repositories.AuthorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;
    public AuthorController(AuthorRepository authorRepository, PasswordEncoder passwordEncoder) {
        this.authorRepository = authorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("")
    public String saveAuthor(@ModelAttribute Author author){
        if(author.getRole() == null)
            author.setRole("USER");
        author.setPassword(passwordEncoder.encode(author.getPassword()));
        authorRepository.save(author);
        return "redirect:/login";
    }

}
