package com.speedy.Blogzy.controllers;

import com.speedy.Blogzy.Exceptions.NotFoundException;
import com.speedy.Blogzy.Exceptions.UnauthorizedAccessException;
import com.speedy.Blogzy.models.Blog;
import com.speedy.Blogzy.repositories.AuthorRepository;
import com.speedy.Blogzy.repositories.BlogRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import javax.validation.Valid;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

@Controller
//@PreAuthorize("hasRole('ADMIN')")
@RequestMapping("/blogs")
public class BlogController {


    private int year;
    private String title;
    private BlogRepository blogRepository;
    private AuthorRepository authorRepository;

    public BlogController(BlogRepository blogRepository, AuthorRepository authorRepository) {
        this.blogRepository = blogRepository;
        this.authorRepository = authorRepository;
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

    @PostMapping("")
    public String addBlog(@Valid @ModelAttribute Blog blog, BindingResult bindingResult, Principal principal, Model model) {
        if(bindingResult.hasErrors()) {
            model.addAttribute("blog", blog);
            return "createblog";
        }
        blog.setAuthor(authorRepository.findByEmail(principal.getName()).get());
        blogRepository.save(blog);
        return "redirect:/blogs/" + blog.getId();
    }

    @PostMapping("/{id}")
    public String updateBlog(@PathVariable Long id, @Valid @ModelAttribute Blog blog, BindingResult bindingResult,Principal principal, Model model) {
        if(!blogRepository.existsById(id))
            throw new NotFoundException("No Such Blog Exists");
        if(!blogRepository.findById(id).get().getAuthor().equals(authorRepository.findByEmail(principal.getName()).get()))
            throw new UnauthorizedAccessException("User does not have access to this blog with id : " + blog.getId());
        if(bindingResult.hasErrors()) {
            model.addAttribute("blog", blog);
            return "createblog";
        }
        blog.setAuthor(authorRepository.findByEmail(principal.getName()).get());
        blogRepository.save(blog);
        return "redirect:/blogs/" + blog.getId();
    }

    @GetMapping("/delete/{id}")
    public String deleteBlog(@PathVariable Long id, Principal principal) {
        if(!blogRepository.existsById(id))
            throw new NotFoundException("No Such Blog Exists");
        if(!blogRepository.findById(id).get().getAuthor().equals(authorRepository.findByEmail(principal.getName()).get()))
            throw new UnauthorizedAccessException("User " + principal.getName() + " does not own the blog with id : " +id + " but tries to delete it!!");
        blogRepository.deleteById(id);
        return "redirect:/my-profile";
    }

//    @ExceptionHandler(NotFoundException.class)
//    public ModelAndView handleException() {
//        ModelAndView modelAndView = new ModelAndView();
//        modelAndView.setViewName("contact");
//        return modelAndView;
//    }
    // Ema
}
