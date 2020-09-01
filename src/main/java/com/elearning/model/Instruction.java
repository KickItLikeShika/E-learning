package com.elearning.model;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Instruction {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(nullable = false)
    private long userId;

    @NotNull
    @Column(nullable = false)
    private long courseId;

    public Instruction() {}

    public Instruction(long userId, long courseId) {
        this.userId = userId;
        this.courseId = courseId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }
}
