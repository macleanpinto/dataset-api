package com.hackerrank.github.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class Actor {
    @Id
    private Long id;
    @Column
    private String login;
    @Column
    @JsonProperty("avatar_url")
    private String avatar;

    public Actor() {
    }

    public Actor(final Long id, final String login, final String avatar) {
        this.id = id;
        this.login = login;
        this.avatar = avatar;
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

}
