package com.mathwithmark.calculatorgamesolver.main;

import java.io.PrintStream;

import org.junit.jupiter.api.Test;

public class PlayTests {
    /**
     * @return the input string to quit a level, then to quit the game
     */
    private static String quitGameFromLevelInput() {
        String s = "";
        s += Play.QUIT_LEVEL_INPUT + "\n";
        s += Play.QUIT_GAME_INPUT + "\n";
        return s;
    }

    @Test
    void loadsAndQuits() {
        PrintStream out = System.out;
        IoUtils.prepareEndToEndTest(quitGameFromLevelInput());

        try {
            Play.main(new String[0]);
        } finally {
            System.setOut(out);
        }
    }
}
