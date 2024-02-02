"""Exception Module for JMBuilder

This module contains several custom exception for `JMBuilder`.

Copyright (c) 2023-2024 Ryuu Mitsuki.


Available Classes
-----------------
JMException
    This is base exception class for all custom exception classes in
    this module and this class extends to `Exception` class.

JMUnknownTypeError
    This class extends to `JMException` and raised when an unknown type
    error occurs during the execution of the package.
"""

from . import exception
from .exception import *
from .._globals import AUTHOR, VERSION, VERSION_INFO

__all__ = ['exception']
__all__.extend(exception.__all__)



__author__       = AUTHOR
__version__      = VERSION
__version_info__ = VERSION_INFO

# Delete imported objects that are no longer being used
del AUTHOR, VERSION, VERSION_INFO
