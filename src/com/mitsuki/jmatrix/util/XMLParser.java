///// ----------------- /////
///       XMLParser       ///
///// ----------------- /////

// -**- Package -**- //
package com.mitsuki.jmatrix.util;

// -**- Built-in Package -**- //
import java.io.InputStream;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.DOMException;

// -**- Local Package -**- //
import com.mitsuki.jmatrix.core.JMBaseException;
import com.mitsuki.jmatrix.util.Options;


/**
* This class will contains data from parsing a config file.<br>
*
* @since  1.0.0
* @author Ryuu Mitsuki
*/
class XMLConfig
{
    static String programName = null;
    static String version = null;
    static String author = null;
    static String packageName = null;
    static String releaseType = null;
}

/**
* This interface for now it just gets the saved config data.
*
* @since  1.0.0
* @author Ryuu Mitsuki
*/
interface XMLData
{
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
                try {
                    throw new IllegalArgumentException("Cannot retrieve data for input \"" + choice + "\"");
                } catch (final IllegalArgumentException iae) {
                    try {
                        throw new JMBaseException(iae);
                    } catch (final JMBaseException jme) {
                        Options.raiseError(jme, -1);
                    }
                }
        }

        return data;
    }
}


/**
* This class provides requirements to parse XML document. Construct example:
* <br>
* <code>
*     XMLParser cfg = new XMLParser(XMLParser.XMLType.CONFIG);
* </code>
* <br>
*
* @since   1.0.0
* @version 1.0
* @author  Ryuu Mitsuki
*
* @see     com.mitsuki.jmatrix.util.Options
* @see     com.mitsuki.jmatrix.util.OSUtils
*/
public class XMLParser implements XMLData
{
    /**
    * {@code Enum} that contains types of XML document.<br>
    *
    * @since 1.0.0
    * @see   #getCurrentType()
    */
    public static enum XMLType {
        CONFIG
    };

    // -- Private Attributes
    private static XMLType xmlType = null;
    private final static String configPath = String.format("assets%sconfiguration%sconfig.xml", OSUtils.sep, OSUtils.sep);
    private static InputStream configStream = null;


    // -- Static Block
    static {
        configStream = XMLParser.class.getClassLoader().getResourceAsStream(configPath);

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = factory.newDocumentBuilder();

            Document doc = docBuilder.parse(configStream);
            Element xml = doc.getDocumentElement();

            XMLConfig.programName = xml.getElementsByTagName("program_name").item(0).getTextContent();
            XMLConfig.author = xml.getElementsByTagName("author").item(0).getTextContent();
            XMLConfig.version = xml.getElementsByTagName("version").item(0).getTextContent();
            XMLConfig.releaseType = xml.getElementsByTagName("version").item(0)
                .getAttributes().getNamedItem("type").getNodeValue();
            XMLConfig.packageName = xml.getElementsByTagName("package").item(0).getTextContent();
        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme, -1);
            }
        }
    }

    /**
    * Creates new {@code XMLParser} object.<br>
    *
    * @param type  a chosen XML type.
    *
    * @since 1.0.0
    */
    public XMLParser(XMLType type) {
        switch (type) {
            case CONFIG:
                xmlType = XMLType.CONFIG;
                break;

            default:
                try {
                    throw new IllegalArgumentException("Invalid XML type for input \"" + type + "\"");
                } catch (final IllegalArgumentException iae) {
                    try {
                        throw new JMBaseException(iae);
                    } catch (final JMBaseException jme) {
                        Options.raiseError(jme, -1);
                    }
                }
        }
    }


    /**
    * This method retrieves the XML data.<br>
    *
    * @param  choice  the {@code String} to chooses XML property.
    *
    * @return returns XML data if {@code choice} not {@code null}.
    *
    * @since  1.0.0
    */
    public String getProperty(final String choice) {
        if (choice != null) {
            switch (xmlType) {
                case CONFIG:
                    return XMLData.getData(choice);
            }
        }

        return null;
    }

    /**
    * Gets current XML type.<br>
    *
    * @return the current XML type.
    *
    * @since  1.0.0
    * @see    XMLParser#XMLType
    */
    public XMLType getCurrentType() {
        return this.xmlType;
    }


    private static void initializeParser() {
        try {
            // -- config.xml --
            Document doc = buildToDoc(configStream);
            Element xml = doc.getDocumentElement();

            XMLConfig.programName = xml.getElementsByTagName("program_name").item(0).getTextContent();
            XMLConfig.author = xml.getElementsByTagName("author").item(0).getTextContent();
            XMLConfig.version = xml.getElementsByTagName("version").item(0).getTextContent();
            XMLConfig.releaseType = xml.getElementsByTagName("version").item(0)
                .getAttributes().getNamedItem("type").getNodeValue();
            XMLConfig.packageName = xml.getElementsByTagName("package").item(0).getTextContent();

        } catch (final Exception e) {
            try {
                throw new JMBaseException(e);
            } catch (final JMBaseException jme) {
                Options.raiseError(jme, -1);
            }
        }
    }

    private static Document buildToDoc(InputStream stream)
    throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder docBuilder = factory.newDocumentBuilder();
        Document doc = docBuilder.parse(configStream);

        return doc;
    }
}
