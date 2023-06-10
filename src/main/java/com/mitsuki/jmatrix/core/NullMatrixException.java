// :: --------------------- :: //
/* --  NullMatrixException  -- */
// :: --------------------- :: //

package com.mitsuki.jmatrix.core;

public class NullMatrixException extends JMBaseException
{
    private String message = null;

    public NullMatrixException() {
        super();
    }

    public NullMatrixException(String message) {
        super(message);
        this.message = message;
    }

    public NullMatrixException(Throwable cause) {
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
