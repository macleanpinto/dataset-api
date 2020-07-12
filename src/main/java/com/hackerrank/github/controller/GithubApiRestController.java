package com.hackerrank.github.controller;

import java.util.List;

import com.hackerrank.github.model.Event;
import com.hackerrank.github.repository.EventRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubApiRestController {
    public static final Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");
    @Autowired
    private EventRepository eventRepository;

    @PostMapping("/events")
    public ResponseEntity<HttpStatus> addNewEvents(@RequestBody final Event event) {
        if (eventRepository.findById(event.getId()).isPresent())
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        eventRepository.save(event);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/erase")
    public ResponseEntity<HttpStatus> eraseAllEvents() {
        eventRepository.deleteAll();
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping("/events")
    public ResponseEntity<List<Event>> fetchAllEvents() {
        return ResponseEntity.status(HttpStatus.OK).body(eventRepository.findAll(SORT_BY_ID));
    }

    @GetMapping("/events/actors/{actorID}")
    public ResponseEntity<List<Event>> fetchAllEventsForActor(@PathVariable("actorID") final Long actorID) {
        List<Event> events = eventRepository.findByActorId(actorID, SORT_BY_ID);
        if (events.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(events);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(events);
    }
}
