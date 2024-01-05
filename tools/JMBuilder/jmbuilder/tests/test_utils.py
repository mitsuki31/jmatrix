"""
Test suite for utilities members and properties parser, exclusively
for `jmbuilder.utils` submodule.

Copyright (c) 2023-2024 Ryuu Mitsuki.

"""

import os
import json
import unittest

from .._globals import AUTHOR, CONFDIR, VERSION, VERSION_INFO
from ..utils import utils as jmutils


class TestUtilities(unittest.TestCase):
    """Test class for utilities functions."""

    # The path reference to JSON config file
    jsonfile: str = os.path.join(CONFDIR, 'setup.json')

    def test_json_parser(self) -> None:
        """Test the `jmbuilder.utils.config.json_parser` function."""
        test_obj = jmutils.json_parser

        # Check the existence of config file
        self.assertTrue(os.path.exists(self.jsonfile))

        # Get the config data
        jsondata: dict = test_obj(self.jsonfile)
        self.assertIsNotNone(jsondata)
        self.assertIsInstance(jsondata, dict)

        jsondata_manual: dict = {}
        # Extract the JSON data manually and compare with the other one
        with open(self.jsonfile, 'r', encoding='utf-8') as file:
            jsondata_manual = json.load(file)

        self.assertDictEqual(jsondata, jsondata_manual)

    @unittest.skip(reason="The 'setupinit' are no longer available in 'jmbuilder.utils'")
    def test_setupinit(self) -> None:
        """
        Deprecated, due to the function of `setupinit` are no longer
        available in `jmbuilder.utils` package (i.e., has been removed).

        Test the `jmbuilder.utils.config.setupinit` function.
        """


    def test_remove_comments(self) -> None:
        """Test the `jmbuilder.utils.config.remove_comments` function."""
        test_obj = jmutils.remove_comments

        contents: list = [
            'Hello, world!',
            '# This is a comment, you know.',
            '! This is also a comment',
            'Foo And Bar'
        ]

        expected_contents: tuple = (
            [
                'Hello, world!',
                '! This is also a comment',
                'Foo And Bar'
            ],
            [
                'Hello, world!',
                '# This is a comment, you know.',
                'Foo And Bar'
            ],
            [
                'Hello, world!',
                'Foo And Bar'
            ]
        )

        expected_delimiters: tuple = ('#', '!')

        for i, delimiter in enumerate(expected_delimiters):
            self.assertListEqual(
                test_obj(contents, delim=delimiter),
                expected_contents[i]
            )

        self.assertListEqual(test_obj(
            test_obj(contents, delim=expected_delimiters[0]),
            delim=expected_delimiters[1]
        ), expected_contents[2])

    def test_remove_blanks(self) -> None:
        """Test the `jmbuilder.utils.config.remove_blanks` function."""
        test_obj = jmutils.remove_blanks

        contents: list = [
            '',      # blank line
            'Not blank line',
            '    ',  # line with trailing whitespace
            None
        ]

        expected_contents: tuple = (
            [
                'Not blank line',
                None
            ],
            [
                'Not blank line'
            ]
        )


        len_exp_contents: int = len(expected_contents)
        for i in range(len_exp_contents):
            self.assertListEqual(
                # Pass any numbers other than zero to `bool` will returning True value,
                # including negative numbers
                test_obj(contents, none=bool(i)), expected_contents[i]
            )


__author__     = AUTHOR
__version__    = VERSION
__version_info = VERSION_INFO


# Remove imported objects that are no longer used
del AUTHOR, VERSION, VERSION_INFO


if __name__ == '__main__':
    unittest.main()
