// ------------------- //
/*      MainClass      */
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

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.lang.Iterable;
import java.time.Year;


/**
 * Main class for <b>JMatrix</b> library that provides some information
 * such as <b>JMatrix</b> version. If this class is imported, it will be
 * not valuable because it does not provides APIs to construct the matrices.
 *
 * As of JMatrix 1.5.0, the class name has changed from
 * {@code Main} to {@code MainClass}.
 *
 * @author   <a href="https://github.com/mitsuki31">Ryuu Mitsuki</a>
 * @version  1.6, 8 January 2024
 * @since    1.0.0b.1
 * @see      com.mitsuki.jmatrix.Matrix
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0">
 *           Apache License 2.0</a>
 */
public class MainClass {

    /**
     * A {@link Properties} object reference to synchronized setup properties.
     */
    private static Properties setupProperties = SetupProperties.getSetupProperties();

    /**
     * A list of known version arguments.
     */
    private static List<String> versionArgs = Arrays.asList(
        "-V", "--version", "ver", "version"
    );

    /**
     * A list of known copyright arguments.
     */
    private static List<String> copyrightArgs = Arrays.asList(
        "-cr", "--copyright", "copyright"
    );

    /**
     * A list of known help arguments.
     */
    private static List<String> helpArgs = Arrays.asList(
        "-h", "--help", "help", "?" /* << Exclusive to help arguments */
    );

    /**
     * Main method for <b>JMatrix</b> library.
     *
     * @param args  a {@code String} array containing arguments from command line.
     *
     * @since       1.0.0b.1
     */
    public static void main(String[] args) {
        final String name = setupProperties.getProperty("JM-Name");
        final String version = setupProperties.getProperty("JM-Version");
        final String author = setupProperties.getProperty("JM-Author");
        final String license = setupProperties.getProperty("JM-License");
        final String groupId = setupProperties.getProperty("JM-GroupId");
        final String artifactId = setupProperties.getProperty("JM-ArtifactId");
        final String inceptionYear = setupProperties.getProperty("JM-InceptionYear");
        final String buildTime = JMatrixUtils.dateISOToLocal(
            setupProperties.getProperty("JM-BuildTime"), "MMM dd, yyyy HH:mm:ss");

        // Parse the command line arguments, removing all duplicate arguments
        List<String> parsedArgs = new ArgumentsParser<String>(args)
                                      .getArguments();

        // Print the help message if the parsed args is empty
        if (parsedArgs.isEmpty()) {
            printHelpMessage();
            return;  // then return to stop further execution
        }

        StringBuilder sb = new StringBuilder();
        final String firstArg = getFirstArgument(parsedArgs),
                     newline = System.lineSeparator();

        // Check for version arguments
        if (versionArgs.contains(firstArg)) {
            sb.append(String.format("%s v%s", name, version))
              .append(String.format(" <%s:%s>", groupId, artifactId))
              .append(String.format("%sBuilt on %s UTC", newline, buildTime));
        // Check for copyright arguments
        } else if (copyrightArgs.contains(firstArg)) {
            int currentYear = Year.now().getValue();
            sb.append(String.format("%s - Copyright (C) %s - %d %s%s",
                      name, inceptionYear, currentYear, author, newline))
              .append(String.format("Licensed under the %s", license));
        // Check for help arguments
        } else if (helpArgs.contains(firstArg)) {
            printHelpMessage();
        } else if (firstArg != null && firstArg.equals("--version-only")) {
            sb.append(version);
        }

        // Print to the console (standard output stream)
        if (sb.toString().length() != 0) System.out.println(sb.toString());
    }


    /**
     * Searches and returns the first known argument from a list of arguments.
     * This method compares the provided list of arguments with a predefined
     * list of known version and copyright arguments.
     *
     * <p>If a known argument is found, it is returned as the first
     * known argument. If none of the provided arguments match the known ones,
     * it returns {@code null}.
     *
     * @param  args  A list of arguments to search for known arguments.
     *
     * @return       The first known argument found, or {@code null}
     *               if none are known.
     *
     * @since        1.5.0
     */
    static String getFirstArgument(List<String> args) {
        List<String> allKnownArgs = new ArrayList<>();
        allKnownArgs.addAll(versionArgs);
        allKnownArgs.addAll(copyrightArgs);
        allKnownArgs.addAll(helpArgs);
        allKnownArgs.add("--version-only");

        return args.stream()
                   .filter(arg -> allKnownArgs.contains(arg))
                   .findFirst()    // Get the first index
                   .orElse(null);  // Get and return the value, null if not present
    }

    /**
     * Prints the help message to the standard output stream.
     *
     * @since 1.5.0
     */
    private static void printHelpMessage() {
        final String newline = System.lineSeparator(),  // A newline
                     dblNewline = newline + newline;    // Double newlines
 
        final String header = String.format("%s v%s",
            setupProperties.getProperty("JM-Name"),
            setupProperties.getProperty("JM-Version")
        );

        final StringBuilder sb = new StringBuilder(),
                            lineSb = new StringBuilder();

        // Create a line with length equals to header length
        for (int i = 0; i < header.length(); i++) lineSb.append('-');

        sb.append(header + newline)
          .append(lineSb.toString() + dblNewline)
          .append("USAGE:" + newline)
          .append("   java -jar path/to/jmatrix.jar [options]" + dblNewline)
          .append("OPTIONS:" + newline)
          .append("   ver, version, -V, --version" + newline)
          .append(
                "       Print the current version information of JMatrix." + newline +
                "       Use `--version-only` to print the number version only." +
                dblNewline
          )
          .append("   copyright, -cr, --copyright" + newline)
          .append("       Print the copyright and license information." + dblNewline)
          .append("   ?, help, -h, --help" + newline)
          .append("       Print this help message." + dblNewline)
          .append("ISSUES:" + newline)
          .append(String.format(
                "   Having some bugs or having any issues with algorithm of matrix%s" +
                "   operations? Please report to the following link.%s" +
                "       https://github.com/mitsuki31/jmatrix/issues/new/choose%s",
                newline, dblNewline, dblNewline
          ))
          .append(String.format(
                "   Or if you want to contribute as well, you can create your own%s" +
                "   pull request.",
                newline
          ));

        System.out.println(sb.toString());
    }
}


/**
 * An utility class for parsing and managing a set of unique arguments.
 * It is designed only for {@link MainClass} class to parse the
 * command line arguments.
 *
 * @param   <E>  The type of object stored in this class.
 *
 * @author       <a href="https://github.com/mitsuki31">Ryuu Mitsuki</a>
 * @version      1.2, 17 September 2023
 * @since        1.5.0
 */
final class ArgumentsParser<E> implements Iterable<E> {

    /**
     * Stores a list of arguments with duplicate elements removed.
     */
    private List<E> arguments;

    /**
     * Sole constructor. Constructs this class with the specified array of arguments.
     * Duplicate elements are removed, and the order is preserved.
     *
     * @param args  The array of arguments to parse.
     *
     * @since       1.5.0
     */
    ArgumentsParser(E[] args) {
        // Using LinkedHashSet to remove duplicate elements and
        // preserve the order of elements
        Set<E> argsSet = new LinkedHashSet<E>(Arrays.asList(args));

        // Convert to List after removing all duplicate elements
        this.arguments = new ArrayList<E>(argsSet);
    }


    /**
     * Checks whether this parser contains the specified element.
     *
     * @param  o  element whose presence in this list is to be tested.
     *
     * @return    {@code true} if the element is present, {@code false} otherwise.
     *
     * @since     1.5.0
     */
    boolean contains(Object o) {
        return this.arguments.contains(o);
    }

    /**
     * Returns the element at the specified position in this list of arguments.
     *
     * @param  index  The index of the element to retrieve
     *                (index &gt; 0 &amp;&amp; index &lt; size()).
     *
     * @return        The element at the specified index in this list.
     *
     * @since         1.5.0
     *
     * @throws IndexOutOfBoundsException
     *         if the index is out of range (index &lt; 0 || index &gt; size()).
     */
    E get(int index) {
        return this.arguments.get(index);
    }

    /**
     * Returns an unmodifiable view of the arguments contained in this class.
     * Modifications to the returned list result in an
     * {@code UnsupportedOperationException}.
     *
     * @return An unmodifiable view of the arguments.
     *
     * @since  1.5.0
     */
    List<E> getArguments() {
        // Return the properties with read-only attribute,
        // this means users cannot modify the elements.
        return Collections.unmodifiableList(this.arguments);
    }

    /**
     * Returns an iterator over the elements in this list of arguments
     * in proper sequence.
     *
     * @return An iterator over the elements.
     *
     * @since  1.5.0
     */
    @Override
    public Iterator<E> iterator() {
        return this.arguments.iterator();
    }
}
