package com.example.movie.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movie.service.ActorsService;
import com.example.movie.entity.Actors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/actors")
public class ActorController {

@Autowired
ActorsService actorsService;

@PostMapping("/save")
public Actors saveActors(@RequestBody Actors actors)
{
    System.out.println("saveActors method called in ActorController");
    System.out.println("Received Actors object: " + actors);
    Actors obj= actorsService.saveActors(actors);
    return obj;
}

}
