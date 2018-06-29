package com.app.pack.constants;

/**
 * Different states of Review during Peer Review
 */
public enum ReviewStates {

    CURRENT(0),
    PAST(1);

    private int value;

    ReviewStates(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
