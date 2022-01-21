package com.speedy.Blogzy.controllers;

import com.speedy.Blogzy.DTO.AuthorAndBlogs;
import com.speedy.Blogzy.models.Author;
import com.speedy.Blogzy.models.Blog;
import com.speedy.Blogzy.repositories.AuthorRepository;
import com.speedy.Blogzy.repositories.BlogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.jws.WebParam;
import java.security.Principal;
import java.util.Calendar;


@Controller
public class NavController {

    private int year;
    private String title;
    private BlogRepository blogRepository;
    private AuthorRepository authorRepository;

    public NavController(BlogRepository blogRepository,  AuthorRepository authorRepository) {
        this.blogRepository = blogRepository;
        this.authorRepository = authorRepository;
        title = "Blogzy";
        year = Calendar.getInstance().get(Calendar.YEAR);
    }

    @RequestMapping("")
    public String getHome(@RequestParam(defaultValue = "0") int page, Model model)
    {
        Pageable pageable = PageRequest.of(page, 4);
        Page<Blog> blogs = blogRepository.findAll(pageable);
        model.addAttribute("title", title);
        model.addAttribute("year", year);
        model.addAttribute("blogs", blogs);
        model.addAttribute("page", page);
        model.addAttribute("pages",blogs.getTotalPages());
        return "index";
    }

    @RequestMapping("/about")
    public String getAbout(Model model) {
        model.addAttribute("title", title);
        model.addAttribute("year", year);
        model.addAttribute("blogs", blogRepository.findAll());
        return "about";
    }

    @RequestMapping("/login")
    public String getLogin(Model model) {
        model.addAttribute("title", title);
        return "authentication/login";
    }

    @RequestMapping("/register")
    public String getRegister(Model model) {
        model.addAttribute("author", new Author());
        model.addAttribute("title", title);
        return "authentication/register";
    }

    @RequestMapping("/add-new-blog")
    public String addBlog(Model model) {
        model.addAttribute("blog", new Blog());
        model.addAttribute("title", title);
        return "createblog";
    }

    @RequestMapping("/my-profile")
    public String getProfile(@RequestParam(defaultValue = "0") int page, Principal principal, Model model) {
        Author author = authorRepository.findByEmail(principal.getName()).get();
        Pageable pageable = PageRequest.of(page, 2);
        Page<Blog> blogs = blogRepository.findBlogsByAuthor(author, pageable);
        model.addAttribute("title", title);
        model.addAttribute("author", author);
        model.addAttribute("blogs", blogs);
        model.addAttribute("page", page);
        model.addAttribute("pages",blogs.getTotalPages());
        return "Author";
    }
}
