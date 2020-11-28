package org.example.scoretenpinbowling.file;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import static org.junit.Assert.assertNotNull;

public class SimpleScoreFileReaderTest {

    @Test
    public void testReadFromDefaultPath() throws IOException {
        final ScoreFileReader fileReader = new SimpleScoreFileReader(null);
        final ScoreFile scoreFile = fileReader.read();
        assertNotNull("file should be read from default path", scoreFile);
    }

    @Test
    public void testReadFromPath() throws IOException {
        final ScoreFileReader fileReader = new SimpleScoreFileReader("file-samples/score.tsv");
        final ScoreFile scoreFile = fileReader.read();
        assertNotNull("file should be read from default path", scoreFile);
        assertNotNull("file should contain player Jeff", scoreFile.getScoreFileRowsByPlayer().get("Jeff"));
        assertNotNull("file should contain player John", scoreFile.getScoreFileRowsByPlayer().get("John"));
    }

    @Test(expected = NoSuchFileException.class)
    public void testReadNonExistingFile() throws IOException {
        new SimpleScoreFileReader("does-not-exist.tsv").read();
    }

    @Test(expected = ScoreFileValidationException.class)
    public void testFileWithInvalidFormat() throws IOException {
        new SimpleScoreFileReader("file-samples/invalid-format.txt").read();
    }

    @Test(expected = ScoreFileValidationException.class)
    public void testFileWithInvalidContent() throws IOException {
        new SimpleScoreFileReader("file-samples/invalid-content.tsv").read();
    }

    @Test(expected = ScoreFileValidationException.class)
    public void testFileWithLessRecordsPerPlayerThanTheMinAllowed() throws IOException {
        new SimpleScoreFileReader("file-samples/min-num-of-records.tsv").read();
    }

    @Test(expected = ScoreFileValidationException.class)
    public void testFileWithLessRecordsPerPlayerThanTheMaxAllowed() throws IOException {
        new SimpleScoreFileReader("file-samples/max-num-of-records.tsv").read();
    }
}