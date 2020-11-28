package org.example.scoretenpinbowling;

import org.example.scoretenpinbowling.file.ScoreFileRow;
import org.example.scoretenpinbowling.file.SimpleScoreFileReader;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class ScoreCalculatorFromFileTest {

    @Test
    public void testProcessScoresByPlayerScenario1() {
        final ScoreCalculatorFromFile calculator =
                new ScoreCalculatorFromFile(new SimpleScoreFileReader(null));
        final String playerName = "Player1";
        final List<ScoreFileRow> rows = new ArrayList<>();

        // (frame 1)
        rows.add(new ScoreFileRow(playerName, "5"));
        rows.add(new ScoreFileRow(playerName, "2"));

        // (frame 2)
        rows.add(new ScoreFileRow(playerName, "3"));
        rows.add(new ScoreFileRow(playerName, "5"));

        // (frame 3)
        rows.add(new ScoreFileRow(playerName, "5"));
        rows.add(new ScoreFileRow(playerName, "0"));

        // (frame 4)
        rows.add(new ScoreFileRow(playerName, "4"));
        rows.add(new ScoreFileRow(playerName, "1"));

        // Spare (frame 5)
        rows.add(new ScoreFileRow(playerName, "9"));
        rows.add(new ScoreFileRow(playerName, "1"));

        // (frame 6)
        rows.add(new ScoreFileRow(playerName, "5"));
        rows.add(new ScoreFileRow(playerName, "1"));

        // (frame 7)
        rows.add(new ScoreFileRow(playerName, "7"));
        rows.add(new ScoreFileRow(playerName, "1"));

        // Strike (frame 8)
        rows.add(new ScoreFileRow(playerName, "10"));

        // (frame 9)
        rows.add(new ScoreFileRow(playerName, "5"));
        rows.add(new ScoreFileRow(playerName, "2"));

        // Strike (frame 10)
        rows.add(new ScoreFileRow(playerName, "10"));
        rows.add(new ScoreFileRow(playerName, "10"));
        rows.add(new ScoreFileRow(playerName, "2"));

        final Frame[] frames = calculator.processScoresByPlayer(playerName, rows);
        // System.out.println(Arrays.toString(frames));
        assertEquals(7, frames[0].getScore());
        assertEquals(15, frames[1].getScore());
        assertEquals(20, frames[2].getScore());
        assertEquals(25, frames[3].getScore());
        assertEquals(40, frames[4].getScore());
        assertEquals(46, frames[5].getScore());
        assertEquals(54, frames[6].getScore());
        assertEquals(71, frames[7].getScore());
        assertEquals(78, frames[8].getScore());
        //
        assertEquals(100, frames[9].getScore());
    }

    @Test
    public void testProcessScoresByPlayerScenario2() {
        final ScoreCalculatorFromFile calculator =
                new ScoreCalculatorFromFile(new SimpleScoreFileReader(null));
        final String playerName = "Player1";
        final List<ScoreFileRow> rows = new ArrayList<>();
        // Spare (frame 1)
        rows.add(new ScoreFileRow(playerName, "8"));
        rows.add(new ScoreFileRow(playerName, "2"));

        // Spare (frame 2)
        rows.add(new ScoreFileRow(playerName, "7"));
        rows.add(new ScoreFileRow(playerName, "3"));

        // (frame 3)
        rows.add(new ScoreFileRow(playerName, "3"));
        rows.add(new ScoreFileRow(playerName, "4"));

        // Strike (frame 4)
        rows.add(new ScoreFileRow(playerName, "10"));

        // Spare (frame 5)
        rows.add(new ScoreFileRow(playerName, "2"));
        rows.add(new ScoreFileRow(playerName, "8"));

        // Strike (frame 6)
        rows.add(new ScoreFileRow(playerName, "10"));

        // Strike (frame 7)
        rows.add(new ScoreFileRow(playerName, "10"));

        // (frame 8)
        rows.add(new ScoreFileRow(playerName, "8"));
        rows.add(new ScoreFileRow(playerName, "f"));

        // Strike (frame 9)
        rows.add(new ScoreFileRow(playerName, "10"));

        // Spare (frame 10)
        rows.add(new ScoreFileRow(playerName, "8"));
        rows.add(new ScoreFileRow(playerName, "2"));
        rows.add(new ScoreFileRow(playerName, "9"));

        final Frame[] frames = calculator.processScoresByPlayer(playerName, rows);
        // System.out.println(Arrays.toString(frames));
        assertEquals(17, frames[0].getScore());
        assertEquals(30, frames[1].getScore());
        assertEquals(37, frames[2].getScore());
        assertEquals(57, frames[3].getScore());
        assertEquals(77, frames[4].getScore());
        assertEquals(105, frames[5].getScore());
        //
        assertEquals(123, frames[6].getScore());
        assertEquals(131, frames[7].getScore());
        assertEquals(151, frames[8].getScore());
        assertEquals(170, frames[9].getScore());
    }

    @Test
    public void testProcessScoresByPlayerWorstGameScenario() {
        // Worst game scenario (all 0)
        final ScoreCalculatorFromFile calculator =
                new ScoreCalculatorFromFile(new SimpleScoreFileReader(null));
        final String playerName = "Player1";
        final List<ScoreFileRow> rows = new ArrayList<>();
        rows.add(new ScoreFileRow(playerName, "f"));
        rows.add(new ScoreFileRow(playerName, "F"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));
        rows.add(new ScoreFileRow(playerName, "0"));

        final Frame[] frames = calculator.processScoresByPlayer(playerName, rows);
        // System.out.println(Arrays.toString(frames));
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
    public void testProcessScoresByPlayerPerfectGameScenario() {
        // Perfect game scenario (all strikes)
        final ScoreCalculatorFromFile calculator =
                new ScoreCalculatorFromFile(new SimpleScoreFileReader(null));
        final String playerName = "Player1";
        final List<ScoreFileRow> rows = new ArrayList<>();
        rows.add(new ScoreFileRow(playerName, "10"));
        rows.add(new ScoreFileRow(playerName, "10"));
        rows.add(new ScoreFileRow(playerName, "10"));
        rows.add(new ScoreFileRow(playerName, "10"));
        rows.add(new ScoreFileRow(playerName, "10"));
        rows.add(new ScoreFileRow(playerName, "10"));
        rows.add(new ScoreFileRow(playerName, "10"));
        rows.add(new ScoreFileRow(playerName, "10"));
        rows.add(new ScoreFileRow(playerName, "10"));
        rows.add(new ScoreFileRow(playerName, "10"));
        rows.add(new ScoreFileRow(playerName, "10"));
        rows.add(new ScoreFileRow(playerName, "10"));

        final Frame[] frames = calculator.processScoresByPlayer(playerName, rows);
        // System.out.println(Arrays.toString(frames));
        assertEquals(30, frames[0].getScore());
        assertEquals(60, frames[1].getScore());
        assertEquals(90, frames[2].getScore());
        assertEquals(120, frames[3].getScore());
        assertEquals(150, frames[4].getScore());
        //
        assertEquals(180, frames[5].getScore());
        assertEquals(210, frames[6].getScore());
        assertEquals(240, frames[7].getScore());
        assertEquals(270, frames[8].getScore());
        assertEquals(300, frames[9].getScore());
    }

    @Test
    public void testProcessScoresByPlayerNoSpareAndStrikeScenario() {
        // Perfect game scenario (all strikes)
        final ScoreCalculatorFromFile calculator =
                new ScoreCalculatorFromFile(new SimpleScoreFileReader(null));
        final String playerName = "Player1";
        final List<ScoreFileRow> rows = new ArrayList<>();
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));
        rows.add(new ScoreFileRow(playerName, "1"));

        final Frame[] frames = calculator.processScoresByPlayer(playerName, rows);
        // System.out.println(Arrays.toString(frames));
        assertEquals(2, frames[0].getScore());
        assertEquals(4, frames[1].getScore());
        assertEquals(6, frames[2].getScore());
        assertEquals(8, frames[3].getScore());
        assertEquals(10, frames[4].getScore());
        assertEquals(12, frames[5].getScore());
        assertEquals(14, frames[6].getScore());
        assertEquals(16, frames[7].getScore());
        assertEquals(18, frames[8].getScore());
        assertEquals(20, frames[9].getScore());
    }
}