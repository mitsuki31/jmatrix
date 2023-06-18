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
    }

    @Test
    public void test_NullMatrix() {
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
}
