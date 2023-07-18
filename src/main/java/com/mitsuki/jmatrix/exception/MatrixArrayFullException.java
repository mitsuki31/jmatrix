// :: -------------------------- :: //
/* --  MatrixArrayFullException  -- */
// :: -------------------------- :: //

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
 * Thrown when attempting to add an element to a matrix that has reached its maximum size.
 * This exception is deprecated because it contradicts the principles of linear algebra, where matrices can grow
 * dynamically with matrix operations.
 *
 * <p>It is recommended to review the code and consider alternative approaches that align with the principles of linear
 * algebra and avoid the need for fixed-size matrices.
 *
 * <p><b>Type:</b> Unchecked exception</p>
 *
 * @since      0.1.0
 * @version    1.2, 18 July 2023
 * @author     <a href="https://github.com/mitsuki31" target="_blank">
 *             Ryuu Mitsuki</a>
 * @license    <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *             Apache License 2.0</a>
 *
 * @deprecated This exception is deprecated because it contradicts the principles of linear algebra. It is recommended
 *             to review the code and find alternative approaches that align with the dynamic nature of matrices in linear algebra.
 */
@Deprecated
public class MatrixArrayFullException extends JMatrixBaseException
{

    /**
     * Stores a string represents the detail message of this exception.
     *
     * @see #getMessage()
     */
    private String message = null;

    /**
     * Constructs a new {@code MatrixArrayFullException} with no detail message.
     *
     * @since 0.1.0
     */
    public MatrixArrayFullException() {
        super();
    }

    /**
     * Constructs a new {@code MatrixArrayFullException} with the specified detail message.
     *
     * @param message  the detail message.
     *
     * @since          0.1.0
     */
    public MatrixArrayFullException(String message) {
        super(message);
        this.message = message;
    }

    /**
     * Constructs a new {@code MatrixArrayFullException} with the specified cause
     * and a detail message of the cause.
     *
     * @param cause  the cause of this exception.
     *
     * @since        0.1.0
     */
    public MatrixArrayFullException(Throwable cause) {
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
