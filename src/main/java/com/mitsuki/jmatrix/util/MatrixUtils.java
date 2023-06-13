// :: --------------------- :: //
/* --   MATRIX UTILITIES    -- */
// :: --------------------- :: //

// Copyright (c) 2023 Ryuu Mitsuki
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.


// -**- Package -**- //
package com.mitsuki.jmatrix.util;

// -**- Local Package -**- //
import com.mitsuki.jmatrix.Matrix;

/**
* Utility interface for matrix operations and utilities.
*
* @author   <a href="https://github.com/mitsuki31" target="_blank">
*           Ryuu Mitsuki</a>
* @version  1.1, 7 June 2023
* @since    1.0.0
* @license  <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
*           Apache License 2.0</a>
* @see      com.mitsuki.jmatrix.Matrix
*/
public interface MatrixUtils {
    /**
    * Creates a deep copy of the given {@linkplain Matrix}.
    *
    * @param  m  the {@linkplain Matrix} object.
    *
    * @return    a new {@linkplain Matrix} object that is a deep copy of
    *            the input matrix, or {@code null} if the entries
    *            of input matrix is {@code null}.
    *
    * @since     1.0.0
    * @see       #deepCopyOf(double[][])
    * @see       Matrix#deepCopy()
    */
    public static Matrix deepCopyOf(Matrix m) {
        if (m.getEntries() == null) {
            return new Matrix();
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
    * Creates a deep copy of the given two-dimensional array.
    *
    * @param  a   the array to be copied.
    *
    * @return     a new two-dimensional array that is a deep copy of
    *             the input array, or {@code null} if the input array
    *             is {@code null} or empty.
    *
    * @since      1.0.0
    * @see        #deepCopyOf(Matrix)
    * @see        #deepCopyOf(double[][])
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
    * @param  a  the first {@linkplain Matrix} to compare.
    * @param  b  the second {@linkplain Matrix} to compare.
    *
    * @return    {@code true} if the matrices have equal sizes, {@code false} otherwise.
    *
    * @since     1.0.0
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
    * Checks whether the given two arrays have equal sizes.
    *
    * @param  a  the first two-dimensional array to compare.
    * @param  b  the second two-dimensional array to compare.
    *
    * @return    {@code true} if the arrays have equal sizes, {@code false} otherwise.
    *
    * @since     1.0.0
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
    * @param  a  the first {@linkplain Matrix} to compare.
    * @param  b  the second {@linkplain Matrix} to compare.
    *
    * @return    {@code true} if the matrices are equal, {@code false} otherwise.
    *
    * @since     1.0.0
    * @see       #isEquals(double[][], double[][])
    * @see       #isEqualsSize(Matrix, Matrix)
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
    * Checks whether the given two arrays are equal by comparing their entries.
    *
    * @param  a  the first two-dimensional array to compare.
    * @param  b  the second two-dimensional array to compare.
    *
    * @return    {@code true} if the arrays are equal, {@code false} otherwise.
    *
    * @since     1.0.0
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
     * @return {@code true} if the matrix has {@code null} entries, {@code false} otherwise.
     *
     * @since  1.0.0
     * @see    Matrix#getEntries()
     */
    public static boolean isNullEntries(Matrix m) {
        if (m == null || m.getEntries() == null) {
            return true;
        }

        return false;
    }
}
