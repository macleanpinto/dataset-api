package com.hackerrank.github.repository;

import java.util.List;
import java.util.Optional;

import com.hackerrank.github.model.Actor;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends CrudRepository<Actor, Long> {
    public List<Actor> findAll(Sort sort);

    public Optional<Actor> findById(Long id);

    @Query(value = "SELECT * FROM ACTOR a JOIN EVENT e GROUP BY e.id ORDER BY count(e.id) ASC", nativeQuery = true)
    List<Actor> findAllOrderByCountOfEvents();

}
