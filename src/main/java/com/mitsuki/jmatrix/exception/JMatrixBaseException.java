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
        this.errcode = JMErrorCode.UNKERR;
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
        // Prevent from internal error due to null value
        if (this.errcode == null) this.errcode = JMErrorCode.UNKERR;
    }

    /**
     * Constructs a new {@code JMatrixBaseException} with the specified cause.
     *
     * <p>This constructor allows for exception chaining, where the new exception 
     * is caused by an existing throwable. This is useful for wrapping lower-level
     * exceptions in higher-level exceptions, providing more context about the error
     * that occurred.
     *
     * <p>The detailed message for the new exception is derived from the cause:
     * <ul>
     * <li> If the {@code cause} is not {@code null}, the detailed message is obtained
     *      by calling {@code cause.toString()}, which typically includes the class name 
     *      and the message of the cause.
     * <li> If the {@code cause} is {@code null}, the detailed message for the new 
     *      exception is set to {@code null}.
     * </ul>
     *
     * <p>Additionally, this constructor attempts to propagate the error code from 
     * the cause to the new exception:
     * <ul>
     * <li> If the {@code cause} is an instance of {@code JMatrixBaseException} or a 
     *      subclass thereof, the error code is retrieved using {@link #getErrorCode()} 
     *      and set for the new exception.
     * <li> If the {@code cause} is not an instance of {@code JMatrixBaseException}, 
     *      the error code for the new exception is set to {@link JMErrorCode#UNKERR 
     *      UNKERR} (Unknown error).
     * </ul>
     *
     * @param cause  The cause of this exception. A {@code null} value is permitted
     *               and indicates that the cause is nonexistent or unknown.
     *
     * @since  1.0.0b.1
     */
    public JMatrixBaseException(Throwable cause) {
        super(cause);
        this.message = (cause == null) ? null : cause.toString();

        // Get the error code if the `cause` is an instance of this class,
        // if not, fallback to UNKERR error code
        this.errcode = (cause instanceof JMatrixBaseException)
            ? ((JMatrixBaseException) cause).errcode
            : JMErrorCode.UNKERR;
    }

    /**
     * Constructs a new {@code JMatrixBaseException} with the specified cause
     * and a detailed message for later retrieval by {@link #getMessage()}.
     *
     * <p>This constructor allows for exception chaining, where the new exception 
     * is caused by an existing throwable. This is useful for wrapping lower-level
     * exceptions in higher-level exceptions, providing more context about the error
     * that occurred.
     *
     * <p>Additionally, this constructor attempts to propagate the error code from 
     * the cause to the new exception:
     * <ul>
     * <li> If the {@code cause} is an instance of {@code JMatrixBaseException} or a 
     *      subclass thereof, the error code is retrieved using {@link #getErrorCode()} 
     *      and set for the new exception.
     * <li> If the {@code cause} is not an instance of {@code JMatrixBaseException}, 
     *      the error code for the new exception is set to {@link JMErrorCode#UNKERR 
     *      UNKERR} (Unknown error).
     * </ul>
     *
     * @param s      The descriptive message.
     * @param cause  The cause of this exception. A {@code null} value is permitted
     *               and indicates that the cause is nonexistent or unknown.
     *
     * @since  1.5.0
     */
    public JMatrixBaseException(String s, Throwable cause) {
        super(s, cause);
        this.message = s;

        // Get the error code if the `cause` is an instance of this class,
        // if not, fallback to UNKERR error code
        this.errcode = (cause instanceof JMatrixBaseException)
            ? ((JMatrixBaseException) cause).errcode
            : JMErrorCode.UNKERR;
    }

    protected JMatrixBaseException(String message, Throwable cause,
                                   boolean enableSuppression,
                                   boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.message = message;

        // Get the error code if the `cause` is an instance of this class,
        // if not, fallback to UNKERR error code
        this.errcode = (cause instanceof JMatrixBaseException)
            ? ((JMatrixBaseException) cause).errcode
            : JMErrorCode.UNKERR;
    }

    /**
     * Prints this throwable and its backtrace to the standard error stream ({@code System.err}).
     *
     * <p>The format of this information depends on the implementation, but the following
     * example may be regarded as typical:
     *
     * <blockquote><pre>&nbsp;
     * /!\ EXCEPTION
     * &gt;&gt;&gt;&gt;&gt;&gt;&gt;&gt;&gt;&gt;&gt;&gt;&gt;
     * com.mitsuki.jmatrix.exception.InvalidIndexException [INVIDX]: Given row index is out of bounds
     *         at "Example.main" -> "Example.java": line 7
     *         ...
     *
     * [EXCEPTION INFO]
     * Type: com.mitsuki.jmatrix.exception.InvalidIndexException
     * Code: INVIDX
     * Message: Given row index is out of bounds
     * </pre></blockquote>
     *
     * <p>Output above was produced by this code:
     * <pre>&nbsp;
     *   public class Example {
     *      public static void main(String[] args) {
     *          Matrix m = new Matrix(new double[][] {
     *              { 10, 10, 10 },
     *              { 20, 20, 20 }
     *          });
     *          m = m.dropRow(3);  // Throws
     *      }
     *   }
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

        if (trace.length != 0 /* detect UNASSIGNED_STACK */) {
            for (StackTraceElement ste : trace) {
                stream.printf(STACK_TRACE_FORMAT,
                    ste.getClassName(),   //* class name
                    ste.getMethodName(),  //* method name
                    ste.getFileName(),    //* file name
                    ste.getLineNumber()   //* line number
                );
            }
        }

        if (cause != null) {
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

        stream.printf(EXCEPTION_INFO_FORMAT,
            (cause == null)
                ? this.getClass().getName()
                : cause.toString().split(":\\s")[0],
            this.getErrorCode().getCode(),
            this.getMessage()
        );
    }

    /**
     * {@inheritDoc}
     *
     * @since  1.0.0b.1
     */
    @Override
    public String getMessage() {
        Throwable cause = getCause();
        return (this.message != null)
            ? this.message
            : (cause != null) ? cause.toString() : this.errcode.getMessage();
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
        JMErrorCode ec = getErrorCode();
        return String.format(ERR_MSG_WITH_CODE_FORMAT,
            getClass().getName(),
            ec.getCode(),
            (this.message != null) ? this.message : ec.getMessage()
        );
    }

}
