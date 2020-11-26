package org.example.scoretenpinbowling;

import org.example.scoretenpinbowling.file.*;

import java.io.IOException;
import java.util.List;

public class ScoreTenPinBowlingGame {

    public static void main(String[] args) {
        System.out.println("Score Ten-Pin Bowling Game!");
        String fileName = null;
        if (args != null && args.length == 1) {
            fileName = args[0];
        }
        final ScoreFileReader fileReader = new SimpleScoreFileReader();
        try {
            final List<ScoreFileRow> scoreFileRows = fileReader.read(fileName);
            System.out.println(scoreFileRows);
        } catch (ScoreFileValidationException | IOException e) {
            System.err.println("ERROR: " + e.getMessage());
        }
    }
}
