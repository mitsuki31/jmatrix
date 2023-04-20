// :: ------------------- :: //
/* --   JMATRIX BUILDER   -- */
// :: ------------------- :: //

// Developed by Ryuu Mitsuki

/*
      ---  LIST FUNCTION  ---

  Non-static function(s):
  -  Matrix select(int);

  -  Matrix copy();

  -  void add(int...);
  -  void add(int);

  -  void change(int...);
  -  void change(int);

  -  void sum(Matrix);
  -  void sum(int[ ][ ]);

  -  void sub(Matrix);
  -  void sub(int[ ][ ]);

  -  void mult(Matrix);
  -  void mult(int[ ][ ]);

  -  void transpose();

  -  void display();
  -  void display(int);

  -  void sort();

  -  int[ ] getSize();

  -  void clear();


  Static function(s):
  -  int[ ][ ] sum(int[ ][ ], int[ ][ ]);
  -  Matrix sum(Matrix, Matrix);

  -  int[ ][ ] sub(int[ ][ ], int[ ][ ]);
  -  Matrix sub(Matrix, Matrix);

  -  int[ ][ ] mult(int[ ][ ], int[ ][ ]);
  -  Matrix mult(Matrix, Matrix);

  -  int[ ][ ] transpose(int[ ][ ]);
  -  Matrix transpose(Matrix);

  -  void display(int[ ][ ]);
  -  void display(int[ ][ ], int);

  -  void sort(int[ ][ ]);
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
* Creates matrix array with specified sizes of rows and columns or just creates a {@code null} matrix.<br>
* Also this class provides basic matrix operations.<br>
*
* @version  1.2
* @since    0.1.0
* @author   Ryuu Mitsuki
*/
public class Matrix
{
    // - Private attributes
    private int[ ][ ] MATRIX = null; // Create null matrix
    private int ROWS = 0,            // Initialize matrix rows
                COLS = 0,            // Initialize matrix columns
                index = 0,           // Index for add() function
                selectedIndex = -1;

    private boolean isSquare; // boolean for check a square matrix


    //// ----------------- ////
    // -**- CONSTRUCTOR -**- //
    //// ----------------- ////

    /**
    * Constructs new <b>Matrix</b> object without any parameter.<br>
    * This would creates a {@code null} matrix.<br>
    *
    * @since 0.2.0
    * @see   Matrix(int, int)
    * @see   Matrix(int, int, int)
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
    */
    public Matrix(int rows, int cols) {
        this.ROWS = rows;
        this.COLS = cols;
        this.MATRIX = new int[rows][cols];

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
    * @param rows  a value for size of matrix row.
    * @param cols  a value for size of matrix column.
    * @param val   a value to be filled out into all elements of matrix.
    *
    * @throws IllegalArgumentException
    *         if the two input sizes is negative value
    *         or one of two input sizes are equal to zero.
    *
    * @since 0.1.0
    * @see   Matrix()
    * @see   Matrix(int, int)
    */
    public Matrix(int rows, int cols, int val) {
        this.ROWS = rows;
        this.COLS = cols;

        this.MATRIX = new int[rows][cols];

        if (rows != cols) {
            this.isSquare = false;
        } else {
            this.isSquare = true;
        }

        // Fill all elements inside matrix array with "val"
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                this.MATRIX[r][c] = val;
            }
        }
    }

    //// ----------------------- ////
    // -**- [END] CONSTRUCTOR -**- //
    //// ----------------------- ////


    /**
    * Creates a new <b>Matrix</b> with specific rows and columns.<br>
    * Can be called even the <b>Matrix</b> not {@code null}.<br>
    *
    * @param  rows  a value for new matrix row size.
    * @param  cols  a value for new matrix column size.
    *
    * @throws IllegalArgumentException
    *         if the two input sizes is negative value
    *         or one of two input sizes are equal to zero.
    *
    * @since  0.2.0
    */
    public void create(int rows, int cols) {
        this.ROWS = rows;
        this.COLS = cols;
        this.MATRIX = new int[rows][cols];

        this.index = 0; // reset index row

        if (rows != cols) {
            this.isSquare = false;
        } else {
            this.isSquare = true;
        }
    }


    /**
    * Duplicates current matrix to another matrix object.<br>
    * Throw <b>NullMatrixException</b> if the current matrix is {@code null}.<br>
    *
    * @return the copied of current <b>Matrix</b> object with all of its attributes.
    *
    * @throws NullMatrixException
    *         if this matrix is {@code null}.
    *
    * @since  0.2.0
    */
    public Matrix copy() {
        if (this.MATRIX == null) {
            try {
                throw new NullMatrixException("Matrix is null, cannot copy matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        // Create new and copy the matrix
        Matrix copiedMatrix = new Matrix(this.ROWS, this.COLS);
        copiedMatrix.MATRIX = this.MATRIX;

        return copiedMatrix;
    }


    /**
    * Selects <b>Matrix</b> column with specific index,
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
        // Check for null matrix
        if (this.MATRIX == null) {
            try {
                throw new NullMatrixException("Matrix array is null, cannot select matrix row");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        // Check for negative value
        if (index < 0) {
            try {
                throw new InvalidIndexException("Index must be positive value");
            } catch (final InvalidIndexException iie) {
                try {
                    throw new JMBaseException(iie);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        // Check for given index is greater than total rows
        } else if (index > this.ROWS - 1) {
            try {
                throw new InvalidIndexException("Index is too large than total matrix rows");
            } catch (final InvalidIndexException iie) {
                try {
                    throw new JMBaseException(iie);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
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
    * It can be an {@code Integer} array ({@code int[]}) or inserts the values one by one.<br>
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
    public void add(int... values) {
        // Throw "NullMatrixException" when matrix array is null
        if (this.MATRIX == null) {
            try {
                throw new NullMatrixException("Matrix array is null, cannot be added values");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }
        /** ---
        - I've created add() function, that would only fill a row
            when index isn't equal to total of matrix rows.
        - Throw "MatrixArrayFullException" while attempt to add
            new column when index has equal to total matrix rows
        --- **/
        else if (this.index >= this.ROWS) {
            try {
                throw new MatrixArrayFullException("Matrix array is already full, cannot add some values anymore");
            } catch (final MatrixArrayFullException mafe) {
                try {
                    throw new JMBaseException(mafe);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }
        // Length of values list shouldn't greater than total matrix columns
        else if (values.length > this.COLS) {
            try {
                throw new IllegalMatrixSizeException("Too many arguments; Max column: " + this.COLS);
            } catch (final IllegalMatrixSizeException imse) {
                try {
                    throw new JMBaseException(imse);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }
        // And shouldn't be less than total matrix columns
        else if (values.length < this.COLS) {
            try {
                throw new IllegalMatrixSizeException("Not enough argument");
            } catch (final IllegalMatrixSizeException imse) {
                try {
                    throw new JMBaseException(imse);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        // Iterate values list and fill elements of matrix array
        int i = 0;
        for (int val : values) {
            this.MATRIX[this.index][i++] = val;
        }

        this.index++; // increment index row
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
    * @see    #add(int...)
    */
    public void add(int value) {
        // Throw "NullMatrixException" when matrix array is null
        if (this.MATRIX == null) {
            try {
                throw new NullMatrixException("Matrix array is null, cannot add some values");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }
        /** ---
        - Throw "MatrixArrayFullExceotion" while attempt to add
            a new column, but the index has equal to total matrix rows
        --- **/
        else if (this.index >= this.ROWS) {
            try {
                throw new MatrixArrayFullException("Matrix array is already full, cannot add some values anymore");
            } catch (final MatrixArrayFullException mafe) {
                try {
                    throw new JMBaseException(mafe);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        // Create list of repeated value
        /** ---
        - Example: call add() function with single Integer
            with the value 8, then it would create column:

        >> [8, 8, 8, ...]

        - Then created column would be pushed to matrix array
        --- **/
        int[ ] values = new int[this.COLS]; // create list with size equal to column length
        for (int i = 0; i < values.length; i++) {
            values[i] = value;
        }

        // Iterate values list and fill elements of matrix array
        int i = 0;
        for (int val : values) {
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
    *
    * @since  0.2.0
    * @see    #select(int)
    * @see    #change(int)
    */
    public void change(int... values) {
        // Check size of values from argument parameter
        if (values.length > this.COLS) {
            try {
                throw new IllegalMatrixSizeException("Too many arguments for matrix with total columns " + this.COLS);
            } catch (final IllegalMatrixSizeException imse) {
                try {
                    throw new JMBaseException(imse);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }
        else if (values.length < this.COLS) {
            try {
                throw new IllegalMatrixSizeException("Not enough argument for matrix with total columns " + this.COLS);
            } catch (final IllegalMatrixSizeException imse) {
                try {
                    throw new JMBaseException(imse);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
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
    * @since  0.2.0
    * @see    #select(int)
    * @see    #change(int...)
    */
    public void change(int value) {
        // Create new array with same value
        int[ ] values = new int[this.COLS];
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

    /**
    * Operates addition for current <b>Matrix</b> and other <b>Matrix</b> object,
    * and both matrices should be same size.<br>
    *
    * @param  other  the <b>Matrix</b> object as addend.
    *
    * @since  0.1.0
    * @see    #sum(int[][])
    * @see    #sum(int[][], int[][])
    * @see    #sum(Matrix, Matrix)
    */
    public void sum(Matrix other) {
        // Throw "NullMatrixException" if matrix is null
        if (this.MATRIX == null || other.MATRIX == null) {
            try {
                throw new NullMatrixException("Cannot summarize null matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }
        // Else throw "IllegalMatrixSizeException" if the matrices size are not same
        else if (this.ROWS != other.ROWS || this.COLS != other.COLS) {
            try {
                throw new IllegalMatrixSizeException("Cannot summarize two matrices with different size");
            } catch (final IllegalMatrixSizeException imse) {
                try {
                    throw new JMBaseException(imse);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        // Create new matrix for the result
        int[ ][ ] result = new int[this.ROWS][other.COLS];

        // Iterate over each element of the matrices and add the corresponding values together
        for (int i = 0; i < this.ROWS; i++) {
            for (int j = 0; j < this.COLS; j++) {
                result[i][j] = this.MATRIX[i][j] + other.MATRIX[i][j];
            }
        }

        this.MATRIX = result; // copy the result to Matrix
    }


    /**
    * Operates addition for current <b>Matrix</b> and {@code Integer} array ({@code int[][]}),
    * and both objects should be same size.<br>
    *
    * @param  matrix  an array as addend.
    *
    * @since  0.1.0
    * @see    #sum(Matrix)
    * @see    #sum(int[][], int[][])
    * @see    #sum(Matrix, Matrix)
    */
    public void sum(int[ ][ ] matrix) {
        // Throw "NullMatrixException" if matrix is null
        if (this.MATRIX == null || matrix == null) {
            try {
                throw new NullMatrixException("Cannot summarize null matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }
        // Else throw "IllegalMatrixSizeException" if the matrices are not same size
        else if (this.ROWS != matrix.length || this.COLS != matrix[0].length) {
            try {
                throw new IllegalMatrixSizeException("Cannot summarize two matrices with different size");
            } catch (final IllegalMatrixSizeException imse) {
                try {
                    throw new JMBaseException(imse);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        // Create new matrix for the result
        int[ ][ ] result = new int[this.ROWS][matrix[0].length];

        // Using nested loop for iterate over each element of matrix
        for (int i = 0; i < this.ROWS; i++) {
            for (int j = 0; j < this.COLS; j++) {
                result[i][j] = this.MATRIX[i][j] + matrix[i][j];
            }
        }

        this.MATRIX = result; // copy the result to Matrix
    }


    /**
    * Operates addition for two {@code Integer} arrays ({@code int[][]}),
    * and both arrays should be same size.<br>
    *
    * @param  matrixA  the first array as addend.
    * @param  matrixB  the second array as addend.
    *
    * @return the array which contains the sum of two arrays.
    *
    * @since  0.2.0
    * @see    #sum(Matrix)
    * @see    #sum(int[])
    * @see    #sum(Matrix, Matrix)
    */
    public static int[ ][ ] sum(int[ ][ ] matrixA, int[ ][ ] matrixB) {
        // Throw "NullMatrixException" if matrix array is null
        if (matrixA == null || matrixB == null) {
            try {
                throw new NullMatrixException("Cannot summarize null matrix");
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
                throw new IllegalMatrixSizeException("Cannot summarize two matrices with different size");
            } catch (final IllegalMatrixSizeException imse) {
                try {
                    throw new JMBaseException(imse);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        // Create a new matrix array
        int[ ][ ] result = new int[matrixA.length][matrixB[0].length];

        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                result[i][j] = matrixA[i][j] + matrixB[i][j];
            }
        }

        return result;
    }


    /**
    * Operates addition for two <b>Matrix</b> objects,
    * and both matrices should be same size.<br>
    *
    * @param  matrixA  the first <b>Matrix</b> object as addend.
    * @param  matrixB  the second <b>Matrix</b> object as addend.
    *
    * @return the <b>Matrix</b> object which contains the sum of two matrices.
    *
    * @since  0.2.0
    * @see    #sum(Matrix)
    * @see    #sum(int[][])
    * @see    #sum(int[][], int[][])
    */
    public static Matrix sum(Matrix matrixA, Matrix matrixB) {
        if (matrixA.MATRIX == null || matrixB.MATRIX == null) {
            try {
                throw new NullMatrixException("Cannot summarize null matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }
        else if (matrixA.MATRIX.length != matrixB.MATRIX.length ||
                matrixA.MATRIX[0].length != matrixB.MATRIX[0].length) {
            try {
                throw new IllegalMatrixSizeException("Cannot summarize two matrices with different size");
            } catch (final IllegalMatrixSizeException imse) {
                try {
                    throw new JMBaseException(imse);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
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



    /**
    * Operates subtraction for current <b>Matrix</b> and other <b>Matrix</b> object,
    * and both matrices should be same size.<br>
    *
    * @param other  the <b>Matrix</b> object as subtrahend.
    *
    * @since 0.1.0
    * @see   #sub(int[][])
    * @see   #sub(int[][], int[][])
    * @see   #sub(Matrix, Matrix)
    */
    public void sub(Matrix other) {
        // Throw "NullMatrixException" if matrix is null
        if (this.MATRIX == null || other.MATRIX == null) {
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
        // Else throw "IllegalMatrixSizeException" if the matrices are not same size
        else if (this.ROWS != other.ROWS || this.COLS != other.COLS) {
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

        // Create new matrix for the result
        int[ ][ ] result = new int[this.ROWS][other.COLS];

        // Iterate over each element of all matrices and subtract each values together
        for (int i = 0; i < this.ROWS; i++) {
            for (int j = 0; j < this.COLS; j++) {
                result[i][j] = this.MATRIX[i][j] - other.MATRIX[i][j];
            }
        }

        this.MATRIX = result; // copy the result to Matrix
    }


    /**
    * Operates subtraction for current {@code Matrix} and {@code Integer} array,
    * and both objects should be same size.<br>
    *
    * @param matrix  an array as subtrahend.
    *
    * @since 0.1.0
    * @see   #sub(Matrix)
    * @see   #sub(int[][], int[][])
    * @see   #sub(Matrix, Matrix)
    */
    public void sub(int[ ][ ] matrix) {
        // Throw "NullMatrixException" if matrix is null
        if (this.MATRIX == null || matrix == null) {
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
        // Else throw "IllegalMatrixSizeException" if the matrices are not same size
        else if (this.ROWS != matrix.length || this.COLS != matrix[0].length) {
            try {
                throw new IllegalMatrixSizeException();
            } catch (final IllegalMatrixSizeException imse) {
                try {
                    throw new JMBaseException(imse);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        // Create new matrix for the result
        int[ ][ ] result = new int[this.ROWS][matrix[0].length];

        // Iterate over each element of all matrices and subtract each values together
        for (int i = 0; i < this.ROWS; i++) {
            for (int j = 0; j < this.COLS; j++) {
                result[i][j] = this.MATRIX[i][j] - matrix[i][j];
            }
        }

        this.MATRIX = result; // copy the result to Matrix
    }


    /**
    * Operates subtraction of two list of {@code Integer} arrays,
    * and both arrays should be same size.<br>
    *
    * @param  matrixA  the first array as minuend.
    * @param  matrixB  the second array as subtrahend.
    *
    * @return the array which contains the difference of two arrays.
    *
    * @since  0.2.0
    * @see    #sub(Matrix)
    * @see    #sub(int[][])
    * @see    #sub(Matrix, Matrix)
    */
    public static int[ ][ ] sub(int[ ][ ] matrixA, int[ ][ ] matrixB) {
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
        int[ ][ ] result = new int[matrixA.length][matrixB[0].length];

        for (int i = 0; i < matrixA.length; i++) {
            for (int j = 0; j < matrixB[0].length; j++) {
                result[i][j] = matrixA[i][j] - matrixB[i][j];
            }
        }

        return result;
    }


    /**
    * Operates subtraction for two matrices objects,
    * and both matrices size should be same.<br>
    *
    * @param  matrixA  the first <b>Matrix</b> object as minuend.
    * @param  matrixB  the second <b>Matrix</b> object as subtrahend.
    *
    * @return the <b>Matrix</b> object which contains the difference of two matrices.
    *
    * @since 0.2.0
    * @see   #sub(Matrix)
    * @see   #sub(int[][])
    * @see   #sub(int[][], int[][])
    */
    public static Matrix sub(Matrix matrixA, Matrix matrixB) {
        if (matrixA.MATRIX == null || matrixB.MATRIX == null) {
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
        else if (matrixA.MATRIX.length != matrixB.MATRIX.length ||
                matrixA.MATRIX[0].length != matrixB.MATRIX[0].length) {
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

        // Create new matrix object
        Matrix matrixRes = new Matrix(matrixA.MATRIX.length, matrixB.MATRIX[0].length);

        for (int i = 0; i < matrixA.MATRIX.length; i++) {
            for (int j = 0; j < matrixB.MATRIX[0].length; j++) {
                matrixRes.MATRIX[i][j] = matrixA.MATRIX[i][j] - matrixB.MATRIX[i][j];
            }
        }

        return matrixRes;
    }



    /**
    * Calculates product of a specific cell in two arrays.<br>
    * This method is used for matrix multiplication operation.<br>
    *
    * @param  matrixA  the first array.
    * @param  matrixB  the second array.
    * @param  row      a row index of the cell to be calculated.
    * @param  col      a column index of the cell to be calculated.
    *
    * @return the product of the specified cell of two arrays.
    *
    * @since  0.2.0
    * @see    #mult
    */
    private static int multCell(int[ ][ ] matrixA, int[ ][ ] matrixB, int row, int col) {
        int result = 0;
        for (int i = 0; i < matrixB.length; i++) {
            result += matrixA[row][i] * matrixB[i][col];
        }

        return result;
    }


    /**
    * Operates multiplication for current <b>Matrix</b> and other <b>Matrix</b> object.<br>
    *
    * @param other  the <b>Matrix</b> object as multiplicand.
    *
    * @since 0.2.0
    * @see   #mult(int[][])
    * @see   #mult(int[][], int[][])
    * @see   #mult(Matrix, Matrix)
    */
    public void mult(Matrix other) {
        if (this.MATRIX == null || other.MATRIX == null) {
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

        // Create new matrix array
        int[ ][ ] multiplyResult = new int[this.ROWS][other.MATRIX[0].length];

        // Iterate and multiply each element
        for (int r = 0; r < multiplyResult.length; r++) {
            for (int c = 0; c < multiplyResult[r].length; c++) {
                multiplyResult[r][c] = this.multCell(this.MATRIX, other.MATRIX, r, c);
            }
        }

        this.MATRIX = multiplyResult;
    }


    /**
    * Operates multiplication for current <b>Matrix</b> and {@code Integer} array.<br>
    *
    * @param matrix  an array as multiplicand.
    *
    * @since 0.2.0
    * @see   #mult(Matrix)
    * @see   #mult(int[][], int[][])
    * @see   #mult(Matrix, Matrix)
    */
    public void mult(int[ ][ ] matrix) {
        if (this.MATRIX == null || matrix == null) {
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

        // Create new matrix array
        int[ ][ ] multiplyResult = new int[this.ROWS][matrix[0].length];

        // Iterate and multiply each element
        for (int r = 0; r < multiplyResult.length; r++) {
            for (int c = 0; c < multiplyResult[r].length; c++) {
                multiplyResult[r][c] = this.multCell(this.MATRIX, matrix, r, c);
            }
        }

        this.MATRIX = multiplyResult;
    }


    /**
    * Operates multiplication for two {@code Integer} arrays.<br>
    *
    * @param  matrixA  the first array as multiplier.
    * @param  matrixB  the second array as multiplicand.
    *
    * @return the array which contains the product of two arrays.
    *
    * @since  0.2.0
    * @see    #mult(Matrix)
    * @see    #mult(int[][])
    * @see    #mult(Matrix, Matrix)
    */
    public static int[ ][ ] mult(int[ ][ ] matrixA, int[ ][ ] matrixB)
    throws NullMatrixException {
        if (matrixA == null || matrixB == null) {
            throw new NullMatrixException("Cannot iterate null matrix");
        }

        int[ ][ ] multiplyResult = new int[matrixA.length][matrixB[0].length];

        for (int r = 0; r < multiplyResult.length; r++) {
            for (int c = 0; c < multiplyResult[r].length; c++) {
                multiplyResult[r][c] = multCell(matrixA, matrixB, r, c);
            }
        }

        return multiplyResult;
    }


    /**
    * Operates multiplication for two <b>Matrix</b> objects.<br>
    *
    * @param  matrixA  the first <b>Matrix</b> object as multiplier.
    * @param  matrixB  the second <b>Matrix</b> object as multiplicand.
    *
    * @return the <b>Matrix</b> object which contains the product of two matrices.
    *
    * @since  0.2.0
    * @see    #mult(Matrix)
    * @see    #mult(int[][])
    * @see    #mult(int[][], int[][])
    */
    public static Matrix mult(Matrix matrixA, Matrix matrixB) {
        if (matrixA.MATRIX == null || matrixB.MATRIX == null) {
            try {
                throw new NullMatrixException("Cannot iterate null matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
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
    * Method that will prints out or displays current matrix to standard output.<br>
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
    * @see   #display(int[][])
    * @see   #display(int[][], int)
    */
    public final void display() {
        // Throw "NullMatrixException" if matrix array is null
        if (this.MATRIX == null) {
            try {
                throw new NullMatrixException("Cannot display null matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        System.out.print("[   ");
        for (int r = 0; r < this.ROWS; r++) {
            System.out.print(this.toString(this.MATRIX[r]));
            if (r != this.ROWS - 1) {
                System.out.print("\n    ");
            }
        }
        System.out.println("   ]");
    }


    /**
    * Method that will prints out or displays specific column of current matrix to standard output.<br>
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
        // Check whether matrix array is null or not
        if (this.MATRIX == null) {
            try {
                throw new NullMatrixException("Cannot display null matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        // Checking index value from argument parameter
        if (index < 0) {
            try {
                throw new InvalidIndexException("Index must be positive value");
            } catch (final InvalidIndexException iie) {
                try {
                    throw new JMBaseException(iie);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        } else if (index > this.ROWS - 1) {
            try {
                throw new InvalidIndexException("Index is too large than total matrix rows");
            } catch (final InvalidIndexException iie) {
                try {
                    throw new JMBaseException(iie);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        System.out.println(this.toString(this.MATRIX[index]));
    }


    /**
    * Method that will prints out or displays specified {@code Integer} array to standard output.<br>
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
    * @param matrix  the array to be displayed.
    *
    * @since 0.1.0
    * @see   #display()
    * @see   #display(int)
    * @see   #display(int[][], int)
    */
    public static final void display(int[ ][ ] matrix) {
        // Throw "NullMatrixException" if matrix array is null
        if (matrix == null) {
            try {
                throw new NullMatrixException("Cannot display null matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        final int rows = matrix.length;
        final int cols = matrix[0].length;

        System.out.print("[   ");
        for (int r = 0; r < rows; r++) {
            System.out.print(toString(matrix[r]));
            if (r != rows - 1) {
                System.out.print("\n    ");
            }
        }
        System.out.println("   ]");
    }


    /**
    * Method that will prints out or displays specified column of given {@code Integer} array to standard output.<br>
    * <br>
    *
    * Output example:<br>
    * <code>
    * &nbsp; [ n, n, n, ... ]
    * </code>
    * <br>
    *
    * @param matrix  the array to be selected its column.
    * @param index   the index for selecting the array column.
    *
    * @since 0.2.0
    * @see #display()
    * @see #display(int)
    * @see #display(int[][])
    */
    public static final void display(int[ ][ ] matrix, int index) {
        if (matrix == null) {
            try {
                throw new NullMatrixException("Cannot display null matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        // Checking index value
        if (index < 0) {
            try {
                throw new InvalidIndexException("Index must be positive value");
            } catch (final InvalidIndexException iie) {
                try {
                    throw new JMBaseException(iie);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        } else if (index > matrix.length - 1) {
            try {
                throw new InvalidIndexException("Index is too large than total matrix rows");
            } catch (final InvalidIndexException iie) {
                try {
                    throw new JMBaseException(iie);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        System.out.println(toString(matrix[index]));
    }


    /**
    * Method that converts {@code Integer} array to {@code String}.<br>
    * This method is similar to {@link java.util.Arrays#toString}.<br>
    *
    * @param  array  the array to be converted into {@code String}.
    *
    * @return the converted {@code Integer} array to {@code String}.
    *
    * @since  0.2.0
    * @see    java.util.Arrays#toString
    */
    private static String toString(int[ ] array) {
        final int len = array.length;
        String strResult;

        strResult = "[ ";
        for (int i = 0; i < len; i++) {
            strResult += String.format("%d", array[i]);
            if (i != len - 1) strResult += ", ";
        }
        strResult += " ]";

        return strResult;
    }


    /**
    * Method that will transposes current matrix.<br>
    * If the matrix type is square, the sizes won't be inverted.<br>
    *
    * @since 0.2.0
    * @see   #transpose(int[][])
    * @see   #transpose(Matrix)
    */
    public void transpose() {
        if (this.MATRIX == null) {
            try {
                throw new NullMatrixException("Cannot transpose null matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        int[ ][ ] transposedMatrix; // create null transposed matrix

        if (this.isSquare) {
            // Initialize new matrix with same size as MATRIX size
            transposedMatrix = new int[this.ROWS][this.COLS];

            // Iterate over elements and tranpose each element
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
            transposedMatrix = new int[this.COLS][this.ROWS];

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
    public static int[ ][ ] transpose(int[ ][ ] matrix) {
        if (matrix == null) {
            try {
                throw new NullMatrixException("Cannot transpose null matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        boolean isSquare;
        int[ ][ ] transposedMatrix; // create null transposed matrix

        final int rows = matrix.length,
                  cols = matrix[0].length;

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
            transposedMatrix = new int[rows][cols];

            for (int i = 0; i < rows; i++) {
                for (int j = 0; j < cols; j++) {
                    transposedMatrix[i][j] = matrix[j][i];
                }
            }
        } else {
            transposedMatrix = new int[cols][rows];

            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    transposedMatrix[i][j] = matrix[j][i];
                }
            }
        }

        return transposedMatrix;
    }


    /**
    * Method that will transposes specific <b>Matrix</b> object.<br>
    * If the matrix type is square, the sizes won't be inverted.<br>
    *
    * @param  other  the matrix to be transposed.
    *
    * @return the transposed of given <b>Matrix</b>.
    *
    * @since 0.2.0
    * @see   #transpose()
    * @see   #transpose(int[][])
    */
    public static Matrix transpose(Matrix other) {
        if (other == null) {
            try {
                throw new NullMatrixException("Cannot transpose null matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        Matrix transposedMatrix = new Matrix(other.ROWS, other.COLS);

        transposedMatrix.MATRIX = Matrix.transpose(other.MATRIX);

        return transposedMatrix;
    }



    /**
    * Clears all values inside current matrix.<br>
    * This method will creates the matrix into null type matrix.<br>
    *
    * @since 0.2.0
    */
    public void clear() {
        if (this.MATRIX == null) {
            try {
                throw new NullMatrixException("Cannot clearing all values inside null matrix");
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
    * @see   #sort(int[][])
    * @see   java.util.Arrays#sort
    */
    public void sort() {
        if (this.MATRIX == null) {
            try {
                throw new NullMatrixException("Cannot sorting values inside null matrix");
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
    * @param matrix  the array to be sorted.
    *
    * @since 0.2.0
    * @see   #sort()
    * @see   java.util.Arrays#sort
    */
    public static void sort(int[ ][ ] matrix) {
        if (matrix == null) {
            try {
                throw new NullMatrixException("Cannot sorting values inside null matrix");
            } catch (final NullMatrixException nme) {
                try {
                    throw new JMBaseException(nme);
                } catch (final JMBaseException jme) {
                    Options.raiseError(jme);
                }
            }
        }

        for (int r = 0; r < matrix.length; r++) {
            Arrays.sort(matrix[r]);
        }
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
