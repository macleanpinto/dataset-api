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

    @Query("SELECT a from Actor a JOIN Event e ON a.id=e.actor.id GROUP BY e.actor.id ORDER BY count(e.actor.id) DESC,max(e.createdAt) DESC,e.actor.login")
    List<Actor> findActorsOrderByNumberEventsDESC();

    @Query(value = "select max(streak) streak from (select count(*) streak from ( select row_number() over (order by min(e.created_at)) rowNumber, cast(min(e.created_at) as date) date, cast(min(e.created_at)-cast(row_number() over (order by min(e.created_at)) as int) as date) dateMinusRow from Event e where actor_id=:actorID group by cast(e.created_at as date) order by min(e.created_at)) grouped_days group by dateMinusRow) max_streak", nativeQuery = true)
    Integer findActorsMaximumStreak(@Param("actorID") long actorID);

}
