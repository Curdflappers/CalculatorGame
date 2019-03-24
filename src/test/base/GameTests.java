package base;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class GameTests {

    ///////////////////
    // VALID PORTALS //
    ///////////////////

    @Test
    void oneAndZeroAreValid() {
        int[] validPortals = {
            1, 0
        };

        assertTrue(Game.validPortals(validPortals));
    }

    @Test
    void negativesAreInvalid() {
        int[] negatives = {
            -2, -3
        };

        assertFalse(Game.validPortals(negatives));
    }

    @Test
    void zerosAreInvalid() {
        int[] zeros = {
            0, 0
        };

        assertFalse(Game.validPortals(zeros));
    }

    @Test
    void lengthZeroIsInvalid() {
        int[] zeroLength = new int[0];

        assertFalse(Game.validPortals(zeroLength));
    }

    @Test
    void lengthOneIsInvalid() {
        int[] lengthOne = {
            1
        };

        assertFalse(Game.validPortals(lengthOne));
    }

    @Test
    void anyNegativeIsInvalid() {
        int[] aNegative = {
            1, -1
        };

        assertFalse(Game.validPortals(aNegative));
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

        Game game = new Game(value, 0, 0, null, portals);

        assertEquals(expected, (int) game.getValue());
    }

    @Test
    void decimalsDoNothing() {
        double value = 10.5;
        int[] portals = {
            1, 0
        };
        double expected = value;

        Game game = new Game(value, 0, 0, null, portals);
        double actual = game.getValue();

        assertEquals(expected, actual, 0.001);
    }
}
