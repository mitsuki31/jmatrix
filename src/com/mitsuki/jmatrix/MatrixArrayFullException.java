// :: -------------------------- :: //
/* --  MatrixArrayFullException  -- */
// :: -------------------------- :: //

package com.mitsuki.jmatrix;

import java.lang.RuntimeException;

public class MatrixArrayFullException extends RuntimeException
{
    private String message = null;

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
        return String.format("%s: %s", this.getClass().getName(), this.message);
    }
}
