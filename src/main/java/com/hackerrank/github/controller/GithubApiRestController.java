package com.hackerrank.github.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
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
    public ResponseEntity<HttpStatus> addNewEvents(@RequestBody Event event) {
        if(eventRepository.findById(event.getId()).isPresent()) return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/erase")
    public ResponseEntity<HttpStatus> eraseAllEvents(){
        eventRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
