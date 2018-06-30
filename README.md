# peerreviewsimulator

########### OVERVIEW #########

A peer review simulation is a discrete-time, deterministic, simulation of the interactions between learners.
	- In the simulation, time will move in discrete ticks [0,1,2,3 ...].
 	- As time ticks forward, the simulated learners will,
 					- work on and finish submissions 
 					- complete their reviews on others' submissions.
 					- resubmit until they pass.

Simulation progresses as follows.
	- At {firstSubmissionStartTick} , the learner immediately starts working on their submission.
	- The learner's submission appears in the pool for other learners to review the next tick.
	- When a learner finishes their submission, the learner submits the submission and immediately starts waiting for submissions to review.
	- Learners finishes review on a current tick if it past 20 ticks from review start tick.
	- A learner can be in the following states until he receive a passing score for their own submission.
			- waiting state to start their submission when time is their firstSubmissionStartTick.
			- starts reviewing (when a submission in the pool is ready and eligible for them to reviewed on the current tick)
			- When a learner finishes their review, the learner submits their review and :
     				- If the learner's latest submission has a failing score  : then the learner immediately starts working on their next submission.
     				- If the learner has submitted fewer than 3 * {theirSubmittedSubmissionCount} reviews, then the learner immediately starts waiting for submissions to review.
     				- If the learner's own submission does not have the score , then the learner immediately starts waiting for a score.
     				- If none of the above apply, then the learner is finished and they stop taking actions.		
			
	- Submissions are submitted to pool by learners when they are ready.
	- If the learner's latest submission has a failing score  : then the learner immediately starts working on their next submission.
    - If the pool contains submissions that they can start reviewing : then the learner selects one and immediately starts working on their review of that submission
    - learner doesn't do anything this tick if learner does not fall into any of the above.
    
    - Conditions for which submissions a learner can start reviewing :
     		- The learner may not start reviewing their own submissions.
     		- The learner may not start reviewing a submission that the learner had started reviewing in the past.
     		- Taking into account all actions in the current tick by learners with lower IDs, {number of reviews that the submission already has} + { number of learners who are working on reviewing the submission} must be less than 3.
    - Conditions of which submission a learner selects if there are multiple submissions that they can review :
     		- The earliest submitted submission.
     		- If there is a tie, the submission submitted by the learner with the lowest ID.
			

########### INPUT TO SIMULATION #########
     * - learnerId : The learner's ID, in the range [0,1000].
     * - firstSubmissionStartTick : The tick at which the learner start working on their first submission,
     * in the range [0,10000].
     * - firstSubmissionTrueScore : The ground truth score of their first submission, in [0,100].
     * This represents what score their first submission should receive from another learner reviewing it,
     * assuming the learner has 0 bias.
     * - reviewBias : The bias of of this learner when scoring, in [-20,20].
     * When this learner reviews other submissions, they will assign a score of {submissionTrueScore} + {reviewBias} ,
     * clamped to [0,100].
