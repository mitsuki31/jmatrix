package com.mitsuki.jmatrix.test;

import com.mitsuki.jmatrix.Matrix;
import com.mitsuki.jmatrix.core.MatrixUtils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
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
        assertEquals(2, m.getNumRows());
        assertEquals(3, m.getNumCols());
        assertEquals(2, n.getNumRows());
        assertEquals(3, n.getNumCols());
        assertTrue(MatrixUtils.isEqualsSize(m, n));

        assertEquals(6, mI.getNumRows());
        assertEquals(6, mI.getNumCols());
        assertEquals(6, nI.getNumRows());
        assertEquals(6, nI.getNumCols());
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

        Matrix expectedRes = new Matrix(new double[][] {
            { -10, 17 },
            { 2, -5 },
            { 4, 13 },
            { 2, -9 }
        });

        // The result of "mI" - "nI" = 0 (zero matrix)
        // with dimensions still the same (12x12)
        Matrix expectedResI = new Matrix(12, 12);

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
        assertEquals(4, m.getNumRows());
        assertEquals(2, m.getNumCols());
        assertEquals(4, n.getNumRows());
        assertEquals(2, n.getNumCols());
        assertTrue(MatrixUtils.isEqualsSize(m, n));

        assertEquals(12, mI.getNumRows());
        assertEquals(12, mI.getNumCols());
        assertEquals(12, nI.getNumRows());
        assertEquals(12, nI.getNumCols());
        assertTrue(MatrixUtils.isEqualsSize(mI, nI));

        // Test and check the matrix type
        assertFalse(m.isSquare());
        assertFalse(n.isSquare());

        assertTrue(mI.isSquare());
        assertTrue(nI.isSquare());
        assertTrue(mI.isDiagonal());
        assertTrue(nI.isDiagonal());

        // Perform subtraction and check the results
        Matrix res = Matrix.sub(m, n);
        Matrix resI = Matrix.sub(mI, nI);

        assertEquals(expectedRes, res);
        assertEquals(expectedResI, resI);
        assertTrue(res.equals(expectedRes));
        assertTrue(resI.equals(expectedResI));
    }

    @Test
    public void test_ScalarMultiplication() {
        Matrix m = new Matrix(new double[][] {
            { 4, 8, 2, -10 },
            { 3, 5, -4, 15 }
        });

        Matrix mI = Matrix.identity(8);

        Matrix expectedRes = new Matrix(new double[][] {
            { 20, 40, 10, -50 },
            { 15, 25, -20, 75 }
        });

        Matrix expectedResI = new Matrix(new double[][] {
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

        // Test and check for null entries
        assertFalse(MatrixUtils.isNullEntries(m));
        assertNotNull(m.getEntries());

        assertFalse(MatrixUtils.isNullEntries(mI));
        assertNotNull(mI.getEntries());

        // Test and check the dimensions
        assertEquals(2, m.getNumRows());
        assertEquals(4, m.getNumCols());

        assertEquals(8, mI.getNumRows());
        assertEquals(8, mI.getNumCols());

        // Test and check the matrix type
        assertFalse(m.isSquare());

        assertTrue(mI.isSquare());
        assertTrue(mI.isDiagonal());

        // Perform scalar multiplication and check the results
        Matrix res = Matrix.mult(m, constant);
        Matrix resI = Matrix.mult(mI, constant);

        assertEquals(expectedRes, res);
        assertEquals(expectedResI, resI);
        assertTrue(res.equals(expectedRes));
        assertTrue(resI.equals(expectedResI));
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

        Matrix expectedRes = new Matrix(new double[][] {
            { 55, 46 },
            { 76, 64 }
        });

        Matrix expectedResI = new Matrix(new double[][] {
            { -38.25, 0, 0, 0 },
            { 0, -38.25, 0, 0 },
            { 0, 0, -38.25, 0 },
            { 0, 0, 0, -38.25 }
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
        assertEquals(2, m.getNumRows());
        assertEquals(3, m.getNumCols());
        assertEquals(3, n.getNumRows());
        assertEquals(2, n.getNumCols());
        assertFalse(MatrixUtils.isEqualsSize(m, n));

        assertEquals(4, mI.getNumRows());
        assertEquals(4, mI.getNumCols());
        assertEquals(4, nI.getNumRows());
        assertEquals(4, nI.getNumCols());
        assertTrue(MatrixUtils.isEqualsSize(mI, nI));

        // Test and check the matrix type
        assertFalse(m.isSquare());
        assertFalse(n.isSquare());

        assertTrue(mI.isSquare());
        assertTrue(mI.isDiagonal());
        assertTrue(nI.isSquare());
        assertTrue(nI.isDiagonal());

        // Perform matrix multiplication and check the results
        Matrix res = Matrix.mult(m, n);
        Matrix resI = Matrix.mult(mI, nI);

        assertEquals(expectedRes, res);
        assertEquals(expectedResI, resI);
        assertTrue(res.equals(expectedRes));
        assertTrue(resI.equals(expectedResI));
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

        Matrix expected_mT = new Matrix(new double[][] {
            { 6, 9, 11 },
            { 2, 2, -3 },
            { 3, -1, 2 },
            { 4, -9, 5 },
            { 1, 0, 3 }
        });

        Matrix expected_xT = new Matrix(new double[][] {
            { 3, 2, 0 },
            { 4, 1, 7 },
            { 5, 9, 8 }
        });

        // Test and check for null entries
        assertFalse(MatrixUtils.isNullEntries(m));
        assertFalse(MatrixUtils.isNullEntries(x));
        assertNotNull(m.getEntries());
        assertNotNull(x.getEntries());

        // Test and check the dimensions
        assertEquals(3, m.getNumRows());
        assertEquals(5, m.getNumCols());

        assertEquals(3, x.getNumRows());
        assertEquals(3, x.getNumCols());

        // Test and check the matrix type
        assertFalse(m.isSquare());

        assertTrue(x.isSquare());
        assertFalse(x.isDiagonal());

        // Perform matrix transposition and check the results
        Matrix mT = Matrix.transpose(m);
        Matrix xT = Matrix.transpose(x);

        assertEquals(expected_mT, mT);
        assertEquals(expected_xT, xT);
        assertTrue(mT.equals(expected_mT));
        assertTrue(xT.equals(expected_xT));
    }

    @Test
    public void test_MatrixTrace() {
        Matrix m = new Matrix(new double[][] {
            { 5, 6, 7 },
            { 8, 9, 0 },
            { 4, 3, 2 }
        });
        Matrix i = Matrix.identity(5);  // 5x5 identity matrix
        double[][] a = {
            { -8, 12 },
            { -2, -6 }
        };

        // t = 5 + 9 + 2 = 16
        double m_expectedTrace = 16.0;
        double m_trace = m.trace();

        // t = 1 + 1 + 1 + 1 + 1 = 5
        double i_expectedTrace = 5.0;
        double i_trace = i.trace();

        // t = -8 + (-6) = -14
        double a_expectedTrace = -14.0;
        double a_trace = Matrix.trace(a);

        // Equality tests
        assertEquals(m_expectedTrace, m_trace, Matrix.THRESHOLD);
        assertEquals(i_expectedTrace, i_trace, Matrix.THRESHOLD);
        assertEquals(a_expectedTrace, a_trace, Matrix.THRESHOLD);
    }
}
