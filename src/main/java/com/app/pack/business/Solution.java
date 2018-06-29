package com.app.pack.business;

import com.app.pack.Exception.InvalidInputException;
import com.app.pack.pojo.Learner;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Created by jayakrishnansomasekharannair on 8/19/17.
 */

public class Solution {

    private static final int EXPECTED_FLOOR_INPUT_PARAM = 0;
    private static final int REVIEW_BIAS_FLOOR = -20;
    private static final int REVIEW_BIAS_CEIL = 20;
    private static final int LEARNER_ID_CEIL_PARAM = 1000;
    private static final int SUBMISSION_START_TRUE_SCORE_CEIL = 100;
    private static final int SUBMISSION_START_TICK_CEIL = 10000;
    private static final String LEARNER_INPUT_DELIMITER_PARAM = "\\s";

    public static void main(String args[]) throws Exception {

        List<?> inputList = new ArrayList<>();

        Solution solution = new Solution();
        //To read the input
        solution.readInputs(inputList);

        PeerReviewSimulator peerReviewSimulator = new PeerReviewSimulator();
        peerReviewSimulator.initAndSimulate(inputList);


    }

    /**
     * Validate and receive the input parameters
     *
     * - learnerId : The learner's ID, in the range [0,1000].
     * - firstSubmissionStartTick : The tick at which the learner start working on their first submission,
     * in the range [0,10000].
     * - firstSubmissionTrueScore : The ground truth score of their first submission, in [0,100].
     * This represents what score their first submission should receive from another learner reviewing it,
     * assuming the learner has 0 bias.
     * - reviewBias : The bias of of this learner when scoring, in [-20,20].
     * When this learner reviews other submissions, they will assign a score of {submissionTrueScore} + {reviewBias} ,
     * clamped to [0,100].
     *
     * @param inputList
     */
    private void readInputs(List inputList) {

        Scanner scan = new Scanner(System.in);

        try {

            if (null != inputList) {

                // D - the number of ticks for which the simulation runs
                int D = scan.nextInt();
                if(D < EXPECTED_FLOOR_INPUT_PARAM) {
                    throw new InvalidInputException("Number Of Ticks");
                }
                inputList.add(D);
                //N - number of learners
                int numberOfLearners = scan.nextInt();
                inputList.add(numberOfLearners);

                List<Learner> inputtedLearnerProperties = new ArrayList<Learner>();

                scan.useDelimiter(LEARNER_INPUT_DELIMITER_PARAM);
                //to read four learner parameters
                while (numberOfLearners-- > EXPECTED_FLOOR_INPUT_PARAM) {


                    int learnerId = scan.nextInt();
                    if(learnerId < EXPECTED_FLOOR_INPUT_PARAM || learnerId > LEARNER_ID_CEIL_PARAM) {
                        throw new InvalidInputException("learnerId");
                    }
                    int firstSubmissionStartTick = scan.nextInt();
                    if(firstSubmissionStartTick < EXPECTED_FLOOR_INPUT_PARAM || firstSubmissionStartTick > SUBMISSION_START_TICK_CEIL) {
                        throw new InvalidInputException("firstSubmissionStartTick");
                    }

                    int firstSubmissionTrueScore = scan.nextInt();
                    if(firstSubmissionStartTick < EXPECTED_FLOOR_INPUT_PARAM || firstSubmissionTrueScore > SUBMISSION_START_TRUE_SCORE_CEIL) {
                        throw new InvalidInputException("firstSubmissionStartTick");
                    }

                    int reviewBias = scan.nextInt();
                    if(reviewBias > REVIEW_BIAS_CEIL || reviewBias < REVIEW_BIAS_FLOOR) {
                        throw new InvalidInputException("reviewBias");
                    }

                    Learner learner = new Learner();
                    learner.setLearnerId(learnerId);
                    learner.setFirstSubmissionStartTick(firstSubmissionStartTick);
                    learner.setNextSubmissionStartTick(learner.getFirstSubmissionStartTick());
                    learner.setFirstSubmissionTrueScore(firstSubmissionTrueScore);
                    learner.setSubmissionTrueScore(learner.getFirstSubmissionTrueScore());
                    learner.setReviewBias(reviewBias);

                    inputtedLearnerProperties.add(learner);

                }

                inputList.add(inputtedLearnerProperties);

            }
        } catch (InvalidInputException ex) {
            forceExit();
        } catch(Exception ex){
            forceExit();
        } finally{
            scan.close();
        }

    }

    /**
     * Force exit in case if input receiving or validation fails.
     */
    private void forceExit() {
        System.exit(0);
    }

}


