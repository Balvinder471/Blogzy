package com.speedy.Blogzy.controllers;

import com.speedy.Blogzy.service.BlogData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;

@Controller
public class BlogController {


    private int year;
    private BlogData natureBlog;
    private BlogData fantasyBlog;
    private BlogData scifiBlog;
    private BlogData sportsBlog;
    private String title;

    public BlogController(@Qualifier("natureData") BlogData natureBlog, @Qualifier("fantasyData") BlogData fantasyBlog, @Qualifier("scifiData") BlogData scifiBlog, @Qualifier("sportsData") BlogData sportsBlog) {
        this.natureBlog = natureBlog;
        this.fantasyBlog = fantasyBlog;
        this.scifiBlog = scifiBlog;
        this.sportsBlog = sportsBlog;
        title = "Blogzy";
        year = Calendar.getInstance().get(Calendar.YEAR);
    }

    @RequestMapping("/blog/{id}")
    public String getBlogs(@PathVariable int id, Model model) {
        model.addAttribute("title", title);
        model.addAttribute("year", year);
        switch (id) {
            case 1:
                model.addAttribute("blogDetails", sportsBlog.getBlogDetails());
                model.addAttribute("authorDetails", sportsBlog.getAuthorDetails());
                break;
            case 2:
                model.addAttribute("blogDetails", fantasyBlog.getBlogDetails());
                model.addAttribute("authorDetails", fantasyBlog.getAuthorDetails());
                break;
            case 3:
                model.addAttribute("blogDetails", scifiBlog.getBlogDetails());
                model.addAttribute("authorDetails", scifiBlog.getAuthorDetails());
                break;
            case 4:
                model.addAttribute("blogDetails", natureBlog.getBlogDetails());
                model.addAttribute("authorDetails", natureBlog.getAuthorDetails());
                break;
        }
        return "blog";
    }


}
