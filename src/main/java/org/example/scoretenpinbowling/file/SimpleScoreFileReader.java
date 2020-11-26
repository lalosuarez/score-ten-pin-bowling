package org.example.scoretenpinbowling.file;

import org.example.scoretenpinbowling.BowlingGame;
import org.example.scoretenpinbowling.Frame;
import org.example.scoretenpinbowling.Player;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SimpleScoreFileReader implements ScoreFileReader {

    private static final String DEFAULT_FILE_NAME = "file-samples/score.tsv";

    @Override
    public List<ScoreFileRow> read(String fileName) throws ScoreFileValidationException, IOException {
        // Reads a default file if no file is provided
        if (fileName == null) fileName = DEFAULT_FILE_NAME;
        Map<String, BowlingGame> map = new HashMap<>();
        List<ScoreFileRow> fileRows = Files.lines(Paths.get(fileName))
                .map(row -> {
                    if (!row.contains("\t")) {
                        throw new ScoreFileValidationException("File contains rows that are not tab-separated");
                    }
                    String[] split = row.split("\t");
                    // TODO: Add validations
                    validateRow();
                    return new ScoreFileRow(split[0], split[1]);
                })
                /*.forEach(row -> {
                    if (map.containsKey(row.getPlayerName())) {
                        map.get(playerName).add(new ScoreFileRow(split[0], split[1]));
                    } else {
                        final BowlingGame bowlingGame = new BowlingGame(new Player(row.getPlayerName()));
                        bowlingGame.getFrames().add(new Frame())
                        map.put(row.getPlayerName(), bowlingGame);
                    }
                })*/
                .collect(Collectors.toList());
        return fileRows;
    }

    private void validateRow() throws ScoreFileValidationException {
    }
}
