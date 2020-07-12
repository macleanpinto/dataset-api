package com.hackerrank.github.repository;

import java.util.List;
import java.util.Optional;

import com.hackerrank.github.model.Actor;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ActorRepository extends CrudRepository<Actor, Long> {
    public List<Actor> findAll(Sort sort);

    public Optional<Actor> findById(Long id);

    @Query("SELECT a from Actor a JOIN Event e ON a.id=e.actor.id GROUP BY e.actor.id ORDER BY count(e.actor.id) DESC,e.createdAt DESC,e.actor.login")
    List<Actor> findActorsOrderByNumberEventsDESC();

    @Query(value = "SELECT MAX(streak) FROM ( SELECT COUNT(*) streak FROM ( SELECT COUNT(*) amount, CAST(MIN(e.created_at)AS TIMESTAMP) date, CAST(MIN(e.created_at) - ROW_NUMBER() OVER (ORDER BY MIN(e.created_at)) as TIMESTAMP) dateMinusRow FROM Event e WHERE e.actor_id= :actorID GROUP BY CAST(e.created_at AS TIMESTAMP) ) groupedDays GROUP BY dateMinusRow) max_streak",nativeQuery = true)
    List<Actor> findActorsOrderByMaximumStreakDESC(@Param("actorID") long username);

}
