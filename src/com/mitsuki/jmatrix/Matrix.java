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

package com.mitsuki.jmatrix;

// -**- Built-in Package -**- //
import java.util.Arrays;
import java.lang.Math;

// -**- Local Package -**- //
import com.mitsuki.jmatrix.core.*;
import com.mitsuki.jmatrix.util.Options;

// ** MATRIX CLASS ** //
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

    /*  -- Matrix() --
    * Matrix constructor without parameter, would create null matrix
    *
    * @return Null Matrix object
    */
    public Matrix() {}


    /*  -- Matrix() --
    * Matrix constructor with 2 parameters
    *
    * @param rows - Value size for matrix rows
    * @param cols - Value size for matrix columns
    *
    * @return Create Matrix array with specified rows and columns
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


    /*  -- Matrix() --
    * Matrix constructor with 3 parameter:
    *
    * @param rows - Value size for matrix rows
    * @param cols - Value size for matrix columns
    * @param val - Value for fill the entire elements of matrix array
    *
    * @return Create Matrix array with specified rows and columns, then fill entire elements with specified value
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


    /*  -- Matrix() --
    * Create new Matrix, it can be called even the Matrix object isn't null
    *
    * @param rows - Size for new Matrix rows
    * @param cols - Size for new Matrix columns
    *
    * @return Create new Matrix array with specified rows and columns
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


    /*  -- copy() --
    * Copy the Matrix object to another Matrix object
    *
    * @return Copied Matrix object and all attributes
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


    /*  -- select() --
    * Select index of Matrix rows, this method should be combined with "change()" method. Instead it'll just do nothing
    *
    * @param index - Index row that want to be selected
    * @return Matrix - Return itself
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

    /*  -- add() --
    * Fill column with list of Integer(s) (int...) or Integer array (int[ ])
    *
    * @param values - Values to be added into column. It can be Integer array (int[ ]) or you can write the values one by one
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


    /*  -- add() --
    * Fill column with a repeated single Integer value
    *
    * @param value - Number value that want to fill out the column
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

    /*  -- change() --
    * Method that change all values at specified column, combine this method with "select()" method
    * 
    *
    * @param values - Values for change the specified column
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


    /*  -- change() --
    *
    * @params
    * @return: void (None)
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

    /*  -- sum() --
    * Summarize current matrix with other matrix,
    * and both matrices should be same size
    *
    * @params: other (Matrix class)
    * @return: void (None)
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


    /*  -- sum()  --
    * Summarize current matrix with list of Integer array,
    * both should be same size
    *
    * @params: matrix (List of Integer array)
    * @return: void (None)
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


    /*  -- sum() --
    * Summarize two list of Integer array,
    * both should be same size
    *
    * @params:
    *     - matrixA (List of Integer array)
    *     - matrixB (List of Integer array)
    * @return:
    *     int[ ][ ] (List of Integer array)
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


    /*  -- sum() --
    * Summarize two matrices object,
    * both matrices should be same size
    *
    * @params:
    *     - matrixA (Matrix class)
    *     - matrixB (Matrix class)
    * @return: Matrix (Matrix class)
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



    /*  -- sub() --
    * Subtract current matrix with other matrix object,
    * both matrices should be same size
    *
    * @params: other (Matrix class)
    * @return: void (None)
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


    /*  -- sub() --
    * Subtract current matrix with list of Integer array,
    * both should be same size
    *
    * @params: matrix (list of Integer array)
    * @return: void (None)
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


    /*  -- sub() --
    * Subtract two list of Integer array,
    * both should be same size
    *
    * @params:
    *     - matrixA (List of Integer array)
    *     - matrixB (List of Integer array)
    * @return: int[ ][ ] (List of Integer array)
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


    /*  -- sub() --
    * Subtract two matrices object,
    * both matrices should be same size
    *
    * @params:
    *     - matrixA (Matrix class)
    *     - matrixB (Matrix class)
    * @return: Matrix (Matrix class)
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



    /*  -- multCell() --
    *
    * @params:
    *     - matrixA (List of Integer array)
    *     - matrixB (List of Integer array)
    * @return: int (Integer)
    */
    private static int multCell(int[ ][ ] matrixA, int[ ][ ] matrixB, int row, int col) {
        int result = 0;
        for (int i = 0; i < matrixB.length; i++) {
            result += matrixA[row][i] * matrixB[i][col];
        }

        return result;
    }


    /*  -- mult() --
    * Multiply current matrix with other matrix
    *
    * @params: other (Matrix class)
    * @return: void (None)
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


    /*  -- mult() --
    * Multiply current matrix with list of Integer array
    *
    * @params: matrix (List of Integer array)
    * @return: void (None)
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


    /*  -- mult() --
    * Multiply two list of Integer array
    *
    * @params:
    *     - matrixA (List of Integer array)
    *     - matrixB (List of Integer array)
    * @return: int[ ][ ] (List of Integer array)
    */
    public static int[ ][ ] mult(int[ ][ ] matrixA, int[ ][ ] matrixB)
    throws NullMatrixException {
        if (matrixA == null || matrixB == null) {
            throw new NullMatrixException("Can not iterate null matrix");
        }

        int[ ][ ] multiplyResult = new int[matrixA.length][matrixB[0].length];

        for (int r = 0; r < multiplyResult.length; r++) {
            for (int c = 0; c < multiplyResult[r].length; c++) {
                multiplyResult[r][c] = multCell(matrixA, matrixB, r, c);
            }
        }

        return multiplyResult;
    }


    /*  -- mult() --
    * Multiply two matrices object
    *
    * @params:
    *     - matrixA (Matrix class)
    *     - matrixB (Matrix class)
    * @return: Matrix (Matrix class)
    */
    public static Matrix mult(Matrix matrixA, Matrix matrixB) {
        if (matrixA.MATRIX == null || matrixB.MATRIX == null) {
            try {
                throw new NullMatrixException("Can not iterate null matrix");
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



    /*  -- display() --
    * Method that will print out/display current matrix array
    * Output example:
    *      [   [ n, n, n, ... ]
    *          [ n, n, n, ... ]
    *          [ .. .. .. ... ]   ]
    *
    * @params: void (None)
    * @return: void (None)
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


    /*  -- display() --
    * Method that will print out/display specific matrix column
    *
    * @params: index (Integer)
    * @return: void (None)
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


    /*  -- display() --
    * Method that will print out/display matrix array
    *
    * @params: matrix (List of Integer array)
    * @return: void (None)
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


    /*  -- display() --
    * Method that will print out/display specific matrix column
    *
    * @params:
    *     - matrix (List of Integer array)
    *     - index (Integer)
    * @return: void (None)
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


    /*  -- toString() --
    * Method that convert Integer array to String
    *
    * @params: array (Integer array)
    * @return: String
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


    /* -- transpose() --
    * Method that will transpose current matrix array
    *
    * @params: void (None)
    * @return: void (None)
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


    /*  -- transpose() --
    * Method that will transpose given list of Integer array
    *
    * @params: matrix (List of Integer array)
    * @return: int[ ][ ] (List of Integer array)
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


    /*  -- transpose() --
    * Method that will transpose given matrix array
    *
    * @params: other (Matrix class)
    * @return: Matrix (Matrix class)
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



    /*  -- clear() --
    * Method that clearing all values inside current matrix array
    *
    * @params: void (None)
    * @return: void (None)
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


    /*  -- sort() --
    * Method that will sort each columns matrix
    *
    * @params: void (None)
    * @return: void (None)
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


    /*  -- sort() --
    * Method that will sort each columns given matrix
    *
    * @params: matrix (List of Integer array)
    * @return: void (None)
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


    /*  -- getSize() --
    * Get size of current matrix array
    *
    * @params: void (None)
    * @return: int[ ] (Integer array)
    */
    public int[ ] getSize() {
        // It would return list: [<rows>, <cols>]
        final int[ ] size = { this.ROWS, this.COLS };
        return size;
    }
}
