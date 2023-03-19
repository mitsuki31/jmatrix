package lib.matrix;

import java.lang.Exception;

public class NullMatrixException extends Exception
{
    private String message = "Matrix array is null";

    public NullMatrixException() {
        super();
    }

    public NullMatrixException(String message) {
        super(message);
        this.message = message;
    }

    public NullMatrixException(Throwable cause) {
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
