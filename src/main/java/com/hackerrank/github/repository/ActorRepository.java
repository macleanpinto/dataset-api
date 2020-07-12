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

    @Query("SELECT a from Actor a JOIN Event e ON a.id=e.actor.id GROUP BY e.actor.id ORDER BY count(e.actor.id),e.createdAt,e.actor.login DESC")
    List<Actor> findActorsOrderByNumberEventsDESC();

    @Query("SELECT a FROM Actor a")
	List<Actor> findActorsOrderByMaximumStreakDESC();

}
