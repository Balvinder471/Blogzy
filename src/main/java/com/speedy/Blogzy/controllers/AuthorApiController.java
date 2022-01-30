package com.speedy.Blogzy.controllers;

import com.speedy.Blogzy.DTO.AuthorAndBlogs;
import com.speedy.Blogzy.DTO.ExceptionDto;
import com.speedy.Blogzy.Exceptions.NotFoundException;
import com.speedy.Blogzy.Exceptions.UnauthorizedAccessException;
import com.speedy.Blogzy.Utils.JWTConstants;
import com.speedy.Blogzy.models.Author;
import com.speedy.Blogzy.repositories.AuthorRepository;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebParam;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
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
            author.setRole("ROLE_USER");
        if(null != author.getPassword())
            author.setPassword(passwordEncoder.encode(author.getPassword()));
        authorRepository.save(author);
        return author;
    }

    @GetMapping("/{id}")
    public AuthorAndBlogs getAuthorBlogs(@PathVariable Long id) {
        System.out.println("Inside Get!!");
        Optional<Author> authorOptional = authorRepository.findById(id);
        if(!authorOptional.isPresent())
            throw new NotFoundException("No Author With that Id : " + id);
        return modelMapper.map(authorOptional.get(), AuthorAndBlogs.class);
    }

    @PutMapping("")
    public AuthorAndBlogs editAuthor(@RequestBody Author author, Principal principal) {
        Author authenticatedAuthor = authorRepository.findByEmail(principal.getName()).get();
        author.setId(authenticatedAuthor.getId());
        author.setEmail(authenticatedAuthor.getEmail());
        authorRepository.save(author);
        return modelMapper.map(author, AuthorAndBlogs.class);
    }

    @DeleteMapping("")
    public ExceptionDto deleteAuthor(Principal principal) {
        System.out.println("Inside Delete");
        Author author = authorRepository.findByEmail(principal.getName()).get();
        authorRepository.delete(author);
        ExceptionDto mssg = new ExceptionDto();
        mssg.setTimeStamp(System.currentTimeMillis());
        mssg.setStatus(200);
        mssg.setMessage("Author Deleted Successfully");
        return mssg;
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

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ExceptionDto> handleUnauthorizedAccess() {
        ExceptionDto exceptionDto = new ExceptionDto();
        exceptionDto.setStatus(HttpStatus.FORBIDDEN.value());
        exceptionDto.setTimeStamp(System.currentTimeMillis());
        exceptionDto.setMessage("You do not have access to that resource!!!");
        return new ResponseEntity<>(exceptionDto, HttpStatus.FORBIDDEN);
    }
}
