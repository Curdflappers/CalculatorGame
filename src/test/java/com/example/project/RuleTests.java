package com.example.project;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class RuleTests {
    @Test
    void shiftRule() {
        int[] zeroDigits = ShiftRule.digits(0);

        assertEquals(1, zeroDigits.length);
        assertEquals(0, zeroDigits[0]);
    }
}
