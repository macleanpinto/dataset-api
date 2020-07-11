package com.hackerrank.github.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.hackerrank.github.model.Event;
import com.hackerrank.github.repository.EventRepository;

@RestController
public class GithubApiRestController {
    @Autowired
    private EventRepository eventRepository;

    @PostMapping("/events")
    public ResponseEntity<HttpStatus> createEvent(@RequestBody Event event) {
        if(eventRepository.findById(event.getId()).isPresent()) return ResponseEntity.ok().body(HttpStatus.BAD_REQUEST);
        eventRepository.save(event);
        return ResponseEntity.ok().body(HttpStatus.CREATED);
    }
}
