package com.mitsuki.jmatrix;

import com.mitsuki.jmatrix.util.Options;
import com.mitsuki.jmatrix.util.XMLParser;

/**
 * Main class for <b>JMatrix</b> library that provides some information such as <b>JMatrix</b> version.
 *
 * <p>This class does not provides API for build the matrix,
 * it's just <i>useless<i> class if get imported.
 *
 * <p><b>Usage:</b></p>
 *
 * <pre>&nbsp;
 *   java -jar path/to/jmatrix.jar [-h|-V|-cr]
 * </pre>
 *
 * @author   <a href="https://github.com/mitsuki31" target="_blank">
 *           Ryuu Mitsuki</a>
 * @version  1.2, 20 June 2023
 * @since    1.0.0
 * @see      com.mitsuki.jmatrix.Matrix
 */
public class Main
{
    private static XMLParser XML = new XMLParser(XMLParser.XMLType.CONFIG);
    private static String programVersion = XML.getProperty("programName") + " " + getVersion();

    public static void main(String[ ] args) {
        String arg1 = null;

        if (args.length >= 1 && args.length < 2) {
            arg1 = args[0];
        } else if (args.length > 1) {
            try {
                throw new IllegalArgumentException("Too much arguments");
            } catch (final IllegalArgumentException iae) {
                Options.raiseErrorMsg(iae, 0);
                System.out.println(Options.getHelpMsg()[0]);
                System.out.println("    " +
                    "java -jar <jar_file> [-h|-V|-cr]");
                return;
            }
        }

        if (arg1 == null) {
            try {
                throw new NullPointerException("Null argument");
            } catch (final Exception e) {
                String helps[ ] = Options.getHelpMsg();
                Options.raiseErrorMsg(e, 0);

                for (int i = 0; i < helps.length; i++) {
                    if (i >= 7) break;
                    System.out.println(helps[i]);
                }
                return;
            }
        }

        switch (Options.getOptions(arg1)) {
            case VERSION:
                String pkgName = String.format(" <%s>", XML.getProperty("packageName"));

                System.out.println(programVersion + pkgName);
                break;

            case HELP:
                String lines = System.lineSeparator();
                for (byte i = 0; i < (byte) programVersion.length(); i++) lines += "-";

                System.out.println(programVersion + lines + System.lineSeparator());
                for (String s : Options.getHelpMsg()) System.out.println(s);
                break;

            case COPYRIGHT:
                for (String s : Options.getCopyright()) System.out.println(s);
                break;
        }
    }

    private static String getVersion() {
        String ver = XML.getProperty("version");

        // Only retrieve the release type if not release and stable release
        if ( !(XML.getProperty("releaseType").equals("release") ||
                XML.getProperty("releaseType").equals("stable")) ) {
            ver += "-" + XML.getProperty("releaseType");

            // This will only retrieve a non-zero beta number
            if (!XML.getProperty("betaNum").equals("0")) {
                ver += "." + XML.getProperty("betaNum");
            }
        }

        return ver;
    }
}
