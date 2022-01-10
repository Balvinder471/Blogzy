package com.speedy.Blogzy.controllers;

import com.speedy.Blogzy.repositories.BlogRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;

@Controller
public class BlogController {


    private int year;
    private String title;
    private BlogRepository blogRepository;

    public BlogController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
        title="Blogzy";
        year=Calendar.getInstance().get(Calendar.YEAR);
    }



    @RequestMapping("/blog/{id}")
    public String getBlog(@PathVariable long id, Model model) {
        model.addAttribute("title", title);
        model.addAttribute("year", year);
        model.addAttribute("blogDetails", blogRepository.findById(id).get());
        model.addAttribute("blogs", blogRepository.findAll());
        model.addAttribute("authorDetails", blogRepository.findById(id).get().getAuthor());
        return "blog";
    }
}
