package com.elearning.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class EnrollmentRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(name = "statuss", nullable = false)
    private String status;

    @ManyToOne()
    private User user;

    @ManyToOne()
    private Course course;

    public EnrollmentRequest() {}

    public EnrollmentRequest(long id, String status,
                             User user, Course course) {
        this.id = id;
        this.status = status;
        this.user = user;
        this.course = course;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

//    public List<User> getUsers() {
//        return users;
//    }
//
//    public void addUser(User user) {
//        this.users.add(user);
//    }
//
//    public void removeUser(User user) {
//        this.users.remove(user);
//    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


//    public List<Course> getCourses() {
//        return courses;
//    }
//
//    public void addCourse(Course course) {
//        this.courses.add(course);
//    }
//
//    public void removeCourse(Course course) {
//        this.courses.remove(course);
//    }


    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
}
