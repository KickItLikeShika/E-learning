package com.elearning.model;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
public class UserAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(nullable = false)
    private String answer;

    private String grade;

    @ManyToOne
    private User user;

    @ManyToOne
    private Assignment assignment;

    public UserAnswer() {}

    public UserAnswer(String answer, String grade,
                      User user, Assignment assignment) {
        this.answer = answer;
        this.grade = grade;
        this.user = user;
        this.assignment = assignment;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Assignment getAssignment() {
        return assignment;
    }

    public void setAssignment(Assignment assignment) {
        this.assignment = assignment;
    }
}
