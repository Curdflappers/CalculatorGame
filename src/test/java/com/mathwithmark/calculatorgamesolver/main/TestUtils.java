package com.mathwithmark.calculatorgamesolver.main;

import com.mathwithmark.calculatorgamesolver.calculatorgame.Config;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Helpers;

/**
 * Utility functions used only by tests. Mocking, fixture-creation, etc.
 */
public class TestUtils {
    /** Creates VM args from the method parameters */
    static String[] args(
        int value,
        int goal,
        int moves,
        String[] ruleStrings,
        int[] portals
    ) {
        String rulesString =
            Helpers.combineStrings(ruleStrings, Config.CMDLINE_SEPARATOR);
        boolean portalsPresent = portals != null;
        String portalsResponse = portalsPresent ? "y" : "n";
        String leftPortal = portalsPresent ? String.valueOf(portals[0]) : "";
        String rightPortal = portalsPresent ? String.valueOf(portals[1]) : "";
        return new String[] {
            "" + value,
            "" + goal,
            "" + moves,
            rulesString,
            portalsResponse,
            leftPortal,
            rightPortal
        };
    }
}
