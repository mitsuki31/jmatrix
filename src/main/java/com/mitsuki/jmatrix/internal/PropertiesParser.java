// ---------------------- //
/*    PropertiesParser    */
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.IOException;
import java.util.Properties;


/**
 * A class that provides utilities (methods) to parse specific properties file.
 * Do not use the constructor to create the instance of this class, use
 * the {@link PropertiesParser#load(String)} method instead.
 *
 * <p><b>For example:</b>
 *
 * <pre><code class="language-java">&nbsp;
 *   PropertiesParser parser = PropertiesParser.load("example.properties");
 *   Properties properties = parser.getProperties();
 * </code></pre>
 *
 * <p>Or with one variable:
 *
 * <pre><code class="language-java">&nbsp;
 *   Properties properties = PropertiesParser.load("example.properties")
 *                                           .getProperties();
 * </code></pre>
 *
 * @author   <a href="https://github.com/mitsuki31">Ryuu Mitsuki</a>
 * @version  1.0, 16 September 2023
 * @since    1.5.0
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0">
 *           Apache License 2.0</a>
 */
public final class PropertiesParser {

    /**
     * A path to the specified property file (initialized inside constructor).
     */
    private String propertyFile;

    /**
     * Stores the properties data from the specified properties file.
     */
    private Properties properties;

    /**
     * A sole constructor. Users should never use this constructor to create a new
     * instance of this class.
     *
     * @param  inStream      The (@link InputStream} object to be loaded by
     *                       {@link Properties}.
     * @param  propertyPath  The string path to the property file.
     *
     * @throws IOException
     *         If an I/O error occurs while loading the properties.
     *
     * @since                1.5.0
     * @see                  #load(String)
     */
    private PropertiesParser(InputStream inStream,
                             String propertyPath) throws IOException {
        this.propertyFile = propertyPath;
        this.properties = new Properties();
        this.properties.load(inStream);
    }

    /**
     * Loads and retrieves the properties from the specified file path.
     *
     * @param  file  The file path reference to a property file.
     *
     * @return       A new instance of this class with parsed properties.
     *               To get the properties, users can use {@link #getProperties()}.
     *
     * @throws IOException
     *         If an I/O error occurs while loading the properties.
     *
     * @throws NullPointerException
     *         If the given file path is {@code null}.
     *
     * @since        1.5.0
     */
    public static PropertiesParser load(String file) throws IOException {
        if (file == null) {
            throw new NullPointerException("The file path cannot be null");
        }

        // Get the property using InputStream
        InputStream propertyStream = JMatrixUtils.getFileAsStream(file);
        if (propertyStream == null) {
            throw new IOException(String.format("'%s' not found", file));
        }

        return new PropertiesParser(propertyStream, file);
    }

    /**
     * Gets the instance of {@link Properties} class with parsed properties
     * from this class.
     *
     * @return The instance of {@link Properties} with parsed properties.
     *
     * @since  1.5.0
     */
    public Properties getProperties() {
        return this.properties;
    }
}


/**
 * This class provides access to get the setup properties and it is designed
 * to be synchronized and thread-safe.
 *
 * <p>The setup properties are retrieved from the file statically using
 * the {@code static} block, which means that when the program starts it will
 * immediately retrieve the properties from the file.
 *
 * @author   <a href="https://github.com/mitsuki31">Ryuu Mitsuki</a>
 * @version  1.0, 16 September 2023
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
     * all properties data from it. Before retrieving the properties,
     * it will checks whether the properties file is exist and readable.
     */
    static {
        // Although the Properties class has been designed to be synchronized,
        // the code below further ensures that it is implicitly synchronized
        if (setupProperties == null) {
            try (InputStream inStream = SetupProperties.class
                    .getClassLoader()
                    .getResourceAsStream(setupFile)) {
                setupProperties = new Properties();
                setupProperties.load(inStream);

                if (setupProperties == null) {
                    throw new FileNotFoundException(String.format(
                        "InputStream cannot found '%s' file", setupFile
                    ));
                }
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
