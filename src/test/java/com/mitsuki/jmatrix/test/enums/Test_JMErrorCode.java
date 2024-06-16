package com.mitsuki.jmatrix.test.enums;

import com.mitsuki.jmatrix.enums.JMErrorCode;

import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for the {@link JMErrorCode} enum.
 *
 * <p>This class contains various test methods to verify the functionality
 * of the {@link JMErrorCode} enum, including tests for retrieving error numbers,
 * error codes, error messages, and custom string representations.
 *
 * @since    1.5.0
 * @version  1.0, 17 June 2024
 * @author   <a href="https://github.com/mitsuki31" target="_blank">
 *           Ryuu Mitsuki</a>
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *           Apache License 2.0</a>
 * @see      JMErrorCode
 */
public class Test_JMErrorCode {
    /**
     * A list of expected error numbers for each {@link JMErrorCode} value.
     */
    List<Integer> expectedErrno;
    /**
     * A list of expected string representations of error numbers for each
     * {@link JMErrorCode} value.
     */
    List<String> expectedErrnoStr;
    /**
     * A list of expected error codes for each {@link JMErrorCode} value.
     */
    List<String> expectedErrcode;
    /**
     * The format string for the custom {@link JMErrorCode#toString()} method.
     */
    String toStringFormat = "%s[%s]";

    /**
     * Initializes the expected error numbers, error codes, and their string
     * representations before each test is run.
     */
    @Before
    public void setup() {
        expectedErrno = Arrays.asList(
            201,  // INVIDX
            202,  // INVTYP
            203,  // NULLMT
            400   // UNKERR
        );

        expectedErrcode = Arrays.asList(
            "INVIDX",  // 201
            "INVTYP",  // 202
            "NULLMT",  // 203
            "UNKERR"   // 400
        );

        expectedErrnoStr = new ArrayList<>(expectedErrno.size());
        for (Integer errno : expectedErrno) {
            expectedErrnoStr.add("JM" + errno);
        }
    }

    /**
     * Tests the {@link JMErrorCode#getErrno()} method to ensure that it returns
     * the correct error number for each {@link JMErrorCode} value.
     */
    @Test
    public void test_getErrno() {
        int n = 0;
        for (JMErrorCode ec : JMErrorCode.values()) {
            int actualErrno = ec.getErrno();
            assertTrue(expectedErrno.get(n++) == actualErrno);
        }
    }

    /**
     * Tests the {@link JMErrorCode#getErrnoStr()} method to ensure that it returns
     * the correct string representation of the error number for each {@link
     * JMErrorCode} value.
     */
    @Test
    public void test_getErrnoStr() {
        int n = 0;
        for (JMErrorCode ec : JMErrorCode.values()) {
            String actualErrnoStr = ec.getErrnoStr();
            assertNotNull(actualErrnoStr);
            assertEquals(expectedErrnoStr.get(n++), actualErrnoStr);
        }
    }

    /**
     * Tests the {@link JMErrorCode#getCode()} method to ensure that it returns
     * the correct error code for each {@link JMErrorCode} value.
     */
    @Test
    public void test_getErrcode() {
        int n = 0;
        for (JMErrorCode ec : JMErrorCode.values()) {
            String actualCode = ec.getCode();
            assertNotNull(actualCode);
            assertEquals(expectedErrcode.get(n++), actualCode);
        }
    }

    /**
     * Tests the {@link JMErrorCode#getMessage()} method to ensure that it returns
     * a non-null message for each {@link JMErrorCode} value.
     */
    @Test
    public void test_getMessage() {
        for (JMErrorCode ec : JMErrorCode.values()) {
            String actualMessage = ec.getMessage();
            // Currently, only test for non-null
            assertNotNull(actualMessage);
        }
    }

    /**
     * Tests the {@link JMErrorCode#toString()} method to ensure that it returns
     * the correct custom string representation for each {@link JMErrorCode} value.
     */
    @Test
    public void test_toString() {
        int n = 0;
        for (JMErrorCode ec : JMErrorCode.values()) {
            String expectedString = String.format(
                toStringFormat, expectedErrcode.get(n), expectedErrno.get(n));
            String actualString = ec.toString();
            assertNotNull(actualString);
            assertEquals(expectedString, actualString);
            n++;
        }
    }

    /**
     * Tests the {@link JMErrorCode#valueOf(Object)} method to ensure that
     * they return the correct {@link JMErrorCode} value based on the input.
     */
    @Test
    public void test_valueOf() {
        // Test using a string (error code)
        JMErrorCode fromStr = JMErrorCode.valueOf("NULLMT");
        assertNotNull(fromStr);
        assertEquals("NULLMT", fromStr.getCode());
        assertTrue(203 == fromStr.getErrno());

        // Test using an integer (errno)
        JMErrorCode fromInt = JMErrorCode.valueOf(201);
        assertNotNull(fromInt);
        assertEquals("INVIDX", fromInt.getCode());
        assertTrue(201 == fromInt.getErrno());

        // Test using unknown type
        JMErrorCode nullEC = JMErrorCode.valueOf(new int[1]);
        assertNull(nullEC);
        assertNotSame(fromStr, nullEC);
        assertNotSame(fromInt, nullEC);
    }

    /**
     * Tests the {@link JMErrorCode#valueOf(String)} method to ensure that it throws
     * an {@link IllegalArgumentException} when an unknown error code is provided.
     */
    @Test(expected=java.lang.IllegalArgumentException.class)
    public void test_valueOfThrows() {
        JMErrorCode.valueOf("UNKNOWN");
    }
}
