package org.example.scoretenpinbowling.representation;

/**
 * Each implementation will define what values need to be returned in order to represent the score
 * for example: strikes will be represented as "X", fouls as "F", zeros as "-" etc.
 *
 */
public interface BallScoreOutput {
    String getStrike();
    String getSpare();
    String getFoul();
    String getZero();
}
