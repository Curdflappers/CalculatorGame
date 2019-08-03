package com.mathwithmark.calculatorgamesolver.calculatorgame;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CalculatorGameTests {

    ///////////////////
    // VALID PORTALS //
    ///////////////////

    @Test
    void oneAndZeroAreValid() {
        int[] validPortals = {
            1, 0
        };

        assertTrue(CalculatorGame.validPortals(validPortals));
    }

    @Test
    void negativesAreInvalid() {
        int[] negatives = {
            -2, -3
        };

        assertFalse(CalculatorGame.validPortals(negatives));
    }

    @Test
    void zerosAreInvalid() {
        int[] zeros = {
            0, 0
        };

        assertFalse(CalculatorGame.validPortals(zeros));
    }

    @Test
    void lengthZeroIsInvalid() {
        int[] zeroLength = new int[0];

        assertFalse(CalculatorGame.validPortals(zeroLength));
    }

    @Test
    void lengthOneIsInvalid() {
        int[] lengthOne = {
            1
        };

        assertFalse(CalculatorGame.validPortals(lengthOne));
    }

    @Test
    void anyNegativeIsInvalid() {
        int[] aNegative = {
            1, -1
        };

        assertFalse(CalculatorGame.validPortals(aNegative));
    }

    ///////////////////
    // APPLY PORTALS //
    ///////////////////

    @Test
    void multipleCarriesAndFalls() {
        int value = 991;
        int[] portals = {
            2, 0
        };
        int expected = 1;

        CalculatorGame game =
            CalculatorGame.generateGame(value, 0, 0, new Rule[] {}, portals);

        assertEquals(expected, (int) game.getValue());
    }

    @Test
    void multipleDigitsFalling() {
        int value = 30110;
        int[] portals = {
            3, 0
        };
        int expected = 113;

        CalculatorGame game =
            CalculatorGame.generateGame(value, 0, 0, new Rule[] {}, portals);

        assertEquals(expected, game.getValue(), 0.001);
    }
}

class RuleSanitationTests {
    private static CalculatorGame generateLevelWith(Rule[] rules) {
        return CalculatorGame.generateGame(1, 1, 1, rules, null);
    }

    @Test
    void rulesExactlyCopied() {
        Rule[] expectedRules = {
            Rule.makeRule(Config.ADD),
        };

        CalculatorGame level = generateLevelWith(expectedRules);

        assertArrayEquals(expectedRules, level.getRules());
    }

    @Test
    void noDuplicateSimpleRules() {
        Rule rule = Rule.makeRule(Config.ADD);
        Rule[] inputRules = {
            rule, rule,
        };
        Rule[] expectedRules = {
            rule,
        };

        CalculatorGame level = generateLevelWith(inputRules);

        assertArrayEquals(expectedRules, level.getRules());
    }

    @Test
    void noDuplicateStoreOrUpdateStoreRules() {
        Rule storeRule = Rule.makeRule(Config.STORE);
        Rule updateStoreRule = Rule.makeRule(Config.UPDATE_STORE);
        Rule[] inputRules = {
            storeRule, storeRule, updateStoreRule, updateStoreRule,
        };
        Rule[] expectedRules = {
            storeRule, updateStoreRule,
        };

        CalculatorGame level = generateLevelWith(inputRules);

        assertArrayEquals(expectedRules, level.getRules());
    }

    @Test
    void updateStoreWithoutStoreGeneratesNull() {
        Rule updateStoreRule = Rule.makeRule(Config.UPDATE_STORE);
        Rule[] inputRules = {
            updateStoreRule,
        };

        CalculatorGame level = generateLevelWith(inputRules);

        assertNull(level);
    }
}
