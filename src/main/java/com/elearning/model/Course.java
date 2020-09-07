package com.elearning.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(nullable = false)
    private String title;

    @Lob
    private String description;

    @NotNull
    @Column(nullable = false)
    private String published;

    @NotNull
    @Column(nullable = false)
    private Date publishedOn;

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private List<Review> reviews = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private List<EnrollmentRequest> enrollmentRequests = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private List<Assignment> assignments = new ArrayList<>();

    @JsonIgnore
    @OneToMany(mappedBy = "course")
    private List<Lesson> lessons = new ArrayList<>();

    public Course() {}

    public Course(String title, String description,
                  String published, Date publishedOn) {
        this.title = title;
        this.description = description;
        this.published = published;
        this.publishedOn = publishedOn;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public Date getPublishedOn() {
        return publishedOn;
    }

    public void setPublishedOn(Date publishedOn) {
        this.publishedOn = publishedOn;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void addReview(Review review) {
        this.reviews.add(review);
    }

    public void removeReview(Review review) {
        this.reviews.remove(review);
    }

    public void clearReviews() {
        this.reviews.clear();
    }

    public List<EnrollmentRequest> getEnrollmentRequests() {
        return enrollmentRequests;
    }

    public void addEnrollmentRequest(EnrollmentRequest enrollmentRequest) {
        this.enrollmentRequests.add(enrollmentRequest);
    }

    public void removeEnrollmentRequest(EnrollmentRequest enrollmentRequest) {
        this.enrollmentRequests.remove(enrollmentRequest);
    }

    public void clearEnrollmentRequests() {
        this.enrollmentRequests.clear();
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void addAssignments(Assignment assignment) {
        this.assignments.add(assignment);
    }

    public void removeAssignments(Assignment assignment) {
        this.assignments.remove(assignment);
    }

    public void clearAssignments() {
        this.assignments.clear();
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void addLessons(Lesson lesson) {
        this.lessons.add(lesson);
    }

    public void removeLessons(Lesson lesson) {
        this.lessons.remove(lesson);
    }

    public void clearLessons() {
        this.lessons.clear();
    }
}
