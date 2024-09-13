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
import com.mitsuki.jmatrix.exception.NullMatrixException;
import com.mitsuki.jmatrix.exception.IllegalMatrixSizeException;

/**
 * This interface provides methods for calculating the determinant of a matrix
 * using various algorithms. It ensures that the matrix satisfies the necessary
 * requirements, such as being non-null and a square matrix, before proceeding
 * with the determinant calculation.
 * 
 * <p>Two main static methods are provided for determinant calculation:</p>
 * <ul>
 *   <li> {@link #determinant_CofactorExpansion(Matrix)}:
 *            Calculates the determinant using <b>Laplace expansion</b>,
 *            suitable for small matrices (e.g., {@code 3x3} or {@code 4x4}).
 *   <li> {@link #determinant_GaussElimination(Matrix)}:
 *            Calculates the determinant using <b>Gaussian elimination</b> for
 *            larger matrices, providing a more efficient approach.
 * </ul>
 *
 * @since    1.5.0
 * @version  1.0, 13 September 2024
 * @author   <a href="https://github.com/mitsuki31" target="_blank">
 *           Ryuu Mitsuki</a>
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *           Apache License 2.0</a>
 * @see      Matrix
 */
public strictfp interface MatrixDeterminant {

    /**
     * Validates that the matrix meets the requirements for determinant calculation.
     * 
     * <p>The matrix must be non-null and square. If the matrix does not meet these 
     * requirements, appropriate exceptions will be thrown.
     * 
     * @param  m  The {@link Matrix} to validate.
     *
     * @throws NullMatrixException         If the matrix is {@code null} or contains {@code null} references.
     * @throws IllegalMatrixSizeException  If the matrix is not a square matrix.
     *
     * @since  1.5.0
     * @see    #checkRequirements(double[][])
     */
    static void checkRequirements(Matrix m) {
        // Check for null matrix and throw NullMatrixException
        // if the matrix have null references
        MatrixChecker.requireNonNull(m, (new StringBuilder())
            .append("Matrix is null. Cannot calculate the determinant")
            .toString()
        );
        // Check for square matrix and throw IllegalMatrixSizeException
        // if the matrix is not a square matrix
        MatrixChecker.requireSquareMatrix(m, (new StringBuilder())
            .append("Matrix is not square. Determinant calculation is only ")
            .append("support on square matrices")
            .toString()
        );
    }

    /**
     * Validates that the 2D array representation of the matrix meets the requirements 
     * for determinant calculation.
     * 
     * <p>The array must be non-null and represent a square matrix. If these conditions
     * are not met, appropriate exceptions will be thrown.
     * 
     * @param  a  The two-dimensional array representation of the matrix to validate.
     *
     * @throws NullMatrixException         If the matrix is {@code null} or contains {@code null} references.
     * @throws IllegalMatrixSizeException  If the matrix is not a square matrix.
     *
     * @since  1.5.0
     * @see    #checkRequirements(Matrix)
     */
    static void checkRequirements(double[][] a) {
        // Check for null matrix and throw NullMatrixException
        // if the matrix have null references
        MatrixChecker.requireNonNull(a, (new StringBuilder())
            .append("Matrix is null. Cannot calculate the determinant")
            .toString()
        );
        // Check for square matrix and throw IllegalMatrixSizeException
        // if the matrix is not a square matrix
        MatrixChecker.requireSquareMatrix(a, (new StringBuilder())
            .append("Matrix is not square. Determinant calculation is only ")
            .append("support on square matrices")
            .toString()
        );
    }

    /**
     * Calculates the determinant of the matrix using cofactor expansion.
     * 
     * <p>Cofactor expansion, also known as <i>Laplace expansion</i>, is a recursive method
     * for calculating the determinant. It is best suited for small matrices due to 
     * its computational complexity, which grows exponentially with matrix size.
     * 
     * <p>For an {@code n x n} matrix, the method expands along the first row of the matrix,
     * recursively computing the determinant of its minors. For each element in the row,
     * the minor matrix is calculated by removing the row and column of the element,
     * and the determinant of this minor is computed. The final result is the sum of
     * the cofactor multiplications.
     * 
     * <p>This method is inefficient for large matrices due to the recursive nature,
     * with a time complexity of {@code O(n!)}. It is typically used for small matrices
     * like {@code 2x2}, {@code 3x3}, or {@code 4x4} matrices.
     * 
     * @param  m  The {@link Matrix} for which to calculate the determinant.
     * @return    The determinant of the matrix.
     *
     * @throws NullMatrixException         If the matrix is {@code null} or contains {@code null} references.
     * @throws IllegalMatrixSizeException  If the matrix is not a square matrix.
     * 
     * @implNote
     * This method recursively breaks the matrix down into smaller matrices, 
     * making it inefficient for large matrices. For larger matrices, consider using
     * {@link #determinant_GaussElimination(Matrix)} method.
     *
     * @since  1.5.0
     * @see    #determinant_GaussElimination(Matrix)
     */
    static double determinant_CofactorExpansion(Matrix m) {
        checkRequirements(m);

        double det = 0.0;        // To hold the determinant result
        int n = m.getNumRows();  // Get the matrix rows

        // Base case: 1x1 (1-by-1) matrix
        if (n == 1) return m.get(0, 0);  // Return the first element
        for (int i = 0; i < n; i++) {
            // Calculate minor matrix for each element in the first row
            Matrix minor = m.minorMatrix(0, i);
            // Recursively calculate the determinant of the minor
            double cofactor =
                Math.pow(-1, i) * determinant_CofactorExpansion(minor);
            // Add to the result with element value multiplied by cofactor
            det += m.get(0, i) * cofactor;
        }
        return det;
    }

    /**
     * Calculates the determinant of the given matrix using cofactor expansion.
     *
     * <p>This method is an overloaded version of {@link #determinant_CofactorExpansion(Matrix)}
     * and creates a new matrix from the given 2D array first.
     *
     * <p>Cofactor expansion, also known as <i>Laplace expansion</i>, is a recursive method
     * for calculating the determinant. It is best suited for small matrices due to 
     * its computational complexity, which grows exponentially with matrix size.
     * 
     * <p>For an {@code n x n} matrix, the method expands along the first row of the matrix,
     * recursively computing the determinant of its minors. For each element in the row,
     * the minor matrix is calculated by removing the row and column of the element,
     * and the determinant of this minor is computed. The final result is the sum of
     * the cofactor multiplications.
     * 
     * <p>This method is inefficient for large matrices due to the recursive nature,
     * with a time complexity of {@code O(n!)}. It is typically used for small matrices
     * like {@code 2x2}, {@code 3x3}, or {@code 4x4} matrices.
     * 
     * @param  a  The two-dimensional array representation of the matrix to calculate the determinant for.
     * @return    The determinant of the matrix.
     *
     * @throws NullMatrixException         If the matrix is {@code null} or contains {@code null} references.
     * @throws IllegalMatrixSizeException  If the matrix is not a square matrix.
     *
     * @implNote
     * This method recursively breaks the matrix down into smaller matrices, 
     * making it inefficient for large matrices. For larger matrices, consider using
     * {@link #determinant_GaussElimination(double[][])} method.
     * 
     * @since  1.5.0
     * @see    #determinant_CofactorExpansion(Matrix)
     */
    static double determinant_CofactorExpansion(double[][] a) {
        return determinant_CofactorExpansion(new Matrix(a));
    }

    /**
     * Calculates the determinant of the matrix using Gaussian elimination.
     * 
     * <p>Gaussian elimination is a more efficient method for calculating the determinant
     * of larger matrices. This method reduces the matrix to an upper triangular form by
     * performing row operations. Once the matrix is in upper triangular form, the 
     * determinant is the product of the diagonal elements.
     * 
     * <p>During the process, the algorithm swaps rows to ensure that pivot elements
     * are non-zero. Each row swap inverts the sign of the determinant, and this is
     * taken into account in the final calculation.
     * 
     * <p>The time complexity of this method is {@code O(n^3)}, making it significantly faster than
     * cofactor expansion for larger matrices. This method is particularly useful for matrices
     * that are larger than {@code 4x4}, where cofactor expansion becomes too slow.
     * 
     * <p>Gaussian elimination is not suitable for matrices that are close to singular (i.e., matrices
     * that have a very small determinant), as it may result in numerical instability due to rounding errors.
     * However, for most practical purposes, this method provides an efficient and accurate determinant
     * calculation.
     * 
     * @param  m  The {@link Matrix} for which to calculate the determinant.
     * @return    The determinant of the matrix, or 0 if the matrix is singular.
     *
     * @throws NullMatrixException         If the matrix is {@code null} or contains {@code null} references.
     * @throws IllegalMatrixSizeException  If the matrix is not a square matrix.
     * 
     * @implNote
     * This method is preferred for larger matrices due to its lower time complexity. It is more suitable
     * and efficient than the cofactor expansion method for matrices larger than {@code 4x4}.
     *
     * @since  1.5.0
     * @see    #determinant_CofactorExpansion(Matrix)
     */
    static double determinant_GaussElimination(Matrix m) {      
        checkRequirements(m);

        double det = 1;
        int n = m.getNumRows();  // Get the number of rows

        // Return 1 if the matrix is an identity matrix
        if (m.isIdentity()) return det;

        // Create a copy of the matrix for manipulation
        double[][] matrix = m.getEntries();

        // Perform Gaussian elimination
        for (int col = 0; col < n; col++) {
            // Find the pivot row
            int pivotRow = col;
            for (int row = col + 1; row < n; row++) {
                if (Math.abs(matrix[row][col]) > Math.abs(matrix[pivotRow][col])) {
                    pivotRow = row;
                }
            }

            // Swap rows if needed
            if (pivotRow != col) {
                double[] tempRow = matrix[col];
                matrix[col] = matrix[pivotRow];
                matrix[pivotRow] = tempRow;
                det *= -1;  // Swap changes the sign of the determinant
            }

            // Check if the pivot element is zero (singular matrix)
            if (matrix[col][col] == 0) return 0;

            // Update the determinant with the pivot element
            det *= matrix[col][col];

            // Perform row operations to zero out the elements below the pivot
            for (int row = col + 1; row < n; row++) {
                double factor = matrix[row][col] / matrix[col][col];
                for (int i = col; i < n; i++) {
                    matrix[row][i] -= factor * matrix[col][i];
                }
            }
        }
        return det;
    }

    /**
     * Calculates the determinant of the given matrix using Gaussian elimination.
     * 
     * <p>This method is an overloaded version of {@link #determinant_GaussElimination(Matrix)}
     * and creates a new matrix from the given 2D array first.
     *
     * <p>Gaussian elimination is a more efficient method for calculating the determinant
     * of larger matrices. This method reduces the matrix to an upper triangular form by
     * performing row operations. Once the matrix is in upper triangular form, the 
     * determinant is the product of the diagonal elements.
     * 
     * <p>During the process, the algorithm swaps rows to ensure that pivot elements
     * are non-zero. Each row swap inverts the sign of the determinant, and this is
     * taken into account in the final calculation.
     * 
     * <p>The time complexity of this method is {@code O(n^3)}, making it significantly faster than
     * cofactor expansion for larger matrices. This method is particularly useful for matrices
     * that are larger than {@code 4x4}, where cofactor expansion becomes too slow.
     * 
     * <p>Gaussian elimination is not suitable for matrices that are close to singular (i.e., matrices
     * that have a very small determinant), as it may result in numerical instability due to rounding errors.
     * However, for most practical purposes, this method provides an efficient and accurate determinant
     * calculation.
     * 
     * @param  a  The 2D array representation of the matrix for which to calculate the determinant.
     * @return    The determinant of the matrix, or 0 if the matrix is singular.
     *
     * @throws NullMatrixException         If the matrix is {@code null} or contains {@code null} references.
     * @throws IllegalMatrixSizeException  If the matrix is not a square matrix.
     *
     * @implNote
     * This method is preferred for larger matrices due to its lower time complexity. It is more suitable
     * and efficient than the cofactor expansion method for matrices larger than {@code 4x4}.
     * 
     * @see    #determinant_GaussElimination(Matrix)
     */
    static double determinant_GaussElimination(double[][] a) {
        return determinant_GaussElimination(new Matrix(a));
    }
}
