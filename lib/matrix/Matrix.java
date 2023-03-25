// :: ------------------ :: //
/* --   MATRIX BUILDER   -- */
// :: ------------------ :: //

// Author : Ryuu Mitsuki

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

package lib.matrix;

// -**- Built-in Package -**- //
import java.util.Arrays;
import java.util.Collections;
import java.lang.Math;

// -**- Local Package -**- //
import lib.matrix.NullMatrixException;
import lib.matrix.MatrixArrayFullException;
import lib.matrix.IllegalMatrixSizeException;
import lib.matrix.InvalidIndexException;

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

    // -- Create null Matrix
    public Matrix() {}

    /* ---
    - Matrix constructor with 2 parameter:
        > rows : For size of matrix rows
        > cols : For size of matrix columns
    --- */
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

    /* ---
    - Matrix constructor with 3 parameter:
        > rows : For size of matrix rows
        > cols : For size of matrix columns
        > val  : Value for fill entire elements matrix array
    --- */
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


    // -- Create new matrix
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


    // -- Copy the Matrix
    public Matrix copy() throws NullMatrixException {
        if (this.MATRIX == null) {
            throw new NullMatrixException("Matrix is null, can not copy the Matrix");
        }

        // Create new and copy the matrix
        Matrix copiedMatrix = new Matrix(this.ROWS, this.COLS);
        copiedMatrix.MATRIX = this.MATRIX;

        return copiedMatrix;
    }


    // -- Select index of Matrix rows
    public Matrix select(final int index) throws InvalidIndexException {
        if (index < 0) {
            throw new InvalidIndexException("Index must be positive value");
        } else if (index > this.ROWS - 1) {
            throw new InvalidIndexException("Index is too large than total matrix rows");
        }

        this.selectedIndex = index;

        return this; // return self
    }


    //// ---------------- ////
    // -**- ADD VALUES -**- //
    //// ---------------- ////

    // Fill column with list of Integer(s)
    public void add(int... values)
    throws NullMatrixException, MatrixArrayFullException, IllegalMatrixSizeException {
        // Throw "NullMatrixException" when matrix array is null
        if (this.MATRIX == null) {
            throw new NullMatrixException();
        }
        /** ---
        - I've created add() function, that would only fill a row
            when index isn't equal to total of matrix rows.
        - Throw "MatrixArrayFullException" while attempt to add
            new column when index has equal to total matrix rows
        --- **/
        else if (this.index >= this.ROWS) {
            throw new MatrixArrayFullException();
        }
        // Length of values list shouldn't greater than total matrix columns
        else if (values.length > this.COLS) {
            throw new IllegalMatrixSizeException("Too many arguments; Max column: " + this.COLS);
        }
        // And shouldn't be less than total matrix columns
        else if (values.length < this.COLS) {
            throw new IllegalMatrixSizeException("Not enough argument");
        }

        // Iterate values list and fill elements of matrix array
        int i = 0;
        for (int val : values) {
            this.MATRIX[this.index][i++] = val;
        }

        this.index++; // increment index row
    }

    // Fill column with an Integer value (repeated)
    public void add(int value)
    throws NullMatrixException, MatrixArrayFullException {
        // Throw "NullMatrixException" when matrix array is null
        if (this.MATRIX == null) {
            throw new NullMatrixException();
        }
        /** ---
        - Throw "MatrixArrayFullExceotion" while attempt to add
            a new column, but the index has equal to total matrix rows
        --- **/
        else if (this.index >= this.ROWS) {
            throw new MatrixArrayFullException();
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


    /** ---
    - Function that change values entire column,
    - select column by "selectedIndex" value
    --- **/
    public void change(int... values)
    throws NullMatrixException, IllegalMatrixSizeException {
        // Check for null matrix
        if (this.MATRIX == null) {
            throw new NullMatrixException();
        }
        // Check size of values from argument parameter
        else if (values.length > this.COLS) {
            throw new IllegalMatrixSizeException("Too many arguments; Max column: " + this.COLS);
        }
        else if (values.length < this.COLS) {
            throw new IllegalMatrixSizeException("Not enough argument");
        }

        for (int i = 0; i < this.COLS; i++) {
            this.MATRIX[this.selectedIndex][i] = values[i];
        }

        this.selectedIndex = -1; // reset to default
    }

    public void change(int value)
    throws NullMatrixException {
        if (this.MATRIX == null) {
            throw new NullMatrixException();
        }

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


    //// ----------------------- ////
    // -**- MATRIX OPERATIONS -**- //
    //// ----------------------- ////

    // -- Summation (Matrix object) -- //
    public void sum(Matrix other)
    throws IllegalMatrixSizeException, NullMatrixException {
        // Throw "NullMatrixException" if matrix is null
        if (this.MATRIX == null || other.MATRIX == null) {
            throw new NullMatrixException("Can not iterate null matrix");
        }
        // Else throw "IllegalMatrixSizeException" if the matrices are not same size
        else if (this.ROWS != other.ROWS || this.COLS != other.COLS) {
            throw new IllegalMatrixSizeException();
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

    // -- Summation (Integer array) -- //
    public void sum(int[ ][ ] matrix)
    throws IllegalMatrixSizeException, NullMatrixException {
        // Throw "NullMatrixException" if matrix is null
        if (this.MATRIX == null || matrix == null) {
            throw new NullMatrixException("Can not iterate null matrix");
        }
        // Else throw "IllegalMatrixSizeException" if the matrices are not same size
        else if (this.ROWS != matrix.length || this.COLS != matrix[0].length) {
            throw new IllegalMatrixSizeException();
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

    // -- Summation [Static] -- /
    public static int[ ][ ] sum(int[ ][ ] matrixA, int[ ][ ] matrixB)
    throws IllegalMatrixSizeException, NullMatrixException {
        // Throw "NullMatrixException" if matrix array is null
        if (matrixA == null || matrixB == null) {
            throw new NullMatrixException("Can not iterate null matrix");
        }
        // Else throw "IllegalMatrixSizeException" if the both matrix and are not same size
        else if (matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length) {
            throw new IllegalMatrixSizeException();
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

    // -- Summation [Static] (Object) -- //
    public static Matrix sum(Matrix matrixA, Matrix matrixB)
    throws IllegalMatrixSizeException, NullMatrixException {
        if (matrixA.MATRIX == null || matrixB.MATRIX == null) {
            throw new NullMatrixException("Can not iterate null matrix");
        }
        else if (matrixA.MATRIX.length != matrixB.MATRIX.length ||
                matrixA.MATRIX[0].length != matrixB.MATRIX[0].length) {
            throw new IllegalMatrixSizeException();
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



    // -- Subtraction (Matrix object) -- //
    public void sub(Matrix other)
    throws IllegalMatrixSizeException, NullMatrixException {
        // Throw "NullMatrixException" if matrix is null
        if (this.MATRIX == null || other.MATRIX == null) {
            throw new NullMatrixException("Can not iterate null matrix");
        }
        // Else throw "IllegalMatrixSizeException" if the matrices are not same size
        else if (this.ROWS != other.ROWS || this.COLS != other.COLS) {
            throw new IllegalMatrixSizeException();
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

    // -- Subtraction (Integer array) -- //
    public void sub(int[ ][ ] matrix)
    throws IllegalMatrixSizeException, NullMatrixException {
        // Throw "NullMatrixException" if matrix is null
        if (this.MATRIX == null || matrix == null) {
            throw new NullMatrixException("Can not iterate null matrix");
        }
        // Else throw "IllegalMatrixSizeException" if the matrices are not same size
        else if (this.ROWS != matrix.length || this.COLS != matrix[0].length) {
            throw new IllegalMatrixSizeException();
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

    // -- Subtraction [Static] -- /
    public static int[ ][ ] sub(int[ ][ ] matrixA, int[ ][ ] matrixB)
    throws IllegalMatrixSizeException, NullMatrixException {
        // Throw "NullMatrixException" if matrix array is null
        if (matrixA == null || matrixB == null) {
            throw new NullMatrixException("Can not iterate null matrix");
        }
        // Else throw "IllegalMatrixSizeException" if the both matrix and are not same size
        else if (matrixA.length != matrixB.length || matrixA[0].length != matrixB[0].length) {
            throw new IllegalMatrixSizeException();
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

    // -- Subtraction [Static] (Object) -- //
    public static Matrix sub(Matrix matrixA, Matrix matrixB)
    throws IllegalMatrixSizeException, NullMatrixException {
        if (matrixA.MATRIX == null || matrixB.MATRIX == null) {
            throw new NullMatrixException("Can not iterate null matrix");
        }
        else if (matrixA.MATRIX.length != matrixB.MATRIX.length ||
                matrixA.MATRIX[0].length != matrixB.MATRIX[0].length) {
            throw new IllegalMatrixSizeException();
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



    // -- Multiply Matrices Cell -- //
    private static int multCell(int[ ][ ] matrixA, int[ ][ ] matrixB, int row, int col) {
        int result = 0;
        for (int i = 0; i < matrixB.length; i++) {
            result += matrixA[row][i] * matrixB[i][col];
        }

        return result;
    }

    // -- Multiplication (Matrix object) -- //
    public void mult(Matrix other) throws NullMatrixException {
        if (this.MATRIX == null || other.MATRIX == null) {
            throw new NullMatrixException("Can not iterate null matrix");
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

    // -- Multiplication (Integer array)
    public void mult(int[ ][ ] matrix) throws NullMatrixException {
        if (this.MATRIX == null || matrix == null) {
            throw new NullMatrixException("Can not iterate null matrix");
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

    // -- Multiplication [Static] -- //
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

    // -- Multiplication [Static] (Object) -- //
    public static Matrix mult(Matrix matrixA, Matrix matrixB)
    throws NullMatrixException {
        if (matrixA.MATRIX == null || matrixB.MATRIX == null) {
            throw new NullMatrixException("Can not iterate null matrix");
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


    // -- Matrix Displayer -- //
    /** ---
    - Output example:
          [   [ n, n, n, ... ]
              [ n, n, n, ... ]
              [ .. .. .. ... ]   ]
    --- **/
    public final void display() throws NullMatrixException {
        // Throw "NullMatrixException" if matrix array is null
        if (this.MATRIX == null) {
            throw new NullMatrixException();
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

    // -- Display specific column
    public final void display(int index)
    throws NullMatrixException, InvalidIndexException {
        // Check whether matrix array is null or not
        if (this.MATRIX == null) {
            throw new NullMatrixException();
        }

        // Checking index value from argument parameter
        if (index < 0) {
            throw new InvalidIndexException("Index must be positive value");
        } else if (index > this.ROWS - 1) {
            throw new InvalidIndexException("Index is too large than total matrix rows");
        }

        System.out.println(this.toString(this.MATRIX[index]));
    }

    public static final void display(int[ ][ ] matrix)
    throws NullMatrixException {
        // Throw "NullMatrixException" if matrix array is null
        if (matrix == null) {
            throw new NullMatrixException();
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

    public static final void display(int[ ][ ] matrix, int index)
    throws NullMatrixException, InvalidIndexException {
        if (matrix == null) {
            throw new NullMatrixException();
        }

        // Checking index value
        if (index < 0) {
            throw new InvalidIndexException("Index must be positive value");
        } else if (index > matrix.length - 1) {
            throw new InvalidIndexException("Index is too large than total matrix rows");
        }

        System.out.println(toString(matrix[index]));
    }


    // Function to convert Integer arrays to String
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

    // -- Matrix Transpose -- //
    public void transpose() throws NullMatrixException {
        if (this.MATRIX == null) {
            throw new NullMatrixException();
        }

        int[ ][ ] transposedMatrix; // create null matrix

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

    public static int[ ][ ] transpose(int[ ][ ] matrix)
    throws NullMatrixException {
        if (matrix == null) {
            throw new NullMatrixException();
        }

        boolean isSquare;
        int[ ][ ] transposedMatrix;

        final int rows = matrix.length,
                  cols = matrix[0].length;

        if (rows == cols) {
            isSquare = true;
        } else {
            isSquare = false;
        }

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

    public static Matrix transpose(Matrix other)
    throws NullMatrixException {
        if (other == null) {
            throw new NullMatrixException();
        }

        Matrix transposedMatrix = new Matrix(other.ROWS, other.COLS);

        transposedMatrix.MATRIX = Matrix.transpose(other.MATRIX);

        return transposedMatrix;
    }

    // -- Matrix Cleaner (assign 0 to each element) -- //
    public void clear() throws NullMatrixException {
        if (this.MATRIX == null) {
            throw new NullMatrixException();
        }

        for (int r = 0; r < this.ROWS; r++) {
            for (int c = 0; c < this.COLS; c++) {
                this.MATRIX[r][c] = 0;
            }
        }
        this.index = 0; // reset the index row
    }

    // -- Matrix Sorter -- //
    public void sort() throws NullMatrixException {
        if (this.MATRIX == null) {
            throw new NullMatrixException();
        }

        // Just simply iterate matrix rows, then sort each columns
        for (int r = 0; r < this.ROWS; r++) {
            Arrays.sort(this.MATRIX[r]);
        }
    }

    public static void sort(int[ ][ ] matrix) throws NullMatrixException {
        if (matrix == null) {
            throw new NullMatrixException();
        }

        for (int r = 0; r < matrix.length; r++) {
            Arrays.sort(matrix[r]);
        }
    }

    // -- Matrix Size -- //
    public int[ ] getSize() {
        // It would return list: [<rows>, <cols>]
        final int[ ] size = { this.ROWS, this.COLS };
        return size;
    }
}
