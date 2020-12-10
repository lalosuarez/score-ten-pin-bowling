package org.example.scoretenpinbowling.representation;

/**
 * This will show '0' instead of '-' and everything in caps
 */
public class BallScoreOutputStandard implements BallScoreOutput {
    @Override
    public String getStrike() {
        return "X";
    }

    @Override
    public String getSpare() {
        return "/";
    }

    @Override
    public String getFoul() {
        return "F";
    }

    @Override
    public String getZero() {
        return "0";
    }
}
