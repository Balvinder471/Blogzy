package com.speedy.Blogzy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
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
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Column(length = 50)
    private String description;
    @Column(unique = true, nullable = false)
    private String email;
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
}
