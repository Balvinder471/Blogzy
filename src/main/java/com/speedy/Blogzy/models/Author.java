package com.speedy.Blogzy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
//insert into authors (name, email, description) values ('Hari Prasad', 'hari123@gmail.com', 'A proud Blogzy developer');
//        insert into authors (name, email, description) values ('Makashi', 'makash123@yahoo.in', 'Tech Enthusiast and passionate blogger');
//        insert into authors (name, email, description) values ('Speedy', 'sspeedy472@gmail.com', 'Runs at high speed, no one can catch him');
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name = "authors")
public class Author implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;
    @Column(length = 50)
    @NotBlank
    @Size(min = 10 ,max = 50)
    private String description;
    @Column(unique = true, nullable = false)
    @NotBlank
    @Pattern(regexp = "[a-z0-9!#$%&'+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'+/=?^_`{|}~-]+)@(?:[a-z0-9](?:[a-z0-9-][a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message = "Invalid Email!!")
    private String email;
    @Column(nullable = false)
    @NotBlank
    @Size(min = 8, max = 100)
    private String password;
    @Column(columnDefinition = "varchar(255) default 'ROLE_USER'", nullable = false)
    private String role;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "author", fetch = FetchType.LAZY)
    @JsonBackReference
    @ToString.Exclude
    private List<Blog> blogs;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Author author = (Author) o;
        return id != null && Objects.equals(id, author.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(this.role));
        return authorities;
    }

    @Override
    public String getUsername() {
        return this.email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
