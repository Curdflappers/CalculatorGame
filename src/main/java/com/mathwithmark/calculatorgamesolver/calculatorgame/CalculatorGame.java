package com.mathwithmark.calculatorgamesolver.calculatorgame;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mathwithmark.calculatorgamesolver.brutesolver.Game;
import com.mathwithmark.calculatorgamesolver.brutesolver.State;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Rule;
import com.mathwithmark.calculatorgamesolver.yaml.Mappable;

import org.yaml.snakeyaml.Yaml;

public class CalculatorGame implements Game, Mappable {
    /** The current number for this game */
    private final int VALUE;

    /** The goal number for this game */
    private final int GOAL;

    /** The moves left in this game */
    private final int MOVES_LEFT;

    /**
     * The rules that can be used in this game.
     */
    private final Rule[] RULES;

    /**
     * The portals present on this game. `null` indicates no portals.
     * If portals is not null, it is 2 elements, the first greater than the
     * second, the second nonnegative.
     * The first element indicates the zero-based distance from the ones index
     * of the left portal, the second that same index for the right portal. 0
     * is ones place, 1 is tens place, 2 is hundreds place, etc.
     */
    private final int[] PORTALS;

    /**
     * Create a level of the given parameters
     *
     * If any of the parameters are invalid, throws an exception
     *
     * @param value The start value
     * @param goal The goal value
     * @param moves The number of moves to be used
     * @param rules The rules that can be applied
     * @param portals The portals of this level
     *
     * @throws IllegalArgumentException if any parameters are invalid
     */
    public CalculatorGame(
        int value,
        int goal,
        int moves,
        Rule[] rules,
        int[] portals
    )
        throws IllegalArgumentException {
        ValidateUtils.validateParameters(value, goal, moves, rules, portals);
        this.VALUE = applyPortals(portals, value);
        this.GOAL = goal;
        this.MOVES_LEFT = moves;
        this.RULES = sanitize(rules);
        this.PORTALS = portals;
    }

    /**
     * Returns a version of the rules with an update store rule present if a
     * store rule is present. Ensures no duplicate rules are present
     * @param rules a valid rules array
     * @return an array of sanitized rules
     */
    private static Rule[] sanitize(Rule[] rules) {
        List<Rule> newRules = new ArrayList<>();
        for (int i = 0; i < rules.length; i++) {
            Rule rule = rules[i];
            if (newRules.indexOf(rule) != -1) continue; // don't add duplicate
            newRules.add(rule);
            if (rule.getOperator() == Config.STORE) {
                newRules.add(Rule.makeRule(Config.UPDATE_STORE));
            }
        }
        return newRules.toArray(new Rule[0]);
    }

    public int getValue() {
        return VALUE;
    }

    public int getGoal() {
        return GOAL;
    }

    public int getMovesLeft() {
        return MOVES_LEFT;
    }

    public boolean isValidRule(Rule rule) {
        return Arrays.asList(RULES).contains(rule);
    }

    /** The valid rules for this game */
    public Rule[] getRules() {
        return Arrays.copyOf(RULES, RULES.length);
    }

    public int[] getPortals() {
        if (!hasPortals()) return null;
        return Arrays.copyOf(PORTALS, PORTALS.length);
    }

    /**
     * Make the digits of value "fall through" the portals
     * @param portals null or a two-element array where the first element is
     * greater than the second AND the second is at least 0
     * @param value the value before the digits fall through the portals
     * @return the value after the digits fall through the portals (the same
     * value if portals == null)
     */
    private static int applyPortals(int[] portals, int value) {
        if (portals == null) return value;

        boolean negative = value < 0;
        value = Math.abs(value); // only worry about the positive version

        int leftPortalIndex = portals[0];
        int rightPortalIndex = portals[1];

        while (value >= Math.pow(10, leftPortalIndex)) {
            // Have the digit fall
            int digit = Helpers.getDigit(value, leftPortalIndex);
            value -= digit * Math.pow(10, leftPortalIndex);
            value += digit * Math.pow(10, rightPortalIndex);

            // Take digits at left of portal and shift them one right
            int valueLeft = Helpers.digitsToTheLeft(value, leftPortalIndex);
            value -= valueLeft * Math.pow(10, leftPortalIndex + 1);
            value += valueLeft * Math.pow(10, leftPortalIndex);
        }

        return negative ? -value : value;
    }

    public boolean equals(Object other) {
        if (other instanceof CalculatorGame) {
            CalculatorGame otherGame = (CalculatorGame) other;
            return otherGame.getValue() == getValue()
                && otherGame.getGoal() == getGoal()
                && otherGame.getMovesLeft() == getMovesLeft()
                && Arrays.equals(otherGame.getRules(), getRules())
                && Arrays.equals(otherGame.getPortals(), getPortals());
        } else
            return false;
    }

    /**
     * Returns true if the two games are equal in every way except moves
     * remaining
     * @param other The other game
     * @return true iff the two games are equal if their moves are the same.
     * Returns true if the two games are equal.
     */
    public boolean equalsExceptMoves(CalculatorGame other) {
        CalculatorGame newOther =
            new CalculatorGame(
                other.getValue(),
                other.getGoal(),
                getMovesLeft(),
                other.getRules(),
                other.getPortals()
            );
        return equals(newOther);
    }

    public boolean hasPortals() {
        return PORTALS != null;
    }

    /**
     * Get the successors of this as a list of States.
     *
     * @param parent a state whose game is equal to this.
     * @return a list of states with a parent whose game is this. Each game is a
     * successor of this, either by applying a rule or updating a rule. If this
     * game is won, returns an empty list.
     */
    public List<State> getSuccessors(State parent) {
        List<State> successors = new ArrayList<>();
        for (Rule rule : getRules()) {
            CalculatorGame successorGame = getSuccessor(rule);
            if (successorGame == null) continue;
            String transitionString = CalculatorGame.transitionString(rule);
            State successorState =
                new State(successorGame, parent, transitionString);
            successors.add(successorState);
        }
        return successors;
    }

    /**
     * Generates a successor game
     * @param rule the rule to apply
     * @return null if the successor would be invalid, otherwise the successor
     */
    private CalculatorGame getSuccessor(Rule rule) {
        if (MOVES_LEFT == 0) return null;
        return rule.apply(this);
    }

    /**
     * Generates a string describing the transition from this to a successor
     * @param rule the rule of interest
     * @param applied true to apply the rule, false to update the rule
     * @return the string representing the use of the rule
     */
    public static String transitionString(Rule rule) {
        return rule.toString();
    }

    /**
     * The YAML external representation for this object
     */
    @Override
    public String toString() {
        Yaml yaml = new Yaml();
        StringWriter sb = new StringWriter();
        yaml.dump(this.toMap(), sb);
        return sb.toString();
    }

    @Override
    public boolean isWon() {
        return VALUE == GOAL;
    }

    public boolean isLost() {
        return MOVES_LEFT <= 0 && !isWon();
    }

    @Override
    public boolean roughlyEquals(Game other) {
        return other instanceof CalculatorGame
            && equalsExceptMoves((CalculatorGame) other);
    }

    @Override
    public Map<String, Object> toMap() {
        return MappableUtils.gameToMap(this);
    }

    /**
     * Returns the CalculatorGame described by the given map
     *
     * Assumes the given map describes a game, returns null otherwise
     */
    public static CalculatorGame from(Map<String, Object> map) {
        return MappableUtils.mapToGame(map);
    }
}

/**
 * Utility methods for converting the game to a map
 */
class MappableUtils {
    /**
     * Whether this rule should be serialized as part the external
     * representation of the game.
     * @param rule not null
     * @return true unless the rule is an Update Store rule
     */
    private static boolean isExternal(Rule rule) {
        return rule.getOperator() != Config.UPDATE_STORE;
    }

    /**
     * Creates an external representation of the rules of the game
     */
    private static List<String> externalRuleStrings(Rule[] rules) {
        List<String> ruleStrings = new ArrayList<String>();
        for (int i = 0; i < rules.length; i++) {
            if (isExternal(rules[i])) ruleStrings.add(rules[i].toString());
        }
        return ruleStrings;
    }

    /**
     * Converts the given object to an array of rules
     * @param obj an ArrayList<Rule> (type hidden by deserialization)
     * @return an array of rules constructed from the given object
     */
    private static Rule[] toRulesArray(Object obj) {
        ArrayList<?> list = (ArrayList<?>) obj;
        Rule[] rules = new Rule[list.size()];
        for (int i = 0; i < list.size(); i++) {
            rules[i] = Rule.ruleFromString(list.get(i).toString());
        }
        return rules;
    }

    /**
     * Converts the given object to an array of ints, representing the portals
     * @param obj an ArrayList<Integer> (type hidden by deserialization)
     * @return an array of integers constructed from the given object
     */
    private static int[] toPortalsArray(Object obj) {
        if (obj == null) return null;
        ArrayList<?> list = (ArrayList<?>) obj;
        int[] portals = new int[list.size()];
        for (int i = 0; i < list.size(); i++) {
            portals[i] = Integer.parseInt(list.get(i).toString());
        }
        return portals;
    }

    static Map<String, Object> gameToMap(CalculatorGame game) {
        Map<String, Object> map = new HashMap<>();
        map.put("value", game.getValue());
        map.put("goal", game.getGoal());
        map.put("moves", game.getMovesLeft());
        map.put("rules", externalRuleStrings(game.getRules()));
        map.put("portals", game.getPortals());
        return map;
    }

    /**
     * Returns null if the given map isn't of the correct format
     */
    static CalculatorGame mapToGame(Map<String, Object> map) {
        return new CalculatorGame(
            (int) map.get("value"),
            (int) map.get("goal"),
            (int) map.get("moves"),
            toRulesArray(map.get("rules")),
            toPortalsArray(map.get("portals"))
        );
    }
}

/**
 * Utility functions to validate parameters used to instantiate CalculatorGame
 */
class ValidateUtils {
    /**
     * Validates the given parameters. Does nothing if valid, throws exception
     * if any parameters are invalid.
     * @throws IllegalArgumentException if any parameters are invalid
     */
    static void validateParameters(
        int value,
        int goal,
        int moves,
        Rule[] rules,
        int[] portals
    )
        throws IllegalArgumentException {

        if (Helpers.numDigits(value) > Config.MAX_DIGITS) {
            throwException(
                "Too many digits in '%d', max %d",
                value,
                Config.MAX_DIGITS
            );
        }
        if (moves < 0) {
            throwException(
                "Cannot have negative moves remaining, must be at least 0"
            );
        }
        validateRules(rules);
        validatePortals(portals);
    }

    /**
     * Ensures that the given rules array is valid and can be sanitized. Does
     * not check for duplicate rules. Throws an exception if there is an Update
     * Store rule but no Store rule.
     *
     * @throws IllegalArgumentException if the rules are invalid
     */
    private static void validateRules(Rule[] rules)
        throws IllegalArgumentException {

        if (
            rulesContains(rules, Config.UPDATE_STORE)
                && !rulesContains(rules, Config.STORE)
        ) {
            throwException(
                "Illegal rules: '%s'. "
                    + "Cannot have an Update Store rule without a Store rule",
                Arrays.toString(rules)
            );
        }
    }

    /**
     * Validates the given portals, ensuring they can be used in a game. Does
     * nothing if the portals are valid, throws an exception if they aren't.
     * @param portals the portals to validate
     * @throws IllegalArgumentException if the portals are invalid
     */
    private static void validatePortals(int[] portals)
        throws IllegalArgumentException {
        if (!validPortals(portals)) {
            throwException(
                "Illegal portals: '%s'. "
                    + "Must be two integers, "
                    + "the first larger than the second, both >= 0",
                Arrays.toString(portals)
            );
        }
    }

    /**
     * Formats the message and throws an {@code IllegalArgumentException}
     * @param message the message to format
     * @param args the args to fill in the placeholder values
     * @throws IllegalArgumentException with the formatted version of the
     * message
     */
    private static void throwException(String message, Object... args)
        throws IllegalArgumentException {

        String formattedMessage = String.format(message, args);
        throw new IllegalArgumentException(formattedMessage);
    }

    /**
     * @return true iff the array contains a rule with the given operator
     */
    private static boolean rulesContains(Rule[] rules, int operator) {
        for (Rule rule : rules) {
            if (rule.getOperator() == operator) return true;
        }
        return false;
    }

    /**
     * Valid portals are (null) OR (a two-element array where the first element
     * is greater than the second AND the second is at least 0)
     * @param portals the array to check
     * @return true iff the parameter is valid
     */
    private static boolean validPortals(int[] portals) {
        if (portals == null) return true;
        if (portals.length != 2) return false;
        return portals[0] > portals[1] && portals[1] >= 0;
    }
}
