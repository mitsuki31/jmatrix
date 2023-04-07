///// ----------------- /////
///       XMLParser       ///
///// ----------------- /////

package com.mitsuki.jmatrix.util;

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

import com.mitsuki.jmatrix.core.JMBaseException;
import com.mitsuki.jmatrix.util.Options;

class XMLConfig
{
    static String programName = null;
    static String version = null;
    static String author = null;
    static String packageName = null;
    static String releaseType = null;
}

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
                } catch (final IllegalArgumentException e) {
                    try {
                        throw new JMBaseException(e);
                    } catch (final JMBaseException ex) {
                        Options.raiseError(ex, -1);
                    }
                }
        }

        return data;
    }
}

public class XMLParser implements XMLData
{
    public static enum XMLType {
        CONFIG
    };

    private static XMLType xmlType = null;
    private final static String configPath = String.format("assets%sconfiguration%sconfig.xml", OSUtils.sep, OSUtils.sep);
    private static InputStream configStream = null;

    public String getProperty(final String choice) {
        switch (xmlType) {
            case CONFIG:
                return XMLData.getData(choice);
        }

        return null;
    }

    public XMLType getCurrentType() {
        return this.xmlType;
    }

    public static XMLParser newParser(XMLType type) {
        switch (type) {
            case CONFIG:
                xmlType = XMLType.CONFIG;
                configStream = Options.getFileAsStream(configPath);
                break;

            default:
                try {
                    throw new IllegalArgumentException("Invalid XML type for input \"" + type + "\"");
                } catch (final IllegalArgumentException e) {
                    try {
                        throw new JMBaseException(e);
                    } catch (final JMBaseException ex) {
                        Options.raiseError(ex, -1);
                    }
                }
        }

        initializeParser();

        return new XMLParser();
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
            } catch (final JMBaseException ex) {
                Options.raiseError(ex, -1);
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
