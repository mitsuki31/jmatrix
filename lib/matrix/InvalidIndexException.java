// :: ----------------------- :: //
/* --  InvalidIndexException  -- */
// :: ----------------------- :: //

package lib.matrix;

import java.lang.Exception;

public class InvalidIndexException extends Exception
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
