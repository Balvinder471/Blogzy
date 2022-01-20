package com.speedy.Blogzy.controllers;

import com.speedy.Blogzy.models.Author;
import com.speedy.Blogzy.repositories.BlogRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;


@Controller
public class NavController {

    private int year;
    private String title;
    private BlogRepository blogRepository;

    public NavController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
        title = "Blogzy";
        year = Calendar.getInstance().get(Calendar.YEAR);
    }

    @RequestMapping("")
    public String getHome(Model model)
    {
        model.addAttribute("title", title);
        model.addAttribute("year", year);
        model.addAttribute("blogs", blogRepository.findAll());
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
}
