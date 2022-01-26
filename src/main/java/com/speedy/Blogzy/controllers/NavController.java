package com.speedy.Blogzy.controllers;

import com.speedy.Blogzy.DTO.ApiAuthDto;
import com.speedy.Blogzy.DTO.AuthorAndBlogs;
import com.speedy.Blogzy.DTO.JWTTokenResponse;
import com.speedy.Blogzy.Exceptions.NotFoundException;
import com.speedy.Blogzy.Exceptions.UnauthorizedAccessException;
import com.speedy.Blogzy.Utils.JWTConstants;
import com.speedy.Blogzy.Utils.JWTUtil;
import com.speedy.Blogzy.models.Author;
import com.speedy.Blogzy.models.Blog;
import com.speedy.Blogzy.repositories.AuthorRepository;
import com.speedy.Blogzy.repositories.BlogRepository;
import com.speedy.Blogzy.service.AuthorDetailsService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;


@Controller
public class NavController {

    private int year;
    private String title;
    private BlogRepository blogRepository;
    private AuthorRepository authorRepository;
    private AuthorDetailsService authorDetailsService;
    private AuthenticationManager authenticationManager;


    public NavController(BlogRepository blogRepository,  AuthorRepository authorRepository, AuthorDetailsService authorDetailsService, AuthenticationManager authenticationManager) {
        this.blogRepository = blogRepository;
        this.authorRepository = authorRepository;
        this.authorDetailsService = authorDetailsService;
        this.authenticationManager=authenticationManager;
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
        model.addAttribute("ApiAuthDto", new ApiAuthDto());
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

    @RequestMapping("/update/{id}")
    public String editBlog(@PathVariable("id") Long id, Principal principal, Model model) {
        Optional<Blog> blogOptional = blogRepository.findById(id);
        if(!blogOptional.isPresent())
            throw  new NotFoundException("No Blog with id : " +id + " exists!");
        Author author = authorRepository.findByEmail(principal.getName()).get();
        Blog blog = blogOptional.get();
        if(!blog.getAuthor().equals(author))
            throw new UnauthorizedAccessException("User " +author.getName() + " does not own the blog titled " + blog.getTitle() + " but tries to update it!!");
        model.addAttribute("blog", blog);
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

    @RequestMapping("/edit-profile")
    public String editProfile(Principal principal, Model model) {
        Author author = authorRepository.findByEmail(principal.getName()).get();
        model.addAttribute("author", author);
        model.addAttribute("title", title);
        return "authentication/register";
    }

    @PostMapping("/api/authenticate")
    @ResponseBody
    public ResponseEntity<JWTTokenResponse> authAndGenerateToken(@RequestBody ApiAuthDto apiAuthDto){
       authenticate(apiAuthDto.getUsername(), apiAuthDto.getPassword());
       final UserDetails userDetails = authorDetailsService.loadUserByUsername(apiAuthDto.getUsername());
       JWTTokenResponse tokenResponse = new JWTTokenResponse();
       tokenResponse.setToken(JWTUtil.generateToken(userDetails));
       tokenResponse.setMessage("Authenticated Successfully, use this token in the authorization header for accessing protected resources :)");
       tokenResponse.setTimeStamp(System.currentTimeMillis());
       return new ResponseEntity<>(tokenResponse, HttpStatus.OK);
    }

    @GetMapping("/api/authenticate")
    @ResponseBody
    public void throwError() {
        throw new BadCredentialsException("Invalid Credentials!!");
    }

    @PostMapping("/authenticate")
    public String authAndGenerateToken(@ModelAttribute ApiAuthDto apiAuthDto, HttpServletResponse response){
        try {
            authenticate(apiAuthDto.getUsername(), apiAuthDto.getPassword());
        }
       catch (BadCredentialsException bce) {
            return "redirect:/login?error";
       }
        final UserDetails userDetails = authorDetailsService.loadUserByUsername(apiAuthDto.getUsername());
        Cookie cookie = new Cookie("AUTH", JWTUtil.generateToken(userDetails));
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(604800);
        response.addCookie(cookie);
        return "redirect:/";
    }

    private void authenticate(String username, String password) throws BadCredentialsException, DisabledException{
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        }
        catch (DisabledException e) {
            throw new DisabledException("USER_DISABLED", e);
        }
        catch (BadCredentialsException e) {
            throw new BadCredentialsException("INVALID_CREDENTIALS", e);
        }
    }
}
