// :: ------------------- :: //
/* --   MATRIX BUILDER    -- */
// :: ------------------- :: //

// Copyright 2023 Ryuu Mitsuki
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
package com.mitsuki.jmatrix;

// -**- Built-in Package -**- //
import java.util.Arrays;
import java.lang.Math;

// -**- Local Package -**- //
import com.mitsuki.jmatrix.core.*;
import com.mitsuki.jmatrix.util.Options;


/**
* Creates matrix array with specified sizes of rows and columns.<br>
* Also this class provides basic matrix operations.<br>
*
* @version  1.2
* @since    0.1.0
* @author   Ryuu Mitsuki
*/
public class Matrix
{
    // - Private attributes
    private double[ ][ ] ENTRIES = null; // Create null matrix
    private int ROWS = 0,               // Initialize matrix rows
                COLS = 0,               // Initialize matrix columns
                index = 0,              // Index for add() function
                selectedIndex = -1;

    private boolean isSquare; // Boolean value to check a square matrix



    //// ----------------- ////
    // -**- CONSTRUCTOR -**- //
    //// ----------------- ////

    /**
    * Constructs new <b>Matrix</b> object without any parameter.<br>
    * This would creates a {@code null} matrix (not {@code null} type matrix).<br>
    *
    * @since 0.2.0
    * @see   Matrix(int, int)
    * @see   Matrix(int, int, int)
    * @see   Matrix(double[][])
    */
    public Matrix() {}


    /**
    * Constructs new <b>Matrix</b> object with specified sizes of rows and columns.<br>
    *
    * @param rows  a value for size of matrix row.
    * @param cols  a value for size of matrix column.
    *
    * @throws IllegalArgumentException
    *         if the two input sizes is negative value
    *         or one of two input sizes are equal to zero.
    *
    * @since 0.1.0
    * @see   Matrix()
    * @see   Matrix(int, int, int)
    * @see   Matrix(double[][])
    */
    public Matrix(int rows, int cols) {
        // Check for negative or zero value size
        try {
            if (rows < 0) {
                throw new IllegalArgumentException("Value for rows cannot be negative value");
            } else if (cols < 0) {
                throw new IllegalArgumentException("Value for columns cannot be negative value");
            } else if (rows == 0 || cols == 0) {
                throw new IllegalArgumentException("Cannot create a matrix with zero size between rows and columns");
            }
        } catch (final IllegalArgumentException iae) {
            throw new JMBaseException(iae);
        }

        this.ROWS = rows;
        this.COLS = cols;
        this.ENTRIES = new double[rows][cols];

        if (rows != cols) {
            this.isSquare = false;
        } else {
            this.isSquare = true;
        }
    }


    /**
    * Constructs new <b>Matrix</b> object with specified sizes of rows and columns.<br>
    * Also fills out entire elements of matrix with specified value.<br>
    *
    * @param rows  the value for size of matrix row.
    * @param cols  the value for size of matrix column.
    * @param val   the value to filled out into all entries of matrix.
    *
    * @throws IllegalArgumentException
    *         if the two input sizes is negative value
    *         or one of two input sizes are equal to zero.
    *
    * @since 0.1.0
    * @see   Matrix()
    * @see   Matrix(int, int)
    * @see   Matrix(double[][])
    */
    public Matrix(int rows, int cols, int val) {
        // Check for negative or zero value at input arguments
        try {
            if (rows < 0) {
                throw new IllegalArgumentException("Value for rows cannot be negative value");
            } else if (cols < 0) {
                throw new IllegalArgumentException("Value for columns cannot be negative value");
            } else if (rows == 0 || cols == 0) {
                throw new IllegalArgumentException("Cannot create a matrix with zero size between rows and columns");
            }
        } catch (final IllegalArgumentException iae) {
            throw new JMBaseException(iae);
        }

        this.ROWS = rows;
        this.COLS = cols;

        this.ENTRIES = new double[rows][cols];

        // Fill all matrix entries with "val"
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.ENTRIES[r][c] = val;
            }
        }
    }

    /**
    * Constructs <b>Matrix</b> object with specified array.<br>
    * Converts the array into <b>Matrix</b> object, without changing it's values.<br>
    *
    * @param array  an array to be converted into <b>Matrix</b> object.
    *
    * @since 1.0.0
    * @see   Matrix()
    * @see   Matrix(int, int)
    * @see   Matrix(int, int, int)
    */
    public Matrix(double[ ][ ] array) {
        this.ROWS = array.length;
        this.COLS = array[0].length;
        this.ENTRIES = array;
    }

    //// ----------------------- ////
    // -**- [END] CONSTRUCTOR -**- //
    //// ----------------------- ////




    //// --------------------- ////
    // -**- PRIVATE METHODS -**- //
    //// --------------------- ////

    /**
    * Calculates the dot product of a specified cell in two arrays.<br>
    * This method is used for matrix multiplication operation.<br>
    *
    * @param  matrixA  the first array.
    * @param  matrixB  the second array.
    * @param  row      a row index of the cell to be calculated.
    * @param  col      a column index of the cell to be calculated.
    *
    * @return the dot product of the specified cell of two arrays.
    *
    * @since  0.2.0
    * @see    #mult
    */
    private static double multCell(double[ ][ ] arrayA, double[ ][ ] arrayB, int row, int col) {
        double result = 0;
        for (int i = 0; i < arrayB.length; i++) {
            result += (arrayA[row][i] * arrayB[i][col]);
        }

        return result;
    }


    //// --------------------------- ////
    // -**- [END] PRIVATE METHODS -**- //
    //// --------------------------- ////


    /**
    * Creates a new matrix with specified rows and columns.<br>
    * Can be called even the matrix not {@code null}.<br>
    *
    * @param  rows  a value for new matrix row size.
    * @param  cols  a value for new matrix column size.
    *
    * @throws IllegalArgumentException
    *         if the two input sizes is negative value
    *         or one of two input sizes are equal to zero.
    *
    * @since  0.2.0
    * @see    #create(double[][])
    */
    public void create(int rows, int cols) {
        // Check for negative or zero value at input arguments
        try {
            if (rows < 0) {
                throw new IllegalArgumentException("Value for rows cannot be negative value");
            } else if (cols < 0) {
                throw new IllegalArgumentException("Value for columns cannot be negative value");
            } else if (rows == 0 || cols == 0) {
                throw new IllegalArgumentException("Cannot create matrix with zero size between rows or columns");
            }
        } catch (final IllegalArgumentException iae) {
            try {
                throw new JMBaseException(iae);
            } catch(final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        this.ROWS = rows;
        this.COLS = cols;
        this.ENTRIES = new double[rows][cols];

        this.index = 0; // reset index row
    }


    /**
    * Creates new matrix with specified array.<br>
    * Sizes of new matrix (<i>rows and columns</i>) will be equated to array size.<br>
    * Can be called even the matrix not {@code null}.<br>
    *
    * @param  array  the array to be created into new matrix.
    *
    * @since  1.0.0
    * @see    #create(int, int)
    */
    public void create(double[ ][ ] array) {
        this.ROWS = array.length;
        this.COLS = array[0].length;
        this.ENTRIES = array;
    }


    /**
    * Duplicates current matrix to another matrix object.<br>
    *
    * @return the copied of current matrix with all of its attributes.
    *
    * @throws NullMatrixException
    *         if this matrix is {@code null}.
    *
    * @since  0.2.0
    */
    public Matrix copy() {
        if (this.ENTRIES == null) {
            try {
                throw new NullMatrixException("Cannot copy the matrix, because current matrix is null");
            } catch (final NullMatrixException nme) {
                Options.raiseError(nme);
            }
        }

        // Create new and copy the matrix
        Matrix copiedMatrix = new Matrix(this.ROWS, this.COLS);
        copiedMatrix.ENTRIES = this.ENTRIES;

        return copiedMatrix;
    }


    /**
    * Selects matrix column with specified index,
    * this method should be combined with {@link #change} method.<br>
    *
    * @param  index  the index row to be selected.
    *
    * @return self.
    *
    * @throws NullMatrixException
    *         if current matrix is {@code null}.
    * @throws InvalidIndexException
    *         if the input index is negative value or larger than this matrix rows.
    *
    * @since  0.2.0
    * @see    #change
    */
    public Matrix select(final int index) {
        try {
            // Check for null matrix
            if (this.ENTRIES == null) {
                throw new NullMatrixException("Cannot select matrix row, because current matrix is null");
            }
            // Check for negative index
            else if (index < 0) {
                throw new InvalidIndexException("Index must be positive value");
            }
            // Check for given index is greater than total rows
            else if (index > this.ROWS - 1) {
                throw new InvalidIndexException("Index is too larger than total matrix rows");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        this.selectedIndex = index;

        return this; // return self
    }


    //// ---------------- ////
    // -**- ADD VALUES -**- //
    //// ---------------- ////

    /**
    * Fills the column with specified array.<br>
    * It can be an array or inserts the values one by one.<br>
    *
    * @param  values  the values to be added into matrix column.
    *
    * @throws NullMatrixException
    *         if current matrix is {@code null}.
    * @throws MatrixArrayFullException
    *         if the matrix cannot be added more values.
    * @throws IllegalMatrixSizeException
    *         if the given argument is overcapacity to matrix column
    *         or not enough argument to fill the column.
    *
    * @since  0.1.0
    * @see    #add(int)
    */
    public void add(double ... values) {
        try {
            // Throw "NullMatrixException" if current matrix array is null
            if (this.ENTRIES == null) {
                throw new NullMatrixException("Cannot add values, becuase current Matrix is null");
            }
            else if (this.index >= this.ROWS) {
                throw new MatrixArrayFullException("Cannot add values anymore, Matrix is already full");
            }
            // Length of values list shouldn't greater than total matrix columns
            else if (values.length > this.COLS) {
                throw new IllegalMatrixSizeException("Too many arguments for matrix with columns " + this.COLS);
            }
            // And shouldn't be less than total matrix columns
            else if (values.length < this.COLS) {
                throw new IllegalMatrixSizeException("Not enough argument for matrix with columns " + this.COLS);
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Iterate values list and fill elements of matrix array
        int i = 0;
        for (double val : values) {
            this.ENTRIES[this.index][i++] = val;
        }

        this.index++; // increment index of matrix row
    }


    /**
    * Fills the column with repeated of specified value.<br>
    *
    * @param  value  the value to fills the matrix column.
    *
    * @throws NullMatrixException
    *         if current matrix is {@code null}.
    * @throws MatrixArrayFullException
    *         if the matrix cannot be added more values.
    *
    * @since  0.1.0
    * @see    #add(double ...)
    */
    public void add(double value) {
        try {
            // Throw "NullMatrixException" when matrix array is null
            if (this.ENTRIES == null) {
                throw new NullMatrixException("Matrix array is null, cannot add some values");
            }
            /** ---
            - Throw "MatrixArrayFullException" while attempt to add
                a new column, but the index has equal to total matrix rows
            --- **/
            else if (this.index >= this.ROWS) {
                throw new MatrixArrayFullException("Cannot add values anymore, Matrix is already full");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
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

    //// ---------------------- ////
    // -**- [END] ADD VALUES -**- //
    //// ---------------------- ////


    //// ------------------- ////
    // -**- CHANGE VALUES -**- //
    //// ------------------- ////

    /**
    * Changes all values at specified column with an array.<br>
    * Uses this method only when already called {@link #select(int)} method.<br>
    *
    * @param  values  the values to changes all values at specified column.
    *
    * @throws IllegalMatrixSizeException
    *         if the given argument is overcapacity to matrix column
    *         or not enough argument to fill the column.
    * @throws NegativeArraySizeException
    *         if attempts to call this method but haven't called {@code select} method.
    *
    * @since  0.2.0
    * @see    #select(int)
    * @see    #change(double)
    */
    public void change(double ... values) {
        // Checking values size which from argument parameter
        try {
            if (values.length > this.COLS) {
                throw new IllegalMatrixSizeException("Too many arguments for matrix with columns " + this.COLS);
            }
            else if (values.length < this.COLS) {
                throw new IllegalMatrixSizeException("Not enough argument for matrix with columns " + this.COLS);
            }
            else if (this.selectedIndex == -1) {
                throw new NegativeArraySizeException("Selected index is negative value");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Change values of matrix column with values from argument parameter
        for (int i = 0; i < this.COLS; i++) {
            this.ENTRIES[this.selectedIndex][i] = values[i];
        }

        this.selectedIndex = -1; // reset to default
    }


    /**
    * Changes all values at specified column with repeated of specific value.<br>
    * Uses this method only when you've called {@link #select(int)} method.<br>
    *
    * @param  value  the value to changes all values at specified column.
    *
    * @throws NegativeArraySizeException
    *         if attempts to call this method but haven't called {@code select} method.
    *
    * @since  0.2.0
    * @see    #select(int)
    * @see    #change(double ...)
    */
    public void change(double value) {
        try {
            if (this.selectedIndex == -1) {
                throw new NegativeArraySizeException("Selected index is negative value");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create new array with same value
        double[ ] values = new double[this.COLS];
        for (int i = 0; i < this.COLS; i++) {
            values[i] = value;
        }

        for (int i = 0; i < this.COLS; i++) {
            this.ENTRIES[this.selectedIndex][i] = values[i];
        }

        this.selectedIndex = -1; // reset to default
    }

    //// ------------------------- ////
    // -**- [END] CHANGE VALUES -**- //
    //// ------------------------- ////




    //// ----------------------- ////
    // -**- MATRIX OPERATIONS -**- //
    //// ----------------------- ////


    /////////////////////////
    ///     SUMMATION     ///
    /////////////////////////

    /**
    * Operates addition for current matrix and other matrix object,
    * and both matrices should be same size.<br>
    *
    * @param  other  the matrix object as addend.
    *
    * @since  0.1.0
    * @see    #sum(double[][])
    * @see    #sum(double[][], double[][])
    * @see    #sum(Matrix, Matrix)
    */
    public void sum(Matrix other) {
        try {
            // Throw "NullMatrixException" if current Matrix or given Matrix is null
            if (this.ENTRIES == null) {
                throw new NullMatrixException("Cannot operate addition, because current matrix is \"null\"");
            } else if (other == null) {
                throw new NullMatrixException("Cannot operate addition, because <param1> is \"null\" matrix");
            }
            // Else throw "IllegalMatrixSizeException" if the matrices size are not same
            else if (this.ROWS != other.ROWS || this.COLS != other.COLS) {
                throw new IllegalMatrixSizeException("Cannot summing up two matrices with different size");
            }
        } catch (final Exception e) {
            try {
                 throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create new matrix for the result
        double[ ][ ] result = new double[this.ROWS][other.COLS];

        // Iterate over each element of the matrices and add the corresponding values together
        for (int i = 0; i < this.ROWS; i++) {
            for (int j = 0; j < this.COLS; j++) {
                result[i][j] = this.ENTRIES[i][j] + other.ENTRIES[i][j];
            }
        }

        this.ENTRIES = result; // copy the result to this matrix
    }


    /**
    * Operates addition for current matrix and an array,
    * and both objects should be same size.<br>
    *
    * @param  array  an array as addend.
    *
    * @since  0.1.0
    * @see    #sum(Matrix)
    * @see    #sum(double[][], double[][])
    * @see    #sum(Matrix, Matrix)
    */
    public void sum(double[ ][ ] array) {
        try {
            // Throw "NullMatrixException" if current Matrix or given array is null
            if (this.ENTRIES == null) {
                throw new NullMatrixException("Cannot operate summation, because current matrix is \"null\"");
            } else if (array == null) {
                throw new NullMatrixException("Cannot operate summation, because <param1> is \"null\" array");
            }
            // Else throw "IllegalMatrixSizeException" if the matrices are not same size
            else if (this.ROWS != array.length || this.COLS != array[0].length) {
                throw new IllegalMatrixSizeException("Cannot summing up two objects with a different size");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create new matrix for the result
        double[ ][ ] result = new double[this.ROWS][array[0].length];

        // Using nested loop for iterate over each element of matrix
        for (int i = 0; i < this.ROWS; i++) {
            for (int j = 0; j < this.COLS; j++) {
                result[i][j] = this.ENTRIES[i][j] + array[i][j];
            }
        }

        this.ENTRIES = result; // copy the result to Matrix
    }


    /**
    * Operates addition for two arrays,
    * and both arrays should be same size.<br>
    *
    * @param  matrixA  the first array as addend.
    * @param  matrixB  the second array as addend.
    *
    * @return the array which contains the sum of two arrays.
    *
    * @since  0.2.0
    * @see    #sum(Matrix)
    * @see    #sum(double[][])
    * @see    #sum(Matrix, Matrix)
    */
    public static double[ ][ ] sum(double[ ][ ] matrixA, double[ ][ ] matrixB) {
        try {
            // Throw "NullMatrixException" if matrix array is null
            if (matrixA == null) {
                throw new NullMatrixException("Cannot operate summation, because <param1> is \"null\" array");
            } else if (matrixB == null) {
                throw new NullMatrixException("Cannot operate summation, because <param2> is \"null\" array");
            }
            // Else throw "IllegalMatrixSizeException" if the both matrices and are not same size
            else if (matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length) {
                throw new IllegalMatrixSizeException("Cannot summing up two arrays with a different size");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create a new matrix array
        double[ ][ ] result = new double[matrixA.length][matrixB[0].length];

        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                result[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }

        return result;
    }


    /**
    * Operates addition for two matrices,
    * and both matrices should be same size.<br>
    *
    * @param  matrixA  the first matrix object as addend.
    * @param  matrixB  the second matrix object as addend.
    *
    * @return the matrix object which contains the sum of two matrices.
    *
    * @since  0.2.0
    * @see    #sum(Matrix)
    * @see    #sum(double[][])
    * @see    #sum(double[][], double[][])
    */
    public static Matrix sum(Matrix matrixA, Matrix matrixB) {
        try {
            // Throw "NullMatrixException" if both matrices is null
            if (matrixA.ENTRIES == null) {
                throw new NullMatrixException("Cannot operate summation, because <param1> is \"null\" matrix");
            } else if (matrixB.ENTRIES == null) {
                throw new NullMatrixException("Cannot operate summation, because <param2> is \"null\" matrix");
            }
            // Else throw "IllegalMatrixSizeException" if both matrices size are not same
            else if (matrixA.ROWS != matrixB.ROWS || matrixA.COLS != matrixB.COLS) {
                throw new IllegalMatrixSizeException("Cannot summing up two matrices with a different size");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create new matrix object
        Matrix matrixRes = new Matrix(matrixA.ENTRIES.length, matrixB.ENTRIES[0].length);

        for (int i = 0; i < matrixA.ENTRIES.length; i++) {
            for (int j = 0; j < matrixB.ENTRIES[0].length; j++) {
                matrixRes.ENTRIES[i][j] = matrixA.ENTRIES[i][j] + matrixB.ENTRIES[i][j];
            }
        }

        return matrixRes;
    }




    ///////////////////////////
    ///     SUBTRACTION     ///
    ///////////////////////////

    /**
    * Operates subtraction for current matrix and other matrix object,
    * and both matrices should be same size.<br>
    *
    * @param other  the matrix object as subtrahend.
    *
    * @since 0.1.0
    * @see   #sub(double[][])
    * @see   #sub(double[][], double[][])
    * @see   #sub(Matrix, Matrix)
    */
    public void sub(Matrix other) {
        try {
            // Throw "NullMatrixException" if current Matrix or given Matrix is null
            if (this.ENTRIES == null) {
                throw new NullMatrixException("Cannot operate subtraction, because current matrix is \"null\"");
            } else if (other == null) {
                throw new NullMatrixException("Cannot operate subtraction, because <param1> is \"null\" matrix");
            }
            // Else throw "IllegalMatrixSizeException" if both matrices are not same size
            else if (this.ROWS != other.ROWS || this.COLS != other.COLS) {
                throw new IllegalMatrixSizeException("Cannot subtract two matrices with different size");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create new matrix for the result
        double[ ][ ] result = new double[this.ROWS][other.COLS];

        // Iterate over each element of all matrices and subtract each values together
        for (int i = 0; i < this.ROWS; i++) {
            for (int j = 0; j < this.COLS; j++) {
                result[i][j] = this.ENTRIES[i][j] - other.ENTRIES[i][j];
            }
        }

        this.ENTRIES = result; // copy the result to this matrix
    }


    /**
    * Operates subtraction for current matrix and an array,
    * and both objects should be same size.<br>
    *
    * @param array  an array as subtrahend.
    *
    * @since 0.1.0
    * @see   #sub(Matrix)
    * @see   #sub(double[][], double[][])
    * @see   #sub(Matrix, Matrix)
    */
    public void sub(double[ ][ ] array) {
        try {
            // Throw "NullMatrixException" if current matrix is null
            if (this.ENTRIES == null) {
                throw new NullMatrixException("Cannot operate subtraction, because current matrix is \"null\"");
            } else if (array == null) {
                throw new NullMatrixException("Cannot operate subtraction, because <param1> is \"null\" array");
            }
            // Else throw "IllegalMatrixSizeException" if the matrices are not same size
            else if (this.ROWS != array.length || this.COLS != array[0].length) {
                throw new IllegalMatrixSizeException("Cannot subtract two matrices with different size");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create new matrix for the result
        double[ ][ ] result = new double[this.ROWS][array[0].length];

        // Iterate over each element of all matrices and subtract each values together
        for (int i = 0; i < this.ROWS; i++) {
            for (int j = 0; j < this.COLS; j++) {
                result[i][j] = this.ENTRIES[i][j] - array[i][j];
            }
        }

        this.ENTRIES = result; // copy the result to Matrix
    }


    /**
    * Operates subtraction for two arrays,
    * and both arrays should be same size.<br>
    *
    * @param  matrixA  the first array as minuend.
    * @param  matrixB  the second array as subtrahend.
    *
    * @return the array which contains the difference of two arrays.
    *
    * @since  0.2.0
    * @see    #sub(Matrix)
    * @see    #sub(double[][])
    * @see    #sub(Matrix, Matrix)
    */
    public static double[ ][ ] sub(double[ ][ ] matrixA, double[ ][ ] matrixB) {
        try {
            // Throw "NullMatrixException" if matrix array is null
            if (matrixA == null) {
                throw new NullMatrixException("Cannot operate subtraction, because <param1> is \"null\" array");
            } else if (matrixB == null) {
                throw new NullMatrixException("Cannot operate subtraction, because <param2> is \"null\" array");
            }
            // Else throw "IllegalMatrixSizeException" if the both matrix and are not same size
            else if (matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length) {
                throw new IllegalMatrixSizeException("Cannot subtract two matrices with different size");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create a new matrix array
        double[ ][ ] result = new double[matrixA.length][matrixB[0].length];

        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                result[i][j] = matrixA[i][j] - matrixB[i][j];
            }
        }

        return result;
    }


    /**
    * Operates subtraction for two matrices,
    * and both matrices size should be same.<br>
    *
    * @param  matrixA  the first matrix object as minuend.
    * @param  matrixB  the second matrix object as subtrahend.
    *
    * @return the matrix object which contains the difference of two matrices.
    *
    * @since 0.2.0
    * @see   #sub(Matrix)
    * @see   #sub(double[][])
    * @see   #sub(double[][], double[][])
    */
    public static Matrix sub(Matrix matrixA, Matrix matrixB) {
        try {
            // Throw "NullMatrixException" if both matrices is null
            if (matrixA.ENTRIES == null) {
                throw new NullMatrixException("Cannot operate subtraction, because <param1> is \"null\" matrix");
            } else if (matrixB.ENTRIES == null) {
                throw new NullMatrixException("Cannot operate subtraction, because <param2> is \"null\" matrix");
            }
            // Else throw "IllegalMatrixSizeException" if both matrices size are not same
            else if (matrixA.ENTRIES.length != matrixB.ENTRIES.length || matrixA.ENTRIES[0].length != matrixB.ENTRIES[0].length) {
                throw new IllegalMatrixSizeException("Cannot subtract two matrices with different size");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create new matrix object
        Matrix matrixRes = new Matrix(matrixA.ENTRIES.length, matrixB.ENTRIES[0].length);

        for (int i = 0; i < matrixA.ENTRIES.length; i++) {
            for (int j = 0; j < matrixB.ENTRIES[0].length; j++) {
                matrixRes.ENTRIES[i][j] = matrixA.ENTRIES[i][j] - matrixB.ENTRIES[i][j];
            }
        }

        return matrixRes;
    }



    //////////////////////////////
    ///     MULTIPLICATION     ///
    //////////////////////////////

    /**
    * Operates multiplication for current matrix and other matrix object.<br>
    *
    * @param other  the matrix object as multiplicand.
    *
    * @since 0.2.0
    * @see   #mult(double[][])
    * @see   #mult(double[][], double[][])
    * @see   #mult(Matrix, Matrix)
    */
    public void mult(Matrix other) {
        try {
            // Throw "NullMatrixException" if current Matrix or given Matrix is null
            if (this.ENTRIES == null || other.ENTRIES == null) {
                throw new NullMatrixException("The matrix or one of the operands is null. Please ensure that both matrices are initialized before performing multiplication.");
            } else if (this.COLS != other.ROWS) {
                throw new IllegalMatrixSizeException("The number of columns in the first matrix is different from the number of rows in the second matrix");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create new matrix array
        double[ ][ ] multiplyResult = new double[this.ROWS][other.ENTRIES[0].length];

        // Iterate and multiply each element
        for (int r = 0; r < multiplyResult.length; r++) {
            for (int c = 0; c < multiplyResult[r].length; c++) {
                multiplyResult[r][c] = this.multCell(this.ENTRIES, other.ENTRIES, r, c);
            }
        }

        this.ENTRIES = multiplyResult;
    }


    /**
    * Operates multiplication for current matrix and an array.<br>
    *
    * @param array  an array as multiplicand.
    *
    * @since 0.2.0
    * @see   #mult(Matrix)
    * @see   #mult(double[][], double[][])
    * @see   #mult(Matrix, Matrix)
    */
    public void mult(double[ ][ ] array) {
        try {
            // Throw "NullMatrixException" if current Matrix or given Matrix is null
            if (this.ENTRIES == null || array == null) {
                throw new NullMatrixException("The matrix or one of the operands is null. Please ensure that both matrices are initialized before performing multiplication.");
            } else if (this.COLS != array.length) {
                throw new IllegalMatrixSizeException("The number of columns in the first matrix is different from the number of rows in the second matrix");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create new matrix array
        double[ ][ ] multiplyResult = new double[this.ROWS][array[0].length];

        // Iterate and multiply each element
        for (int r = 0; r < multiplyResult.length; r++) {
            for (int c = 0; c < multiplyResult[r].length; c++) {
                multiplyResult[r][c] = this.multCell(this.ENTRIES, array, r, c);
            }
        }

        this.ENTRIES = multiplyResult;
    }


    /**
    * Operates multiplication for two arrays.<br>
    *
    * @param  matrixA  the first array as multiplier.
    * @param  matrixB  the second array as multiplicand.
    *
    * @return the array which contains the product of two arrays.
    *
    * @since  0.2.0
    * @see    #mult(Matrix)
    * @see    #mult(double[][])
    * @see    #mult(Matrix, Matrix)
    */
    public static double[ ][ ] mult(double[ ][ ] matrixA, double[ ][ ] matrixB) {
        try {
            if (matrixA == null || matrixB == null) {
                throw new NullMatrixException("The matrix or one of the operands is null. Please ensure that both matrices are initialized before performing multiplication.");
            } else if (matrixA[0].length != matrixB.length) {
                throw new IllegalMatrixSizeException("The number of columns in the first matrix is different from the number of rows in the second matrix.");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        double[ ][ ] multiplyResult = new double[matrixA.length][matrixB[0].length];

        for (int r = 0; r < multiplyResult.length; r++) {
            for (int c = 0; c < multiplyResult[r].length; c++) {
                multiplyResult[r][c] = multCell(matrixA, matrixB, r, c);
            }
        }

        return multiplyResult;
    }


    /**
    * Operates multiplication for two matrices.<br>
    *
    * @param  matrixA  the first matrix object as multiplier.
    * @param  matrixB  the second matrix object as multiplicand.
    *
    * @return the matrix object which contains the product of two matrices.
    *
    * @since  0.2.0
    * @see    #mult(Matrix)
    * @see    #mult(double[][])
    * @see    #mult(double[][], double[][])
    */
    public static Matrix mult(Matrix matrixA, Matrix matrixB) {
        try {
            // Throw "NullMatrixException" if given Matrix is null
            if (matrixA.ENTRIES == null || matrixB.ENTRIES == null) {
                throw new NullMatrixException("The matrix or one of the operands is null. Please ensure that both matrices are initialized before performing multiplication.");
            } else if (matrixA.COLS != matrixB.ROWS) {
                throw new IllegalMatrixSizeException("The number of columns in the first matrix is different from the number of rows in the second matrix.");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create new matrix object
        Matrix matrixMultResult = new Matrix(matrixA.ENTRIES.length, matrixB.ENTRIES[0].length);

        for (int r = 0; r < matrixMultResult.ENTRIES.length; r++) {
            for (int c = 0; c < matrixMultResult.ENTRIES[r].length; c++) {
                matrixMultResult.ENTRIES[r][c] = multCell(matrixA.ENTRIES, matrixB.ENTRIES, r, c);
            }
        }

        return matrixMultResult;
    }

    //// ----------------------------- ////
    // -**- [END] MATRIX OPERATIONS -**- //
    //// ----------------------------- ////



    /**
    * Method that will displays current matrix to standard output.<br>
    * Displays {@literal <null_matrix>} if the current matrix is {@code null}.<br>
    * <br>
    *
    * Output example:<br>
    * <code>
    * {@literal   [   [n, n, n, ...]}<br>
    * {@literal       [n, n, n, ...]}<br>
    * {@literal       [n, n, n, ...]   ]}<br>
    * </code>
    * <br>
    *
    * @since 0.1.0
    * @see   #display(int)
    * @see   #display(double[][])
    * @see   #display(double[][], int)
    */
    public final void display() {
        if (this.ENTRIES != null) {
            System.out.println(this.toString());
        } else {
            System.out.println("<null_matrix>");
        }
    }


    /**
    * Method that will displays specified column of current matrix to standard output.<br>
    * Displays {@literal <null_matrix>} if the current matrix is {@code null}.<br>
    * <br>
    *
    * Output example:<br>
    * <code>
    * {@literal   [n, n, n, ...]}
    * </code>
    * <br>
    *
    * @param  index  the row index.
    *
    * @throws InvalidIndexException
    *         if the given index is negative value or
    *         the index is larger than number of rows.
    *
    * @since  0.2.0
    * @see    #display()
    * @see    #display(int[][])
    * @see    #display(int[][], int)
    */
    public final void display(int index) {
        if (this.ENTRIES != null) {
            // Checking index value from argument parameter
            try {
                if (index < 0) {
                    throw new InvalidIndexException("Index cannot be a negative value");
                } else if (index > this.ROWS - 1) {
                    throw new InvalidIndexException("Given index cannot be larger than number of rows");
                }
            } catch (final InvalidIndexException iie) {
                try {
                    throw new JMBaseException(iie);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }

            System.out.println(Arrays.toString(this.ENTRIES[index]));
        } else {
            System.out.println("<null_matrix>");
        }
    }


    /**
    * Method that will displays specified array to standard output.<br>
    * Displays {@literal <null_matrix>} if the given array is {@code null}.<br>
    * <br>
    *
    * Output example:<br>
    * <code>
    * {@literal   [   [n, n, n, ...]}<br>
    * {@literal       [n, n, n, ...]}<br>
    * {@literal       [n, n, n, ...]   ]}<br>
    * </code>
    * <br>
    *
    * @param array  the array to be displayed.
    *
    * @since 0.1.0
    * @see   #display()
    * @see   #display(int)
    * @see   #display(double[][], int)
    */
    public static final void display(double[ ][ ] array) {
        if (array != null) {
            System.out.println((new Matrix(array)).toString());
        } else {
            System.out.println("<null_matrix>");
        }
    }


    /**
    * Method that will displays specified column of given array to standard output.<br>
    * Displays {@literal <null_matrix>} if the given array is {@code null}.<br>
    * <br>
    *
    * Output example:<br>
    * <code>
    * {@literal   [n, n, n, ...]}
    * </code>
    * <br>
    *
    * @param  array  the array to be displayed.
    * @param  index  the row index.
    *
    * @throws InvalidIndexException
    *         if the given index is negative value or
    *         the index is larger than number of rows.
    *
    * @since  0.2.0
    * @see    #display()
    * @see    #display(int)
    * @see    #display(double[][])
    */
    public static final void display(double[ ][ ] array, int index) {
        if (array != null) {
            try {
                // Checking index value
                if (index < 0) {
                    throw new InvalidIndexException("Index cannot be a negative value");
                } else if (index > array.length - 1) {
                    throw new InvalidIndexException("Given index cannot be larger than number of rows");
                }
            } catch (final InvalidIndexException iie) {
                try {
                    throw new JMBaseException(iie);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }

            System.out.println(Arrays.toString(array[index]));
        } else {
            System.out.println("<null_matrix>");
        }
    }


    /**
    * Method that will transposes current matrix.<br>
    * If the matrix type are not square, then the row would be column
    * and the column would be row.<br>
    *
    * @since 0.2.0
    * @see   #transpose(double[][])
    * @see   #transpose(Matrix)
    */
    public void transpose() {
        this.create(Matrix.transpose(this).getEntries());
    }


    /**
    * Method that will transposes specified array.<br>
    * If the matrix type are not square, then the row would be column
    * and the column would be row.<br>
    *
    * @param  array  the array to be transposed.
    *
    * @return the transposed of given array.
    *
    * @since  0.2.0
    * @see    #transpose()
    * @see    #transpose(Matrix)
    */
    public static double[ ][ ] transpose(double[ ][ ] array) {
        try {
            if (array == null) {
                throw new NullMatrixException("Cannot transpose \"null\" array");
            }
        } catch (final NullMatrixException nme) {
            try {
                throw new JMBaseException(nme);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        return Matrix.transpose(new Matrix(array)).getEntries();
    }


    /**
    * Method that will transposes specified matrix object.<br>
    * If the matrix type are not square, then the row would be column
    * and the column would be row.<br>
    *
    * @param  other  the matrix to be transposed.
    *
    * @return the transposed of given matrix.
    *
    * @since 0.2.0
    * @see   #transpose()
    * @see   #transpose(double[][])
    */
    public static Matrix transpose(Matrix other) {
        try {
            if (other.ENTRIES == null) {
                throw new NullMatrixException("Cannot transpose \"null\" matrix");
            }
        } catch (final NullMatrixException nme) {
            try {
                throw new JMBaseException(nme);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        Matrix transposedMatrix = new Matrix(); // create null transposed matrix

        if (other.isSquare()) {
            // Create new matrix with same size as "other" size
            transposedMatrix.create(other.ROWS, other.COLS);

            // Iterate over elements and transpose each element
            for (int i = 0; i < other.ROWS; i++) {
                for (int j = 0; j < other.COLS; j++) {
                    transposedMatrix.ENTRIES[i][j] = other.ENTRIES[j][i];
                }
            }
        } else {
            // Initialize new matrix with inverted size as "other" size
            /*
              - rows    = columns
              - columns = rows
            */
            transposedMatrix.create(other.COLS, other.ROWS);

            for (int i = 0; i < other.COLS; i++) {
                for (int j = 0; j < other.ROWS; j++) {
                    transposedMatrix.ENTRIES[i][j] = other.ENTRIES[j][i];
                }
            }
        }

        return transposedMatrix;
    }


    /**
    * Checks whether the current matrix is a square matrix type.<br>
    * A matrix can be called a square type if the number of rows
    * are equals to the number of columns.<br>
    *
    * @return {@code true} if the matrix is square, otherwise returns {@code false}.
    *
    * @since  1.0.0
    */
    public boolean isSquare() {
        return Matrix.isSquare(this);
    }


    /**
    * Checks whether the given matrix is a square matrix type.<br>
    * A matrix can be called a square type if the number of rows
    * are equals to the number of columns.<br>
    *
    * @param  array  the array.
    *
    * @return {@code true} if the given matrix is square, otherwise returns {@code false}.
    */
    public static boolean isSquare(double[ ][ ] array) {
        return (array.length == array[0].length);
    }


    /**
    * Checks whether the given matrix is a square matrix type.<br>
    * A matrix can be called a square type if the number of rows
    * are equals to the number of columns.<br>
    *
    * @param  m  the Matrix object.
    *
    * @return {@code true} if the given matrix is square, otherwise returns {@code false}.
    */
    public static boolean isSquare(Matrix m) {
        return (m.ROWS == m.COLS);
    }



    /**
    * Clears all values inside current matrix.<br>
    * This method will converts the matrix into null type matrix.<br>
    *
    * @throws NullMatrixException
    *         if the current matrix is a {@code null} matrix.
    *
    * @since  0.2.0
    */
    public void clear() {
        if (this.ENTRIES == null) {
            try {
                throw new NullMatrixException("Cannot clearing values, because current matrix is \"null\"");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        for (int r = 0; r < this.ROWS; r++) {
            for (int c = 0; c < this.COLS; c++) {
                this.ENTRIES[r][c] = 0;
            }
        }
        this.index = 0; // reset the index row
    }


    /**
    * Sorts all values of current matrix.<br>
    *
    * @throws NullMatrixException
    *         if the given matrix is a {@code null} matrix.
    *
    * @since  0.2.0
    * @see    #sort(double[][])
    * @see    #sort(Matrix)
    * @see    java.util.Arrays#sort
    */
    public void sort() {
        this.ENTRIES = Matrix.sort(this).getEntries();
    }


    /**
    * Sorts all values of specified array.<br>
    *
    * @param  array  the array to be sorted.
    *
    * @throws NullMatrixException
    *         if the given array is a {@code null} array.
    *
    * @since  0.2.0
    * @see    #sort()
    * @see    #sort(Matrix)
    * @see    java.util.Arrays#sort
    */
    public static void sort(double[ ][ ] array) {
        if (array == null) {
            try {
                throw new NullMatrixException("Cannot sorting all values, because <param1> is \"null\" array");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        for (int r = 0; r < array.length; r++) {
            Arrays.sort(array[r]);
        }
    }


    /**
    * Sorts all values of specified matrix object.<br>
    *
    * @param  obj  the matrix to be sorted.
    *
    * @return the sorted matrix object.
    *
    * @throws NullMatrixException
    *         if the given matrix is a {@code null} matrix.
    *
    * @since  1.0.0
    * @see    #sort()
    * @see    #sort(double[][])
    */
    public static Matrix sort(Matrix obj) {
        // Check for null matrix
        try {
            if (obj.ENTRIES == null) {
                throw new NullMatrixException("Cannot sorting all values, because <param1> is null matrix");
            }
        } catch (final NullMatrixException nme) {
            try {
                throw new JMBaseException(nme);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create new matrix object
        Matrix sortedMatrix = new Matrix(obj.ROWS, obj.COLS);
        sortedMatrix.ENTRIES = obj.ENTRIES; // copy the matrix

        for (int r = 0; r < sortedMatrix.ROWS; r++) {
            Arrays.sort(sortedMatrix.ENTRIES[r]);
        }

        return sortedMatrix;
    }


    /**
    * Gets the sizes of current matrix.<br>
    * Row: {@code m.getSize()[0];}<br>
    * Column: {@code m.getSize()[1];}<br>
    *
    * @return an {@code Integer} list containing total rows<sup>[0]</sup> and columns<sup>[1]</sup>.
    *
    * @since  0.1.0
    */
    public int[ ] getSize() {
        // It would return list: [<rows>, <cols>]
        return new int[ ] { this.ROWS, this.COLS };
    }


    /**
    * Returns the value at the specified row and column in the matrix.<br>
    * If given index is a negative value, then it will count from the last.<br>
    *
    * @param  row  the row index (min 0).
    * @param  col  the column index (min 0).
    *
    * @return the value at the specified position in the matrix.
    *
    * @throws NullMatrixException
    *         if the matrix is null and no entries are defined
    * @throws InvalidIndexException
    *         if the row or column index is out of range
    *
    * @since  1.0.0
    * @see    #getEntries()
    */
    public double get(int row, int col) {
        try {
            if (this.ENTRIES == null) {
                throw new NullMatrixException("Matrix is null and no entries are defined");
            } else if (row >= this.ROWS || (row < 0 && (row + this.ROWS) >= this.ROWS || (row + this.ROWS) < 0)) {
                throw new InvalidIndexException("Invalid row index: " + row + " (matrix size: " + this.ROWS + "x" + this.COLS + ")");
            } else if (col >= this.COLS || (col < 0 && (col + this.COLS) >= this.COLS || (col + this.COLS) < 0)) {
                throw new InvalidIndexException("Invalid column index: " + col + " (matrix size: " + this.ROWS + "x" + this.COLS + ")");
            }
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // If the given index is negative value,
        // then it will count from last.
        // Example:
        //      m.get(-1, -1);
        //
        // Code above will returns last entry of the last row.
        if (row < 0) {
            row += this.ROWS;
        }
        if (col < 0) {
            col += this.COLS;
        }

        return this.ENTRIES[row][col];
    }

    /**
    * Returns the array representation of this matrix entries.<br>
    *
    * @return the entries array of this matrix.
    *
    * @since  1.0.0
    * @see    #get(int, int)
    */
    public double[ ][ ] getEntries() {
        return this.ENTRIES;
    }

    /**
    * Returns a {@code String} representation of this matrix.<br>
    *
    * @return {@code String} representation of this matrix in Python-style array notation.
    *
    * @since  1.0.0
    */
    @Override
    public String toString() {
        final int rows = this.ROWS, cols = this.COLS;
        String strMatrix;

        strMatrix = "[   "; // head row
        for (int r = 0; r < rows; r++) {
            strMatrix += "["; // head column
            for (int c = 0; c < cols; c++) {
                strMatrix += this.ENTRIES[r][c];
                if (c != cols - 1) strMatrix += ", ";
            }
            strMatrix += "]"; // tail column
            if (r != rows - 1) strMatrix += String.format("%s    ", System.lineSeparator());
        }
        strMatrix += "   ]"; // tail row

        return strMatrix;
    }
}
