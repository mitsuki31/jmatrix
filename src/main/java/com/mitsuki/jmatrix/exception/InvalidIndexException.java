// :: ----------------------- :: //
/* --  InvalidIndexException  -- */
// :: ----------------------- :: //

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
 * Thrown when attempting to retrieve an entry at specified indices of a matrix, but the given indices are out of range.
 * This exception can also be thrown when providing an invalid (out of range)
 * row index to the {@link com.mitsuki.jmatrix.Matrix#display(int)} method
 * for displaying a specified row to the standard output.
 *
 * <p>For example, if a matrix has dimensions {@code 5x5}, accessing the last entry should be done using {@code get(4, 4)}
 * and not {@code get(5, 5)}. It's important to note that indices start from {@code 0} (zero).
 *
 * <p><b>Type:</b> Unchecked exception</p>
 *
 * @since   0.1.0
 * @version 1.2, 18 July 2023
 * @author  <a href="https://github.com/mitsuki31" target="_blank">
 *          Ryuu Mitsuki</a>
 * @license <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *          Apache License 2.0</a>
 */
public class InvalidIndexException extends JMatrixBaseException
{

    /**
     * Stores a string represents the detail message of this exception.
     *
     * @see   #getMessage()
     */
    private String message = null;

    /**
     * Constructs a new {@code InvalidIndexException} with no detail message.
     *
     * @since 0.2.0
     */
    public InvalidIndexException() {
        super();
    }

    /**
     * Constructs a new {@code InvalidIndexException} with the specified detail message.
     *
     * @param message  the detail message.
     *
     * @since          0.2.0
     */
    public InvalidIndexException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * Constructs a new {@code InvalidIndexException} with the specified cause
     * and a detail message of the cause.
     *
     * @param cause  the cause of this exception.
     *
     * @since        0.2.0
     */
    public InvalidIndexException(Throwable cause) {
        super(cause);
        this.message = cause.getMessage();
    }

    /**
     * Returns the detail message of this exception.
     *
     * @return the detail message.
     *
     * @since  0.2.0
     */
    @Override
    public String getMessage() {
        return this.message;
    }

    /**
     * Returns a string representation of this exception, including the class name and the detail message.
     *
     * @return a string representation of this exception.
     *
     * @since  0.2.0
     */
    @Override
    public String toString() {
        return String.format("%s: %s", this.getClass().getName(), this.message);
    }
}
