package com.speedy.Blogzy.repositories;

import com.speedy.Blogzy.models.Author;
import com.speedy.Blogzy.models.Blog;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlogRepository extends CrudRepository<Blog, Long> {
}