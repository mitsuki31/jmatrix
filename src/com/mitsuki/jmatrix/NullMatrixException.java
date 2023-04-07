// :: --------------------- :: //
/* --  NullMatrixException  -- */
// :: --------------------- :: //

package com.mitsuki.jmatrix;

import java.lang.NullPointerException;

public class NullMatrixException extends NullPointerException
{
    private String message = null;

    public NullMatrixException() {
        super();
    }

    public NullMatrixException(String message) {
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
