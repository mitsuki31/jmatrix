// :: ----------------------- :: //
/* --  InvalidIndexException  -- */
// :: ----------------------- :: //

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

import com.mitsuki.jmatrix.enums.JMErrorCode;

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
 * @version 1.3, 09 June 2024
 * @author  <a href="https://github.com/mitsuki31" target="_blank">
 *          Ryuu Mitsuki</a>
 * @license <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *          Apache License 2.0</a>
 */
public class InvalidIndexException extends JMatrixBaseException {

    /**
     * Stores the serial version number of this class for deserialization to
     * verify that the sender and receiver of a serialized object have loaded classes
     * for that object that are compatible with respect to serialization.
     *
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = 10_585_989_792L;

    private static int defaultErrno = JMErrorCode.INVIDX.getErrno();

    /**
     * Constructs a new {@code InvalidIndexException} with no detail message.
     *
     * @since 0.2.0
     */
    public InvalidIndexException() {
        super(defaultErrno, null);
    }

    /**
     * Constructs a new {@code InvalidIndexException} with the specified detail message.
     *
     * @param message  the detail message.
     *
     * @since          0.2.0
     */
    public InvalidIndexException(String message) {
        super(defaultErrno, message);
    }

    public InvalidIndexException(int errno, String s) {
        super(errno, s);
        defaultErrno = errno;
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
    }

    public InvalidIndexException(String s, Throwable cause) {
        super(s, cause);
    }

    /**
     * {@inheritDoc}
     *
     * @since  0.2.0
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public JMErrorCode getErrorCode() {
        return JMErrorCode.valueOf(defaultErrno);
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
        JMErrorCode ec = this.getErrorCode();
        String errmsg = this.getMessage();
        return String.format(this.ERR_MSG_WITH_CODE_FORMAT,
            this.getClass().getName(),
            ec.getCode(),
            (errmsg != null) ? errmsg : ec.getMessage()
        );
    }
}
