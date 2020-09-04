package com.elearning.dto;

public class CourseInfoDto {

    private String title;
    private String description;

    public CourseInfoDto() {}

    public CourseInfoDto(String title, String description) {
        this.title = title;
        this.description = description;

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
}
