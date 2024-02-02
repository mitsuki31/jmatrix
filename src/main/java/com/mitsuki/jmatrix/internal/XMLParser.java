// ------------------- //
/*      XMLParser      */
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

package com.mitsuki.jmatrix.internal;

import com.mitsuki.jmatrix.exception.JMatrixBaseException;

import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * This class contains data from parsing a config file.
 *
 * @author   <a href="https://github.com/mitsuki31" target="_blank">
 *           Ryuu Mitsuki</a>
 * @version  1.22, 19 July 2023
 * @since    1.0.0b.1
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *           Apache License 2.0</a>
 */
class XMLConfig
{
    /**
     * Stores a string that represents the program name.
     */
    static String programName = null;

    /**
     * Stores a string that represents the version number of <b>JMatrix</b> library.
     */
    static String version = null;

    /**
     * Stores a string that represents the beta number. For example, X.Y.Z-beta.<b>1</b>, where 1 is the beta number.
     */
    static String betaNum = null;

    /**
     * Stores a string that represents the author name.
     */
    static String author = null;

    /**
     * Stores a string that represents the package name of <b>JMatrix</b> library.
     */
    static String packageName = null;

    /**
     * Stores a string represents the release type of <b>JMatrix</b> library.
     * The value can be "release", "beta" or "stable".
     */
    static String releaseType = null;
}

/**
 * This interface could retrieves the stored configs data inside {@link XMLConfig} class.
 *
 * @author   <a href="https://github.com/mitsuki31" target="_blank">
 *           Ryuu Mitsuki</a>
 * @version  1.22, 19 July 2023
 * @since    1.0.0b.1
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *           Apache License 2.0</a>
 */
interface XMLData
{

    /**
     * Gets the property data from the specified input string.
     *
     * @param  choice  a string to search the property data.
     *
     * @return         the property data.
     *
     * @since          1.0.0b.1
     */
    static String getData(final String choice) {
        String data = null;

        switch (choice) {
            // -- config.xml --
            case "programName":
                data = XMLConfig.programName;
                break;
            case "version":
                data = XMLConfig.version;
                break;
            case "betaNum":
            case "betaVer":
                data = XMLConfig.betaNum;
                break;
            case "author":
                data = XMLConfig.author;
                break;
            case "packageName":
                data = XMLConfig.packageName;
                break;
            case "releaseType":
                data = XMLConfig.releaseType;
                break;

            default:
                JMatrixUtils.raiseError(
                    new JMatrixBaseException(
                        new IllegalArgumentException(
                            "Cannot retrieve data for input \"" + choice + "\""
                        )
                    ), -1
                );
        }

        return data;
    }
}


/**
 * This class provides requirements to parse the XML document, which is used to retrieves the
 * required properties for <b>JMatrix</b> library.
 *
 * @author   <a href="https://github.com/mitsuki31" target="_blank">
 *           Ryuu Mitsuki</a>
 * @version  1.22, 19 July 2023
 * @since    1.0.0b.1
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0" target="_blank">
 *           Apache License 2.0</a>
 * @see      com.mitsuki.jmatrix.internal.JMatrixUtils
 */
public class XMLParser implements XMLData
{

    /**
     * {@code Enum} that contains types of XML document.
     * Currently there is only one option (CONFIG).
     *
     * @since 1.0.0b.1
     * @see   #getCurrentType()
     */
    public static enum XMLType {
        /**
         * Represents the "config" option.
         */
        CONFIG
    };


    /**
     * Stores the object class of {@link XMLType} class.
     */
    private XMLType xmlType = null;

    /**
     * Stores a static string represents the path of configuration file.
     */
    private final static String configPath = "configuration/config.xml";

    /**
     * Creates new XMLParser object.
     *
     * @param type  the chosen XML type.
     *
     * @since       1.0.0b.1
     */
    public XMLParser(XMLType type) {
        switch (type) {
            case CONFIG:
                this.xmlType = XMLType.CONFIG;
                break;

            default:
                JMatrixUtils.raiseError(
                    new JMatrixBaseException(
                        new IllegalArgumentException(
                            "Invalid XML type: \"" + type + "\""
                        )
                    ), -1
                );
        }

        try {
            InputStream configStream = XMLParser.class.getClassLoader().getResourceAsStream(configPath);
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();

            Document doc = docBuilder.parse(configStream);
            Element xml = doc.getDocumentElement();

            XMLConfig.programName = xml.getElementsByTagName("program_name").item(0).getTextContent().strip();
            XMLConfig.author = xml.getElementsByTagName("author").item(0).getTextContent().strip();
            XMLConfig.version = xml.getElementsByTagName("version").item(0).getTextContent().strip();
            XMLConfig.releaseType = xml.getElementsByTagName("version").item(0)
                .getAttributes().getNamedItem("type").getNodeValue().strip();
            XMLConfig.packageName = xml.getElementsByTagName("package").item(0).getTextContent().strip();

            if ( !(XMLConfig.version.equals("release") ||
                   XMLConfig.version.equals("stable")) ) {
                XMLConfig.betaNum = xml.getElementsByTagName("beta_num").item(0).getTextContent().strip();
            }
        } catch (final Exception e) {
            JMatrixUtils.raiseError(new JMatrixBaseException(e), -1);
        }
    }


    /**
     * Retrieves and returns the XML data.
     *
     * @param  choice  the name of XML property.
     *
     * @return         the value of XML data, if {@code choice} does not available
     *                 in property name or the {@code choice} is {@code null}, it returns {@code null} instead.
     *
     * @since          1.0.0b.1
     */
    public String getProperty(final String choice) {
        return (choice != null) ? XMLData.getData(choice) : null;
    }

    /**
     * Returns current XML type.
     *
     * @return the current XML type.
     *
     * @since  1.0.0b.1
     */
    public XMLType getCurrentType() {
        return this.xmlType;
    }
}
