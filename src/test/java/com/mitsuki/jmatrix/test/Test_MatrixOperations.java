package com.mitsuki.jmatrix.test;

import com.mitsuki.jmatrix.Matrix;
import com.mitsuki.jmatrix.util.MatrixUtils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Test_MatrixOperations {
    @Test
    public void test_MatrixAddition() {
        Matrix m = new Matrix(new double[][] {
            { 1, 3, 5 },
            { 5, 7, 4 }
        });

        Matrix n = new Matrix(new double[][] {
            { 6, 7, -5 },
            { -10, 5, 16 }
        });

        Matrix res = new Matrix(new double[][] {
            { 7, 10, 0 },
            { -5, 12, 20 }
        });

        assertEquals(false, MatrixUtils.isNullEntries(m));
        assertEquals(false, MatrixUtils.isNullEntries(n));
        assertEquals(false, m.equals(n));
        assertEquals(false, m.isSquare());

        // Before operate the addition, ensure both operands are same dimensions
        assertEquals(true, MatrixUtils.isEqualsSize(m, n));

        // Check the addition results
        assertEquals(true, Matrix.sum(m, n).equals(res));
    }

    @Test
    public void test_MatrixSubtraction() {
        Matrix m = new Matrix(new double[][] {
            { 5, -1 },
            { 8, 16 },
            { 4, 10 },
            { 1, -5 }
        });

        Matrix n = new Matrix(new double[][] {
            { 15, -18 },
            { 6, 21 },
            { 0, -3 },
            { -1, 4 }
        });

        Matrix res = new Matrix(new double[][] {
            { -10, 17 },
            { 2, -5 },
            { 4, 13 },
            { 2, -9 }
        });

        assertEquals(false, MatrixUtils.isNullEntries(m));
        assertEquals(false, MatrixUtils.isNullEntries(n));
        assertEquals(false, m.equals(n));
        assertEquals(false, m.isSquare());

        // Before operate the subtraction, ensure both operands are same dimensions
        assertEquals(true, MatrixUtils.isEqualsSize(m, n));

        // Check the subtraction results
        assertEquals(true, Matrix.sub(m, n).equals(res));
    }
}
