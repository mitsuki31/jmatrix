// :: ----------------------- :: //
/* --  InvalidIndexException  -- */
// :: ----------------------- :: //

package com.mitsuki.jmatrix;

import java.lang.ArrayIndexOutOfBoundsException;

public class InvalidIndexException extends ArrayIndexOutOfBoundsException
{
    private String message = null;

    public InvalidIndexException() {
        super();
    }

    public InvalidIndexException(String message) {
        super(message);
        this.message = message;
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
