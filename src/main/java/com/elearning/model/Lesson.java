package com.elearning.model;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Lob
    @Column(nullable = false)
    private String reading;

    @ManyToOne
    private Course course;

    public Lesson() {}

    public Lesson(String reading, Course course) {
        this.reading = reading;
        this.course = course;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
