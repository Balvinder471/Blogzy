package com.speedy.Blogzy.DTO;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.SecondaryTable;

@Getter
@Setter
public class ApiAuthDto {
    private String username;
    private String password;
}
