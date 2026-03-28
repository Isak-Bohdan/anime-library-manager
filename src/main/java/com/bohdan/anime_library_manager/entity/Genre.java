package com.bohdan.anime_library_manager.entity;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "genres")
public class Genre {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @ManyToMany(mappedBy = "genres")
    private Set<Title> titles = new HashSet<>();

    public Genre() {
    }

    public Genre(Long id, String name, Set<Title> titles) {
        this.id = id;
        this.name = name;
        this.titles = titles;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<Title> getTitles() {
        return titles;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTitles(Set<Title> titles) {
        this.titles = titles;
    }
}