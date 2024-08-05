package com.badnarrators.celos.wiki.entity;


import com.badnarrators.celos.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "page")
public class Page {
    @Id
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "category", nullable = false)
    private String category;

    @Column(name = "content", nullable = false)
    private String content;

    private boolean isPrivate = false;

    @ManyToMany
    @JoinTable(
            name = "page_readers",
            joinColumns = @JoinColumn(name = "page_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private List<User> readers;

    @ManyToMany
    @JoinTable(
            name = "page_children",
            joinColumns = @JoinColumn(name = "parent_id"),
            inverseJoinColumns = @JoinColumn(name = "child_id")
    )
    private List<Page> children;

}
