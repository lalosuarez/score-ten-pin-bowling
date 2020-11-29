package org.example.scoretenpinbowling.calculator;

public interface ScoreCalculator {
    int MAX_NUMBER_OF_FRAMES_PER_GAME = 10;
    ScoreData calculate();
}
