"""Custom Exceptions Module for `JMBuilder`

This module provides all custom exceptions for `JMBuilder`.

Copyright (c) 2023-2024 Ryuu Mitsuki.


Available Classes
-----------------
JMException :
    The base custom exception for `JMBuilder`.

JMUnknownTypeError :
    The custom exception that raised when an unknown type error
    occurs during the execution of the software.

JMParserError :
    Raised when an error has occurred during parsing the configuration.

"""

import os as _os
import sys as _sys
import traceback as _tb

from typing import Optional, Any

from .._globals import AUTHOR, VERSION, VERSION_INFO


__all__    = ['JMException', 'JMUnknownTypeError', 'JMParserError']


class JMException(Exception):
    """
    Base custom exception for `JMBuilder`. This class also provides
    traceback control (i.e., customizable).

    Parameters
    ----------
    *args : list of positional arguments
        This parameter accepts a variable number of arguments. It treats the
        first argument as a format message and the subsequent arguments as
        format arguments. If only one argument is specified, it will be treated
        as a literal message. The formatting follows the conventions of C-style
        string formatting. For example::

            # Assuming we have an index variable with a value of 5
            >>> JMException('Invalid index: %d', index)
            JMException('Invalid index: 5', custom_traceback=False)

            # Using multiple format arguments
            >>> JMException('Invalid index: %d, at file: %s', 2, 'foo.py')
            JMException('Invalid index: 2, at file foo.py', custom_traceback=False)

    **kwargs : keyword arguments
        Arbitrary keyword arguments are accepted. Users can also unpack a
        dictionary into this parameter. All keyword arguments will be passed to
        the base exception class (i.e., the `Exception` class), except for the
        following reserved keywords:

            - `tb`
            - `trace`
            - `traces`

        These reserved keywords allow users to control the exception's
        traceback behavior. If more than one of these keywords is specified,
        the `tb` keyword will take precedence. However, be cautious when
        specifying multiple reserved keywords, as this can lead to unexpected
        behavior due to other arguments being passed to the base exception
        class.

    Properties
    ----------
    message : str or None
        The message of this exception.

        To specify the message of this exception, consider place the message
        string at first argument.

    traces : traceback.StackSummary
        The stack traces of this exception. If no traceback is provided during
        the exception creation, it will be overrided by `traceback.extract_stack()`.

        To manually override the stack traces of this exception, consider use keyword
        `tb`, `trace` or `traces`, with the value separated by equals sign (`=`).
        For example::

            # Use the `tb` keyword
            >>> JMException('An error occured', tb=foo)
            JMException('An error occured', custom_traceback=True)

            # Use the `trace` keyword
            >>> JMException('An error occured', trace=foo)
            JMException('An error occured', custom_traceback=True)

            # Use the `traces` keyword
            >>> JMException('An error occured', traces=foo)
            JMException('An error occured', custom_traceback=True)

    Raises
    ------
    TypeError :
        If the given arguments (other than the first argument) are different with
        the format specifier at the first argument. For instance::

            >>> JMBuilder('Syntax error at line: %d', '44')
            TypeError: %d format: a real number is required, not str

    """

    message:  Optional[str]
    traces:   _tb.StackSummary

    def __init__(self, *args, **kwargs) -> None:
        """Initialize self. For accurate signature, see ``help(type(self))``."""

        baseerr: str = f'Error occured during initializing {type(self)}'
        tb_key:  str = None

        if len(args) > 0 and (args[0] and not isinstance(args[0], str)):
            raise self.__class__(baseerr) from \
                TypeError(
                    f'Invalid type of `message`: "{type(args[0]).__name__}". ' + \
                    'Expected "str"'
                )

        self.__message              = None
        self.__traces               = None
        self.__use_custom_traceback = False

        if len(args) > 1:
            try:
                self.__message = args[0] % args[1:]
            except TypeError as type_err:
                raise self.__class__(baseerr) from type_err

        elif len(args) == 1:
            self.__message = args[0]

        # Check the custom stack traces in keyword arguments (kwargs)
        if 'tb' in kwargs:
            tb_key = 'tb'
            self.__use_custom_traceback = True
        elif 'trace' in kwargs or 'traces' in kwargs:
            tb_key = 'trace' if 'trace' in kwargs else 'traces'
            self.__use_custom_traceback = True
        else:
            self.__traces = _tb.extract_stack()

        # Check the instance of 'tb_key', otherwise pass if None
        if tb_key:
            if not isinstance(kwargs[tb_key], _tb.StackSummary):
                raise self.__class__(baseerr) from \
                    TypeError(
                        f'Unknown type: "{type(kwargs[tb_key]).__name__}". ' +
                        'Expected "traceback.StackSummary"'
                    )

            # Retrieve the custom traceback
            self.__traces = kwargs.get(tb_key)
            del kwargs[tb_key]  # delete after retrieved the stack traces

        super().__init__(self.__message, **kwargs)


    def __repr__(self) -> str:
        """
        Return ``repr(self)``.

        Returns
        -------
        str :
            The string representation of this exception, including the class name
            and the message of this exception (if specified), and the
            `with_traceback` with a stringized boolean value.

        Notes
        -----
        The `with_traceback` will have ``True`` value if and only if the
        stack traces are defined either given from initialization class
        or defaults to `traceback.extract_stack()`, otherwise ``False``
        if there is no stack traces defined in this exception.

        """
        return f"{self.__class__.__name__}({self.__message!r}, " + \
               f"custom_traceback={self.__use_custom_traceback})"

    def __str__(self) -> str:
        """
        Return the string representation of this exception's message.

        Returns
        -------
        str :
            The message of this exception.

        Notes
        -----
        This method will never returns ``NoneType`` when the message are not specified,
        instead it returns the empty string.

        """
        return f'{self.__message}' if self.__message is not None else ''

    def __eq__(self, other: Any) -> bool:
        """
        Return ``self==value``.

        Parameters
        ----------
        other : Any
            The object to compare with this exception.

        Returns
        -------
        bool
            ``True`` if the given object are the same exception with the same message
            or if the given object are string with the same message as this exception,
            otherwise ``False``.

        """
        if isinstance(other, (self.__class__, str)):
            return str(self) == str(other)

        return False


    @property
    def message(self) -> Optional[str]:
        """
        Get the detail message of this exception.

        Returns
        -------
        str or None
            The message of this exception. If not specified, returns ``None``.

        """
        return self.__message

    @property
    def traces(self) -> _tb.StackSummary:
        """
        Get the stack traces of this exception.

        Returns
        -------
        traceback.StackSummary
            The stack traces of this exception. If not specified, returns
            the stack traces from ``traceback.extract_stack()``.

        """
        if self.__traces and isinstance(self.__traces, _tb.StackSummary):
            return self.__traces

        return _tb.extract_stack()



class JMUnknownTypeError(JMException, TypeError):
    """
    Custom exception for unknown type errors.

    This exception is raised when an unknown type error occurs during
    the execution of the software.

    Parameters
    ----------
    *args : list of arguments
        This parameter accepts a variable number of arguments.

    **kwargs : keyword arguments
        Additional keyword arguments to customize the exception.

    Notes
    -----
    For more details about arguments, see `JMException` documentation.

    """

    def __init__(self, *args, **kwargs) -> None:
        """Initialize self. See ``help(type(self))`` for accurate signature."""

        super().__init__(*args, **kwargs)
        self.__message = super().message
        self.__traces = super().traces

    @property
    def message(self) -> Optional[str]:
        """
        Get the detail message of this exception.

        Returns
        -------
        str or None :
            The message of this exception. If not specified, returns ``None``.

        """
        return self.__message

    @property
    def traces(self) -> _tb.StackSummary:
        """
        Get the stack traces of this exception.

        Returns
        -------
        traceback.StackSummary :
            The stack traces of this exception. If not specified, returns
            the stack traces from `traceback.extract_stack()`.

        """
        if self.__traces and isinstance(self.__traces, _tb.StackSummary):
            return self.__traces

        return _tb.extract_stack()



class JMParserError(JMException):
    """
    Raised when an error has occurred during parsing the configuration.

    Parameters
    ----------
    *args : list of arguments
        This parameter accepts a variable number of arguments.

    **kwargs : keyword arguments
        Additional keyword arguments to customize the exception.

    Notes
    -----
    For more details about arguments, see `JMException` documentation.

    """

    def __init__(self, *args, **kwargs) -> None:
        """Initialize self. See ``help(type(self))`` for accurate signature."""

        super().__init__(*args, **kwargs)
        self.__message = super().message
        self.__traces = super().traces

    @property
    def message(self) -> Optional[str]:
        """
        Get the detail message of this exception.

        Returns
        -------
        str or None :
            The detail message of this exception. If not specified, returns ``None``.

        """
        return self.__message

    @property
    def traces(self) -> _tb.StackSummary:
        """
        Get the stack traces of this exception.

        Returns
        -------
        traceback.StackSummary :
            The stack traces of this exception. If not specified, returns
            the stack traces from `traceback.extract_stack()`.

        """
        return self.__traces



__author__       = AUTHOR
__version__      = VERSION
__version_info__ = VERSION_INFO


# Delete imported objects that are no longer being used
del AUTHOR, VERSION, VERSION_INFO, Any, Optional
