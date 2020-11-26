package org.example.scoretenpinbowling.file;

import org.junit.Test;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleScoreFileReaderTest {

    @Test
    public void testReadFromDefaultPath() throws IOException {
        final ScoreFileReader fileReader = new SimpleScoreFileReader();
        List<ScoreFileRow> fileRows = fileReader.read(null);
        assertNotNull("file should be read from default path", fileRows);
    }

    @Test
    public void testReadFromPath() throws IOException {
        final ScoreFileReader fileReader = new SimpleScoreFileReader();
        List<ScoreFileRow> fileRows = fileReader.read("file-samples/test.tsv");
        assertNotNull("file should be read from default path", fileRows);
        assertEquals("first row corresponds to player abc","abc",fileRows.get(0).getPlayerName());
    }

    @Test(expected = NoSuchFileException.class)
    public void testReadNonExistingFile() throws IOException {
        new SimpleScoreFileReader().read("does-not-exist.tsv");
    }

    @Test(expected = ScoreFileValidationException.class)
    public void testFileWithInvalidFormat() throws IOException {
        new SimpleScoreFileReader().read("file-samples/invalid-format1.txt");
    }
}