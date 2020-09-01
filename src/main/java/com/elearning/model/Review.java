package com.elearning.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "feedbackk")
    private String feedback;

    @Column(name = "rankk")
    private long rank;

    private Date createdOn;

    @ManyToOne
    private Course course;

    @OneToOne
    private User user;

    public Review() {}

    public Review(String feedback, long rank,
                  Date createdOn, Course course,
                  User user) {
        this.feedback = feedback;
        this.rank = rank;
        this.createdOn = createdOn;
        this.course = course;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public long getRank() {
        return rank;
    }

    public void setRank(long rank) {
        this.rank = rank;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
