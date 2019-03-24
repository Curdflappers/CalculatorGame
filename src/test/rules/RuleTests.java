package rules;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import base.Config;
import base.Game;
import base.Helpers;

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
        for (int i = 0; i < Config.OPERATOR_STRINGS.length; i++) {
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
                    ruleString = Config.ruleString(i, operand1, operand2);
                    assertStringCreatesRule(ruleString, i, operand1, operand2);
                    break;
            }
        }

        // "*-2" used to parse as "*-", leading to invalid operator
        operand1 = -2; // or any negative number
        // note the lack of space between operator and operand
        ruleString = Config.ruleString(Config.MULTIPLY, operand1);
        assertStringCreatesRule(ruleString, Config.MULTIPLY, operand1);
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
        int operand2) {
        Rule rule = Rule.ruleFromString(str);
        assertEquals(Rule.makeRule(operator, operand1, operand2), rule);
        assertEquals(str, rule.toString());
    }

    ///////////
    // APPLY //
    ///////////

    @Test
    void applyAdd() {
        assertApplyRule(3, Config.ADD, 2, 1);
        assertApplyRule(-3, Config.ADD, 4, -7);
    }

    @Test
    void applySubtract() {
        assertApplyRule(3, Config.SUBTRACT, 4, 7);
        assertApplyRule(-3, Config.SUBTRACT, 5, 2);
    }

    @Test
    void applyMultiply() {
        assertApplyRule(12, Config.MULTIPLY, 3, 4);
        assertApplyRule(-27, Config.MULTIPLY, -9, 3);
    }

    @Test
    void applyDivide() {
        assertApplyRule(353, Config.DIVIDE, 2, 706);
        assertApplyRule(-535, Config.DIVIDE, -3, 535 * 3);
    }

    @Test
    void applyPad() {
        assertApplyRule(12, Config.PAD, 2, 1);
        assertApplyRule(1210, Config.PAD, 10, 12);
        assertApplyRule(3, Config.PAD, 3, 0);
        assertApplyRule(30, Config.PAD, 0, 3);
    }

    @Test
    void applySign() {
        assertApplyRule(-1, Config.SIGN, 1);
        assertApplyRule(1, Config.SIGN, -1);
        assertApplyRule(-0, Config.SIGN, 0);
    }

    @Test
    void applyDelete() {
        assertApplyRule(1, Config.DELETE, 12);
        assertApplyRule(0, Config.DELETE, 1);
        assertApplyRule(0, Config.DELETE, 0);
        assertApplyRule(-1, Config.DELETE, -12);
        assertApplyRule(0, Config.DELETE, -1);
        assertApplyRule(0, Config.DELETE, 0);
    }

    @Test
    void applyConvert() {
        assertApplyRule(3, Config.CONVERT, 5, 3, 5);
        assertApplyRule(12, Config.CONVERT, 3, 1, 32);
        assertApplyRule(236523, Config.CONVERT, 56, 23, 566556);
        assertApplyRule(1, Config.CONVERT, 2, 3, 1);
        assertApplyRule(-3, Config.CONVERT, 5, 3, -5);
        assertApplyRule(-12, Config.CONVERT, 3, 1, -32);
        assertApplyRule(-236523, Config.CONVERT, 56, 23, -566556);
        assertApplyRule(-1, Config.CONVERT, 2, 3, -1);
        assertApplyRule(3001, Config.CONVERT, "31", "00", 3311);
    }

    @Test
    void applyPower() {
        assertApplyRule(8, Config.POWER, 3, 2);
    }

    @Test
    void applyReverse() {
        assertApplyRule(35, Config.REVERSE, 53);
        assertApplyRule(0, Config.REVERSE, 0);
        assertApplyRule(-12, Config.REVERSE, -21);
    }

    @Test
    void applySum() {
        assertApplyRule(1 + 2 + 3, Config.SUM, 123);
        assertApplyRule(0, Config.SUM, 0);
    }

    @Test
    void applyShiftRight() {
        assertApplyRule(4123, Config.SHIFT_RIGHT, 1234);
        assertApplyRule(-4123, Config.SHIFT_RIGHT, -1234);
        assertApplyRule(-2, Config.SHIFT_RIGHT, -2);
    }

    @Test
    void applyShiftLeft() {
        assertApplyRule(2341, Config.SHIFT_LEFT, 1234);
        assertApplyRule(-2341, Config.SHIFT_LEFT, -1234);
        assertApplyRule(-2, Config.SHIFT_LEFT, -2);
    }

    @Test
    void applyMirror() {
        assertApplyRule(2332, Config.MIRROR, 23);
        assertApplyRule(0, Config.MIRROR, 0);
        assertApplyRule(-11, Config.MIRROR, -1);
    }

    @Test
    void applyMetaAdd() {
        int addOperand = 1;
        int subtractOperand = 2;
        int metaAddOperand = 3;
        Rule metaAddRule = Rule.makeRule(Config.META_ADD, metaAddOperand);
        Rule[] rules = new Rule[] {
            Rule.makeRule(Config.ADD, addOperand),
            Rule.makeRule(Config.SUBTRACT, subtractOperand),
            metaAddRule,
        };
        Rule[] expectedRules = new Rule[] {
            Rule.makeRule(Config.ADD, addOperand + metaAddOperand),
            Rule.makeRule(Config.SUBTRACT, subtractOperand + metaAddOperand),
            metaAddRule,
        };

        assertApplyMetaRule(expectedRules, metaAddRule, rules);
    }

    @Test
    void applyStore() {
        assertApplyStoreRule(1, 1, 11);
        assertApplyStoreRule(1, -1, 1);
    }

    @Test
    void applyInverseTen() {
        assertApplyRule(1234567, Config.INVERSE_TEN, 9876543);
        assertApplyRule(10, Config.INVERSE_TEN, 90);
        assertApplyRule(-10, Config.INVERSE_TEN, -90);
    }

    // ------- //
    // Helpers //
    // ------- //

    /**
     * Asserts that the result of applying the given non-meta rule to a game
     * with the given value and no portals results in the expected value
     */
    void assertApplyRule(int expected, int operator, int value) {
        Rule rule = Rule.makeRule(operator);
        assertApplyRule(expected, rule, value);
    }

    /**
     * Asserts that the result of applying the given non-meta rule to a game
     * with the given value and no portals results in the expected value
     */
    void assertApplyRule(int expected, int operator, int operand1, int value) {
        Rule rule = Rule.makeRule(operator, operand1);
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
        int value) {
        Rule rule = Rule.makeRule(operator, operand1, operand2);
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
        int value) {
        Rule rule = Rule.makeRule(operator, opString1, opString2);
        assertApplyRule(expected, rule, value);
    }

    /**
     * Asserts that the result of applying the given non-meta rule to a game
     * with the given value and no portals results in the expected value
     */
    void assertApplyRule(int expected, Rule rule, int value) {
        Game originalGame = new Game(value, 0, 0, new Rule[] {}, null);
        double newValue = rule.apply(originalGame).getValue();
        assertEquals(expected, newValue, 0.01, rule.toString());
    }

    /**
     * Asserts that a meta rule correctly affects the rules and not the value
     * @param expectedRules
     * @param rule
     * @param oldRules
     */
    void assertApplyMetaRule(Rule[] expectedRules, Rule rule, Rule[] oldRules) {
        double value = -1;
        int goal = -2;
        int moves = 9; // some value > 0

        Game oldGame = new Game(value, goal, moves, oldRules, null);
        Game newGame = rule.apply(oldGame);
        Game expectedGame =
            new Game(value, goal, moves - 1, expectedRules, null);
        assertEquals(expectedGame, newGame);
    }

    void assertApplyStoreRule(int gameValue, int operand1, int newValue) {
        StoreRule rule = new StoreRule(operand1);
        assertApplyRule(newValue, rule, gameValue);
    }

    ///////////
    // OTHER //
    ///////////

    @Test
    void shiftRecognizesZero() {
        int[] zeroDigits = Helpers.digits(0);

        assertEquals(1, zeroDigits.length);
        assertEquals(0, zeroDigits[0]);
    }

}
