package com.elearning.dto;

import com.elearning.model.Course;

public class LessonDto {

    private String reading;

    public LessonDto() {}

    public LessonDto(String reading) {
        this.reading = reading;
    }

    public String getReading() {
        return reading;
    }

    public void setReading(String reading) {
        this.reading = reading;
    }
}
