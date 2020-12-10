package org.example.scoretenpinbowling.representation;

public class BallScoreInputAlternate implements BallScoreInput {
    @Override
    public String getStrike() {
        return "10";
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
