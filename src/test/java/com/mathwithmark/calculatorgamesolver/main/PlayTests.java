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
    private String quitGameFromLevelInput() {
        StringBuilder sb = new StringBuilder();
        sb.append(1).append("\n"); // first level index
        sb.append(Play.QUIT_LEVEL_INPUT).append("\n");
        return sb.toString();
    }

    /**
     * @return the input required to play and beat all levels
     */
    private String playAllLevelsInput() {
        StringBuilder sb = new StringBuilder();
        sb.append(1).append("\n"); // first level index
        for (String testCasePath : Helpers.testCasePaths()) {
            List<String> solution =
                Serialize.loadTestCase(testCasePath).SOLUTION;
            for (String step : solution) {
                sb.append(step).append("\n");
            }
            sb.append(Play.YES_INPUT).append("\n"); // yes to next level
        }
        return sb.toString();
    }
}
