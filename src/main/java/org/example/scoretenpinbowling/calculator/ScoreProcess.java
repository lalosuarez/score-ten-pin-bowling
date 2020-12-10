package org.example.scoretenpinbowling.calculator;

import org.example.scoretenpinbowling.file.ScoreFileRow;

import java.util.List;

public interface ScoreProcess {
    Frame[] processScoresByPlayer(String playerName, List<ScoreFileRow> scores);
}
