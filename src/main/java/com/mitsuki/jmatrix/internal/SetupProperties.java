// --------------------- //
/*    SetupProperties    */
// --------------------- //

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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.net.URL;

/**
 * This class provides access to get the setup properties and it is designed
 * to be synchronized and thread-safe.
 *
 * <p>The setup properties are retrieved from the file statically using
 * the {@code static} block, which means that when the program starts it will
 * immediately retrieve the properties from the file.
 *
 * @author   <a href="https://github.com/mitsuki31">Ryuu Mitsuki</a>
 * @version  1.1, 12 December 2023
 * @since    1.5.0
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0">
 *           Apache License 2.0</a>
 */
final class SetupProperties {

    /**
     * A string represents a file path to the setup properties stored in.
     */
    private static final String setupFile = "configuration/setup.properties";

    /**
     * A {@code Properties} object that stores all properties data
     * from setup properties file.
     */
    private static Properties setupProperties;

    /* Immediately search and check the setup properties file, then retrieve
     * all properties data from it. Throw IOException if I/O errors occurred.
     */
    static {
        // Although the Properties class has been designed to be synchronized,
        // the code below further ensures that it is implicitly synchronized
        if (setupProperties == null) {
            try {
                URL urlSetupFile = SetupProperties.class
                    .getClassLoader()
                    .getResource(setupFile);

                // Return an error if the resource file cannot be found
                if (urlSetupFile == null) {
                    throw new FileNotFoundException(String.format(
                        "ClassLoader cannot found '%s' file", setupFile
                    ));
                }

                // Convert the URL to InputStream
                InputStream inStream = urlSetupFile.openStream();

                // Load the properties file
                setupProperties = new Properties();
                setupProperties.load(inStream);
            } catch (final IOException ioe) {
                throw new ExceptionInInitializerError(ioe);
            }
        }
    }

    /**
     * Gets the synchronized setup properties from this class.
     *
     * @return A synchronized instance of {@link Properties} from this
     *         class containing all setup properties data.
     *
     * @since  1.5.0
     */
    static Properties getSetupProperties() {
        return setupProperties;
    }
}
