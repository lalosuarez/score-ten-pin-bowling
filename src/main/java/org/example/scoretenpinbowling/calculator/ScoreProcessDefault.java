package org.example.scoretenpinbowling.calculator;

import org.example.scoretenpinbowling.file.ScoreFileRow;
import org.example.scoretenpinbowling.representation.BallScoreInput;
import org.example.scoretenpinbowling.representation.BallScoreOutput;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class ScoreProcessDefault implements ScoreProcess {
    private final BallScoreOutput ballScoreOutput;
    private final BallScoreInput ballScoreInput;

    private static final Integer FIRST_BALL_IDX = 0;
    private static final Integer SECOND_BALL_IDX = 1;
    private static final Integer THIRD_BALL_IDX = 2;
    private static final Integer TEN = 10;
    private static final Integer ZERO = 0;
    private static final int MAX_NUMBER_OF_FRAMES_PER_GAME = 10;

    public ScoreProcessDefault(BallScoreOutput ballScoreOutput, BallScoreInput ballScoreInput) {
        this.ballScoreOutput = ballScoreOutput;
        this.ballScoreInput = ballScoreInput;
    }

    @Override
    public Frame[] processScoresByPlayer(String playerName, List<ScoreFileRow> scores) {
        // Stores the scores that are considered a "plus"
        final Queue<String> plusScoresToBeAdded = new LinkedList<>();
        // Stores the indexes for the frames who score needs to be adjusted because of an strike
        final Queue<Integer> adjustScoreForStrikes = new LinkedList<>();

        int frameNumber = 1, prevFrameIdx = -1;
        boolean spareDetected = false;
        final Frame[] frames = new Frame[MAX_NUMBER_OF_FRAMES_PER_GAME];
        Frame currentFrame = new Frame(ballScoreOutput, ballScoreInput, frameNumber);
        for (int i = 0; i < scores.size(); i++) {

            // Handle scenario for more throws that the allowed within 10 frames
            if (currentFrame.isBallRollsCompleted()) {
                throw new IllegalStateException(
                        String.format("Max number of throws reached within 10 frames for player %s", playerName));
            }

            final String ballRollScore = scores.get(i).getBallScore();
            // currentFrame.setBallRollScore(ballRollScore);
            process(currentFrame, ballRollScore);

            // Rule for spare: you get 10 pins + your next ball
            if (spareDetected) {
                plusScoresToBeAdded.add(ballRollScore);
                spareDetected = processSpare(prevFrameIdx, frames, plusScoresToBeAdded);
            } else if (!adjustScoreForStrikes.isEmpty()) {
                // Rule for strike: you get 10 pins + your next two balls
                plusScoresToBeAdded.add(ballRollScore);
                processStrike(frameNumber, frames, plusScoresToBeAdded, adjustScoreForStrikes);
            }

            if (currentFrame.isBallRollsCompleted()) {
                // Adds the previous score to the current one, just for spare an regular scenarios
                if (arrayIndexIsValid(prevFrameIdx) && !currentFrame.hasStrike()) {
                    setFinalScore(frames[prevFrameIdx].getScore() + currentFrame.getScore(), currentFrame);
                }

                // Adds the frame to the array
                final int nextArrayIdx = frameNumber - 1;
                frames[nextArrayIdx] = currentFrame;
                // Sets the previous frame index
                prevFrameIdx = nextArrayIdx;

                spareDetected = currentFrame.hasSpare();
                if (currentFrame.hasStrike()) {
                    if (!adjustScoreForStrikes.contains(nextArrayIdx) && (i < scores.size() - 1)) {
                        adjustScoreForStrikes.add(nextArrayIdx);
                    }
                }

                if (frameNumber == MAX_NUMBER_OF_FRAMES_PER_GAME) {
                    adjustFinalScoreForLastFrame(scores, prevFrameIdx, frames, currentFrame,
                            adjustScoreForStrikes, i);
                    continue;
                }

                // Increases the frame number and creates a new frame
                // This means it is done with the current frame and will start processing a new one
                frameNumber++;
                currentFrame = new Frame(ballScoreOutput, ballScoreInput, frameNumber);
            }
        }
        return frames;
    }

    private void adjustFinalScoreForLastFrame(List<ScoreFileRow> scores, int prevFrameIdx, Frame[] frames, Frame currentFrame, Queue<Integer> adjustScoreForStrikes, int i) {
        if (i == (scores.size() - 1)) {
            if ((!adjustScoreForStrikes.isEmpty()) || !currentFrame.isFinished()) {
                adjustToFinalScore(String.valueOf(frames[prevFrameIdx - 1].getScore()), frames[prevFrameIdx]);
            }
        }
    }

    private boolean processSpare(int prevFrameIdx, Frame[] frames, Queue<String> plusScoresToBeAdded) {
        if (plusScoresToBeAdded.size() == 1) {
            adjustToFinalScore(plusScoresToBeAdded.poll(), frames[prevFrameIdx]);
            // At this point the queue is empty
            return false;
        }
        return true;
    }

    private void processStrike(int frameNumber, Frame[] frames, Queue<String> plusScoresToBeAdded, Queue<Integer> adjustScoreForStrikes) {
        if (plusScoresToBeAdded.size() == 2) {
            // gets first element from adjustScoreForStrikes queue
            final int idxToAdjust = adjustScoreForStrikes.poll();
            final Frame lastElem =
                    arrayIndexIsValid(frameNumber - 2) ? frames[frameNumber - 2] : null;
            final Frame previousFromLast =
                    arrayIndexIsValid(frameNumber - 3) ? frames[frameNumber - 3] : null;
            // Adjusts the score for that element with the values from plusScoresToBeAdded queue
            String firstScoreToAdd, secondScoreToAdd;
            if (previousFromLast != null && previousFromLast.hasStrike()
                    && !previousFromLast.isFinished() && lastElem != null
                    && lastElem.hasStrike()) { // && !currentFrame.hasStrike()
                // Peek from the Q not remove, otherwise will loose the value to adjust the next strike
                firstScoreToAdd = plusScoresToBeAdded.poll();
                secondScoreToAdd = plusScoresToBeAdded.peek();
            } else {
                firstScoreToAdd = plusScoresToBeAdded.poll();
                secondScoreToAdd = plusScoresToBeAdded.poll();
            }
            adjustToFinalScore(firstScoreToAdd, frames[idxToAdjust]);
            adjustToFinalScore(secondScoreToAdd, frames[idxToAdjust]);

            // Gets the previous from the previous index and adds the previous from the previous score
            final int idxPreviousPrevious = idxToAdjust - 1;
            if (arrayIndexIsValid(idxPreviousPrevious)) {
                adjustToFinalScore(String.valueOf(frames[idxPreviousPrevious].getScore()), frames[idxToAdjust]);
            }
        }
    }

    private boolean arrayIndexIsValid(int prevFrameIdx) {
        return prevFrameIdx != -1;
    }

    private void process(final Frame frame, final String ballRollScore) {
        if (isZero(ballRollScore)) {
            processZero(frame);
        } else if (isFoul(ballRollScore)) {
            // Foul scenario
            processFoul(frame);
        } else if (isStrike(ballRollScore)) {
            // Strike scenario
            processStrike(ballRollScore, frame);
        } else {
            // Regular and spare scenarios
            processSpareAndRegular(ballRollScore, frame);
        }
    }

    private boolean isZero(final String ballScore) {
        return ballScoreInput.getZero().equalsIgnoreCase(ballScore);
    }

    private boolean isFoul(final String ballScore) {
        return ballScoreInput.getFoul().equalsIgnoreCase(ballScore);
    }

    private boolean isStrike(final String ballScore) {
        return TEN.equals(convertToInt(ballScore));
    }

    private boolean isSpare(final String ballRollScore, final Frame frame) {
        if (isFoul(frame.getBallRollsScores()[FIRST_BALL_IDX])) {
            return (ZERO + convertToInt(ballRollScore)) == 10;
        }
        return (convertToInt(frame.getBallRollsScores()[FIRST_BALL_IDX]) + convertToInt(ballRollScore)) == 10;
    }

    private void processZero(final Frame frame) {
        if (isFirstBall(frame)) {
            setBallScore(FIRST_BALL_IDX, ballScoreOutput.getZero(), frame);
            addScore(ZERO, frame);
        } else if (isSecondBall(frame)) {
            setBallScore(SECOND_BALL_IDX, ballScoreOutput.getZero(), frame);
            addScore(ZERO, frame);
            frame.setCompleted();
        } else {
            setBallScore(THIRD_BALL_IDX, ballScoreOutput.getZero(), frame);
            addScore(ZERO, frame);
            frame.setCompleted();
        }
    }

    private void processFoul(final Frame frame) {
        if (isFirstBall(frame)) {
            setBallScore(FIRST_BALL_IDX, ballScoreOutput.getFoul(), frame);
            addScore(ZERO, frame);
        } else if (isSecondBall(frame)) {
            setBallScore(SECOND_BALL_IDX, ballScoreOutput.getFoul(), frame);
            addScore(ZERO, frame);
            frame.setCompleted();
        } else {
            setBallScore(THIRD_BALL_IDX, ballScoreOutput.getFoul(), frame);
            addScore(ZERO, frame);
            frame.setCompleted();
        }
    }

    private void processStrike(final String ballRollScore, final Frame frame) {
        // Special condition for frame 10
        if (TEN.equals(frame.getNumber())) {
            if (isFirstBall(frame)) {
                setBallScore(FIRST_BALL_IDX, ballScoreOutput.getStrike(), frame);
            } else if (isSecondBall(frame)) {
                setBallScore(SECOND_BALL_IDX, ballScoreOutput.getStrike(), frame);
            } else {
                setBallScore(THIRD_BALL_IDX, ballScoreOutput.getStrike(), frame);
                frame.setCompleted();
            }
        } else {
            setBallScore(SECOND_BALL_IDX, ballScoreOutput.getStrike(), frame);
        }
        frame.setHasStrike();
        addScore(convertToInt(ballRollScore), frame);
        if (!isThreeBallFrameWithStrike(frame)) {
            frame.setCompleted();
        }
    }

    private void processSpareAndRegular(final String ballRollScore, final Frame frame) {
        if (isFirstBall(frame)) {
            setBallScore(FIRST_BALL_IDX, ballRollScore, frame);
            addScore(convertToInt(ballRollScore), frame);
        } else if (isSecondBall(frame)) {
            if (isSpare(ballRollScore, frame)) {
                frame.setHasSpare();
                setBallScore(SECOND_BALL_IDX, ballScoreOutput.getSpare(), frame);
            } else {
                setBallScore(SECOND_BALL_IDX, ballRollScore, frame);
            }
            addScore(convertToInt(ballRollScore), frame);
            if (!isThreeBallFrameWithSpare(frame) && !isThreeBallFrameWithStrike(frame)) {
                frame.setCompleted();
            }
        } else {
            setBallScore(THIRD_BALL_IDX, ballRollScore, frame);
            addScore(convertToInt(ballRollScore), frame);
            frame.setCompleted();
        }
    }

    private boolean isThreeBallFrameWithSpare(final Frame frame) {
        return frame.getBallRollsScores().length == 3 && frame.hasSpare();
    }

    private boolean isThreeBallFrameWithStrike(final Frame frame) {
        return frame.getBallRollsScores().length == 3 && frame.hasStrike();
    }

    private boolean isFirstBall(final Frame frame) {
        return frame.getBallRollsScores()[FIRST_BALL_IDX] == null;
    }

    private boolean isSecondBall(final Frame frame) {
        return frame.getBallRollsScores()[SECOND_BALL_IDX] == null;
    }

    private void setBallScore(int idx, String ballScore, final Frame frame) {
        frame.getBallRollsScores()[idx] = ballScore;
    }

    public void adjustToFinalScore(final String ballScore, final Frame frame) {
        frame.setFinished();
        if (isFoul(ballScore)) return;
        addScore(convertToInt(ballScore), frame);
    }

    private int convertToInt(final String value) {
        if (ballScoreOutput.getStrike().equalsIgnoreCase(value)) return TEN;
        if (ballScoreOutput.getZero().equalsIgnoreCase(value)) return ZERO;
        if (ballScoreOutput.getFoul().equalsIgnoreCase(value)) return ZERO;
        return Integer.parseInt(value);
    }

    private void addScore(final int score, final Frame frame) {
        frame.setScore(frame.getScore() + score);
    }

    private void setFinalScore(final int score, final Frame frame) {
        frame.setFinished();
        frame.setScore(score);
    }
}
