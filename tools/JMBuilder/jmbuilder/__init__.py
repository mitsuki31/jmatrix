"""JMatrix Builder written in Python

JMBuilder, provides some utilities and configurations to build the `JMatrix`
library, the module are written in Python. Created by Ryuu Mitsuki.
For more information about `JMatrix` you can refer to link below.

    https://github.com/mitsuki31/jmatrix.git

Copyright (c) 2023-2024 Ryuu Mitsuki


Available Classes
-----------------
JMProperties
    A custom properties parser class, implemented in `jmbuilder.utils` package.
    Python does not provide an easy way to parsing files with extension of
    `.properties`, this class is designed to parse properties file and
    access their contents with ease without any third-party modules.

JMSetupConfRetriever
    A class that provides all configurations for setting up the `JMBuilder` module.

JMException
    This class is a base exception for `JMBuilder` module.

JMParserError
    This exception is raised when an error has occurred during parsing the
    configuration (e.g., properties file).

JMUnknownTypeError
    This exception is raised when an unknown type error occurs during
    the execution in this module.

JMRepairer
    A class for repairing manifest and properties files using information
    from a POM file.

PomParser
    A class that provides an easy way to parse and retrieve useful
    information from the provided POM file.

Available Functions
-------------------
json_parser
    Provide a simple way to parse the specified JSON file.

remove_comments
    Remove lines within the given list of strings that starting with the
    specified delimiter, and returning a new list of strings with the lines
    starting with the specified delimiter removed.

remove_blanks
    Remove blank or empty lines (including line with trailing whitespace)
    and `None` within the given list of strings, and returning a new list
    of strings with empty lines and `None` removed.


Available Constants
-------------------
BASEDIR
    Provides a string that represents the path to base directory
    of `JMBuilder` module.

LOGSDIR
    Provides a string that represents the path to logs directory
    that being used by this package to log some information and errors.

STDOUT
    References the console standard output (`sys.stdout`).

STDERR
    References the console standard error (`sys.stderr`).

TMPDIR
    Provides a string that represents the path to temporary directory
    that being used by this package to store some temporary file(s) or cache(s).

"""

# exception
from . import exception
from .exception import *

# utils
from . import utils
from .utils import *

# core
from . import core
from .core import *

# _globals
from . import _globals
from ._globals import *
from ._globals import AUTHOR, VERSION, VERSION_INFO


__all__ = []
__all__.extend(_globals.__all__)

# We all know it is a bad practice to use wildcard imports, right?
# But people are still using it for specific reasons, and knew the risks.
# Import statement here are no longer being used to prevent the accumulation
# of names after using wildcard imports. Instead, it exports only a few related modules
# without directly exporting the global classes and functions within those modules.
__all__.extend(['exception', 'utils', 'core'])

#__all__.extend(exception.__all__)
#__all__.extend(utils.__all__)


__author__       = AUTHOR
__version__      = VERSION
__version_info__ = VERSION_INFO


# Delete no longer used imported objects
del AUTHOR, VERSION, VERSION_INFO
