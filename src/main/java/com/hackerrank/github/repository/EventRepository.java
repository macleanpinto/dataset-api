package com.hackerrank.github.repository;

import java.util.List;

import com.hackerrank.github.model.Event;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    public List<Event> findAll(Sort sort);

    public List<Event> findByActorId(Long actorID, Sort sort);
}
