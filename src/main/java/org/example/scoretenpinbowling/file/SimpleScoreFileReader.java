package org.example.scoretenpinbowling.file;

import org.example.scoretenpinbowling.Score;

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
    private static final String TAB = "\t";
    private static final int MIN_ROWS_BY_PLAYER = 12;
    private static final int MAX_ROWS_BY_PLAYER = 20;

    private final String fileName;

    public SimpleScoreFileReader(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public ScoreFile read() throws ScoreFileValidationException, IOException {
        // Reads a default file if no file is provided
        String fileName = this.fileName;
        if (fileName == null) fileName = DEFAULT_FILE_NAME;
        Map<String, Score> map = new HashMap<>();
        List<ScoreFileRow> fileRows = Files.lines(Paths.get(fileName))
                .map(row -> {
                    // Validates the row format
                    validateRowFormat(row);
                    final String[] split = row.split(TAB);
                    final ScoreFileRow scoreFileRow = new ScoreFileRow(split[0], split[1]);
                    // Validates the row content
                    validateRowContent(scoreFileRow);
                    return scoreFileRow;
                })
                .collect(Collectors.toList());
        return new ScoreFile(getBallScoreByPlayer(fileRows));
    }

    private void validateRowContent(final ScoreFileRow scoreFileRow) {
        // Validates the player name
        if (scoreFileRow.getPlayerName() == null || scoreFileRow.getPlayerName().trim().isEmpty()) {
            throw new ScoreFileValidationException("File contains rows with invalid player names");
        }

        // Validates the score
        if (scoreFileRow.getBallScore() == null || scoreFileRow.getBallScore().trim().isEmpty()) {
            throw new ScoreFileValidationException("File contains rows with invalid scores");
        }
    }

    private void validateRowFormat(final String row) throws ScoreFileValidationException {
        if (!row.contains(TAB)) {
            throw new ScoreFileValidationException("File contains rows that are not tab-separated");
        }
    }

    private void validateNumberOfRowsByPlayer(List<ScoreFileRow> scoreFileRows) {
        // Min number of records
        if (scoreFileRows == null || scoreFileRows.size() < MIN_ROWS_BY_PLAYER) {
            throw new ScoreFileValidationException("File contains players that does not have" +
                    " the minimum of records required");
        }

        // Max number of records
        if (scoreFileRows == null || scoreFileRows.size() > MAX_ROWS_BY_PLAYER) {
            throw new ScoreFileValidationException("File contains players that has more records" +
                    " than the maximum required");
        }
    }

    private Map<String, List<ScoreFileRow>> getBallScoreByPlayer(List<ScoreFileRow> fileRows) {
        final Map<String, List<ScoreFileRow>> ballScoreByPlayer = new HashMap<>();
        fileRows.stream().forEach(row -> {
            final List<ScoreFileRow> scoreFileRows;
            if (!ballScoreByPlayer.containsKey(row.getPlayerName())) {
                scoreFileRows = new ArrayList<>();
            } else {
                scoreFileRows = ballScoreByPlayer.get(row.getPlayerName());
            }
            scoreFileRows.add(row);
            ballScoreByPlayer.put(row.getPlayerName(), scoreFileRows);
        });
        ballScoreByPlayer.forEach((player, scoreFileRows) -> validateNumberOfRowsByPlayer(scoreFileRows));
        return ballScoreByPlayer;
    }
}
