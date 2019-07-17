package brutesolver;

import java.util.List;

public interface Game {
    public boolean isWon();
    public boolean roughlyEquals(Game other);
    public List<State> getSuccessors(State state);

    /**
     * Creates a root state representing this game
     * @return a state with no parents whose game is this
     */
    public default State rootState() {
        return new State(this);
    }
}
