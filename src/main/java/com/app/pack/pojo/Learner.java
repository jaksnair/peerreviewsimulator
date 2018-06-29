package com.app.pack.pojo;



import com.app.pack.constants.LearnerStates;

import java.util.List;

/**
 * Learners in the Peer Review.
 *
 */

public class Learner {

    private int learnerId;
    private int firstSubmissionStartTick;
    private int firstSubmissionTrueScore;
    private int reviewBias;

    private int nextSubmissionStartTick;
    private int submissionTrueScore;
    private LearnerStates learnerStates;

    private List<Submission> ownSubmissions;
    private List<Submission> reviewedSubmissions;
    private List<Review> reviews;

    public int getLearnerId() {
        return learnerId;
    }

    public void setLearnerId(int learnerId) {
        this.learnerId = learnerId;
    }

    public int getFirstSubmissionStartTick() {
        return firstSubmissionStartTick;
    }

    public void setFirstSubmissionStartTick(int firstSubmissionStartTick) {
        this.firstSubmissionStartTick = firstSubmissionStartTick;
    }

    public int getFirstSubmissionTrueScore() {
        return firstSubmissionTrueScore;
    }

    public void setFirstSubmissionTrueScore(int firstSubmissionTrueScore) {
        this.firstSubmissionTrueScore = firstSubmissionTrueScore;
    }

    public int getReviewBias() {
        return reviewBias;
    }

    public void setReviewBias(int reviewBias) {
        this.reviewBias = reviewBias;
    }

    public int getNextSubmissionStartTick() {
        return nextSubmissionStartTick;
    }

    public void setNextSubmissionStartTick(int nextSubmissionStartTick) {
        this.nextSubmissionStartTick = nextSubmissionStartTick;
    }

    public int getSubmissionTrueScore() {
        return submissionTrueScore;
    }

    public void setSubmissionTrueScore(int submissionTrueScore) {
        this.submissionTrueScore = submissionTrueScore;
    }

    public LearnerStates getLearnerStates() {
        return learnerStates;
    }

    public void setLearnerStates(LearnerStates learnerStates) {
        this.learnerStates = learnerStates;
    }

    public List<Submission> getOwnSubmissions() {
        return ownSubmissions;
    }

    public void setOwnSubmissions(List<Submission> ownSubmissions) {
        this.ownSubmissions = ownSubmissions;
    }

    public List<Submission> getReviewedSubmissions() {
        return reviewedSubmissions;
    }

    public void setReviewedSubmissions(List<Submission> reviewedSubmissions) {
        this.reviewedSubmissions = reviewedSubmissions;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Learner learner = (Learner) o;

        if (learnerId != learner.learnerId) return false;
        if (firstSubmissionStartTick != learner.firstSubmissionStartTick) return false;
        if (firstSubmissionTrueScore != learner.firstSubmissionTrueScore) return false;
        return reviewBias == learner.reviewBias;
    }

    @Override
    public int hashCode() {
        int result = learnerId;
        result = 31 * result + firstSubmissionStartTick;
        result = 31 * result + firstSubmissionTrueScore;
        result = 31 * result + reviewBias;
        return result;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Learner{");
        sb.append("learnerId=").append(learnerId);
        sb.append(", firstSubmissionStartTick=").append(firstSubmissionStartTick);
        sb.append(", firstSubmissionTrueScore=").append(firstSubmissionTrueScore);
        sb.append(", reviewBias=").append(reviewBias);
        sb.append(", nextSubmissionStartTick=").append(nextSubmissionStartTick);
        sb.append(", submissionTrueScore=").append(submissionTrueScore);
        sb.append(", learnerStates=").append(learnerStates);
        sb.append(", ownSubmissionsCount=").append(null != ownSubmissions ? ownSubmissions.size() : 0);
        sb.append(", reviewedSubmissionsCount=").append(null != reviewedSubmissions ?reviewedSubmissions.size(): 0 );
        sb.append(", reviewsCount=").append(null != reviews ?reviews.size(): 0 );
        sb.append('}');
        return sb.toString();
    }
}
