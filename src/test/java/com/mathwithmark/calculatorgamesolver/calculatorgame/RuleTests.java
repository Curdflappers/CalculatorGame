package com.mathwithmark.calculatorgamesolver.calculatorgame;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RuleTests {
    //////////////////
    // CONSTRUCTORS //
    //////////////////

    @Test
    void ruleConstructor() {
        int operand1 = 1;
        int operand2 = 2;
        String ruleString = null;

        // Test all operator strings
        for (int i = 0; i < Config.OPERATOR_STRINGS.length - 1; i++) {
            switch (Config.NUM_OPERANDS[i]) {
                case 0:
                    ruleString = Config.ruleString(i);
                    assertStringCreatesRule(ruleString, i);
                    break;
                case 1:
                    ruleString = Config.ruleString(i, operand1);
                    assertStringCreatesRule(ruleString, i, operand1);
                    break;
                case 2:
                    ruleString =
                        operand1 + Config.OPERATOR_STRINGS[i] + operand2;
                    assertStringCreatesRule(ruleString, i, operand1, operand2);
                    break;
            }
        }

        // "*-2" used to parse as "*-", leading to invalid operator
        operand1 = -2; // or any negative number
        // note the lack of space between operator and operand
        ruleString = Config.ruleString(Rule.MULTIPLY, operand1);
        assertStringCreatesRule(ruleString, Rule.MULTIPLY, operand1);
    }

    /**
     * Asserts that the given string creates a rule with the given operator and
     * that both operands are 0
     */
    void assertStringCreatesRule(String str, int operator) {
        assertStringCreatesRule(str, operator, 0, 0);
    }

    /**
     * Asserts that the given string creates a rule with the given operator and
     * that both operands are 0
     */
    void assertStringCreatesRule(String str, int operator, int operand1) {
        assertStringCreatesRule(str, operator, operand1, 0);
    }

    void assertStringCreatesRule(
        String str,
        int operator,
        int operand1,
        int operand2
    ) {
        Rule rule = Rule.of(str);
        assertEquals(Rule.of(operator, operand1, operand2), rule);
        assertEquals(str, rule.toString());
    }

    ///////////
    // APPLY //
    ///////////

    @Test
    void applyAdd() {
        assertApplyRule(3, Rule.ADD, 2, 1);
        assertApplyRule(-3, Rule.ADD, 4, -7);
    }

    @Test
    void applySubtract() {
        assertApplyRule(3, Rule.SUBTRACT, 4, 7);
        assertApplyRule(-3, Rule.SUBTRACT, 5, 2);
    }

    @Test
    void applyMultiply() {
        assertApplyRule(12, Rule.MULTIPLY, 3, 4);
        assertApplyRule(-27, Rule.MULTIPLY, -9, 3);
    }

    @Test
    void applyDivide() {
        assertApplyRule(353, Rule.DIVIDE, 2, 706);
        assertApplyRule(-535, Rule.DIVIDE, -3, 535 * 3);
    }

    @Test
    void applyPad() {
        assertApplyRule(12, Rule.PAD, 2, 1);
        assertApplyRule(1210, Rule.PAD, 10, 12);
        assertApplyRule(3, Rule.PAD, 3, 0);
        assertApplyRule(30, Rule.PAD, 0, 3);
    }

    @Test
    void applySign() {
        assertApplyRule(-1, Rule.SIGN, 1);
        assertApplyRule(1, Rule.SIGN, -1);
        assertApplyRule(-0, Rule.SIGN, 0);
    }

    @Test
    void applyDelete() {
        assertApplyRule(1, Rule.DELETE, 12);
        assertApplyRule(0, Rule.DELETE, 1);
        assertApplyRule(0, Rule.DELETE, 0);
        assertApplyRule(-1, Rule.DELETE, -12);
        assertApplyRule(0, Rule.DELETE, -1);
        assertApplyRule(0, Rule.DELETE, 0);
    }

    @Test
    void applyConvert() {
        assertApplyRule(3, Rule.CONVERT, 5, 3, 5);
        assertApplyRule(12, Rule.CONVERT, 3, 1, 32);
        assertApplyRule(236523, Rule.CONVERT, 56, 23, 566556);
        assertApplyRule(1, Rule.CONVERT, 2, 3, 1);
        assertApplyRule(-3, Rule.CONVERT, 5, 3, -5);
        assertApplyRule(-12, Rule.CONVERT, 3, 1, -32);
        assertApplyRule(-236523, Rule.CONVERT, 56, 23, -566556);
        assertApplyRule(-1, Rule.CONVERT, 2, 3, -1);
        assertApplyRule(3001, Rule.CONVERT, "31", "00", 3311);
    }

    @Test
    void applyPower() {
        assertApplyRule(8, Rule.POWER, 3, 2);
    }

    @Test
    void applyReverse() {
        assertApplyRule(35, Rule.REVERSE, 53);
        assertApplyRule(0, Rule.REVERSE, 0);
        assertApplyRule(-12, Rule.REVERSE, -21);
    }

    @Test
    void applySum() {
        assertApplyRule(1 + 2 + 3, Rule.SUM, 123);
        assertApplyRule(0, Rule.SUM, 0);
    }

    @Test
    void applyShiftRight() {
        assertApplyRule(4123, Rule.SHIFT_RIGHT, 1234);
        assertApplyRule(-4123, Rule.SHIFT_RIGHT, -1234);
        assertApplyRule(-2, Rule.SHIFT_RIGHT, -2);
    }

    @Test
    void applyShiftLeft() {
        assertApplyRule(2341, Rule.SHIFT_LEFT, 1234);
        assertApplyRule(-2341, Rule.SHIFT_LEFT, -1234);
        assertApplyRule(-2, Rule.SHIFT_LEFT, -2);
    }

    @Test
    void applyMirror() {
        assertApplyRule(2332, Rule.MIRROR, 23);
        assertApplyRule(0, Rule.MIRROR, 0);
        assertApplyRule(-11, Rule.MIRROR, -1);
    }

    @Test
    void applyMetaAdd() {
        int addOperand = 1;
        int subtractOperand = 2;
        int metaAddOperand = 3;
        Rule metaAddRule = Rule.of(Rule.META_ADD, metaAddOperand);
        Rule[] rules = new Rule[] {
            Rule.of(Rule.ADD, addOperand),
            Rule.of(Rule.SUBTRACT, subtractOperand),
            metaAddRule,
        };
        Rule[] expectedRules = new Rule[] {
            Rule.of(Rule.ADD, addOperand + metaAddOperand),
            Rule.of(Rule.SUBTRACT, subtractOperand + metaAddOperand),
            metaAddRule,
        };

        assertApplyMetaRule(expectedRules, metaAddRule, rules);
    }

    @Test
    void applyStore() {
        assertApplyStoreRule(1, 1, 11);
    }

    @Test
    void applyInverseTen() {
        assertApplyRule(123456, Rule.INVERSE_TEN, 987654);
        assertApplyRule(10, Rule.INVERSE_TEN, 90);
        assertApplyRule(-10, Rule.INVERSE_TEN, -90);
    }

    // ------- //
    // Helpers //
    // ------- //

    /**
     * Asserts that the result of applying the given non-meta rule to a game
     * with the given value and no portals results in the expected value
     */
    void assertApplyRule(int expected, int operator, int value) {
        Rule rule = Rule.of(operator);
        assertApplyRule(expected, rule, value);
    }

    /**
     * Asserts that the result of applying the given non-meta rule to a game
     * with the given value and no portals results in the expected value
     */
    void assertApplyRule(int expected, int operator, int operand1, int value) {
        Rule rule = Rule.of(operator, operand1);
        assertApplyRule(expected, rule, value);
    }

    /**
     * Asserts that the result of applying the given non-meta rule to a game
     * with the given value and no portals results in the expected value
     */
    void assertApplyRule(
        int expected,
        int operator,
        int operand1,
        int operand2,
        int value
    ) {
        Rule rule = Rule.of(operator, operand1, operand2);
        assertApplyRule(expected, rule, value);
    }

    /**
     * Asserts that the result of applying the given non-meta rule to a game
     * with the given value and no portals results in the expected value
     */
    void assertApplyRule(
        int expected,
        int operator,
        String opString1,
        String opString2,
        int value
    ) {
        Rule rule = Rule.of(operator, opString1, opString2);
        assertApplyRule(expected, rule, value);
    }

    /**
     * Asserts that the result of applying the given non-meta rule to a game
     * with the given value and no portals results in the expected value
     */
    void assertApplyRule(int expected, Rule rule, int value) {
        CalculatorGame originalGame =
            new CalculatorGame(value, 0, 1, new Rule[] {}, null);
        int newValue = rule.apply(originalGame).getValue();
        assertEquals(expected, newValue, rule.toString());
    }

    /**
     * Asserts that a meta rule correctly affects the rules and not the value
     * @param expectedRules
     * @param rule
     * @param oldRules
     */
    void assertApplyMetaRule(Rule[] expectedRules, Rule rule, Rule[] oldRules) {
        int value = -1;
        int goal = -2;
        int moves = 9; // some value > 0

        CalculatorGame oldGame =
            new CalculatorGame(value, goal, moves, oldRules, null);
        CalculatorGame newGame = rule.apply(oldGame);
        CalculatorGame expectedGame =
            new CalculatorGame(value, goal, moves - 1, expectedRules, null);
        assertEquals(expectedGame, newGame);
    }

    void assertApplyStoreRule(int gameValue, int operand1, int newValue) {
        StoreRule rule = new StoreRule(operand1);
        assertApplyRule(newValue, rule, gameValue);
    }
}
