package com.app.pack.business;

/**
 * Created by jayakrishnansomasekharannair on 8/19/17.
 */



import com.app.pack.constants.LearnerStates;
import com.app.pack.constants.ReviewStates;
import com.app.pack.constants.SubmissionStates;
import com.app.pack.constants.WorkingDurations;
import com.app.pack.pojo.Learner;
import com.app.pack.pojo.Review;
import com.app.pack.pojo.Submission;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * Class that simulates the peer review.
 *
 * It implements a discrete-time, deterministic, simulation of the interactions between learners taking a
 * peer-reviewed assignment. In the simulation, time will move in discrete ticks [0,1,2,3 ...].
 * As time ticks forward, the simulated learners will work on and finish submissions and reviews,
 * resubmitting until they pass. The input to simulation will be a set of learners.
 * Class will run for a duration specified in ticks and output the state of the simulation.
 *
 *
 */
class PeerReviewSimulator {

    private static final int PASS_GRADE = 240;

    private static final int IMPROVE_GRADE = 15;

    private static final int MAX_GRADE_SCORE = 100;

    private static final int  MIN_GRADE_SCORE = 0;

    private static final int SEQUENCE_NUMBER_CONSTANT = 31;

    private static final int REVIEW_COUNT_FOR_A_GRADE = 3;

    private  static final int BASE_SEQUENCE_NUMBER = 1;

    // Maximum duration for which the Peer Review should run
    private int maxTick;

    // Number of learners involved in the Peer Review.
    private int numberOfLearners;

    // Learners involved in the Peer Review.
    private List<Learner> learners;

    private List<Submission> submissionsPool = new LinkedList<Submission>();

    private void setMaxTick(int maxTick) {
        this.maxTick = maxTick;
    }

    private void setNumberOfLearners(int numberOfLearners) {
        this.numberOfLearners = numberOfLearners;
    }

    private void setLearners(List<Learner> learners) {
        this.learners = learners;
    }




    /**
     *
     *  Below methods drive the flow of the Peer Review
     *
     */


    /**
     * To initialize the inputs/entities of the Peer Review.
     * @param inputList
     */
    public void initAndSimulate(List<?> inputList) {

        if (!inputList.isEmpty() && inputList.size() >= 3) {

            setMaxTick((Integer) inputList.get(0));
            setNumberOfLearners((Integer) inputList.get(1));
            setLearners((List<Learner>) inputList.get(2));

            initializeLearnerStates ();
        }

        if (maxTick > 0) {
            simulate();
        }

    }


    /**
     *  Core method of simulation
     */
    private void simulate() {

        for (int currentTick = 0; currentTick < maxTick; currentTick++) {

            final int currTick = currentTick;

            // At {firstSubmissionStartTick} , the learner immediately starts working on their submission.
            learners.stream()
                    .filter(learner -> LearnerStates.WAITING_TO_SUBMIT.equals(learner.getLearnerStates())
                            && currTick == learner.getFirstSubmissionStartTick())
                    .forEach(learner-> startWorkingOnSubmission(learner, currTick));

            //When a learner finishes their submission, the learner submits the submission and immediately starts waiting for submissions to review.
            learners.stream()
                    .filter(learner -> (LearnerStates.WORKING_ON_SUBMISSION.equals(learner.getLearnerStates())
                            || LearnerStates.WORKING_ON_NEXT_SUBMISSION.equals(learner.getLearnerStates()))
                            && currTick == learner.getNextSubmissionStartTick() + WorkingDurations.SUBMISSION.getValue())
                    .forEach(learner -> submitToPool(learner, currTick));

            //When a learner finishes their submission, the learner submits the submission (the submission appears in the pool for other learners to review the next tick)
            submissionsPool.stream()
                    .filter(submission -> submission.getSubmissionTick()+1 == currTick
                            && submission.getSubmissionState().equals(SubmissionStates.IN_POOL))
                    .forEach(submission -> updateSubmissionState(submission,SubmissionStates.REVIEW_READY));

            //A learner who is in review finishes his review
            learners.stream()
                    .filter(learner -> LearnerStates.REVIEW_SUBMISSION.equals(learner.getLearnerStates())
                            && checkIfLearnerHasReviewsToSubmitOnCurrentTick(learner, currTick))
                    .forEach(learner-> doFinishReviewActivities(learner, currTick));

            //A learner who is waiting for submissions to review
            learners.stream()
                    .filter(learner -> LearnerStates.WAITING_FOR_SUBMITSSION_TO_REVIEW.equals(learner.getLearnerStates()))
                    .forEach(learner-> doWaitingForSubmissionActivities(learner, currTick));

            //A learner who waits for a score
            learners.stream()
                    .filter(learner -> LearnerStates.WAITING_FOR_A_GRADE.equals(learner.getLearnerStates()))
                    .forEach(learner -> startWaitingForAGradeActivities(learner,currTick));
        }

        learners.stream().forEach(learner -> displayOutput(learner));
    }


    /**
     * Initialize all learners wait to submit until currentTick is their {firstSubmissionStartTick}
     */
    private  void initializeLearnerStates(){
        learners.stream().peek(learner -> learner.setLearnerStates(LearnerStates.WAITING_TO_SUBMIT))
                .forEach(learner -> learner.setLearnerStates(LearnerStates.WAITING_TO_SUBMIT));

    }


    /**
     * Learners starts working on submission
     *
     * @param learner
     * @param currentTick
     */
    private void startWorkingOnSubmission(Learner learner, int currentTick) {

        List<Submission> ownSubmissions = learner.getOwnSubmissions();
        Submission latestOwnSubmission = getLatestOwnSubmissionOfLearner(learner);

        Submission submission = new Submission();
        submission.setOwner(learner);
        submission.setSubmissionSequenceNumber(null != latestOwnSubmission ? latestOwnSubmission.getSubmissionSequenceNumber() + BASE_SEQUENCE_NUMBER : 0);
        submission.setSubmissionId(learner.getLearnerId() * SEQUENCE_NUMBER_CONSTANT + submission.getSubmissionSequenceNumber());
        submission.setSubmissionState(SubmissionStates.IN_SUBMISSION);
        submission.setSubmissionStartTick(currentTick);

        if(null == ownSubmissions) {

            //first submission
            updateLearnerState(learner, LearnerStates.WORKING_ON_SUBMISSION);
            ownSubmissions = new ArrayList<Submission>();
            learner.setOwnSubmissions(ownSubmissions);

        } else {
            //next submission
            updateLearnerState(learner,LearnerStates.WORKING_ON_NEXT_SUBMISSION);
            int trueScore = learner.getSubmissionTrueScore() + IMPROVE_GRADE;
            trueScore = trueScore >= MAX_GRADE_SCORE ? MAX_GRADE_SCORE : trueScore;
            learner.setSubmissionTrueScore(trueScore);

        }

        learner.getOwnSubmissions().add(submission);

    }


    /**
     * Submissions are submitted to pool by learners when they are ready (when it reached 20 ticks past review start)
     * @param learner
     * @param currentTick
     */
    private void submitToPool (Learner learner, int currentTick) {

        Submission submission = getLatestOwnSubmissionOfLearner(learner);
        updateSubmissionState(submission,SubmissionStates.IN_POOL);
        submission.setSubmissionTick(currentTick);

        submissionsPool.add(submission);

        updateLearnerState(learner,LearnerStates.WAITING_FOR_SUBMITSSION_TO_REVIEW);

    }


    /**
     *
     * If the learner's latest submission has a failing score  : then the learner immediately starts working on their next submission.
     * If the pool contains submissions that they can start reviewing : then the learner selects one and immediately starts working on their review of that submission
     * If none of the above apply , then the learner doesn't do anything this tick.
     *
     * @param learner
     * @param currentTick
     */
    private void doWaitingForSubmissionActivities(Learner learner, int currentTick) {

        Submission submission = getLatestOwnSubmissionOfLearner(learner);

        if(hasSubmissionGotAFailingGrade(submission)) {
            learner.setNextSubmissionStartTick(currentTick);
            startWorkingOnSubmission(learner,currentTick);
        } else {
            Submission reviewableSubmission = submissionInPoolEligibleToBeReviewedByLearner(learner);
            if(null != reviewableSubmission ) {
                startSubmissionReview(learner,reviewableSubmission,currentTick);
            }
        }

    }



    /**
     * Conditions for which submissions a learner can start reviewing :
     *  - The learner may not start reviewing their own submissions.
     *  - The learner may not start reviewing a submission that the learner had started reviewing in the past.
     *  - Taking into account all actions in the current tick by learners with lower IDs, {number of reviews that the submission already has} + { number of learners who are working on reviewing the submission} must be less than 3.
     *  Conditions of which submission a learner selects if there are multiple submissions that they can review :
     *  - The earliest submitted submission.
     *  - If there is a tie, the submission submitted by the learner with the lowest ID.
     *
     * @param learner
     * @return
     */
    private Submission submissionInPoolEligibleToBeReviewedByLearner(Learner learner) {

        Submission reviewableSubmission = null;

        List<Submission> reviewableSubmissionsInPool = submissionsPool.stream()
                .filter(submission -> null != submission
                        && (SubmissionStates.REVIEW_READY == submission.getSubmissionState()
                        || SubmissionStates.IN_REVIEW == submission.getSubmissionState())
                        && !submission.getOwner().equals(learner)
                        && (null == submission.getReviews() ||
                        (null !=submission.getReviewers() &&  !submission.getReviewers().contains(learner)))
                        && (null == submission.getReviews() ||
                        (null != submission.getReviews() && submission.getReviews().size() < 3)))
                .collect(Collectors.toList());

        if (null != reviewableSubmissionsInPool && reviewableSubmissionsInPool.size() == 1) {

            reviewableSubmission =  reviewableSubmissionsInPool.get(0);

        } else if (null != reviewableSubmissionsInPool && reviewableSubmissionsInPool.size() > 1) {

            Comparator<Submission> bySubmissionTick =
                    Comparator.comparing(submission -> submission.getSubmissionTick());
            Comparator<Submission> bySubmissionOwnerId =
                    Comparator.comparing(submission -> submission.getOwner().getLearnerId());

            reviewableSubmission =  reviewableSubmissionsInPool.stream()
                    .sorted(bySubmissionTick.thenComparing(bySubmissionOwnerId)).findFirst().get();


        }

        return reviewableSubmission;
    }




    /**
     * A reviewer aka learner starts reviewing a submission (when a submission in the pool is ready and eligible for
     * them to reviewed on the current tick)
     *
     * @param learner
     * @param submission
     * @param currentTick
     */
    private void startSubmissionReview(Learner learner, Submission submission, int currentTick) {

        if (null != learner && null != submission ) {

            updateSubmissionState(submission,SubmissionStates.IN_REVIEW);

            Review review = new Review();

            review.setReviewId(generateReviewIdForSubmissionForLearner(learner,submission));
            review.setReviewer(learner);
            review.setSubmission(submission);
            review.setReviewStartTick(currentTick);
            updateReviewState(review, ReviewStates.CURRENT);

            if(null == learner.getReviewedSubmissions()) {
                learner.setReviewedSubmissions(new ArrayList<>());
            }
            learner.getReviewedSubmissions().add(submission);
            if(null == learner.getReviews()) {
                learner.setReviews(new ArrayList<>());
            }
            learner.getReviews().add(review);
            updateLearnerState(learner,LearnerStates.REVIEW_SUBMISSION);

            if(null == submission.getReviewers()) {
                submission.setReviewers(new ArrayList<>());
            }
            submission.getReviewers().add(learner);

            if(null == submission.getReviews()) {
                submission.setReviews(new ArrayList<>());
            }
            submission.getReviews().add(review);

        }
    }

    /**
     * When a learner finishes their review, the learner submits their review and :
     * If the learner's latest submission has a failing score  : then the learner immediately starts working on their next submission.
     * If the learner has submitted fewer than 3 * {theirSubmittedSubmissionCount} reviews, then the learner immediately starts waiting for submissions to review.
     * If the learner's own submission does not have the score , then the learner immediately starts waiting for a score.
     * If none of the above apply, then the learner is finished and they stop taking actions.
     *
     * @param learner
     * @param currentTick
     */
    private void doFinishReviewActivities(Learner learner, int currentTick) {

        List<Review> reviewsToFinish  = learner.getReviews().stream()
                .filter(review ->currentTick == review.getReviewStartTick() + WorkingDurations.REVIEW.getValue())
                .collect(Collectors.toList());

        reviewsToFinish.stream().forEach(review->finishReview(review, currentTick));

        Submission latestOwnSubmissionOfLearner = getLatestOwnSubmissionOfLearner(learner);

        if(hasSubmissionGotAFailingGrade(latestOwnSubmissionOfLearner)) {
            learner.setNextSubmissionStartTick(currentTick);
            startWorkingOnSubmission(learner,currentTick);
        } else if(!hasLearnerFinishedReviews(learner)) {
            updateLearnerState(learner,LearnerStates.WAITING_FOR_SUBMITSSION_TO_REVIEW);
        } else if (!hasSubmissionGotAGrade(latestOwnSubmissionOfLearner)) {
            updateLearnerState(learner,LearnerStates.WAITING_FOR_A_GRADE);
        } else {
            updateLearnerState(learner,LearnerStates.STOPPED_ACTIONS);
        }

    }


    /**
     * Learners finishes review on a current tick if it past 20 ticks from review start tick.
     *
     * @param review
     */
    public void finishReview(Review review, int currentTick) {

        if(null != review) {

            int reviewScore = review.getSubmission().getOwner().getSubmissionTrueScore() + review.getReviewer().getReviewBias();

            review.setScore(((reviewScore >= MAX_GRADE_SCORE) ? MAX_GRADE_SCORE : ((reviewScore <= MIN_GRADE_SCORE) ? MIN_GRADE_SCORE: reviewScore)));
            updateReviewState(review,ReviewStates.PAST);

            Submission submission = review.getSubmission();
            submission.setScoreSum(submission.getScoreSum()+review.getScore());

            if(submission.getReviews().size() == REVIEW_COUNT_FOR_A_GRADE) {
                submission.setGradeTick(currentTick);
            }

            if((null != submission.getReviews()) && (learners.size() == submission.getReviews().size()+1)
                    && (submission.getReviews().stream()
                    .filter(submissionReview -> ReviewStates.PAST.equals(submissionReview.getReviewState()))
                    .count() == submission.getReviews().size())) {
                updateSubmissionState(submission,SubmissionStates.REVIEWS_COMPLETED);
                submissionsPool.remove(submission);
            }
        }

    }


    /**
     * To display the output
     *
     * @param learner
     */
    private void displayOutput (Learner learner) {

        learner.getOwnSubmissions().stream()
                .filter(submission -> submission.getSubmissionTick() > 0 || submission.getScoreSum() > 0)
                .peek(submission -> submission.setSubmissionId(submission.getSubmissionId()- (learner.getLearnerId()*SEQUENCE_NUMBER_CONSTANT)))
                .forEach(submission -> System.out.println(submission.getOwner().getLearnerId() + " "+ submission.getSubmissionSequenceNumber()+" "+submission.getSubmissionTick()+" "+submission.getScoreSum()+" "+ submission.getGradeTick()));

    }



    /**
     *
     * Below are helper/aid methods that helps main methods that drive the Peer Review.
     *
     */


    /**
     * Updates the learner's state at current Tick
     * @param learner
     * @param learnerState
     */
    private void updateLearnerState(Learner learner, LearnerStates learnerState) {
        learner.setLearnerStates(learnerState);
    }

    /**
     * Updates the submission's state at current Tick
     * @param submission
     * @param submissionState
     */
    private void updateSubmissionState(Submission submission, SubmissionStates submissionState) {
        submission.setSubmissionState(submissionState);
    }

    /**
     * Updates the review's state at current Tick
     * @param review
     * @param reviewState
     */
    private void updateReviewState(Review review, ReviewStates reviewState) {
        review.setReviewState(reviewState);
    }


    /**
     * This returns the latest submission a learner done.
     *
     * @param learner
     * @return
     */
    private Submission getLatestOwnSubmissionOfLearner(Learner learner) {

        Submission submission = null;

        if(null != learner.getOwnSubmissions() && !learner.getOwnSubmissions().isEmpty()) {
            submission = learner.getOwnSubmissions().get(learner.getOwnSubmissions().size() - 1);
        }

        return submission;

    }


    /**
     * A submission is considered to have a grade if it has 3 reviews.
     *
     * @param submission
     * @return
     */
    private boolean hasSubmissionGotAGrade(Submission submission) {

        boolean result = false;
        if(null != submission && null !=submission.getReviews()) {

            result = REVIEW_COUNT_FOR_A_GRADE == (submission.getReviews().stream()
                    .filter(review -> ReviewStates.PAST.equals(review.getReviewState())).count());
        }

        return result;
    }

    /**
     * The grade is computed as the sum of the scores of the reviews.
     * A grade is failing if it is less than 240.
     *
     * @param submission
     * @return
     */
    private boolean hasSubmissionGotAFailingGrade(Submission submission) {

        return (null != submission && hasSubmissionGotAGrade(submission) && submission.getScoreSum() < PASS_GRADE);

    }

    /**
     * Aid method to generate reviewId.
     * @param learner
     * @param submission
     * @return
     */
    private String generateReviewIdForSubmissionForLearner(Learner learner, Submission submission) {

        return (learner.getLearnerId()+"-"+submission.getSubmissionId());

    }


    /**
     * Method checks if learner has reviews to submit on current tick.
     *
     * @param learner
     * @param currentTick
     * @return
     */
    private boolean checkIfLearnerHasReviewsToSubmitOnCurrentTick (Learner learner, int currentTick) {

        return  (null != learner.getReviews() && learner.getReviews().stream()
                .filter(review-> currentTick == review.getReviewStartTick() + WorkingDurations.REVIEW.getValue())
                .count() > 0);

    }

    /**
     * Method returns whether the learner finished their reviews or not.
     *
     * @param learner
     * @return
     */
    private boolean hasLearnerFinishedReviews(Learner learner) {

        int ownSubmissionCount = null != learner.getOwnSubmissions() ? learner.getOwnSubmissions().size() : 0;
        int submittedReviewsCount = Math.toIntExact(learner.getReviews().stream()
                .filter(review -> ReviewStates.PAST.equals(review.getReviewState())).count());
        boolean result =  (submittedReviewsCount == 3 * ownSubmissionCount);

        return result;
    }


    /**
     * A learner waiting for score checks if their latest submission has a score :
     *  If the score is failing , then they immediately start working on their next submission.
     *  If the score is passing, then the learner is finished and they stop taking actions.
     *
     * @param learner
     * @param currentTick
     */
    private void startWaitingForAGradeActivities (Learner learner, int currentTick) {

        if(!hasSubmissionGotAFailingGrade(getLatestOwnSubmissionOfLearner(learner))) {
            updateLearnerState(learner,LearnerStates.STOPPED_ACTIONS);
        } else {
            learner.setNextSubmissionStartTick(currentTick);
            startWorkingOnSubmission(learner,currentTick);
        }
    }
}
