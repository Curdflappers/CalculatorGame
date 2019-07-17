package rules;

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

    @Test
    void numDigitsNegativeSign() {
        String valString = "-";
        int expected = 0;

        int actual = Helpers.numDigits(valString);

        assertEquals(expected, actual);
    }

    @Test
    void numDigitsPositiveOneDigit() {
        String valString = "1";
        int expected = 1;

        int actual = Helpers.numDigits(valString);

        assertEquals(expected, actual);
    }

    @Test
    void numDigitsPositiveMultipleDigits() {
        String valString = "12345";
        int expected = 5;

        int actual = Helpers.numDigits(valString);

        assertEquals(expected, actual);
    }

    @Test
    void numDigitsNegativeMultipleDigits() {
        String valString = "-12345";
        int expected = 5;

        int actual = Helpers.numDigits(valString);

        assertEquals(expected, actual);
    }
}
