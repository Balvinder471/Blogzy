package com.speedy.Blogzy.service;

import com.speedy.Blogzy.models.Author;
import com.speedy.Blogzy.repositories.AuthorRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthorDetailsService  implements UserDetailsService {
    private final AuthorRepository authorRepository;

    public AuthorDetailsService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Author> authorOptional = authorRepository.findByEmail(email);
        if(!authorOptional.isPresent())
            throw new UsernameNotFoundException("No User Exists with email : " + email);
        return authorOptional.get();
    }
}
