package com.mathwithmark.calculatorgamesolver.main;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import com.mathwithmark.calculatorgamesolver.calculatorgame.Helpers;
import com.mathwithmark.calculatorgamesolver.yaml.Serialize;

import org.junit.jupiter.api.Test;

public class PlayTests {
    @Test
    void playAllLevels() {
        PrintStream out = System.out;
        ByteArrayOutputStream baos =
            IoUtils.prepareEndToEndTest(playAllLevelsInput());
        try {
            Play.main(new String[0]);
        } finally {
            System.setOut(out);
        }

        assertTrue(baos.toString().contains(Play.GAME_WON_MESSAGE));
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

    /**
     * @return the input string to quit a level, then to quit the game
     */
    private static String quitGameFromLevelInput() {
        String s = "";
        s += Play.QUIT_LEVEL_INPUT + "\n";
        s += Play.NO_INPUT + "\n";
        return s;
    }

    /**
     * @return the input required to play and beat all levels
     */
    private static String playAllLevelsInput() {
        String s = "";
        for (String testCasePath : Helpers.testCasePaths()) {
            List<String> solution =
                Serialize.loadTestCase(testCasePath).SOLUTION;
            for (String step : solution) {
                s += step + "\n";
            }
            s += Play.YES_INPUT + "\n"; // yes to next level
        }
        return s;
    }
}
