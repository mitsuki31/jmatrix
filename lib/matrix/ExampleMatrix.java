// ::-------------------:: //
/*      ExampleMatrix      */
// ::-------------------:: //
package lib.matrix;

import java.util.Arrays;

import lib.matrix.*; // import all packages in "lib.matrix"

public class ExampleMatrix
{
    public static void main(String[ ] args)
    throws NullMatrixException, IllegalMatrixSizeException, MatrixArrayFullException, InvalidIndexException
    {
        // Rows and columns size for new Matrix
        int rows = 2;
        int cols = 3;

        // Values for each row
        int[ ] row1 = { 1, 2, 3 };
        int[ ] row2 = { 4, 5, 6 };
        int[ ] row3 = { 7, 8, 9 };

        int[ ] rowA1 = { -9, -6, -3 };
        int[ ] rowA2 = { -8, -5, -2 };
        int[ ] rowA3 = { -7, -4, -1 };

        // Create new Matrix object
        Matrix matrixA = new Matrix(rows, cols);
        Matrix matrixB = new Matrix(rows, cols);
        Matrix result = new Matrix(); // null matrix

        // Get the Matrix size
        int[ ] matrixSize = matrixA.getSize(); // return [rows, cols]

        // Display matrix size
        System.out.println("\nSIZE OF MATRIX A: " + Arrays.toString(matrixSize));

        // Adding values into Matrix
        matrixA.add(row1);
        matrixA.add(row2);

        matrixB.add(rowA1);
        matrixB.add(rowA2);

        // Display the matrix
        System.out.println("\nMATRIX A");
        matrixA.display();

        System.out.println("\nMATRIX B");
        matrixB.display();

        System.out.println("\n-------------------------");
        System.out.println("        SUMMATION");
        System.out.println("-------------------------");

        System.out.println("MATRIX A + MATRIX B\n");
        result = Matrix.sum(matrixA, matrixB);
        result.display();

        System.out.println("\n-------------------------");
        System.out.println("       SUBTRACTION");
        System.out.println("-------------------------");

        System.out.println("MATRIX A - MATRIX B\n");
        result = Matrix.sub(matrixA, matrixB);
        result.display();

        System.out.println("\n-------------------------");
        System.out.println("     MULTIPLICATION");
        System.out.println("-------------------------");

        System.out.println("MATRIX A * MATRIX B\n");
        result = Matrix.mult(matrixA, matrixB);
        result.display();

        System.out.println("\n-------------------------");
        System.out.println("       TRANSPOSE");
        System.out.println("-------------------------");

        System.out.println("TRANSPOSED MATRIX B\n");
        matrixB.transpose();
        matrixB.display();

        System.out.println("\n-------------------------");
        System.out.println("     CLEARING MATRIX");
        System.out.println("-------------------------");

        System.out.println("CLEARED MATRIX A\n");
        matrixA.clear();
        matrixA.display();

        System.out.println("\n-------------------------");
        System.out.println("       COPY MATRIX");
        System.out.println("-------------------------");

        System.out.println("COPY MATRIX B TO MATRIX A\n");
        matrixA = matrixB.copy();
        matrixA.display();
    }
}
