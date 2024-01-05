"""Custom Logger Module for JMBuilder

This module provides a custom logger utility that initializes and creates a new
`Logger` object for logging information or errors to the console or a log file.

To use the custom logger in your project, you can import the `init_logger` function
from this module and create a new `Logger` object with desired settings.

Copyright (c) 2023-2024 Ryuu Mitsuki.


Available Functions
-------------------
init_logger
    Initialize and create new `Logger` object for logging any
    information or errors to file, if specified, otherwise the output
    will be written to console standard error.

Available Constants
-------------------
BASIC_FORMAT : str
    The basic (default) format for `logging.Formatter`.
    This is alias for `logging.BASIC_FORMAT`.

CUSTOM_FORMAT : str
    The custom format for `logging.Formatter`.

CRITICAL : int
    The integer value representation of logging level 50.
    This is alias for `logging.CRITICAL`.

DEBUG : int
    The integer value representation of logging level 10.
    This is alias for `logging.DEBUG`.

ERROR : int
    The integer value representation of logging level 40.
    This is alias for `logging.ERROR`.

FATAL : int
    The integer value representation of logging level 50.
    This is alias for `logging.FATAL`.

INFO : int
    The integer value representation of logging level 20.
    This is alias for `logging.INFO`.

NOTSET : int
    The integer value of representation of logging level 0.
    This is alias for `logging.NOTSET`.

WARN : int
    The integer value of representation of logging level 30.
    This is alias for `logging.WARN`.

WARNING : int
    The integer value of representation of logging level 30.
    This is alias for `logging.WARNING`.

Example
-------
# Import the module
>>> from jmbuilder import logger

# Create a new logger object
>>> log = logger.init_logger(fmt=logger.CUSTOM_FORMAT,
...                          level=logger.INFO)
>>> log
<RootLogger root (INFO)>
>>> type(log).__name__
'RootLogger'

# Log some information message
# The output will be printed to console standard error (stderr),
# because the `filename` are not defined on `init_logger`.
>>> log.info('This is an information message.')
This is an information message.

# Log some exception
# To trace the error, pass any true value to `exc_info` argument.
>>> try:
...     x = 3 / 0
... except ZeroDivisionError as div_err:
...     log.error('An error occurred.', exc_info=1)
An error occurred.
Traceback (most recent call last):
  ...
ZeroDivisionError: division by zero
"""

import os as _os
import sys as _sys
import logging as _log
from typing import Union

from .._globals import AUTHOR, VERSION, VERSION_INFO, STDERR
from ..exception import JMUnknownTypeError as _JMTypeError


__all__ = ['init_logger']
__author__ = AUTHOR
__version__ = VERSION
__version_info__ = VERSION_INFO


# References of formatter
BASIC_FORMAT  = _log.BASIC_FORMAT
CUSTOM_FORMAT = '%(asctime)s - %(name)s - %(levelname)s - %(message)s'

# References of logging levels
NOTSET   = _log.NOTSET      # 0
DEBUG    = _log.DEBUG       # 10
INFO     = _log.INFO        # 20
WARN     = _log.WARN        # 30
WARNING  = _log.WARNING     # 30
ERROR    = _log.ERROR       # 40
CRITICAL = _log.CRITICAL    # 50
FATAL    = _log.FATAL       # 50


def init_logger(filename: str = None, *, fmt: Union[str, _log.Formatter] = None,
                level: int = DEBUG) -> _log.Logger:
    """
    Initializes and creates a new `Logger` object.

    Parameters
    ----------
    filename : str or None, optional
        A string representing the name of the logger file. If specified,
        logs will be written to the specified file, otherwise logs
        will be printed to `stderr` (standard error). Default is ``None``.

    fmt : str or logging.Formatter, optional
        A string representation of the log formatter or an object
        of `logging.Formatter` class. If not specified, a customized
        formatter will be used. See ``CUSTOM_FORMAT`` constant variable.

    level : int, optional
        An integer value that specifies the logging level for the logger.
        Default is ``logger.DEBUG`` (equal to 10).

    Returns
    -------
    logging.Logger :
        A new `Logger` object for logging any information or errors.

    Raises
    ------
    JMUnknownTypeError :
        If the 'fmt' are not instance of `str` or `logging.Formatter` class.

    """

    handler: _log.Handler = None

    # Check whether the 'filename' are specified
    if not filename:
        handler = _log.StreamHandler(STDERR)
    else:
        if not isinstance(filename, str):
            filename = str(filename)

        if isinstance(filename, str) and not filename.endswith('.log'):
            filename += '.log'

        # Check whether the parent directory of 'filename' exist
        if not _os.path.exists(_os.path.dirname(filename)):
            # Create the directory if not exist
            _os.mkdir(_os.path.dirname(filename))
        handler = _log.FileHandler(filename)

    # Check whether the 'fmt' as log formatter are specified
    if not fmt:
        handler.setFormatter(_log.Formatter(CUSTOM_FORMAT))
    elif fmt and isinstance(fmt, (str, _log.Formatter)):
        if isinstance(fmt, str):
            handler.setFormatter(_log.Formatter(fmt))
        else:
            handler.setFormatter(fmt)
    else:
        raise _JMTypeError(
            f'Invalid type of `fmt`: "{type(fmt).__name__}". ' + \
            'Expected "str" and "logging.Formatter"')

    logger = _log.getLogger(
        _os.path.basename(filename)
    ) if filename else _log.getLogger('JMBuilder log')
    logger.setLevel(level)      # set the logger level, default is DEBUG
    logger.addHandler(handler)  # set the handler

    return logger


# Remove unnecessary variables
del AUTHOR, VERSION, VERSION_INFO, Union
