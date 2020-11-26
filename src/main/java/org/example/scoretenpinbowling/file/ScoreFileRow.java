package org.example.scoretenpinbowling.file;

public class ScoreFileRow {
    private String playerName;
    private String score;

    public ScoreFileRow(String playerName, String score) {
        this.playerName = playerName;
        this.score = score;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getScore() {
        return score;
    }

    @Override
    public String toString() {
        return "ScoreFileRow{" +
                "playerName='" + playerName + '\'' +
                ", score='" + score + '\'' +
                '}';
    }
}
