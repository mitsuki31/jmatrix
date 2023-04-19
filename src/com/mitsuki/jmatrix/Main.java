package com.mitsuki.jmatrix;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.lang.IllegalArgumentException;
import java.lang.NullPointerException;

import com.mitsuki.jmatrix.util.Options;
import com.mitsuki.jmatrix.util.OSUtils;
import com.mitsuki.jmatrix.util.XMLParser;

public class Main
{
    private static int errCode = 0;

    public static void main(String[ ] args) {
        String arg1 = null,
               arg2 = null;

        XMLParser XML = new XMLParser(XMLParser.XMLType.CONFIG);

        if (args.length >= 1 && args.length < 2) {
            arg1 = args[0];
            if (args.length == 2) {
                arg2 = args[1];
            }
        }
        else if (args.length > 1) {
            try {
                throw new IllegalArgumentException("Too much arguments");
            } catch (final Exception e) {
                Options.raiseErrorMsg(e, 0);
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
                System.out.print(XML.getProperty("programName") + " " +
                    XML.getProperty("version") + "-");
                System.out.print(XML.getProperty("releaseType"));
                System.out.printf(" <%s>\n", XML.getProperty("packageName"));
                break;

            case HELP:
                System.out.println(XML.getProperty("programName") + " " +
                    XML.getProperty("version"));
                for (String s : Options.getHelpMsg()) System.out.println(s);
                break;

            case COPYRIGHT:
                for (String s : Options.getCopyright()) System.out.println(s);
                break;
        }
    }
}
