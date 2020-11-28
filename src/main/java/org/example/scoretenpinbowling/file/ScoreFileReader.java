package org.example.scoretenpinbowling.file;

import java.io.IOException;
import java.util.List;

public interface ScoreFileReader {
    ScoreFile read() throws ScoreFileValidationException, IOException;
}
