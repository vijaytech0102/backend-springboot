package com.example.movie.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.movie.entity.Actors;
import com.example.movie.repo.ActorRepository;

@Service
public class ActorsService {

@Autowired   
ActorRepository actorRepository;

public Actors saveActors(Actors actors)
{
    System.out.println("saveActors method called in ActorsService");
    System.out.println("Received Actors object: " + actors);
 Actors obj=actorRepository.save(actors);
 return obj;
}

}

