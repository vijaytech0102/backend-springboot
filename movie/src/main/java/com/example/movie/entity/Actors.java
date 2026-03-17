package com.example.movie.entity;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;

import jakarta.persistence.OneToMany;

@Entity
public class Actors{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    String actorName;
    String gender;
    Integer age;

    @OneToMany(cascade=CascadeType.ALL)
    @JoinColumn(name="actor_id", referencedColumnName="id")
    public List<Movie> movies=new ArrayList<>();
    public Actors() {
    }

    public Actors(Long id, String actorName, String gender, Integer age) {
        this.id = id;
        this.actorName = actorName;
        this.gender = gender;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getActorName() {
        return actorName;
    }

    public void setActorName(String actorName) {
        this.actorName = actorName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
