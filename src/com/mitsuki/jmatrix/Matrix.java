// :: ------------------- :: //
/* --   JMATRIX BUILDER   -- */
// :: ------------------- :: //

// Developed by Ryuu Mitsuki

/*
      ---  LIST OF METHODS  ---

>>  Non-static method(s):
  -  Matrix select(int);

  -  Matrix copy();

  -  void add(int...);
  -  void add(int);

  -  void change(int...);
  -  void change(int);

  -  void sum(Matrix);
  -  void sum(double[ ][ ]);

  -  void sub(Matrix);
  -  void sub(double[ ][ ]);

  -  void mult(Matrix);
  -  void mult(double[ ][ ]);

  -  void transpose();

  -  void display();
  -  void display(int);

  -  void sort();

  -  int[ ] getSize();

  -  void clear();


>>  Static method(s):
  -  double[ ][ ] sum(double[ ][ ], double[ ][ ]);
  -  Matrix sum(Matrix, Matrix);

  -  double[ ][ ] sub(double[ ][ ], double[ ][ ]);
  -  Matrix sub(Matrix, Matrix);

  -  double[ ][ ] mult(double[ ][ ], double[ ][ ]);
  -  Matrix mult(Matrix, Matrix);

  -  double[ ][ ] transpose(double[ ][ ]);
  -  Matrix transpose(Matrix);

  -  void display(double[ ][ ]);
  -  void display(double[ ][ ], int);

  -  void sort(double[ ][ ]);
  -  Matrix sort(Matrix);

*/


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
    private double[ ][ ] MATRIX = null; // Create null matrix
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
        this.MATRIX = new double[rows][cols];

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

        this.MATRIX = new double[rows][cols];

        if (rows != cols) {
            this.isSquare = false;
        } else {
            this.isSquare = true;
        }

        // Fill all matrix entries with "val"
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.MATRIX[r][c] = val;
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
        this.MATRIX = array;

        if (this.ROWS == this.COLS) {
            this.isSquare = true;
        } else {
            this.isSquare = false;
        }
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
    private static int multCell(double[ ][ ] arrayA, double[ ][ ] arrayB, int row, int col) {
        int result = 0;
        for (int i = 0; i < arrayB.length; i++) {
            result += arrayA[row][i] * arrayB[i][col];
        }

        return result;
    }


    /**
    * Method that converts an array to {@code String} in Python-style.<br>
    * This method is similar to {@link java.util.Arrays#toString}.<br>
    *
    * @param  array  the array to be converted into {@code String}.
    *
    * @return the converted array to {@code String} in Python-style.
    *
    * @since  0.2.0
    * @see    java.util.Arrays#toString
    */
    private static String toString(double[ ] array) {
        final int len = array.length;
        String strResult;

        strResult = "[ ";
        for (int i = 0; i < len; i++) {
            strResult += array[i];
            if (i != len - 1) strResult += ", ";
        }
        strResult += " ]";

        return strResult;
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
        this.MATRIX = new double[rows][cols];

        this.index = 0; // reset index row

        if (rows != cols) {
            this.isSquare = false;
        } else {
            this.isSquare = true;
        }
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
        this.MATRIX = array;

        if (this.ROWS == this.COLS) {
            this.isSquare = true;
        } else {
            this.isSquare = false;
        }
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
        if (this.MATRIX == null) {
            try {
                throw new NullMatrixException("Cannot copy the matrix, because current matrix is null");
            } catch (final NullMatrixException nme) {
                Options.raiseError(nme);
            }
        }

        // Create new and copy the matrix
        Matrix copiedMatrix = new Matrix(this.ROWS, this.COLS);
        copiedMatrix.MATRIX = this.MATRIX;

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
            if (this.MATRIX == null) {
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

    /**
    * Checks whether the current <b>Matrix</b> is square matrix type.<br>
    * The matrix with square type is when the both sizes (<i>rows and columns</i>) are same.<br>
    *
    * @return  return boolean {@code true} if the matrix is square type, else return {@code false}.
    *
    * @since   1.0.0
    */
    public boolean isSquare() {
        return this.isSquare;
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
            if (this.MATRIX == null) {
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
            this.MATRIX[this.index][i++] = val;
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
            if (this.MATRIX == null) {
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
            this.MATRIX[this.index][i++] = val;
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
            this.MATRIX[this.selectedIndex][i] = values[i];
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
            this.MATRIX[this.selectedIndex][i] = values[i];
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
            if (this.MATRIX == null) {
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
                result[i][j] = this.MATRIX[i][j] + other.MATRIX[i][j];
            }
        }

        this.MATRIX = result; // copy the result to Matrix
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
            if (this.MATRIX == null) {
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
                result[i][j] = this.MATRIX[i][j] + array[i][j];
            }
        }

        this.MATRIX = result; // copy the result to Matrix
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
        // Throw "NullMatrixException" if matrix array is null
        if (matrixA == null || matrixB == null) {
            try {
                throw new NullMatrixException("Cannot operate summation to null matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }
        // Else throw "IllegalMatrixSizeException" if the both matrices and are not same size
        else if (matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length) {
            try {
                throw new IllegalMatrixSizeException("Cannot summing up two arrays with a different size");
            } catch (final IllegalMatrixSizeException imse) {
                try {
                    throw new JMBaseException(imse);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
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
            if (matrixA.MATRIX == null) {
                throw new NullMatrixException("Cannot operate summation, because <param1> is \"null\" matrix");
            } else if (matrixB.MATRIX == null) {
                throw new NullMatrixException("Cannot operate summation, because <param2> is \"null\" matrix");
            }
            // Else throw "IllegalMatrixSizeException" if both matrices size are not same
            else if (matrixA.MATRIX.length != matrixB.MATRIX.length || matrixA.MATRIX[0].length != matrixB.MATRIX[0].length) {
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
        Matrix matrixRes = new Matrix(matrixA.MATRIX.length, matrixB.MATRIX[0].length);

        for (int i = 0; i < matrixA.MATRIX.length; i++) {
            for (int j = 0; j < matrixB.MATRIX[0].length; j++) {
                matrixRes.MATRIX[i][j] = matrixA.MATRIX[i][j] + matrixB.MATRIX[i][j];
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
            if (this.MATRIX == null) {
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
                result[i][j] = this.MATRIX[i][j] - other.MATRIX[i][j];
            }
        }

        this.MATRIX = result; // copy the result to Matrix
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
            if (this.MATRIX == null) {
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
                result[i][j] = this.MATRIX[i][j] - array[i][j];
            }
        }

        this.MATRIX = result; // copy the result to Matrix
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
        // Throw "NullMatrixException" if matrix array is null
        if (matrixA == null || matrixB == null) {
            try {
                throw new NullMatrixException("Cannot subtract null matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }
        // Else throw "IllegalMatrixSizeException" if the both matrix and are not same size
        else if (matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length) {
            try {
                throw new IllegalMatrixSizeException("Cannot subtract two matrices with different size");
            } catch (final IllegalMatrixSizeException imse) {
                try {
                    throw new JMBaseException(imse);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
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
            if (matrixA.MATRIX == null) {
                throw new NullMatrixException("Cannot operate subtraction, because <param1> is \"null\" matrix");
            } else if (matrixB.MATRIX == null) {
                throw new NullMatrixException("Cannot operate subtraction, because <param2> is \"null\" matrix");
            }
            // Else throw "IllegalMatrixSizeException" if both matrices size are not same
            else if (matrixA.MATRIX.length != matrixB.MATRIX.length || matrixA.MATRIX[0].length != matrixB.MATRIX[0].length) {
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
        Matrix matrixRes = new Matrix(matrixA.MATRIX.length, matrixB.MATRIX[0].length);

        for (int i = 0; i < matrixA.MATRIX.length; i++) {
            for (int j = 0; j < matrixB.MATRIX[0].length; j++) {
                matrixRes.MATRIX[i][j] = matrixA.MATRIX[i][j] - matrixB.MATRIX[i][j];
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
            if (this.MATRIX == null) {
                throw new NullMatrixException("Cannot operate multiplication, because current matrix is \"null\"");
            } else if (other.MATRIX == null) {
                throw new NullMatrixException("Cannot operate multiplication, because <param1> is \"null\" matrix");
            }
        } catch (final NullMatrixException nme) {
            try {
                throw new JMBaseException(nme);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create new matrix array
        double[ ][ ] multiplyResult = new double[this.ROWS][other.MATRIX[0].length];

        // Iterate and multiply each element
        for (int r = 0; r < multiplyResult.length; r++) {
            for (int c = 0; c < multiplyResult[r].length; c++) {
                multiplyResult[r][c] = this.multCell(this.MATRIX, other.MATRIX, r, c);
            }
        }

        this.MATRIX = multiplyResult;
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
            if (this.MATRIX == null) {
                throw new NullMatrixException("Cannot operate multiplication, because current matrix is \"null\"");
            } else if (array == null) {
                throw new NullMatrixException("Cannot operate multiplication, because <param1> is \"null\" array");
            }
        } catch (final NullMatrixException nme) {
            try {
                throw new JMBaseException(nme);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create new matrix array
        double[ ][ ] multiplyResult = new double[this.ROWS][array[0].length];

        // Iterate and multiply each element
        for (int r = 0; r < multiplyResult.length; r++) {
            for (int c = 0; c < multiplyResult[r].length; c++) {
                multiplyResult[r][c] = this.multCell(this.MATRIX, array, r, c);
            }
        }

        this.MATRIX = multiplyResult;
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
        if (matrixA == null || matrixB == null) {
            try {
                throw new NullMatrixException("Cannot multiply null matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
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
            if (matrixA.MATRIX == null) {
                throw new NullMatrixException("Cannot operate multiplication, because <param1> is \"null\" matrix");
            } else if (matrixB.MATRIX == null) {
                throw new NullMatrixException("Cannot operate multiplication, because <param2> is \"null\" matrix");
            }
        } catch (final NullMatrixException nme) {
            try {
                throw new JMBaseException(nme);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme);
            }
        }

        // Create new matrix object
        Matrix matrixMultResult = new Matrix(matrixA.MATRIX.length, matrixB.MATRIX[0].length);

        for (int r = 0; r < matrixMultResult.MATRIX.length; r++) {
            for (int c = 0; c < matrixMultResult.MATRIX[r].length; c++) {
                matrixMultResult.MATRIX[r][c] = multCell(matrixA.MATRIX, matrixB.MATRIX, r, c);
            }
        }

        return matrixMultResult;
    }

    //// ----------------------------- ////
    // -**- [END] MATRIX OPERATIONS -**- //
    //// ----------------------------- ////



    /**
    * Method that will displays current matrix to standard output.<br>
    * Displays <code>null_matrix</code> if the current matrix is {@code null}.<br>
    * <br>
    *
    * Output example:<br>
    * <code>
    * &nbsp; [&nbsp; &nbsp;[ n, n, n, ... ]<br>
    * &nbsp; &nbsp; &nbsp; [ n, n, n, ... ]<br>
    * &nbsp; &nbsp; &nbsp; [ .. .. .. ... ]&nbsp; &nbsp;]
    * </code>
    * <br>
    *
    * @since 0.1.0
    * @see   #display(int)
    * @see   #display(double[][])
    * @see   #display(double[][], int)
    */
    public final void display() {
        if (this.MATRIX != null) {
            System.out.print("[   ");
            for (int r = 0; r < this.ROWS; r++) {
                System.out.print(toString(this.MATRIX[r]));
                if (r != this.ROWS - 1) {
                    System.out.print("\n    ");
                }
            }
            System.out.println("   ]");
        } else {
            System.out.println("<null_matrix>");
        }
    }


    /**
    * Method that will displays specified column of current matrix to standard output.<br>
    * Displays <code>null_matrix</code> if the current matrix is {@code null}.<br>
    * <br>
    *
    * Output example:<br>
    * <code>
    * &nbsp; [ n, n, n, ... ]
    * </code>
    * <br>
    *
    * @param index  the index column to be selected.
    *
    * @since 0.2.0
    * @see   #display()
    * @see   #display(int[][])
    * @see   #display(int[][], int)
    */
    public final void display(int index) {
        if (this.MATRIX != null) {
            // Checking index value from argument parameter
            try {
                if (index < 0) {
                    throw new InvalidIndexException("Index must be a positive value");
                } else if (index > this.ROWS - 1) {
                    throw new InvalidIndexException("Given index cannot be larger than total matrix rows");
                }
            } catch (final InvalidIndexException iie) {
                try {
                    throw new JMBaseException(iie);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }

            System.out.println(toString(this.MATRIX[index]));
        } else {
            System.out.println("<null_matrix>");
        }
    }


    /**
    * Method that will displays specified array to standard output.<br>
    * Displays <code>&lt;null_matrix&lt;</code> if the given {@code array} is {@code null}.<br>
    * <br>
    *
    * Output example:<br>
    * <code>
    * &nbsp; [&nbsp; &nbsp;[ n, n, n, ... ]<br>
    * &nbsp; &nbsp; &nbsp; [ n, n, n, ... ]<br>
    * &nbsp; &nbsp; &nbsp; [ .. .. .. ... ]&nbsp; &nbsp;]
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
            final int rows = array.length;
            final int cols = array[0].length;

            System.out.print("[   ");
            for (int r = 0; r < rows; r++) {
                System.out.print(toString(array[r]));
                if (r != rows - 1) {
                    System.out.print("\n    ");
                }
            }
            System.out.println("   ]");
        } else {
            System.out.println("<null_matrix>");
        }
    }


    /**
    * Method that will displays specified column of given array to standard output.<br>
    * Displays <code>&lt;null_matrix&lt;</code> if the given array is {@code null}.<br>
    * <br>
    *
    * Output example:<br>
    * <code>
    * &nbsp; [ n, n, n, ... ]
    * </code>
    * <br>
    *
    * @param array  the array to be selected its column.
    * @param index  the index for selecting the array column.
    *
    * @since 0.2.0
    * @see #display()
    * @see #display(int)
    * @see #display(double[][])
    */
    public static final void display(double[ ][ ] array, int index) {
        if (array != null) {
            try {
                // Checking index value
                if (index < 0) {
                    throw new InvalidIndexException("Index at <param2> must be a positive value");
                } else if (index > array.length - 1) {
                    throw new InvalidIndexException("Given index cannot be larger than total array rows");
                }
            } catch (final InvalidIndexException iie) {
                try {
                    throw new JMBaseException(iie);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }

            System.out.println(toString(array[index]));
        } else {
            System.out.println("<null_matrix>");
        }
    }


    /**
    * Method that will transposes current matrix.<br>
    * If the matrix type is square, the sizes won't be inverted.<br>
    *
    * @since 0.2.0
    * @see   #transpose(double[][])
    * @see   #transpose(Matrix)
    */
    public void transpose() {
        if (this.MATRIX == null) {
            try {
                throw new NullMatrixException("Cannot transpose \"null\" matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        double[ ][ ] transposedMatrix; // create null transposed matrix

        if (this.isSquare) {
            // Initialize new matrix with same size as MATRIX size
            transposedMatrix = new double[this.ROWS][this.COLS];

            // Iterate over elements and transpose each element
            for (int i = 0; i < this.ROWS; i++) {
                for (int j = 0; j < this.COLS; j++) {
                    transposedMatrix[i][j] = this.MATRIX[j][i];
                }
            }
        } else {
            // Initialize new matrix with inverted size as MATRIX size
            /*
              - rows    = columns
              - columns = rows
            */
            transposedMatrix = new double[this.COLS][this.ROWS];

            for (int i = 0; i < this.COLS; i++) {
                for (int j = 0; j < this.ROWS; j++) {
                    transposedMatrix[i][j] = this.MATRIX[j][i];
                }
            }

            this.create(this.COLS, this.ROWS); // transpose matrix size
        }

        this.MATRIX = transposedMatrix; // assign with transposed matrix
    }


    /**
    * Method that will transposes specified array.<br>
    * If the matrix type is square, the sizes won't be inverted.<br>
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
        if (array == null) {
            try {
                throw new NullMatrixException("Cannot transpose \"null\" array");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        boolean isSquare;
        double[ ][ ] transposedMatrix; // create null transposed matrix

        final int rows = array.length,
                  cols = array[0].length;

        // Check for square matrix
        if (rows == cols) {
            isSquare = true;
        } else {
            isSquare = false;
        }

        /** ---
        - If the matrix type is square matrix, then transposed matrix
        -   size should be same as previous matrix size.
        - However, if not a square matrix then the transposed matrix
        -   size should be inverted
        --- **/
        if (isSquare) {
            transposedMatrix = new double[rows][cols];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    transposedMatrix[i][j] = array[j][i];
                }
            }
        } else {
            transposedMatrix = new double[cols][rows];

            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    transposedMatrix[i][j] = array[j][i];
                }
            }
        }

        return transposedMatrix;
    }


    /**
    * Method that will transposes specified matrix object.<br>
    * If the matrix type is square, the sizes won't be inverted.<br>
    *
    * @param  other  the matrix to be transposed.
    *
    * @return the transposed of given <b>Matrix</b>.
    *
    * @since 0.2.0
    * @see   #transpose()
    * @see   #transpose(double[][])
    */
    public static Matrix transpose(Matrix other) {
        if (other == null) {
            try {
                throw new NullMatrixException("Cannot transpose \"null\" matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        Matrix transposedMatrix = new Matrix(); // create null transposed matrix

        if (other.isSquare) {
            // Create new matrix with same size as "other" size
            transposedMatrix.create(other.ROWS, other.COLS);

            // Iterate over elements and transpose each element
            for (int i = 0; i < other.ROWS; i++) {
                for (int j = 0; j < other.COLS; j++) {
                    transposedMatrix.MATRIX[i][j] = other.MATRIX[j][i];
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
                    transposedMatrix.MATRIX[i][j] = other.MATRIX[j][i];
                }
            }
        }

        return transposedMatrix;
    }



    /**
    * Clears all values inside current matrix.<br>
    * This method will converts the matrix into null type matrix.<br>
    *
    * @since 0.2.0
    */
    public void clear() {
        if (this.MATRIX == null) {
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
                this.MATRIX[r][c] = 0;
            }
        }
        this.index = 0; // reset the index row
    }


    /**
    * Sorts all values of current matrix.<br>
    *
    * @since 0.2.0
    * @see   #sort(double[][])
    * @see   #sort(Matrix)
    * @see   java.util.Arrays#sort
    */
    public void sort() {
        if (this.MATRIX == null) {
            try {
                throw new NullMatrixException("Cannot sorting all values, because current matrix is \"null\"");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        // Just simply iterate matrix rows, then sort each columns
        for (int r = 0; r < this.ROWS; r++) {
            Arrays.sort(this.MATRIX[r]);
        }
    }


    /**
    * Sorts all values of specified array.<br>
    *
    * @param array  the array to be sorted.
    *
    * @since 0.2.0
    * @see   #sort()
    * @see   #sort(Matrix)
    * @see   java.util.Arrays#sort
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
    * @since  1.0.0
    * @see    #sort()
    * @see    #sort(double[][])
    */
    public static Matrix sort(Matrix obj) {
        // Check for null matrix
        try {
            if (obj.MATRIX == null) {
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
        sortedMatrix.MATRIX = obj.MATRIX; // copy the matrix

        for (int r = 0; r < sortedMatrix.ROWS; r++) {
            Arrays.sort(sortedMatrix.MATRIX[r]);
        }

        return sortedMatrix;
    }


    /**
    * Gets the sizes of current matrix.<br>
    *
    * @return a list which contains total rows<sup>[0]</sup> and columns<sup>[1]</sup>.
    *
    * @since 0.1.0
    */
    public int[ ] getSize() {
        // It would return list: [<rows>, <cols>]
        final int[ ] size = { this.ROWS, this.COLS };
        return size;
    }
}
