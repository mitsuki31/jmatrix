// :: ------------------ :: //
/* --   MATRIX BUILDER   -- */
// :: ------------------ :: //

package lib.matrix;

// -**- Built-in Package -**- //
import java.util.Arrays;
import java.util.Collections;
import java.lang.Math;

// -**- Local Package -**- //
import lib.matrix.NullMatrixException;
import lib.matrix.MatrixArrayFullException;
import lib.matrix.IllegalMatrixSizeException;

// ** MATRIX CLASS ** //
public class Matrix
{
    // - Private attributes
    private int[ ][ ] MATRIX = null; // Create null matrix
    private int ROWS = 0,  // Initialize matrix rows
                COLS = 0,  // Initialize matrix columns
                index = 0; // Index for add() function


    //// ----------------- ////
    // -**- CONSTRUCTOR -**- //
    //// ----------------- ////

    /* ---
    - Matrix constructor with 2 parameter:
        > rows : For size of matrix rows
        > cols : For size of matrix columns
    --- */
    public Matrix(int rows, int cols) {
        this.ROWS = rows;
        this.COLS = cols;
        this.MATRIX = new int[rows][cols]; // Initialize matrix array
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
        this.MATRIX = new int[rows][cols]; // Initialize matrix array

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
            throw new IllegalMatrixSizeException("Too much arguments; Max column: " + this.COLS);
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



    //// ----------------------- ////
    // -**- MATRIX OPERATIONS -**- //
    //// ----------------------- ////

    // -- Summation (Matrix object) -- //
    public int[ ][ ] sum(Matrix other)
    throws IllegalMatrixSizeException, NullMatrixException {
        // Throw "NullMatrixException" if matrix is null
        if (this.MATRIX == null || other.MATRIX == null) {
            throw new NullMatrixException();
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

        return result;
    }

    // -- Summation (Integer array) -- //
    public int[ ][ ] sum(int[ ][ ] matrix)
    throws IllegalMatrixSizeException, NullMatrixException {
        // Throw "NullMatrixException" if matrix is null
        if (this.MATRIX == null || matrix == null) {
            throw new NullMatrixException();
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

        return result;
    }


    // -- Subtraction (Matrix object) -- //
    public int[ ][ ] sub(Matrix other)
    throws IllegalMatrixSizeException, NullMatrixException {
        // Throw "NullMatrixException" if matrix is null
        if (this.MATRIX == null || other.MATRIX == null) {
            throw new NullMatrixException();
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

        return result;
    }

    // -- Subtraction (Integer array) -- //
    public int[ ][ ] sub(int[ ][ ] matrix)
    throws IllegalMatrixSizeException, NullMatrixException {
        // Throw "NullMatrixException" if matrix is null
        if (this.MATRIX == null || matrix == null) {
            throw new NullMatrixException();
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

        return result;
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
            System.out.print("[ ");
            for (int c = 0; c < this.COLS; c++) {
                System.out.print(this.MATRIX[r][c]);
                if (c != this.COLS - 1) System.out.print(", ");
            }
            System.out.print(" ]");
            if (r != this.ROWS - 1) {
                System.out.print("\n    ");
            }
        }
        System.out.println("   ]");
    }

    public static final void display(int[ ][ ] matrix) throws NullMatrixException {
        // Throw "NullMatrixException" if matrix array is null
        if (matrix == null) {
            throw new NullMatrixException();
        }

        final int rows = matrix.length;
        final int cols = matrix[0].length;

        System.out.print("[   ");
        for (int r = 0; r < rows; r++) {
            System.out.print("[ ");
            for (int c = 0; c < cols; c++) {
                System.out.print(matrix[r][c]);
                if (c != cols - 1) System.out.print(", ");
            }
            System.out.print(" ]");
            if (r != rows - 1) {
                System.out.print("\n    ");
            }
        }
        System.out.println("   ]");
    }


    // -- Matrix Cleaner (assign 0 to each element) -- //
    public void clear() {
        for (int r = 0; r < this.ROWS; r++) {
            for (int c = 0; c < this.COLS; c++) {
                this.MATRIX[r][c] = 0;
            }
        }
    }

    // -- Matrix Sorter -- //
    public void sort() {
        // Just simply iterate matrix rows, then sort each columns
        for (int r = 0; r < this.ROWS; r++) {
            Arrays.sort(this.MATRIX[r]);
        }
    }

    public static void sort(int[ ][ ] matrix) {
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
