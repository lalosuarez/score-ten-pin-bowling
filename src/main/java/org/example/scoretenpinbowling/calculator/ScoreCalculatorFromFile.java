package org.example.scoretenpinbowling.calculator;

import org.example.scoretenpinbowling.file.*;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.stream.Collectors;

public class ScoreCalculatorFromFile implements ScoreCalculator {

    private final ScoreFileReader fileReader;
    private final ScoreProcess scoreProcess;

    public ScoreCalculatorFromFile(ScoreFileReader scoreFileReader, ScoreProcess scoreProcess) {
        this.fileReader = scoreFileReader;
        this.scoreProcess = scoreProcess;
    }

    @Override
    public ScoreData calculate() {
        try {
            final ScoreFile scoreFile = fileReader.read();
            List<Score> scores = scoreFile.getScoreFileRowsByPlayer()
                    .entrySet().stream().map(entry -> {
                        final String playerName = entry.getKey();
                        final Frame[] frames = scoreProcess.processScoresByPlayer(playerName, entry.getValue());
                        return new Score(new Player(playerName), frames);
                    }).collect(Collectors.toList());
            return new ScoreData(scores);
        } catch (ScoreFileValidationException | IOException | IllegalStateException e) {
            String error = e.getMessage();
            if (e instanceof NoSuchFileException) {
                error = "No such file: " + e.getMessage();
            }
            return new ScoreData(error);
        }
    }
}
