package com.speedy.Blogzy.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.format.annotation.DateTimeFormat;


import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

//insert into blogs (title, short_description, author_id) values('Wonderland' ,'A place where you find yourself indulged in doing things that make you wonder!!', 3);
//        insert into blogs (title, short_description, author_id) values('Cricket' ,'A game played between two teams of 11 with a bat and a ball for some reason',1);
//        insert into blogs (title, short_description, author_id) values('Metaverse' ,'concept where we can generate real time 3d worlds to connect virtually and do stuff',1);
//        insert into blogs (title, short_description, author_id) values('Medicinal Herbs' ,'Medicinal herbs are of great importance specially in the Indian subcontinent',2);
@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table(name="blogs")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

//    @Enumerated(value = EnumType.STRING)
//    private Genre genre;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date creationDate;

    private String shortDescription;
    @Lob
    private String description;

    @ManyToOne
//    @JsonManagedReference
    @JsonIgnore
    private Author author;
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Blog blog = (Blog) o;
        return id != null && Objects.equals(id, blog.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
