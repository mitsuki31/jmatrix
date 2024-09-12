/*
 * Copyright (c) 2023-2024 Ryuu Mitsuki
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

import com.mitsuki.jmatrix.enums.JMErrorCode;
import com.mitsuki.jmatrix.exception.*;
import static com.mitsuki.jmatrix.exception.JMatrixBaseException.raise;

import com.mitsuki.jmatrix.Matrix;

/**
 * Utility class for validating 2D array-related parameters.
 *
 * <p>This class is a utility class that provides methods for validating and checking
 * properties of 2D arrays. It is a static class, meaning it does not require an instance
 * to be created to use its methods and provides static methods for checking matrix properties
 * such as nullity, squareness, diagonalness, and identity.
 *
 * @since  1.5.0
 */
public class DoubleArray2DChecker {
    /** Hide the constructor because it is a static class */
    protected DoubleArray2DChecker() {}

    /**
     * Validates that the given 2D array and its entries are not {@code null}.
     *
     * <p>This method checks if the provided 2D array is {@code null}
     * or if its entries are {@code null}. If either the matrix itself or its entries
     * are found to be {@code null}, this method throws a {@link NullMatrixException}
     * with a default message. This validation step is essential to ensure that matrix
     * operations can be safely performed without encountering {@code NullPointerException}s.
     *
     * <p>Usage of this method is crucial in scenarios where matrix integrity
     * must be maintained, and the presence of {@code null} entries could lead
     * to errors or undefined behavior in matrix computations.
     *
     * @param  m  The {@link Matrix} to be checked for {@code null} references
     *            and entries.
     *
     * @throws NullMatrixException  If the given matrix is {@code null} or its
     *                              entries are {@code null}.
     *
     * @since  1.5.0
     * @see    #requireNonNull(Matrix, String)
     * @see    MatrixUtils#isNullEntries(Matrix)
     */
    public static void requireNonNull(double[][] a) {
        requireNonNull(a, null);
    }

    /**
     * Validates that the given 2D array and its entries are not {@code null}.
     *
     * <p>This method checks if the provided 2D array is {@code null}
     * or if its entries are {@code null}. If either the matrix itself or its entries
     * are found to be {@code null}, this method throws a {@link NullMatrixException}
     * with a default message. This validation step is essential to ensure that matrix
     * operations can be safely performed without encountering {@code NullPointerException}s.
     *
     * <p>Usage of this method is crucial in scenarios where matrix integrity
     * must be maintained, and the presence of {@code null} entries could lead
     * to errors or undefined behavior in matrix computations.
     *
     * @param  a  The two-dimensional array to be checked for {@code null} references
     *            and entries.
     * @param  s  The descriptive message to display as error
     *
     * @throws NullMatrixException  If the given two-dimensional array is {@code null} or its
     *                              entries are {@code null}.
     *
     * @since  1.5.0
     * @see    #requireNonNull(double[][])
     */
    public static void requireNonNull(double[][] a, String s) {
        if (s == null) s = JMErrorCode.NULLMT.getMessage();  // Use default message if null are given
        if (a == null || (a != null && a.length == 0)) {
            raise(new NullMatrixException(s));
        }
    }

    /**
     * Validates that the given 2D array is a square matrix.
     *
     * <p>This method checks if the provided 2D array is a square matrix, in other words, a matrix
     * that has the same number of rows and columns. If the matrix is not a square matrix,
     * this method throws an {@link IllegalMatrixSizeException} with a default message.
     *
     * <p>Usage of this method is crucial in scenarios where matrix operations can be
     * safely performed only on square matrices.
     *
     * @param  a  The two-dimensional array to be checked for being a square matrix.
     *
     * @throws IllegalMatrixSizeException  If the given two-dimensional array is not a
     *                                     square matrix.
     *
     * @since  1.5.0
     * @see    #requireSquareMatrix(double[][], String)
     */
    public static void requireSquareMatrix(double[][] a) {
        requireSquareMatrix(a, null);
    }

    /**
     * Validates that the given 2D array is a square matrix.
     *
     * <p>This method checks if the provided 2D array is a square matrix, in other words, a matrix
     * that has the same number of rows and columns. If the matrix is not a square matrix,
     * this method throws an {@link IllegalMatrixSizeException} with the specified message.
     *
     * <p>Usage of this method is crucial in scenarios where matrix operations can be
     * safely performed only on square matrices.
     *
     * @param  a  The two-dimensional array to be checked for being a square matrix.
     * @param  s  The descriptive message to display as error message. If
     *            {@code null}, the default message is used.
     *
     * @throws IllegalMatrixSizeException  If the given two-dimensional array is not a
     *                                     square matrix.
     *
     * @since  1.5.0
     * @see    #requireSquareMatrix(double[][])
     */
    public static void requireSquareMatrix(double[][] a, String s) {
        if (s == null) s = JMErrorCode.INVTYP.getMessage();  // Use default message if null are given
        requireNonNull(a, null);
        if (a.length != a[0].length) {
            raise(new IllegalMatrixSizeException(s));
        }
    }

    /**
     * Validates that the given two 2D arrays have the same dimensions.
     *
     * <p>This method checks if the two given 2D arrays have the same dimensions, in other words,
     * if their row and column sizes are equal. If either the row or column sizes of
     * the two arrays are not equal, this method throws an {@link IllegalMatrixSizeException}
     * with the default message.
     *
     * <p>Usage of this method is crucial in scenarios where matrix operations can be
     * safely performed only on matrices with the same dimensions.
     *
     * @param  a  The first two-dimensional array to be checked for having the same
     *            dimensions as the second array.
     * @param  b  The second two-dimensional array to be checked for having the same
     *            dimensions as the first array.
     *
     * @throws IllegalMatrixSizeException  If the given two-dimensional arrays do not
     *                                     have the same dimensions.
     *
     * @since  1.5.0
     * @see    #requireSameDimensions(double[][], double[][], String)
     */
    public static void requireSameDimensions(double[][] a, double[][] b) {
        requireSameDimensions(a, b, null);
    }

    /**
     * Validates that the given two 2D arrays have the same dimensions.
     *
     * <p>This method checks if the two given 2D arrays have the same dimensions, in other words,
     * if their row and column sizes are equal. If either the row or column sizes of
     * the two arrays are not equal, this method throws an {@link IllegalMatrixSizeException}
     * with the specified message.
     *
     * <p>Usage of this method is crucial in scenarios where matrix operations can be
     * safely performed only on matrices with the same dimensions.
     *
     * @param  a  The first two-dimensional array to be checked for having the same
     *            dimensions as the second array.
     * @param  b  The second two-dimensional array to be checked for having the same
     *            dimensions as the first array.
     * @param  s  The descriptive message to display as error
     *
     * @throws IllegalMatrixSizeException  If the given two-dimensional arrays do not
     *                                     have the same dimensions.
     *
     * @since  1.5.0
     * @see    #requireSameDimensions(double[][], double[][])
     */
    public static void requireSameDimensions(double[][] a, double[][] b, String s) {
        if (s == null) s = "Matrices must have the same dimensions";
        if (a.length != b.length || a[0].length != b[0].length) {
            raise(new IllegalMatrixSizeException(s));
        }
    }

    /**
     * Validates that the given matrix is a diagonal matrix.
     *
     * <p>This method checks if the matrix is a diagonal matrix, in other words, all
     * non-zero entries are on the diagonal. If this condition is not met,
     * the method throws a {@link IllegalMatrixSizeException} with a default
     * message.
     *
     * <p>This validation is essential for operations that require diagonal
     * matrices, such as matrix exponentiation, matrix logarithm, and matrix
     * trace.
     *
     * @param  a  The matrix to be validated.
     *
     * @throws IllegalMatrixSizeException  If the given matrix is not a diagonal
     *                                     matrix.
     *
     * @since  1.5.0
     * @see    #requireDiagonalMatrix(double[][], String)
     * @see    Matrix#isDiagonal(double[][])
     */
    public static void requireDiagonalMatrix(double[][] a) {
        requireDiagonalMatrix(a, null);
    }

    /**
     * Validates that the given matrix is a diagonal matrix.
     *
     * <p>This method checks if the matrix is a diagonal matrix, in other words, all
     * non-zero entries are on the diagonal. If this condition is not met,
     * the method throws a {@link IllegalMatrixSizeException} with a default
     * message.
     *
     * <p>This validation is essential for operations that require diagonal
     * matrices, such as matrix exponentiation, matrix logarithm, and matrix
     * trace.
     *
     * @param  a  The matrix to be validated.
     * @param  s  The descriptive message to display as error
     *
     * @throws IllegalMatrixSizeException  If the given matrix is not a diagonal
     *                                     matrix.
     *
     * @since  1.5.0
     * @see    #requireDiagonalMatrix(double[][])
     * @see    Matrix#isDiagonal(double[][])
     */
    public static void requireDiagonalMatrix(double[][] a, String s) {
        requireSquareMatrix(a, null);
        if (s == null) s = "Matrix must be diagonal";
        if (!Matrix.isDiagonal(a)) {
            raise(new IllegalMatrixSizeException(s));
        }
    }

    /**
     * Validates that the given 2D array is an identity matrix.
     *
     * <p>This method checks if the 2D array is an identity matrix, in other words, a
     * square matrix with ones on the main diagonal and zeros elsewhere. If this
     * condition is not met, the method throws a {@link IllegalMatrixSizeException}
     * with a default message.
     *
     * <p>This validation is essential for operations that require identity
     * matrices.
     *
     * @param  a  The two-dimensional array to be validated.
     *
     * @throws IllegalMatrixSizeException  If the given two-dimensional array is not an identity
     *                                     matrix.
     *
     * @since  1.5.0
     * @see    #requireIdentityMatrix(double[][], String)
     * @see    Matrix#isIdentity(double[][])
     */
    public static void requireIdentityMatrix(double[][] a) {
        requireIdentityMatrix(a, null);
    }

    /**
     * Validates that the given 2D array is an identity matrix.
     *
     * <p>This method checks if the 2D array is an identity matrix, in other words, a
     * square matrix with ones on the main diagonal and zeros elsewhere. If this
     * condition is not met, the method throws a {@link IllegalMatrixSizeException}
     * with a specified message.
     *
     * <p>This validation is essential for operations that require identity
     * matrices.
     *
     * @param  a  The two-dimensional array to be validated.
     * @param  s  The descriptive message to display as error message. If
     *            {@code null}, the default error message is used.
     *
     * @throws IllegalMatrixSizeException  If the given two-dimensional array is not an identity
     *                                     matrix.
     *
     * @since  1.5.0
     * @see    #requireIdentityMatrix(double[][])
     * @see    Matrix#isIdentity(double[][])
     */
    public static void requireIdentityMatrix(double[][] a, String s) {
        requireSquareMatrix(a, null);
        if (s == null) s = JMErrorCode.INVTYP.getMessage();
        if (!Matrix.isIdentity(a)) {
            raise(new IllegalMatrixSizeException(s));
        }
    }
}
