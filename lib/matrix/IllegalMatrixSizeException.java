package lib.matrix;

import java.lang.IllegalArgumentException;

public class IllegalMatrixSizeException extends IllegalArgumentException
{
    private String message = "The two matrices are not same size";

    public IllegalMatrixSizeException() {
        super();
    }

    public IllegalMatrixSizeException(String message) {
        super(message);
        this.message = message;
    }

    public IllegalMatrixSizeException(Throwable cause) {
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
