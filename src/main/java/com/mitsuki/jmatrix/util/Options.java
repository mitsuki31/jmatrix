// ------------------- //
/*       Options       */
// ------------------- //

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

package com.mitsuki.jmatrix.util;

import com.mitsuki.jmatrix.exception.JMatrixBaseException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.List;
import java.util.Arrays;

/**
 * This class provides all requirements for <b>JMatrix</b> library.
 *
 * @author   <a href="https://github.com/mitsuki31" target="_blank">
 *           Ryuu Mitsuki</a>
 * @version  1.3, 27 June 2023
 * @since    1.0.0b.1
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *           Apache License 2.0</a>
 *
 * @see      com.mitsuki.jmatrix.util.XMLParser
 */
public class Options
{

    /**
     * {@code Enum} that contains all available options.
     *
     * @since 1.0.0b.1
     * @see   #getOptions(String)
     */
    public enum OPT {
        VERSION("-V", "version", "ver"),
        HELP("-h", "help"),
        COPYRIGHT("-cr", "copyright");

        private final List<String> aliases;

        OPT(String ... aliases) {
            this.aliases = Arrays.asList(aliases);
        }
    }

    // -- Private Attributes
    private static XMLParser XML = new XMLParser(XMLParser.XMLType.CONFIG);
    private static String PROGNAME = XML.getProperty("programName").toLowerCase();
    private static String PACKAGE = getPackageName(Options.class);
    private static String THISCLASS = getClassName(Options.class);
    private static String contentsPath = "contents/";

    /**
     * Method that checks the input argument then returns specific option.
     *
     * @param  inputOpt                  the {@code String} that wants to be checked.
     *
     * @return                           the corresponding {@code OPT} value.
     *
     * @throws IllegalArgumentException  if the given option name does not
     *                                   match with any known options.
     *                                   This exception will be thrown as the causing exception.
     *
     * @since                            1.0.0b.1
     */
    public static OPT getOptions(String inputOpt) {
        for (OPT opt : OPT.values()) {
            if (opt.aliases.contains(inputOpt)) {
                return opt;
            }
        }

        raiseError(new JMatrixBaseException(
            new IllegalArgumentException(
                String.format("Unknown argument option for input \"%s\"", inputOpt)
            )
        ), 0);

        System.err.println(System.lineSeparator() + getHelpMsg()[0]);
        System.err.println("    " +
            "java -jar <jar_file> [-h|-V|-cr]");
        System.exit(-1);

        // This never get executed, but to suppress missing return statement error
        return null;
    }


    ///// ---------------------- /////
    ///      Class & Packages      ///
    ///// ---------------------- /////

    /**
     * Returns the package name (without class name) of specified class.
     *
     * <p>For example:</p>
     *
     * <pre><code class="language-java">&nbsp;
     *     Options.getPackageName(MyClass.class);
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
     *     Options.getClassName(MyClass.class);
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



    ///// -------------------- /////
    ///       Files Options      ///
    ///// -------------------- /////

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
        return Options.class.getClassLoader().getResourceAsStream(filePath);
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
            Options.raiseError(new JMatrixBaseException(ioe), 0);
        }

        return false;
    }


    ///// ------------------ /////
    ///        Contents        ///
    ///// ------------------ /////

    /**
     * Returns a list containing the help message contents.
     *
     * @return the contents of help message.
     *
     * @since  1.0.0b.1
     * @see    #readFile(InputStream)
     * @see    #getFileAsStream(String)
     * @see    #removeComment(String[])
     */
    public static String[ ] getHelpMsg() {
        return removeComment(readFile(getFileAsStream(contentsPath + "help.content")));
    }

    /**
     * Returns a list containing the copyright contents.
     *
     * @return the contents of copyright.
     *
     * @since  1.0.0b.1
     * @see    #readFile(InputStream)
     * @see    #getFileAsStream(String)
     * @see    java.lang.StringBuilder
     */
    public static String[ ] getCopyright() {
        final String[ ] contents = readFile(getFileAsStream(contentsPath + "additional.content"));
        String[ ] copyright = new String[contents.length];

        for (int i = 0; i < contents.length; i++) {
            // Ignore the comment
            if (!contents[i].startsWith("#")) {
                StringBuilder sb = new StringBuilder();
                for (int j = 0; j < contents[i].length(); j += 2) {
                    String bytes = contents[i].substring(j, j + 2);
                    sb.append((char) Integer.parseInt(bytes, 16));
                }
                copyright[i] = sb.toString();
            } else {
                copyright[i] = contents[i];
            }
        }

        for (int i = 0, j = 0; i != contents.length; i++) {
            if (copyright[i].contains("${PACKAGE_NAME}")) {
                copyright[i] = copyright[i].replace("${PACKAGE_NAME}", XML.getProperty("programName"));
            }

            if (copyright[i].contains("${AUTHOR}")) {
                copyright[i] = copyright[i].replace("${AUTHOR}", XML.getProperty("author"));
            }
        }

        return removeComment(copyright);
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
