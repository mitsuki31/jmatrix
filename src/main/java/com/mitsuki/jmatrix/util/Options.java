// ------------------- //
/*       Options       */
// ------------------- //

// -**- Package -**- //
package com.mitsuki.jmatrix.util;

// -**- Built-in Package -**- //
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.util.List;
import java.util.Arrays;

import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;

import java.math.BigDecimal;
import java.math.BigInteger;

// -**- Local Package -**- //
import com.mitsuki.jmatrix.core.JMBaseException;
import com.mitsuki.jmatrix.util.OSUtils;
import com.mitsuki.jmatrix.util.XMLParser;

/**
* This class provides all requirements for {@code JMatrix} package.<br>
*
* @version 1.0
* @since   1.0.0
* @author  Ryuu Mitsuki
*
* @see     com.mitsuki.jmatrix.util.OSUtils
* @see     com.mitsuki.jmatrix.util.XMLParser
*/
public class Options
{
    /**
    * {@code Enum} contains all available options.<br>
    *
    * @since 1.0.0
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
    private static String contentsPath = String.format("contents%s", OSUtils.sep);

    /**
    * Method that checks the input argument then returns specific opt>
    *
    * @param  inputOpt  the {@code String} that wants to be checked.
    *
    * @return the corresponding {@code OPT} value.
    *
    * @since  1.0.0
    * @see    Options#OPT
    */
    public static OPT getOptions(String inputOpt) {
        for (OPT opt : OPT.values()) {
            if (opt.aliases.contains(inputOpt)) {
                return opt;
            }
        }

        try {
            throw new IllegalArgumentException(String.format("Unknown>
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException ex) {
                raiseError(ex, 0);
                System.err.println(System.lineSeparator() + getHelpMs>
                System.err.println("    " +
                    "java -jar <jar_file> [-h|-V|-cr]");
                System.exit(-1);
            }
        }

        return null;
    }


    ///// ---------------------- /////
    ///      Class & Packages      ///
    ///// ---------------------- /////

    /**
    * Gets the package name of specific class. For example:
    * <br>
    * <code>
    *     Options.getPackageName(MyClass.class);
    * </code>
    * <br>
    *
    * @param  cls  a class name to retrieves it's package name.
    *
    * @return the {@link String} that contains package name of specified class.
    *
    * @since  1.0.0
    * @see    #getClassName(Class<?>)
    */
    public static String getPackageName(Class<?> cls) {
        return cls.getPackage().getName();
    }

    /**
    * Gets the full package name (with class name) of specific class. For example:
    * <br>
    * <code>
    *     Options.getClassName(MyClass.class);
    * </code>
    * <br>
    *
    * @param  cls  a class name to retrieves it's full package name.
    *
    * @return a {@link String} that contains full package name of specified class.
    *
    * @since  1.0.0
    * @see    #getPackageName(Class<?>)
    */
    public static String getClassName(Class<?> cls) {
        return cls.getName();
    }

    /**
    * Returns the class of the given template object.<br>
    *
    * @param  template  the template object to retrieve the class.
    * @param  <T>       the type of the template object.
    *
    * @return the class of the template object.
    *
    * @since  1.0.0
    * @see    #getClassNameFromTemplate(T)
    */
    public static <T> Class<?> getClassFromTemplate(T template) {
        return template.getClass();
    }

    /**
    * Returns {@code String} contains the name of the class of the given template object.<br>
    *
    * @param  template  the template object to retrieves the class name from.
    * @param  <T>       the type of the template object.
    *
    * @return the name of the class of the template object.
    *
    * @since  1.0.0
    * @see    #getClassFromTemplate(T)
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
    * Returns the total lines of specified file.<br>
    *
    * @param  filePath  a path to specific file.
    *
    * @return total lines of specified file.
    *
    * @since  1.0.0
    * @see    #getLinesFile(InputStream)
    * @see    java.io.FileReader
    * @see    java.io.BufferedReader
    */
    public static long getLinesFile(String filePath) {
        long lines = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            while (br.readLine() != null) lines++;
        } catch (final IOException e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException ex) {
                raiseError(ex, -1);
            }
        }

        return lines;
    }

    /**
    * Returns the total lines of specified file.<br>
    *
    * @param  stream  a stream that contains path to specific file.
    *
    * @return total lines of specified file.
    *
    * @since  1.0.0
    * @see    #getLinesFile(String)
    * @see    java.io.InputStreamReader
    * @see    java.io.BufferedReader
    */
    public static long getLinesFile(InputStream stream) {
        long lines = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            while (br.readLine() != null) lines++;
        } catch (final IOException e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException ex) {
                raiseError(ex, -1);
            }
        }

        return lines;
    }


    /**
    * Reads and returns the contents of specified file.<br>
    *
    * @param  filePath a path to specific file.
    *
    * @return the contents of specified file.
    *
    * @since  1.0.0
    * @see    #readFile(InputStream)
    * @see    java.io.BufferedReader
    * @see    java.io.FileReader
    */
    public static String[ ] readFile(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            return br.lines().toArray(String[ ]::new);
        } catch (final IOException e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException ex) {
                raiseError(ex, -1);
            }
        }

        return null;
    }

    /**
    * Reads and returns the contents of specified file.<br>
    *
    * @param  stream  a stream that contains path to specific file.
    *
    * @return the contents of specified file.
    *
    * @since  1.0.0
    * @see    #readFile(String)
    * @see    java.io.BufferedReader
    * @see    java.io.InputStreamReader
    */
    public static String[ ] readFile(InputStream stream) {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(stream))) {
            return br.lines().toArray(String[ ]::new);
        } catch (final IOException e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException ex) {
                raiseError(ex, -1);
            }
        }

        return null;
    }

    /**
    * Returns the stream of specified file path.<br>
    * This method is similar to {@link java.lang.ClassLoader#getResourceAsStream(String)}, but this method is a simple way.<br>
    *
    * @param  filePath  a path to specific file.
    *
    * @return returns the {@link java.io.InputStream}
    *
    * @since  1.0.0
    * @see    java.lang.ClassLoader
    * @see    java.lang.InputStream
    */
    public static InputStream getFileAsStream(String filePath) {
        InputStream stream = null;
        try {
            ClassLoader cl = Options.class.getClassLoader();
            stream = cl.getResourceAsStream(filePath);
        } catch (final NullPointerException nme) {
            try {
                throw new JMBaseException(nme);
            } catch (final JMBaseException jme) {
                raiseError(jme, -1);
            }
        }

        return stream;
    }

    /**
    * Writes the contents to specific file.<br>
    *
    * @param  filePath  the {@link String} that contains path to specific file.
    * @param  contents  the contents to be writen into specific file.
    *
    * @return returns {@code true} if succeed, else return {@code false}
    *
    * @since  1.0.0
    * @see    java.io.FileWriter
    */
    public static boolean writeToFile(String filePath, String[ ] contents) {
        try (FileWriter fw = new FileWriter(filePath)) {
            for (String line : contents) {
                fw.write(line + System.lineSeparator());
            }
            return true;
        } catch (final IOException e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException ex) {
                raiseError(ex, -1);
            }
        }

        return false;
    }


    ///// ------------------ /////
    ///        Contents        ///
    ///// ------------------ /////

    /**
    * Returns the help message contents.<br>
    *
    * @return the contents of help message.
    *
    * @since  1.0.0
    * @see    #readFile
    * @see    #getFileAsStream(String)
    * @see    #removeComment(String[])
    */
    public static String[ ] getHelpMsg() {
        return removeComment(readFile(getFileAsStream(contentsPath + "help.content")));
    }

    /**
    * Returns the copyright contents.<br>
    *
    * @return the contents of copyright.
    *
    * @since  1.0.0
    * @see    #readFile
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
    * Removes comment lines from an array of strings.<br>
    * Default set the delimiter to <b>#</b>.<br>
    *
    * @param  contents  the array of strings containing the content.
    *
    * @return a new array of strings with all comment lines removed.
    *
    * @since  1.0.0
    * @see    #removeComment(String[], String)
    */
    public static String[ ] removeComment(String[ ] contents) {
        return removeComment(contents, "#");
    }

    /**
    * Removes comment lines that start with a specified delimiter
    * from an array of strings.<br>
    * Most used delimiter is (<b>#</b>, <b>//</b>).<br>
    *
    * @param  contents  the array of strings containing the content.
    * @param  del       the string that specifies the delimiter.
    *
    * @return a new array of strings with all comment lines
    *         that start with the specified delimiter removed.
    *
    * @since  1.0.0
    * @see    #removeComment(String[])
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
    * Raises an error or exception and prints the stack trace of the
    * input exception object and then exits the program (default exit code: 1).<br>
    *
    * @param ex  the {@code Exception} object to be printed.
    *
    * @since 1.0.0
    * @see   #raiseError(Exception, int)
    * @see   #raiseErrorMsg(Exception)
    * @see   java.lang.Exception
    * @see   java.lang.Exception#printStackTrace
    */
    public final static void raiseError(final Exception ex) {
        ex.printStackTrace();
        System.err.println(System.lineSeparator() +
            "Exited with error code: 1");
        System.exit(1);
    }

    /**
    * Raises an error or exception and prints the stack trace of the
    * input exception object and then exits the program with the specified exit code.<br>
    * Sets the {@code exitStatus} to {@code 0} (zero) if don't want the program to exits.<br>
    *
    * @param ex          the {@code Exception} object to be printed.
    * @param exitStatus  the value that specifies the exit code.
    *
    * @since 1.0.0
    * @see   #raiseError(Exception)
    * @see   #raiseErrorMsg(Exception, int)
    * @see   java.lang.Exception
    * @see   java.lang.Exception#printStackTrace
    */
    public final static void raiseError(final Exception ex, final int exitStatus) {
        ex.printStackTrace();
        if (exitStatus != 0) {
            System.err.printf(System.lineSeparator() +
                "Exited with error code: %d%s", exitStatus, System.lineSeparator());
            System.exit(exitStatus);
        }
    }


    /**
    * Raises an error or exception but only prints the message
    * and then exits the program (default exit code: 1).<br>
    *
    * @param ex  the {@code Exception} object to prints the message.
    *
    * @since 1.0.0
    * @see   #raiseErrorMsg(Exception, int)
    * @see   #raiseError(Exception)
    * @see   java.lang.Exception
    * @see   java.lang.Exception#printStackTrace
    */
    public final static void raiseErrorMsg(final Exception ex) {
        System.err.printf("[%s] Error: %s%s", PROGNAME, ex.getMessage(), System.lineSeparator());
        System.err.println(System.lineSeparator() +
            "Exited with error code: 1");
        System.exit(1);
    }

    /**
    * Raises an error or exception but only prints the message
    * and then exits the program with the specified exit code.<br>
    * Sets the {@code exitStatus} to {@code 0} (zero) if don't want the program to exits.<br>
    *
    * @param ex          the {@code Exception} object to prints the message.
    * @param exitStatus  the value that specifies the exit code.
    *
    * @since 1.0.0
    * @see   #raiseErrorMsg(Exception)
    * @see   #raiseError(Exception, int)
    * @see   java.lang.Exception
    * @see   java.lang.Exception#printStackTrace
    */
    public final static void raiseErrorMsg(final Exception ex, final int exitStatus) {
        System.err.printf("[%s] Error: %s%s", PROGNAME, ex.getMessage(), System.lineSeparator());
        if (exitStatus != 0) {
            System.err.printf(System.lineSeparator() +
                "Exited with error code: %d%s", exitStatus, System.lineSeparator());
            System.exit(exitStatus);
        }
    }
}
