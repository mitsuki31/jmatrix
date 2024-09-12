// :: ------------------- :: //
/* --   MATRIX BUILDER    -- */
// :: ------------------- :: //

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

package com.mitsuki.jmatrix;

import com.mitsuki.jmatrix.core.MatrixChecker;
import com.mitsuki.jmatrix.core.MatrixUtils;
import com.mitsuki.jmatrix.exception.IllegalMatrixSizeException;
import com.mitsuki.jmatrix.exception.InvalidIndexException;
import com.mitsuki.jmatrix.exception.JMatrixBaseException;
import com.mitsuki.jmatrix.exception.NullMatrixException;
import static com.mitsuki.jmatrix.exception.JMatrixBaseException.raise;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The <b>Matrix</b> class represents a two-dimensional (2D) array of {@code double}s.
 * An array with two dimensions (has rows and columns), also can be called a matrix.
 *
 * <p><b>In mathematics, a matrix is a rectangular array or table of numbers, arranged in rows and columns,
 * and enclosed by brackets {@code ( )} or square brackets {@code [ ]}, which is used to represent a
 * mathematical object or a property of such an object</b>. Numbers arranged in a matrix are called matrix's
 * <b>elements</b> or <b>entries</b>. Matrices are usually symbolized using upper-case letters, while the corresponding
 * lower-case letters, with two subscript indices. Matrices are widely used in various fields, including
 * mathematics, computer science, physics, and engineering, to represent and manipulate data.
 *
 * <p>This class provides methods for creating, accessing and manipulating matrices,
 * as well as basic matrix operations such as:
 * <ul>
 * <li> {@linkplain #sum(Matrix)    Addition}
 * <li> {@linkplain #sub(Matrix)    Subtraction}
 * <li> {@linkplain #mult(Matrix)   Matrix multiplication}
 * <li> {@linkplain #mult(double)   Scalar multiplication}
 * <li> {@linkplain #transpose()    Transposition}
 * <li> {@linkplain #trace()        Trace}
 * </ul>
 *
 * <p>Also it provides several matrix type checkers, such as:
 * <ul>
 * <li> {@link #isSquare()} - Checks whether the matrix is a square matrix.
 * <li> {@link #isDiagonal()} - Checks whether the matrix is a diagonal matrix.
 * <li> {@link #isLowerTriangular()} - Checks whether the matrix is a lower triangular matrix.
 * <li> {@link #isUpperTriangular()} - Checks whether the matrix is a upper triangular matrix.
 * <li> {@link #isSparse()} - Checks whether the matrix is a sparse matrix.
 * </ul>
 *
 * <p>Moreover, there are also some helper methods, such as:
 * <ul>
 * <li> {@link #addRow(double[])} - Appends a given array to the last row in the matrix.
 * <li> {@link #addColumn(double[])} - Appends a given array to the last column in the matrix.
 * <li> {@link #insertRow(int, double[])} - Inserts a given array at the specified row index in the matrix.
 * <li> {@link #insertColumn(int, double[])} - Inserts a given array at the specified column index in the matrix.
 * <li> {@link #dropRow(int)} - Drops and removes specific row at the given row index.
 * <li> {@link #dropColumn(int)} - Drops and removes specific column at the given column index.
 * </ul>
 *
 * <blockquote>
 * <p><b>Note:</b> All the above helper methods are implemented in such a way that they help in transforming the
 * matrix entries efficiently. Additionally, they also allow the use of negative indices in arguments that depend
 * on the matrix index (all of them, except {@link #addRow} and {@link #addColumn} methods).
 * </blockquote>
 *
 * <p><b>Example:</b></p>
 *
 * <pre><code class="language-java">&nbsp;
 *   double[][] entries = {
 *       {1.0, 2.0},
 *       {3.0, 4.0},
 *       {5.0, 6.0}
 *   };
 *
 *   Matrix m = new Matrix(entries);
 *   m.mult(2.0);
 *
 *   m.display();
 * </code></pre>
 *
 * <p><b>Output:</b></p>
 *
 * <pre>&nbsp;
 *   [   [2.0, 4.0],
 *       [6.0, 8.0],
 *       [10.0, 12.0]   ]
 * </pre>
 *
 * <p>For creating the "zero matrix" (the matrix with all elements is zero),
 * it just simply by using the {@link #Matrix(int, int)} constructor and specify the number of rows and columns, respectively.
 *
 * <pre><code class="language-java">&nbsp;
 *   Matrix m = new Matrix(5, 5);
 * </code></pre>
 *
 * <p>Code above will create and construct a new "zero matrix"
 * with dimensions {@code 5x5}.
 *
 *
 * @author   <a href="https://github.com/mitsuki31" target="_blank">
 *           Ryuu Mitsuki</a>
 * @version  3.3, 12 September 2024
 * @since    0.1.0
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *           Apache License 2.0</a>
 *
 * @see      MatrixUtils
 * @see      <a href="https://en.m.wikipedia.org/wiki/Matrix_(mathematics)" target="_blank">
 *           "Matrix (Wikipedia)"</a>
 */
public class Matrix implements MatrixUtils {

    /**
     * Stores the entries array of this matrix.
     *
     * <p>Please refer to {@link #getEntries()} method
     * to retrieve the entries array of this matrix.
     *
     * @see #getEntries()
     */
    private double[ ][ ] ENTRIES = null;

    /**
     * Stores the number of rows of this matrix.
     *
     * <p>Please refer to {@link #getSize()} method
     * to retrieve the number of rows and columns of this matrix.
     *
     * @see #getSize()
     */
    private int ROWS = 0;

    /**
     * Stores the number of columns of this matrix.
     *
     * <p>Please refer to {@link #getSize()} method
     * to retrieve the number of rows and columns of this matrix.
     *
     * @see #getSize()
     */
    private int COLS = 0;

    /**
     * Deprecated variable.
     */
    private int index = 0;

    /**
     * Stores the selected index row of this matrix
     * from {@link #select(int)} method.
     *
     * @see #select(int)
     * @see #change(double ...)
     */
    private int selectedIndex = 0;

    /**
     * Stores {@code boolean} value that detects if the
     * user has called {@link #select(int)} method.
     *
     * @see #select(int)
     * @see #change(double ...)
     */
    private boolean hasSelect = false;

    /**
     * This variable stores the exception that want to be thrown.
     *
     * <p>The value itself is currently {@code null} unless it stored an exception.
     */
    private static RuntimeException cause = null;


    /**
     * A threshold for {@code double} comparison.
     */
    public static final double THRESHOLD = 1e-6;



    /*=========================================
    ::
    ::  MATRIX CONSTRUCTORS
    ::
    =========================================*/


    /**
     * Constructs new <b>Matrix</b> object without any parameter.
     *
     * <p>This would creates a new <b>Matrix</b> object with {@code null} entries.
     * To create a "zero matrix", consider using {@link #Matrix(int, int)}.
     *
     * @since 0.2.0
     * @see   #Matrix(int, int)
     * @see   #Matrix(int, int, double)
     * @see   #Matrix(double[][])
     */
    public Matrix() {}


    /**
     * Constructs new <b>Matrix</b> object with specified number of rows and columns.
     *
     * <p>Furthermore, this constructor would creates a new "zero matrix"
     * (matrix with all elements equal to zero).
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = new Matrix(3, 3);
     *   m.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [0.0, 0.0, 0.0],
     *       [0.0, 0.0, 0.0],
     *       [0.0, 0.0, 0.0]   ]
     * </pre>
     *
     * @param  rows                        a value for number of rows of matrix (min 1).
     * @param  cols                        a value for number of columns of matrix (min 1).
     *
     * @throws IllegalMatrixSizeException  if the two input sizes is negative value
     *                                     or one of two input sizes are equals to zero.
     *
     * @since                              0.1.0
     * @see                                #Matrix()
     * @see                                #Matrix(int, int, double)
     * @see                                #Matrix(double[][])
     * @see                                #identity(int)
     */
    public Matrix(int rows, int cols) {
        // Check for negative or zero value size
        if (rows < 0) {
            cause = new IllegalMatrixSizeException(
                "Value for number of rows cannot be negative value.");
        } else if (cols < 0) {
            cause = new IllegalMatrixSizeException(
                "Value for number of columns cannot be negative value.");
        } else if (rows == 0 || cols == 0) {
            cause = new IllegalMatrixSizeException(
                "Cannot create a matrix with zero size between rows and columns.");
        }

        // Throw the exception if got one
        if (cause != null) raise(cause);

        // Initialize the entries, but does not assign any values.
        // Which means it would creates zero matrix with specified dimensions.
        this.ENTRIES = new double[rows][cols];
    }


    /**
     * Constructs new <b>Matrix</b> object with specified sizes of rows and columns.
     *
     * <p>Also fills out entire elements of matrix with the given value.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = new Matrix(3, 3, 5);
     *   m.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [5.0, 5.0, 5.0]
     *       [5.0, 5.0, 5.0],
     *       [5.0, 5.0, 5.0]   ]
     * </pre>
     *
     * @param  rows                        a value for number of rows of matrix (min 1).
     * @param  cols                        a value for number of columns of matrix (min 1).
     * @param  val                         a value to filled out into all matrix elements.
     *
     * @throws IllegalMatrixSizeException  if the one or two input sizes is negative value
     *                                     or if they are equals to zero.
     *
     * @since                              0.1.0
     * @see                                #Matrix()
     * @see                                #Matrix(int, int)
     * @see                                #Matrix(double[][])
     * @see                                #identity(int)
     *
     * @warning                            It is recommended to use {@link #Matrix(double[][])}
     *                                     instead for efficiency on constructing a matrix array.
     */
    public Matrix(int rows, int cols, double val) {
        // Check for negative or zero value at input arguments
        if (rows < 0) {
            cause = new IllegalMatrixSizeException(
                "Value for number of rows cannot be negative value.");
        } else if (cols < 0) {
            cause = new IllegalMatrixSizeException(
                "Value for number of columns cannot be negative value.");
        } else if (rows == 0 || cols == 0) {
            cause = new IllegalMatrixSizeException(
                "Cannot create a matrix with zero size between rows and columns.");
        }

        // Throw the exception if got one
        if (cause != null) raise(cause);

        // Copy the sizes from input parameters
        this.ROWS = rows;
        this.COLS = cols;

        // Initialize the entries
        this.ENTRIES = new double[rows][cols];

        // Fill all matrix entries with "val"
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.ENTRIES[r][c] = val;
            }
        }
    }

    /**
     * Constructs new <b>Matrix</b> object with the given two-dimensional array.
     *
     * <p>Converts the two-dimensional array into <b>Matrix</b> object, without changing its values.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] entries = {
     *       { 1, 2 },
     *       { 3, 4 }
     *   };
     *
     *   Matrix m = new Matrix(entries);
     *   m.display();
     * </code></pre>
     *
     * <p>Code above equivalent with ...
     *
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = new Matrix(new double[][] {
     *       { 1, 2 },
     *       { 3, 4 }
     *   });
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [1.0, 2.0],
     *       [3.0, 4.0]   ]
     * </pre>
     *
     * @param  arr                  the two-dimensional array.
     *
     * @throws NullMatrixException  if the given array is {@code null} or empty.
     *
     * @since                       1.0.0b.1
     * @see                         #Matrix()
     * @see                         #Matrix(int, int)
     * @see                         #Matrix(int, int, double)
     * @see                         #identity(int)
     */
    public Matrix(double[ ][ ] arr) {
        // Raise the exception immediately if given array is null
        if (arr == null || arr.length == 0) {
            raise(new NullMatrixException(
                "Given two-dimensional array is null. Please ensure the array has valid elements."));
        }

        // Retrieve the value of each sizes
        this.ROWS = arr.length;
        this.COLS = arr[0].length;

        // Initialize the entries
        this.ENTRIES = new double[this.ROWS][this.COLS];

        // Iterate over each elements to prevent the shallow copy
        for (int r = 0; r < this.ROWS; r++) {
            for (int c = 0; c < this.COLS; c++) {
                this.ENTRIES[r][c] = arr[r][c];
            }
        }
    }



    /*=========================================
    ::
    ::  MATRIX SPECIAL CONSTRUCTORS
    ::
    =========================================*/

    /*--------------------------
    ::         Create
    --------------------------*/

    /**
     * Creates a new matrix with specified number of rows and columns.
     *
     * <p>This method would constructs and creates a "zero matrix" in implicit way,
     * and forcibly converts this matrix into zero matrix with specified dimensions
     * if this matrix has valid entries (which means it will deletes and creates a new one).
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = new Matrix();
     *   m.create(3, 4);
     * </code></pre>
     *
     * <p><b>Code above equivalents to:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = new Matrix(3, 4);
     * </code></pre>
     *
     * @param  rows                        a value for number of rows (min 1).
     * @param  cols                        a value for number of columns (min 1).
     *
     * @throws IllegalMatrixSizeException  if one or both inputs are
     *                                     negative value or equals to zero.
     *
     * @since                              0.2.0
     * @see                                #create(double[][])
     * @see                                #Matrix(int, int)
     */
    public void create(int rows, int cols) {
        // Check for negative or zero value on input arguments
        if (rows < 0) {
            cause = new IllegalMatrixSizeException(
                "Value for number of rows cannot be negative value.");
        } else if (cols < 0) {
            cause = new IllegalMatrixSizeException(
                "Value for number of columns cannot be negative value.");
        } else if (rows == 0 || cols == 0) {
            cause = new IllegalMatrixSizeException(
                "Cannot create a matrix with zero size between rows and columns.");
        }

        // Throw the exception if got one
        if (cause != null) raise(cause);

        // Copy the sizes from input parameters
        this.ROWS = rows;
        this.COLS = cols;

        // Initialize the entries
        this.ENTRIES = new double[rows][cols];
    }


    /**
     * Creates a new matrix or overwrites the elements of this matrix with the given two-dimensional array.
     *
     * <p>Dimensions of new matrix (also known as, the number of rows and columns)
     * will be equated with the dimensions from given two-dimensional array.
     *
     * @param  arr                  the two-dimensional array.
     *
     * @throws NullMatrixException  if the given array is {@code null} or empty.
     *
     * @since                       1.0.0b.1
     * @see                         #create(int, int)
     * @see                         #Matrix(double[][])
     */
    public void create(double[ ][ ] arr) {
        // Raise the exception immediately if given array is null
        MatrixChecker.requireNonNull(arr, (new StringBuilder())
            .append("Given two-dimensional array is null. ")
            .append("Please ensure the array has valid elements.")
            .toString()
        );

        // Initialize the entries
        this.ENTRIES = new double[arr.length][arr[0].length];

        // Iterate over each elements to prevent the shallow copy
        for (int r = 0; r < arr.length; r++) {
            for (int c = 0; c < arr[0].length; c++) {
                this.ENTRIES[r][c] = arr[r][c];
            }
        }
    }


    /*--------------------------
    ::     Matrix Identity
    --------------------------*/

    /**
     * Constructs an identity matrix with dimensions {@code n x n}.
     *
     * <p>This method generates and constructs an identity matrix of size {@code n x n}.<br>
     * To check the matrix is diagonal matrix, please refer to {@link #isDiagonal()} method.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = Matrix.identity(3);
     *   m.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [1.0, 0.0, 0.0],
     *       [0.0, 1.0, 0.0],
     *       [0.0, 0.0, 1.0]   ]
     * </pre>
     *
     * @param  n                           the size of the identity matrix
     *                                     to be generated (min 1).
     *
     * @return                             a new identity matrix with
     *                                     dimensions {@code n x n}.
     *
     * @throws IllegalMatrixSizeException  if the given size matrix is
     *                                     less than 1.
     *
     * @since                              1.0.0b.7
     * @see                                #Matrix(int, int)
     * @see                                #Matrix(double[][])
     * @see                                #isDiagonal()
     * @see                                #isIdentity()
     */
    public static Matrix identity(int n) {
        // Check for negative value on input argument
        if (n < 1) {
            raise(new IllegalMatrixSizeException(
                "Sizes of identity matrix cannot be lower than 1."));
        }

        // Create the identity matrix with size "n x n"
        double[ ][ ] entries = new double[n][n];

        for (int i = 0; i < n; i++) {
            entries[i][i] = 1.0;
        }

        return new Matrix(entries);
    }


    /*--------------------------
    ::        Add Row
    --------------------------*/

    /**
     * Adds a new row to this matrix by appending the specified array, while
     * avoiding shallow copies.
     *
     * <p>This method appends the given array {@code a} as a new row to the
     * bottom of the matrix. It ensures that no shallow copies are created,
     * guaranteeing independent data structures for the matrix and the
     * appended array.
     *
     * <p>The length of the input array {@code a} must be equal to the number
     * of columns in the matrix. If not, an {@code IllegalArgumentException}
     * is thrown. The appended row might be truncated if the input array has
     * more elements than the matrix column count.
     *
     * <p><b>Example:</b></p>
     * <pre><code class="language-java">&nbsp;
     *   // Create a 3x3 matrix filled with 5.0
     *   Matrix m = new Matrix(3, 3, 5);
     *   double[] p = { 7, 7, 7 };
     *
     *   // Append the array 'p' to matrix 'm'
     *   m = m.addRow(p);
     * </code></pre>
     *
     * <p>Matrix {@code m} will looks like this after appended the array:</p>
     * <pre>&nbsp;
     *   [   [5.0, 5.0, 5.0],
     *       [5.0, 5.0, 5.0],
     *       [5.0, 5.0, 5.0],
     *       [7.0, 7.0, 7.0]   ]
     * </pre>
     *
     * @param  a  The array to be appended as the new row.
     * @return    A new matrix with the appended row.
     *
     * @throws NullMatrixException
     *           If this matrix is {@code null}.
     * @throws NullPointerException
     *           If the given array is {@code null} or empty. This exception might
     *           be thrown as caused exception.
     * @throws IllegalArgumentException
     *           If the length of array is less than the number of columns in matrix.
     *           This exception might be thrown as caused exception.
     *
     * @since 1.5.0
     * @see   #addRow(Matrix, double[])
     * @see   #insertRow(int, double[])
     * @see   #dropRow(int)
     */
    public Matrix addRow(double[] a) {
        return Matrix.addRow(this, a);
    }

    /**
     * Adds a new row to a matrix by appending the specified array, while
     * avoiding shallow copies.
     *
     * <p>This method appends the given array {@code a} as a new row to the
     * bottom of the matrix {@code m}. It ensures that <b>no shallow copies</b> are
     * created, guaranteeing independent data structures for the matrix and the
     * appended array.
     *
     * <p>The length of the input array {@code a} must be equal to the number
     * of columns in the matrix. If not, an {@code IllegalArgumentException}
     * is thrown. The appended row might be <b>truncated</b> if the input array has
     * more elements than the matrix column count.
     *
     * <p><b>Example:</b></p>
     * <pre><code class="language-java">&nbsp;
     *   // Create a 3x3 matrix filled with 5.0
     *   Matrix m = new Matrix(3, 3, 5);
     *   double[] p = { 7, 7, 7 };
     *
     *   // Append the array 'p' to matrix 'm'
     *   m = Matrix.addRow(m, p);
     * </code></pre>
     *
     * <p>Matrix {@code m} will looks like this after appended the array:</p>
     * <pre>&nbsp;
     *   [   [5.0, 5.0, 5.0],
     *       [5.0, 5.0, 5.0],
     *       [5.0, 5.0, 5.0],
     *       [7.0, 7.0, 7.0]   ]
     * </pre>
     *
     * @param  m  The matrix to which the new row will be added.
     * @param  a  The array to be appended as the new row.
     * @return    A new matrix with the appended row.
     *
     * @throws NullMatrixException
     *           If the given matrix is {@code null}.
     * @throws NullPointerException
     *           If the given array is {@code null} or empty. This exception might
     *           be thrown as caused exception.
     * @throws IllegalArgumentException
     *           If the length of array is less than the number of columns in matrix.
     *           This exception might be thrown as caused exception.
     *
     * @since 1.5.0
     * @see   #addRow(double[])
     * @see   #insertRow(Matrix, int, double[])
     * @see   #dropRow(Matrix, int)
     */
    public static Matrix addRow(Matrix m, double[] a) {
        if (MatrixUtils.isNullEntries(m)) {
            raise(new NullMatrixException(
                "Matrix is null. Please ensure the matrix are initialized"));
        } else if (a == null || a.length == 0) {
            raise(new JMatrixBaseException(new NullPointerException(
                "Given array is null or empty. Cannot append it into the matrix")));
        }

        return Matrix.insertRow(m, m.getNumRows() - 1, a);
    }


    /*--------------------------
    ::       Add Column
    --------------------------*/

    /**
     * Adds a new column to this matrix by appending the specified array, while
     * avoiding shallow copies.
     *
     * <p>This method appends the given array {@code a} as a new column to the
     * last column of the matrix. It ensures that no shallow copies are created,
     * guaranteeing independent data structures for the matrix and the
     * appended array.
     *
     * <p>The length of the input array {@code a} must be equal to the number
     * of rows in the matrix. If not, an {@code IllegalArgumentException}
     * is thrown. The appended column might be truncated if the input array has
     * more elements than the matrix row count.
     *
     * <p><b>Example:</b></p>
     * <pre><code class="language-java">&nbsp;
     *   // Create a 3x3 identity matrix
     *   Matrix m = Matrix.identity(3);
     *   double[] p = { 7, 7, 7 };
     *
     *   // Append the array 'p' to matrix 'm'
     *   m = m.addRow(p);
     * </code></pre>
     *
     * <p>Matrix {@code m} will looks like this after appended the array:</p>
     * <pre>&nbsp;
     *   [   [1.0, 0.0, 0.0, 7.0],
     *       [0.0, 1.0, 0.0, 7.0],
     *       [0.0, 0.0, 1.0, 7.0]   ]
     * </pre>
     *
     * @param  a  The array to be appended as the new column.
     * @return    A new matrix with the appended column.
     *
     * @throws NullMatrixException
     *           If this matrix is {@code null}.
     * @throws NullPointerException
     *           If the given array is {@code null} or empty. This exception might
     *           be thrown as caused exception.
     * @throws IllegalArgumentException
     *           If the length of array is less than the number of rows in matrix.
     *           This exception might be thrown as caused exception.
     *
     * @since 1.5.0
     * @see   #addColumn(Matrix, double[])
     * @see   #insertColumn(int, double[])
     * @see   #dropColumn(int)
     */
    public Matrix addColumn(double[] a) {
        return Matrix.addColumn(this, a);
    }

    /**
     * Adds a new column to a matrix by appending the specified array, while
     * avoiding shallow copies.
     *
     * <p>This method appends the given array {@code a} as a new column to the
     * last column of the matrix. It ensures that no shallow copies are created,
     * guaranteeing independent data structures for the matrix and the
     * appended array.
     *
     * <p>The length of the input array {@code a} must be equal to the number
     * of rows in the matrix. If not, an {@code IllegalArgumentException}
     * is thrown. The appended column might be truncated if the input array has
     * more elements than the matrix row count.
     *
     * <p><b>Example:</b></p>
     * <pre><code class="language-java">&nbsp;
     *   // Create a 3x3 identity matrix
     *   Matrix m = Matrix.identity(3);
     *   double[] p = { 7, 7, 7 };
     *
     *   // Append the array 'p' to matrix 'm'
     *   m = Matrix.addRow(m, p);
     * </code></pre>
     *
     * <p>Matrix {@code m} will looks like this after appended the array:</p>
     * <pre>&nbsp;
     *   [   [1.0, 0.0, 0.0, 7.0],
     *       [0.0, 1.0, 0.0, 7.0],
     *       [0.0, 0.0, 1.0, 7.0]   ]
     * </pre>
     *
     * @param  m  The matrix to which the new column will be added.
     * @param  a  The array to be appended as the new column.
     * @return    A new matrix with the appended column.
     *
     * @throws NullMatrixException
     *           If the given matrix is {@code null}.
     * @throws NullPointerException
     *           If the given array is {@code null} or empty. This exception might
     *           be thrown as caused exception.
     * @throws IllegalArgumentException
     *           If the length of array is less than the number of rows in matrix.
     *           This exception might be thrown as caused exception.
     *
     * @since 1.5.0
     * @see   #addColumn(double[])
     * @see   #insertColumn(Matrix, int, double[])
     * @see   #dropColumn(Matrix, int)
     */
    public static Matrix addColumn(Matrix m, double[] a) {
        if (MatrixUtils.isNullEntries(m)) {
            raise(new NullMatrixException(
                "Matrix is null. Please ensure the matrix are initialized"));
        } else if (a == null || a.length == 0) {
            raise(new JMatrixBaseException(new NullPointerException(
                "Given array is null or empty. Cannot append it into the matrix")));
        }

        return Matrix.insertColumn(m, m.getNumCols() - 1, a);
    }


    /*--------------------------
    ::       Insert Row
    --------------------------*/

    /**
     * Inserts a new row into this matrix at a specified index, shifting existing
     * rows down.
     *
     * <p>This method prioritizes clarity, efficiency, flexibility, and data
     * integrity by:
     *
     * <ul>
     * <li> Emphasizes data integrity by avoiding shallow copies. It creates
     *      independent copies of arrays using {@link System#arraycopy}, ensuring
     *      that modifications to the inserted <b>row</b> or the original matrix
     *      do not affect each other unintentionally.
     * <li> For primitive types like {@code double[]}, {@link System#arraycopy}
     *      potentially offers performance benefits over direct assignment due
     *      to its avoidance of reflection-based copying and it is a native call.
     * <li> Gracefully handles potential inconsistencies between the input array
     *      length and the matrix column count. If the array is longer,
     *      it truncates it to match the matrix shape, maintaining structural
     *      consistency.
     * <li> The support for negative row indices allows for more flexible row
     *      selection, enabling users to count from the end of the matrix for
     *      convenience.
     * </ul>
     *
     * <p><b>Example:</b></p>
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = new Matrix(new double[][] {
     *       { 5.0 }, { 5.0 }
     *   });
     *   // Insert the array to the second row
     *   m = m.insertRow(1, new double[] { 10.0 });
     * </code></pre>
     *
     * <p>Matrix {@code m} will be looks like this after inserted a row:</p>
     * <pre>&nbsp;
     *   [   [5.0],
     *       [10.0],
     *       [5.0]   ]
     * </pre>
     *
     * @param  row  The index at which to insert the row (can be negative).
     * @param  a    The array representing the new row to be inserted (not {@code null}
     *              and not less than matrix column count).
     * @return      A new matrix with the specified array inserted as a new row
     *              at the given index.
     *
     * @throws NullMatrixException
     *           If this matrix is {@code null}.
     * @throws NullPointerException
     *           If the given array is {@code null} or empty. This exception
     *           might be thrown as caused exception.
     * @throws InvalidIndexException
     *           If the row index is out of bounds.
     * @throws IllegalArgumentException
     *           If the length of the array is less than the number of columns
     *           in the matrix. This exception might be thrown as caused exception.
     *
     * @since 1.5.0
     * @see   #insertRow(Matrix, int, double[])
     * @see   #insertColumn(int, double[])
     * @see   #addRow(double[])
     * @see   #dropRow(int)
     */
    public Matrix insertRow(int row, double[] a) {
        return Matrix.insertRow(this, row, a);
    }

    /**
     * Inserts a new row into a matrix at a specified index, shifting existing
     * rows down.
     *
     * <p>This method prioritizes clarity, efficiency, flexibility, and data
     * integrity by:
     *
     * <ul>
     * <li> Emphasizes data integrity by avoiding shallow copies. It creates
     *      independent copies of arrays using {@link System#arraycopy}, ensuring
     *      that modifications to the inserted <b>row</b> or the original matrix
     *      do not affect each other unintentionally.
     * <li> For primitive types like {@code double[]}, {@link System#arraycopy}
     *      potentially offers performance benefits over direct assignment due
     *      to its avoidance of reflection-based copying and it is a native call.
     * <li> Gracefully handles potential inconsistencies between the input array
     *      length and the matrix column count. If the array is longer,
     *      it truncates it to match the matrix shape, maintaining structural
     *      consistency.
     * <li> The support for negative row indices allows for more flexible row
     *      selection, enabling users to count from the end of the matrix for
     *      convenience.
     * </ul>
     *
     * <p><b>Example:</b></p>
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = new Matrix(new double[][] {
     *       { 5.0 }, { 5.0 }
     *   });
     *   // Insert the array to the second row
     *   m = Matrix.insertRow(m, 1, new double[] { 10.0 });
     * </code></pre>
     *
     * <p>Matrix {@code m} will be looks like this after inserted a row:</p>
     * <pre>&nbsp;
     *   [   [5.0],
     *       [10.0],
     *       [5.0]   ]
     * </pre>
     *
     * @param  m    The matrix to insert the row into (not {@code null}).
     * @param  row  The index at which to insert the row (can be negative).
     * @param  a    The array representing the new row to be inserted (not {@code null}
     *              and not less than matrix column count).
     * @return      A new matrix with the specified array inserted as a new row
     *              at the given index.
     *
     * @throws NullMatrixException
     *           If the given matrix is {@code null}.
     * @throws NullPointerException
     *           If the given array is {@code null} or empty. This exception
     *           might be thrown as caused exception.
     * @throws InvalidIndexException
     *           If the row index is out of bounds.
     * @throws IllegalArgumentException
     *           If the length of the array is less than the number of columns
     *           in the matrix. This exception might be thrown as caused exception.
     *
     * @since 1.5.0
     * @see   #insertRow(int, double[])
     * @see   #insertColumn(Matrix, int, double[])
     * @see   #addRow(Matrix, double[])
     * @see   #dropRow(Matrix, int)
     */
    public static Matrix insertRow(Matrix m, int row, double[] a) {
        // Retrieve the matrix sizes, do not worry about users
        // input a null matrix (uninitialized matrix), because these
        // will be zeros (0), and then an exception will be thrown afterwards
        int mRows = m.getNumRows();
        int mCols = m.getNumCols();
        row += (row < 0) ? (mRows + 1) : 0;  // Allow negative indexing

        if (MatrixUtils.isNullEntries(m)) {  // Check for null matrix
            raise(new NullMatrixException(
                "Matrix is null. Please ensure the matrix are initialized"));
        } else if (a == null || a.length == 0) {  // Check for null or empty array
            raise(new JMatrixBaseException(new NullPointerException(
                "Given array is null or empty. Cannot insert it into the matrix")));
        } else if (row < 0 || row >= mRows) {  // Check for the index is out of bounds
            raise(new InvalidIndexException(
                String.format("Given row index is out of range: %d",
                    (row < 0) ? (row - mRows - 1) : row
                )
            ));
        } else if (a.length < mRows) {
            // Check for the array length is less than matrix number of rows
            raise(new JMatrixBaseException(
                new IllegalArgumentException(String.format(
                    "The length of array is less than matrix row count: %d < %d",
                    a.length, mRows
                ))
            ));
        }

        double[][] entries = m.getEntries();                   // Matrix entries array
        double[][] newEntries = new double[mRows + 1][mCols];  // Result array

        for (int i = 0, x = 0; i < newEntries.length; i++) {
            if (i == row) {
                // Create the copy of the array and truncating the array to fit
                // with the column of the matrix and also it would not use
                // reflection-based copy as far as it being used to copy primitive types.
                System.arraycopy(a, 0, newEntries[i], 0, Math.min(mCols, newEntries[0].length));
                continue;
            }
            System.arraycopy(entries[x++], 0, newEntries[i], 0, Math.min(mCols, newEntries[0].length));
        }

        return new Matrix(newEntries);
    }


    /*--------------------------
    ::     Insert Column
    --------------------------*/

    /**
     * Inserts a new column into this matrix at a specified index, shifting existing
     * columns to the right.
     *
     * <p>This method prioritizes clarity, efficiency, flexibility, and data
     * integrity by:
     *
     * <ul>
     * <li> Emphasizes data integrity by avoiding shallow copies. It creates
     *      independent copies of arrays using {@link System#arraycopy}, ensuring
     *      that modifications to the inserted <b>column</b> or the original matrix
     *      do not affect each other unintentionally.
     * <li> For primitive types like {@code double[]}, {@link System#arraycopy}
     *      potentially offers performance benefits over direct assignment due
     *      to its avoidance of reflection-based copying and it is a native call.
     * <li> Gracefully handles potential inconsistencies between the input array
     *      length and the matrix row count. If the array is longer,
     *      it truncates it to match the matrix shape, maintaining structural
     *      consistency.
     * <li> The support for negative column indices allows for more flexible column
     *      selection, enabling users to count from the end of the matrix for
     *      convenience.
     * </ul>
     *
     * <p><b>Example:</b></p>
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = new Matrix(new double[][] {
     *       { 15.0, 16.0 }
     *   });
     *   // Insert the array to the last column
     *   m = m.insertColumn(-1, new double[] { 17.0 });
     * </code></pre>
     *
     * <p>Matrix {@code m} will be looks like this after inserted a row:</p>
     * <pre>&nbsp;
     *   [   [15.0, 16.0, 17.0]   ]
     * </pre>
     *
     * @implNote
     * <p>This method achieves its purpose efficiently by leveraging the
     * {@link #insertRow} method and matrix transpositions, as follows:
     *
     * <ol>
     * <li> The method begins by transposing the input matrix using
     *      {@link #transpose}. This effectively swaps rows and columns,
     *      converting the original columns into rows.
     * <li> With the matrix transposed, the method calls {@link #insertRow} to
     *      insert the given array as a new row at the specified index {@code col}.
     *      Importantly, this insertion now operates on the transposed matrix,
     *      effectively adding a new column in the original context.
     * <li> The result of {@link #insertRow} is a transposed matrix with the new
     *      column added. The method then transposes the matrix again using
     *      {@link #transpose}, returning it to its original shape with the new
     *      column correctly positioned.
     * </ol>
     *
     * @param  col  The index at which to insert the column (can be negative).
     * @param  a    The array representing the new column to be inserted (not
     *              {@code null} and not less than matrix row count).
     * @return      A new matrix with the specified array inserted as a new column
     *              at the given index.
     *
     * @throws NullMatrixException
     *           If this matrix is {@code null}.
     * @throws NullPointerException
     *           If the given array is {@code null} or empty. This exception
     *           might be thrown as caused exception.
     * @throws InvalidIndexException
     *           If the column index is out of bounds.
     * @throws IllegalArgumentException
     *           If the length of the array is less than the number of rows
     *           in the matrix. This exception might be thrown as caused exception.
     *
     * @since 1.5.0
     * @see   #insertColumn(Matrix, int, double[])
     * @see   #insertRow(int, double[])
     * @see   #addColumn(double[])
     * @see   #dropColumn(int)
     */
    public Matrix insertColumn(int col, double[] a) {
        return Matrix.insertColumn(this, col, a);
    }

    /**
     * Inserts a new column into a matrix at a specified index, shifting existing
     * columns to the right.
     *
     * <p>This method prioritizes clarity, efficiency, flexibility, and data
     * integrity by:
     *
     * <ul>
     * <li> Emphasizes data integrity by avoiding shallow copies. It creates
     *      independent copies of arrays using {@link System#arraycopy}, ensuring
     *      that modifications to the inserted <b>column</b> or the original matrix
     *      do not affect each other unintentionally.
     * <li> For primitive types like {@code double[]}, {@link System#arraycopy}
     *      potentially offers performance benefits over direct assignment due
     *      to its avoidance of reflection-based copying and it is a native call.
     * <li> Gracefully handles potential inconsistencies between the input array
     *      length and the matrix row count. If the array is longer,
     *      it truncates it to match the matrix shape, maintaining structural
     *      consistency.
     * <li> The support for negative column indices allows for more flexible column
     *      selection, enabling users to count from the end of the matrix for
     *      convenience.
     * </ul>
     *
     * <p><b>Example:</b></p>
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = new Matrix(new double[][] {
     *       { 15.0, 16.0 }
     *   });
     *   // Insert the array to the last column
     *   m = Matrix.insertColumn(m, -1, new double[] { 17.0 });
     * </code></pre>
     *
     * <p>Matrix {@code m} will be looks like this after inserted a row:</p>
     * <pre>&nbsp;
     *   [   [15.0, 16.0, 17.0]   ]
     * </pre>
     *
     * @implNote
     * <p>This method achieves its purpose efficiently by leveraging the
     * {@link #insertRow} method and matrix transpositions, as follows:
     *
     * <ol>
     * <li> The method begins by transposing the input matrix using
     *      {@link #transpose}. This effectively swaps rows and columns,
     *      converting the original columns into rows.
     * <li> With the matrix transposed, the method calls {@link #insertRow} to
     *      insert the given array as a new row at the specified index {@code col}.
     *      Importantly, this insertion now operates on the transposed matrix,
     *      effectively adding a new column in the original context.
     * <li> The result of {@link #insertRow} is a transposed matrix with the new
     *      column added. The method then transposes the matrix again using
     *      {@link #transpose}, returning it to its original shape with the new
     *      column correctly positioned.
     * </ol>
     *
     * @param  m    The matrix to insert the column into (not {@code null}).
     * @param  col  The index at which to insert the column (can be negative).
     * @param  a    The array representing the new column to be inserted (not
     *              {@code null} and not less than matrix row count).
     * @return      A new matrix with the specified array inserted as a new column
     *              at the given index.
     *
     * @throws NullMatrixException
     *           If the given matrix is {@code null}.
     * @throws NullPointerException
     *           If the given array is {@code null} or empty. This exception
     *           might be thrown as caused exception.
     * @throws InvalidIndexException
     *           If the column index is out of bounds.
     * @throws IllegalArgumentException
     *           If the length of the array is less than the number of rows
     *           in the matrix. This exception might be thrown as caused exception.
     *
     * @since 1.5.0
     * @see   #insertColumn(int, double[])
     * @see   #insertRow(Matrix, int, double[])
     * @see   #addColumn(Matrix, double[])
     * @see   #dropColumn(Matrix, int)
     */
    public static Matrix insertColumn(Matrix m, int col, double[] a) {
        MatrixChecker.requireNonNull(m,
            "Matrix is null. Please ensure the matrix are initialized"
        );

        int mCols = m.getNumCols();          // Get the number of rows
        col += (col < 0) ? (mCols + 1) : 0;  // Allow negative indexing

        if (col < 0 || col > mCols) {  // Check for the index is out of bounds
            raise(new InvalidIndexException(
                String.format("Given column index is out of range: %d",
                    (col < 0) ? (col - mCols - 1) : col
                )
            ));
        } else if (a.length < mCols) {
            // Check for the array length is less than matrix number of rows
            raise(new JMatrixBaseException(
                new IllegalArgumentException(String.format(
                    "The length of array is less than matrix column count: %d < %d",
                    a.length, mCols
                ))
            ));
        }

        return Matrix.transpose(Matrix.insertRow(Matrix.transpose(m), col, a));
    }


    /*--------------------------
    ::        Drop Row
    --------------------------*/

    /**
     * Constructs a new matrix by removing the specified row from this matrix.
     *
     * <p>This method demonstrates a fundamental matrix manipulation technique:
     * removing a specific row to create a new matrix tailored to different needs.
     * It is crucial for various matrix-based operations, such as data filtering,
     * feature selection, or numerical analysis.
     *
     * <p>Additionally, this method also supports negative indexing, where negative
     * values count from the end of the matrix. For example, {@code -1} refers to the
     * <b>last row</b>, {@code -2} to the <b>second-to-last row</b>, and so on.
     *
     * <p>Supporting negative indexing aligns with common indexing conventions
     * in programming languages. It offers flexibility in accessing and
     * manipulating elements from the end of the matrix, often simplifying
     * calculations and logical operations.
     *
     * <p><b>Example:</b></p>
     * <pre><code class="language-java">&nbsp;
     *   Matrix matrix = // ... Initialize a new matrix
     *   Matrix matrixWithoutLastRow = matrix.dropRow(-1);
     * </code></pre>
     *
     * @param  row  An integer represents the index of the row to remove.
     *              Allow negative indexing, which count from the end of the matrix.
     * @return      A new matrix with the specified row removed.
     *
     * @throws NullMatrixException    If the given matrix is {@code null}.
     * @throws InvalidIndexException  If the provided row index is out of bounds.
     *
     * @since 1.5.0
     * @see   #dropRow(Matrix, int)
     * @see   #dropColumn(int)
     * @see   #minorMatrix(int, int)
     */
    public Matrix dropRow(int row) {
        return Matrix.dropRow(this, row);
    }

    /**
     * Constructs a new matrix by removing the specified row from the given matrix.
     *
     * <p>This method demonstrates a fundamental matrix manipulation technique:
     * removing a specific row to create a new matrix tailored to different needs.
     * It is crucial for various matrix-based operations, such as data filtering,
     * feature selection, or numerical analysis.
     *
     * <p>Additionally, this method also supports negative indexing, where negative
     * values count from the end of the matrix. For example, {@code -1} refers to the
     * <b>last row</b>, {@code -2} to the <b>second-to-last row</b>, and so on.
     *
     * <p>Supporting negative indexing aligns with common indexing conventions
     * in programming languages. It offers flexibility in accessing and
     * manipulating elements from the end of the matrix, often simplifying
     * calculations and logical operations.
     *
     * <p><b>Example:</b></p>
     * <pre><code class="language-java">&nbsp;
     *   Matrix matrix = // ... Initialize a new matrix
     *   Matrix matrixWithoutLastRow = matrix.dropRow(-1);
     * </code></pre>
     *
     * @param  m    The matrix to remove a row from.
     * @param  row  An integer represents the index of the row to remove.
     *              Allow negative indexing, which count from the end of the matrix.
     * @return      A new matrix with the specified row removed.
     *
     * @throws NullMatrixException    If the given matrix is {@code null}.
     * @throws InvalidIndexException  If the provided row index is out of bounds.
     *
     * @since 1.5.0
     * @see   #dropRow(int)
     * @see   #dropColumn(Matrix, int)
     * @see   #minorMatrix(Matrix, int, int)
     */
    public static Matrix dropRow(Matrix m, int row) {
        MatrixChecker.requireNonNull(m,
            "Matrix is null. Please ensure the matrix are initialized"
        );

        int rows = m.getNumRows();
        int cols = m.getNumCols();

        row += (row < 0) ? rows : 0;  // Allow negative indexing
        if (row >= rows || row < 0) {
            raise(new InvalidIndexException(
                String.format("Given row index is out of range: %d",
                    (row < 0) ? (row - rows) : row
                )
            ));
        }

        double[][] entries = m.getEntries();
        double[][] newEntries = new double[rows - 1][cols];
        for (int i = 0, destRow = 0; i < rows; i++) {
            if (i == row) continue;  // Skip the desired row index
            System.arraycopy(newEntries[destRow++], 0, entries[i], 0, cols);
        }

        return new Matrix(newEntries);
    }


    /*--------------------------
    ::       Drop Column
    --------------------------*/

    /**
     * Constructs a new matrix by removing the specified column from this matrix.
     *
     * <p>This method demonstrates a fundamental matrix manipulation technique:
     * removing a specific column to create a new matrix tailored to different needs.
     * It is crucial for various matrix-based operations, such as data filtering,
     * feature selection, or numerical analysis.
     *
     * <p>Additionally, this method also supports negative indexing, where negative
     * values count from the end of the matrix. For example, {@code -1} refers to the
     * <b>last column</b>, {@code -2} to the <b>second-to-last column</b>, and so on.
     *
     * <p>Supporting negative indexing aligns with common indexing conventions
     * in programming languages. It offers flexibility in accessing and
     * manipulating elements from the end of the matrix, often simplifying
     * calculations and logical operations.
     *
     * <p><b>Example:</b></p>
     * <pre><code class="language-java">&nbsp;
     *   Matrix matrix = // ... Initialize a new matrix
     *   Matrix matrixWithoutLastColumn = matrix.dropColumn(-1);
     * </code></pre>
     *
     * @param  col  An integer represents the index of the column to remove.
     *              Allow negative indexing, which count from the end of the matrix.
     * @return      A new matrix with the specified column removed.
     *
     * @throws NullMatrixException    If the given matrix is {@code null}.
     * @throws InvalidIndexException  If the provided column index is out of bounds.
     *
     * @since 1.5.0
     * @see   #dropColumn(Matrix, int)
     * @see   #dropRow(int)
     * @see   #minorMatrix(int, int)
     */
    public Matrix dropColumn(int col) {
        return Matrix.dropColumn(this, col);
    }

    /**
     * Constructs a new matrix by removing the specified column from the given matrix.
     *
     * <p>This method demonstrates a fundamental matrix manipulation technique:
     * removing a specific column to create a new matrix tailored to different needs.
     * It is crucial for various matrix-based operations, such as data filtering,
     * feature selection, or numerical analysis.
     *
     * <p>Additionally, this method also supports negative indexing, where negative
     * values count from the end of the matrix. For example, {@code -1} refers to the
     * <b>last column</b>, {@code -2} to the <b>second-to-last column</b>, and so on.
     *
     * <p>Supporting negative indexing aligns with common indexing conventions
     * in programming languages. It offers flexibility in accessing and
     * manipulating elements from the end of the matrix, often simplifying
     * calculations and logical operations.
     *
     * <p><b>Example:</b></p>
     * <pre><code class="language-java">&nbsp;
     *   Matrix matrix = // ... Initialize a new matrix
     *   Matrix matrixWithoutLastColumn = matrix.dropColumn(-1);
     * </code></pre>
     *
     * @param  m    The matrix to remove a column from.
     * @param  col  An integer represents the index of the column to remove.
     *              Allow negative indexing, which count from the end of the matrix.
     * @return      A new matrix with the specified column removed.
     *
     * @throws NullMatrixException    If the given matrix is {@code null}.
     * @throws InvalidIndexException  If the provided column index is out of bounds.
     *
     * @since 1.5.0
     * @see   #dropColumn(int)
     * @see   #dropRow(Matrix, int)
     * @see   #minorMatrix(Matrix, int, int)
     */
    public static Matrix dropColumn(Matrix m, int col) {
        if (MatrixUtils.isNullEntries(m)) {
            raise(new NullMatrixException(
                "Matrix is null. Please ensure the matrix are initialized"));
        }

        int rows = m.getNumRows();
        int cols = m.getNumCols();

        col += (col < 0) ? cols : 0;  // Allow negative indexing
        if (col >= cols || col < 0) {
            raise(new InvalidIndexException(
                String.format("Given column index is out of range: %d",
                    (col < 0) ? (col - cols) : col
                )
            ));
        }

        double[][] entries = new double[rows][cols - 1];
        for (int i = 0; i < rows; i++) {
            for (int j = 0, destCol = 0; j < cols; j++) {
                if (j == col) continue;  // Skip the desired column index
                entries[i][destCol++] = m.get(i, j);
            }
        }

        return new Matrix(entries);
    }


    /**
     * Swaps the specified rows in this matrix.
     *
     * <p>This method swaps the rows specified by the indices {@code row1} and {@code row2}
     * in this matrix. The indices are zero-based and can be negative, where negative indices
     * represent counting from the end of the rows.
     *
     * <p>This method is used to efficiently swap two rows of a matrix. It utilizes the
     * {@link System#arraycopy} method to perform the row swaps, which offers better performance
     * compared to using a <i>for-loop</i> or other methods such as {@link Arrays#copyOf}.
     *
     * <p>If either {@code row1} or {@code row2} is out of range, an {@link InvalidIndexException} will be thrown.
     *
     * @param  row1  The index of the first row to swap (accept negative indexing).
     * @param  row2  The index of the second row to swap (accept negative indexing).
     *
     * @return       A new {@link Matrix} object with the specified rows swapped.
     *
     * @throws InvalidIndexException  If either {@code row1} or {@code row2} index is out of range.
     *
     * @since  1.5.0
     * @see    #swapRows(Matrix, int, int)
     * @see    #swapColumns(int, int)
     */
    public Matrix swapRows(int row1, int row2) {
        return Matrix.swapRows(this, row1, row2);
    }

    /**
     * Swaps the specified rows in the given matrix.
     *
     * <p>This method swaps the rows specified by the indices {@code row1} and {@code row2}
     * in the provided matrix. The indices are zero-based and can be negative, where negative
     * indices represent counting from the end of the rows.
     *
     * <p>This method is used to efficiently swap two rows of a matrix. It utilizes the
     * {@link System#arraycopy} method to perform the row swaps, which offers better performance
     * compared to using a <i>for-loop</i> or other methods such as {@link java.util.Arrays#copyOf}.
     *
     * <p>If either {@code m} is {@code null}, or its entries are {@code null}, a {@link NullMatrixException} will be thrown.
     * If either {@code row1} or {@code row2} is out of range, an {@link InvalidIndexException} will be thrown.
     *
     * @param  m     The {@link Matrix} whose rows are to be swapped.
     * @param  row1  The index of the first row to swap (accept negative indexing).
     * @param  row2  The index of the second row to swap (accept negative indexing).
     *
     * @return       A new {@link Matrix} object with the specified rows swapped.
     *
     * @throws NullMatrixException    If the provided matrix or its entries are {@code null}.
     * @throws InvalidIndexException  If either {@code row1} or {@code row2} index is out of range.
     *
     * @since  1.5.0
     * @see    #swapRows(int, int)
     * @see    #swapColumns(Matrix, int, int)
     */
    public static Matrix swapRows(Matrix m, int row1, int row2) {
        MatrixChecker.requireNonNull(m,
            "Matrix is null. Please ensure the matrix are initialized."
        );

        double[][] entries = m.getEntries();
        double[] temp = new double[entries[row1].length];

        // Allow negative indexing
        row1 += (row1 < 0) ? entries.length : 0;
        row2 += (row2 < 0) ? entries.length : 0;

        if (row1 >= entries.length || row1 < 0) {
            raise(new InvalidIndexException(
                String.format("Given row index #1 is out of range: %d",
                    (row1 < 0) ? (row1 - entries.length) : row1
                )
            ));
        } else if (row2 >= entries.length || row2 < 0) {
            raise(new InvalidIndexException(
                String.format("Given row index #2 is out of range: %d",
                    (row2 < 0) ? (row2 - entries.length) : row2
                )
            ));
        }

        // We prefer use the `System.arraycopy` method instead of `Arrays.copyOf` or
        // use a very slow method, *for-loop*. It because the `System.arraycopy` offers
        // better performance and speed when compared to those methods, and also
        // it is a native call which does copy operation directly at memory.
        System.arraycopy(entries[row1], 0, temp, 0,
                         Math.min(entries[row1].length, temp.length));
        System.arraycopy(entries[row2], 0, entries[row1], 0,
                         Math.min(entries[row2].length, entries[row1].length));  // Copy `row2` to `row1`
        System.arraycopy(temp, 0, entries[row2], 0,
                         Math.min(temp.length, entries[row2].length));           // Copy the copy of `row1` to `row2`
        return new Matrix(entries);  // Return a new Matrix with the rows swapped
    }


    /**
     * Swaps the specified columns in this matrix.
     *
     * <p>This method swaps the columns specified by the indices {@code col1} and {@code col2} in this matrix.
     * The indices are zero-based and can be negative, where negative indices represent counting from the end of the columns.
     *
     * <p>The method modifies the entries of the matrix without any modification to the original matrix.
     * It swaps the elements of columns {@code col1} and {@code col2} directly without using a temporary variable,
     * relying on arithmetic operations to perform the swap with a single loop.
     *
     * <p>If either {@code col1} or {@code col2} is out of range, an {@link InvalidIndexException} will be thrown.
     *
     * @param  col1  The index of the first column to swap (accept negative indexing).
     * @param  col2  The index of the second column to swap (accept negative indexing).
     *
     * @return       A new {@link Matrix} object with the specified columns swapped.
     *
     * @throws InvalidIndexException  If either {@code col1} or {@code col2} is out of range.
     *
     * @since  1.5.0
     * @see    #swapColumns(Matrix, int, int)
     * @see    #swapRows(int, int)
     */
    public Matrix swapColumns(int col1, int col2) {
        return Matrix.swapColumns(this, col1, col2);
    }

    /**
     * Swaps the specified columns in the given matrix.
     *
     * <p>This method swaps the columns specified by the indices {@code col1} and {@code col2} in the provided matrix.
     * The indices are zero-based and can be negative, where negative indices represent counting from the end of the columns.
     *
     * <p>The method modifies the entries of the matrix without any modification to the original matrix.
     * It swaps the elements of columns {@code col1} and {@code col2} directly without using a temporary variable,
     * relying on arithmetic operations to perform the swap with a single loop.
     *
     * <p>If either {@code m} is {@code null}, or its entries are {@code null}, a {@link NullMatrixException} will be thrown.
     * If either {@code col1} or {@code col2} is out of range, an {@link InvalidIndexException} will be thrown.
     *
     * @param  m     The {@link Matrix} whose columns are to be swapped.
     * @param  col1  The index of the first column to be swapped.
     * @param  col2  The index of the second column to be swapped.
     *
     * @return       A new {@link Matrix} object with the specified columns swapped.
     *
     * @throws NullMatrixException    If the provided matrix or its entries are {@code null}.
     * @throws InvalidIndexException  If either {@code col1} or {@code col2} is out of range.
     *
     * @since  1.5.0
     * @see    #swapColumns(int, int)
     * @see    #swapRows(Matrix, int, int)
     */
    public static Matrix swapColumns(Matrix m, int col1, int col2) {
        MatrixChecker.requireNonNull(m,
            "Matrix is null. Please ensure the matrix are initialized."
        );

        double[][] entries = m.getEntries();

        // Allow negative indexing
        col1 += (col1 < 0) ? entries[0].length : 0;
        col2 += (col2 < 0) ? entries[0].length : 0;

        if (col1 >= entries.length || col1 < 0) {
            raise(new InvalidIndexException(
                String.format("Given column index #1 is out of range: %d",
                    (col1 < 0) ? (col1 - entries[col1].length) : col1
                )
            ));
        } else if (col2 >= entries.length || col2 < 0) {
            raise(new InvalidIndexException(
                String.format("Given column index #2 is out of range: %d",
                    (col2 < 0) ? (col2 - entries[col2].length) : col2
                )
            ));
        }

        for (int i = 0; i < entries.length; i++) {
            // Swap the elements of columns `col1` and `col2` directly without using
            // a temporary variable, but it relies on arithmetic operations to perform the swap.
            entries[i][col1] = entries[i][col1] + entries[i][col2];
            entries[i][col2] = entries[i][col1] - entries[i][col2];
            entries[i][col1] = entries[i][col1] - entries[i][col2];
        }

        return new Matrix(entries);  // Return a new Matrix with columns swapped
    }


    /**
     * Fills the column with specified array.
     *
     * <p>It can be an array or insert the values one by one.
     *
     * @param      values                    the values to be added into matrix column.
     *
     * @throws     IllegalArgumentException  if the given argument is overcapacity to matrix column
     *                                       or not enough argument to fill the column.
     * @throws     com.mitsuki.jmatrix.exception.MatrixArrayFullException  if the matrix cannot be added more values.
     * @throws     NullMatrixException       if this matrix is a {@code null} object.
     *
     * @since                                0.1.0
     * @see                                  #add(double)
     *
     * @deprecated                           Highly inefficient method to create a matrix array.
     *                                       It is very recommended to use {@link #Matrix(double[][])}
     *                                       or {@link #create(double[][])} instead.
     */
    @Deprecated
    public void add(double ... values) {
        try {
            // Throw "NullMatrixException" if this matrix array is null
            if (this.ENTRIES == null) {
                throw new NullMatrixException(
                    "Cannot add values, becuase this matrix is null");
            }
            else if (this.index >= this.ROWS) {
                throw new com.mitsuki.jmatrix.exception.MatrixArrayFullException(
                    "Cannot add values anymore, Matrix is already full");
            }
            // Length of values list shouldn't greater than total matrix columns
            else if (values.length > this.COLS) {
                throw new IllegalArgumentException(
                    "Too many arguments for matrix with columns " + this.COLS);
            }
            // And shouldn't be less than total matrix columns
            else if (values.length < this.COLS) {
                throw new IllegalArgumentException(
                    "Not enough argument for matrix with columns " + this.COLS);
            }
        } catch (final IllegalArgumentException iae) {
            try {
                throw new JMatrixBaseException(iae);
            } catch (final JMatrixBaseException jme) {
                raise(jme);
            }
        } catch (final RuntimeException re) {
            raise(re);
        }

        // Iterate values list and fill elements of matrix array
        int i = 0;
        for (double val : values) {
            this.ENTRIES[this.index][i++] = val;
        }

        this.index++; // increment index of matrix row
    }


    /**
     * Fills the column with repeated of specified value.
     *
     * @param      value                     the value to filled out the matrix column.
     *
     * @throws     com.mitsuki.jmatrix.exception.MatrixArrayFullException  if the matrix cannot be added more values.
     * @throws     NullMatrixException       if this matrix is {@code null}.
     *
     * @since                                0.1.0
     * @see                                  #add(double ...)
     *
     * @deprecated                           Highly inefficient method to create a matrix array.
     *                                       It is very recommended to use {@link #Matrix(double[][])}
     *                                       or {@link #create(double[][])} instead.
     */
    @Deprecated
    public void add(double value) {
        try {
            // Throw "NullMatrixException" when matrix array is null
            if (this.ENTRIES == null) {
                throw new NullMatrixException(
                    "Matrix array is null, cannot add some values");
            }
            /** ---
            - Throw "MatrixArrayFullException" while attempt to add
                a new column, but the index has equal to total matrix rows
            --- **/
            else if (this.index >= this.ROWS) {
                throw new com.mitsuki.jmatrix.exception.MatrixArrayFullException(
                    "Cannot add values anymore, Matrix is already full");
            }
        } catch (final RuntimeException re) {
            raise(re);
        }

        // Creates list of repeated value
        /** ---
        - Example: call add() function with single Integer
            with the value 8, then it would create array:

        >> [8, 8, 8, ...]

        - Then created array would be pushed to matrix array
        --- **/
        double[ ] values = new double[this.COLS]; // create list with size equal to column length
        for (int i = 0; i < values.length; i++) {
            values[i] = value;
        }

        // Iterate values list and fill elements of matrix array
        int i = 0;
        for (double val : values) {
            this.ENTRIES[this.index][i++] = val;
        }

        this.index++; // increment index row
    }



    /*=========================================
    ::
    ::  PRIVATE METHODS
    ::
    =========================================*/


    /**
     * Calculates the dot product of a specified cell in two arrays.
     *
     * <p>This method is used for matrix multiplication operation.
     *
     * @param  a    the first array.
     * @param  b    the second array.
     * @param  row  the row index of the cell to be calculated.
     * @param  col  the column index of the cell to be calculated.
     *
     * @return      the dot product of the specified cell of two arrays.
     *
     * @since       0.2.0
     * @see         #mult(Matrix)
     * @see         #mult(double[][])
     */
    private static double multCell(double[ ][ ] a, double[ ][ ] b, int row, int col) {
        double result = 0;
        for (int i = 0; i < b.length; i++) {
            result += (a[row][i] * b[i][col]);
        }

        return result;
    }



    /*=========================================
    ::
    ::  MATRIX OPERATIONS
    ::
    =========================================*/


    /*--------------------------
    ::     Matrix Addition
    --------------------------*/


    /**
     * Operates addition for this matrix and the given matrix.
     *
     * <p>Both operands should be same dimensions or sizes before performing addition.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] entriesA = {
     *       { 1, 2 },
     *       { 3, 4 }
     *   };
     *
     *   double[][] entriesB = {
     *       { 5, 6 },
     *       { 7, 8 }
     *   };
     *
     *   Matrix m = new Matrix(entriesA);
     *   Matrix n = new Matrix(entriesB);
     *
     *   m.sum(n);
     *   m.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [6.0, 8.0],
     *       [10.0, 12.0]   ]
     * </pre>
     *
     * @param  m                           the <b>Matrix</b> object as addend.
     *
     * @throws IllegalMatrixSizeException  if the two operands are not same dimensions.
     * @throws NullMatrixException         if one or both matrices is {@code null}
     *                                     or its entries is {@code null}.
     *
     * @since                              0.1.0
     * @see                                #sum(double[][])
     * @see                                #sum(double[][], double[][])
     * @see                                #sum(Matrix, Matrix)
     */
    public void sum(Matrix m) {
        // Throw "NullMatrixException" if the matrix is null
        MatrixChecker.requireNonNull(this,
            "This matrix is null. " +
            "Please ensure the matrix are initialized before performing addition"
        );
        MatrixChecker.requireNonNull(m,
            "Given matrix is null. " +
            "Please ensure the matrix are initialized before performing addition"
        );

        int[] thisShape = this.shape();
        int[] mShape = m.shape();

        // Throw "IllegalMatrixSizeException" if the matrices size are not same
        if (thisShape[0] != mShape[0] || thisShape[1] != mShape[1]) {
            raise(new IllegalMatrixSizeException(
                String.format(
                    "Cannot perform addition for two matrices with different dimensions. " +
                    "A = %dx%d, B = %dx%d",
                    thisShape[0], thisShape[1], mShape[0], mShape[1]
                )
            ));
        }

        // Create new matrix for the result
        double[ ][ ] result = new double[this.ROWS][m.COLS];

        // Iterate over each element of the matrices and add the corresponding values together
        for (int r = 0; r < this.ROWS; r++) {
            for (int c = 0; c < this.COLS; c++) {
                result[r][c] = this.ENTRIES[r][c] + m.ENTRIES[r][c];
            }
        }

        this.ENTRIES = result; // copy the result to this matrix
    }


    /**
     * Operates addition for this matrix and the given two-dimensional array.
     *
     * <p>Both operands should be same dimensions or sizes before performing addition.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] entriesA = {
     *       { 1, 2 },
     *       { 3, 4 }
     *   };
     *
     *   double[][] entriesB = {
     *       { 1, 3 },
     *       { 7, 12 }
     *   };
     *
     *   Matrix m = new Matrix(entriesA);
     *
     *   m.sum(entriesB);
     *   m.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [2.0, 5.0],
     *       [10.0, 16.0]   ]
     * </pre>
     *
     * @param  arr                         the two-dimensional array as addend.
     *
     * @throws IllegalMatrixSizeException  if the two operands are not same dimensions.
     * @throws NullMatrixException         if the entries of this matrix is {@code null}
     *                                     or if the given array is {@code null} or empty.
     *
     * @since                              0.1.0
     * @see                                #sum(Matrix)
     * @see                                #sum(double[][], double[][])
     * @see                                #sum(Matrix, Matrix)
     */
    public void sum(double[ ][ ] arr) {
        // Throw "NullMatrixException" if entries of this matrix is null
        // or if the given two-dimensional array is null or empty
        if (this.ENTRIES == null) {
            cause = new NullMatrixException(
                "This matrix is null. " +
                "Please ensure the matrix are initialized before performing addition."
            );
        } else if (arr == null || arr.length == 0) {
            cause = new NullMatrixException(
                "Given array is null. " +
                "Please ensure the array has valid elements."
            );
        }
        // Else throw "IllegalMatrixSizeException" if the matrices are not same dimensions
        else if (this.ROWS != arr.length || this.COLS != arr[0].length) {
            cause = new IllegalMatrixSizeException(
                String.format(
                    "Cannot perform addition for two matrices with different dimensions. " +
                    "A = %dx%d, B = %dx%d",
                    this.ROWS, this.COLS, arr.length, arr[0].length
                )
            );
        }

        // Throw the exception if got one
        if (cause != null) raise(cause);

        // Create new matrix for the result
        double[ ][ ] result = new double[this.ROWS][arr[0].length];

        // Using nested loop for iterate over each element of matrix
        for (int r = 0; r < this.ROWS; r++) {
            for (int c = 0; c < this.COLS; c++) {
                result[r][c] = this.ENTRIES[r][c] + arr[r][c];
            }
        }

        this.ENTRIES = result; // copy the result to Matrix
    }


    /**
     * Operates addition for two two-dimensional arrays from input parameters and
     * produces new two-dimensional array contains the sum of given two arrays.
     *
     * <p>Both operands should be same dimensions or sizes before performing addition.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] entriesA = {
     *       { 4, 4 },
     *       { 3, 3 }
     *   };
     *
     *   double[][] entriesB = {
     *       { 2, 2 },
     *       { 1, 1 }
     *   };
     *
     *   double[][] result = Matrix.sum(
     *       entriesA, entriesB);
     *
     *   Matrix.display(result);
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [6.0, 6.0],
     *       [4.0, 4.0]   ]
     * </pre>
     *
     * @param  a                           the first two-dimensional array as addend.
     * @param  b                           the second two-dimensional array as addend.
     *
     * @return                             the two-dimensional array which contains the sum of two arrays.
     *
     * @throws IllegalMatrixSizeException  if the two operands are not same dimensions.
     * @throws NullMatrixException         if one or both given arrays is {@code null} or empty.
     *
     * @since                              0.2.0
     * @see                                #sum(Matrix)
     * @see                                #sum(double[][])
     * @see                                #sum(Matrix, Matrix)
     */
    public static double[ ][ ] sum(double[ ][ ] a, double[ ][ ] b) {
        // Throw "NullMatrixException" if the array is null or empty
        if (a == null || a.length == 0) {
            cause = new NullMatrixException(
                "Given array A is null. " +
                "Please ensure the array has valid elements."
            );
        } else if (b == null || b.length == 0) {
            cause = new NullMatrixException(
                "Given array B is null. " +
                "Please ensure the array has valid elements."
            );
        }
        // Else throw "IllegalMatrixSizeException" if the both arrays
        // are not same dimensions
        else if (a.length != b.length || a[0].length != b[0].length) {
            cause = new IllegalMatrixSizeException(
                String.format(
                    "Cannot perform addition for two matrices with different dimensions. " +
                    "A = %dx%d, B = %dx%d",
                    a.length, a[0].length, b.length, b[0].length
                )
            );
        }

        // Throw the exception if got one
        if (cause != null) raise(cause);

        // Create a new array for the result
        double[ ][ ] result = new double[a.length][b[0].length];

        for (int r = 0; r < a.length; r++) {
            for (int c = 0; c < b[0].length; c++) {
                result[r][c] = a[r][c] + b[r][c];
            }
        }

        return result;
    }


    /**
     * Operates addition for two matrices from input parameters and
     * produces new <b>Matrix</b> object contains the sum of given two matrices.
     *
     * <p>Both operands should be same dimensions or sizes before performing addition.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] a = {
     *       { 12, 0 },
     *       { 3, -4 }
     *   };
     *
     *   double[][] b = {
     *       { -7, 5 },
     *       { 2, 9 }
     *   };
     *
     *   Matrix m = new Matrix(a);
     *   Matrix n = new Matrix(b);
     *
     *   Matrix result = Matrix.sum(m, n);
     *   result.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [5.0, 5.0],
     *       [5.0, 5.0]   ]
     * </pre>
     *
     * @param  a                           the first <b>Matrix</b> object as addend.
     * @param  b                           the second <b>Matrix</b> object as addend.
     *
     * @return                             the <b>Matrix</b> object which
     *                                     contains the sum of two matrices.
     *
     * @throws IllegalMatrixSizeException  if the two operands are not same dimensions.
     * @throws NullMatrixException         if either object or entries of one or both matrices is {@code null}.
     *
     * @since                              0.2.0
     * @see                                #sum(Matrix)
     * @see                                #sum(double[][])
     * @see                                #sum(double[][], double[][])
     */
    public static Matrix sum(Matrix a, Matrix b) {
        // Throw "NullMatrixException" if the entries of given matrix is null
        if (a == null || a.getEntries() == null) {
            cause = new NullMatrixException(
                "Given matrix A is null. " +
                "Please ensure the matrix are initialized before performing addition."
            );
        } else if (b == null || b.getEntries() == null) {
            cause = new NullMatrixException(
                "Given matrix B is null. " +
                "Please ensure the matrix are initialized before performing addition."
            );
        }
        // Else throw "IllegalMatrixSizeException" if both matrices
        // are not same dimensions
        else if (a.getSize()[0] != b.getSize()[0] || a.getSize()[1] != b.getSize()[1]) {
            cause = new IllegalMatrixSizeException(
                String.format(
                    "Cannot perform addition for two matrices with different dimensions. " +
                    "A = %dx%d, B = %dx%d",
                    a.getSize()[0], a.getSize()[1], b.getSize()[0], b.getSize()[1]
                )
            );
        }

        // Throw the exception if got one
        if (cause != null) raise(cause);

        // Create new matrix object
        Matrix matrixRes = new Matrix(a.getSize()[0], b.getSize()[1]);

        for (int r = 0; r < a.getSize()[0]; r++) {
            for (int c = 0; c < b.getSize()[1]; c++) {
                matrixRes.ENTRIES[r][c] = a.get(r, c) + b.get(r, c);
            }
        }

        return matrixRes;
    }




    /*--------------------------
    ::    Matrix Subtraction
    --------------------------*/


    /**
     * Operates subtraction for this matrix and the given matrix.
     *
     * <p>Both operands should be same dimensions or sizes before performing subtraction.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] a = {
     *       { 1, 3, 5 },
     *       { 7, 9, 11 }
     *   };
     *
     *   Matrix m = new Matrix(a);
     *   Matrix n = new Matrix(2, 3, 5);
     *
     *   m.sub(n);
     *   m.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [-4.0, -2.0, 0.0],
     *       [2.0, 4.0, 6.0]   ]
     * </pre>
     *
     * @param  m                           the <b>Matrix</b> object as subtrahend.
     *
     * @throws IllegalMatrixSizeException  if the two operands are not same dimensions.
     * @throws NullMatrixException         if either object or entries of one or both matrices
     *                                     is {@code null}.
     *
     * @since                              0.1.0
     * @see                                #sub(double[][])
     * @see                                #sub(double[][], double[][])
     * @see                                #sub(Matrix, Matrix)
     */
    public void sub(Matrix m) {
        // Throw "NullMatrixException" if either object or entries
        // of this matrix or given matrix is null
        if (this.ENTRIES == null) {
            cause = new NullMatrixException(
                "This matrix is null. " +
                "Please ensure the matrix are initialized before performing subtraction."
            );
        } else if (m == null || m.ENTRIES == null) {
            cause = new NullMatrixException(
                "Given matrix is null. " +
                "Please ensure the matrix are initialized before performing subtraction."
            );
        }
        // Else throw "IllegalMatrixSizeException" if both matrices are not same size
        else if (this.ROWS != m.ROWS || this.COLS != m.COLS) {
            cause = new IllegalMatrixSizeException(
                String.format(
                    "Cannot perform subtraction for two matrices with different dimensions. " +
                    "A = %dx%d, B = %dx%d",
                    this.ROWS, this.COLS, m.ROWS, m.COLS
                )
            );
        }

        // Throw the exception if got one
        if (cause != null) raise(cause);

        // Create new matrix for the result
        double[ ][ ] result = new double[this.ROWS][m.COLS];

        // Iterate over each element of all matrices and subtract each values together
        for (int r = 0; r < this.ROWS; r++) {
            for (int c = 0; c < this.COLS; c++) {
                result[r][c] = this.ENTRIES[r][c] - m.ENTRIES[r][c];
            }
        }

        this.ENTRIES = result; // copy the result to this matrix
    }


    /**
     * Operates subtraction for this matrix and the given two-dimensional array.
     *
     * <p>Both operands should be same dimemsions or sizes before performing subtraction.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] a = {
     *       { 1 ,2, 3 },
     *       { 4, 5, 6 }
     *   };
     *
     *   Matrix m = new Matrix(a);
     *   Matrix n = new Matrix(2, 3, 2);
     *
     *   m.sub(n);
     *   m.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [-1.0, 0.0, 1.0],
     *       [2.0, 3.0, 4.0]   ]
     * </pre>
     *
     * @param  arr                         the two-dimensional array as subtrahend.
     *
     * @throws IllegalMatrixSizeException  if the two operands are not same dimensions.
     * @throws NullMatrixException         if the entries of this matrix is {@code null}
     *                                     or if the given array is {@code null} or empty.
     *
     * @since                              0.1.0
     * @see                                #sub(Matrix)
     * @see                                #sub(double[][], double[][])
     * @see                                #sub(Matrix, Matrix)
     */
    public void sub(double[ ][ ] arr) {
        // Throw "NullMatrixException" if entries of this matrix is null
        // or the given two-dimensional array is null or empty
        if (this.ENTRIES == null) {
            cause = new NullMatrixException(
                "This matrix is null. " +
                "Please ensure the matrix are initialized before performing subtraction."
            );
        } else if (arr == null || arr.length == 0) {
            cause = new NullMatrixException(
                "Given array is null. " +
                "Please ensure the array has valid elements before performing subtraction."
            );
        }
        // Else throw "IllegalMatrixSizeException" if the matrices are not same size
        else if (this.ROWS != arr.length || this.COLS != arr[0].length) {
            cause = new IllegalMatrixSizeException(
                String.format(
                    "Cannot perform subtraction for two matrices with different dimensions. " +
                    "A = %dx%d, B = %dx%d",
                    this.ROWS, this.COLS, arr.length, arr[0].length
                )
            );
        }

        // Throw the exception if got one
        if (cause != null) raise(cause);

        // Create new matrix for the result
        double[ ][ ] result = new double[this.ROWS][arr[0].length];

        // Iterate over each element of all matrices and subtract each values together
        for (int r = 0; r < this.ROWS; r++) {
            for (int c = 0; c < this.COLS; c++) {
                result[r][c] = this.ENTRIES[r][c] - arr[r][c];
            }
        }

        this.ENTRIES = result; // copy the result to Matrix
    }


    /**
     * Operates subtraction for two two-dimensional arrays from input parameters and
     * produces new two-dimensional array contains the difference of given two arrays.
     *
     * <p>Both operands should be same dimensions or sizes before performing subtraction.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] a = {
     *       { 5, 6, 7 },
     *       { 0, -2, 3 },
     *       { 1, 0, 9 }
     *   };
     *
     *   double[][] b = {
     *       { 3, 0, 4 },
     *       { 0, 4, 6 },
     *       { 1, 2, 5 }
     *   };
     *
     *   double[][] res = Matrix.sub(a, b);
     *   Matrix.display(res);
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [2.0, 6.0, 3.0],
     *       [0.0, -6.0, -3.0],
     *       [0.0, -2.0, 4.0]   ]
     * </pre>
     *
     * @param  a                           the first two-dimensional array as minuend.
     * @param  b                           the second two-dimensional array as subtrahend.
     *
     * @return                             a two-dimensional array which contains the difference of two arrays.
     *
     * @throws IllegalMatrixSizeException  if the two operands are not same dimensions.
     * @throws NullMatrixException         if one or both given arrays is {@code null} or empty.
     *
     * @since                              0.2.0
     * @see                                #sub(Matrix)
     * @see                                #sub(double[][])
     * @see                                #sub(Matrix, Matrix)
     */
    public static double[ ][ ] sub(double[ ][ ] a, double[ ][ ] b) {
        // Throw "NullMatrixException" if one or both arrays is null
        if (a == null || a.length == 0) {
            cause = new NullMatrixException(
                "Given array A is null. " +
                "Please ensure the array has valid elements before performing subtraction."
            );
        } else if (b == null || b.length == 0) {
            cause = new NullMatrixException(
                "Given array B is null. " +
                "Please ensure the array has valid elements before performing subtraction."
            );
        }
        // Else throw "IllegalMatrixSizeException" if the both matrix and are not same size
        else if (a.length != b.length || a[0].length != b[0].length) {
            cause = new IllegalMatrixSizeException(
                String.format(
                    "Cannot perform subtraction for two matrices with different dimensions. " +
                    "A = %dx%d, B = %dx%d",
                    a.length, a[0].length, b.length, b[0].length
                )
            );
        }

        if (cause != null) raise(cause);

        // Create a new matrix array
        double[ ][ ] result = new double[a.length][b[0].length];

        for (int r = 0; r < a.length; r++) {
            for (int c = 0; c < b[0].length; c++) {
                result[r][c] = a[r][c] - b[r][c];
            }
        }

        return result;
    }


    /**
     * Operates subtraction for two matrices from input parameters and
     * produces new <b>Matrix</b> object contains the difference of given two matrices.
     *
     * <p>Both matrices should be same dimensions or sizes before performing subtraction.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] a = {
     *       { 1, 1, 1 },
     *       { 2, 2, 2 },
     *       { 1, 0, 9 }
     *   };
     *
     *   double[][] b = {
     *       { 3, 0, 4 },
     *       { 0, 4, 6 },
     *       { 1, 2, 5 }
     *   };
     *
     *   double[][] result = Matrix.sub(a, b);
     *   Matrix.display(result);
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [-2.0, 1.0, -3.0],
     *       [2.0, -2.0, -4.0],
     *       [0.0, -2.0, 4.0]   ]
     * </pre>
     *
     * @param  a                           the first <b>Matrix</b> object as minuend.
     * @param  b                           the second <b>Matrix</b> object as subtrahend.
     *
     * @return                             the <b>Matrix</b> object which contains the difference of two matrices.
     *
     * @throws IllegalMatrixSizeException  if the two operands are not same dimensions.
     * @throws NullMatrixException         if the entries of given matrix is {@code null}.
     *
     * @since                              0.2.0
     * @see                                #sub(Matrix)
     * @see                                #sub(double[][])
     * @see                                #sub(double[][], double[][])
     */
    public static Matrix sub(Matrix a, Matrix b) {
        // Throw "NullMatrixException" if both matrices is null
        if (a == null || a.getEntries() == null) {
            cause = new NullMatrixException(
                "Given matrix A is null. " +
                "Please ensure the matrix are initialized before performing subtraction."
            );
        } else if (b == null || b.getEntries() == null) {
            cause = new NullMatrixException(
                "Given matrix B is null. " +
                "Please ensure the matrix are initialized before performing subtraction."
            );
        }
        // Else throw "IllegalMatrixSizeException" if both matrices size are not same
        else if (a.getSize()[0] != b.getSize()[0] || a.getSize()[1] != b.getSize()[1]) {
            cause = new IllegalMatrixSizeException(
                String.format(
                    "Cannot perform subtraction for two matrices with different dimensions. " +
                    "A = %dx%d, B = %dx%d",
                    a.getSize()[0], a.getSize()[1], b.getSize()[0], b.getSize()[1]
                )
            );
        }

        if (cause != null) raise(cause);

        // Create new matrix object
        Matrix matrixRes = new Matrix(a.getSize()[0], b.getSize()[1]);

        for (int r = 0; r < a.getSize()[0]; r++) {
            for (int c = 0; c < b.getSize()[1]; c++) {
                matrixRes.ENTRIES[r][c] = a.get(r, c) - b.get(r, c);
            }
        }

        return matrixRes;
    }




    /*---------------------------
    ::   Matrix Multiplication
    ---------------------------*/


    /**
     * Operates multiplication for this matrix and the given matrix.
     *
     * <p>The number of columns in this matrix and the number of rows in
     * given matrix should be same before performing multiplication.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] x = {
     *       { 3, 7 },
     *       { 1, 5 }
     *   };
     *
     *   double[][] y = {
     *       { 12, 4 },
     *       { 7, 3 }
     *   };
     *
     *   Matrix m = new Matrix(x);
     *   Matrix n = new Matrix(y);
     *
     *   m.mult(n);
     *   m.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [85.0, 33.0],
     *       [47.0, 19.0]   ]
     * </pre>
     *
     * @param  m                           the <b>Matrix</b> object as multiplicand.
     *
     * @throws IllegalMatrixSizeException  if the number of columns in this matrix
     *                                     is different from the number of rows in given matrix.
     * @throws NullMatrixException         if the entries of this matrix or
     *                                     the given matrix is {@code null}.
     *
     * @since                              0.2.0
     * @see                                #mult(double[][])
     * @see                                #mult(double[][], double[][])
     * @see                                #mult(Matrix, Matrix)
     */
    public void mult(Matrix m) {
        // Throw "NullMatrixException" if this matrix or given Matrix is null
        if (this.ENTRIES == null) {
            cause = new NullMatrixException(
                "This matrix is null. " +
                "Please ensure the matrix are initialized before performing multiplication."
            );
        } else if (m == null || m.ENTRIES == null) {
            cause = new NullMatrixException(
                "Given matrix is null. " +
                "Please ensure the matrix are initialized before performing multiplication."
            );
        }
        /* Throw exception if the number of columns this matrix are
         * not the same as the number of rows in given matrix
         */
        else if (this.COLS != m.ROWS) {
            cause = new IllegalMatrixSizeException(
                "The number of columns in this matrix is different " +
                "from the number of rows in the given matrix"
            );
        }

        // Throw the exception if got one
        if (cause != null) raise(cause);

        // Create new matrix array
        double[ ][ ] result = new double[this.ROWS][m.COLS];

        // Iterate and multiply each element
        for (int r = 0; r < result.length; r++) {
            for (int c = 0; c < result[r].length; c++) {
                result[r][c] = multCell(this.ENTRIES, m.ENTRIES, r, c);
            }
        }

        this.ENTRIES = result;
    }


    /**
     * Operates multiplication for this matrix and the given two-dimensional array.
     *
     * <p>The number of columns in this matrix and the number of rows in
     * given two-dimensional array should be same before performing multiplication.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] x = {
     *       { 9, -2 },
     *       { 0, 5 },
     *       {-8, 10 }
     *   };
     *
     *   double[][] y = {
     *       { 6, 2, 2 },
     *       { 8, 1, 4 }
     *   };
     *
     *   Matrix m = new Matrix(x);
     *
     *   m.mult(y);
     *   m.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [38.0, 16.0],
     *       [40.0, 5.0],
     *       [32.0, -6.0]   ]
     * </pre>
     *
     * @param  a                           the two-dimensional array as multiplicand.
     *
     * @throws IllegalMatrixSizeException  if the number of columns in this matrix
     *                                     is different from the number of rows in given two-dimensional array.
     * @throws NullMatrixException         if the entries of this matrix is {@code null}
     *                                     or the given array is {@code null} or empty.
     *
     * @since                              0.2.0
     * @see                                #mult(Matrix)
     * @see                                #mult(double[][], double[][])
     * @see                                #mult(Matrix, Matrix)
     */
    public void mult(double[ ][ ] a) {
        // Throw "NullMatrixException" if this matrix or given Matrix is null
        if (this.ENTRIES == null) {
            cause = new NullMatrixException(
                "This matrix is null. " +
                "Please ensure the matrix are initialized before performing multiplication."
            );
        } else if (a == null || a.length == 0) {
            cause = new NullMatrixException(
                "Given array is null. " +
                "Please ensure the array has valid elements before performing multiplication."
            );
        }
        /* Throw exception if the number of columns this matrix are
         * not the same as the number of rows in given two-dimensional array
         */
        else if (this.COLS != a.length) {
            cause = new IllegalMatrixSizeException(
                "The number of columns in this matrix is different " +
                "from the number of rows in the given array"
            );
        }

        // Throw the exception if got one
        if (cause != null) raise(cause);

        // Create new matrix array
        double[ ][ ] result = new double[this.ROWS][a[0].length];

        // Iterate and multiply each element
        for (int r = 0; r < result.length; r++) {
            for (int c = 0; c < result[r].length; c++) {
                result[r][c] = multCell(this.ENTRIES, a, r, c);
            }
        }

        this.ENTRIES = result;
    }


    /**
     * Operates matrix multiplication for two two-dimensional arrays from input parameters
     * and produces new two-dimensional array contains the product of given two arrays.
     *
     * <p>The number of columns in first array and number of rows in
     * second array should be same before performing multiplication.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] x = {
     *       { 1, 1, 1 },
     *       { 2, 2, 2 }
     *   };
     *
     *   double[][] y = {
     *       { 3, 4 },
     *       { 3, 4 },
     *       { 3, 4 }
     *   };
     *
     *   double[][] res = Matrix.mult(x, y);
     *   Matrix.display(res);
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [9.0, 12.0],
     *       [18.0, 24.0]   ]
     * </pre>
     *
     * @param  a                           the first two-dimensional array as multiplier.
     * @param  b                           the second two-dimensional array as multiplicand.
     *
     * @return                             the two-dimensional array which contains the product of two arrays.
     *
     * @throws IllegalMatrixSizeException  if the number of columns in first array
     *                                     is different from the number of rows in second array.
     * @throws NullMatrixException         if the given two-dimensional array is {@code null} or empty.
     *
     * @since                              0.2.0
     * @see                                #mult(Matrix)
     * @see                                #mult(double[][])
     * @see                                #mult(Matrix, Matrix)
     */
    public static double[ ][ ] mult(double[ ][ ] a, double[ ][ ] b) {
        // Throw exception if the given arrays is null
        if (a == null || a.length == 0) {
            cause = new NullMatrixException(
                "Given array A is null. " +
                "Please ensure the array has valid elements before performing multiplication."
            );
        } else if (b == null || b.length == 0) {
            cause = new NullMatrixException(
                "Given array B is null. " +
                "Please ensure the array has valid elements before performing multiplication."
            );
        }
        /* Throw exception if the number of columns in array A are
         * not the same as the number of rows in array B
         */
        else if (a[0].length != b.length) {
            cause = new IllegalMatrixSizeException(
                "The number of columns in the first array is different " +
                "from the number of rows in the second array."
            );
        }

        // Throw the exception if got one
        if (cause != null) raise(cause);

        double[ ][ ] result = new double[a.length][b[0].length];

        for (int r = 0; r < result.length; r++) {
            for (int c = 0; c < result[r].length; c++) {
                result[r][c] = multCell(a, b, r, c);
            }
        }

        return result;
    }


    /**
     * Operates matrix multiplication for two matrices from input parameters
     * and produces new matrix contains the product of given two matrices.
     *
     * <p>The number of columns in first matrix and number of rows in
     * second matrix should be same before performing multiplication.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] x = {
     *       { 1, 1, 1 },
     *       { 2, 2, 2 }
     *   };
     *
     *   double[][] y = {
     *       { 3, 4 },
     *       { 3, 4 },
     *       { 3, 4 }
     *   };
     *
     *   Matrix m = new Matrix(x);
     *   Matrix n = new Matrix(y);
     *
     *   Matrix res = Matrix.mult(m, n);
     *   res.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [9.0, 12.0],
     *       [18.0, 24.0]   ]
     * </pre>
     *
     * @param  a                           the first <b>Matrix</b> object as multiplier.
     * @param  b                           the second <b>Matrix</b> object as multiplicand.
     *
     * @return                             the <b>Matrix</b> object which contains the product of two matrices.
     *
     * @throws IllegalMatrixSizeException  if the number of columns in first matrix
     *                                     is different from the number of rows in second matrix.
     * @throws NullMatrixException         if the entries of given matrix is {@code null}.
     *
     * @since                              0.2.0
     * @see                                #mult(Matrix)
     * @see                                #mult(double[][])
     * @see                                #mult(double[][], double[][])
     */
    public static Matrix mult(Matrix a, Matrix b) {
        // Throw "NullMatrixException" if given Matrix is null
        if (a == null || a.getEntries() == null) {
            cause = new NullMatrixException(
                "Given matrix A is null. " +
                "Please ensure the matrix are initialized before performing multiplication."
            );
        } else if (b == null || b.getEntries() == null) {
            cause = new NullMatrixException(
                "Given matrix B is null. " +
                "Please ensure the matrix are initialized before performing multiplication."
            );
        }
        /* Throw exception if the number of columns in matrix A are
         * not the same as the number of rows in matrix B
         */
        else if (a.getSize()[1] != b.getSize()[0]) {
            cause = new IllegalMatrixSizeException(
                "The number of columns in the first matrix is different " +
                "from the number of rows in the second matrix."
            );
        }

        // Throw the exception if got one
        if (cause != null) raise(cause);

        // Create new matrix object
        Matrix result = new Matrix(a.getSize()[0], b.getSize()[1]);

        for (int r = 0; r < result.getSize()[0]; r++) {
            for (int c = 0; c < result.getSize()[1]; c++) {
                result.ENTRIES[r][c] = multCell(a.getEntries(), b.getEntries(), r, c);
            }
        }

        return result;
    }



    /*---------------------------
    ::   Scalar Multiplication
    ---------------------------*/


    /**
     * Operates scalar multiplication for this matrix.
     *
     * <p>Multiplies all entries of this matrix by a specified scalar value.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   // Create 3x3 identity matrix
     *   Matrix m = Matrix.identity(3);
     *
     *   m.mult(10);
     *   m.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [10.0, 0.0, 0.0],
     *       [0.0, 10.0, 0.0],
     *       [0.0, 0.0, 10.0]   ]
     * </pre>
     *
     * @param  x                    the scalar value to multiply each entry of this matrix.
     *
     * @throws NullMatrixException  if the entries of this matrix is {@code null}.
     *
     * @since                       1.0.0b.7
     * @see                         #mult(Matrix, double)
     * @see                         #mult(Matrix)
     */
    public void mult(double x) {
        // Throw the exception immediately if this matrix has null entries
        if (this.ENTRIES == null) {
            raise(new NullMatrixException(
                "This matrix is null. " +
                "Please ensure the matrix are initialized before performing scalar multiplication."
            ));
        }

        this.create( Matrix.mult(this, x).getEntries() );
    }


    /**
     * Operates scalar multiplication for the given matrix and produces
     * a new scalar matrix.
     *
     * <p>Multiplies all entries of the given <b>Matrix</b> object by a specified scalar value.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   // Create 4x4 identity matrix
     *   Matrix m = Matrix.identity(4);
     *
     *   Matrix res = Matrix.mult(m, 3);
     *   res.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [3.0, 0.0, 0.0, 0.0],
     *       [0.0, 3.0, 0.0, 0.0],
     *       [0.0, 0.0, 3.0, 0.0],
     *       [0.0, 0.0, 0.0, 3.0]   ]
     * </pre>
     *
     * @param  m                    the <b>Matrix</b> object to multiply.
     * @param  x                    the scalar value to multiply each entry of given matrix.
     *
     * @return                      a new <b>Matrix</b> object with entries equal
     *                              to the original matrix entries multiplied by {@code x}.
     *
     * @throws NullMatrixException  if the entries of given matrix is {@code null}.
     *
     * @since                       1.0.0b.7
     * @see                         #mult(double)
     * @see                         #mult(Matrix, Matrix)
     */
    public static Matrix mult(Matrix m, double x) {
        // Throw the exception immediately if given matrix has null entries
        if (m == null || m.getEntries() == null) {
            raise(new NullMatrixException(
                "Given matrix is null. " +
                "Please ensure the matrix are initialized before performing scalar multiplication."
            ));
        }

        double[ ][ ] result = new double[m.getSize()[0]][m.getSize()[1]];

        for (int row = 0; row < m.getSize()[0]; row++) {
            for (int col = 0; col < m.getSize()[1]; col++) {
                result[row][col] = m.get(row, col) * x;
            }
        }

        return new Matrix(result);
    }



    /*---------------------------
    ::   Matrix Transposition
    ---------------------------*/


    /**
     * Performs transposition for this matrix.
     *
     * <p>If this matrix type are not square, then it would switches the row and column
     * indices of this matrix. Which means the matrix size would be switched
     * (for example, {@code 2x4 -> 4x2}).
     *
     * <p>The transposed matrix always get denoted by upperscript <b>T</b>, <b>t</b> or <b>tr</b>,
     * for example:
     * <ul>
     * <li> <b>A</b><sup>T</sup>
     * <li> <b>A</b><sup>t</sup>
     * <li> <b>A</b><sup>tr</sup>
     * </ul>
     *
     * <p><b>Example code:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = new Matrix(
     *       new double[][] {
     *           { 1, 7, 8 },
     *           { 3, 0, 2 }
     *       }
     *   );
     *
     *   // Perform the tranposition
     *   m.transpose();
     *   m.display();
     * </code></pre>
     *
     * <p>This code would transpose the matrix {@code m}, which means
     * the size would be switched. Here is the output:
     *
     * <pre>&nbsp;
     *   [   [1.0, 3.0],
     *       [7.0, 0.0],
     *       [8.0, 2.0]   ]
     * </pre>
     *
     * @apiNote
     * <p>Repeating the process on the transposed matrix returns
     * the elements to their original position. Also can be written
     * like this, <code><b>(A</b><sup>T</sup>)<sup>T</sup></code>.
     *
     * @throws NullMatrixException  if the entries of this matrix is {@code null}.
     *
     * @since                       0.2.0
     * @see                         #transpose(Matrix)
     * @see                         #transpose(double[][])
     */
    public void transpose() {
        // Throw the exception immediately if this matrix has null entries
        if (this.ENTRIES == null) {
            raise(new NullMatrixException(
                "This matrix is null. " +
                "Please ensure the matrix are initialized before performing transposition."
            ));
        }

        this.create( Matrix.transpose(this).getEntries() );
    }


    /**
     * Performs transposition for the given two-dimensional array and produces new
     * array with the transposed elements.
     *
     * <p>If the given two-dimensional array type are not square, then it would switches the row and column
     * indices of the array. Which means the array size would be switched
     * (for example, {@code 2x4 -> 4x2}).
     *
     * @apiNote
     * <p>Repeating the process on the transposed two-dimensional array returns
     * the elements to their original position. Also can be written
     * like this, <code><b>(A</b><sup>T</sup>)<sup>T</sup></code>.
     *
     * @param  arr                  the two-dimensional array to be transposed.
     *
     * @return                      the transposed of given two-dimensional array.
     *
     * @throws NullMatrixException  if the given array is {@code null} or empty.
     *
     * @since                       0.2.0
     * @see                         #transpose()
     * @see                         #transpose(Matrix)
     */
    public static double[ ][ ] transpose(double[ ][ ] arr) {
        if (arr == null || arr.length == 0) {
            raise(new NullMatrixException(
                "Given array is null. " +
                "Please ensure the array has valid elements before performing transposition."
            ));
        }

        return Matrix.transpose(new Matrix(arr)).getEntries();
    }


    /**
     * Performs transposition for the given matrix and produces new
     * matrix with the transposed elements.
     *
     * <p>If the given matrix type are not square, then it would switches the row and column
     * indices of the matrix. Which means the matrix size would be switched
     * (for example, {@code 2x4 -> 4x2}).
     *
     * <p>The transposed matrix always get denoted by upperscript <b>T</b>, <b>t</b> or <b>tr</b>,
     * for example:
     * <ul>
     * <li> <b>A</b><sup>T</sup>
     * <li> <b>A</b><sup>t</sup>
     * <li> <b>A</b><sup>tr</sup>
     * </ul>
     *
     * <p><b>Example code:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = new Matrix(
     *       new double[][] {
     *           { 1, 7, 8 },
     *           { 3, 0, 2 }
     *       }
     *   );
     *
     *   // Transpose and create new matrix named "mT",
     *   // which the capital "T" is to indicates the
     *   // transposed matrix of matrix "m".
     *   Matrix mT = Matrix.transpose(m);
     *   mT.display();
     * </code></pre>
     *
     * <p>This code would transpose the matrix {@code m}, which means
     * the size would be switched. Here is the output:
     *
     * <pre>&nbsp;
     *   [   [1.0, 3.0],
     *       [7.0, 0.0],
     *       [8.0, 2.0]   ]
     * </pre>
     *
     * @apiNote
     * <p>Repeating the process on the transposed matrix returns
     * the elements to their original position. Also can be written
     * like this, <code><b>(A</b><sup>T</sup>)<sup>T</sup></code>.
     *
     * @param  m                    the <b>Matrix</b> object to be transposed.
     *
     * @return                      the transposed of given matrix.
     *
     * @throws NullMatrixException  if the entries of given matrix is {@code null}.
     *
     * @since                       0.2.0
     * @see                         #transpose()
     * @see                         #transpose(double[][])
     */
    public static Matrix transpose(Matrix m) {
        // Throw the exception immediately if the given matrix has null entries
        if (m == null || m.getEntries() == null) {
            raise(new NullMatrixException(
                "Given matrix is null. " +
                "Please ensure the matrix are initialized before performing transposition."
            ));
        }

        // Declare new entries for the transposed matrix
        double[ ][ ] transposedEntries;

        // Check whether the matrix is square
        if (m.isSquare()) {
            // Initialize the entries
            transposedEntries = new double[m.getSize()[0]][m.getSize()[1]];

            // Iterate over elements and transpose each element
            for (int r = 0; r < m.getSize()[0]; r++) {
                for (int c = 0; c < m.getSize()[1]; c++) {
                    transposedEntries[r][c] = m.get(c, r);
                }
            }
        } else {
            // Initialize the entries with row and column switched
            transposedEntries = new double[m.getSize()[1]][m.getSize()[0]];

            for (int c = 0; c < m.getSize()[1]; c++) {
                for (int r = 0; r < m.getSize()[0]; r++) {
                    transposedEntries[c][r] = m.get(r, c);
                }
            }
        }

        return new Matrix(transposedEntries);
    }


    /*---------------------------
    ::       Matrix Trace
    ---------------------------*/

    /**
     * Calculates the trace of this matrix.
     *
     * <p>The trace of a square matrix is the sum of its diagonal elements.
     * In other words, it is the sum of the elements located at the main diagonal of
     * the matrix, from the top-left to the bottom-right.
     *
     * <p>Throws {@link IllegalMatrixSizeException} if this matrix is not a square matrix.
     *
     * <p><b>Example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = new Matrix(new double[][] {
     *       { 5, 6, 2, 8 },
     *       { 3, 2, 4, 19 },
     *       { 1, 1, -2, 12 },
     *       { 15, 11, -9, 7 }
     *   });
     *
     *   System.out.println(m.trace());
     * </code></pre>
     *
     * <p>Output:</p>
     * <code>trace = 5 + 2 + (-2) + 7 = <b>12</b></code>.
     *
     * @return                             the sum of the diagonal elements of this matrix.
     *
     * @throws IllegalMatrixSizeException  If this matrix is not square.
     *                                     In other words, it does not have the same
     *                                     number of rows and columns.
     *
     * @since                              1.5.0
     * @see                                #trace(Matrix)
     * @see                                #trace(double[][])
     */
    public double trace() {
        return Matrix.trace(this);
    }


    /**
     * Calculates the trace of a square matrix.
     *
     * <p>The trace of a square matrix is the sum of its diagonal elements.
     * In other words, it is the sum of the elements located at the main diagonal of
     * the matrix, from the top-left to the bottom-right.
     *
     * <p><b>Example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = new Matrix(new double[][] {
     *       { 5, 6, 2, 8 },
     *       { 3, 2, 4, 19 },
     *       { 1, 1, -2, 12 },
     *       { 15, 11, -9, 7 }
     *   });
     *
     *   System.out.println(m.trace());
     * </code></pre>
     *
     * <p>Output:</p>
     * <code>trace = 5 + 2 + (-2) + 7 = <b>12</b></code>.
     *
     * @param  m                           the square matrix for which to calculate the trace.
     *
     * @return                             the sum of the diagonal elements of the given matrix.
     *
     * @throws IllegalMatrixSizeException  If the given matrix is not square.
     *                                     In other words, it does not have the same
     *                                     number of rows and columns.
     *
     * @since                              1.5.0
     * @see                                #trace()
     * @see                                #trace(double[][])
     */
    public static double trace(Matrix m) {
        // Raise an error if the matrix is not square
        if (!m.isSquare()) {
            raise(new IllegalMatrixSizeException(
                "Matrix is non-square type. " +
                "Please ensure the matrix has the same number of rows and columns."
            ));
        }

        double res = 0.0;  // Store the result

        // Iterate through the matrix and calculate
        // the sum of its diagonal elements
        for (int i = 0; i < m.getSize()[0]; i++) {
            res += m.get(i, i);
        }

        return res;
    }


    /**
     * Calculates the trace of a two-dimensional array that represents a square matrix.
     *
     * <p>The trace of a square matrix is the sum of its diagonal elements.
     * In other words, it is the sum of the elements located at the main diagonal of
     * the matrix, from the top-left to the bottom-right.
     *
     * <p><b>Example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = new Matrix(new double[][] {
     *       { 5, 6, 2, 8 },
     *       { 3, 2, 4, 19 },
     *       { 1, 1, -2, 12 },
     *       { 15, 11, -9, 7 }
     *   });
     *
     *   System.out.println(m.trace());
     * </code></pre>
     *
     * <p>Output:</p>
     * <code>trace = 5 + 2 + (-2) + 7 = <b>12.0</b></code>.
     *
     * @param  arr                         the two-dimensional array for which to calculate the trace.
     *
     * @return                             the sum of the diagonal elements of the given array.
     *
     * @throws IllegalMatrixSizeException  If the given two-dimensional array is not square.
     *                                     In other words, it does not have the same
     *                                     number of rows and columns.
     *
     * @since                              1.5.0
     * @see                                #trace()
     * @see                                #trace(double[][])
     */
    public static double trace(double[ ][ ] arr) {
        if (!Matrix.isSquare(arr)) {
            raise(new IllegalMatrixSizeException(
                "Array is non-square type. " +
                "Please ensure the array has the same number of rows and columns."
            ));
        }

        double res = 0.0;
        for (int i = 0; i < arr.length; i++) {
            res += arr[i][i];
        }

        return res;
    }


    /*--------------------------
    ::      Minor Matrix
    --------------------------*/

    /**
     * Calculates and returns a new matrix representing the <b>minor</b> of this matrix.
     *
     * <p>The minor of a matrix is a submatrix formed by deleting a single row and a
     * single column from the original matrix. It plays a crucial role in various
     * linear algebra operations, including determinant calculations, matrix inversion,
     * and cofactor expansion.
     *
     * <p>This method supports <b>negative indexing</b> for both row and column
     * indices. A negative index is interpreted as an offset from the end of the
     * respective dimension.
     *
     * <p><b>Example:</b></p>
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = // ... initialize a new matrix
     *   // Remove the last row and last column
     *   Matrix minorM = m.minorMatrix(-1, -1);
     * </code></pre>
     *
     * @apiNote
     * <p>This method only works for <b>square matrices</b>. If the input matrix
     * is not square, an {@link IllegalMatrixSizeException} will be thrown.
     * Users can use the {@link #isSquare()} helper method to check whether
     * the matrix is square type.
     *
     * <p>From <a href="https://en.wikipedia.org/wiki/Minor_(linear_algebra)">
     * <q>Minor (linear algebra), Wikipedia</q></a>:
     *
     * <blockquote>
     * <p>In linear algebra, a <b>minor</b> of a matrix is the determinant of some
     * smaller square matrix, by removing one or more of its rows and columns. Minors
     * obtained by removing just one row and one column from square matrices
     * (<b>first minors</b>) are required for calculating matrix <b>cofactors</b>,
     * which in turn are useful for computing both the determinant and inverse of
     * square matrices.
     * </blockquote>
     *
     * @param  row  The index of the row to be removed (0-based or negative index).
     * @param  col  The index of the column to be removed (0-based or negative index).
     * @return      A new matrix representing the minor of this matrix, with the
     *              specified row and column removed.
     *
     * @throws NullMatrixException
     *           If this matrix is null.
     * @throws InvalidIndexException
     *           If either the given {@code row} or {@code col} index is out of bounds.
     * @throws IllegalMatrixSizeException
     *           If this matrix is not square.
     *
     * @since 1.5.0
     * @see   #minorMatrix(Matrix, int, int)
     * @see   #dropRow(int)
     * @see   #dropColumn(int)
     * @see   #isSquare()
     */
    public Matrix minorMatrix(int row, int col) {
        return Matrix.minorMatrix(this, row, col);
    }

    /**
     * Calculates and returns a new matrix representing the <b>minor</b> of the
     * given matrix.
     *
     * <p>The minor of a matrix is a submatrix formed by deleting a single row and a
     * single column from the original matrix. It plays a crucial role in various
     * linear algebra operations, including determinant calculations, matrix inversion,
     * and cofactor expansion.
     *
     * <p>This method supports <b>negative indexing</b> for both row and column
     * indices. A negative index is interpreted as an offset from the end of the
     * respective dimension.
     *
     * <p><b>Example:</b></p>
     * <pre><code class="language-java">&nbsp;
     *   Matrix m = // ... initialize a new matrix
     *   // Remove the last row and last column
     *   Matrix minorM = m.minorMatrix(-1, -1);
     * </code></pre>
     *
     * @apiNote
     * <p>This method only works for <b>square matrices</b>. If the input matrix
     * is not square, an {@link IllegalMatrixSizeException} will be thrown.
     *
     * <p>From <a href="https://en.wikipedia.org/wiki/Minor_(linear_algebra)">
     * <q>Minor (linear algebra), Wikipedia</q></a>:
     *
     * <blockquote>
     * <p>In linear algebra, a <b>minor</b> of a matrix is the determinant of some
     * smaller square matrix, by removing one or more of its rows and columns. Minors
     * obtained by removing just one row and one column from square matrices
     * (<b>first minors</b>) are required for calculating matrix <b>cofactors</b>,
     * which in turn are useful for computing both the determinant and inverse of
     * square matrices.
     * </blockquote>
     *
     * @param  m    The matrix to compute the minor of.
     * @param  row  The index of the row to be removed (0-based or negative index).
     * @param  col  The index of the column to be removed (0-based or negative index).
     * @return      A new matrix representing the minor of this matrix, with the
     *              specified row and column removed.
     *
     * @throws NullMatrixException
     *           If the given matrix is null.
     * @throws InvalidIndexException
     *           If either the given {@code row} or {@code col} index is out of bounds.
     * @throws IllegalMatrixSizeException
     *           If the given matrix is not square.
     *
     * @since 1.5.0
     * @see   #minorMatrix(int, int)
     * @see   #dropRow(Matrix, int)
     * @see   #dropColumn(Matrix, int)
     * @see   #isSquare()
     */
    public static Matrix minorMatrix(Matrix m, int row, int col) {
        if (MatrixUtils.isNullEntries(m)) {
            raise(new NullMatrixException(
                "Matrix is null. Please ensure the matrix are initialized"));
        }

        if (!m.isSquare()) {
            raise(new IllegalMatrixSizeException(
                "Matrix is not square. Please ensure the matrix have the same rows"
                    + " and columns size"
            ));
        }

        // This method are utilizes both `dropRow` and `dropColumn` methods to
        // remove and exclude the specified row and column index, and then returns
        // a new matrix with specified row and column removed,
        // also known as minor matrix.
        return m.dropRow(row).dropColumn(col);
    }


    /*=========================================
    ::
    ::  MATRIX TYPE CHECKERS
    ::
    =========================================*/


    /*---------------------------
    ::      Square Matrix
    ---------------------------*/


    /**
     * Checks whether this matrix is a square matrix.
     *
     * <p>The matrix can be called a square type if the number of rows
     * are equals to the number of columns.
     *
     * @return                      {@code true} if this matrix is square,
     *                              returns {@code false} otherwise.
     *
     * @throws NullMatrixException  if the entries of this matrix is {@code null}.
     *
     * @since                       1.0.0b.1
     * @see                         #isSquare(double[][])
     * @see                         #isSquare(Matrix)
     */
    public boolean isSquare() {
        return Matrix.isSquare(this);
    }


    /**
     * Checks whether the given two-dimensional array represents a square matrix.
     *
     * <p>The matrix can be called a square type if the number of rows
     * are equals to the number of columns.
     *
     * @param  arr                  the two-dimensional array to be checked.
     *
     * @return                      {@code true} if the array is square,
     *                              returns {@code false} otherwise.
     *
     * @throws NullMatrixException  if the given two-dimensional array is {@code null} or empty.
     *
     * @since                       1.0.0b.7
     * @see                         #isSquare()
     * @see                         #isSquare(Matrix)
     */
    public static boolean isSquare(double[ ][ ] arr) {
        if (arr == null || arr.length == 0) {
            raise(new NullMatrixException(
                "Given array is null. Please ensure the array has valid elements."));
        }

        return (arr.length == arr[0].length);
    }


    /**
     * Checks whether the given matrix is a square matrix.
     *
     * <p>The matrix can be called a square type if the number of rows
     * are equals to the number of columns.
     *
     * @param  m                    the <b>Matrix</b> object to be checked.
     *
     * @return                      {@code true} if the matrix is square,
     *                              returns {@code false} otherwise.
     *
     * @throws NullMatrixException  if the entries of given matrix is {@code null}.
     *
     * @since                       1.0.0b.7
     * @see                         #isSquare()
     * @see                         #isSquare(double[][])
     */
    public static boolean isSquare(Matrix m) {
        if (m == null || m.getEntries() == null) {
            raise(new NullMatrixException(
                "Matrix is null. Please ensure the matrix are initialized."));
        }

        return (m.getSize()[0] == m.getSize()[1]);
    }



    /*---------------------------
    ::     Diagonal Matrix
    ---------------------------*/


    /**
     * Checks whether this matrix is a diagonal matrix.
     *
     * <p>A diagonal matrix is a square matrix in which all the entries outside the main
     * diagonal (the diagonal line from the top-left to the bottom-right) are zero.
     *
     * @return                             {@code true} if this matrix is diagonal,
     *                                     returns {@code false} otherwise.
     *
     * @throws IllegalMatrixSizeException  if this matrix is not a square type.
     *
     * @since                              1.0.0b.7
     * @see                                #isDiagonal(Matrix)
     * @see                                #isDiagonal(double[][])
     */
    public boolean isDiagonal() {
        return Matrix.isDiagonal(this);
    }


    /**
     * Checks whether the given matrix is a diagonal matrix.
     *
     * <p>A diagonal matrix is a square matrix in which all the entries outside the main
     * diagonal (the diagonal line from the top-left to the bottom-right) are zero.
     *
     * @param  m                           the <b>Matrix</b> object to be checked.
     *
     * @return                             {@code true} if the matrix is diagonal,
     *                                     otherwise returns {@code false}.
     *
     * @throws IllegalMatrixSizeException  if the given matrix is not a square type.
     *
     * @since                              1.0.0b.7
     * @see                                #isDiagonal()
     * @see                                #isDiagonal(double[][])
     */
    public static boolean isDiagonal(Matrix m) {
        if (!m.isSquare()) {
            raise(new IllegalMatrixSizeException(
                "Matrix is non-square type. " +
                "Please ensure the matrix has the same number of rows and columns."
            ));
        }

        for (int row = 0; row < m.getSize()[0]; row++) {
            for (int col = 0; col < m.getSize()[1]; col++) {
                if (row != col && Math.abs(m.get(row, col)) > Matrix.THRESHOLD) {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Checks whether the given two-dimensional array represents a diagonal matrix.
     *
     * <p>A diagonal matrix is a square matrix in which all the entries outside the main
     * diagonal (the diagonal line from the top-left to the bottom-right) are zero.
     *
     * @param  arr                         the two-dimensional array to be checked.
     *
     * @return                             {@code true} if the array represents a diagonal matrix,
     *                                     returns {@code false} otherwise.
     *
     * @throws IllegalMatrixSizeException  if the two-dimensional array is not a square type.
     *
     * @since                              1.0.0b.7
     * @see                                #isDiagonal()
     * @see                                #isDiagonal(Matrix)
     */
    public static boolean isDiagonal(double[ ][ ] arr) {
        if (!Matrix.isSquare(arr)) {
            raise(new IllegalMatrixSizeException(
                "Given array is non-square type. " +
                "Please ensure the array has the same number of rows and columns."
            ));
        }

        for (int row = 0; row < arr.length; row++) {
            for (int col = 0; col < arr[0].length; col++) {
                if (row != col && Math.abs(arr[row][col]) > Matrix.THRESHOLD) {
                    return false;
                }
            }
        }

        return true;
    }



    /*---------------------------
    :: Lower Triangular Matrix
    ---------------------------*/


    /**
     * Checks whether this matrix is lower triangular.
     *
     * <p>A square matrix is considered lower triangular if all the elements above
     * the main diagonal (elements with row index greater than column index) are zero
     * or within the threshold defined by the constant {@code THRESHOLD}.
     *
     * <p>The {@linkplain #transpose() transpose} of an lower triangular matrix
     * is a upper triangular matrix and vice versa. A diagonal matrix is one
     * that consists of upper and lower a triangular elements. You can refer
     * to {@link #isDiagonal()} to check whether the matrix is diagonal.
     *
     * <p>Lower triangularity is preserved by many operations:
     *
     * <ul>
     *  <li>The sum of two lower triangular matrices is lower triangular.
     *  <li>The product of two lower triangular matrices is lower triangular.
     *  <li>The inverse of an lower triangular matrix, is lower triangular (if exists).
     *  <li>The product of an lower triangular matrix and a scalar is lower triangular.
     * </ul>
     *
     * <p>Example of lower triangular matrix:
     *
     * <pre>&nbsp;
     *   [   [3, 5, -2],
     *       [0, 4, 12],
     *       [0, 0, -5]   ]
     * </pre>
     *
     * @return                             {@code true} if the matrix is lower triangular,
     *                                     {@code false} otherwise.
     *
     * @throws NullMatrixException         if the entries of this matrix is {@code null}.
     * @throws IllegalMatrixSizeException  if this matrix is non-square type.
     *
     * @since                              1.2.0
     * @see                                #isLowerTriangular(Matrix)
     * @see                                #isLowerTriangular(double[][])
     * @see                                #THRESHOLD
     */
    public boolean isLowerTriangular() {
        return Matrix.isLowerTriangular(this);
    }


    /**
     * Checks whether the given square matrix is lower triangular.
     *
     * <p>A square matrix is considered lower triangular if all the elements above
     * the main diagonal (elements with row index greater than column index) are zero
     * or within the threshold defined by the constant {@code THRESHOLD}.
     *
     * <p>The {@linkplain #transpose() transpose} of an lower triangular matrix
     * is a upper triangular matrix and vice versa. A diagonal matrix is one
     * that consists of upper and lower a triangular elements. You can refer
     * to {@link #isDiagonal()} to check whether the matrix is diagonal.
     *
     * <p>Lower triangularity is preserved by many operations:
     *
     * <ul>
     *  <li>The sum of two lower triangular matrices is lower triangular.
     *  <li>The product of two lower triangular matrices is lower triangular.
     *  <li>The inverse of an lower triangular matrix, is lower triangular (if exists).
     *  <li>The product of an lower triangular matrix and a scalar is lower triangular.
     * </ul>
     *
     * <p>Example of lower triangular matrix:
     *
     * <pre>&nbsp;
     *   [   [3, 5, -2],
     *       [0, 4, 12],
     *       [0, 0, -5]   ]
     * </pre>
     *
     * @param  m                           the square matrix to be checked.
     *
     * @return                             {@code true} if the matrix is lower triangular,
     *                                     {@code false} otherwise.
     *
     * @throws NullMatrixException         if the given matrix or its entries is {@code null}.
     * @throws IllegalMatrixSizeException  if the given matrix is non-square type.
     *
     * @since                              1.2.0
     * @see                                #isLowerTriangular()
     * @see                                #isLowerTriangular(double[][])
     * @see                                #THRESHOLD
     */
    public static boolean isLowerTriangular(Matrix m) {
        if (MatrixUtils.isNullEntries(m)) {
            raise(new NullMatrixException(
                "Matrix is null. Please ensure the matrix have been initialized.")
            );
        }

        // The matrix must be square
        else if (!m.isSquare()) {
            raise(new IllegalMatrixSizeException(
                "Matrix is non-square type. " +
                "Please ensure the matrix has the same number of rows and columns."
            ));
        }

        for (int r = 1; r < m.getSize()[0]; r++) {
            for (int c = 0; c < r; c++) {
                if (Math.abs(m.get(r, c)) > Matrix.THRESHOLD) {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Checks whether the given square two-dimensional array is lower triangular.
     *
     * <p>A square matrix is considered lower triangular if all the elements above
     * the main diagonal (elements with row index greater than column index) are zero
     * or within the threshold defined by the constant {@code THRESHOLD}.
     *
     * <p>The {@linkplain #transpose() transpose} of an lower triangular matrix
     * is a upper triangular matrix and vice versa. A diagonal matrix is one
     * that consists of upper and lower a triangular elements. You can refer
     * to {@link #isDiagonal()} to check whether the matrix is diagonal.
     *
     * <p>Lower triangularity is preserved by many operations:
     *
     * <ul>
     *  <li>The sum of two lower triangular matrices is lower triangular.
     *  <li>The product of two lower triangular matrices is lower triangular.
     *  <li>The inverse of an lower triangular matrix, is lower triangular (if exists).
     *  <li>The product of an lower triangular matrix and a scalar is lower triangular.
     * </ul>
     *
     * <p>Example of lower triangular matrix:
     *
     * <pre>&nbsp;
     *   [   [3, 5, -2],
     *       [0, 4, 12],
     *       [0, 0, -5]   ]
     * </pre>
     *
     * @param  arr                          the square array to be checked.
     *
     * @return                             {@code true} if the matrix is lower triangular,
     *                                     {@code false} otherwise.
     *
     * @throws NullMatrixException         if the given array is {@code null} or empty.
     * @throws IllegalMatrixSizeException  if the given array is non-square type.
     *
     * @since                              1.2.0
     * @see                                #isLowerTriangular()
     * @see                                #isLowerTriangular(Matrix)
     * @see                                #THRESHOLD
     */
    public static boolean isLowerTriangular(double[ ][ ] arr) {
        if (arr == null || arr.length == 0) {
            raise(new NullMatrixException(
                "Array is null. Please ensure the array has valid elements.")
            );
        }

        // The two-dimensional array must be square
        else if (!Matrix.isSquare(arr)) {
            raise(new IllegalMatrixSizeException(
                "Array is non-square type. " +
                "Please ensure the array has the same number of rows and columns."
            ));
        }

        for (int r = 1; r < arr.length; r++) {
            for (int c = 0; c < r; c++) {
                if (Math.abs(arr[r][c]) > Matrix.THRESHOLD) {
                    return false;
                }
            }
        }

        return true;
    }



    /*---------------------------
    :: Upper Triangular Matrix
    ---------------------------*/


    /**
     * Checks whether this matrix is upper triangular.
     *
     * <p>A square matrix is considered upper triangular if all the elements below
     * the main diagonal (elements with row index greater than column index) are zero
     * or within the threshold defined by the constant {@code THRESHOLD}.
     *
     * <p>The {@linkplain #transpose() transpose} of an upper triangular matrix
     * is a lower triangular matrix and vice versa. A diagonal matrix is one
     * that consists of upper and lower a triangular elements. You can refer
     * to {@link #isDiagonal()} to check whether the matrix is diagonal.
     *
     * <p>Upper triangularity is preserved by many operations:
     *
     * <ul>
     *  <li>The sum of two upper triangular matrices is upper triangular.
     *  <li>The product of two upper triangular matrices is upper triangular.
     *  <li>The inverse of an upper triangular matrix, is upper triangular (if exists).
     *  <li>The product of an upper triangular matrix and a scalar is upper triangular.
     * </ul>
     *
     * <p>Example of upper triangular matrix:
     *
     * <pre>&nbsp;
     *   [   [-2, 0, 0],
     *       [15, 5, 0],
     *       [2, -8, 7]   ]
     * </pre>
     *
     * @return                             {@code true} if the matrix is upper triangular,
     *                                     {@code false} otherwise.
     *
     * @throws NullMatrixException         if the entries of this matrix is {@code null}.
     * @throws IllegalMatrixSizeException  if this matrix is non-square type.
     *
     * @since                              1.2.0
     * @see                                #isUpperTriangular(Matrix)
     * @see                                #isUpperTriangular(double[][])
     * @see                                #THRESHOLD
     */
    public boolean isUpperTriangular() {
        return Matrix.isUpperTriangular(this);
    }


    /**
     * Checks whether the given square matrix is upper triangular.
     *
     * <p>A square matrix is considered upper triangular if all the elements below
     * the main diagonal (elements with row index greater than column index) are zero
     * or within the threshold defined by the constant {@code THRESHOLD}.
     *
     * <p>The {@linkplain #transpose() transpose} of an upper triangular matrix
     * is a lower triangular matrix and vice versa. A diagonal matrix is one
     * that consists of upper and lower a triangular elements. You can refer
     * to {@link #isDiagonal()} to check whether the matrix is diagonal.
     *
     * <p>Upper triangularity is preserved by many operations:
     *
     * <ul>
     *  <li>The sum of two upper triangular matrices is upper triangular.
     *  <li>The product of two upper triangular matrices is upper triangular.
     *  <li>The inverse of an upper triangular matrix, is upper triangular (if exists).
     *  <li>The product of an upper triangular matrix and a scalar is upper triangular.
     * </ul>
     *
     * <p>Example of upper triangular matrix:
     *
     * <pre>&nbsp;
     *   [   [-2, 0, 0],
     *       [15, 5, 0],
     *       [2, -8, 7]   ]
     * </pre>
     *
     * @param  m                           the square matrix to be checked.
     *
     * @return                             {@code true} if the matrix is upper triangular,
     *                                     {@code false} otherwise.
     *
     * @throws NullMatrixException         if the given matrix or its entries is {@code null}.
     * @throws IllegalMatrixSizeException  if the given matrix is non-square type.
     *
     * @since                              1.2.0
     * @see                                #isUpperTriangular()
     * @see                                #isUpperTriangular(double[][])
     * @see                                #THRESHOLD
     */
    public static boolean isUpperTriangular(Matrix m) {
        if (MatrixUtils.isNullEntries(m)) {
            raise(new NullMatrixException(
                "Matrix is null. Please ensure the matrix have been initialized.")
            );
        }

        // The matrix must be square
        else if (!m.isSquare()) {
            raise(new IllegalMatrixSizeException(
                "Matrix is non-square type. " +
                "Please ensure the matrix has the same number of rows and columns."
            ));
        }

        for (int r = 0; r < m.getSize()[0]; r++) {
            for (int c = r + 1; c < m.getSize()[1]; c++) {
                if (Math.abs(m.get(r, c)) > Matrix.THRESHOLD) {
                    return false;
                }
            }
        }

        return true;
    }


    /**
     * Checks whether the given square two-dimensional array is upper triangular.
     *
     * <p>A square matrix is considered upper triangular if all the elements below
     * the main diagonal (elements with row index greater than column index) are zero
     * or within the threshold defined by the constant {@code THRESHOLD}.
     *
     * <p>The {@linkplain #transpose() transpose} of an upper triangular matrix
     * is a lower triangular matrix and vice versa. A diagonal matrix is one
     * that consists of upper and lower a triangular elements. You can refer
     * to {@link #isDiagonal()} to check whether the matrix is diagonal.
     *
     * <p>Upper triangularity is preserved by many operations:
     *
     * <ul>
     *  <li>The sum of two upper triangular matrices is upper triangular.
     *  <li>The product of two upper triangular matrices is upper triangular.
     *  <li>The inverse of an upper triangular matrix, is upper triangular (if exists).
     *  <li>The product of an upper triangular matrix and a scalar is upper triangular.
     * </ul>
     *
     * <p>Example of upper triangular matrix:
     *
     * <pre>&nbsp;
     *   [   [-2, 0, 0],
     *       [15, 5, 0],
     *       [2, -8, 7]   ]
     * </pre>
     *
     * @param  arr                         the square array to be checked.
     *
     * @return                             {@code true} if the matrix is upper triangular,
     *                                     {@code false} otherwise.
     *
     * @throws NullMatrixException         if the given array is {@code null} or empty.
     * @throws IllegalMatrixSizeException  if the given array is non-square type.
     *
     * @since                              1.2.0
     * @see                                #isUpperTriangular()
     * @see                                #isUpperTriangular(Matrix)
     * @see                                #THRESHOLD
     */
    public static boolean isUpperTriangular(double[ ][ ] arr) {
        if (arr == null || arr.length == 0) {
            raise(new NullMatrixException(
                "Array is null. Please ensure the array has valid elements.")
            );
        }

        // The matrix must be square
        else if (!Matrix.isSquare(arr)) {
            raise(new IllegalMatrixSizeException(
                "Array is non-square type. " +
                "Please ensure the array has the same number of rows and columns."
            ));
        }

        for (int r = 0; r < arr.length; r++) {
            for (int c = r + 1; c < arr[0].length; c++) {
                if (Math.abs(arr[r][c]) > Matrix.THRESHOLD) {
                    return false;
                }
            }
        }

        return true;
    }


    /*---------------------------
    :: Sparse Matrix
    ---------------------------*/

    /**
     * Determines whether this matrix is considered sparse.
     *
     * <p>This method employs a specific criterion for sparsity: a matrix is
     * deemed <b>sparse</b> if the number of non-zero elements within it is less than
     * or equal to the maximum dimension of the matrix itself. If most of the elements
     * are non-zero, the matrix is considered <b>dense</b>. To determine whether
     * an element is effectively zero, it compares its absolute value against a defined
     * {@linkplain Matrix#THRESHOLD threshold}. Elements falling below this threshold
     * are considered zero for the purpose of sparsity evaluation.
     *
     * <p><b>Examples:</b></p>
     *
     * <p>A sparse matrix is one where most of the elements are zero.
     * Here's an example:
     *
     * <pre>&nbsp;
     *   [ 1 0 7 0 ]
     *   [ 9 0 0 3 ]
     *   [ 0 0 0 0 ]
     * </pre>
     *
     * <p>In this {@code 3x4} matrix, there is only a few non-zero elements,
     * and the rest are zero. Such matrices are considered <b>sparse</b>.
     *
     * <hr>
     *
     * <p>A diagonal matrix is a special case of a sparse matrix where all the
     * non-zero elements are on the main diagonal. Here's an example:
     *
     * <pre>&nbsp;
     *   [ 7 0 0 ]
     *   [ 0 2 0 ]
     *   [ 0 0 5 ]
     * </pre>
     *
     * <p>In this {@code 3x3} matrix, only the diagonal elements (7, 2, 5)
     * are non-zero, and the rest are zero. Diagonal matrices are a type of
     * <b>sparse matrix</b>.
     *
     * @return {@code true} if the matrix is sparse, {@code false} otherwise.
     *
     * @throws NullMatrixException  If this matrix has {@code null} entries.
     *
     * @since 1.5.0
     * @see   #isSparse(Matrix)
     */
    public boolean isSparse() {
        return Matrix.isSparse(this);
    }


    /**
     * Determines whether the given matrix is considered sparse.
     *
     * <p>This method employs a specific criterion for sparsity: a matrix is
     * deemed <b>sparse</b> if the number of non-zero elements within it is less than
     * or equal to the maximum dimension of the matrix itself. If most of the elements
     * are non-zero, the matrix is considered <b>dense</b>. To determine whether
     * an element is effectively zero, it compares its absolute value against a defined
     * {@linkplain Matrix#THRESHOLD threshold}. Elements falling below this threshold
     * are considered zero for the purpose of sparsity evaluation.
     *
     * <p><b>Examples:</b></p>
     *
     * <p>A sparse matrix is one where most of the elements are zero.
     * Here's an example:
     *
     * <pre>&nbsp;
     *   [ 1 0 7 0 ]
     *   [ 9 0 0 3 ]
     *   [ 0 0 0 0 ]
     * </pre>
     *
     * <p>In this {@code 3x4} matrix, there is only a few non-zero elements,
     * and the rest are zero. Such matrices are considered <b>sparse</b>.
     *
     * <hr>
     *
     * <p>A diagonal matrix is a special case of a sparse matrix where all the
     * non-zero elements are on the main diagonal. Here's an example:
     *
     * <pre>&nbsp;
     *   [ 7 0 0 ]
     *   [ 0 2 0 ]
     *   [ 0 0 5 ]
     * </pre>
     *
     * <p>In this {@code 3x3} matrix, only the diagonal elements (7, 2, 5)
     * are non-zero, and the rest are zero. Diagonal matrices are a type of
     * <b>sparse matrix</b>.
     *
     * @param  m  The matrix to be evaluated for sparsity.
     * @return    {@code true} if the matrix is sparse, {@code false} otherwise.
     *
     * @throws NullMatrixException  If the provided matrix has {@code null} entries.
     *
     * @since 1.5.0
     */
    public static boolean isSparse(Matrix m) {
        final int[] mSize = m.getSize();
        int numberNonZero = 0;  // To hold the total number of non-zero entries

        if (MatrixUtils.isNullEntries(m)) {
            raise(new NullMatrixException(
                "Matrix is null. Please ensure the matrix have been initialized.")
            );
        }

        for (int r = 0; r < mSize[0]; r++) {
            for (int c = 0; c < mSize[1]; c++) {
                if (Math.abs(m.get(r, c)) > Matrix.THRESHOLD) {
                    numberNonZero++;  // increment
                }
            }
        }

        return (numberNonZero <= Math.max(mSize[0], mSize[1]));
    }


    /**
     * Determines whether the two-dimensional array that represents a matrix is
     * considered sparse.
     *
     * <p>This method employs a specific criterion for sparsity: a matrix is
     * deemed <b>sparse</b> if the number of non-zero elements within it is less than
     * or equal to the maximum dimension of the matrix itself. If most of the elements
     * are non-zero, the matrix is considered <b>dense</b>. To determine whether
     * an element is effectively zero, it compares its absolute value against a defined
     * {@linkplain Matrix#THRESHOLD threshold}. Elements falling below this threshold
     * are considered zero for the purpose of sparsity evaluation.
     *
     * <p><b>Examples:</b></p>
     *
     * <p>A sparse matrix is one where most of the elements are zero.
     * Here's an example:
     *
     * <pre>&nbsp;
     *   [ 1 0 7 0 ]
     *   [ 9 0 0 3 ]
     *   [ 0 0 0 0 ]
     * </pre>
     *
     * <p>In this {@code 3x4} matrix, there is only a few non-zero elements,
     * and the rest are zero. Such matrices are considered <b>sparse</b>.
     *
     * <hr>
     *
     * <p>A diagonal matrix is a special case of a sparse matrix where all the
     * non-zero elements are on the main diagonal. Here's an example:
     *
     * <pre>&nbsp;
     *   [ 7 0 0 ]
     *   [ 0 2 0 ]
     *   [ 0 0 5 ]
     * </pre>
     *
     * <p>In this {@code 3x3} matrix, only the diagonal elements (7, 2, 5)
     * are non-zero, and the rest are zero. Diagonal matrices are a type of
     * <b>sparse matrix</b>.
     *
     * @param  a  The two-dimensional array to be evaluated for sparsity.
     * @return    {@code true} if the array is sparse, {@code false} otherwise.
     *
     * @throws NullMatrixException  If the provided array is {@code null} or empty.
     *
     * @since 1.5.0
     */
    public static boolean isSparse(double[ ][ ] a) {
        final int rows = a.length;
        final int cols = a[0].length;
        int numberNonZero = 0;  // To hold the total number of non-zero entries

        if (a == null || a.length == 0) {
            raise(new NullMatrixException(
                "Array is null. Please ensure the array has valid elements.")
            );
        }

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (Math.abs(a[r][c]) > Matrix.THRESHOLD) {
                    numberNonZero++;  // increment
                }
            }
        }

        return (numberNonZero <= Math.max(rows, cols));
    }


    /**
     * Checks whether this matrix represents an identity matrix.
     *
     * <p>An <b>identity matrix</b> is a square matrix with all elements on the main diagonal is
     * equal to {@code 1}, and all other elements equal to {@code 0}. In an identity matrix,
     * the number of rows is equal to the number of columns, also known as square matrix.
     * You can utilize the {@link #isSquare()} method to determine whether the matrix is square.
     *
     * <p>The elements on the main diagonal must be represents as integers or floating-point numbers
     * represented as integers (for example, {@code 1.0}), but not fractions or decimal numbers with
     * fractional parts (for example, {@code 1.2} or {@code 1.8}).
     *
     * @apiNote
     * This method has a time complexity of {@code O(n^2)} and a space complexity of {@code O(1)},
     * where {@code n} is the number of rows or columns in the matrix.
     *
     * @return    {@code true} if the matrix represents an identity matrix, {@code false} otherwise.
     *
     * @throws NullMatrixException         If this matrix has {@code null} entries.
     * @throws IllegalMatrixSizeException  If this matrix is not a square matrix.
     *
     * @since  1.5.0
     * @see    #isIdentity(Matrix)
     * @see    #isIdentity(double[][])
     */
    public boolean isIdentity() {
        return Matrix.isIdentity(this);
    }

    /**
     * Checks whether the given matrix represents an identity matrix.
     *
     * <p>An <b>identity matrix</b> is a square matrix with all elements on the main diagonal is
     * equal to {@code 1}, and all other elements equal to {@code 0}. In an identity matrix,
     * the number of rows is equal to the number of columns, also known as square matrix.
     * You can utilize the {@link #isSquare()} method to determine whether the matrix is square.
     *
     * <p>The elements on the main diagonal must be represents as integers or floating-point numbers
     * represented as integers (for example, {@code 1.0}), but not fractions or decimal numbers with
     * fractional parts (for example, {@code 1.2} or {@code 1.8}).
     *
     * @apiNote
     * This method has a time complexity of {@code O(n^2)} and a space complexity of {@code O(1)},
     * where {@code n} is the number of rows or columns in the input matrix.
     * 
     * @param  m  The {@link Matrix} to be checked.
     *
     * @return    {@code true} if the matrix represents an identity matrix, {@code false} otherwise.
     *
     * @throws NullMatrixException         If the input matrix is {@code null}.
     * @throws IllegalMatrixSizeException  If the input matrix is not a square matrix.
     *
     * @since  1.5.0
     * @see    #isIdentity()
     * @see    #isIdentity(double[][])
     */
    public static boolean isIdentity(Matrix m) {
        if (MatrixUtils.isNullEntries(m)) {  // Check for uninitialized matrix
            raise(new NullMatrixException(
                "Matrix is null. Please ensure the matrix have been initialized."));
        }
        // ** no else-if after throw
        if (!m.isSquare()) {  // Check for non-square matrix
            raise(new IllegalMatrixSizeException(
                "Matrix is not square. " +
                "Please ensure the matrix has the same number of rows and columns."
            ));
        }

        // First, check whether the matrix is a diagonal matrix
        if (!m.isDiagonal()) return false;  // Return false if not a diagonal matrix

        // After that, check each of the elements of its principal diagonal
        // and they all must be strictly equal to 1.0
        for (int n = 0; n < m.getNumRows(); n++) {
            // The elements on the main diagonal must be integers or floating-point numbers
            // represented as integers (e.g., 1.0), but not fractions or decimal numbers with
            // fractional parts (e.g., 1.2 or 1.8).
            if (m.get(n, n) != 1.0) return false;
        }

        return true;  // Pass all checks of identity matrix
    }

    /**
     * Checks whether the given two-dimensional array represents an identity matrix.
     *
     * <p>An <b>identity matrix</b> is a square matrix with all elements on the main diagonal is
     * equal to {@code 1}, and all other elements equal to {@code 0}. In an identity matrix,
     * the number of rows is equal to the number of columns, also known as square matrix.
     * You can utilize the {@link #isSquare(double[][])} to determine whether the two-dimensional
     * array represents a square matrix.
     *
     * <p>The elements on the main diagonal must be represents as integers or floating-point numbers
     * represented as integers (for example, {@code 1.0}), but not fractions or decimal numbers with
     * fractional parts (for example, {@code 1.2} or {@code 1.8}).
     *
     * @apiNote
     * This method has a time complexity of {@code O(n^2)} and a space complexity of {@code O(1)},
     * where {@code n} is the number of rows or columns in the input array.
     * 
     * @param  arr  The two-dimensional array to be checked.
     *
     * @return      {@code true} if the array represents an identity matrix, {@code false} otherwise.
     *
     * @throws NullMatrixException         If the input array is {@code null}.
     * @throws IllegalMatrixSizeException  If the input array is not represented as a square matrix.
     *
     * @since  1.5.0
     * @see    #isIdentity()
     * @see    #isIdentity(Matrix)
     */
    public static boolean isIdentity(double[][] arr) {
        if (arr == null || arr.length == 0) {  // Check for null array
            raise(new NullMatrixException(
                "Array is null. Please ensure the array have been initialized."));
        }
        // ** no else-if after throw
        if (!Matrix.isSquare(arr)) {  // Check for non-square array
            raise(new IllegalMatrixSizeException(
                "Array is not square. " +
                "Please ensure the array has the same number of rows and columns."
            ));
        }

        // First, check whether the 2D array is represented as diagonal matrix
        if (!Matrix.isDiagonal(arr)) return false;  // Return false if not a diagonal matrix

        // After that, check each of the elements of its principal diagonal
        // and they all must be strictly equal to 1.0
        for (int n = 0; n < arr.length; n++) {
            // The elements on the main diagonal must be integers or floating-point numbers
            // represented as integers (e.g., 1.0), but not fractions or decimal numbers with
            // fractional parts (e.g., 1.2 or 1.8).
            if (arr[n][n] != 1.0) return false;
        }

        return true;  // Pass all checks of identity matrix
    }


    /*=========================================
    ::
    ::  ADDITIONAL / UTILITIES METHODS
    ::
    =========================================*/


    /*---------------------------
    ::       Matrix Copy
    ---------------------------*/

    /**
     * Duplicates this matrix to another matrix object.
     *
     * <p><b>Deprecated:</b> This method is deprecated due to its usage of shallow copy,
     * which may lead to unexpected behavior. It is recommended to use {@link #deepCopy()} method instead,
     * which performs a deep copy of the matrix.</p>
     *
     * @return                          the copied of this matrix with all of its attributes.
     *
     * @throws     NullMatrixException  if the entries of this matrix is {@code null}.
     *
     * @since                           0.2.0
     * @see                             #deepCopy()
     *
     * @deprecated                      This method is deprecated and may
     *                                  result in unexpected behavior.
     *                                  Use {@link #deepCopy()} for performing a
     *                                  deep copy of the matrix instead.
     */
    @Deprecated
    public Matrix copy() {
        try {
            if (this.ENTRIES == null) {
                throw new NullMatrixException(
                    "Matrix is null. Please ensure the matrix are initialized.");
            }
        } catch (final NullMatrixException nme) {
            raise(nme);
        }

        // Create new and copy the matrix
        return new Matrix(this.getEntries());
    }


    /**
     * Creates and returns a deep copy of this matrix.
     *
     * @return a new <b>Matrix</b> object which is a deep copy of this matrix.
     *
     * @since  1.0.0b.7
     * @see    MatrixUtils#deepCopyOf(Matrix)
     * @see    com.mitsuki.jmatrix.core.MatrixUtils
     */
    public Matrix deepCopy() {
        return MatrixUtils.deepCopyOf(this);
    }



    /*---------------------------
    ::      Matrix Select
    ---------------------------*/


    /**
     * Selects the matrix row with the specified index.
     *
     * <p>This method should be combined with {@link #change(double ...) change} method.<br>
     * The indexing is similar to array which zero is the first index.
     * And the index cannot be a negative value.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] a = {
     *       { 5, 6, 7 },
     *       { 4, 5, 6 }
     *   };
     *
     *   Matrix m = new Matrix(a);
     *
     *   // Change the values of first row
     *   double[] newRow = { 1, 2, 3 };
     *   m.select(0).change(newRow);
     *
     *   m.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [1.0, 2.0, 3.0],
     *       [4.0, 5.0, 6.0]   ]
     * </pre>
     *
     * @param  index                  the index row to be selected.
     *
     * @return                        self.
     *
     * @throws InvalidIndexException  if the input index is negative value
     *                                or larger than number of matrix rows.
     * @throws NullMatrixException    if entries of this matrix is {@code null}.
     *
     * @since                         0.2.0, 1.5.0
     * @see                           #change(double ...)
     * @see                           #change(double)
     * @deprecated                    As of the deprecation of {@link #change} methods,
     *                                this method is no longer in use and no longer recommended.
     */
    @Deprecated
    public Matrix select(final int index) {
        // Check for matrix with null entries
        if (this.ENTRIES == null) {
            cause = new NullMatrixException(
                "Matrix is null. Please ensure the matrix are initialized.");
        }
        // Check for negative index
        else if (index < 0) {
            cause = new InvalidIndexException(
                "Given index is negative value. Please ensure the index is positive value.");
        }
        // Check for given index is greater than total rows
        else if (index > this.ROWS - 1) {
            cause = new InvalidIndexException(
                "Given index is too larger than number of rows.");
        }

        if (cause != null) raise(cause);

        this.selectedIndex = index;
        this.hasSelect = true;

        return this; // return self
    }



    /*---------------------------
    ::      Matrix Change
    ---------------------------*/


    /**
     * Changes all values at specified row with the given values.
     *
     * <p>Only use this method when already called {@link #select(int) select} method.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] a = {
     *       { 5, 6, 7 },
     *       { 4, 5, 6 }
     *   };
     *
     *   Matrix m = new Matrix(a);
     *
     *   // Change the values of first row
     *   double[] newRow = { 1, 2, 3 };
     *   m.select(0).change(newRow);
     *
     *   m.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [1.0, 2.0, 3.0],
     *       [4.0, 5.0, 6.0]   ]
     * </pre>
     *
     * @param  values                    the values to changes all column entries at specified row.
     *
     * @throws IllegalArgumentException  if the given argument is overcapacity to matrix column
     *                                   or not enough argument to fill the column.
     * @throws InvalidIndexException     if attempts to call this method
     *                                   but have not called {@link #select(int) select} method
     *                                   or the selected index is a negative value.
     *
     * @since                            0.2.0, 1.5.0
     * @see                              #select(int)
     * @see                              #change(double)
     * @deprecated                       Due to having an inefficient way to change entries in a
     *                                   specific row of the matrix. Please transition to using
     *                                   the {@link #insertRow(int, double[]) insertRow}
     *                                   method for improved performance and functionality.
     *                                   If want to update and change specific column of the matrix,
     *                                   you can utilize the {@link #insertColumn(int, double[]) insertColumn} method.
     */
    @Deprecated
    public void change(double ... values) {
        // Check whether the values size is greater than number of columns of this matrix
        if (values.length > this.COLS) {
            cause = new JMatrixBaseException(new IllegalArgumentException(
                "Too many values for matrix with columns: " + this.COLS));
        }
        // Check whether the values size is lower than number of columns of this matrix
        else if (values.length < this.COLS) {
            cause = new JMatrixBaseException(new IllegalArgumentException(
                "Not enough values for matrix with columns: " + this.COLS));
        }
        // Check if the user have not select any index row
        else if (!this.hasSelect) {
            cause = new InvalidIndexException(
                "Selected index is null. " +
                "Please ensure you have already called \"select(int)\" method."
            );
        }

        // Throw the exception if got one
        if (cause != null) raise(cause);

        // Change values of matrix column with values from argument parameter
        for (int i = 0; i < this.COLS; i++) {
            this.ENTRIES[this.selectedIndex][i] = values[i];
        }

        // reset to default
        this.selectedIndex = 0;
        this.hasSelect = false;
    }


    /**
     * Changes all values at specified column with the repeated of given value.
     *
     * <p>Only use this method when already called {@link #select(int) select} method.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] a = {
     *       { 5, 6, 7 },
     *       { 4, 5, 6 }
     *   };
     *
     *   Matrix m = new Matrix(a);
     *
     *   // Change the values of first row
     *   m.select(0).change(5);
     *
     *   m.display();
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     *
     * <pre>&nbsp;
     *   [   [5.0, 5.0, 5.0],
     *       [4.0, 5.0, 6.0]   ]
     * </pre>
     *
     * @param  value                  the value to changes all values at specified column.
     *
     * @throws InvalidIndexException  if attempts to call this method
     *                                but have not called {@link #select(int) select} method
     *                                or the selected index is a negative value.
     *
     * @since                         0.2.0, 1.5.0
     * @see                           #select(int)
     * @see                           #change(double ...)
     * @deprecated                    Due to having an inefficient way to change entries in a
     *                                specific row of the matrix. Please transition to using
     *                                the {@link #insertRow(int, double[]) insertRow}
     *                                method for improved performance and functionality.
     *                                If want to update and change specific column of the matrix,
     *                                you can utilize the {@link #insertColumn(int, double[]) insertColumn} method.
     */
    @Deprecated
    public void change(double value) {
        // Check if the user have not select any index row
        // If user have not then it will immediately raise the exception
        if (!this.hasSelect) {
            raise(new InvalidIndexException(
                "Selected index is null. " +
                "Please ensure you have already called \"select(int)\" method."
            ));
        }

        // Create new array with same value
        double[ ] values = new double[this.COLS];
        for (int i = 0; i < this.COLS; i++) {
            values[i] = value;
        }

        for (int i = 0; i < this.COLS; i++) {
            this.ENTRIES[this.selectedIndex][i] = values[i];
        }

        // reset to default
        this.selectedIndex = 0;
        this.hasSelect = false;
    }



    /*---------------------------
    ::      Matrix Clear
    ---------------------------*/


    /**
     * Clears and changes all elements of this matrix to zero,
     * and converts this matrix into "zero matrix".
     *
     * @throws NullMatrixException  if the entries of this matrix is {@code null}.
     *
     * @since                       0.2.0
     * @see                         #sort()
     */
    public void clear() {
        if (this.ENTRIES == null) {
            raise(new NullMatrixException(
                "Matrix is null. Please ensure the matrix have been initialized."));
        }

        for (int r = 0; r < this.ROWS; r++) {
            for (int c = 0; c < this.COLS; c++) {
                this.ENTRIES[r][c] = 0.0;
            }
        }
    }



    /*---------------------------
    ::       Matrix Sort
    ---------------------------*/


    /**
     * Sorts the rows of this matrix in ascending order.
     *
     * @throws NullMatrixException  if the entries of this matrix is {@code null}.
     *
     * @since                       0.2.0
     * @see                         #sort(Matrix)
     * @see                         #sort(double[][])
     * @see                         Arrays#sort(double[])
     */
    public void sort() {
        if (this.ENTRIES == null) {
            raise(new NullMatrixException(
                "This matrix is null. Please ensure the matrix are initialized."));
        }

        // Sort elements of this matrix and store it
        double[ ][ ] sortedEntries = Matrix.sort(this).getEntries();

        // Change all the elements on this matrix with the sorted elements
        for (int r = 0; r < this.ROWS; r++) {
            for (int c = 0; c < this.COLS; c++) {
                this.ENTRIES[r][c] = sortedEntries[r][c];
            }
        }
    }


    /**
     * Sorts the rows of the given two-dimensional array in ascending order.
     *
     * @param  arr                  the two-dimensional array to be sorted.
     *
     * @throws NullMatrixException  if the given array is {@code null} or empty.
     *
     * @since                       0.2.0
     * @see                         #sort()
     * @see                         #sort(Matrix)
     * @see                         Arrays#sort(double[])
     */
    public static void sort(double[ ][ ] arr) {
        if (arr == null || arr.length == 0) {
            raise(new NullMatrixException(
                "Given array is null. Please ensure the array has valid elements."));
        }

        for (int r = 0; r < arr.length; r++) {
            Arrays.sort(arr[r]);
        }
    }


    /**
     * Sorts the rows of the given matrix in ascending order.
     *
     * @param  m                    the <b>Matrix</b> object to be sorted.
     *
     * @return                      the sorted <b>Matrix</b> object.
     *
     * @throws NullMatrixException  if the given matrix or the entries is {@code null} or empty.
     *
     * @since                       1.0.0b.1
     * @see                         #sort()
     * @see                         #sort(double[][])
     * @see                         Arrays#sort(double[])
     */
    public static Matrix sort(Matrix m) {
        // Check for matrix with null entries
        if (m == null || m.getEntries() == null) {
            raise(new NullMatrixException(
                "Given matrix is null. Please ensure the matrix are initialized."));
        }

        // Extract and sort the values
        double[ ][ ] sortedArr = m.getEntries();

        for (int r = 0; r < sortedArr.length; r++) {
            Arrays.sort(sortedArr[r]);
        }

        return new Matrix(sortedArr);
    }



    /*---------------------------
    ::       Matrix Get
    ---------------------------*/


    /**
     * Retrieves the dimensions (size) of this matrix as an integer array of length two.
     *
     * <p>The first element represents the number of rows (height), and the second element
     * represents the number of columns (width).
     *
     * <p>This method ensures data consistency by directly retrieving the sizes from the
     * internal entries array of the matrix instead of relying on pre-defined variables,
     * guaranteeing synchronized lengths even if those variables haven't been updated.
     *
     * @return An array of two integers, where the first element is the number of rows
     *         (height) and the second element is the number of columns (width). If the
     *         matrix has null entries, {@code null} is returned instead.
     *
     * @since  0.1.0
     * @see    #getSize(Matrix)
     */
    public int[ ] getSize() {
        // Retrieve both sizes directly from the entries array, ensuring consistency
        return MatrixUtils.isNullEntries(this)
            ? null : new int[ ] { this.ENTRIES.length, this.ENTRIES[0].length };
    }

    /**
     * Retrieves the dimensions (size) of the given matrix as an integer array
     * of length two.
     *
     * <p>The first element represents the number of rows (height), and the second element
     * represents the number of columns (width).
     *
     * <p>This method ensures data consistency by directly retrieving the sizes from the
     * internal entries array of the matrix instead of relying on pre-defined variables,
     * guaranteeing synchronized lengths even if those variables haven't been updated.
     *
     * @param  m  The matrix from which to retrieve the size.
     * @return    An array of two integers, where the first element is the number of rows
     *            (height) and the second element is the number of columns (width). If the
     *            matrix has null entries, {@code null} is returned instead.
     *
     * @since  1.5.0
     * @see    #getSize()
     */
    public static int[ ] getSize(Matrix m) {
        return MatrixUtils.isNullEntries(m)
            ? null : new int[] { m.ENTRIES.length, m.ENTRIES[0].length };
    }

    /**
     * Retrieves the dimensions (size) of this matrix, acting as an alias for the
     * {@link #getSize()} method. It provides a convenient alternative name for
     * expressing the matrix shape.
     *
     * @return An array of two integers, where the first element is the number of rows
     *         (height) and the second element is the number of columns (width).
     *
     * @since 1.5.0
     * @see   #getSize()
     * @see   #shape(Matrix)
     */
    public int[ ] shape() {
        return this.getSize();
    }

    /**
     * Retrieves the dimensions (size) of the given matrix, acting as an alias for the
     * {@link #getSize(Matrix)} method. It provides a convenient alternative name for
     * expressing the matrix shape.
     *
     * @param  m  The matrix from which to retrieve the size.
     * @return    An array of two integers, where the first element is the number of rows
     *            (height) and the second element is the number of columns (width).
     *
     * @since 1.5.0
     * @see   #getSize(Matrix)
     * @see   #shape()
     */
    public static int[ ] shape(Matrix m) {
        return Matrix.getSize(m);
    }


    /**
     * Retrieves the number of rows (height) in this matrix.
     *
     * <p>This method provides a convenient way to access the row dimension
     * directly from the matrix object.
     *
     * <p>This method is equivalent with {@code m.shape()[0]} or
     * {@code m.getSize()[0]}, with {@code m} represents the matrix object.
     *
     * @return The number of rows in this matrix. If the matrix has null entries,
     *         0 (zero) is returned.
     *
     * @since 1.5.0
     * @see   #getNumRows(Matrix)
     * @see   #getNumCols()
     * @see   #getSize()
     * @see   #shape()
     */
    public int getNumRows() {
        return Matrix.getNumRows(this);
    }

    /**
     * Retrieves the number of rows (height) in the provided matrix.
     *
     * <p>This method provides a convenient way to access the row dimension
     * directly from the matrix object.
     *
     * <p>This method is equivalent with {@code Matrix.shape(m)[0]} or
     * {@code Matrix.getSize(m)[0]}, with {@code m} represents the matrix object.
     *
     * @param  m  The matrix for which to determine the number of rows.
     * @return    The number of rows in the provided matrix. If the matrix has
     *            null entries, 0 (zero) is returned.
     *
     * @since 1.5.0
     * @see   #getNumRows()
     * @see   #getNumCols(Matrix)
     * @see   #getSize(Matrix)
     * @see   #shape(Matrix)
     */
    public static int getNumRows(Matrix m) {
        return MatrixUtils.isNullEntries(m) ? 0 : m.ENTRIES.length;
    }


    /**
     * Retrieves the number of columns (width) in this matrix.
     *
     * <p>This method provides a convenient way to access the column dimension
     * directly from the matrix object.
     *
     * <p>This method is equivalent with {@code m.shape()[1]} or
     * {@code m.getSize()[1]}, with {@code m} represents the matrix object.
     *
     * @return The number of columns in this matrix. If the matrix has null entries,
     *         0 (zero) is returned.
     *
     * @since 1.5.0
     * @see   #getNumCols(Matrix)
     * @see   #getNumRows()
     * @see   #getSize()
     * @see   #shape()
     */
    public int getNumCols() {
        return Matrix.getNumCols(this);
    }

    /**
     * Retrieves the number of columns (width) in the provided matrix.
     *
     * <p>This method provides a convenient way to access the column dimension
     * directly from the matrix object.
     *
     * <p>This method is equivalent with {@code Matrix.shape(m)[1]} or
     * {@code Matrix.getSize(m)[1]}, with {@code m} represents the matrix object.
     *
     * @param  m  The matrix for which to determine the number of columns.
     * @return    The number of columns in the provided matrix. If the matrix has
     *            null entries, 0 (zero) is returned.
     *
     * @since 1.5.0
     * @see   #getNumCols(Matrix)
     * @see   #getNumRows()
     * @see   #getSize(Matrix)
     * @see   #shape(Matrix)
     */
    public static int getNumCols(Matrix m) {
        return MatrixUtils.isNullEntries(m) ? 0 : m.ENTRIES[0].length;
    }


    /**
     * Returns the value at the specified row and column within this matrix.
     *
     * <p>This method is an alias for {@link #getEntry(int, int)} method, also it
     * allows both positive and negative indexing.
     *
     * @param  row  The row index of the desired element.
     * @param  col  The column index of the desired element.
     * @return      The {@code double} value at the specified entry in this matrix.
     *
     * @throws InvalidIndexException  If the row or column index is out of bounds.
     * @throws NullMatrixException    If the entries of this matrix is {@code null}.
     *
     * @since 1.0.0b.7
     * @see   #get(Matrix, int, int)
     * @see   #getEntry(int, int)
     * @see   #getEntries()
     */
    public double get(int row, int col) {
        return this.getEntry(row, col);
    }

    /**
     * Returns the value at the specified row and column within a given matrix.
     *
     * <p>This method is an alias for {@link #getEntry(Matrix, int, int)} method,
     * also it allows both positive and negative indexing.
     *
     * @param  m    The matrix from which to retrieve the element.
     * @param  row  The row index of the desired element.
     * @param  col  The column index of the desired element.
     * @return      The {@code double} value at the specified entry in this matrix.
     *
     * @throws InvalidIndexException  If the row or column index is out of bounds.
     * @throws NullMatrixException    If the entries of this matrix is {@code null}.
     *
     * @since 1.5.0
     * @see   #get(int, int)
     * @see   #getEntry(Matrix, int, int)
     * @see   #getEntries()
     */
    public static double get(Matrix m, int row, int col) {
        return Matrix.getEntry(m, row, col);
    }


    /**
     * Retrieves the value of the element at a specified row and column within
     * this matrix.
     *
     * This method allows for both positive and negative indexing, where negative
     * indices wrap around to the end of the respective dimension. For example,
     * {@code get(m, -1, -1)} would return the value at the last row and last
     * column of this matrix.
     *
     * @param  row  The row index of the desired element.
     * @param  col  The column index of the desired element.
     * @return      The {@code double} value of the specified entry in this matrix.
     *
     * @throws InvalidIndexException  If the row or column index is out of bounds.
     * @throws NullMatrixException    If the provided matrix is {@code null}.
     *
     * @since 1.5.0
     * @see   #get(int, int)
     * @see   #getEntry(Matrix, int, int)
     */
    public double getEntry(int row, int col) {
        return Matrix.getEntry(this, row, col);
    }

    /**
     * Retrieves the value of the element at a specified row and column within
     * a given matrix.
     *
     * This method allows for both positive and negative indexing, where negative
     * indices wrap around to the end of the respective dimension. For example,
     * {@code get(m, -1, -1)} would return the value at the last row and last
     * column of the matrix {@code m}.
     *
     * @param  m    The matrix from which to retrieve the element.
     * @param  row  The row index of the desired element.
     * @param  col  The column index of the desired element.
     * @return      The {@code double} value of the specified entry in the matrix.
     *
     * @throws InvalidIndexException  If the row or column index is out of bounds.
     * @throws NullMatrixException    If the provided matrix is {@code null}.
     *
     * @since 1.5.0
     * @see   #get(Matrix, int, int)
     * @see   #getEntry(int, int)
     */
    public static double getEntry(Matrix m, int row, int col) {
        final int[] mSize = m.getSize();  // Retrieve the matrix shape
        // Allow negative indexing
        row += (row < 0) ? mSize[0] : 0;
        col += (col < 0) ? mSize[1] : 0;

        if (MatrixUtils.isNullEntries(m)) {
            cause = new NullMatrixException(
                "Matrix is null. Please ensure the matrix are initialized.");
        } else if (row >= mSize[0] || row < 0) {
            cause = new InvalidIndexException(String.format(
                "Invalid row index: %d (matrix size: %dx%d)",
                (row < 0) ? (row - mSize[0]) : row, mSize[0], mSize[1]
            ));
        } else if (col >= mSize[1] || col < 0) {
            cause = new InvalidIndexException(String.format(
                "Invalid column index: %d (matrix size: %dx%d",
                (col < 0) ? (col - mSize[1]) : col, mSize[0], mSize[1]
            ));
        }

        // Throw the exception if got one
        if (cause != null) raise(cause);

        return m.ENTRIES[row][col];
    }


    /**
     * Retrieves a two-dimensional array representing the elements of this matrix,
     * preserving data integrity.
     *
     * <p>If the matrix was constructed using the default {@link #Matrix()} constructor
     * without any explicit entries, this method returns {@code null}, indicating that
     * the matrix has not been initialized with any data. To determine whether a matrix
     * has uninitialized ({@code null}) entries, use the
     * {@link MatrixUtils#isNullEntries(Matrix)} helper method.
     *
     * @implNote
     * <p>As of version 1.5.0, this method would returns a deep copy of the internal
     * matrix entries instead of returning the object reference of the entries itself
     * (see <a href="https://github.com/mitsuki31/jmatrix/issues/103">#103</a>),
     * this ensuring that modifications to the returned array do not affect
     * the original matrix data. This is essential for preventing unintended side
     * effects and maintaining data consistency.
     *
     * @return A two-dimensional array of {@code double} values representing the
     *         matrix entries, or {@code null} if the matrix is uninitialized.
     *
     * @since  1.0.0b.5
     * @see    #getReadOnlyEntries()
     * @see    MatrixUtils#isNullEntries(Matrix)
     */
    public double[ ][ ] getEntries() {
        return MatrixUtils.deepCopyOf(this.ENTRIES);
    }

    /**
     * Obtains a read-only view of the matrix elements, safeguarding against
     * unintended modifications.
     *
     * <p>This method offers a secure way to access the matrix elements without
     * the risk of unintended modifications. This method returns a list of lists,
     * where each inner list represents an immutable row of the matrix, ensuring
     * data integrity and preventing accidental changes. Attempting to modify the
     * elements will causing the method to throws an exception
     * {@code UnsupportedOperationException}. If you want to get the matrix
     * elements but can still access and modify each elements, consider to use the
     * {@link #getEntries()} method instead.
     *
     * @implNote
     * <p>The method leverages the {@code Arrays.stream} API introduced in Java 8.
     * This allows for concise and efficient conversion of primitive {@code double}
     * arrays to {@code Double} objects and subsequent creation of unmodifiable
     * lists. However, it's crucial to note that this approach might not be
     * compatible with older Java versions that lack {@code Arrays.stream} functionality.
     *
     * @return An unmodifiable view list of lists containing the matrix entries.
     *         Each inner list represents a single row of the matrix, containing
     *         {@code Double} objects instead of primitive {@code double} values.
     *
     * @throws NullMatrixException  If the entries of this matrix is {@code null}
     *                              or this matrix has not been initialized.
     *
     * @since 1.5.0
     * @see   #getEntries()
     */
    public List<List<Double>> getReadOnlyEntries() {
        if (MatrixUtils.isNullEntries(this)) {
            raise(new NullMatrixException(
                "Matrix is null. Please ensure the matrix are initialized."));
        }

        final int rows = this.getNumRows();  // Get the rows size
        final List<List<Double>> readonlyEntries = new ArrayList<>(rows /* initial size */);

        for (int i = 0; i < rows; i++) {
            // Convert the double (primitive type) array to list of Double (class type)
            List<Double> thisRow = Arrays.stream(this.ENTRIES[i])
                                         .boxed()
                                         .collect(Collectors.toList());  // Convert to List
            // Lock each row and make it unmodifiable
            readonlyEntries.add(Collections.unmodifiableList(thisRow));
        }

        // For the last touch, lock entirely the entries
        // and return the read-only entries
        return Collections.unmodifiableList(readonlyEntries);
    }



    /*---------------------------
    ::      Matrix Display
    ---------------------------*/


    /**
     * Displays and prints this matrix to standard output in Python-style.
     *
     * <p>Displays <code>{@literal <null_matrix>}</code> if the entries
     * of this matrix is {@code null}.<br>
     * To retrieve the elements of this matrix, please refer to {@link #getEntries()} method.
     *
     * <p><b>Output example:</b></p>
     *
     * <pre>&nbsp;
     *   [   [n, n, n, ...],
     *       [n, n, n, ...],
     *       [n, n, n, ...]   ]
     * </pre>
     *
     * @since 0.1.0
     * @see   #display(int)
     * @see   #getEntries()
     */
    final public void display() {
        if (this.ENTRIES != null) {
            System.out.println(this.toString());
        } else {
            System.out.println("<null_matrix>");
        }
    }


    /**
     * Displays and prints the specified row of this matrix to standard output in Python-style.
     *
     * <p>Displays <code>{@literal <null_matrix>}</code> if the entries
     * of this matrix is {@code null}.<br>
     * To retrieve the elements of this matrix, please refer to {@link #getEntries()} method.
     *
     * <p><b>Output example:</b></p>
     *
     * <pre>&nbsp;
     *   [n, n, n, ...]
     * </pre>
     *
     * @param  index                  the index row of this matrix.
     *
     * @throws InvalidIndexException  if the given index is negative value or
     *                                the index is larger than number of rows.
     *
     * @since                         0.2.0
     * @see                           #display()
     * @see                           #getEntries()
     */
    final public void display(int index) {
        if (this.ENTRIES != null) {
            // Check for negative index and throw the exception
            if (index < 0) {
                cause = new InvalidIndexException(
                    "Invalid given index. Index cannot be a negative value.");
            }
            // Check if the given index greater than number of rows this matrix
            else if (index > this.ROWS - 1) {
                cause = new InvalidIndexException(
                    "Invalid given index. Index cannot be larger than number of rows.");
            }

            // Throw the exception if got one
            if (cause != null) raise(cause);

            System.out.println(Arrays.toString(this.ENTRIES[index]));
        } else {
            System.out.println("<null_matrix>");
        }
    }


    /**
     * Displays and prints the given two-dimensional array to standard output in Python-style.
     *
     * <p>Displays <code>{@literal <null_2darray>}</code> if the entries
     * of given two-dimensional array is {@code null} or empty.
     *
     * <p><b>Output example:</b></p>
     *
     * <pre>&nbsp;
     *   [   [n, n, n, ...],
     *       [n, n, n, ...],
     *       [n, n, n, ...]   ]
     * </pre>
     *
     * @param arr  the two-dimensional array to be displayed.
     *
     * @since      0.1.0
     * @see        #display(double[][], int)
     */
    final public static void display(double[ ][ ] arr) {
        if (arr == null || arr.length == 0) {
            System.out.println("<null_2darray>");
        } else {
            System.out.println((new Matrix(arr)).toString());
        }
    }


    /**
     * Displays and prints the specified row of given two-dimensional array to standard output
     * in Python-style.
     *
     * <p>Displays <code>{@literal <null_2darray>}</code> if the entries
     * of given two-dimensional array is {@code null} or empty.
     *
     * <p><b>Output example:</b></p>
     *
     * <pre>&nbsp;
     *   [n, n, n, ...]
     * </pre>
     *
     * @param  arr                    the two-dimensional array to be displayed.
     * @param  index                  the index row of two-dimensional array.
     *
     * @throws InvalidIndexException  if the given index is negative value or
     *                                the index is larger than number of rows.
     *
     * @since                         0.2.0
     * @see                           #display(double[][])
     */
    final public static void display(double[ ][ ] arr, int index) {
        if (arr == null || arr.length == 0) {
            System.out.println("<null_2darray>");
        } else {
            // Checking index value
            if (index < 0) {
                cause = new InvalidIndexException(
                    "Invalid given index. Index cannot be a negative value.");
            } else if (index > arr.length - 1) {
                cause = new InvalidIndexException(
                    "Invalid given index. Index cannot be larger than number of rows.");
            }

            // Throw the exception if got one
            if (cause != null) raise(cause);

            System.out.println(Arrays.toString(arr[index]));
        }
    }



    /*=========================================
    ::
    ::  OVERRIDEN METHODS
    ::
    =========================================*/


    /**
     * Returns a {@code String} representation of this matrix.
     *
     * <p>Use the {@link #display()} method to display this matrix in simply way.
     *
     * @return the {@code String} representation of this matrix in Python-style array notation.
     *
     * @since  1.0.0b.5
     */
    @Override
    public String toString() {
        final int rows = this.ROWS;
        final int cols = this.COLS;
        String strMatrix;

        strMatrix = "[   "; // row head
        for (int r = 0; r < rows; r++) {
            strMatrix += "["; // column head
            for (int c = 0; c < cols; c++) {
                strMatrix += this.ENTRIES[r][c];
                if (c != cols - 1) strMatrix += ", ";
            }

            strMatrix += "]"; // column tail
            if (r != rows - 1) strMatrix += String.format(
                ",%s    ", System.lineSeparator());
        }
        strMatrix += "   ]"; // row tail

        return strMatrix;
    }


    /**
     * Compares this matrix with the specified object for equality.
     *
     * <p>Returns {@code true} if and only if the specified object is
     * also a matrix and both matrices have the same dimensions and
     * equal elements at corresponding positions.
     *
     * @param  obj  The object to compare for equality with this matrix.
     *
     * @return      {@code true} if the specified object is equal to this matrix, {@code false} otherwise.
     *
     * @since       1.0.0
     * @see         MatrixUtils#isEquals(Matrix, Matrix)
     * @see         MatrixUtils#isEqualsSize(Matrix, Matrix)
     * @see         com.mitsuki.jmatrix.core.MatrixUtils
     */
    @Override
    public boolean equals(Object obj) {
        // If the memory references are equal, returns true
        if (this == obj) {
            return true;
        }

        // If the given object is null, returns false
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        // Check for the given object are instance of Matrix class
        if (obj instanceof Matrix) {
            // Typecast the given object to Matrix class
            Matrix otherMatrix = (Matrix) obj;

            /*
            * Check the null entries for both matrices.
            * If both matrices has null entries, returns true.
            */
            if ( MatrixUtils.isNullEntries(this) &&
                    MatrixUtils.isNullEntries(otherMatrix) ) {
                return true;
            }

            /*
            * Check whether the entire elements are equal.
            * The "isEquals" method would also checks the dimensions,
            * which is would returns false if both are not same.
            */
            else if ( MatrixUtils.isEquals(this, otherMatrix) ) {
                return true;
            }

            // Check and compare the hash code for both objects
            else if (this.hashCode() == obj.hashCode()) {
                return true;
            }
        }

        return false;
    }


    /**
     * Returns the hash code value for this matrix.
     *
     * <p>It combines the hash code of the matrix's dimensions by multiplying the
     * number of rows and columns, then computing the sine of the result, and finally
     * multiplying it by a large constant factor (10e+6), taking the absolute value,
     * and adding 43 times the hash code of the superclass.
     *
     * <p>It is recommended to use this method in conjunction with the {@link #equals(Object)} method
     * to ensure consistent behavior when working with hash-based data structures.
     *
     * @return the hash code value for this matrix.
     *
     * @since  1.1.0
     * @see    #equals(Object)
     */
    @Override
    public int hashCode() {
        return (int) Math.abs(10e+6 * Math.sin(this.ROWS * this.COLS)) * 43 + super.hashCode();
    }
}
