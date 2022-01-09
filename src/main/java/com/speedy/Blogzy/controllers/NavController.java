package com.speedy.Blogzy.controllers;

import com.speedy.Blogzy.service.HomeData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Calendar;


@Controller
public class NavController {

    private HomeData homeData;
    private int year;
    private String title;

    public NavController(HomeData homeData) {
        this.homeData = homeData;
        year = Calendar.getInstance().get(Calendar.YEAR);
        title="Blogzy";
    }

    @RequestMapping("")
    public String getHome(Model model)
    {
        model.addAttribute("title", title);
        model.addAttribute("year", year);
        model.addAttribute("blogs", homeData.getBlogs());
        return "index";
    }

    @RequestMapping("/about")
    public String getAbout(Model model) {
        model.addAttribute("title", title);
        model.addAttribute("year", year);
        return "about";
    }
}
