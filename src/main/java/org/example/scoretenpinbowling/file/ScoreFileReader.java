package org.example.scoretenpinbowling.file;

import java.io.IOException;
import java.util.List;

public interface ScoreFileReader {
    List<ScoreFileRow> read(String fileName) throws ScoreFileValidationException, IOException;
}
