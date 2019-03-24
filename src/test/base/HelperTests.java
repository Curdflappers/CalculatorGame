package base;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class HelperTests {
    @Test
    void getDigit() {
        int value = 12345;
        int digitIndex = 3;
        int expected = 2;

        int actual = Helpers.getDigit(value, digitIndex);

        assertEquals(expected, actual);
    }

    @Test
    void digitsToTheLeft() {
        int value = 12345;
        int digitIndex = 2;
        int expected = 12;

        int actual = Helpers.digitsToTheLeft(value, digitIndex);

        assertEquals(expected, actual);
    }
}
