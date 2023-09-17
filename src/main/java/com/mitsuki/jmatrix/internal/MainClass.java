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


/**
 * Main class for <b>JMatrix</b> library that provides some information
 * such as <b>JMatrix</b> version. If this class is imported, it will be
 * not valuable because it does not provides APIs to construct the matrices.
 *
 * As of JMatrix 1.5, the class name has changed from
 * {@code Main} to {@code MainClass}.
 *
 * @author   <a href="https://github.com/mitsuki31">Ryuu Mitsuki</a>
 * @version  1.4, 16 September 2023
 * @since    1.0.0b.1
 * @see      com.mitsuki.jmatrix.Matrix
 * @license  <a href="https://www.apache.org/licenses/LICENSE-2.0">
 *           Apache License 2.0</a>
 */
public class MainClass {

    private static Properties setupProperties = SetupProperties.getSetupProperties();

    private static List<String> versionArgs = Arrays.asList(
        "-V", "--version", "ver", "version"
    );

    private static List<String> copyrightArgs = Arrays.asList(
        "-cr", "--copyright", "copyright"
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

        // Parse the command line arguments, removing all duplicate arguments
        List<String> parsedArgs = new ArgumentsParser<String>(args)
                                      .getArguments();

        // Return immediately if the parsed args is empty
        if (parsedArgs.isEmpty()) return;

        StringBuilder sb = new StringBuilder();
        final String firstArg = getFirstArgument(parsedArgs);
        boolean hasOutput = false;

        // Check for version arguments
        if (versionArgs.contains(firstArg)) {
            sb.append(String.format("%s v%s", name, version))
              .append(String.format(" <%s:%s>", groupId, artifactId));
            hasOutput = true;

        // Check for copyright arguments
        } else if (copyrightArgs.contains(firstArg)) {
            sb.append(String.format("%s - Copyright (C) 2023 %s", name, author))
              .append(System.lineSeparator())
              .append(String.format("Licensed under the \"%s\"", license));
            hasOutput = true;
        }

        if (hasOutput)
            System.out.println(sb.toString());
    }


    static String getFirstArgument(List<String> args) {
        List<String> allKnownArgs = new ArrayList<>();
        allKnownArgs.addAll(versionArgs);
        allKnownArgs.addAll(copyrightArgs);

        return args.stream()
                   .filter(arg -> allKnownArgs.contains(arg))
                   .findFirst()    // Get the first index
                   .orElse(null);  // Get and return the value, null if not present
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
 * @version      1.0, 16 September 2023
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
