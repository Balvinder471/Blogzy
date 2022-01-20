package com.speedy.Blogzy.DTO;

import com.speedy.Blogzy.models.Blog;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AuthorAndBlogs {
    private Long id;
    private String name;
    private List<Blog> blogs;
}
