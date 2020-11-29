package org.example.scoretenpinbowling.calculator;

import org.example.scoretenpinbowling.file.*;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class ScoreCalculatorFromFile implements ScoreCalculator {

    private final ScoreFileReader fileReader;

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
            System.err.println("ERROR: " + e);
        }
        return new ScoreData(scores);
    }

    protected Frame[] processScoresByPlayer(String playerName, List<ScoreFileRow> scores) {
        int frameNumber = 1, prevFrameIdx = -1;
        boolean spareDetected = false, strikeDetected = false;
        final Frame[] frames = new Frame[MAX_NUMBER_OF_FRAMES_PER_GAME];
        Frame currentFrame = new Frame(frameNumber);
        // Stores the scores that are considered a "plus"
        final Queue<String> plusScoresToBeAdded = new LinkedList<>();
        // Stores the indexes for the frames who score needs to be adjusted because of an strike
        final Queue<Integer> adjustScoreForStrikes = new LinkedList<>();
        for (int i = 0; i < scores.size(); i++) {
            final String ballRollScore = scores.get(i).getBallScore();
            currentFrame.setBallRollScore(ballRollScore);
            // Rule for spare: you get 10 pins + your next ball
            if (spareDetected) {
                plusScoresToBeAdded.add(ballRollScore);
                if (plusScoresToBeAdded.size() == 1) {
                    frames[prevFrameIdx].adjustToFinalScore(plusScoresToBeAdded.poll());
                    // At this point the queue is empty
                    spareDetected = false;
                }
            } else if (!adjustScoreForStrikes.isEmpty()) {
                // Rule for strike: you get 10 pins + your next two balls
                plusScoresToBeAdded.add(ballRollScore);
                if (plusScoresToBeAdded.size() == 2) {
                    // gets first element from adjustScoreForStrikes queue
                    final int idxToAdjust = adjustScoreForStrikes.poll();
                    final Frame lastElem =
                            arrayIndexIsValid(frameNumber - 2) ? frames[frameNumber - 2] : null;
                    final Frame previousFromLast =
                            arrayIndexIsValid(frameNumber - 3) ? frames[frameNumber - 3] : null;
                    // Adjusts the score for that element with the values from plusScoresToBeAdded queue
                    String firstScoreToAdd, secondScoreToAdd;
                    if (previousFromLast != null && previousFromLast.hasStrike()
                            && !previousFromLast.isFinished() && lastElem != null
                            && lastElem.hasStrike()) { // && !currentFrame.hasStrike()
                        // Peek from the Q not remove, otherwise will loose the value to adjust the next strike
                        firstScoreToAdd = plusScoresToBeAdded.poll();
                        secondScoreToAdd = plusScoresToBeAdded.peek();
                    } else {
                        firstScoreToAdd = plusScoresToBeAdded.poll();
                        secondScoreToAdd = plusScoresToBeAdded.poll();
                    }
                    frames[idxToAdjust].adjustToFinalScore(firstScoreToAdd);
                    frames[idxToAdjust].adjustToFinalScore(secondScoreToAdd);

                    // Gets the previous from the previous index and adds the previous from the previous score
                    final int idxPreviousPrevious = idxToAdjust - 1;
                    if (arrayIndexIsValid(idxPreviousPrevious)) {
                        frames[idxToAdjust].adjustToFinalScore(String.valueOf(frames[idxPreviousPrevious].getScore()));
                    }
                }
            }

            if (currentFrame.isBallRollsCompleted()) {
                // Adds the previous score to the current one, just for spare an regular scenarios
                if (arrayIndexIsValid(prevFrameIdx) && !currentFrame.hasStrike()) {
                    currentFrame.setFinalScore(frames[prevFrameIdx].getScore() + currentFrame.getScore());
                }

                // Adds the frame to the array
                final int nextArrayIdx = frameNumber - 1;
                frames[nextArrayIdx] = currentFrame;
                // Sets the previous frame index
                prevFrameIdx = nextArrayIdx;

                spareDetected = currentFrame.hasSpare();
                if (currentFrame.hasStrike()) {
                    if (!adjustScoreForStrikes.contains(nextArrayIdx) && (i < scores.size() - 1)) {
                        adjustScoreForStrikes.add(nextArrayIdx);
                    }
                }

                if (frameNumber == MAX_NUMBER_OF_FRAMES_PER_GAME) {
                    if (i == (scores.size() - 1)) {
                        if ((!adjustScoreForStrikes.isEmpty()) || !currentFrame.isFinished()) {
                            frames[prevFrameIdx].adjustToFinalScore(String.valueOf(frames[prevFrameIdx - 1].getScore()));
                        }
                    }
                    continue;
                }

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
