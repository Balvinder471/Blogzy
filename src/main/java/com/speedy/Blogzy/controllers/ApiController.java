package com.speedy.Blogzy.controllers;

import com.speedy.Blogzy.Exceptions.NotFoundException;
import com.speedy.Blogzy.models.Blog;
import com.speedy.Blogzy.repositories.BlogRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/blogs")
public class ApiController {

    private BlogRepository blogRepository;

    public ApiController(BlogRepository blogRepository) {
        this.blogRepository = blogRepository;
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
    public Blog addBlog(@RequestBody Blog blog) {
        blogRepository.save(blog);
        return blog;
    }

    @GetMapping("")
    public List<Blog> getBlogs() {
        return (List<Blog>) blogRepository.findAll();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Blog> handleException() {
        Blog blog = new Blog();
        blog.setTitle("No Such Blog!!!");
        blog.setDescription("You tried to access a blog that does not exists or might be removed by the author or an admin!!");
        return new ResponseEntity<>(blog, HttpStatus.NOT_FOUND);
    }
}


