// :: --------------------- :: //
/* --   MATRIX UTILITIES    -- */
// :: --------------------- :: //

/* Copyright (c) 2023 Ryuu Mitsuki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mitsuki.jmatrix.core;

import com.mitsuki.jmatrix.Matrix;

/**
 * Utility interface for matrix operations and other utilities. All methods in this interface
 * has their own default implementation.
 *
 * @author   <a href="https://github.com/mitsuki31" target="_blank">
 *           Ryuu Mitsuki</a>
 * @version  1.2, 26 June 2023
 * @since    1.0.0b.7
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *           Apache License 2.0</a>
 * @see      com.mitsuki.jmatrix.Matrix
 */
public interface MatrixUtils {

    /**
     * Creates and returns a deep copy of the given {@linkplain Matrix}.
     *
     * <p>If the given matrix has {@code null} entries (unitialized),
     * then it would returns new matrix with {@code null} entries instead.
     *
     * @param  m  the {@linkplain Matrix} object.
     *
     * @return    a new {@linkplain Matrix} object that is a deep copy of
     *            the input matrix.
     *
     * @since     1.0.0b.7
     * @see       #deepCopyOf(double[][])
     * @see       Matrix#deepCopy()
     */
    public static Matrix deepCopyOf(Matrix m) {
        if (m.getEntries() == null) {
            return new Matrix();  // return matrix with null entries
        }

        // Retrieve the sizes
        int rows = m.getSize()[0];
        int cols = m.getSize()[1];

        double[ ][ ] copiedEntries = new double[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                copiedEntries[r][c] = m.get(r, c);
            }
        }

        return new Matrix(copiedEntries);
    }


    /**
     * Creates and returns a deep copy of the given two-dimensional array.
     *
     * <p>If the given array {@code null} or empty, then it would returns {@code null} instead.
     *
     * @param  a   the array to be copied.
     *
     * @return     a new two-dimensional array that is a deep copy of
     *             the input array.
     *
     * @since      1.0.0b.7
     * @see        #deepCopyOf(Matrix)
     */
    public static double[ ][ ] deepCopyOf(double[ ][ ] a) {
        if (a == null || a.length == 0) {
            return null;
        }

        // Retrieve the sizes
        int rows = a.length;
        int cols = a[0].length;

        double[ ][ ] copiedArray = new double[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                copiedArray[r][c] = a[r][c];
            }
        }

        return copiedArray;
    }



    /**
     * Checks whether the given two matrices have equal sizes.
     *
     * <p>Returns {@code false} if any of the following statements is {@code true}:
     *
     * <ul>
     *   <li> One or both matrices are {@code null} object or have {@code null} entries.
     *   <li> Both matrices have different sizes, either in terms of row size or column size.
     * </ul>
     *
     * @param  a  the first {@linkplain Matrix} to compare.
     * @param  b  the second {@linkplain Matrix} to compare.
     *
     * @return    {@code true} if both matrices have equal sizes, {@code false} otherwise.
     *
     * @since     1.0.0b.7
     * @see       #isEqualsSize(double[][], double[][])
     * @see       #isEquals(Matrix, Matrix)
     */
    public static boolean isEqualsSize(Matrix a, Matrix b) {
        if ( (a == null || a.getEntries() == null) ||
                (b == null || b.getEntries() == null) ) {
            return false;
        }

        return (a.getSize()[0] == b.getSize()[0] &&
                a.getSize()[1] == b.getSize()[1]);
    }


    /**
     * Checks whether the given two-dimensional arrays have equal sizes.
     *
     * <p>Returns {@code false} if any of the following statements is {@code true}:
     *
     * <ul>
     *   <li> One or both arrays are {@code null} object or empty.
     *   <li> Both arrays have different sizes, either in terms of row size or column size.
     * </ul>
     *
     * @param  a  the first two-dimensional array to compare.
     * @param  b  the second two-dimensional array to compare.
     *
     * @return    {@code true} if the arrays have equal sizes, {@code false} otherwise.
     *
     * @since     1.0.0b.7
     * @see       #isEqualsSize(Matrix, Matrix)
     * @see       #isEquals(double[][], double[][])
     */
    public static boolean isEqualsSize(double[ ][ ] a, double[ ][ ] b) {
        if ( (a == null || a.length == 0) ||
                (b == null || b.length == 0) ) {
            return false;
        }

        return (a.length == b.length && a[0].length == b[0].length);
    }



    /**
     * Checks whether the given two matrices are equal by comparing their entries.
     *
     * <p>Returns {@code false} if any of the following statements is {@code true}:
     *
     * <ul>
     *   <li> One or both matrices are {@code null} object or have {@code null} entries.
     *   <li> Both matrices have different dimensions (number of rows and columns).
     *   <li> There exists an entry in one matrix that is not equal to the entry
     *        in the corresponding position in the other matrix.
     * </ul>
     *
     * @param  a  the first {@linkplain Matrix} to compare.
     * @param  b  the second {@linkplain Matrix} to compare.
     *
     * @return    {@code true} if the matrices are equal, {@code false} otherwise.
     *
     * @since     1.0.0b.7
     * @see       #isEquals(double[][], double[][])
     * @see       #isEqualsSize(Matrix, Matrix)
     * @see       Matrix#equals(Object)
     */
    public static boolean isEquals(Matrix a, Matrix b) {
        if ( (a == null || a.getEntries() == null) ||
                (b == null || b.getEntries() == null) ||
                !(isEqualsSize(a, b)) ) {
            return false;
        }

        int rows = a.getSize()[0];
        int cols = b.getSize()[1];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (a.get(r, c) != b.get(r, c)) return false;
            }
        }

        return true;
    }


    /**
     * Checks whether the given two-dimensional arrays are equal by comparing their entries.
     *
     * <p>Returns {@code false} if any of the following statements is {@code true}:
     *
     * <ul>
     *   <li> One or both arrays are {@code null} object or empty.
     *   <li> Both arrays have different dimensions (number of rows and columns).
     *   <li> There exists an entry in one array that is not equal to the entry
     *        in the corresponding position in the other array.
     * </ul>
     *
     * @param  a  the first two-dimensional array to compare.
     * @param  b  the second two-dimensional array to compare.
     *
     * @return    {@code true} if the arrays are equal, {@code false} otherwise.
     *
     * @since     1.0.0b.7
     * @see       #isEquals(Matrix, Matrix)
     * @see       #isEqualsSize(double[][], double[][])
     */
    public static boolean isEquals(double[ ][ ] a, double[ ][ ] b) {
        if ( (a == null || a.length == 0) ||
                (b == null || b.length == 0) ||
                !(isEqualsSize(a, b)) ) {
            return false;
        }

        int rows = a.length;
        int cols = b[0].length;

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (a[r][c] != b[r][c]) return false;
            }
        }

        return true;
    }

    /**
     * Checks whether the given {@linkplain Matrix} has {@code null} entries.
     *
     * @param  m  the {@linkplain Matrix} object.
     *
     * @return    {@code true} if the matrix has {@code null} entries, {@code false} otherwise.
     *
     * @since     1.0.0b.7
     * @see       Matrix#getEntries()
     */
    public static boolean isNullEntries(Matrix m) {
        if (m == null || m.getEntries() == null) {
            return true;
        }

        return false;
    }
}
