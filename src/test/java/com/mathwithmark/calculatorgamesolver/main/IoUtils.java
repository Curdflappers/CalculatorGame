package com.mathwithmark.calculatorgamesolver.main;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 * Contains utility functions for input and output manipulation
 */
public class IoUtils {
    private static ByteArrayOutputStream baos = null;

    static String output() {
        return baos.toString();
    }

    /**
     * Sets output to print to BAOS, sets input to {@code input}
     *
     * @param input the new input String
     * @return the BAOS to which output is printed
     */
    static void prepareEndToEndTest(String input) {
        baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        System.setOut(ps);
    }
}
