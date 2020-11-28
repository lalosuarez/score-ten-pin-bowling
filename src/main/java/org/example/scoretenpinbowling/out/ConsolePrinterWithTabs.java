package org.example.scoretenpinbowling.out;

import org.example.scoretenpinbowling.Frame;
import org.example.scoretenpinbowling.Score;
import org.example.scoretenpinbowling.ScoreData;

import java.util.Arrays;

public class ConsolePrinterWithTabs implements Printer {
    private final ScoreData scoreData;

    public ConsolePrinterWithTabs(ScoreData scoreData) {
        this.scoreData = scoreData;
    }

    @Override
    public void print() {
        // System.out.println(scoreData);
        // Frames
        printFrames();
        scoreData.getScores().forEach(score -> {
            // Player Names
            printNames(score);
            // Pinfalls
            printPinfalls(score);
            // Scores
            printScores(score);
        });
    }

    private void printNames(Score score) {
        System.out.println(score.getPlayer().getName());
    }

    private void printPinfalls(Score score) {
        System.out.print("Pinfalls");
        Arrays.stream(score.getFrames()).forEach(frame -> {
            for (final String ball : frame.getBallRollScores()) {
                if (ball == null) {
                    System.out.print("\t");
                } else {
                    System.out.print("\t" + ball);
                }
            }
        });
        System.out.print("\n");
    }

    private void printScores(Score score) {
        System.out.print("Score \t");
        Frame[] frames = score.getFrames();
        for (int i = 0; i < frames.length; i++) {
            if (i == (frames.length - 1)) {
                System.out.print("\t" + frames[i].getScore());
            } else {
                System.out.print("\t" + frames[i].getScore() + "\t");
            }
        }
        System.out.print("\n");
    }

    private void printFrames() {
        System.out.print("Frame \t");
        Frame[] frames = scoreData.getScores().get(0).getFrames();
        for (int i = 0; i < frames.length; i++) {
            if (i == (frames.length - 1)) {
                System.out.print("\t" + frames[i].getNumber());
            } else {
                System.out.print("\t" + frames[i].getNumber() + "\t");
            }
        }
        System.out.print("\n");
    }
}
