// :: --------------------- :: //
/* --  NullMatrixException  -- */
// :: --------------------- :: //

/* Copyright (c) 2023 Ryuu Mitsuki
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

/**
 * Thrown when an operation is attempted on uninitialized matrices. This exception
 * indicates that the matrices involved in the operation have not been initialized
 * correctly or are {@code null}. For example:
 *
 * <pre><code class="language-java">&nbsp;
 *   Matrix nullMatrix = new Matrix();
 *   nullMatrix.transpose();  // this will throw an exception
 * </code></pre>
 *
 * <p><b>Type:</b> Unchecked exception</p>
 *
 * <p>The uninitialized or {@code null} matrices can be identified by
 * using {@link com.mitsuki.jmatrix.core.MatrixUtils#isNullEntries(Matrix)}.
 * Or can be manually, for example:
 *
 * <pre><code class="language-java">&nbsp;
 *   if (nullMatrix.getEntries() == null) {
 *       // ...
 *   } else {
 *       // ...
 *   }
 * </code></pre>
 *
 * @since   0.1.0
 * @version 1.3, 18 July 2023
 * @author  <a href="https://github.com/mitsuki31" target="_blank">
 *          Ryuu Mitsuki</a>
 * @license <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *          Apache License 2.0</a>
 *
 * @see     com.mitsuki.jmatrix.exception.JMatrixBaseException
 */
public class NullMatrixException extends JMatrixBaseException
{

    /**
     * Stores the serial version number of this class for deserialization to
     * verify that the sender and receiver of a serialized object have loaded classes
     * for that object that are compatible with respect to serialization.
     *
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = 6_418_048_608L;

    /**
     * Stores a string represents the detail message of this exception.
     *
     * @see   #getMessage()
     */
    private String message = null;

    /**
     * Constructs a new {@code NullMatrixException} with no detail message.
     * The message will be {@code null} by default.
     *
     * @since 0.1.0
     */
    public NullMatrixException() {
        super();
    }

    /**
     * Constructs a new {@code NullMatrixException} with the specified detail message.
     *
     * @param message  the detail message.
     *
     * @since          0.1.0
     */
    public NullMatrixException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * Constructs a new {@code NullMatrixException} with the specified cause
     * and a detail message of the cause.
     *
     * @param cause  the cause of this exception.
     *
     * @since        0.1.0
     */
    public NullMatrixException(Throwable cause) {
        super(cause);
        this.message = cause.getMessage();
    }


    /**
     * Returns the detail message of this exception.
     *
     * @return the detail message.
     *
     * @since  0.1.0
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    /**
     * Returns a string representation of this exception, including
     * the class name and the detail message.
     *
     * @return a string representation of this exception.
     *
     * @since  0.1.0
     */
    @Override
    public String toString() {
        return String.format("%s: %s", this.getClass().getName(), this.message);
    }
}
