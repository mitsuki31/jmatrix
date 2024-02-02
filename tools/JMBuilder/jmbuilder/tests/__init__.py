"""Test module.

Copyright (c) 2023-2024 Ryuu Mitsuki.
"""

from . import test_utils
from .._globals import AUTHOR, VERSION, VERSION_INFO

__all__ = ['test_utils']

__author__       = AUTHOR
__version__      = VERSION
__version_info__ = VERSION_INFO

# Remove imported objects that are no longer used
del AUTHOR, VERSION, VERSION_INFO
