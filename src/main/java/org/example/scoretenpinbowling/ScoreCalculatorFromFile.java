package org.example.scoretenpinbowling;

import org.example.scoretenpinbowling.file.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ScoreCalculatorFromFile implements ScoreCalculator {

    final ScoreFileReader fileReader;

    public ScoreCalculatorFromFile(ScoreFileReader fileReader) {
        this.fileReader = fileReader;
    }

    @Override
    public ScoreData calculate() {
        List<Score> scores = new LinkedList<>();
        try {
            final ScoreFile scoreFile = fileReader.read();
            scores = scoreFile.getScoreFileRowsByPlayer()
                    .entrySet().stream().map(entry -> {
                        final String playerName = entry.getKey();
                        Frame[] frames = processScoresByPlayer(playerName, entry.getValue());
                        return new Score(new Player(playerName), frames);
                    }).collect(Collectors.toList());
        } catch (ScoreFileValidationException | IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
        return new ScoreData(scores);
    }

    protected Frame[] processScoresByPlayer(String playerName, List<ScoreFileRow> scores) {
        int frameNumber = 1, prevFrameIdx = -1;
        boolean hasSpare = false;
        final Frame[] frames = new Frame[Score.MAX_NUMBER_OF_FRAMES_PER_GAME];
        Frame currentFrame = new Frame(frameNumber);
        final Queue<String> plusScoresToAdd = new LinkedList<>();
        final Queue<Integer> adjustScoreAtPos = new LinkedList<>();
        for (int i = 0; i < scores.size(); i++) {
            final String ballRollScore = scores.get(i).getBallScore();
            currentFrame.setBallRollScore(ballRollScore);
            // Rule for spare: you get 10 pins + your next ball
            if (hasSpare) {
                plusScoresToAdd.add(ballRollScore);
                if (plusScoresToAdd.size() == 1) {
                    frames[prevFrameIdx].adjustScore(plusScoresToAdd.remove());
                    // At this point the queue is empty
                    hasSpare = false;
                }
            } else if (!adjustScoreAtPos.isEmpty()) {
                // Rule for strike: you get 10 pins + your next two balls
                plusScoresToAdd.add(ballRollScore);
                if (plusScoresToAdd.size() == 2) {
                    // get first element from adjustScoreAtPos queue
                    final int idxToAdjust = adjustScoreAtPos.remove();
                    if (frameNumber != Score.MAX_NUMBER_OF_FRAMES_PER_GAME) {
                        // adjust the score for that element with the values from plusScoresToAdd queue
                        frames[idxToAdjust].adjustScore(plusScoresToAdd.remove());
                        frames[idxToAdjust].adjustScore(plusScoresToAdd.remove());
                    }
                    // also add the previous from the previous score
                    final int idxPrevious = idxToAdjust - 1;
                    if (arrayIndexIsValid(idxPrevious)) {
                        frames[idxToAdjust].adjustScore(String.valueOf(frames[idxPrevious].getScore()));
                    }
                    // At this point plusScoresToAdd queue is empty
                }
            }

            if (currentFrame.isCompleted()) {
                // Sets the new score
                if (arrayIndexIsValid(prevFrameIdx)) {
                    // don't do this step for strikes
                    if (!currentFrame.hasStrike()) {
                        currentFrame.setScore(frames[prevFrameIdx].getScore() + currentFrame.getScore());
                    }
                }

                // Sets whether the frame has a strike or spare in it
                hasSpare = currentFrame.hasSpare();

                // Adds the frame to the array
                final int nextArrayIdx = frameNumber - 1;
                frames[nextArrayIdx] = currentFrame;

                if (currentFrame.hasStrike()) {
                    if (!adjustScoreAtPos.contains(nextArrayIdx)) {
                        adjustScoreAtPos.add(nextArrayIdx);
                    }
                }

                // Sets the previous frame index
                prevFrameIdx = nextArrayIdx;

                if (frameNumber == Score.MAX_NUMBER_OF_FRAMES_PER_GAME)
                    continue;

                // Increases the frame number and creates a new frame
                // This means it is done with the current frame and will start processing a new one
                frameNumber++;
                currentFrame = new Frame(frameNumber);
            }
        }
        return frames;
    }

    private boolean arrayIndexIsValid(int prevFrameIdx) {
        return prevFrameIdx != -1;
    }
}
