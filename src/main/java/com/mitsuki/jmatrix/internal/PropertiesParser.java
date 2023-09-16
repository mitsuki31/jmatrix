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

import java.io.File;
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.nio.file.AccessDeniedException;
import java.util.Properties;

public final class PropertiesParser {
    private String propertyFile;
    private Properties properties;

    private PropertiesParser(InputStream inStream,
                             String propertyPath) throws IOException {
        this.propertyFile = propertyPath;
        this.properties = new Properties();
        this.properties.load(inStream);
    }

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

    public Properties getProperties() {
        return this.properties;
    }
}


class SetupProperties {
    private static final String setupFile = "configuration/setup.properties";
    private static Properties setupProperties;

    static {
        File setupFile_FileObj = new File(setupFile);

        // Store the exception and will be thrown later if not null
        Throwable causeException = null;

        if (!setupFile_FileObj.exists()) {
            causeException = new FileNotFoundException(
                String.format("Cannot found '%s' file", setupFile)
            );
        } else if (setupFile_FileObj.exists() &&
                   !setupFile_FileObj.canRead()) {
            causeException = new AccessDeniedException(setupFile,
                null, "Read access is denied"
            );
        }

        // It is a good practice to throw occurred exception immediately,
        // especially after exiting a code block (e.g., if-else statement block)
        if (causeException != null)
            throw new ExceptionInInitializerError(causeException);

        // Although the Properties class has been designed to be synchronized,
        // the code below further ensures that it is implicitly synchronized
        if (setupProperties == null) {
            try (InputStream inStream = SetupProperties.class
                    .getClassLoader()
                    .getResourceAsStream(setupFile)) {
                setupProperties = new Properties();
                setupProperties.load(inStream);
            } catch (final IOException ioe) {
                throw new ExceptionInInitializerError(ioe);
            }
        }
    }

    static Properties getSetupProperties() {
        return setupProperties;
    }
}
