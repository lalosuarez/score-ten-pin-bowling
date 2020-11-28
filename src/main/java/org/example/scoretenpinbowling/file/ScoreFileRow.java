package org.example.scoretenpinbowling.file;

public class ScoreFileRow {
    private String playerName;
    private String ballScore;

    public ScoreFileRow(String playerName, String ballScore) {
        this.playerName = playerName;
        this.ballScore = ballScore;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getBallScore() {
        return ballScore;
    }

    @Override
    public String toString() {
        return "ScoreFileRow{" +
                "playerName='" + playerName + '\'' +
                ", ballScore='" + ballScore + '\'' +
                '}';
    }
}
