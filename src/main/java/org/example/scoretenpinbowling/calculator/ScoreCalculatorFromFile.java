package org.example.scoretenpinbowling.calculator;

import org.example.scoretenpinbowling.file.*;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.stream.Collectors;

public class ScoreCalculatorFromFile implements ScoreCalculator {

    private final ScoreFileReader fileReader;

    public ScoreCalculatorFromFile(ScoreFileReader fileReader) {
        this.fileReader = fileReader;
    }

    @Override
    public ScoreData calculate() {
        try {
            final ScoreFile scoreFile = fileReader.read();
            List<Score> scores = scoreFile.getScoreFileRowsByPlayer()
                    .entrySet().stream().map(entry -> {
                        final String playerName = entry.getKey();
                        Frame[] frames = processScoresByPlayer(playerName, entry.getValue());
                        return new Score(new Player(playerName), frames);
                    }).collect(Collectors.toList());
            return new ScoreData(scores, null);
        } catch (ScoreFileValidationException | IOException | IllegalStateException e) {
            String error = e.getMessage();
            if (e instanceof NoSuchFileException) {
                error = "No such file: " + e.getMessage();
            }
            return new ScoreData(null, error);
        }
    }

    protected Frame[] processScoresByPlayer(String playerName, List<ScoreFileRow> scores) {
        // Stores the scores that are considered a "plus"
        final Queue<String> plusScoresToBeAdded = new LinkedList<>();
        // Stores the indexes for the frames who score needs to be adjusted because of an strike
        final Queue<Integer> adjustScoreForStrikes = new LinkedList<>();

        int frameNumber = 1, prevFrameIdx = -1;
        boolean spareDetected = false;
        final Frame[] frames = new Frame[MAX_NUMBER_OF_FRAMES_PER_GAME];
        Frame currentFrame = new Frame(frameNumber);
        for (int i = 0; i < scores.size(); i++) {

            // Handle scenario for more throws that the allowed within 10 frames
            if (currentFrame.isBallRollsCompleted()) {
                throw new IllegalStateException(
                        String.format("Max number of throws reached within 10 frames for player %s", playerName));
            }

            final String ballRollScore = scores.get(i).getBallScore();
            currentFrame.setBallRollScore(ballRollScore);

            // Rule for spare: you get 10 pins + your next ball
            if (spareDetected) {
                plusScoresToBeAdded.add(ballRollScore);
                spareDetected = processSpare(prevFrameIdx, frames, plusScoresToBeAdded);
            } else if (!adjustScoreForStrikes.isEmpty()) {
                // Rule for strike: you get 10 pins + your next two balls
                plusScoresToBeAdded.add(ballRollScore);
                processStrike(frameNumber, frames, plusScoresToBeAdded, adjustScoreForStrikes);
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
                    adjustFinalScoreForLastFrame(scores, prevFrameIdx, frames, currentFrame,
                            adjustScoreForStrikes, i);
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

    private void adjustFinalScoreForLastFrame(List<ScoreFileRow> scores, int prevFrameIdx, Frame[] frames, Frame currentFrame, Queue<Integer> adjustScoreForStrikes, int i) {
        if (i == (scores.size() - 1)) {
            if ((!adjustScoreForStrikes.isEmpty()) || !currentFrame.isFinished()) {
                frames[prevFrameIdx].adjustToFinalScore(String.valueOf(frames[prevFrameIdx - 1].getScore()));
            }
        }
    }

    private boolean processSpare(int prevFrameIdx, Frame[] frames, Queue<String> plusScoresToBeAdded) {
        if (plusScoresToBeAdded.size() == 1) {
            frames[prevFrameIdx].adjustToFinalScore(plusScoresToBeAdded.poll());
            // At this point the queue is empty
            return false;
        }
        return true;
    }

    private void processStrike(int frameNumber, Frame[] frames, Queue<String> plusScoresToBeAdded, Queue<Integer> adjustScoreForStrikes) {
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

    private boolean arrayIndexIsValid(int prevFrameIdx) {
        return prevFrameIdx != -1;
    }
}
