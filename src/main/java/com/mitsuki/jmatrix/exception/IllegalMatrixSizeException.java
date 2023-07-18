// :: ---------------------------- :: //
/* --  IllegalMatrixSizeException  -- */
// :: ---------------------------- :: //

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
 * Thrown when attempting to perform matrix operations with matrices that do not
 * conform to the sizes requirements of the operation.
 *
 * <p>For example, if matrix {@code A} has dimensions {@code 3x5} and matrix {@code B} has dimensions {@code 5x5}, this exception will be thrown
 * because the matrices do not conform to the requirements of matrix addition, where the dimensions of the operand matrices should be the same.
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
public class IllegalMatrixSizeException extends JMatrixBaseException
{

    /**
     * Stores a string represents the detail message of this exception.
     *
     * @see #getMessage()
     */
    private String message = null;

    /**
     * Constructs a new {@code IllegalMatrixSizeException} with no detail message.
     *
     * @since 0.1.0
     */
    public IllegalMatrixSizeException() {
        super();
    }

    /**
     * Constructs a new {@code IllegalMatrixSizeException} with the specified detail message.
     *
     * @param message  the detail message.
     *
     * @since          0.1.0
     */
    public IllegalMatrixSizeException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * Constructs a new {@code IllegalMatrixSizeException} with the specified cause
     * and a detail message of the cause.
     *
     * @param cause  the cause of this exception.
     *
     * @since        0.1.0
     */
    public IllegalMatrixSizeException(Throwable cause) {
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
     * Returns a string representation of this exception, including the class name and the detail message.
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
