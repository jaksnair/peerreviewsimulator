package com.app.pack.pojo;

import com.app.pack.constants.ReviewStates;

/**
 * Reviews of Submission done by Learners
 */
public class Review {

    private String reviewId;

    private Learner reviewer;
    private Submission submission;

    private int reviewStartTick;
    private int score;
    private ReviewStates reviewState;

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public Learner getReviewer() {
        return reviewer;
    }

    public void setReviewer(Learner reviewer) {
        this.reviewer = reviewer;
    }

    public Submission getSubmission() {
        return submission;
    }

    public void setSubmission(Submission submission) {
        this.submission = submission;
    }

    public int getReviewStartTick() {
        return reviewStartTick;
    }

    public void setReviewStartTick(int reviewStartTick) {
        this.reviewStartTick = reviewStartTick;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public ReviewStates getReviewState() {
        return reviewState;
    }

    public void setReviewState(ReviewStates reviewState) {
        this.reviewState = reviewState;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Review review = (Review) o;

        if (reviewId != review.reviewId) return false;
        if (reviewStartTick != review.reviewStartTick) return false;
        if (score != review.score) return false;
        if (reviewer != null ? !reviewer.equals(review.reviewer) : review.reviewer != null) return false;
        return submission != null ? submission.equals(review.submission) : review.submission == null;
    }

    @Override
    public int hashCode() {
        int result = reviewId != null ? reviewId.hashCode() : 0;
        result = 31 * result + (reviewer != null ? reviewer.hashCode() : 0);
        result = 31 * result + (submission != null ? submission.hashCode() : 0);
        result = 31 * result + reviewStartTick;
        return result;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Review{");
        sb.append("reviewId=").append(reviewer != null ? reviewer.getLearnerId() : 0);
        sb.append(", reviewer=").append(reviewer);
        sb.append(", submission=").append(submission != null ? submission.getSubmissionId() : 0);
        sb.append(", reviewStartTick=").append(reviewStartTick);
        sb.append(", score=").append(score);
        sb.append(", reviewState=").append(reviewState);
        sb.append('}');
        return sb.toString();
    }
}
