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

import com.mitsuki.jmatrix.Matrix;
import com.mitsuki.jmatrix.enums.JMErrorCode;
import com.mitsuki.jmatrix.exception.*;
import static com.mitsuki.jmatrix.exception.JMatrixBaseException.raise;

/**
 * Utility class for validating matrix-related parameters.
 *
 * <p>This class is a utility class that provides various methods for validating and checking
 * the properties of matrices. It extends the {@link DoubleArray2DChecker} class and provides
 * static methods for checking matrix properties such as nullity, squareness, diagonalness, and identity.
 *
 * @since    1.5.0
 * @version  1.0, 12 September 2024
 * @author   <a href="https://github.com/mitsuki31" target="_blank">
 *           Ryuu Mitsuki</a>
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *           Apache License 2.0</a>
 */
public class MatrixChecker extends DoubleArray2DChecker {
    /** Hide the constructor because it is a static class */
    protected MatrixChecker() {}

    /**
     * Validates that the given matrix and its entries are not {@code null}.
     *
     * <p>This method checks if the provided {@link Matrix} instance is {@code null}
     * or if the matrix's entries are {@code null}. It uses the {@link
     * MatrixUtils#isNullEntries(Matrix)} method to perform these checks. If
     * either the matrix itself or its entries are found to be {@code null},
     * this method throws a {@link NullMatrixException} with a default message.
     * This validation step is essential to ensure that matrix operations can be
     * safely performed without encountering {@code NullPointerException}s.
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
    public static void requireNonNull(Matrix m) {
        requireNonNull(m, null);
    }

    /**
     * Validates that the given matrix and its entries are not {@code null}.
     *
     * <p>This method checks if the provided {@link Matrix} instance is {@code null}
     * or if the matrix's entries are {@code null}. It uses the {@link
     * MatrixUtils#isNullEntries(Matrix)} method to perform these checks. If
     * either the matrix itself or its entries are found to be {@code null},
     * this method throws a {@link NullMatrixException} with a specified message.
     * This validation step is essential to ensure that matrix operations can be
     * safely performed without encountering {@code NullPointerException}s.
     *
     * <p>Usage of this method is crucial in scenarios where matrix integrity
     * must be maintained, and the presence of {@code null} entries could lead
     * to errors or undefined behavior in matrix computations.
     *
     * @param  m  The {@link Matrix} to be checked for {@code null} references
     *            and entries.
     * @param  s  The descriptive message to display as error message. If
     *            {@code null}, the default message is used.
     *
     * @throws NullMatrixException  If the given matrix is {@code null} or its
     *                              entries are {@code null}.
     *
     * @since  1.5.0
     * @see    #requireNonNull(Matrix)
     * @see    MatrixUtils#isNullEntries(Matrix)
     */
    public static void requireNonNull(Matrix m, String s) {
        if (s == null) s = JMErrorCode.NULLMT.getMessage();
        if (MatrixUtils.isNullEntries(m)) {
            raise(new NullMatrixException(s));
        }
    }

    /**
     * Validates that the given matrix is a square matrix.
     *
     * <p>This method performs two checks on the provided {@link Matrix} instance:
     * <ol>
     * <li> First, it ensures that the matrix and its entries are not {@code null}.
     *      This check is done using the {@link #requireNonNull(Matrix)} method,
     *      which throws a {@link NullMatrixException} if the matrix itself or its
     *      entries are {@code null}. The exception message is defined by the
     *      {@link JMErrorCode#NULLMT} enum.
     * <li> Second, it verifies that the matrix is a square matrix. A matrix is
     *      considered square if it has the same number of rows and columns.
     *      If this condition is not met, the method throws an {@link
     *      IllegalMatrixSizeException} with a message specified by the {@link
     *      JMErrorCode#INVTYP} enum.
     * </ol>
     *
     * <p>This validation is essential for operations that require square
     * matrices, such as certain mathematical computations and transformations.
     * Ensuring the matrix is both non-null and square helps prevent runtime
     * errors and ensures the integrity of matrix operations.
     *
     * @param  m  The {@link Matrix} to be validated.
     *
     * @throws NullMatrixException         If the given matrix is {@code null}
     *                                     or contains {@code null} entries.
     * @throws IllegalMatrixSizeException  If the given matrix does not have
     *                                     equal number of rows and columns
     *                                     (in other words, is not square).
     *
     * @since  1.5.0
     * @see    #requireNonNull(Matrix)
     * @see    #requireSquareMatrix(Matrix, String)
     * @see    Matrix#isSquare()
     */
    public static void requireSquareMatrix(Matrix m) {
        requireSquareMatrix(m, null);
    }

    /**
     * Validates that the given matrix is a square matrix.
     *
     * <p>This method performs two checks on the provided {@link Matrix} instance:
     * <ol>
     * <li> First, it ensures that the matrix and its entries are not {@code null}.
     *      This check is done using the {@link #requireNonNull(Matrix)} method,
     *      which throws a {@link NullMatrixException} if the matrix itself or its
     *      entries are {@code null}. The exception message is defined by the
     *      {@link JMErrorCode#NULLMT} enum.
     * <li> Second, it verifies that the matrix is a square matrix. A matrix is
     *      considered square if it has the same number of rows and columns.
     *      If this condition is not met, the method throws an {@link
     *      IllegalMatrixSizeException} with a specified message.
     * </ol>
     *
     * <p>This validation is essential for operations that require square
     * matrices, such as certain mathematical computations and transformations.
     * Ensuring the matrix is both non-null and square helps prevent runtime
     * errors and ensures the integrity of matrix operations.
     *
     * @param  m  The {@link Matrix} to be validated.
     * @param  s  The descriptive message to display as error message. If
     *            {@code null}, the default message is used.
     *
     * @throws NullMatrixException         If the given matrix is {@code null}
     *                                     or contains {@code null} entries.
     * @throws IllegalMatrixSizeException  If the given matrix does not have
     *                                     equal number of rows and columns
     *                                     (in other words, is not square).
     *
     * @since  1.5.0
     * @see    #requireNonNull(Matrix)
     * @see    #requireSquareMatrix(Matrix, String)
     * @see    Matrix#isSquare()
     */
    public static void requireSquareMatrix(Matrix m, String s) {
        requireNonNull(m, null);
        if (s == null) s = JMErrorCode.INVTYP.getMessage();
        if (!m.isSquare()) {
            raise(new IllegalMatrixSizeException(s));
        }
    }

    /**
     * Validates that the given two matrices have the same dimensions.
     *
     * <p>This method checks if the two matrices have the same number of rows and
     * columns. If this condition is not met, the method throws an {@link
     * IllegalMatrixSizeException} with a default message.
     *
     * <p>This validation is essential for operations that require matrices with
     * equal dimensions, such as matrix addition, subtraction, multiplication,
     * and element-wise operations.
     *
     * @param  m  The first {@link Matrix} to be validated.
     * @param  n  The second {@link Matrix} to be validated.
     *
     * @throws IllegalMatrixSizeException  If the given matrices do not have
     *                                     equal number of rows and columns.
     *
     * @since  1.5.0
     * @see    #requireSameDimensions(Matrix, Matrix, String)
     * @see    Matrix#getNumRows()
     * @see    Matrix#getNumCols()
     */
    public static void requireSameDimensions(Matrix m, Matrix n) {
        requireSameDimensions(m, n, null);
    }

    /**
     * Validates that the given two matrices have the same dimensions.
     *
     * <p>This method checks if the two matrices have the same number of rows and
     * columns. If this condition is not met, the method throws an {@link
     * IllegalMatrixSizeException} with the specified message.
     *
     * <p>This validation is essential for operations that require matrices with
     * equal dimensions, such as matrix addition, subtraction, multiplication,
     * and element-wise operations.
     *
     * @param  m  The first {@link Matrix} to be validated.
     * @param  n  The second {@link Matrix} to be validated.
     * @param  s  The message to be used if the matrices have different dimensions.
     *            If {@code null}, a default message is used.
     *
     * @throws IllegalMatrixSizeException  If the given matrices do not have
     *                                     equal number of rows and columns.
     *
     * @since  1.5.0
     * @see    #requireSameDimensions(Matrix, Matrix)
     * @see    Matrix#getNumRows()
     * @see    Matrix#getNumCols()
     */
    public static void requireSameDimensions(Matrix m, Matrix n, String s) {
        if (s == null) s = "Matrices must have the same dimensions";
        if (m.getNumRows() != n.getNumRows() || m.getNumCols() != n.getNumCols()) {
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
     * @param  m  The matrix to be validated.
     *
     * @throws IllegalMatrixSizeException  If the given matrix is not a diagonal
     *                                     matrix.
     *
     * @since  1.5.0
     * @see    #requireDiagonalMatrix(Matrix, String)
     * @see    Matrix#isDiagonal()
     */
    public static void requireDiagonalMatrix(Matrix m) {
        requireDiagonalMatrix(m, null);
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
     * @param  m  The matrix to be validated.
     * @param  s  The error message to be used if the matrix is not diagonal.
     *            If {@code null}, the default error message is used.
     *
     * @throws IllegalMatrixSizeException  If the given matrix is not a diagonal
     *                                     matrix.
     *
     * @since  1.5.0
     * @see    #requireDiagonalMatrix(Matrix)
     * @see    Matrix#isDiagonal()
     */
    public static void requireDiagonalMatrix(Matrix m, String s) {
        requireSquareMatrix(m, null);
        if (s == null) s = "Matrix must be diagonal";
        if (!m.isDiagonal()) {
            raise(new IllegalMatrixSizeException(s));
        }
    }

    /**
     * Validates that the given matrix is an identity matrix.
     *
     * <p>This method checks if the matrix is an identity matrix, in other words, a square
     * matrix with ones on the main diagonal and zeros elsewhere. If this
     * condition is not met, the method throws a {@link IllegalMatrixSizeException}
     * with a default message.
     *
     * <p>This validation is essential for operations that require identity
     * matrices, such as matrix exponentiation, matrix logarithm, and matrix
     * trace.
     *
     * @param  m  The matrix to be validated.
     *
     * @throws IllegalMatrixSizeException  If the given matrix is not an identity
     *                                     matrix.
     *
     * @since  1.5.0
     * @see    #requireIdentityMatrix(Matrix, String)
     * @see    Matrix#isIdentity()
     */
    public static void requireIdentityMatrix(Matrix m) {
        requireIdentityMatrix(m, null);
    }

    /**
     * Validates that the given matrix is an identity matrix.
     *
     * <p>This method checks if the matrix is an identity matrix, in other words, a square
     * matrix with ones on the main diagonal and zeros elsewhere. If this
     * condition is not met, the method throws a {@link IllegalMatrixSizeException}
     * with a specified message.
     *
     * <p>This validation is essential for operations that require identity
     * matrices, such as matrix exponentiation, matrix logarithm, and matrix
     * trace.
     *
     * @param  m  The matrix to be validated.
     * @param  s  The error message to be used if the matrix is not an identity
     *            matrix. If {@code null}, the default error message is used.
     *
     * @throws IllegalMatrixSizeException  If the given matrix is not an identity
     *                                     matrix.
     *
     * @since  1.5.0
     * @see    #requireIdentityMatrix(Matrix)
     * @see    Matrix#isIdentity()
     */
    public static void requireIdentityMatrix(Matrix m, String s) {
        requireSquareMatrix(m, null);
        if (s == null) s = JMErrorCode.INVTYP.getMessage();
        if (!m.isIdentity()) {
            raise(new IllegalMatrixSizeException(s));
        }
    }
}
