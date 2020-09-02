package com.elearning.dto;

import com.elearning.model.Assignment;
import com.elearning.model.Lesson;

import java.util.ArrayList;
import java.util.List;

public class CourseInfoDto {

    private String title;
    private String description;
    private List<Assignment> assignments = new ArrayList<>();
    private List<Lesson> lessons = new ArrayList<>();

    public CourseInfoDto() {}

    public CourseInfoDto(String title, String description,
                         List<Assignment> assignments, List<Lesson> lessons) {
        this.title = title;
        this.description = description;
        this.assignments = assignments;
        this.lessons = lessons;
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

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void setAssignments(List<Assignment> assignments) {
        this.assignments = assignments;
    }

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
