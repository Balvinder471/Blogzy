package com.speedy.Blogzy.controllers;

import com.speedy.Blogzy.DTO.CustomBlogDto;
import com.speedy.Blogzy.DTO.ExceptionDto;
import com.speedy.Blogzy.Exceptions.NotFoundException;
import com.speedy.Blogzy.models.Blog;
import com.speedy.Blogzy.repositories.BlogRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public Blog getBlog(@PathVariable Long id) {
        Optional<Blog> blogOptional = blogRepository.findById(id);
        if(!blogOptional.isPresent())
            throw new NotFoundException("No Blog exists with ID : " + id);
        blogRepository.save(blogOptional.get());
        return blogOptional.get();
    }

    @PostMapping("")
    @ResponseStatus(value = HttpStatus.CREATED)
    public CustomBlogDto addBlog(@RequestBody Blog blog) {
        blogRepository.save(blog);
        return modelMapper.map(blog, CustomBlogDto.class);
    }

    @GetMapping("")
    public List<Blog> getBlogs() {
        return (List<Blog>) blogRepository.findAll();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> handleException() {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setStatus(HttpStatus.NOT_FOUND.value());
        exceptionDto.setMessage("You tried to access a blog that does not exists or might be removed by the author or an admin!!");
        exceptionDto.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }
}


