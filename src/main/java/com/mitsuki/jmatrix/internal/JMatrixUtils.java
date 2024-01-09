// ---------------------- //
/*      JMatrixUtils      */
// ---------------------- //

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

package com.mitsuki.jmatrix.internal;

import com.mitsuki.jmatrix.exception.JMatrixBaseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.Properties;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * This class provides all neccessary utilities for <b>JMatrix</b> library.
 *
 * @author   <a href="https://github.com/mitsuki31">Ryuu Mitsuki</a>
 * @version  1.7, 8 January 2024
 * @since    1.0.0b.1
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0">
 *           Apache License 2.0</a>
 */
public class JMatrixUtils {

    /**
     * A {@link Properties} object reference to synchronized setup properties.
     */
    private static final Properties setupProperties = SetupProperties.getSetupProperties();

    /**
     * A string variable holding the program name, retrieved from {@link SetupProperties}.
     */
    private static final String PROGNAME = setupProperties.getProperty("JM-Name");

    ///// ---------------------- /////
    ///      Class & Packages      ///
    ///// ---------------------- /////

    /**
     * Returns the package name (without class name) of specified class.
     *
     * <p>For example:</p>
     *
     * <pre><code class="language-java">&nbsp;
     *     JMatrixUtils.getPackageName(MyClass.class);
     * </code></pre>
     *
     * @param  cls  the {@link Class} object.
     *
     * @return      the {@code String} that contains package name of specified class.
     *
     * @since       1.0.0b.1
     * @see         #getClassName(Class)
     */
    public static String getPackageName(Class<?> cls) {
        return cls.getPackage().getName();
    }

    /**
     * Returns the full package name (with class name) of specified class.
     *
     * <p>For example:</p>
     *
     * <pre><code class="language-java">&nbsp;
     *     JMatrixUtils.getClassName(MyClass.class);
     * </code></pre>
     *
     * @param  cls  the {@link Class} object.
     *
     * @return      the {@code String} that contains full package name of specified class.
     *
     * @since       1.0.0b.1
     * @see         #getPackageName(Class)
     */
    public static String getClassName(Class<?> cls) {
        return cls.getName();
    }

    /**
     * Returns the {@link Class} object of the given template object.
     *
     * @param  template  the template object.
     * @param  <T>       the type of the template object.
     *
     * @return           the {@link Class} object of the given template object.
     *
     * @since            1.0.0b.1
     * @see              #getClassNameFromTemplate
     */
    public static <T> Class<?> getClassFromTemplate(T template) {
        return template.getClass();
    }

    /**
     * Returns {@code String} contains the name of the class of the given template object.
     *
     * @param  template  the template object.
     * @param  <T>       the type of the template object.
     *
     * @return           the name of the class of the template object.
     *
     * @since            1.0.0b.1
     * @see              #getClassFromTemplate
     */
    public static <T> String getClassNameFromTemplate(T template) {
        String fullNameClass = template.getClass().getName();
        int lastIndex = fullNameClass.lastIndexOf('.');

        if (lastIndex == -1) {
            // Return unextracted full name of class, if doesn't contain any dots
            return fullNameClass;
        } else {
            // Extract just the class name from the full name
            return fullNameClass.substring(lastIndex + 1);
        }
    }



    ///// --------------------- /////
    ///      Files Utilities      ///
    ///// --------------------- /////

    /**
     * Returns the total lines of specified file.
     *
     * @param  filePath     a path to the specific file.
     *
     * @return              total lines of specified file.
     *
     * @since               1.0.0b.1
     * @see                 #getLinesFile(InputStream)
     * @see                 java.io.FileReader
     * @see                 java.io.BufferedReader
     */
    public static long getLinesFile(String filePath) {
        long lines = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while (br.readLine() != null) lines++;
        } catch (final IOException ioe) {
            raiseError(new JMatrixBaseException(ioe), -1);
        }

        return lines;
    }

    /**
     * Returns the total lines of specified file.
     *
     * @param  stream       a stream that contains path to the specific file.
     *
     * @return              total lines of specified file.
     *
     * @since               1.0.0b.1
     * @see                 #getLinesFile(String)
     * @see                 java.io.InputStreamReader
     * @see                 java.io.BufferedReader
     */
    public static long getLinesFile(InputStream stream) {
        long lines = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            while (br.readLine() != null) lines++;
        } catch (final IOException ioe) {
            raiseError(new JMatrixBaseException(ioe), -1);
        }

        return lines;
    }


    /**
     * Reads and returns the list of contents from specified file.
     *
     * @param  filePath     a path to the specific file.
     *
     * @return              a {@code String} array containing the contents of specified file.
     *
     * @since               1.0.0b.1
     * @see                 #readFile(InputStream)
     * @see                 java.io.BufferedReader
     * @see                 java.io.FileReader
     */
    public static String[ ] readFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.lines().toArray(String[ ]::new);
        } catch (final IOException ioe) {
            raiseError(new JMatrixBaseException(ioe), -1);
        }

        return null;
    }

    /**
     * Reads and returns the list of contents from specified file.
     *
     * @param  stream       a stream that contains path to the specific file.
     *
     * @return              a {@code String} array containing the contents of specified file.
     *
     * @since               1.0.0b.1
     * @see                 #readFile(String)
     * @see                 java.io.BufferedReader
     * @see                 java.io.InputStreamReader
     */
    public static String[ ] readFile(InputStream stream) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            return br.lines().toArray(String[ ]::new);
        } catch (final IOException ioe) {
            raiseError(new JMatrixBaseException(ioe), -1);
        }

        return null;
    }

    /**
     * Returns an {@link InputStream} object for a file located in
     * the classpath specified by its file path.
     *
     * <p>This method is similar to {@link ClassLoader#getResourceAsStream(String)},
     * but this method provides a simple way.
     *
     * @param  filePath                a path to specific resource file.
     *
     * @return                         an {@link InputStream} of the file located at the specified file path.
     *
     * @since                          1.0.0b.1
     * @see                            java.lang.ClassLoader
     * @see                            java.io.InputStream
     */
    public static InputStream getFileAsStream(String filePath) {
        return JMatrixUtils.class.getClassLoader().getResourceAsStream(filePath);
    }

    /**
     * Writes the list of contents to the specified file.
     *
     * @param  filePath     the path to specific file.
     * @param  contents     the {@code String} array containing the contents.
     *
     * @return              {@code true} if succeed, otherwise return {@code false}
     *
     * @since               1.0.0b.1
     * @see                 java.io.FileWriter
     */
    public static boolean writeToFile(String filePath, String[ ] contents) {
        try (FileWriter fw = new FileWriter(filePath)) {
            for (String line : contents) {
                fw.write(line + System.lineSeparator());
            }

            return true;
        } catch (final IOException ioe) {
            JMatrixUtils.raiseError(new JMatrixBaseException(ioe), 0);
        }

        return false;
    }


    /**
     * Removes comment lines from an array of strings.
     *
     * <p>Default set the delimiter to <b>#</b>. If want to change the
     * delimiter, consider using {@link #removeComment(String[], String)}.
     *
     * @param  contents  the array of strings containing the content.
     *
     * @return           a new array of strings with all comment lines removed.
     *
     * @since            1.0.0b.1
     * @see              #removeComment(String[], String)
     */
    public static String[ ] removeComment(String[ ] contents) {
        return removeComment(contents, "#");
    }

    /**
     * Removes comment lines that start with a specified delimiter
     * from an array of strings.
     *
     * <p>Most used delimiter is <b>#</b>, <b>//</b>.
     *
     * @param  contents  the array of strings containing the content.
     * @param  del       the string that specifies the delimiter.
     *
     * @return           a new array of strings with all comment lines
     *                   that start with the specified delimiter removed.
     *
     * @since            1.0.0b.1
     * @see              #removeComment(String[])
     */
    public static String[ ] removeComment(String[ ] contents, String del) {
        String[ ] tmp = contents; // copy the contents
        int ct = 0;

        // Count the line of contents that contains the comment
        for (int i = 0; i < tmp.length; i++) {
            if (tmp[i].startsWith(del)) ct++;
        }

        // If the contents doesn't contains any comment
        if (ct == 0) return contents;

        // Initialize new contents
        contents = new String[contents.length - ct];
        for (int i = 0, j = 0; i < tmp.length && j < contents.length; i++) {
            if (!tmp[i].startsWith(del)) contents[j++] = tmp[i];
        }

        return contents;
    }


    ///// --------------------- /////
    ///       Date Utilities      ///
    ///// --------------------- /////

    /**
     * Converts a date and time string in ISO 8601 format to a localized string
     * using the specified format pattern.
     *
     * <p><b>Note:</b></p>
     * <p>This method is thread-safe, but does not handle null inputs gracefully.
     * If {@code date} or {@code formatPattern} is {@code null}, a
     * {@code NullPointerException} will be thrown.
     *
     * @param  date                    The date and time string in ISO 8601
     *                                 format (e.g., "2024-01-08T21:53:00Z").
     * @param  formatPattern           The format pattern to use for the
     *                                 localized date and time string
     *                                 (e.g., "dd/MM/yyyy HH:mm:ss").
     * @return                         The localized date and time string in
     *                                 the specified format.
     *
     * @throws DateTimeParseException  If the input {@code date} string cannot be parsed
     *                                 using the ISO 8601 formatter.
     * @throws NullPointerException    If {@code null} are known on {@code date} or
     *                                 {@code formatPattern} argument.
     *
     * @since 1.5.0
     * @see   #dateISOToLocal(String)
     */
    public static String dateISOToLocal(String date, String formatPattern) {
        LocalDateTime dateTime = LocalDateTime.parse(date, DateTimeFormatter.ISO_DATE_TIME);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern);
        return dateTime.format(formatter);
    }

    /**
     * Converts a date and time string in ISO 8601 format to a localized string
     * using the {@code yyyy-MM-dd HH:mm:ss} format pattern.
     *
     * <p>For customizing the format pattern, use the
     * {@link #dateISOToLocal(String, String)} method instead. The format pattern
     * will use format {@code yyyy-MM-dd HH:mm:ss}.
     *
     * <p><b>Note:</b></p>
     * <p>This method is thread-safe, but does not handle null inputs gracefully.
     * If {@code date} is {@code null}, a {@code NullPointerException} will be thrown.
     *
     * @param  date                    The date and time string in ISO 8601
     *                                 format (e.g., "2024-01-08T21:53:00Z").
     * @return                         The localized date and time string in
     *                                 the {@code yyyy-MM-dd HH:mm:ss} format.
     *
     * @throws DateTimeParseException  If the input {@code date} string cannot be parsed
     *                                 using the ISO 8601 formatter.
     * @throws NullPointerException    If {@code null} are known on {@code date} argument.
     *
     * @since 1.5.0
     */
    public static String dateISOToLocal(String date) {
        return dateISOToLocal(date, "yyyy-MM-dd HH:mm:ss");
    }


    ///// --------------------- /////
    ///       Error Options       ///
    ///// --------------------- /////

    /**
     * Raises an error or exception and prints the stack traces of the
     * input {@link Throwable} object and then exits the program (default exit code: 1).
     *
     * @param cause  the {@link Throwable} object.
     *
     * @since        1.0.0b.1
     * @see          #raiseError(Throwable, int)
     * @see          #raiseErrorMsg(Throwable)
     * @see          java.lang.Throwable
     * @see          java.lang.Throwable#printStackTrace()
     */
    final public static void raiseError(final Throwable cause) {
        cause.printStackTrace();
        System.err.println(System.lineSeparator() +
            "Exited with error code: 1");
        System.exit(1);
    }

    /**
     * Raises an error or exception and prints the stack traces of the
     * input {@link Throwable} object and then exits the program with the specified exit code.
     *
     * <p>Set the {@code errno} to {@code 0} (zero) if want the program to continue runs
     * even the exception traces has been printed.
     *
     * @param cause  the {@link Throwable} object.
     * @param errno  the value that specifies the exit code,
     *               zero for continue running after prints the exception.
     *
     * @since        1.0.0b.1
     * @see          #raiseError(Throwable)
     * @see          #raiseErrorMsg(Throwable, int)
     * @see          java.lang.Throwable
     * @see          java.lang.Throwable#printStackTrace()
     */
    final public static void raiseError(final Throwable cause, final int errno) {
        cause.printStackTrace();
        if (errno != 0) {
            System.err.printf(System.lineSeparator() +
                "Exited with error code: %d%s", errno, System.lineSeparator());
            System.exit(errno);
        }
    }


    /**
     * Raises an error or exception but only prints its message
     * and then exits the program (default exit code: 1).
     *
     * <p>This method only prints the message of given {@link Throwable}
     * object, if want the traces also get printed use {@link #raiseError(Throwable)} instead.
     *
     * @param cause  the {@link Throwable} object.
     *
     * @since        1.0.0b.1
     * @see          #raiseErrorMsg(Throwable, int)
     * @see          #raiseError(Throwable)
     * @see          java.lang.Throwable
     * @see          java.lang.Throwable#printStackTrace()
     */
    final public static void raiseErrorMsg(final Throwable cause) {
        System.err.printf("[%s] Error: %s%s", PROGNAME, cause.getMessage(), System.lineSeparator());
        System.err.println(System.lineSeparator() +
            "Exited with error code: 1");
        System.exit(1);
    }

    /**
     * Raises an error or exception but only prints its message
     * and then exits the program with the specified exit code.
     *
     * <p>Set the {@code errno} to {@code 0} (zero) if want the program to continue runs
     * even the exception traces has been printed.
     *
     * <p>This method only prints the message of given {@link Throwable}
     * object, if want the traces also get printed use {@link #raiseError(Throwable, int)} instead.
     *
     * @param cause  the {@link Throwable} object.
     * @param errno  the value that specifies the exit code,
     *               zero for continue running after prints the exception.
     *
     * @since        1.0.0b.1
     * @see          #raiseErrorMsg(Throwable)
     * @see          #raiseError(Throwable, int)
     * @see          java.lang.Throwable
     * @see          java.lang.Throwable#printStackTrace()
     */
    final public static void raiseErrorMsg(final Throwable cause, final int errno) {
        System.err.printf("[%s] Error: %s%s", PROGNAME, cause.getMessage(), System.lineSeparator());
        if (errno != 0) {
            System.err.printf(System.lineSeparator() +
                "Exited with error code: %d%s", errno, System.lineSeparator());
            System.exit(errno);
        }
    }
}
