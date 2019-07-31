package com.mathwithmark.calculatorgamesolver.calculatorgame;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mathwithmark.calculatorgamesolver.brutesolver.Game;
import com.mathwithmark.calculatorgamesolver.brutesolver.State;
import com.mathwithmark.calculatorgamesolver.calculatorgame.Rule;
import com.mathwithmark.calculatorgamesolver.yaml.Mappable;

public class CalculatorGame implements Game, Mappable {
    /** The current number for this game */
    private int value;

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
     * Create a game of the given parameters
     *
     * @param value The start state
     * @param goal The end state
     * @param moves The number of moves to be used
     * @param rules The rules that can be used
     * @param portals The portals of this game
     */
    private CalculatorGame(
        int value,
        int goal,
        int moves,
        Rule[] rules,
        int[] portals
    ) {
        this.value = value;
        this.GOAL = goal;
        this.MOVES_LEFT = moves;
        this.RULES = rules;
        this.PORTALS = portals;
        applyPortals();
    }

    public static CalculatorGame generateGame(
        int value,
        int goal,
        int moves,
        Rule[] rules,
        int[] portals
    ) {
        return generateGame(String.valueOf(value), goal, moves, rules, portals);
    }

    public static CalculatorGame generateGame(
        String valueString,
        int goal,
        int moves,
        Rule[] rules,
        int[] portals
    ) {
        int numDigits = Helpers.numDigits(valueString);
        if (
            numDigits <= Config.MAX_DIGITS
                && validPortals(portals)
                && rules != null
        ) {
            int value = Integer.parseInt(valueString);
            if (value != Integer.MAX_VALUE && value != Integer.MIN_VALUE) {
                rules = sanitize(rules);
                return new CalculatorGame(value, goal, moves, rules, portals);
            }
        }
        return null;
    }

    /**
     * Returns a version of the rules with the meta store rules added for each
     * store rule present. Returns the original array if no store rules are
     * present. Ensures no double meta store rules exist.
     * @param rules the rules to which to add meta store rules.
     * @return an array of rules with meta store rules added
     */
    private static Rule[] sanitize(Rule[] rules) {
        List<Rule> newRules = Helpers.copyAsList(rules);
        for (int i = 0; i < rules.length; i++) {
            Rule rule = rules[i];
            if (rule.getOperator() == Config.STORE) {
                MetaStoreRule metaStoreRule = new MetaStoreRule(i);
                if (newRules.indexOf(metaStoreRule) == -1) {
                    newRules.add(new MetaStoreRule(i));
                }
            }
        }
        Rule[] newRulesArray = new Rule[newRules.size()];
        newRules.toArray(newRulesArray);
        return newRulesArray;
    }

    public int getValue() {
        return value;
    }

    private void setValue(int value) {
        this.value = value;
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
     * Valid portals are (null) OR (a two-element array where the first element
     * is greater than the second AND the second is at least 0)
     * @param portals the array to check
     * @return true iff the parameter is valid
     */
    static boolean validPortals(int[] portals) {
        if (portals == null) return true;
        if (portals.length != 2) return false;
        return portals[0] > portals[1] && portals[1] >= 0;
    }

    /**
     * Make the digits "fall through" the portals to get the correct value
     */
    private void applyPortals() {
        if (!hasPortals()) return;

        int toyValue = getValue();
        boolean negative = toyValue < 0;
        toyValue = Math.abs(toyValue); // only worry about the positive version

        int leftPortalIndex = PORTALS[0];
        int rightPortalIndex = PORTALS[1];

        while (toyValue >= Math.pow(10, leftPortalIndex)) {
            // Have the digit fall
            int digit = Helpers.getDigit(toyValue, leftPortalIndex);
            toyValue -= digit * Math.pow(10, leftPortalIndex);
            toyValue += digit * Math.pow(10, rightPortalIndex);

            // Take digits at left of portal and shift them one right
            int valueLeft = Helpers.digitsToTheLeft(toyValue, leftPortalIndex);
            toyValue -= valueLeft * Math.pow(10, leftPortalIndex + 1);
            toyValue += valueLeft * Math.pow(10, leftPortalIndex);
        }

        setValue(negative ? -toyValue : toyValue);
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
            CalculatorGame
                .generateGame(
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
     * PRECONDITION: parent == null OR parent.getGame().equals(this)
     * @param predecessor null means no parent, otherwise a state whose game is
     * equal to this.
     * @return a list of states with a parent whose game is this. Each game is a
     * successor of this, either by applying a rule or updating a rule. If this
     * game is won, returns an empty list.
     */
    public List<State> getSuccessors(State predecessor) {
        List<State> successors = new ArrayList<>();
        if (predecessor == null) predecessor = rootState();
        addSuccessors(predecessor, successors);
        return successors;
    }

    private void addSuccessors(State parent, List<State> successors) {
        for (Rule rule : getRules()) {
            CalculatorGame successorGame = getSuccessor(rule);
            if (successorGame == null) continue;
            String transitionString = CalculatorGame.transitionString(rule);
            State successorState =
                new State(successorGame, parent, transitionString);
            successors.add(successorState);
        }
    }

    /**
     * Generates a successor game
     * @param rule
     * @param applied
     * @return null if the successor would be invalid, otherwise the successor
     */
    private CalculatorGame getSuccessor(Rule rule) {
        if (MOVES_LEFT == 0) return null;
        CalculatorGame potentialSuccessor = rule.apply(this);
        return potentialSuccessor;
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

    public String toString() {
        String str = "{";
        str += " value: " + (int) value + ",";
        str += " goal: " + GOAL + ",";
        str += " movesLeft: " + MOVES_LEFT + ",";

        str += " rules: [";
        for (int i = 0; i < RULES.length; i++) {
            Rule rule = RULES[i];
            if (i != 0) str += ", ";
            str += rule.toString();
        }
        str += "],";

        str += " portals: " + Arrays.toString(PORTALS);
        str += " }";
        return str;
    }

    public boolean isWon() {
        return value == GOAL;
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
     * Creates an external representation of the rules of the game
     */
    private static List<String> externalRuleStrings(Rule[] rules) {
        List<String> ruleStrings = new ArrayList<String>();
        for (int i = 0; i < rules.length; i++) {
            if (rules[i].EXTERNAL) ruleStrings.add(rules[i].toString());
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
        return CalculatorGame
            .generateGame(
                (int) map.get("value"),
                (int) map.get("goal"),
                (int) map.get("moves"),
                toRulesArray(map.get("rules")),
                toPortalsArray(map.get("portals"))
            );
    }
}
