package com.app.pack.constants;

/**
 * Different states of Submission during Peer Review
 */
public enum SubmissionStates {

    IN_SUBMISSION(0),
    IN_POOL(1),
    REVIEW_READY(2),
    IN_REVIEW(3),
    REVIEWS_COMPLETED(4);

    private int value;

    SubmissionStates(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
