"""Global Module for `JMBuilder`

This module contains all global variables, constants, and classes
used by the `JMBuilder` module. It provides access to various path
variables and other configurations used throughout the module.

Copyright (c) 2023-2024 Ryuu Mitsuki.


Available Classes
-----------------
JMSetupConfRetriever
    This class provide an easy way to retrieve and access the setup
    configurations for `JMBuilder` module.

Available Constants
-------------------
AUTHOR : str
    The author name (as well as the creator) of the `JMBuilder` module.

BASEDIR : str
    Provides the path to the base directory of the `JMBuilder` package.

    This is an alias for `_JMCustomPath().basedir`.

CONFDIR : str
    Provides the path to the directory that contains configuration
    files for configuring the `JMBuilder` module. The path itself
    is relative to `BASEDIR`.

    This is an alias for `_JMCustomPath().confdir`.

LOGSDIR : str
    Provides the path to the directory used for logging by the
    `JMBuilder` module. The path itself is relative to `BASEDIR`.

    This is an alias for `_JMCustomPath().logsdir`.

STDOUT : TextIO
    An alias for `sys.stdout`, representing the standard output console.

STDERR : TextIO
    An alias for `sys.stderr`, representing the standard error console.

TMPDIR : str or pathlib.Path
    Provides the path to the temporary directory used by the
    `JMBuilder` module. The path itself is relative to `BASEDIR`.

    This is an alias for `_JMCustomPath().tmpdir`.

VERSION : str
    A string representing the JMBuilder's version information.

"""

import os as _os
import sys as _sys
import json as _json
import collections as _collections
from pathlib import Path as _Path
from typing import (
    TextIO,
    Type,
    Union
)

if '_global_imported' in globals():
    raise RuntimeError(
        "Cannot import the '_globals' module more than once.")
_global_imported: bool = True

class _JMCustomPath:
    """
    Custom class to manage read-only path variables for `JMBuilder` module.

    This class provides read-only properties for common path variables
    such as `basedir`, `tmpdir`, `logsdir` and more.

    Parameters
    ----------
    _type : Type[str] or Type[pathlib.Path], optional
        The class type used for casting the path variables. Defaults to
        Python built-in string class (`str`).

        This parameter only supported the following types:
            - `str`
            - `pathlib.Path`

    Raises
    ------
    TypeError
        If `_type` is not a class of `str` neither `pathlib.Path`.

    Notes
    -----
    The path variables are read-only properties, and attempts to modify
    them will raise an `AttributeError`.

    Examples
    --------
      # Use `pathlib.Path` to cast the path
      >>> _JMCustomPath(Path).tmpdir
      PosixPath('.../tmp')  # if Windows, it will be `WindowsPath`

      # `str` type is the default value
      >>> _JMCustomPath().basedir
      '/path/to/base/directory'

      # User can also check the class type that currently
      # used for casting
      >>> _JMCustomPath(Path).type
      <class 'pathlib.Path'>

      # Attempt to modify the value will cause an error
      >>> _JMCustomPath().basedir = '/path/to/another/directory'
      Traceback (most recent call last):
        ...
      AttributeError: can't set attribute

    """

    def __init__(self, _type: Union[Type[str], Type[_Path]] = str):
        """Initialize self. See ``help(type(self))``, for accurate signature."""
        self.__type = _type
        err = 'Invalid type of `_type`: %r. ' + \
              'Expected "str" and "pathlib.Path"'

        if not isinstance(_type, type):
            err = TypeError(err % type(_type).__name__)
            raise err

        if isinstance(_type, type) and \
                 _type.__name__ not in ('str', 'Path', 'pathlib.Path'):
            err = TypeError(err % _type.__name__)
            raise err

        self.__basedir: str = self.__type(str(_Path(__file__).resolve().parent))
        self.__tmpdir:  str = self.__type(_os.path.join(self.__basedir, 'tmp'))
        self.__logsdir: str = self.__type(_os.path.join(self.__basedir, 'logs'))
        self.__confdir: str = self.__type(_os.path.join(self.__basedir, '.config'))

    def __repr__(self) -> str:
        """
        Return the string represents the class name and the class type.

        Returns
        -------
        str
            A string representation of this class.
        """
        return f'{self.__class__.__name__}(type: {self.__type.__name__!r})'

    @property
    def basedir(self) -> Union[str, _Path]:
        """
        The JMBuilder's base directory path based on parent directory of this file.

        Returns
        -------
        str or pathlib.Path
            The current working directory path.
        """
        return self.__basedir

    @property
    def tmpdir(self) -> Union[str, _Path]:
        """
        The path to 'tmp' directory relative to `basedir`.

        Returns
        -------
        str or pathlib.Path
            The path to temporary directory.
        """
        return self.__tmpdir

    @property
    def logsdir(self) -> Union[str, _Path]:
        """
        The path to 'logs' directory relative to `basedir`.

        Returns
        -------
        str or pathlib.Path
            The path to logs directory.
        """
        return self.__logsdir

    @property
    def confdir(self) -> Union[str, _Path]:
        """
        The path to '.config' directory relative to `basedir`.

        Returns
        -------
        str or pathlib.Path
            The path to the specified directory that contains configuration
            files for configuring `JMBuilder` module.
        """
        return self.__confdir

    @property
    def type(self) -> Union[Type[str], Type[_Path]]:
        """
        Return the current class type for casting the path.

        Returns
        -------
        Type[str] or Type[pathlib.Path]
            The class type.
        """
        return self.__type


class JMSetupConfRetriever:
    """
    A class that retrieves and provides all setup configuration.

    Notes
    -----
    This class only retrieves the setup configuration without any modification
    methods to their values.

    """

    def __init__(self):
        """Initialize self."""

        setupfile: str = _os.path.join(_JMCustomPath(str).confdir, 'setup.json')
        # Get the properties
        configs: dict = {}
        with open(setupfile, 'r', encoding='utf-8') as setup_file:
            configs = _json.load(setup_file)

        # Create an empty named tuple
        frozen_ver = _collections.namedtuple(
            'FrozenJMVersion',
            ['major', 'minor', 'patch'],
            module=__package__
        )

        # Change the named tuple's documentation
        frozen_ver.__doc__ = f'{configs.get("Program-Name")}\'s ' + \
            'version information as a frozen named tuple.'

        key_names: list = ['progname', 'version', 'author', 'license']
        self.__jmsetup_data: dict = dict(zip(key_names, configs.values()))

        # Convert the version info to FrozenJMVersion (a named tuple)
        self.__jmsetup_data['version'] = frozen_ver(*self.__jmsetup_data['version'])

    @property
    def progname(self) -> str:
        """
        Get the program name from setup configuration.

        Returns
        -------
        str :
            A string representing the program name.

        """
        return self.__jmsetup_data['progname']

    @property
    def version(self) -> 'FrozenJMVersion':
        """
        Get the module version from setup configuration.

        Returns
        -------
        FrozenJMVersion :
            A frozen named tuple representing the module version.

        """
        return self.__jmsetup_data['version']

    @property
    def author(self) -> str:
        """
        Get the author name from setup configuration.

        Returns
        -------
        str :
            A string representing the author name.

        """
        return self.__jmsetup_data['author']

    @property
    def license(self) -> str:
        """
        Get the license name from setup configuration.

        Returns
        -------
        str :
            A string representing the license name.

        """
        return self.__jmsetup_data['license']


# Aliases
__jmsetup__ = JMSetupConfRetriever()
__jmpath__  = _JMCustomPath()

BASEDIR: Union[str, _Path] = __jmpath__.basedir
TMPDIR:  Union[str, _Path] = __jmpath__.tmpdir
LOGSDIR: Union[str, _Path] = __jmpath__.logsdir
CONFDIR: Union[str, _Path] = __jmpath__.confdir

STDOUT: TextIO = _sys.stdout
STDERR: TextIO = _sys.stderr

AUTHOR:       str = __jmsetup__.author
VERSION:      str = '.'.join(map(str, __jmsetup__.version))
VERSION_INFO: str = __jmsetup__.version

__author__        = AUTHOR
__version__       = VERSION
__version_info__  = VERSION_INFO


__all__ = [
    'BASEDIR', 'CONFDIR',
    'LOGSDIR', 'TMPDIR',
    'STDOUT', 'STDERR',
    'JMSetupConfRetriever'
]


# Remove unnecessary variables
del Type, TextIO, Union
