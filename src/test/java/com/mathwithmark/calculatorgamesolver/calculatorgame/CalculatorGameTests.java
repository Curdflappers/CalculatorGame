package com.mathwithmark.calculatorgamesolver.calculatorgame;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class CalculatorGameTests {
}

class ValidatePortalsTests {
    @Test
    void oneAndZeroAreValid() {
        int[] sut = {
            1, 0
        };

        validatePortals(sut);
    }

    @Test
    void negativesAreInvalid() {
        int[] sut = {
            -2, -3
        };

        assertThrows(
            IllegalArgumentException.class,
            () -> validatePortals(sut)
        );
    }

    @Test
    void zerosAreInvalid() {
        int[] sut = {
            0, 0
        };

        assertThrows(
            IllegalArgumentException.class,
            () -> validatePortals(sut)
        );
    }

    @Test
    void lengthZeroIsInvalid() {
        int[] sut = new int[0];

        assertThrows(
            IllegalArgumentException.class,
            () -> validatePortals(sut)
        );
    }

    @Test
    void lengthOneIsInvalid() {
        int[] sut = {
            1
        };

        assertThrows(
            IllegalArgumentException.class,
            () -> validatePortals(sut)
        );
    }

    @Test
    void anyNegativeIsInvalid() {
        int[] sut = {
            1, -1
        };

        assertThrows(
            IllegalArgumentException.class,
            () -> validatePortals(sut)
        );
    }

    private static void validatePortals(int[] portals)
        throws IllegalArgumentException {

        int value = 1;
        int goal = 1;
        int moves = 1;
        Rule[] rules = {};
        new CalculatorGame(value, goal, moves, rules, portals);
    }
}

class ApplyPortalsTests {
    @Test
    void multipleCarriesAndFalls() {
        int value = 991;
        int[] portals = {
            2, 0
        };
        int expected = 1;

        CalculatorGame sut =
            new CalculatorGame(value, 0, 0, new Rule[] {}, portals);

        assertEquals(expected, (int) sut.getValue());
    }

    @Test
    void multipleDigitsFalling() {
        int value = 30110;
        int[] portals = {
            3, 0
        };
        int expected = 113;

        CalculatorGame sut =
            new CalculatorGame(value, 0, 0, new Rule[] {}, portals);

        assertEquals(expected, sut.getValue(), 0.001);
    }
}

class RuleSanitationTests {
    private static CalculatorGame constructLevelWith(Rule[] rules) {
        return new CalculatorGame(1, 1, 1, rules, null);
    }

    @Test
    void rulesExactlyCopied() {
        Rule[] expectedRules = {
            Rule.of(Rule.ADD),
        };

        CalculatorGame level = constructLevelWith(expectedRules);

        assertArrayEquals(expectedRules, level.getRules());
    }

    @Test
    void noDuplicateSimpleRules() {
        Rule rule = Rule.of(Rule.ADD);
        Rule[] inputRules = {
            rule, rule,
        };
        Rule[] expectedRules = {
            rule,
        };

        CalculatorGame level = constructLevelWith(inputRules);

        assertArrayEquals(expectedRules, level.getRules());
    }

    @Test
    void noDuplicateStoreOrUpdateStoreRules() {
        Rule storeRule = Rule.of(Rule.STORE);
        Rule updateStoreRule = Rule.of(Rule.UPDATE_STORE);
        Rule[] inputRules = {
            storeRule, storeRule, updateStoreRule, updateStoreRule,
        };
        Rule[] expectedRules = {
            storeRule, updateStoreRule,
        };

        CalculatorGame level = constructLevelWith(inputRules);

        assertArrayEquals(expectedRules, level.getRules());
    }

    @Test
    void updateStoreWithoutStoreThrowsIllegalArgumentException() {
        Rule updateStoreRule = Rule.of(Rule.UPDATE_STORE);
        Rule[] inputRules = {
            updateStoreRule,
        };

        assertThrows(IllegalArgumentException.class, () -> {
            constructLevelWith(inputRules);
        });
    }
}
