package com.elearning.dto;

public class ReviewDto {

    private String feedback;
    private int rank;

    public ReviewDto() {}

    public ReviewDto(String feedback, int rank) {
        this.feedback = feedback;
        this.rank = rank;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }
}
