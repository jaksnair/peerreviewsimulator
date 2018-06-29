package com.app.pack.pojo;



import com.app.pack.constants.SubmissionStates;

import java.util.List;

/**
 * Submissions done by learners.
 *
 */
public class Submission {

    private int submissionId;
    private int submissionSequenceNumber;
    private Learner owner;
    private int submissionStartTick;
    private int submissionTick;
    private int gradeTick = -1;
    private int scoreSum;
    private SubmissionStates submissionState;
    private List<Learner> reviewers;
    private List<Review> reviews;

    public int getSubmissionId() {
        return submissionId;
    }

    public void setSubmissionId(int submissionId) {
        this.submissionId = submissionId;
    }

    public int getSubmissionSequenceNumber() {
        return submissionSequenceNumber;
    }

    public void setSubmissionSequenceNumber(int submissionSequenceNumber) {
        this.submissionSequenceNumber = submissionSequenceNumber;
    }

    public Learner getOwner() {
        return owner;
    }

    public void setOwner(Learner owner) {
        this.owner = owner;
    }

    public int getSubmissionStartTick() {
        return submissionStartTick;
    }

    public void setSubmissionStartTick(int submissionStartTick) {
        this.submissionStartTick = submissionStartTick;
    }

    public int getSubmissionTick() {
        return submissionTick;
    }

    public void setSubmissionTick(int submissionTick) {
        this.submissionTick = submissionTick;
    }

    public int getGradeTick() {
        return gradeTick;
    }

    public void setGradeTick(int gradeTick) {
        this.gradeTick = gradeTick;
    }

    public int getScoreSum() {
        return scoreSum;
    }

    public void setScoreSum(int scoreSum) {
        this.scoreSum = scoreSum;
    }

    public SubmissionStates getSubmissionState() {
        return submissionState;
    }

    public void setSubmissionState(SubmissionStates submissionState) {
        this.submissionState = submissionState;
    }

    public List<Learner> getReviewers() {
        return reviewers;
    }

    public void setReviewers(List<Learner> reviewers) {
        this.reviewers = reviewers;
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

        Submission that = (Submission) o;

        if (submissionId != that.submissionId) return false;
        if (submissionSequenceNumber != that.submissionSequenceNumber) return false;
        return owner != null ? owner.equals(that.owner) : that.owner == null;
    }

    @Override
    public int hashCode() {
        int result = submissionId;
        result = 31 * result + submissionSequenceNumber;
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Submission{");
        sb.append("submissionId=").append(submissionId);
        sb.append(", submissionSequenceNumber=").append(submissionSequenceNumber);
        sb.append(", owner=").append(owner);
        sb.append(", submissionStartTick=").append(submissionStartTick);
        sb.append(", submissionTick=").append(submissionTick);
        sb.append(", gradeTick=").append(gradeTick);
        sb.append(", scoreSum=").append(scoreSum);
        sb.append(", submissionState=").append(submissionState);
        sb.append(", reviewersCount=").append(null != reviewers ? reviewers.size() : 0);
        sb.append(", reviewsCount=").append(null != reviews ? reviews.size() : 0);
        sb.append('}');
        return sb.toString();
    }
}
