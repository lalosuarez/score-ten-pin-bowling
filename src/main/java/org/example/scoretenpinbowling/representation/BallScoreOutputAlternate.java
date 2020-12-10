package org.example.scoretenpinbowling.representation;

/**
 * This will show '-' instead of '0' and everything in lower case
 */
public class BallScoreOutputAlternate implements BallScoreOutput {
    @Override
    public String getStrike() {
        return "x";
    }

    @Override
    public String getSpare() {
        return "/";
    }

    @Override
    public String getFoul() {
        return "f";
    }

    @Override
    public String getZero() {
        return "-";
    }
}
