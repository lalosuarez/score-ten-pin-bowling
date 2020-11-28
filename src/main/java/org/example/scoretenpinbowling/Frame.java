package org.example.scoretenpinbowling;

import java.util.Arrays;

public class Frame {
    private int number;
    private String[] ballRollScores;
    private int score;
    private boolean completed;
    private boolean hasSpare;
    private boolean hasStrike;

    private static final Integer TEN = 10;
    private static final Integer FIRST_BALL_IDX = 0;
    private static final Integer SECOND_BALL_IDX = 1;
    private static final Integer THIRD_BALL_IDX = 2;
    private static final String SPARE_CHAR = "/";
    private static final String STRIKE_CHAR = "X";
    private static final String FOUL_CHAR = "F";

    public Frame(int frameNumber) {
        this.number = frameNumber;
        // IF you pull a strike in the first ball at frame 10, you get two more balls
        // because it has to add those two up
        this.ballRollScores = TEN.equals(frameNumber) ? new String[3] : new String[2];
        this.score = 0;
        this.completed = false;
        this.hasSpare = false;
        this.hasStrike = false;
    }

    public void setBallRollScore(final String ballRollScore) {
        if (isFoul(ballRollScore)) {
            // Foul scenario
            processFoul(ballRollScore);
        } else if (isStrike(ballRollScore)) {
            // Strike scenario
            processStrike(ballRollScore);
        } else {
            // Regular and spare scenarios
            processSpareAndRegular(ballRollScore);
        }
    }

    private void processSpareAndRegular(String ballRollScore) {
        if (isFirstBall()) {
            setBallScore(FIRST_BALL_IDX, ballRollScore);
            addScore(convertToInt(ballRollScore));
        } else if (isSecondBall()) {
            if (isSpare(ballRollScore)) {
                setHasSpare();
                setBallScore(SECOND_BALL_IDX, SPARE_CHAR);
            } else {
                setBallScore(SECOND_BALL_IDX, ballRollScore);
            }
            addScore(convertToInt(ballRollScore));
            setCompleted();
        } else {
            setBallScore(THIRD_BALL_IDX, ballRollScore);
            addScore(convertToInt(ballRollScore));
            setCompleted();
        }
    }

    private void processStrike(String ballRollScore) {
        // Special condition for frame 10
        if (TEN.equals(this.number)) {
            if (isFirstBall()) {
                setBallScore(FIRST_BALL_IDX, STRIKE_CHAR);
            } else if (isSecondBall()) {
                setBallScore(SECOND_BALL_IDX, STRIKE_CHAR);
            } else {
                setBallScore(THIRD_BALL_IDX, STRIKE_CHAR);
            }
        } else {
            setBallScore(SECOND_BALL_IDX, STRIKE_CHAR);
        }
        setHasStrike();
        addScore(convertToInt(ballRollScore));
        setCompleted();
    }

    private void processFoul(String ballRollScore) {
        if (isFirstBall()) {
            setBallScore(FIRST_BALL_IDX, FOUL_CHAR);
            addScore(0);
        } else if (isSecondBall()) {
            setBallScore(SECOND_BALL_IDX, FOUL_CHAR);
            addScore(0);
            setCompleted();
        } else {
            setBallScore(THIRD_BALL_IDX, ballRollScore);
            addScore(convertToInt(ballRollScore));
            setCompleted();
        }
    }

    private void setHasSpare() {
        this.hasSpare = true;
    }

    public boolean hasStrike() {
        return this.hasStrike;
    }

    private void setHasStrike() {
        this.hasStrike = true;
    }

    public boolean hasSpare() {
        return this.hasSpare;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void adjustScore(String ballScore) {
        if (isFoul(ballScore)) return;
        this.score = this.score + convertToInt(ballScore);
    }

    private void setBallScore(int idx, String ballScore) {
        this.ballRollScores[idx] = ballScore;
    }

    private boolean isFirstBall() {
        return this.ballRollScores[0] == null;
    }

    private boolean isSecondBall() {
        return this.ballRollScores[1] == null;
    }

    private void setCompleted() {
        this.completed = true;
    }

    public boolean isCompleted() {
        return this.completed;
    }

    private void addScore(int score) {
        this.score = this.score + score;
    }

    private boolean isFoul(final String ballScore) {
        return FOUL_CHAR.equalsIgnoreCase(ballScore);
    }

    private boolean isStrike(final String ballScore) {
        return TEN.equals(convertToInt(ballScore));
    }

    private boolean isSpare(final String ballRollScore) {
        if (isFoul(this.ballRollScores[0])) {
            return (0 + convertToInt(ballRollScore)) == 10;
        }
        return (convertToInt(this.ballRollScores[0]) + convertToInt(ballRollScore)) == 10;
    }

    private int convertToInt(final String value) {
        if (STRIKE_CHAR.equalsIgnoreCase(value)) return TEN;
        return Integer.parseInt(value);
    }

    public int getNumber() {
        return number;
    }

    public String[] getBallRollScores() {
        return ballRollScores;
    }

    @Override
    public String toString() {
        return "Frame " + number + " { " +
                "" + Arrays.toString(ballRollScores) +
                " score:" + score + " }";
    }
}
