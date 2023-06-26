// ------------------- //
/*       OSUtils       */
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

import java.io.File;

/**
 * This class provides operating system utilities.
 *
 * @author   <a href="https://github.com/mitsuki31" target="_blank">
 *           Ryuu Mitsuki</a>
 * @version  1.1, 26 June 2023
 * @since    1.0.0b.1
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *           Apache License 2.0</a>
 * @see      com.mitsuki.jmatrix.util.Options
 * @see      com.mitsuki.jmatrix.util.XMLParser
 */
public class OSUtils
{
    /**
     * List names of operating system.
     *
     * <p>Use {@link #getOSName()} to retrieve the name of current operating system.
     *
     * @since 1.0.0b.1
     * @see   #getOSName()
     */
    public enum OS {
        WINDOWS, LINUX, MAC, SOLARIS, OTHER
    };

    private static OS os = null;

    /**
     * Returns path separator based on operating system.
     *
     * @since 1.0.0b.1
     * @see   File#separator
     */
    public final static String sep = File.separator;

    /**
     * Retrieves and returns the name of current operating system.
     *
     * <p>If the operating system name unknown, then it will returns {@code OTHER}.
     *
     * @return the name of current operating system
     *
     * @since  1.0.0b.1
     * @see    System#getProperty(String)
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
