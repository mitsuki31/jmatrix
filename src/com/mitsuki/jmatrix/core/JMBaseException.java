// :: --------------------- :: //
/* --    JMBaseException    -- */
// :: --------------------- :: //

package com.mitsuki.jmatrix.core;

import java.lang.Exception;

import com.mitsuki.jmatrix.util.XMLParser;

public class JMBaseException extends Exception
{
    private final XMLParser XML = XMLParser.newParser(XMLParser.XMLType.CONFIG);
    private final String PKG = XML.getProperty("packageName");

    private StackTraceElement[ ] stackTraceElements = null;
    private StackTraceElement[ ] causedStackTraceElements = null;
    private String strException = null;
    private String message = null;
    private boolean isCausedException = false;

    public JMBaseException() {
        this.stackTraceElements = this.getStackTrace();
    }

    public JMBaseException(String s) {
        super(s);
        this.message = s;
        this.stackTraceElements = this.getStackTrace();
    }

    public JMBaseException(Throwable exception) {
        super(exception);
        this.stackTraceElements = this.getStackTrace();
        this.causedStackTraceElements = exception.getStackTrace();
        this.strException = exception.toString();
        this.message = exception.getMessage();
        this.isCausedException = true;
    }

    @Override
    public void printStackTrace() {
        String spc = "        ";
        String arrow = ">>>>>>>>>>>>>";

        if (this.isCausedException && this.stackTraceElements.length > 2) {
            System.err.printf("/!\\ EXCEPTION%s%s%s: %s%s",
                    System.lineSeparator(),
                    arrow,
                    System.lineSeparator(),
                    this.toString(),
                    System.lineSeparator());
            for (int i = this.stackTraceElements.length - 2; i != this.stackTraceElements.length; i++) {
                System.err.printf("%sat \"%s.%s\" -> \"%s\": line %d" + System.lineSeparator(),
                    spc,
                    this.stackTraceElements[i].getClassName(),
                    this.stackTraceElements[i].getMethodName(),
                    this.stackTraceElements[i].getFileName(),
                    this.stackTraceElements[i].getLineNumber());
            }
        } else {
            System.err.printf("/!\\ EXCEPTION%s%s%s: %s%s",
                    System.lineSeparator(),
                    arrow,
                    System.lineSeparator(),
                    this.toString(),
                    System.lineSeparator());
            for (StackTraceElement ste : this.stackTraceElements) {
                System.err.printf("%sat \"%s.%s\" -> \"%s\": line %d" + System.lineSeparator(),
                    spc,
                    ste.getClassName(),
                    ste.getMethodName(),
                    ste.getFileName(),
                    ste.getLineNumber());
            }
        }

        if (this.isCausedException) {
            System.err.printf("%s/!\\ CAUSED BY%s%s%s",
                    System.lineSeparator(),
                    System.lineSeparator(),
                    arrow,
                    System.lineSeparator());
            System.err.println(this.strException);

            for (StackTraceElement cste : this.causedStackTraceElements) {
                System.err.printf("%sat \"%s.%s\" -> \"%s\": line %d" + System.lineSeparator(),
                    spc,
                    cste.getClassName(),
                    cste.getMethodName(),
                    cste.getFileName(),
                    cste.getLineNumber());
            }
        }
        System.err.println(System.lineSeparator() + "[EXCEPTION INFO]");
        System.err.println("Type: " + this.strException.split(":\s")[0]);
        System.err.println("Message: " + this.message);
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public String toString() {
        return String.format("%s: %s", this.getClass().getName(), (this.isCausedException ? this.strException : this.message));
    }
}
