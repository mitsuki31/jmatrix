package com.mitsuki.jmatrix.test;

import com.mitsuki.jmatrix.Matrix;
import com.mitsuki.jmatrix.util.MatrixUtils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

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

        Matrix mI = Matrix.identity(6);
        Matrix nI = Matrix.identity(6);

        Matrix expectedRes = new Matrix(new double[][] {
            { 7, 10, 0 },
            { -5, 12, 20 }
        });

        Matrix expectedResI = new Matrix(new double[][] {
            { 2, 0, 0, 0, 0, 0 },
            { 0, 2, 0, 0, 0, 0 },
            { 0, 0, 2, 0, 0, 0 },
            { 0, 0, 0, 2, 0, 0 },
            { 0, 0, 0, 0, 2, 0 },
            { 0, 0, 0, 0, 0, 2 }
        });

        // Test and check for null entries
        assertFalse(MatrixUtils.isNullEntries(m));
        assertFalse(MatrixUtils.isNullEntries(n));
        assertNotNull(m.getEntries());
        assertNotNull(n.getEntries());

        assertFalse(MatrixUtils.isNullEntries(mI));
        assertFalse(MatrixUtils.isNullEntries(nI));
        assertNotNull(mI.getEntries());
        assertNotNull(nI.getEntries());

        // Test and check the dimensions
        assertEquals(2, m.getSize()[0]);
        assertEquals(3, m.getSize()[1]);
        assertEquals(2, n.getSize()[0]);
        assertEquals(3, n.getSize()[1]);
        assertTrue(MatrixUtils.isEqualsSize(m, n));

        assertEquals(6, mI.getSize()[0]);
        assertEquals(6, mI.getSize()[1]);
        assertEquals(6, nI.getSize()[0]);
        assertEquals(6, nI.getSize()[1]);
        assertTrue(MatrixUtils.isEqualsSize(mI, nI));

        // Test and check the matrix type
        assertFalse(m.isSquare());
        assertFalse(n.isSquare());

        assertTrue(mI.isSquare());
        assertTrue(mI.isDiagonal());
        assertTrue(nI.isSquare());
        assertTrue(nI.isDiagonal());

        // Perform addition and check the results
        Matrix res = Matrix.sum(m, n);
        Matrix resI = Matrix.sum(mI, nI);

        assertEquals(expectedRes, res);
        assertEquals(expectedResI, resI);
        assertTrue(res.equals(expectedRes));
        assertTrue(resI.equals(expectedResI));
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

        Matrix mI = Matrix.identity(12);
        Matrix nI = Matrix.identity(12);

        Matrix res = new Matrix(new double[][] {
            { -10, 17 },
            { 2, -5 },
            { 4, 13 },
            { 2, -9 }
        });

        // The result of "mI" - "nI" = 0 (zero matrix)
        // with dimensions still the same (12x12)
        Matrix resI = new Matrix(12, 12);

        assertEquals(false, MatrixUtils.isNullEntries(m));
        assertEquals(false, MatrixUtils.isNullEntries(n));
        assertEquals(false, m.equals(n));
        assertEquals(false, (m.isSquare() && n.isSquare()));

        assertEquals(false, MatrixUtils.isNullEntries(mI));
        assertEquals(false, MatrixUtils.isNullEntries(nI));
        assertEquals(true, mI.equals(nI));
        assertEquals(true, (mI.isSquare() && nI.isSquare()));
        assertEquals(true, (mI.isDiagonal() && nI.isDiagonal()));

        // Before operate the subtraction, ensure both operands are same dimensions
        assertEquals(true, MatrixUtils.isEqualsSize(m, n));
        assertEquals(true, MatrixUtils.isEqualsSize(mI, nI));

        // Check the subtraction results
        assertEquals(true, Matrix.sub(m, n).equals(res));
        assertEquals(true, Matrix.sub(mI, nI).equals(resI));
    }

    @Test
    public void test_ScalarMultiplication() {
        Matrix m = new Matrix(new double[][] {
            { 4, 8, 2, -10 },
            { 3, 5, -4, 15 }
        });

        Matrix mI = Matrix.identity(8);

        Matrix res = new Matrix(new double[][] {
            { 20, 40, 10, -50 },
            { 15, 25, -20, 75 }
        });

        Matrix resI = new Matrix(new double[][] {
            { 5, 0, 0, 0, 0, 0, 0, 0 },
            { 0, 5, 0, 0, 0, 0, 0, 0 },
            { 0, 0, 5, 0, 0, 0, 0, 0 },
            { 0, 0, 0, 5, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 5, 0, 0, 0 },
            { 0, 0, 0, 0, 0, 5, 0, 0 },
            { 0, 0, 0, 0, 0, 0, 5, 0 },
            { 0, 0, 0, 0, 0, 0, 0, 5 }
        });

        final double constant = 5.0;

        assertEquals(false, MatrixUtils.isNullEntries(m));
        assertEquals(false, m.isSquare());

        assertEquals(false, MatrixUtils.isNullEntries(mI));
        assertEquals(true, mI.isSquare());
        assertEquals(true, mI.isDiagonal());

        // Check the scalar multiplication results
        assertEquals(true, Matrix.mult(m, constant).equals(res));
        assertEquals(true, Matrix.mult(mI, constant).equals(resI));
    }

    @Test
    public void test_MatrixMultiplication() {
        Matrix m = new Matrix(new double[][] {
            { 1, 3, 5 },
            { 2, 4, 6 }
        });

        Matrix n = new Matrix(new double[][] {
            { 9, 8 },
            { 7, 6 },
            { 5, 4 }
        });

        // Create 4x4 diagonal matrix with value on main diagonal = 8.5
        Matrix mI = Matrix.mult(Matrix.identity(4), 8.5);

        // Create 4x4 diagonal matrix with value on main diagonal = -4.5
        Matrix nI = Matrix.mult(Matrix.identity(4), -4.5);

        Matrix res = new Matrix(new double[][] {
            { 55, 46 },
            { 76, 64 }
        });

        Matrix resI = new Matrix(new double[][] {
            { -38.25, 0, 0, 0 },
            { 0, -38.25, 0, 0 },
            { 0, 0, -38.25, 0 },
            { 0, 0, 0, -38.25 }
        });

        assertEquals(false, MatrixUtils.isNullEntries(m));
        assertEquals(false, MatrixUtils.isNullEntries(n));
        assertEquals(false, (m.isSquare() && n.isSquare()));

        assertEquals(false, MatrixUtils.isNullEntries(mI));
        assertEquals(false, MatrixUtils.isNullEntries(nI));
        assertEquals(true, (mI.isSquare() && nI.isSquare()));
        assertEquals(true, (mI.isDiagonal() && nI.isDiagonal()));

        // Check the matrix multiplication results
        assertEquals(true, Matrix.mult(m, n).equals(res));
        assertEquals(true, Matrix.mult(mI, nI).equals(resI));
    }

    @Test
    public void test_MatrixTransposition() {
        Matrix m = new Matrix(new double[][] {
            { 6, 2, 3, 4, 1 },
            { 9, 2, -1, -9, 0 },
            { 11, -3, 2, 5, 3 }
        });

        Matrix x = new Matrix(new double[][] {
            { 3, 4, 5 },
            { 2, 1, 9 },
            { 0, 7, 8 }
        });

        Matrix mT = new Matrix(new double[][] {
            { 6, 9, 11 },
            { 2, 2, -3 },
            { 3, -1, 2 },
            { 4, -9, 5 },
            { 1, 0, 3 }
        });

        Matrix xT = new Matrix(new double[][] {
            { 3, 2, 0 },
            { 4, 1, 7 },
            { 5, 9, 8 }
        });

        assertEquals(false, MatrixUtils.isNullEntries(m));
        assertEquals(false, MatrixUtils.isNullEntries(x));
        assertEquals(false, m.isSquare());
        assertEquals(true, x.isSquare());
        assertEquals(false, x.isDiagonal());

        // Check the matrix transposition results
        assertEquals(true, Matrix.transpose(m).equals(mT));
        assertEquals(true, Matrix.transpose(x).equals(xT));
    }
}
