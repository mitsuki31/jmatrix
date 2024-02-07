package com.mitsuki.jmatrix.test;

import com.mitsuki.jmatrix.Matrix;
import com.mitsuki.jmatrix.core.MatrixUtils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

public class Test_MatrixConstructor {
    @Test
    public void test_MatrixWithNullEntries() {
        Matrix x = new Matrix();
        Matrix y = new Matrix();

        // Test and check for null entries
        assertTrue(MatrixUtils.isNullEntries(x));
        assertTrue(MatrixUtils.isNullEntries(y));
        assertNull(x.getEntries());
        assertNull(y.getEntries());

        // Test and check the dimensions
        assertEquals(null, x.getSize());
        assertEquals(null, y.getSize());

        assertFalse(MatrixUtils.isEqualsSize(x, y));

        // Test and check for equality of elements
        assertTrue(x.equals(y));

        y.create(new double[][] { {5, 5}, {4, 4} });  // initialize new entries
        assertFalse(x.equals(y));
    }

    @Test
    public void test_ZeroMatrix() {
        Matrix m = new Matrix(3, 1);
        Matrix n = new Matrix(4, 5);

        double[][] a = {
            { 0 },
            { 0 },
            { 0 }
        };

        double[][] b = {
            { 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0 },
            { 0, 0, 0, 0, 0 },
        };

        // Test and check for null entries
        assertFalse(MatrixUtils.isNullEntries(m));
        assertFalse(MatrixUtils.isNullEntries(n));
        assertNotNull(m.getEntries());
        assertNotNull(n.getEntries());

        // Test and check the dimensions
        assertEquals(3, m.getSize()[0]);
        assertEquals(1, m.getSize()[1]);
        assertEquals(4, n.getSize()[0]);
        assertEquals(5, n.getSize()[1]);

        assertFalse(MatrixUtils.isEqualsSize(m, n));
        assertTrue(MatrixUtils.isEqualsSize(m.getEntries(), a));
        assertTrue(MatrixUtils.isEqualsSize(n.getEntries(), b));

        // Test and check the matrix type
        assertFalse(m.isSquare());
        assertFalse(n.isSquare());

        // Test and check for equality of elements
        assertFalse(m.equals(n));
        assertTrue(MatrixUtils.isEquals(m.getEntries(), a));
        assertTrue(MatrixUtils.isEquals(n.getEntries(), b));

        n = m.deepCopy();  // deep copy matrix "m"
        assertTrue(m.equals(n));
        assertTrue(MatrixUtils.isEqualsSize(n, m));
        assertFalse((m == n));  // check for memory references

        n.create(new double[][] { {5}, {5}, {5}, {5} });
        assertFalse(n.equals(m));
        assertFalse(MatrixUtils.isEqualsSize(n, m));
    }

    @Test
    public void test_IdentityMatrix() {
        Matrix mI = Matrix.identity(6); // 6x6 identity matrix
        Matrix nI = Matrix.identity(8); // 8x8 identity matrix

        Matrix x = new Matrix(6, 6);
        Matrix y = new Matrix(8, 8);

        // Test and check for null entries
        assertFalse(MatrixUtils.isNullEntries(mI));
        assertFalse(MatrixUtils.isNullEntries(nI));
        assertNotNull(mI.getEntries());
        assertNotNull(nI.getEntries());

        // Test and check the matrix type
        assertTrue(mI.isSquare());
        assertTrue(mI.isDiagonal());

        assertTrue(nI.isSquare());
        assertTrue(nI.isDiagonal());

        // Test and check the dimensions
        assertTrue(MatrixUtils.isEqualsSize(mI, x));
        assertTrue(MatrixUtils.isEqualsSize(nI, y));
        assertFalse(MatrixUtils.isEqualsSize(mI, nI));

        // Test and check for equality of elements
        assertFalse(mI.equals(nI));
        assertFalse(mI.equals(x));
        assertFalse(nI.equals(y));

        mI.clear();  // clear elements and convert it into zero matrix
        assertTrue(mI.equals(x));
    }

    @Test
    public void test_SquareMatrix() {
        // 5x5 square matrix with each entries equal to 5
        Matrix m = new Matrix(5, 5, 5);
        // 2x2 square matrix with each entries equal to 8
        Matrix n = new Matrix(2, 2, 8);

        // Test and check for null entries
        assertFalse(MatrixUtils.isNullEntries(m));
        assertFalse(MatrixUtils.isNullEntries(n));
        assertNotNull(m.getEntries());
        assertNotNull(n.getEntries());

        // Test and check each size
        assertEquals(5, m.getSize()[0]);
        assertEquals(5, m.getSize()[1]);
        assertEquals(2, n.getSize()[0]);
        assertEquals(2, n.getSize()[1]);

        assertFalse(MatrixUtils.isEqualsSize(m, n));
        assertTrue(MatrixUtils.isEqualsSize(m, new Matrix(5, 5)));
        assertTrue(MatrixUtils.isEqualsSize(n, new Matrix(2, 2)));

        // Test and check the matrix type
        assertTrue(m.isSquare());
        assertFalse(m.isDiagonal());

        // Test and check for equality
        assertFalse(m.equals(n));

        n = m.deepCopy();  // deep copy matrix "m"
        assertTrue(m.equals(n));
        assertFalse((m == n));  // check for memory references
    }

    @Test
    public void test_ConstructArrayOfMatrices() {
        // Create the array of matrices
        Matrix[] m4 = new Matrix[] {
            Matrix.identity(3),          // 3x3 identity matrix
            new Matrix(5, 5, 5),         // 5x5 square matrix with elements equal to 5
            new Matrix(8, 8),            // 8x8 zero matrix
            new Matrix(new double[][] {  // 3x3 square matrix, created with 2d array
                { 0, 1, 2 },
                { 3, 4, 5 },
                { 6, 7, 8 }
            })
        };

        for (byte i = 0; i < m4.length; i++) {
            assertTrue((m4[i] instanceof Matrix));
            assertFalse(MatrixUtils.isNullEntries(m4[i]));
            assertFalse(m4[i].equals(new Matrix()));
        }
    }

    @Test
    public void test_SparseMatrix() {
        Matrix m = new Matrix(new double[][] {
            { 6, 8, 4, 5 },
            { 11, 6, 0, 0 },
            { 0, 5, 21, 10 }
        });
        Matrix i = Matrix.identity(3);  // 3x3 identity matrix

        assertFalse(MatrixUtils.isEqualsSize(m, i));
        assertFalse(m.equals(i));

        assertFalse(m.isSparse());
        assertTrue(i.isSparse());
    }
}
