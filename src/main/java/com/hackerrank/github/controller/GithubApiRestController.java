
package com.hackerrank.github.controller;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.hackerrank.github.controller.dto.ActorDTO;
import com.hackerrank.github.model.Actor;
import com.hackerrank.github.model.Event;
import com.hackerrank.github.repository.ActorRepository;
import com.hackerrank.github.repository.EventRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GithubApiRestController {
    public static final Sort SORT_BY_ID = Sort.by(Sort.Direction.ASC, "id");
    @Autowired
    private EventRepository eventRepository;
    @Autowired
    private ActorRepository actorRepository;

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
        final List<Event> events = eventRepository.findByActorId(actorID, SORT_BY_ID);
        if (events.size() > 0) {
            return ResponseEntity.status(HttpStatus.OK).body(events);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(events);
    }

    @PutMapping("/actors")
    public ResponseEntity<HttpStatus> updateActorAvatar(@RequestBody final Actor actor) {
        final Optional<Actor> oldEntry = actorRepository.findById(actor.getId());
        if (!oldEntry.isEmpty()) {
            if (oldEntry.get().getLogin().equals(actor.getLogin())) {

                actorRepository.save(actor);
                return ResponseEntity.status(HttpStatus.OK).build();
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/actors1")
    public ResponseEntity<List<Actor>> allActors() {
        return ResponseEntity.status(HttpStatus.OK).body(actorRepository.findActorsOrderByNumberEventsDESC());
    }

    @GetMapping(value = "/actors", produces = "application/json")
    public ResponseEntity<List<ActorDTO>> getActors() {
        List<Event> events = (List<Event>) eventRepository.findAll();
        List<Actor> actors = (List<Actor>) actorRepository.findAll();

        List<ActorTuple> actorTuples = new ArrayList<>();

        for (Actor actor : actors) {
            List<Event> collect = events.stream().filter(event -> event.getActor().equals(actor))
                    .sorted(Comparator.comparing(Event::getCreatedAt).reversed()).collect(Collectors.toList());
            if (!collect.isEmpty()) {
                actorTuples.add(new ActorTuple(actor, collect.size(), collect.get(0).getCreatedAt()));
            }
        }

        List<ActorDTO> actorList = getCollectionWithCriteria(actorTuples);

        return ResponseEntity.ok(actorList);
    }

    @RequestMapping(value = "/actors/streak1", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<List<Actor>> getAllActorsOrderByStreak(HttpServletRequest request) {
        Map<Integer, Actor> treeMap = new TreeMap<Integer, Actor>();
        List<Actor> actors = (List<Actor>) actorRepository.findAll();
        for (Actor actor : actors) {
            Integer streak = actorRepository.findActorsMaximumStreak(actor.getId());
            treeMap.put(streak, actor);
        }
        actors.clear();
        for (Actor actor : treeMap.values()) {
            actors.add(actor);
        }
        return new ResponseEntity<List<Actor>>(actors, HttpStatus.OK);
    }

    @GetMapping(value = "/actors/streak", produces = "application/json")
    public ResponseEntity<List<ActorDTO>> getActorsStreak() {
        List<Event> events = (List<Event>) eventRepository.findAll();
        List<Actor> actors = (List<Actor>) actorRepository.findAll();

        List<ActorTuple> actorTupleStreaks = new ArrayList<>();

        for (Actor actor : actors) {
            List<Event> collect = events.stream()
                    .filter(event -> event.getActor().equals(actor) && event.getType().equals("PushEvent"))
                    .sorted(Comparator.comparing(Event::getCreatedAt).reversed()).collect(Collectors.toList());

            if (!collect.isEmpty()) {
                if (collect.size() == 1) {
                    actorTupleStreaks.add(new ActorTuple(actor, 0, collect.get(0).getCreatedAt()));
                } else {
                    Integer mayorStreak = getStreak(collect);
                    actorTupleStreaks.add(new ActorTuple(actor, mayorStreak, collect.get(0).getCreatedAt()));
                }
            }
        }
        List<ActorDTO> actorList = getCollectionWithCriteria(actorTupleStreaks);
        return ResponseEntity.ok(actorList);
    }

    private int getStreak(List<Event> collect) {
        int mayorStreak = 0;
        int streak = 0;
        for (int i = collect.size() - 1; i > 0; i--) {
            LocalDateTime currentDate = collect.get(i).getCreatedAt().toLocalDateTime();
            LocalDateTime nextDate = collect.get(i - 1).getCreatedAt().toLocalDateTime();
            LocalDateTime currentDateEndOfDay = currentDate.with(ChronoField.NANO_OF_DAY, LocalTime.MAX.toNanoOfDay());

            long hours = ChronoUnit.HOURS.between(currentDate, nextDate);
            long hoursFinalDay = ChronoUnit.HOURS.between(currentDate, currentDateEndOfDay);
            long days = ChronoUnit.DAYS.between(currentDate, nextDate);

            if (currentDate.getDayOfMonth() == nextDate.getDayOfMonth() || days > 1) {
                streak = 0;
            } else if (hours - hoursFinalDay <= 24) {
                streak++;
                if (streak > mayorStreak)
                    mayorStreak = streak;
            }
        }
        return mayorStreak;
    }

    private List<ActorDTO> getCollectionWithCriteria(List<ActorTuple> actorTuples) {
        return actorTuples.stream().sorted(Comparator.comparing(o -> o.getActor().getLogin()))
                .sorted(Comparator.comparing(ActorTuple::getLast).reversed())
                .sorted(Comparator.comparing(ActorTuple::getCEvents).reversed()).map(ActorTuple::getActor)
                .map(ActorDTO::convertFrom).collect(Collectors.toList());
    }
}
