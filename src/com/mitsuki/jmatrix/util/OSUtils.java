// ------------------- //
/*       OSUtils       */
// ------------------- //

// -**- Package -**- //
package com.mitsuki.jmatrix.util;

// -**- Built-in Package -**- //
import java.io.File;

/**
* This class provides operating system utilities<br>
*
* @version 1.0
* @since   1.0.0
* @author  Ryuu Mitsuki
*
* @see     com.mitsuki.jmatrix.util.Options
* @see     com.mitsuki.jmatrix.util.XMLParser
*/
public class OSUtils
{
    /**
    * List name of operating system.<br>
    * Uses {@link OSUtils#getOSName} to gets current operating system.<br>
    *
    * @since 1.0.0
    * @see   OSUtils#getOSName
    */
    public enum OS {
        WINDOWS, LINUX, MAC, SOLARIS, OTHER
    };

    private static OS os = null;

    /**
    * Gets path separator determined by operating system.<br>
    *
    * @since 1.0.0
    * @see   File#separator
    */
    public final static String sep = File.separator;

    /**
    * Method that gets current operating system name.<br>
    * If operating system name not available on {@link #OS}, then it will returns {@code OTHER} value.<br>
    *
    * @return the name of current operating system
    *
    * @since  1.0.0
    * @see    System#getProperty
    * @see    OSUtils#OS
    */
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
