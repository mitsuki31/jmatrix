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
 * Base exception class for all <b>JMatrix</b> exceptions. This exception and its subclasses
 * are unchecked exceptions, which means they can be thrown during runtime without being 
 * explicitly caught or declared.
 *
 * <p>The class {@code JMatrixBaseException} serves as a foundational exception that can be 
 * used in a matrix-related context. It provides a consistent interface for handling errors 
 * and can wrap other exceptions, allowing for a more flexible and unified error-handling 
 * strategy across the JMatrix library.
 *
 * <p><b>Type:</b> Unchecked exception</p>
 *
 * <p>As of version 1.5.0, a new capability has been introduced to configure the behavior
 * of auto-raised exceptions either using an environment variable or system property. This
 * feature allows applications to determine whether exceptions should be automatically
 * raised and cause the application to exit, or if they should be thrown and handled
 * via traditional try-catch blocks. This flexibility is crucial for scenarios where
 * different environments or phases of development might require different error-handling
 * approaches.
 *
 * <p><b>Configure Auto-raise Behavior:</b>
 *
 * <p>The behavior is controlled by setting either the system property {@value #raiseConfName}
 * or the environment variable {@value #raiseConfEnvName}. The configuration options are
 * as follows:
 *
 * <ul>
 * <li> If the auto-raise configuration either from the system property ({@value
 *      #raiseConfName}) or environment variable ({@value #raiseConfEnvName}) is set to
 *      {@code manual} or {@code no}, the exceptions will be thrown, enabling traditional
 *      error handling through try-catch blocks.
 * <li> If the auto-raise configuration either from the system property ({@value
 *      #raiseConfName}) or environment variable ({@value #raiseConfEnvName}) is set to
 *      {@code auto} or {@code yes}, or if it is undefined or an empty string,
 *      the exceptions will be auto-raised. This means the stack trace of the exception
 *      will be printed, and the application will exit immediately with the specified
 *      error code. This is useful for environments where immediate feedback is necessary,
 *      such as in development or testing phases.
 * </ul>
 *
 * <p>If the auto-raise configuration is set using both environment variables
 * and system properties, the system property will take precedence.
 *
 * <p>This enhancement offers greater control over exception handling, making the
 * <b>JMatrix</b> library more adaptable to various use cases and environments. It
 * ensures that developers can choose the most appropriate error-handling strategy
 * based on their specific needs.
 *
 * <p><b>It is RECOMMENDED to set the configuration using system property.</b>
 * Here is an example usage to set the {@value #raiseConfName} configuration using
 * system property from command-line.
 * <pre>&nbsp;
 *   $ java -D{@value #raiseConfName}=manual -cp /path/to/jmatrix-<VERSION>.jar Foo.java
 * </pre>
 *
 * <p>And this is an example usage to set the {@value #raiseConfName} configuration at runtime.
 * <pre>&nbsp;
 *   // Set the environment variable to configure behavior
 *   System.setProperty({@value #raiseConfName}, "manual");
 *   try {
 *      // Some matrix-related operations that might throw
 *      // JMatrixBaseException or its subclasses
 *   } catch (JMatrixBaseException e) {
 *      // Handle exception
 *   }
 * </pre>
 *
 * @since   1.0.0b.1
 * @version 1.3, 10 June 2024
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
    private static final long serialVersionUID = 43_003_192_023_400L;

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

    /** A property name for the auto-raise configuration. */
    private static final String raiseConfName = "jm.autoraise";
    /** An environment variable name for the auto-raise configuration. */
    private static final String raiseConfEnvName = "jm_autoraise";

    /**
     * The value of auto-raise configuration.
     *
     * <p>The value can be one of the following:
     * <ul>
     * <li>{@code auto}
     * <li>{@code manual}
     * </ul>
     *
     * Fallback to {@code auto} if both the {@value #raiseConfName} (in system
     * properties) and {@value #raiseConfEnvName} (in environment variables) is not
     * defined or set to an empty string.
     *
     * @see   #getRaiseConfig()
     * @see   #getRaiseConfigFromSystemProps()
     */
    private static String raiseConf = getRaiseConfig();

    /**
     * Retrieves the value of the {@value #raiseConfName} from the system property,
     * if not set, then it tries to retrieves from environment variables named
     * {@value #raiseConfEnvName}.
     *
     * <p>This method checks the value of the {@value #raiseConfName} (from system
     * properties) or {@value #raiseConfEnvName} (from environment variables) and sets
     * the configuration accordingly. The configuration will be set to {@code auto}
     * if the value is equal to one of these known values (case-insensitive):
     * <ul>
     * <li>{@code auto} or {@code a}
     * <li>{@code yes} or {@code y}
     * </ul>
     *
     * <p>The configuration will be set to {@code manual} if the value is
     * equal to one of these known values:
     * <ul>
     * <li>{@code manual} or {@code m}
     * <li>{@code no} or {@code n}
     * </ul>
     *
     * <p>If the auto-raise configuration from the system properties is not set or
     * is empty, it tries to retrieve the {@value #raiseConfEnvName} from environment
     * variables. If not set or is an empty string too, set the auto-raise configuration
     * to {@code auto}.
     *
     * <p>If the {@value #raiseConfName} configuration is set using both environment
     * variables and system properties, the system property will take precedence.
     * So, it is recommended to set the auto-raise configuration using system property.
     *
     * @implNote This method is synchronized to ensure thread safety.
     *
     * @return  The configuration value, either {@code auto} or {@code manual}.
     *
     * @throws  SecurityException  If a security manager exists and its
     *                             {@code checkPropertyAccess} method does not
     *                             allow access to the specified system property.
     *                             <b>This exception will never be thrown, but users
     *                             will be warned by a warning message.</b>
     * @throws  IllegalArgumentException  If the auto-raise configuration either from
     *                                    the system property or environment variable
     *                                    is set to an unrecognized value.
     *
     * @since  1.5.0
     * @see    #getRaiseConfigFromSystemProps()
     */
    static synchronized final String getRaiseConfig() {
        String value = getRaiseConfigFromSystemProps();

        if (value == null || (value != null && value.length() == 0)) {
            try {
                // If the auto-raise configuration from system property is not set,
                // then it tries to retrieve from environment variables safely
                value = System.getenv(raiseConfEnvName);
                if (value == null || (value != null && value.length() == 0))
                    return "auto";
            } catch (final SecurityException se) {
                System.err.println(String.format(
                    "jmatrix: [Warning] SecurityException has been occurred internally " +
                    "while attempting to get \"%s\" from environment variables",
                    raiseConfEnvName
                ));
            }
        }

        // Lower case the value if not null (undeclared)
        value = value.toLowerCase();

        if (  // Check for 'auto' value
            (value.equals("auto") || value.equals("a")) ||
            (value.equals("yes") || value.equals("y"))
        ) {
            value = "auto";
        } else if (  // Check for 'manual' value
            (value.equals("manual") || value.equals("m")) ||
            (value.equals("no") || value.equals("n"))
        ) {
            value = "manual";
        } else {
            // Throw an exception if the value is not known
            throw new IllegalArgumentException(String.format(
                "Unknown value of '%s': %s",
                raiseConfName, value
            ));
        }
        return value;
    }

    /**
     * Retrieves the value of the system property named {@value #raiseConfName}.
     *
     * <p>This method attempts to obtain the value of the system property named
     * {@value raiseEnvName}. It handles any {@link SecurityException} that may occur
     * during this process and logs a warning message if such an exception is
     * encountered without throwing the exception.
     *
     * @implNote The method is synchronized to ensure thread safety.
     *
     * @return  The value of the system property {@value raiseEnvName}, or {@code null}
     *          if the property is not found or a {@link SecurityException} occurs.
     *
     * @throws  SecurityException  If a security manager exists and its
     *                             {@code checkPropertyAccess} method does not
     *                             allow access to the specified system property.
     *                             <b>This exception will never be thrown, but users
     *                             will be warned by a warning message.</b>
     *
     * @since  1.5.0
     * @see    #getRaiseConfig()
     */
    static synchronized final String getRaiseConfigFromSystemProps() {
        try {
            return System.getProperty(raiseConfName);
        } catch (final SecurityException se) {
            // Print a warning message when SecurityException occurred
            System.err.println(String.format(
                "jmatrix: [Warning] SecurityException has been occurred internally " +
                "while attempting to get \"%s\" from system properties",
                raiseConfName
            ));
        }
        return null;
    }

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
     * @see   #printStackTrace()
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
     * Returns a string representation of this exception, including the class name, error code, and
     * the detail message.
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


    /**
     * Checks whether the current value of auto-raise configuration is set to {@code auto}.
     * 
     * @return {@code true} if the auto-raise configuration is set to {@code auto}.
     *         Otherwise, returns {@code false}.
     *
     * @implNote The method is synchronized to ensure thread safety.
     *
     * @since  1.5.0
     */
    public static synchronized boolean isAutoRaise() {
        raiseConf = getRaiseConfig();
        return raiseConf.equals("auto");
    }

    /**
     * Raises an exception based on the specified cause, with an automatically
     * determined error number.
     *
     * <p>This method provides a mechanism to either print the stack trace of a 
     * given exception and exit the application with a specified error code, or 
     * throw the exception, depending on the configuration set by the 
     * {@value #raiseConfName} environment variable. The error number is determined 
     * based on the type of the provided exception.
     *
     * <p>If the {@link #isAutoRaise()} method returns {@code true}, indicating 
     * that the {@value #raiseConfName} environment variable is set to {@code auto}:
     * <ul>
     * <li> The stack trace of the provided exception {@code cause} is printed to 
     *      the standard error stream.
     * <li> The application then exits with the determined error number.
     * <li> An error message indicating the exit code is printed to the standard 
     *      error stream ({@code System.err}).
     * </ul>
     *
     * <p>If the {@link #isAutoRaise()} returns {@code false}, the provided exception 
     * {@code cause} is thrown, allowing it to propagate up the call stack. This 
     * behavior supports scenarios where the application prefers to handle exceptions 
     * using traditional try-catch blocks rather than exiting.
     *
     * <p>The error number is determined as follows:
     * <ul>
     * <li> If the provided {@code cause} is either of an instance of {@link JMatrixBaseException}
     *      or a subclass thereof, the error number is retrieved using the {@link
     *      JMErrorCode#getErrno() cause.getErrorCode().getErrno()} method.
     * <li> If the provided {@code cause} is not an instance of {@link JMatrixBaseException}, 
     *      the error number is set to {@link JMErrorCode#UNKERR UNKERR} (Unknown error), 
     *      which corresponds to the error code {@code 400}.
     * </ul>
     *
     * @param <E>    The type of the runtime exception to be raised.
     * @param cause  The exception that caused the error. This exception will either 
     *               be printed and cause the application to exit, or will be thrown 
     *               based on the configuration.
     *
     * @throws E     If the auto-raise configuration is not set to {@code auto},
     *               the method throws the provided exception {@code cause}.
     *
     * @since  1.5.0
     * @see    #raise(RuntimeException, int)
     */
    public static <E extends RuntimeException> void raise(E cause) {
        raise(cause,
            // Retrieve the errno if the given object is an instance of this class
            // Otherwise, set the errno to 400 (Unknown error)
            (cause instanceof JMatrixBaseException)
                ? ((JMatrixBaseException) cause).errcode.getErrno()
                : JMErrorCode.UNKERR.getErrno()
        );
    }

    /**
     * Raises an exception based on the specified cause and error number.
     *
     * <p>This method provides a mechanism to either print the stack trace of a 
     * given exception and exit the application with a specified error code, or 
     * throw the exception, depending on the configuration set by the 
     * {@value #raiseConfName} environment variable. This allows for configurable 
     * error handling that can be controlled through environment settings.
     *
     * <p>It will checks whether the auto-raise configuration is set to {@code auto}
     * utilizing the {@link #isAutoRaise()} method, if the {@link #isAutoRaise()}
     * method returns {@code true}, indicating that the auto-raise configuration
     * is set to {@code auto}:
     * <ul>
     * <li> The stack trace of the provided exception {@code cause} is printed to 
     *      the standard error stream.
     * <li> The application then exits with the specified error number {@code errno}.
     *      If {@code errno} is less than {@link Integer#MIN_VALUE} ({@value
     *      Integer#MIN_VALUE}), it is set to {@link Integer#MIN_VALUE} to ensure
     *      it is within valid range.
     * <li> An error message indicating the exit code is printed to the standard 
     *      error stream.
     * </ul>
     *
     * <p>If {@link #isAutoRaise()} returns {@code false}, the provided exception 
     * {@code cause} is thrown, allowing it to propagate up the call stack. This 
     * behavior supports scenarios where the application prefers to handle exceptions 
     * using traditional try-catch blocks rather than exiting.
     *
     * @param <E>    The type of the runtime exception to be raised.
     * @param cause  The exception that caused the error. This exception will either 
     *               be printed and cause the application to exit, or will be thrown 
     *               based on the configuration.
     * @param errno  The error number to exit with if auto-raise is enabled. This 
     *               number should be a valid integer within the range 
     *               {@link Integer#MIN_VALUE} to {@link Integer#MAX_VALUE}. If the 
     *               provided value is less than {@link Integer#MIN_VALUE}, it will 
     *               be adjusted to {@link Integer#MIN_VALUE}.
     *
     * @throws E     If the auto-raise configuration is not set to {@code auto},
     *               the method throws the provided exception {@code cause}.
     *
     * @since  1.5.0
     * @see    #raise(RuntimeException)
     */
    public static <E extends RuntimeException> void raise(E cause, int errno) {
        if (isAutoRaise()) {
            // Print the stack trace and exit with the specified errno
            cause.printStackTrace();
            errno = (errno < Integer.MIN_VALUE) ? Integer.MIN_VALUE : errno;
            System.err.printf("\nExited with error code: %d\n", errno);
            System.exit(errno);
        } else {
            throw cause;  // Throw the cause if not auto raise
        }
    }
}
