package org.example.scoretenpinbowling.file;

import java.util.List;
import java.util.Map;

public class ScoreFile {
    private final Map<String, List<ScoreFileRow>> scoreFileRowsByPlayer;

    public ScoreFile(Map<String, List<ScoreFileRow>> scoreFileRowsByPlayer) {
        this.scoreFileRowsByPlayer = scoreFileRowsByPlayer;
    }

    public Map<String, List<ScoreFileRow>> getScoreFileRowsByPlayer() {
        return scoreFileRowsByPlayer;
    }

    @Override
    public String toString() {
        return "ScoreFile{" +
                "scoreFileRowsByPlayer=" + scoreFileRowsByPlayer +
                '}';
    }
}
