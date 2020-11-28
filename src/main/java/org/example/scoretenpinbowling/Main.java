package org.example.scoretenpinbowling;

import org.example.scoretenpinbowling.file.SimpleScoreFileReader;
import org.example.scoretenpinbowling.out.ConsolePrinterWithTabs;
import org.example.scoretenpinbowling.out.Printer;

public class Main {
    public static void main(String[] args) {
        String fileName = null;
        if (args != null && args.length == 1) {
            fileName = args[0];
        }
        final ScoreCalculator calculator = new ScoreCalculatorFromFile(
                new SimpleScoreFileReader(fileName));
        final ScoreData scoreData = calculator.calculate();
        final Printer printer = new ConsolePrinterWithTabs(scoreData);
        printer.print();
    }
}
