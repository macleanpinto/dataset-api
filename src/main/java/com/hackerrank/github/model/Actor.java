package com.hackerrank.github.model;

import static javax.persistence.CascadeType.PERSIST;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Actor {
    @Id
    private Long id;
    @Column
    private String login;
    @Column
    private String avatar;

    @OneToMany(cascade = PERSIST, mappedBy = "actor")
    private Set<Event> events;

    public Actor() {
    }

    public Actor(final Long id, final String login, final String avatar, final Set<Event> events) {
        this.id = id;
        this.login = login;
        this.avatar = avatar;
        this.events = events;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(final String login) {
        this.login = login;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(final String avatar) {
        this.avatar = avatar;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(final Set<Event> events) {
        this.events = events;
    }
}
