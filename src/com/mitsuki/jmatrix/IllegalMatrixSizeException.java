// :: ---------------------------- :: //
/* --  IllegalMatrixSizeException  -- */
// :: ---------------------------- :: //

package com.mitsuki.jmatrix;

import java.lang.RuntimeException;

public class IllegalMatrixSizeException extends RuntimeException
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
