package org.example.scoretenpinbowling.calculator;

import org.example.scoretenpinbowling.representation.BallScoreInput;
import org.example.scoretenpinbowling.representation.BallScoreOutput;

import java.util.Arrays;

public class Frame {
    private final BallScoreOutput ballScoreOutput;
    private final BallScoreInput ballScoreInput;
    private int number;
    private String[] ballRollsScores;
    private int score;
    private boolean ballRollsCompleted;
    private boolean hasSpare;
    private boolean hasStrike;
    private boolean finished;

    private static final Integer TEN = 10;

    public Frame(BallScoreOutput ballScoreOutput,
                 BallScoreInput ballScoreInput, int frameNumber) {
        this.ballScoreOutput = ballScoreOutput;
        this.ballScoreInput = ballScoreInput;
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

    public void setHasSpare() {
        this.hasSpare = true;
    }

    public boolean hasStrike() {
        return this.hasStrike;
    }

    public void setHasStrike() {
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

    public void setCompleted() {
        this.ballRollsCompleted = true;
    }

    public boolean isBallRollsCompleted() {
        return this.ballRollsCompleted;
    }

    public int getNumber() {
        return number;
    }

    public String[] getBallRollsScores() {
        return ballRollsScores;
    }

    public void setFinished() { this.finished = true; }

    public boolean isFinished() {
        return finished;
    }

    @Override
    public String toString() {
        return "{ Frame " + number + " " + Arrays.toString(ballRollsScores) + " score:" + score + " }";
    }
}
