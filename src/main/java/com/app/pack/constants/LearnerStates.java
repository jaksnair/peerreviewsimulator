package com.app.pack.constants;

/**
 * Different Learner states during Peer Review
 */
public enum LearnerStates {

    WAITING_TO_SUBMIT(0),
    WORKING_ON_SUBMISSION(1),
    WAITING_FOR_SUBMITSSION_TO_REVIEW(2),
    WORKING_ON_NEXT_SUBMISSION(3),
    REVIEW_SUBMISSION(4),
    WAITING_FOR_A_GRADE(5),
    STOPPED_ACTIONS(6);

    private int value;

    LearnerStates(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
