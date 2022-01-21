package com.speedy.Blogzy.controllers;

import com.speedy.Blogzy.DTO.AuthorAndBlogs;
import com.speedy.Blogzy.Exceptions.NotFoundException;
import com.speedy.Blogzy.models.Author;
import com.speedy.Blogzy.models.Blog;
import com.speedy.Blogzy.repositories.AuthorRepository;
import com.speedy.Blogzy.repositories.BlogRepository;
import com.sun.org.apache.xpath.internal.operations.Mod;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.validation.Valid;
import java.util.Optional;


@Controller
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorRepository authorRepository;
    private final BlogRepository blogRepository;
    private final PasswordEncoder passwordEncoder;
    private String title;
    public AuthorController(AuthorRepository authorRepository, PasswordEncoder passwordEncoder, BlogRepository blogRepository) {
        this.authorRepository = authorRepository;
        this.passwordEncoder = passwordEncoder;
        this.blogRepository = blogRepository;
        title = "Blogzy";
    }

    @PostMapping("")
    public String saveAuthor(@Valid @ModelAttribute Author author, BindingResult bindingResult, @RequestParam("password_confirmation") String cp, Model model){

        boolean match = author.getPassword().contentEquals(cp);

        if(bindingResult.hasErrors() || !match){
            model.addAttribute("author", author);
            model.addAttribute("match", match);
            model.addAttribute("title", title);
            return "authentication/register";
        }

        if(author.getRole() == null)
            author.setRole("ROLE_USER");
        author.setPassword(passwordEncoder.encode(author.getPassword()));
        authorRepository.save(author);
        return "redirect:/login?registered";
    }

    @RequestMapping("/{id}")
    public String getAuthor(@PathVariable Long id,@RequestParam(defaultValue = "0") int page, Model model) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        if(!authorOptional.isPresent())
            throw new NotFoundException("No such Author Exists or might have been removed by an admin");
        Author author = authorOptional.get();
        Pageable pageable = PageRequest.of(page, 2);
        Page<Blog> blogs = blogRepository.findBlogsByAuthor(author, pageable);
        model.addAttribute("title", title);
        model.addAttribute("author", author);
        model.addAttribute("blogs", blogs);
        model.addAttribute("page", page);
        model.addAttribute("pages",blogs.getTotalPages());
        model.addAttribute("title", title);
        return "Author";
    }

}
