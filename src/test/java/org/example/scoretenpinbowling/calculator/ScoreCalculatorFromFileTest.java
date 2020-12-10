package org.example.scoretenpinbowling.calculator;

import org.example.scoretenpinbowling.file.ScoreFileReader;
import org.example.scoretenpinbowling.file.SimpleScoreFileReader;
import org.example.scoretenpinbowling.representation.BallScoreInputStandard;
import org.example.scoretenpinbowling.representation.BallScoreOutputStandard;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class ScoreCalculatorFromFileTest {

    @Test
    public void testCalculateSuccess() {
        final ScoreFileReader scoreFileReader = new SimpleScoreFileReader("file-samples/score.tsv");
        final ScoreProcess scoreProcess = new ScoreProcessDefault(new BallScoreOutputStandard(),
                new BallScoreInputStandard());
        final ScoreCalculator calculator = new ScoreCalculatorFromFile(scoreFileReader, scoreProcess);
        final ScoreData scoreData = calculator.calculate();
        final List<Score> scores = scoreData.getScores();
        assertNull("Should not have any error", scoreData.getError());
        assertEquals("Should contain data for 2 users", 2, scores.size());

        final List<Score> johnScores = scores.stream()
                .filter(score -> score.getPlayer().getName().equals("John"))
                .collect(Collectors.toList());
        final Frame[] frames = johnScores.get(0).getFrames();

        assertEquals("Should contain 10 frames", 10, frames.length);
        assertEquals(16, frames[0].getScore());
        assertEquals(25, frames[1].getScore());
        assertEquals(44, frames[2].getScore());
        assertEquals(53, frames[3].getScore());
        assertEquals(82, frames[4].getScore());
        assertEquals(101, frames[5].getScore());
        assertEquals(110, frames[6].getScore());
        assertEquals(124, frames[7].getScore());
        assertEquals(132, frames[8].getScore());
        assertEquals(151, frames[9].getScore());
    }

    @Test
    public void testCalculatePerfectGame() {
        final ScoreFileReader scoreFileReader = new SimpleScoreFileReader("file-samples/perfect-game.tsv");
        final ScoreProcess scoreProcess = new ScoreProcessDefault(new BallScoreOutputStandard(),
                new BallScoreInputStandard());
        final ScoreCalculator calculator = new ScoreCalculatorFromFile(scoreFileReader, scoreProcess);
        final ScoreData scoreData = calculator.calculate();
        final Frame[] frames = scoreData.getScores().get(0).getFrames();
        assertNull("Should not have any error", scoreData.getError());
        assertEquals("Should contain 10 frames", 10, frames.length);
        assertEquals(30, frames[0].getScore());
        assertEquals(60, frames[1].getScore());
        assertEquals(90, frames[2].getScore());
        assertEquals(120, frames[3].getScore());
        assertEquals(150, frames[4].getScore());
        assertEquals(180, frames[5].getScore());
        assertEquals(210, frames[6].getScore());
        assertEquals(240, frames[7].getScore());
        assertEquals(270, frames[8].getScore());
        assertEquals(300, frames[9].getScore());
    }

    @Test
    public void testCalculateForMoreThrowsThanAllowed() {
        final ScoreFileReader scoreFileReader = new SimpleScoreFileReader("file-samples/all-frames-reached.tsv");
        final ScoreProcess scoreProcess = new ScoreProcessDefault(new BallScoreOutputStandard(),
                new BallScoreInputStandard());
        final ScoreCalculator calculator = new ScoreCalculatorFromFile(scoreFileReader, scoreProcess);
        final ScoreData scoreData = calculator.calculate();
        assertNotNull("Should have error for extra throws", scoreData.getError());
    }

    @Test
    public void testCalculateWorstGame() {
        final ScoreFileReader scoreFileReader = new SimpleScoreFileReader("file-samples/worst-game.tsv");
        final ScoreProcess scoreProcess = new ScoreProcessDefault(new BallScoreOutputStandard(),
                new BallScoreInputStandard());
        final ScoreCalculator calculator = new ScoreCalculatorFromFile(scoreFileReader, scoreProcess);
        final ScoreData scoreData = calculator.calculate();
        final Frame[] frames = scoreData.getScores().get(0).getFrames();
        assertNull("Should not have any error", scoreData.getError());
        assertEquals("Should contain 10 frames", 10, frames.length);
        assertEquals(0, frames[0].getScore());
        assertEquals(0, frames[1].getScore());
        assertEquals(0, frames[2].getScore());
        assertEquals(0, frames[3].getScore());
        assertEquals(0, frames[4].getScore());
        assertEquals(0, frames[5].getScore());
        assertEquals(0, frames[6].getScore());
        assertEquals(0, frames[7].getScore());
        assertEquals(0, frames[8].getScore());
        assertEquals(0, frames[9].getScore());
    }

    @Test
    public void testCalculateFailure(){
        final ScoreFileReader scoreFileReader = new SimpleScoreFileReader("file-samples/invalid-content.tsv");
        final ScoreProcess scoreProcess = new ScoreProcessDefault(new BallScoreOutputStandard(),
                new BallScoreInputStandard());
        final ScoreCalculator calculator = new ScoreCalculatorFromFile(scoreFileReader, scoreProcess);
        assertNotNull("Should contain error", calculator.calculate().getError());
    }
}