package com.mitsuki.jmatrix.test;

import com.mitsuki.jmatrix.Matrix;
import com.mitsuki.jmatrix.util.MatrixUtils;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class Test_MatrixConstructor {
    @Test
    public void test_MatrixWithNullEntries() {
        Matrix x = new Matrix();
        Matrix y = new Matrix();

        assertEquals(true, MatrixUtils.isNullEntries(x));
        assertEquals(true, (x.getEntries() == null));
        assertEquals(true, (0 == x.getSize()[0] && 0 == x.getSize()[1]));
        assertEquals(true, x.equals(y));

        y.create(new double[][] { {5, 5}, {4, 4} });  // initialize new entries
        assertEquals(false, x.equals(y));
    }

    @Test
    public void test_ZeroMatrix() {
        Matrix m = new Matrix(3, 1);
        Matrix n = new Matrix(4, 5);

        assertEquals(true,
            MatrixUtils.isEquals(new double[][] {
                {0},
                {0},
                {0}
            }, m.getEntries())
        );

        assertEquals(false, MatrixUtils.isNullEntries(m));
        assertEquals(true, (3 == m.getSize()[0] && 1 == m.getSize()[1]));
        assertEquals(false, m.isSquare());
        assertEquals(true, m.equals(m));
        assertEquals(false, m.equals(n));

        n = m.deepCopy();  // deep copy matrix "m"
        assertEquals(true, m.equals(n));
        assertEquals(false, (m == n));  // check for memory references
    }

    @Test
    public void test_IdentityMatrix() {
        Matrix mI = Matrix.identity(6); // 6x6 identity matrix
        Matrix nI = Matrix.identity(8); // 8x8 identity matrix

        assertEquals(false, mI.equals(nI));
        assertEquals(false, MatrixUtils.isNullEntries(mI));
        assertEquals(true, mI.isDiagonal());
        assertEquals(true, mI.isSquare());
    }

    @Test
    public void test_SquareMatrix() {
        // 5x5 square matrix with each entries equal to 5
        Matrix m = new Matrix(5, 5, 5);
        // 2x2 square matrix with each entries equal to 8
        Matrix n = new Matrix(2, 2, 8);

        assertEquals(false, MatrixUtils.isNullEntries(m));
        assertEquals(false, MatrixUtils.isNullEntries(n));
        assertEquals(false, m.equals(n));
        assertEquals(true, m.isSquare());
        assertEquals(false, m.isDiagonal());
        assertEquals(true, (m.getSize()[0] == 5 && m.getSize()[1] == 5));

        n = m.deepCopy();  // deep copy matrix "m"
        assertEquals(true, m.equals(n));
        assertEquals(false, (m == n));  // check for memory references
    }
}
