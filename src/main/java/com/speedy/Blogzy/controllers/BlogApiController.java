package com.speedy.Blogzy.controllers;

import com.speedy.Blogzy.DTO.CustomBlogDto;
import com.speedy.Blogzy.DTO.ExceptionDto;
import com.speedy.Blogzy.Exceptions.NotFoundException;
import com.speedy.Blogzy.Exceptions.UnauthorizedAccessException;
import com.speedy.Blogzy.models.Author;
import com.speedy.Blogzy.models.Blog;
import com.speedy.Blogzy.repositories.BlogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blogs")
public class BlogApiController {

    private BlogRepository blogRepository;
    private ModelMapper modelMapper;

    public BlogApiController(BlogRepository blogRepository, ModelMapper modelMapper) {
        this.blogRepository = blogRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/{id}")
    public CustomBlogDto getBlog(@PathVariable Long id) {
        Optional<Blog> blogOptional = blogRepository.findById(id);
        if(!blogOptional.isPresent())
            throw new NotFoundException("No Blog exists with ID : " + id);
        return modelMapper.map(blogOptional.get(), CustomBlogDto.class);
    }

    @PostMapping("")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CustomBlogDto addBlog(@RequestBody Blog blog) {
        blogRepository.save(blog);
        return modelMapper.map(blog, CustomBlogDto.class);
    }

    @PutMapping("/{id}")
    public CustomBlogDto editBlog(@PathVariable Long id, @RequestBody Blog blog, Principal principal) {
        Optional<Blog> blogOptional = blogRepository.findById(id);
        if(!blogOptional.isPresent())
            throw new NotFoundException("No Such Blog Exists");
        if(!blogOptional.get().getAuthor().getEmail().equals(principal.getName()))
            throw new UnauthorizedAccessException("User " +principal.getName() + " does not have access to this resource");
        blog.setId(id);
        blog.setAuthor(blogOptional.get().getAuthor());
        blogRepository.save(blog);
        return modelMapper.map(blog, CustomBlogDto.class);
    }

    @DeleteMapping("/{id}")
    public ExceptionDto deleteBlog(@PathVariable Long id, Principal principal) {
        Optional<Blog> blogOptional = blogRepository.findById(id);
        if(!blogOptional.isPresent())
            throw new NotFoundException("No Such Blog Exists");
        Blog blog = blogOptional.get();
        if(!blog.getAuthor().getEmail().equals(principal.getName()))
            throw new UnauthorizedAccessException("User " +principal.getName() + " does not have access to this resource");
        blogRepository.deleteById(id);
        ExceptionDto mssg = new ExceptionDto();
        mssg.setTimeStamp(System.currentTimeMillis());
        mssg.setMessage("Blog with id: " +id + " deleted successfully");
        mssg.setStatus(200);
        return mssg;
    }

    @GetMapping("")
    public List<Blog> getBlogs() {
        return (List<Blog>) blogRepository.findAll();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> handleException() {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setStatus(HttpStatus.NOT_FOUND.value());
        exceptionDto.setMessage("You tried to access a blog that does not exists or might have been removed by the author or an admin!!");
        exceptionDto.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ExceptionDto> handleUnauthorizedAccess() {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setStatus(HttpStatus.FORBIDDEN.value());
        exceptionDto.setTimeStamp(System.currentTimeMillis());
        exceptionDto.setMessage("You do not have access to that resource!!!");
        return new ResponseEntity<>(exceptionDto, HttpStatus.FORBIDDEN);
    }
}


