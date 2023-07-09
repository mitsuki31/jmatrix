// :: ---------------------------- :: //
/* --  IllegalMatrixSizeException  -- */
// :: ---------------------------- :: //

package com.mitsuki.jmatrix.exception;

public class IllegalMatrixSizeException extends JMBaseException
{
    private String message = null;

    public IllegalMatrixSizeException() {
        super();
    }

    public IllegalMatrixSizeException(String message) {
        super(message);
        this.message = message;
    }

    public IllegalMatrixSizeException(Throwable cause) {
        super(cause);
        this.message = cause.getMessage();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", this.getClass().getName(), this.message);
    }
}
