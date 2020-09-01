package com.elearning.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Assignment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Column(nullable = false)
    private String question;

    @NotNull
    @Column(nullable = false)
    private String correctAnswer;

    @ManyToOne
    private Course course;

    @OneToMany(mappedBy = "assignment")
    private List<UserAnswer> userAnswers = new ArrayList<>();

    public Assignment() {}

    public Assignment(String question, String correctAnswer, Course course) {
        this.question = question;
        this.correctAnswer = correctAnswer;
        this.course = course;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<UserAnswer> getUserAnswers() {
        return userAnswers;
    }

    public void addUserAnswer(UserAnswer userAnswer) {
        this.userAnswers.add(userAnswer);
    }

    public void removeUserAnswer(UserAnswer userAnswer) {
        this.userAnswers.remove(userAnswer);
    }
}