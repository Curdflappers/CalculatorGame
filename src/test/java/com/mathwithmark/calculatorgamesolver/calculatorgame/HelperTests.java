package com.mathwithmark.calculatorgamesolver.calculatorgame;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class HelperTests {
}

class GetDigitTests {
    @Test
    void getDigit() {
        int value = 12345;
        int digitIndex = 3;
        int expected = 2;

        int actual = Helpers.getDigit(value, digitIndex);

        assertEquals(expected, actual);
    }
}

class DigitsToTheLeftTests {
    @Test
    void digitsToTheLeft() {
        int value = 12345;
        int digitIndex = 2;
        int expected = 12;

        int actual = Helpers.digitsToTheLeft(value, digitIndex);

        assertEquals(expected, actual);
    }
}

class NumDigitsTests {
    @Test
    void valueZero() {
        int value = 0;
        int expected = 1;

        int actual = Helpers.numDigits(value);

        assertEquals(expected, actual);
    }

    @Test
    void valueOne() {
        int value = 1;
        int expected = 1;

        int actual = Helpers.numDigits(value);

        assertEquals(expected, actual);
    }

    @Test
    void valueNine() {
        int value = 9;
        int expected = 1;

        int actual = Helpers.numDigits(value);

        assertEquals(expected, actual);
    }

    @Test
    void valueTen() {
        int value = 10;
        int expected = 2;

        int actual = Helpers.numDigits(value);

        assertEquals(expected, actual);
    }

    @Test
    void multipleDigits() {
        int value = 12345;
        int expected = 5;

        int actual = Helpers.numDigits(value);

        assertEquals(expected, actual);
    }
}
