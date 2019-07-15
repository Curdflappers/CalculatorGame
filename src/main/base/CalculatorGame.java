package base;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Game;
import game.State;
import rules.Rule;

public class CalculatorGame implements Game {
    /** The current number for this game */
    private double value; // TODO change to int

    /** The goal number for this game */
    private final int goal;

    /** The moves left in this game */
    private final int movesLeft;

    /**
     * The rules that can be used in this game.
     */
    private final Rule[] validRules;

    /**
     * The portals present on this game. `null` indicates no portals.
     * If portals is not null, it is 2 elements, the first greater than the
     * second, the second nonnegative.
     * The first element indicates the zero-based distance from the ones index
     * of the left portal, the second that same index for the right portal. 0
     * is ones place, 1 is tens place, 2 is hundreds place, etc.
     */
    private final int[] portals;

    /**
     * Create a game of the given parameters
     *
     * @param value The start state
     * @param goal The end state
     * @param moves The number of moves to be used
     * @param rules The rules that can be used
     * @param portals The portals of this game
     */
    // TODO make impossible to instantiate invalid game
    public CalculatorGame(
        double value,
        int goal,
        int moves,
        Rule[] rules,
        int[] portals
    ) {
        this.value = value;
        this.goal = goal;
        this.movesLeft = moves;
        this.validRules = rules;
        if (validPortals(portals)) this.portals = portals;
        else
            throw new RuntimeException(
                "Invalid portals given: " + Arrays.toString(portals)
            );
        applyPortals();
    }

    public double getValue() {
        return value;
    }

    private void setValue(double value) {
        this.value = value;
    }

    public int getGoal() {
        return goal;
    }

    public int getMovesLeft() {
        return movesLeft;
    }

    public boolean isValidRule(Rule rule) {
        return Arrays.asList(validRules).contains(rule);
    }

    /** The valid rules for this game */
    public Rule[] getRules() {
        return Arrays.copyOf(validRules, validRules.length);
    }

    public int[] getPortals() {
        if (!hasPortals()) return null;
        return Arrays.copyOf(portals, portals.length);
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
     * Decimals don't fall through the portals
     */
    private void applyPortals() {
        if (!hasPortals() || getValue() % 1 != 0) return;

        int value = (int) getValue();
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

        setValue(negative ? -value : value);
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
        return portals != null;
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
        addSuccessors(predecessor, successors, true);
        addSuccessors(predecessor, successors, false);
        return successors;
    }

    private void addSuccessors(
        State parent,
        List<State> successors,
        boolean applied
    ) {
        for (Rule rule : getRules()) {
            CalculatorGame successorGame = getSuccessor(rule, applied);
            if (successorGame == null) continue;
            String transitionString = CalculatorGame.transitionString(
                rule,
                applied
            );
            State successorState = new State(
                successorGame,
                parent,
                transitionString
            );
            successors.add(successorState);
        }
    }

    /**
     * Generates a successor game
     * @param rule
     * @param applied
     * @return null if the successor would be invalid, otherwise the successor
     */
    private CalculatorGame getSuccessor(Rule rule, boolean applied) {
        if (movesLeft == 0) return null;
        CalculatorGame potentialSuccessor = null;
        if (applied) {
            potentialSuccessor = rule.apply(this);
        } else {
            potentialSuccessor = rule.update(this);
        }
        if (isValid(potentialSuccessor)) return potentialSuccessor;
        else return null;
    }

    private boolean isValid(CalculatorGame game) {
        boolean valid = true;
        valid &= game.getValue() % 1 == 0; // no decimals
        valid &= Math.abs(game.getValue()) < Math.pow(10, 6); // max 6 digits
        return valid;
    }

    /**
     * Generates a string describing the transition from this to a successor
     * @param rule the rule of interest
     * @param applied true to apply the rule, false to update the rule
     * @return the string representing the use of the rule
     */
    public static String transitionString(Rule rule, boolean applied) {
        String s = "";
        s += applied ? Config.APPLY_PROMPT : Config.UPDATE_PROMPT;
        // space included in prompt
        s += rule;
        return s;
    }

    public String toString() {
        String str = "";
        str += (int) value + "\n";
        str += goal + "\n";
        str += movesLeft + "\n";

        for (Rule rule : validRules) {
            str += rule.toString() + "\n";
        }
        str += "\n";

        str += (hasPortals() ? "y" : "n") + "\n";
        if (hasPortals()) {
            str += portals[0] + "\n";
            str += portals[1] + "\n";
        }

        return str;
    }

    public boolean isWon() {
        return value == goal;
    }

    @Override
    public boolean roughlyEquals(Game other) {
        return other instanceof CalculatorGame
            && equalsExceptMoves((CalculatorGame)other);
    }
}
