package com.speedy.Blogzy.controllers;

import com.speedy.Blogzy.DTO.AuthorAndBlogs;
import com.speedy.Blogzy.DTO.ExceptionDto;
import com.speedy.Blogzy.Exceptions.NotFoundException;
import com.speedy.Blogzy.models.Author;
import com.speedy.Blogzy.repositories.AuthorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/authors")
public class AuthorApiController {
    private final AuthorRepository authorRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    public AuthorApiController(AuthorRepository authorRepository, PasswordEncoder passwordEncoder, ModelMapper modelMapper) {
        this.authorRepository = authorRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public Author createAuthor(@RequestBody Author author){
        if(author.getRole() == null)
            author.setRole("USER");
        author.setPassword(passwordEncoder.encode(author.getPassword()));
        authorRepository.save(author);
        return author;
    }

    @GetMapping("/{id}")
    public AuthorAndBlogs getAuthorBlogs(@PathVariable Long id) {
        Optional<Author> authorOptional = authorRepository.findById(id);
        if(!authorOptional.isPresent())
            throw new NotFoundException("No Author With that Id : " + id);
        return modelMapper.map(authorOptional.get(), AuthorAndBlogs.class);
    }

    @GetMapping("")
    public List<Author> getAuthors(){
        return (List<Author>) authorRepository.findAll();
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> handleException() {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setStatus(HttpStatus.NOT_FOUND.value());
        exceptionDto.setMessage("No Such Author Exists or might have been removed by an admin or may have unregistered!!");
        exceptionDto.setTimeStamp(System.currentTimeMillis());
        return new ResponseEntity<>(exceptionDto, HttpStatus.NOT_FOUND);
    }
}
