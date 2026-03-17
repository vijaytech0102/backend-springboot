package com.example.movie.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.movie.entity.Actors;

@Repository
public interface ActorRepository extends JpaRepository<Actors, Long> {

}
