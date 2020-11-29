package org.example.scoretenpinbowling.calculator;

import java.util.List;

public class ScoreData {
    private final List<Score> scores;

    public ScoreData(List<Score> scores) {
        this.scores = scores;
    }

    public List<Score> getScores() {
        return scores;
    }

    @Override
    public String toString() {
        return "ScoreData{ " + scores + " }";
    }
}
