package org.example.scoretenpinbowling.calculator;

import java.util.List;

public class ScoreData {
    private final List<Score> scores;
    private final String error;

    public ScoreData(List<Score> scores) {
        this.scores = scores;
        this.error = null;
    }

    public ScoreData(String error) {
        this.scores = null;
        this.error = error;
    }

    public List<Score> getScores() {
        return scores;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        return "ScoreData{ " + scores + " }";
    }
}
