package lib.matrix;

// -**- Built-in Package -**- //
import java.util.Arrays;
import java.util.Collections;
import java.lang.Math;

// -**- Local Package -**- //
import lib.matrix.NullMatrixException;
import lib.matrix.MatrixArrayFullException;

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
    throws NullMatrixException, MatrixArrayFullException {
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

        // Length of values list shouldn't greater than matrix columns
        if (values.length > this.COLS) {
            System.out.println("[!] Error: Too much arguments; Max column: " + this.COLS);
            System.exit(1);
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



    // -- Matrix Displayer -- //
    /** ---
    - Output example:
          [   [ n, n, n, ... ]
              [ n, n, n, ... ]
              [ n, n, n, ... ]   ]
    --- **/
    public final void display() throws NullMatrixException {
        // Throw "NullMatrixException" when matrix array is null
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

    // -- Matrix Size -- //
    public int[ ] getSize() {
        // It would return list: [<rows>, <cols>]
        final int[ ] size = { this.ROWS, this.COLS };
        return size;
    }
}
