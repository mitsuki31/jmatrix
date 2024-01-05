"""Utilities Module for JMBuilder

This module provide utilities members, including JSON parser and properties
parser for `JMBuilder` module.

Copyright (c) 2023-2024 Ryuu Mitsuki.


Available Classes
-----------------
JMProperties
    A custom properties parser class, implemented in `jmbuilder.utils` package.
    Python does not provide an easy way to parsing files with extension of
    `.properties`, this class is designed to parse properties file and
    access their contents with ease without any third-party modules.

Available Functions
-------------------
json_parser
    This utility function provides an easy way to parses and retrieves all
    configurations from JSON configuration files.

    Examples::

        >>> json_parser('path/to/configs_file.json')
        {'foo': False, 'bar': True}

remove_comments
    This utility function can remove lines from a list of strings representing
    the file contents that starting with a specified delimiter and returns
    a new list with lines starting with a specified delimiter removed.
    This function are designed for removing comments lines from file contents.

    Examples::

        # Read file contents
        >>> contents = []
        >>> with open('path/to/example.txt') as f:
        ...     contents = f.readlines()
        ...
        >>> contents
        ['Hello', '# This is comment line', 'World']

        >>> remove_comments(contents, delim='#')
        ['Hello', 'World']

remove_blanks
    This utility function can remove blank lines from the list of strings
    representing the file contents, and optionally remove lines with value `None`.
    Return a new list with blank lines removed.

    Examples::

        >>> contents = ['', 'Foo', None, '', '1234']
        >>> remove_blanks(contents, none=False)  # Ignore None
        ['Foo', None, '1234']

        >>> remove_blanks(contents, none=True)
        ['Foo', '1234']

readfile
    This utilty function will read the contents from the given file path and
    return a list containing all contents from that file.
    
    If the given path does not refer to an existing regular file or the given path
    is refer to a directory, it will raises an error.
    
    Examples::

        # Read contents from file called 'myfile.txt'
        >>> contents = readfile('myfile.txt')

"""

import os as _os
import io as _io
import sys as _sys
import json as _json
import locale as _locale
import collections as _collections
from pathlib import Path as _Path
from typing import (
    Dict, List, Optional,
    Union, Type, TextIO, Sequence
)

from .._globals import AUTHOR, VERSION, VERSION_INFO
from ..exception import (
    JMUnknownTypeError as _JMTypeError,
    JMParserError as _JMParserError
)


__all__ = [
    'json_parser', 'remove_comments', 'remove_blanks', 'JMProperties'
]


def json_parser(path: str) -> dict:
    """
    Parse and retrieve all configurations from specified JSON
    configuration file.

    Parameters
    ----------
    path : str
        The path to specify the configuration file to be parsed.

    Returns
    -------
    dict :
        A dictionary containing all parsed configurations from specified
        configuration file.

    Raises
    ------
    JMParserError :
        If something went wrong during parsing the configuration file.

    ValueError :
        If the given path is `None`.

    JMUnknownTypeError :
        If the given path's type are not `str`.

    FileNotFoundError :
        If the given path are refers to a non-existing file.

    IsADirectoryError :
        If the given path are refers to a directory instead a configuration file.

    Notes
    -----
    This function only supported configuration file with JSON type.
    Given file paths will be validated before retrieving their configurations,
    and check the extension file. Make sure the given path references
    to file with the `.json` extension.

    """
    if not path:
        raise ValueError(
            f"Unexpected value: '{type(path).__name__}'. Expected 'str'"
        ) from _JMParserError(
            "Something went wrong while parsing the configuration file"
        )

    # Raise an error if the given path not `str` type
    if not isinstance(path, str):
        raise _JMTypeError(
            f"Unknown type '{type(path).__name__}'. Expected 'str'"
        ) from _JMParserError(
            "Something went wrong while parsing the configuration file"
        )

    # Check existence
    if not _os.path.exists(path):
        raise FileNotFoundError(
            f"No such file or directory: '{path}'"
        ) from _JMParserError(
            'Configuration file was not found'
        )

    # Check whether a directory or regular file
    if _os.path.isdir(path):
        raise IsADirectoryError(
            f"Is a directory: '{path}'"
        ) from _JMParserError(
            'Directory found. No such configuration file'
        )

    # Check the extension file
    if not path.endswith('.json'):
        raise _JMParserError(
            'Unknown file type. No such JSON configuration file'
        )

    configs: dict = {}

    with open(path, 'r', encoding='utf-8') as cfg_file:
        contents: str = cfg_file.read()

        # Check for null to prevent an error on JSONDecoder
        if contents:
            configs = _json.loads(contents)  # return a dictionary

    return configs



def remove_comments(contents: List[str], delim: str = '#') -> List[str]:
    """
    Remove lines starting with a specified delimiter.

    This function removes lines from the input list of contents that start
    with the specified delimiter. It returns a new contents with comments removed.

    Parameters
    ----------
    contents : List[str]
        A list of strings representing the contents of a file.

    delim : str, optional
        The delimiter used to identify comment lines. Lines starting with
        this delimiter will be removed. The default is '#'.

    Returns
    -------
    List[str] :
        A new contents with comment lines (specified by delimiter) removed.

    Raises
    ------
    ValueError :
        If the input list `contents` is empty.

    Notes
    -----
    Multiple specified delimiters cannot be removed in a single call to
    this function. Although the problem can be fixed by executing the
    procedure as many times depending on the delimiters that need
    to be removed. But still it is not a convenient way.

    Examples::

        # Suppose we want to remove lines that starting with
        # hashtags (#) and exclamation marks (!).
        >>> remove_comments(
        ...     remove_comments(contents, delim='#'), delim='!')

    """
    if not contents or len(contents) == 0:
        raise ValueError('File contents cannot be empty')

    # Use a list comprehension to filter out lines starting with the delimiter
    return [line for line in contents if not line.startswith(delim)]



def remove_blanks(contents: List[str], none: bool = True) -> List[str]:
    """
    Remove empty lines from a list of strings.

    This function removes empty lines (lines with no content) and lines
    containing only whitespace from the input list of strings. Optionally,
    it can removes lines containing `None`.

    Parameters
    ----------
    contents : List[str]
        A list of strings representing the contents of a file.

    none : bool, optional
        If True, lines containing `None` are also removed.
        If False, only lines with no content are removed. The default is True.

    Returns
    -------
    List[str] :
        A new contents with empty lines removed.

    Raises
    ------
    ValueError :
        If the input list `contents` is empty.

    """
    if not contents or len(contents) == 0:
        raise ValueError('File contents cannot be empty')

    return [
        line for line in contents
        if (line is None and not none) or \
            (line is not None and line.strip() != '')
    ]


def readfile(path: str, encoding: str = 'UTF-8') -> List[str]:
    """
    Read all contents from the specified file.

    Parameters
    ----------
    path : str
        A string path refers to a regular file.

    encoding : str, optional
        An encoding to be used during read operation.
        Defaults to `UTF-8`.

    Returns
    -------
    List[str] :
        A list of string containing contents from the specified file.

    Raises
    ------
    FileNotFoundError
        If the given path does not refer to the existing file.

    PermissionError
        If there is a permission restrictions during read operation.

    IsADirectoryError
        If the given path is refer to a directory, not a regular file.

    """

    contents: List[str] = []
    with open(path, 'r', encoding=encoding) as file:
        contents = file.readlines()

    return contents


def remove_duplicates(seq: Sequence) -> Sequence:
    """
    Remove duplicates from a sequence while preserving the original order.

    Parameters
    ----------
    seq : list or iterable
        The input sequence containing elements with potential duplicates.

    Returns
    -------
    list
        A new list containing unique elements from the input sequence,
        while maintaining the original order.

    Notes
    -----
    This function uses a set to keep track of seen elements and filters
    out duplicates while preserving the order of the original sequence.

    Example
    -------
    >>> foo = [1, 2, 3, 1, 2, 4, 5]
    >>> remove_duplicates(foo)
    [1, 2, 3, 4, 5]

    """
    seen = set()
    return [x for x in seq if not (x in seen or seen.add(x))]


class JMProperties(_collections.UserDict):
    """
    This class provides a convenient way to parse properties files
    and access their contents.

    Parameters
    ----------
    filename : str or TextIO
        The filename or file object to read properties from. If a filename is
        provided, it checks for the file's existence, opens the file stream,
        and retrieves the properties. If a file object is provided, it directly
        reads the properties from it.

    encoding : str, optional
        The encoding to use when opening the file stream. If not specified,
        it uses the encoding from `locale.getpreferredencoding()`.
        Defaults to system's preferred encoding.

    Attributes
    ----------
    data : Dict[str, str]
        A dictionary containing all the parsed properties.

    filename : str
        An absolute path to the specified property file.

    Raises
    ------
    JMParserError :
        If an error occurs while reading and parsing the properties file.

    FileNotFoundError :
        If the specified file path does not exist.

    ValueError :
        If the `filename` parameter is None.

    """
    def __init__(self, filename: Union[str, TextIO], *,
                 encoding: str = _locale.getpreferredencoding()) -> None:
        """Initialize self."""

        self.filename = filename
        self.encoding = encoding

        def __blank_remover(contents: list) -> list:
            """
            Remove trailing whitespace and newline character from the contents.

            Parameters
            ----------
            contents : list
                A list of strings representing contents of file.

            Returns
            -------
            list :
                A new list with trailing whitespace and newline characters removed.

            """
            return [line.strip() for line in contents]

        def __line_splitter(contents: list, delim: str) -> list:
            """
            Split the given string using specified delimiters from the contents.

            Parameters
            ----------
            contents : list
                A list of strings representing contents of file.

            delim: str
                The delimiter to be used for splitting keys and values.

            Returns
            -------
            list :
                A new list with each entries contains two strings that
                representing a key and value respectively.

            Notes
            -----
            The length of an entry in the returned list might be used to indicate
            an unsuccessful result. If the length is equal to one, it indicates
            that the function was unable to split the contents using the specified
            delimiter (this could be due to the delimiter not being present
            in the contents).

            For clarity, here's some examples::

                >>> contents = ['foo: bar', 'pswd: helloWORLD']
                >>> new_contents = __line_splitter(contents, '$')
                >>> len(new_contents[0])  # Check the length
                1  # Failed to split due to the delim not present in contents

                >>> new_contents = __line_splitter(contents, ':')
                >>> len(new_contents[0])
                2  # Succeed

            """
            return [line.split(delim, maxsplit=1) for line in contents]


        if isinstance(filename, str):
            self.filename = _os.path.abspath(filename)

        # If encoding is not specified, use the system's preferred encoding
        encoding = encoding if encoding else _locale.getpreferredencoding()

        if not self.filename:
            raise ValueError("The 'filename' parameter cannot be None")

        # Raise FileNotFoundError, if the given file are not exist
        # First these code below will checks whether the given file is not None
        # and its type is `str`
        if self.filename and \
                isinstance(self.filename, str) and \
                not _os.path.exists(self.filename):
            raise FileNotFoundError(f"File not found: '{filename}'") \
                from _JMParserError(
                    'The specified path does not reference any property file ' +
                    'or the file does not exist'
                )

        properties_data: Dict[str, str] = {}
        contents: list = []

        # Open and read the contents if the given file is of type `str`
        if isinstance(filename, str):
            with open(filename, 'r', encoding=encoding) as prop:
                contents = prop.readlines()
        elif isinstance(filename, _io.TextIOWrapper):
            contents = filename.readlines()

            # Get the name of property file
            self.filename = filename.name

        # Extract file contents, remove comments and empty strings
        contents = __blank_remover(contents)
        contents = remove_comments(contents, '#')
        contents = remove_blanks(contents, none=True)

        # First, try to split the keys and values using equals sign (=) as a delimiter
        data: List[str] = __line_splitter(contents, delim='=')
        keys, values = None, None

        # Check if the first try has extracted the keys and values successfully
        # If the length of each element is one, it indicates extraction failure,
        # so we try splitting using a colon (:) as a delimiter
        if data and len(data[0]) == 1:
            # In this second try, use a colon (:) as a delimiter
            # for keys and values
            data = __line_splitter(contents, delim=':')

        try:
            # Unpack keys and values into variables (errors can occur here)
            keys, values = zip(*data)
        except (ValueError, TypeError) as type_val_err:
            raise type_val_err from _JMParserError(
                'Unable to unpack keys and values'
            )

        # Remove trailing whitespace in keys and values
        keys = tuple(__blank_remover(keys))
        values = tuple(__blank_remover(values))

        # Build the dictionary from extracted keys and values
        properties_data = dict(zip(keys, values))

        super().__init__(properties_data)


__author__       = AUTHOR
__version__      = VERSION
__version_info__ = VERSION_INFO


# Delete imported objects that are no longer used
del AUTHOR, VERSION, VERSION_INFO
del Dict, List, Optional, Union, Type, TextIO, Sequence
