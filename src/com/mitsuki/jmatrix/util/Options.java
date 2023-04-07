// ------------------- //
/*       Options       */
// ------------------- //

// -**- Package -**- //
package com.mitsuki.jmatrix.util;

// -**- Built-in Package (java.io) -**- //
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileNotFoundException;

// -**- Built-in Package (java.lang) -**- //
import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;

// -**- Local Package -**- //
import com.mitsuki.jmatrix.core.JMBaseException;
import com.mitsuki.jmatrix.util.OSUtils;
import com.mitsuki.jmatrix.util.XMLParser;

public class Options
{
    public enum OPT {
        VERSION, HELP, COPYRIGHT
    };

    private static XMLParser XML = XMLParser.newParser(XMLParser.XMLType.CONFIG);
    private static String PROGNAME = XML.getProperty("programName").toLowerCase();
    private static String PACKAGE = getPackageName(Options.class);
    private static String THISCLASS = getClassName(Options.class);

    private static String contentsPath = String.format("assets%scontents%s", OSUtils.sep, OSUtils.sep);

    public static OPT getOptions(String inputOpt) {
        if (inputOpt != null) {
            if (inputOpt.equals("-V") || inputOpt.equals("version") || inputOpt.equals("ver")) {
                return OPT.VERSION;
            }
            else if (inputOpt.equals("-h") || inputOpt.equals("help")) {
                return OPT.HELP;
            }
            else if (inputOpt.equals("-cr") || inputOpt.equals("copyright")) {
                return OPT.COPYRIGHT;
            }
            else {
                try {
                    throw new IllegalArgumentException(String.format("Unknown argument option for input \"%s\"", inputOpt));
                } catch (final Exception e) {
                    try {
                        throw new JMBaseException(e);
                    } catch (final JMBaseException ex) {
                        raiseError(ex, 0);
                        System.err.println(System.lineSeparator() + getHelpMsg()[0]);
                        System.err.println("    " +
                            "java -jar <jar_file> [-h|-V|-cr]");
                        System.exit(-1);
                    }
                }
            }
        }

        return null;
    }


    ///// -------------- /////
    ///      Packages      ///
    ///// -------------- /////

    public static String getPackageName(Class<?> cls) {
        return cls.getPackage().getName();
    }

    public static String getClassName(Class<?> cls) {
        return cls.getName();
    }


    ///// -------------------- /////
    ///       Files Options      ///
    ///// -------------------- /////

    public static long getLinesFile(String pathFile) {
        long lines = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(pathFile))) {
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

    public static String[ ] readFile(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
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

    public static boolean writeToFile(String filePath, String[ ] content) {
        try (FileWriter fw = new FileWriter(filePath)) {
            for (String line : content) {
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

    public static String[ ] getHelpMsg() {
        return readFile(getFileAsStream(contentsPath + "help.content"));
    }

    public static String[ ] getCopyright() {
        final String license = readFile(getFileAsStream("LICENSE"))[0];
        final String[ ] contents = readFile(getFileAsStream(contentsPath + "additional.content"));
        String[ ] copyright = new String[contents.length];
        String[ ] otherContents = new String[ ]
            {
                XML.getProperty("programName"),
                license
            };

        for (int i = 0; i < contents.length; i++) {
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < contents[i].length(); j += 2) {
                String bytes = contents[i].substring(j, j + 2);
                sb.append((char) Integer.parseInt(bytes, 16));
            }
            copyright[i] = sb.toString();
        }

        for (int i = 0, j = 0; j != otherContents.length; i++) {
            if (copyright[i].contains("%s")) {
                copyright[i] = String.format(copyright[i], otherContents[j++]);
            }
        }

        return copyright;
    }


    ///// --------------------- /////
    ///       Error Options       ///
    ///// --------------------- /////

    public final static void raiseError(final Exception ex) {
        ex.printStackTrace();
        System.err.println("\nExited with error code: 1");
        System.exit(1);
    }

    public final static void raiseError(final Exception ex, final int exitStatus) {
        ex.printStackTrace();
        if (exitStatus != 0) {
            System.err.printf("\nExited with error code: %d\n", exitStatus);
            System.exit(exitStatus);
        }
    }


    // Raise and only print error message

    public final static void raiseErrorMsg(final Exception ex) {
        System.err.printf("[%s] Error: %s\n", PROGNAME, ex.getMessage());
        System.err.println("\nExited with error code: 1");
        System.exit(1);
    }

    public final static void raiseErrorMsg(final Exception ex, final int exitStatus) {
        System.err.printf("[%s] Error: %s\n", PROGNAME, ex.getMessage());
        if (exitStatus != 0) {
            System.err.printf("\nExited with error code: %d\n", exitStatus);
            System.exit(exitStatus);
        }
    }
}
