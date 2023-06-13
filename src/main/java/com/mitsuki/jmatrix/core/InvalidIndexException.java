// :: ----------------------- :: //
/* --  InvalidIndexException  -- */
// :: ----------------------- :: //

package com.mitsuki.jmatrix.core;

public class InvalidIndexException extends JMBaseException
{
    private String message = null;

    public InvalidIndexException() {
        super();
    }

    public InvalidIndexException(String message) {
        super(message);
        this.message = message;
    }

    public InvalidIndexException(Throwable cause) {
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
