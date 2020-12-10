package org.example.scoretenpinbowling;

import org.example.scoretenpinbowling.calculator.*;
import org.example.scoretenpinbowling.file.ScoreFileReader;
import org.example.scoretenpinbowling.file.SimpleScoreFileReader;
import org.example.scoretenpinbowling.out.ConsolePrinterWithTabs;
import org.example.scoretenpinbowling.out.Printer;
import org.example.scoretenpinbowling.representation.BallScoreInputStandard;
import org.example.scoretenpinbowling.representation.BallScoreOutputStandard;

public class Main {
    public static void main(String[] args) {
        String fileName = null;
        if (args != null && args.length == 1) {
            fileName = args[0].trim().isEmpty() ? null : args[0];
        }

        // Instantiates the interfaces implementations
        final ScoreFileReader scoreFileReader = new SimpleScoreFileReader(fileName);
        final ScoreProcess scoreProcess = new ScoreProcessDefault(new BallScoreOutputStandard(),
                new BallScoreInputStandard());
        final ScoreCalculator calculator = new ScoreCalculatorFromFile(scoreFileReader, scoreProcess);

        // Starts the process to calculate scores
        final ScoreData scoreData = calculator.calculate();

        // Prints the results
        final Printer printer = new ConsolePrinterWithTabs(scoreData);
        printer.print();
    }
}
