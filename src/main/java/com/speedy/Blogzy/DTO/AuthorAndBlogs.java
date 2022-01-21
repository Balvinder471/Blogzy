package com.speedy.Blogzy.DTO;

import com.speedy.Blogzy.models.Blog;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthorAndBlogs {
    private String name;
    private String email;
    private String description;
    private List<Blog> blogs;
}
