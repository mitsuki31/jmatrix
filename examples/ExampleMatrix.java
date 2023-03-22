// ::----------------:: //
/*    Example Usage     */
// ::----------------:: //
package examples;

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

        int[ ] rowA1 = { 5, 6, 5, 3, 2 };
        int[ ] rowA2 = { 8, 3, 4, 1, 3 };

        // Create new Matrix object
        Matrix matrix = new Matrix(rows, cols);
        Matrix matrix2 = new Matrix(rows, cols);

        // Get the Matrix size
        int[ ] matrixSize = matrix.getSize(); // return [rows, cols]

        // Create new null matrix for result
        int[ ][ ] result = null;

        // Display matrix size
        System.out.println("\nMATRIX SIZE: " + Arrays.toString(matrixSize));

        // Adding values into Matrix (need to catch some Exception)
        try {
            matrix.add(row1);
            matrix.add(row2);

            matrix2.add(rowA1);
            matrix2.add(rowA2);

            // Display the matrix
            System.out.println("\nMATRIX A");
            matrix.display();

            System.out.println("\nMATRIX B");
            matrix2.display();

            System.out.println("\n-------------------------");

            result = matrix.sum(matrix2);

            System.out.println("\nMATRIX A + B");
            Matrix.display(result);

            result = matrix.sub(matrix2);

            System.out.println("\nMATRIX A - B");
            Matrix.display(result);
        }
        // Catch all exceptions
        catch (final Exception e) {
            System.out.print("Error: ");
            e.printStackTrace();
            System.exit(1);
        }
    }
}
