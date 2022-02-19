package com.speedy.Blogzy.controllers;

import com.nimbusds.jwt.JWT;
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
import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.authentication.AuthenticationManagerFactoryBean;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.jws.WebParam;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.*;


@Controller
public class NavController {

    private int year;
    private String title;
    private BlogRepository blogRepository;
    private PasswordEncoder passwordEncoder;
    private AuthorRepository authorRepository;
    private AuthorDetailsService authorDetailsService;
    private AuthenticationManager authenticationManager;
    private OAuth2AuthorizedClientService authorizedClientService;

    public NavController(BlogRepository blogRepository,  AuthorRepository authorRepository, AuthorDetailsService authorDetailsService,PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, OAuth2AuthorizedClientService authorizedClientService) {
        this.blogRepository = blogRepository;
        this.authorRepository = authorRepository;
        this.authorDetailsService = authorDetailsService;
        this.authenticationManager=authenticationManager;
        this.authorizedClientService = authorizedClientService;
        this.passwordEncoder = passwordEncoder;
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

    @RequestMapping("/process-oauth")
    public String oauth(OAuth2AuthenticationToken authentication, Model model, HttpServletResponse servletResponse) {
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                authentication.getAuthorizedClientRegistrationId(),
                authentication.getName()
        );

        String userInfo = client.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUri();
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Bearer " + client.getAccessToken()
                .getTokenValue());
        HttpEntity entity = new HttpEntity<>("",headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<Map> response = restTemplate.exchange(userInfo, HttpMethod.GET, entity, Map.class);
        Map authDetails = response.getBody();
        model.addAttribute("name", authDetails.get("name"));
        if(!authorRepository.findByEmail((String) authDetails.get("email")).isPresent()) {
            Author author = new Author();
            author.setEmail((String) authDetails.get("email"));
            author.setName((String) authDetails.get("name"));
            author.setRole("ROLE_USER");
            author.setDescription(authentication.getAuthorizedClientRegistrationId() + " user " + authDetails.get("name"));
            author.setPassword(passwordEncoder.encode(JWTConstants.JWT_SECRET));
            authorRepository.save(author);
        }

        UserDetails userDetails = authorDetailsService.loadUserByUsername((String) authDetails.get("email"));
        Cookie cookie = new Cookie("JSESSIONID", "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        servletResponse.addCookie(cookie);
        servletResponse.addCookie(JWTUtil.generateAuthCookie(userDetails));
        return "redirect:/";
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
        response.addCookie(JWTUtil.generateAuthCookie(userDetails));
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
