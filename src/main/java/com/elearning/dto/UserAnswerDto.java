package com.elearning.dto;

public class UserAnswerDto {

    private String answer;

    public UserAnswerDto() {}

    public UserAnswerDto(String answer) {
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
