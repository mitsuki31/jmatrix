// :: ------------------- :: //
/* --   MATRIX BUILDER    -- */
// :: ------------------- :: //

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

package com.mitsuki.jmatrix;

import com.mitsuki.jmatrix.exception.IllegalMatrixSizeException;
import com.mitsuki.jmatrix.exception.InvalidIndexException;
import com.mitsuki.jmatrix.exception.JMatrixBaseException;
import com.mitsuki.jmatrix.exception.MatrixArrayFullException;
import com.mitsuki.jmatrix.exception.NullMatrixException;
import com.mitsuki.jmatrix.util.Options;
import com.mitsuki.jmatrix.core.MatrixUtils;

import java.util.Arrays;

/**
 * The <b>Matrix</b> class represents a two-dimensional (2D) array of {@code double}s.
 * An array with two dimensions (has a rows and columns), also can be called a matrix.
 *
 * <p>It provides methods for creating, accessing and manipulating matrices,
 * as well as basic matrix operations such as:
 * <ul>
 * <li> {@linkplain #sum(Matrix)    Addition}
 * <li> {@linkplain #sub(Matrix)    Subtraction}
 * <li> {@linkplain #mult(Matrix)   Matrix multiplication}
 * <li> {@linkplain #mult(double)   Scalar multiplication}
 * <li> {@linkplain #transpose()    Transposition}
 * </ul>
 *
 * <p><b>For example:</b></p>
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
 * @version  2.4, 22 August 2023
 * @since    0.1.0
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *           Apache License 2.0</a>
 *
 * @see      com.mitsuki.jmatrix.core.MatrixUtils
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
     *
     * @see java.lang.Throwable
     */
    private static Throwable cause = null;


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
        if (cause != null) Options.raiseError(cause);

        // Copy the sizes from input parameters
        this.ROWS = rows;
        this.COLS = cols;

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
        if (cause != null) Options.raiseError(cause);

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
            Options.raiseError(new NullMatrixException(
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
        if (cause != null) Options.raiseError(cause);

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
        if (arr == null || arr.length == 0) {
            Options.raiseError(new NullMatrixException(
                "Given two-dimensional array is null. Please ensure the array has valid elements."));
        }

        // Retrieve the sizes
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
     */
    public static Matrix identity(int n) {
        // Check for negative value on input argument
        if (n < 1) {
            Options.raiseError(new IllegalMatrixSizeException(
                "Sizes of identity matrix cannot be lower than 1."));
        }

        // Create the identity matrix with size "n x n"
        double[ ][ ] entries = new double[n][n];

        for (int i = 0; i < n; i++) {
            entries[i][i] = 1.0;
        }

        return new Matrix(entries);
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
     * @throws     MatrixArrayFullException  if the matrix cannot be added more values.
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
                throw new MatrixArrayFullException(
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
                Options.raiseError(jme);
            }
        } catch (final RuntimeException re) {
            Options.raiseError(re);
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
     * @throws     MatrixArrayFullException  if the matrix cannot be added more values.
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
                throw new MatrixArrayFullException(
                    "Cannot add values anymore, Matrix is already full");
            }
        } catch (final RuntimeException re) {
            Options.raiseError(re);
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
        if (this.ENTRIES == null) {
            cause = new NullMatrixException(
                "This matrix is null. " +
                "Please ensure the matrix are initialized before performing addition."
            );
        } else if (m == null || m.ENTRIES == null) {
            cause = new NullMatrixException(
                "Given matrix is null. " +
                "Please ensure the matrix are initialized before performing addition."
            );
        }
        // Else throw "IllegalMatrixSizeException" if the matrices size are not same
        else if (this.ROWS != m.ROWS || this.COLS != m.COLS) {
            cause = new IllegalMatrixSizeException(
                String.format(
                    "Cannot perform addition for two matrices with different dimensions. " +
                    "A = %dx%d, B = %dx%d",
                    this.ROWS, this.COLS, m.getSize()[0], m.getSize()[1]
                )
            );
        }

        // Throw the exception if got one
        if (cause != null) Options.raiseError(cause);

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
        if (cause != null) Options.raiseError(cause);

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
        if (cause != null) Options.raiseError(cause);

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
        if (cause != null) Options.raiseError(cause);

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
        if (cause != null) Options.raiseError(cause);

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
        if (cause != null) Options.raiseError(cause);

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

        if (cause != null) Options.raiseError(cause);

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

        if (cause != null) Options.raiseError(cause);

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
        if (cause != null) Options.raiseError(cause);

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
        if (cause != null) Options.raiseError(cause);

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
        if (cause != null) Options.raiseError(cause);

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
        if (cause != null) Options.raiseError(cause);

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
            Options.raiseError(new NullMatrixException(
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
            Options.raiseError(new NullMatrixException(
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
     * <p><b>Note:</b></p>
     *
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
            Options.raiseError(new NullMatrixException(
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
     * <p><b>Note:</b></p>
     *
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
            Options.raiseError(new NullMatrixException(
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
     * <p><b>Note:</b></p>
     *
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
            Options.raiseError(new NullMatrixException(
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
            Options.raiseError(new NullMatrixException(
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
            Options.raiseError(new NullMatrixException(
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
            Options.raiseError(new IllegalMatrixSizeException(
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
            Options.raiseError(new IllegalMatrixSizeException(
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
     * Checks if this matrix is lower triangular.
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
     * Checks if the given square matrix is lower triangular.
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
            Options.raiseError(new NullMatrixException(
                "Matrix is null. Please ensure the matrix have been initialized.")
            );
        }

        // The matrix must be square
        else if (!m.isSquare()) {
            Options.raiseError(new IllegalMatrixSizeException(
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
     * Checks if the given square two-dimensional array is lower triangular.
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
            Options.raiseError(new NullMatrixException(
                "Array is null. Please ensure the array has valid elements.")
            );
        }

        // The two-dimensional array must be square
        else if (!Matrix.isSquare(arr)) {
            Options.raiseError(new IllegalMatrixSizeException(
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
     * Checks if this matrix is upper triangular.
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
     * Checks if the given square matrix is upper triangular.
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
            Options.raiseError(new NullMatrixException(
                "Matrix is null. Please ensure the matrix have been initialized.")
            );
        }

        // The matrix must be square
        else if (!m.isSquare()) {
            Options.raiseError(new IllegalMatrixSizeException(
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
     * Checks if the given square two-dimensional array is upper triangular.
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
            Options.raiseError(new NullMatrixException(
                "Array is null. Please ensure the array has valid elements.")
            );
        }

        // The matrix must be square
        else if (!Matrix.isSquare(arr)) {
            Options.raiseError(new IllegalMatrixSizeException(
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
            Options.raiseError(nme);
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
     * @since                         0.2.0
     * @see                           #change(double ...)
     * @see                           #change(double)
     */
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

        if (cause != null) Options.raiseError(cause);

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
     * @since                            0.2.0
     * @see                              #select(int)
     * @see                              #change(double)
     */
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
        if (cause != null) Options.raiseError(cause);

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
     * @since                         0.2.0
     * @see                           #select(int)
     * @see                           #change(double ...)
     */
    public void change(double value) {
        // Check if the user have not select any index row
        // If user have not then it will immediately raise the exception
        if (!this.hasSelect) {
            Options.raiseError(new InvalidIndexException(
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
            Options.raiseError(new NullMatrixException(
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
            Options.raiseError(new NullMatrixException(
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
            Options.raiseError(new NullMatrixException(
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
            Options.raiseError(new NullMatrixException(
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
     * Retrieves the sizes (also known as, number of rows and columns) of this matrix.
     *
     * <p>If the entries of this matrix is {@code null} it will returns {@code null} instead.
     * This also prevents unexpected results that could be happen when comparing the matrix sizes.<br>
     * To check whether the matrix has {@code null} entries, please refer to {@link MatrixUtils#isNullEntries(Matrix)}.
     *
     * <p><b>For example:</b></p>
     *
     * <p>Retrieve the number of rows and columns:
     *
     * <pre><code class="language-java">&nbsp;
     *   int[] sizes = m.getSize();
     * </code></pre>
     *
     * <p>Only retrieve the number of rows:
     *
     * <pre><code class="language-java">&nbsp;
     *   int sizeRow = m.getSize()[0];
     * </code></pre>
     *
     * <p>Only retrieve the number of columns:
     *
     * <pre><code class="language-java">&nbsp;
     *   int sizeColumn = m.getSize()[1];
     * </code></pre>
     *
     * @return an {@code Integer} list containing number of rows<sup>[0]</sup>
     *         and columns<sup>[1]</sup>.
     *
     * @since  0.1.0
     * @see    #getEntries()
     */
    public int[ ] getSize() {
        // Now if this matrix has null entries, then returns null
        if (this.ENTRIES == null) return null;

        // It would return list: [<rows>, <cols>]
        return new int[ ] { this.ROWS, this.COLS };
    }


    /**
     * Returns the value at the specified row and column in this matrix.
     *
     * <p>If the given index is a negative value, then it will count from the last entry.<br>
     * For getting all elements of this matrix, please refer to {@link #getEntries()} method.
     *
     * <p><b>For example:</b></p>
     *
     * <pre><code class="language-java">&nbsp;
     *   double[][] arr = {
     *       { 1, 2 },
     *       { 3, 4 }
     *   };
     *
     *   Matrix m = new Matrix(arr);
     *   double lastEntry = m.get(-1, -1);
     *
     *   System.out.println(lastEntry);
     * </code></pre>
     *
     * <p><b>Output:</b></p>
     * <pre>4</pre>
     *
     * @param  row                    the row index.
     * @param  col                    the column index.
     *
     * @return                        the value at the specified position in this matrix.
     *
     * @throws InvalidIndexException  if the given index is out of range.
     * @throws NullMatrixException    if the entries of this matrix is {@code null}.
     *
     * @since                         1.0.0b.7
     * @see                           #getEntries()
     * @see                           #getSize()
     */
    public double get(int row, int col) {
        if (this.ENTRIES == null) {
            cause = new NullMatrixException(
                "Matrix is null. Please ensure the matrix are initialized.");
        } else if (row >= this.ROWS || (row < 0 && (row + this.ROWS) >= this.ROWS || (row + this.ROWS) < 0)) {
            cause = new InvalidIndexException(
                String.format(
                    "Invalid row index: %d (matrix size: %dx%d)",
                    row, this.ROWS, this.COLS
                )
            );
        } else if (col >= this.COLS || (col < 0 && (col + this.COLS) >= this.COLS || (col + this.COLS) < 0)) {
            cause = new InvalidIndexException(
                String.format(
                    "Invalid column index: %d (matrix size: %dx%d",
                    col, this.ROWS, this.COLS
                )
            );
        }

        // Throw the exception if got one
        if (cause != null) Options.raiseError(cause);

        // Check for negative index for both inputs
        if (row < 0) {
            row += this.ROWS;
        }
        if (col < 0) {
            col += this.COLS;
        }

        return this.ENTRIES[row][col];
    }


    /**
     * Returns the {@code double} two-dimensional array representation of this matrix elements.
     *
     * <p><b>Note:</b></p>
     * If the matrix constructed by using {@link #Matrix()} constructor,
     * this method would returns {@code null} instead (similar with the entries).
     *
     * @return a two-dimensional array that represents entries of this matrix,
     *         returns {@code null} instead if the entries is uninitialized.
     *
     * @since  1.0.0b.5
     * @see    #get(int, int)
     * @see    #getSize()
     * @see    MatrixUtils#isNullEntries(Matrix)
     */
    public double[ ][ ] getEntries() {
        return this.ENTRIES;
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
            if (cause != null) Options.raiseError(cause);

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
            if (cause != null) Options.raiseError(cause);

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
