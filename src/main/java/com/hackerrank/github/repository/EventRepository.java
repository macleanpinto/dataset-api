package com.hackerrank.github.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

import com.hackerrank.github.model.Event;

@Repository
public interface EventRepository extends CrudRepository<Event, Long> {

    public List<Event> findAllById(Sort sort);

    public List<Event> findAllByActorId(Long actorID, Sort sort);
}
