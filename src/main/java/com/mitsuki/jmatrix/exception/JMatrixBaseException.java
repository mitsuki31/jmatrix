// :: -------------------------- :: //
/* --    JMatrixBaseException    -- */
// :: -------------------------- :: //

/*
 * Copyright (c) 2023-2024 Ryuu Mitsuki
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mitsuki.jmatrix.exception;

import com.mitsuki.jmatrix.Matrix;
import com.mitsuki.jmatrix.core.MatrixUtils;
import com.mitsuki.jmatrix.enums.JMErrorCode;

import java.io.PrintStream;
import java.lang.RuntimeException;

/**
 * Base exception class for all <b>JMatrix</b> exceptions. This exception and its subclasses are unchecked exceptions,
 * which means they can be thrown during runtime.
 *
 * <p><b>Type:</b> Unchecked exception</p>
 *
 * @since   1.0.0b.1
 * @version 1.3, 06 June 2024
 * @author  <a href="https://github.com/mitsuki31" target="_blank">
 *          Ryuu Mitsuki</a>
 * @license <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *          Apache License 2.0</a>
 */
public class JMatrixBaseException extends RuntimeException {

    /**
     * Stores the serial version number of this class for deserialization to
     * verify that the sender and receiver of a serialized object have loaded classes
     * for that object that are compatible with respect to serialization.
     *
     * @serial
     * @see    java.io.Serializable
     */
    private static final long serialVersionUID = 8_294_400_000L;

    /**
     * A string represents the detail message of this exception.
     *
     * @serial
     * @see    #getMessage()
     */
    private String message = null;

    /**
     * Stores the corresponding {@link JMErrorCode} for later retrieval
     * by {@link #getErrorCode()}.
     *
     * @serial
     * @see    #getErrorCode()
     */
    private JMErrorCode errcode = null;

    /** An error message format without the error code. */
    protected final String ERR_MSG_FORMAT = "%s: %s";
    /** An error message format with the error code. */
    protected final String ERR_MSG_WITH_CODE_FORMAT = "%s <%s>: %s";

    /** A stack trace format for this throwable's stack traces. */
    private final String STACK_TRACE_FORMAT = "\tat \"%s.%s\" -> \"%s\": line %d\n";
    /** A stack trace format for this throwable's stack traces. */
    private final String EXCEPTION_INFO_FORMAT =
        "\n[EXCEPTION INFO]\nType: %s\nCode: %s\nMessage: %s\n";

    /** Caption for labelling this exception. */
    private final String MSG_CAPTION = "/!\\ EXCEPTION";
    /** Caption for labelling causative exception stack traces. */
    private final String CAUSE_CAPTION = "/!\\ CAUSED BY";

    /**
     *
     */

    /**
     * Stores a string represents the detail message of this exception.
     *
     * @see   #getMessage()
     */
    private String message = null;

    /**
     * Indicates whether this exception is caused by another exception.
     */
    private boolean isCausedException = false;

    /**
     * Constructs a new {@code JMatrixBaseException} with no detail message.
     *
     * @since 1.0.0b.1
     */
    public JMatrixBaseException() {
        super();
    }

    /**
     * Constructs a new {@code JMatrixBaseException} with the specified detail message.
     *
     * @param s  The detail message and will be saved for later retrieval
     *           by the {@link #getMessage()} method.
     *
     * @since    1.0.0b.1
     */
    public JMatrixBaseException(String s) {
        super(s);
        this.message = s;
        this.errcode = JMErrorCode.UNKERR;  // Set to 'Unknown error'
    }

    /**
     * Constructs a new {@code JMatrixBaseException} with the specified errno
     * and the detail message.
     *
     * @param errno  The error number.
     * @param s      The detail message and will be saved for later retrieval
     *               by the {@link #getMessage()} method.
     *
     * @since  1.5.0
     * @see    #getMessage()
     * @see    #getErrorCode()
     */
    public JMatrixBaseException(int errno, String s) {
        super(s);
        this.message = s;
        this.errcode = JMErrorCode.valueOf(errno);
    }

    /**
     * Constructs a new {@code JMatrixBaseException} with the specified cause
     * and a detail message of the cause.
     *
     * @param cause  the cause of this exception.
     *
     * @since        1.0.0b.1
     */
    public JMatrixBaseException(Throwable cause) {
        super(cause);
        this.message = (cause == null) ? null : cause.toString();

        // Get the error code if the `cause` is an instance of this class
        if (cause instanceof JMatrixBaseException) {
            this.errcode = ((JMatrixBaseException) cause).errcode;
        }
    }
        
    public JMatrixBaseException(String s, Throwable cause) {
        super(s, cause);
        this.message = s;

        // Get the error code if the `cause` is an instance of this class
        if (cause instanceof JMatrixBaseException) {
            this.errcode = ((JMatrixBaseException) cause).errcode;
        }
    }

    protected JMatrixBaseException(String message, Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message = message;
    }

    /**
     * Prints this throwable and its backtrace to the standard error stream ({@code System.err}).
     *
     * <p>The format of this information depends on the implementation, but the following
     * example may be regarded as typical:
     *
     * <blockquote><pre>&nbsp;
     * /!\ EXCEPTION
     * >>>>>>>>>>>>>
     * com.mitsuki.jmatrix.exception.InvalidIndexException [INVIDX]: Given row index is out of bounds
     *
     * [EXCEPTION INFO]
     * Type: com.mitsuki.jmatrix.exception.InvalidIndexException
     * Code: INVIDX
     * Message: Given row index is out of bounds
     * </pre></blockquote>
     *
     * <p>Output above was produced by this code:
     * <pre>&nbsp;
     *   Matrix m = new Matrix(new double[][] {
     *       { 10, 10, 10 },
     *       { 20, 20, 20 }
     *   });
     *   m = m.dropRow(3);  // Throws
     * </pre>
     *
     * @since 1.0.0b.1
     */
    @Override
    public void printStackTrace() {
        printStackTrace(System.err);
    }

    /**
     * {@inheritDoc}
     *
     * @since 1.5.0
     */
    @Override
    public void printStackTrace(PrintStream stream) {
        final String arrows = ">>>>>>>>>>>>>";
        final StackTraceElement[] trace = getStackTrace();
        final Throwable cause = getCause();

        // First, print the title including the exception description
        stream.printf("\n%s\n%s\n%s\n", MSG_CAPTION, arrows, this.toString());

        if (trace != null) {
            if (trace.length > 2) {
                for (int i = trace.length - 2; i != trace.length - 1; i++) {
                    stream.printf(STACK_TRACE_FORMAT,
                        trace[i].getClassName(),   //* class name
                        trace[i].getMethodName(),  //* method name
                        trace[i].getFileName(),    //* file name
                        trace[i].getLineNumber()   //* line number
                    );
                }
            } else {
                for (StackTraceElement ste : trace) {
                    stream.printf(STACK_TRACE_FORMAT,
                        ste.getClassName(),   //* class name
                        ste.getMethodName(),  //* method name
                        ste.getFileName(),    //* file name
                        ste.getLineNumber()   //* line number
                    );
                }
            }

            if (cause != this) {
                // Retrieve the stack trace of cause exception
                final StackTraceElement[] causeTrace = cause.getStackTrace();
                stream.printf("\n%s\n%s\n%s\n", CAUSE_CAPTION, arrows, getCause());

                for (StackTraceElement cste : causeTrace) {
                    stream.printf(STACK_TRACE_FORMAT,
                        cste.getClassName(),   //* class name
                        cste.getMethodName(),  //* method name
                        cste.getFileName(),    //* file name
                        cste.getLineNumber()   //* line number
                    );
                }
            }
        }

        stream.printf(EXCEPTION_INFO_FORMAT,
            (cause == this)
                ? this.getClass().getName()
                : cause.toString().split(":\\s")[0],
            this.getErrorCode().getCode(),
            this.message
        );
    }

    /**
     * {@inheritDoc}
     *
     * @since  1.0.0b.1
     */
    @Override
    public String getMessage() {
        return (this.message != null) ? this.message : getCause().toString();
    }

    /**
     * Returns the error code of this throwable.
     *
     * If the error code is {@code null} or not known, it returns {@link JMErrorCode#UNKERR UNKERR} error code.
     *
     * @return  The {@link JMErrorCode} object which contains the message and the error number.
     *
     * @since  1.5.0
     * @see    JMErrorCode
     */
    public synchronized JMErrorCode getErrorCode() {
        return (this.errcode != null) ? this.errcode : JMErrorCode.UNKERR;
     }

     /**
      * {@inheritDoc}
      *
      * @since  1.5.0
      */
     @Override
     public synchronized Throwable getCause() {
        // Return null if the cause is a reference to this class
        Throwable cause = super.getCause();
        return (cause == this) ? null : cause;
     }

    /**
     * Returns a string representation of this exception, including the class name and the detail message.
     *
     * @return a string representation of this exception.
     *
     * @since  1.0.0b.1
     */
    @Override
    public String toString() {
        return ((this.errcode == null)
            ? String.format(ERR_MSG_FORMAT, this.getClass().getName(), this.message)
            : String.format(ERR_MSG_WITH_CODE_FORMAT, this.getClass().getName(),
                this.getErrorCode(),
                this.message
            )
        );
    }

}
