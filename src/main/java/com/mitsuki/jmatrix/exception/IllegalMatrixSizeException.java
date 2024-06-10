// :: ---------------------------- :: //
/* --  IllegalMatrixSizeException  -- */
// :: ---------------------------- :: //

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
 * Thrown when attempting to perform matrix operations with matrices that do not
 * conform to the sizes requirements of the operation.
 *
 * <p>For example, if matrix {@code A} has dimensions {@code 3x5} and matrix {@code B} has dimensions {@code 5x5}, this exception will be thrown
 * because the matrices do not conform to the requirements of matrix addition, where the dimensions of the operand matrices should be the same.
 *
 * <p><b>Type:</b> Unchecked exception</p>
 *
 * @since   0.1.0
 * @version 1.3, 10 June 2024
 * @author  <a href="https://github.com/mitsuki31" target="_blank">
 *          Ryuu Mitsuki</a>
 * @license <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *          Apache License 2.0</a>
 */
public class IllegalMatrixSizeException extends JMatrixBaseException {

    /**
     * Stores the serial version number of this class for deserialization to
     * verify that the sender and receiver of a serialized object have loaded classes
     * for that object that are compatible with respect to serialization.
     *
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = 43_003_192_023_202L;

    private static int defaultErrno = JMErrorCode.INVTYP.getErrno();

    /**
     * Constructs a new {@code IllegalMatrixSizeException} with no detail message.
     *
     * @since 0.1.0
     */
    public IllegalMatrixSizeException() {
        super(defaultErrno, null);
    }

    /**
     * Constructs a new {@code IllegalMatrixSizeException} with the specified detail message.
     *
     * @param message  the detail message.
     *
     * @since          0.1.0
     */
    public IllegalMatrixSizeException(String message) {
        super(defaultErrno, message);
    }

    public IllegalMatrixSizeException(int errno, String message) {
        super(errno, message);
        defaultErrno = errno;
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
    }

    public IllegalMatrixSizeException(String s, Throwable cause) {
        super(s, cause);
    }
    
    public IllegalMatrixSizeException(String s, Throwable cause,
                                      boolean enableSuppression,
                                      boolean writableStackTrace) {
        super(s, cause, enableSuppression, writableStackTrace);
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
     * @since  0.1.0
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
