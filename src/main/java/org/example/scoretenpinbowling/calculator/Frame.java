package org.example.scoretenpinbowling.calculator;

import java.util.Arrays;

public class Frame {
    private int number;
    private String[] ballRollsScores;
    private int score;
    private boolean ballRollsCompleted;
    private boolean hasSpare;
    private boolean hasStrike;
    private boolean finished;

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
        this.ballRollsScores = TEN.equals(frameNumber) ? new String[3] : new String[2];
        this.score = 0;
        this.ballRollsCompleted = false;
        this.hasSpare = false;
        this.hasStrike = false;
        this.finished = false;
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
            if (!isThreeBallFrameWithSpare() && !isThreeBallFrameWithStrike()) {
                setCompleted();
            }
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
                setCompleted();
            }
        } else {
            setBallScore(SECOND_BALL_IDX, STRIKE_CHAR);
        }
        setHasStrike();
        addScore(convertToInt(ballRollScore));
        if (!isThreeBallFrameWithStrike()) {
            setCompleted();
        }
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

    private boolean isThreeBallFrameWithStrike() {
        return this.ballRollsScores.length == 3 && hasStrike;
    }

    private boolean isThreeBallFrameWithSpare() {
        return this.ballRollsScores.length == 3 && hasSpare;
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

    public void setFinalScore(int score) {
        setFinished();
        this.score = score;
    }

    public void adjustToFinalScore(String ballScore) {
        setFinished();
        if (isFoul(ballScore)) return;
        this.score = this.score + convertToInt(ballScore);
    }

    private void setBallScore(int idx, String ballScore) {
        this.ballRollsScores[idx] = ballScore;
    }

    private boolean isFirstBall() {
        return this.ballRollsScores[0] == null;
    }

    private boolean isSecondBall() {
        return this.ballRollsScores[1] == null;
    }

    private void setFinished() { this.finished = true; }

    private void setCompleted() {
        this.ballRollsCompleted = true;
    }

    public boolean isBallRollsCompleted() {
        return this.ballRollsCompleted;
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
        if (isFoul(this.ballRollsScores[0])) {
            return (0 + convertToInt(ballRollScore)) == 10;
        }
        return (convertToInt(this.ballRollsScores[0]) + convertToInt(ballRollScore)) == 10;
    }

    private int convertToInt(final String value) {
        if (STRIKE_CHAR.equalsIgnoreCase(value)) return TEN;
        return Integer.parseInt(value);
    }

    public int getNumber() {
        return number;
    }

    public String[] getBallRollsScores() {
        return ballRollsScores;
    }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public String toString() {
        return "{ Frame " + number + " " + Arrays.toString(ballRollsScores) + " score:" + score + " }";
    }
}
