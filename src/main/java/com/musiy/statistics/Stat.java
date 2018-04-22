package com.musiy.statistics;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 *  Application entry point.
 *  To run type: java Stat <i>filename</i>
 */
public class Stat {

    public static void main(String[] args) throws IOException {
        if (args.length != 1) {
            printHelp();
            System.exit(0);
        }
        String filename = args[0];
        try (InputStreamReader reader = new FileReader(filename)) {
            new StatisticsEngine(reader)
                    .start((k, v) -> System.out.println(k + " : " + v));
        }  catch (FileNotFoundException e) {
            System.out.println("File not found: " + filename);
        }
    }

    /**
     * Print usage help
     */
    private static void printHelp() {
        System.out.println(Utils.getResourceAsString(Stat.class, "UsageInfo.txt"));
    }
}
