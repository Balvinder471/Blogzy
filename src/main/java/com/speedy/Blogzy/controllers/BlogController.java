package com.speedy.Blogzy.controllers;

import com.speedy.Blogzy.Exceptions.NotFoundException;
import com.speedy.Blogzy.models.Blog;
import com.speedy.Blogzy.repositories.BlogRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/blogs")
public class BlogController {


    private int year;
    private String title;
    private BlogRepository blogRepository;

    public BlogController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
        title="Blogzy";
        year=Calendar.getInstance().get(Calendar.YEAR);
    }



    @RequestMapping("/{id}")
    public String getBlog(@PathVariable long id, Model model) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMMM-yy");
        model.addAttribute("title", title);
        model.addAttribute("year", year);
        if(!blogRepository.findById(id).isPresent())
            throw new NotFoundException("No Such Blog with Id : " + id);
        model.addAttribute("blogDetails", blogRepository.findById(id).get());
        model.addAttribute("blogs", blogRepository.findAll());
        model.addAttribute("sdf", sdf);
        model.addAttribute("authorDetails", blogRepository.findById(id).get().getAuthor());
        return "blog";
    }

//    @ExceptionHandler(NotFoundException.class)
//    public ModelAndView handleException() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("contact");
//        return modelAndView;
//    }
}
