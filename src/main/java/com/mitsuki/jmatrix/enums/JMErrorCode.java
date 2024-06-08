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

package com.mitsuki.jmatrix.enums;

import com.mitsuki.jmatrix.Matrix;
import com.mitsuki.jmatrix.exception.*;

/**
 * The {@code JMErrorCode} enum represents error codes for various error conditions
 * that can occur within a <b>JMatrix</b> library. Each error code is associated with
 * an integer error number, a string representation of the error code, and a descriptive
 * error message.
 *
 * <p>This enum provides methods to retrieve the error number, error code, and error message.
 * Additionally, it provides a static method to convert an error number or a string
 * representation of the error code back to its corresponding {@code JMErrorCode} enum value,
 * it is declared as {@link #valueOf(Object)}. The method does not throws an error if the type
 * of value is unrecognized, instead it returns {@code null}.
 *
 * <p><b>Example usage:</b>
 * <pre>&nbsp;
 *   JMErrorCode errorCode = JMErrorCode.INVIDX;
 *   int errno = errorCode.getErrno();        // 201
 *   String errcode = errorCode.getCode();    // INVIDX
 *   String errmsg = errorCode.getMessage();  // Given index is out bounds
 * </pre>
 *
 * <p>Get the error code from a thrown exception:
 * <pre>&nbsp;
 *   try {
 *      Matrix m = new Matrix();
 *      m = Matrix.mult(m, 5);
 *   } catch (Exception e) {
 *      if (e instanceof JMatrixBaseException) {
 *          e = (JMatrixBaseException) e;
 *          String errcode = e.getErrorCode().getCode();  // NULLMT
 *      }
 *   }
 * </pre>
 *
 * @since    1.5.0
 * @version  1.0, 05 June 2024
 * @author   <a href="https://github.com/mitsuki31" target="_blank">
 *           Ryuu Mitsuki</a>
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *           Apache License 2.0</a>
 * @see      java.lang.Enum
 */
public enum JMErrorCode {
    /**
     * Error code indicating that the given index is out of bounds.
     *
     * This error code is useful to indicates that user gives an index
     * either of row or column (which from methods that indexing-related,
     * such as {@link Matrix#insertRow(int, double[])}, {@link
     * Matrix#dropColumn(int)}, and {@link Matrix#display(int)}).
     *
     * <p><b>Error number:</b> {@code JM201}
     * <p><b>Related exception:</b> {@link InvalidIndexException}
     */
    INVIDX( 0xD3 ^ (0xD + 0xD), "Given index is out of bounds" ),
    /**
     * Error code indicating that the matrix has an invalid type.
     *
     * This error code will be in the {@link IllegalMatrixSizeException} exception
     * which related to invalid type of matrix. For example, user attempting to
     * calculate trace with a {@code 5x3} matrix, which is an illegal attemption
     * because trace calculation are only for square matrices.
     *
     * <p><b>Error number:</b> {@code JM202}
     * <p><b>Related exception:</b> {@link IllegalMatrixSizeException}
     */
    INVTYP( 0xD3 ^ (0xD + 0xC), "Matrix has invalid type of matrix" ),
    /**
     * Error code indicating that the matrix is {@code null} or the matrix
     * entries is {@code null}.
     *
     * <p>A matrix with {@code null} entries can be constructed using
     * {@link Matrix#Matrix()} constructor, which is not require any arguments.
     * Thus, the returned matrix is a matrix with a {@code null} entries (in other
     * words, an uninitialized matrix).
     *
     * <p><b>Error number:</b> {@code 203}
     * <p><b>Related exception:</b> {@link NullMatrixException}
     */
    NULLMT( 0xD3 ^ (0xD + 0xB), "Matrix is null" ),
    /**
     * Error code indicating an unknown error.
     *
     * <p>This error code can be made by the {@Link JMatrixBaseException} class
     * when failed to evaluate the given error number (errno) or the cause exception
     * is not an instance of that class while trying to get the error code from the
     * given cause exception, and ended up constructs with this error code.
     *
     * <p><b>Error number:</b> {@code 200}
     * <p><b>Related exception:</b> {@link JMatrixBaseException}
     */
    UNKERR( 0xD3 ^ (0xD - 0xE), "Unknown error" );

    /** The error nuumber stored for later retrieval by {@link #getErrno()} */
    private int errno = 0x00;
    /** The error code stored for later retrieval by {@link #getCode()} */
    private String code = null;
    /** The error message stored for later retrieval by {@link #getMessage()} */
    private String message = null;

    /**
     * Constructs a {@code JMErrorCode} with the specified error number and detail message.
     *
     * @param errno    The error number associated with this error code.
     * @param message  The descriptive error message associated with this error code.
     *
     * @since  1.5.0
     */
    JMErrorCode(int errno, String message) {
        this.code = this.toString();
        this.errno = errno;
        this.message = message;
    }

    /**
     * Returns the error number associated with this error code.
     *
     * @return  The error number.
     *
     * @since   1.5.0
     * @see     #getErrnoStr()
     */
    public int getErrno() {
        return this.errno;
    }

    /**
     * Returns the error number as a string in the format {@code JM###}.
     *
     * @return  The error number as a string, prefixed with {@code JM}.
     *
     * @since   1.5.0
     * @see     #getErrno()
     */
    public String getErrnoStr() {
        return "JM" + this.errno;  // Return: JM###
    }

    /**
     * Returns the string representation of this error code.
     *
     * @return  The error code.
     *
     * @since   1.5.0
     */
    public String getCode() {
        return this.code;
    }

    /**
     * Returns the descriptive error message associated with this error code.
     *
     * @return  The descriptive error message.
     *
     * @since   1.5.0
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * Returns the {@code JMErrorCode} corresponding to the specified value.
     *
     * <p>The value can be either an integer (error number) or a string (error code).
     * If the type of {@code x} is not recognized, this method returns {@code null}.
     *
     * @param  <T>  The type of value, which can be either {@link Integer} or {@link String}.
     * @param  x    The value to be evaluated.
     *
     * @return      The corresponding {@code JMErrorCode}, or {@code null} if the type of
     *              {@code x} is not known.
     *
     * @since  1.5.0
     * @see    Enum#valueOf(String)
     */
    public static <T extends Object> JMErrorCode valueOf(T x) {
        if (x instanceof Integer) {
            for (JMErrorCode ec : JMErrorCode.values()) {
                if ((Integer) x == ec.getErrno()) return ec;
            }
        } else if (x instanceof String) {
            return JMErrorCode.valueOf((String) x);
        }
        //* No error being thrown if got unknown type, but return `null`
        return null;
    }
}
