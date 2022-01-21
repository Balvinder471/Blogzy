package com.speedy.Blogzy.repositories;

import com.speedy.Blogzy.models.Author;
import com.speedy.Blogzy.models.Blog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlogRepository extends JpaRepository<Blog, Long> {
    Page<Blog> findBlogsByAuthor(Author author, Pageable pageable);
}