"""Utilities Module for JMBuilder

This module provide utilities for `JMBuilder` package.

Copyright (c) 2023-2024 Ryuu Mitsuki.
"""

from . import logger, utils
from .logger import *
from .utils import *

from .._globals import AUTHOR, VERSION, VERSION_INFO

__all__ = ['logger', 'utils']
__all__.extend(logger.__all__)
__all__.extend(utils.__all__)


__author__       = AUTHOR
__version__      = VERSION
__version_info__ = VERSION_INFO


# Delete imported objects that are no longer used
del AUTHOR, VERSION, VERSION_INFO
