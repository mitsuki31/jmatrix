// ------------------- //
/*       OSUtils       */
// ------------------- //

package com.mitsuki.jmatrix.util;

import java.io.File;

public class OSUtils
{
    public enum OS {
        WINDOWS, LINUX, MAC, SOLARIS, OTHER
    };

    public final static String sep = File.separator; // get path separator
    private static OS os = null;

    public static OS getOSName() {
        if (os == null) {
            final String osName = System.getProperty("os.name").toLowerCase();

            if (osName.contains("win")) {
                os = OS.WINDOWS;
            }
            else if (osName.contains("nix") || osName.contains("nux") ||
                    osName.contains("aix")) {
                os = OS.LINUX;
            }
            else if (osName.contains("mac")) {
                os = OS.MAC;
            }
            else if (osName.contains("sunos")) {
                os = OS.SOLARIS;
            }
            else {
                os = OS.OTHER; // unknown operating system
            }
        }

        return os;
    }
}
