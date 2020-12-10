package org.example.scoretenpinbowling.representation;

public class BallScoreInputStandard implements BallScoreInput {
    @Override
    public String getStrike() {
        return "X";
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
