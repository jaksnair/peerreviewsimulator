package com.app.pack.constants;

/**
 * Working durations for Submission and Review
 */
public enum WorkingDurations {

    SUBMISSION(50),
    REVIEW(20);

    private int value;

    WorkingDurations(int value) {

        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
