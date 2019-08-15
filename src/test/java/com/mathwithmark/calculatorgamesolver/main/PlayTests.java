package com.mathwithmark.calculatorgamesolver.main;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import com.mathwithmark.calculatorgamesolver.calculatorgame.Helpers;
import com.mathwithmark.calculatorgamesolver.yaml.Serialize;

import org.junit.jupiter.api.Test;

public class PlayTests {
    @Test
    void playAllLevels() {
        IoUtils.prepareEndToEndTest(playAllLevelsInput());

        Play.main(new String[0]);

        assertTrue(IoUtils.output().contains(Play.GAME_WON_MESSAGE));
    }

    @Test
    void loadsAndQuits() {
        IoUtils.prepareEndToEndTest(quitGameFromLevelInput());

        // if this does not throw an exception or infinite loop, the test passes
        Play.main(new String[0]);
    }

    /**
     * @return the input string to quit a level, then to quit the game
     */
    private static String quitGameFromLevelInput() {
        String s = "";
        s += 1 + "\n"; // first level index
        s += Play.QUIT_LEVEL_INPUT + "\n";
        return s;
    }

    /**
     * @return the input required to play and beat all levels
     */
    private static String playAllLevelsInput() {
        String s = "";
        s += 1 + "\n"; // first level index
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
