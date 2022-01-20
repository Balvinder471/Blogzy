package com.speedy.Blogzy.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CustomBlogDto {
    private String title;
    private String shortDescription;
    private Date creationDate;
}
