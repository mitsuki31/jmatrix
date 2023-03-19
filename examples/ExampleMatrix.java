// ::----------------:: //
/*    Example Usage     */
// ::----------------:: //

import java.util.Arrays;

import lib.matrix.*; // import all packages in "lib.matrix"

public class ExampleMatrix
{
    public static void main(String[ ] args) {
        // Rows and columns size for new Matrix
        int rows = 2;
        int cols = 5;

        // Values for each row
        int[ ] row1 = { 9, 1, 5, 6, 0 };
        int[ ] row2 = { 1, 3, 0, 8, 2 };

        // Create new Matrix object
        Matrix matrix = new Matrix(rows, cols);

        // Get the Matrix size
        int[ ] matrixSize = matrix.getSize(); // return [rows, cols]

        // Display matrix size
        System.out.println("MATRIX SIZE: " + Arrays.toString(matrixSize));

        // Adding values into Matrix (need to catch some Exception)
        try {
            matrix.add(row1);
            matrix.add(row2);

            // Display the matrix
            System.out.println("\nMATRIX");
            matrix.display();

            // Sort matrix values
            matrix.sort();

            // Display the matrix
            System.out.println("\nSORTED MATRIX");
            matrix.display();
        }
        // Catch all exceptions
        catch (final Exception e) {
            System.out.println("Error: " + e.getMessage());
            System.exit(1);
        }
    }
}
