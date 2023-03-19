package lib.matrix;

import java.lang.Exception;

public class MatrixArrayFullException extends Exception
{
    private String message = "Matrix array is full, can not be added anymore";

    public MatrixArrayFullException() {
        super();
    }

    public MatrixArrayFullException(String message) {
        super(message);
        this.message = message;
    }

    public MatrixArrayFullException(Throwable cause) {
        super(cause);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return this.message;
    }
}
