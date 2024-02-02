"""JMBuilder's core module.

Copyright (c) 2023-2024 Ryuu Mitsuki.
"""

import os as _os
import sys as _sys
import re as _re
from datetime import datetime as _dt, timezone as _tz
from typing import Dict, List, Optional, Union, TextIO
from warnings import warn as __warn
import bs4 as _bs4

from . import utils as _jmutils
from . import exception as _jmexc

try:
    from ._globals import AUTHOR, VERSION, VERSION_INFO
except (ImportError, ModuleNotFoundError, ValueError):
    from pathlib import Path

    # Add a new Python search path to the first index
    _sys.path.insert(0, str(Path(_sys.path[0]).parent))
    del Path

    from jmbuilder._globals import AUTHOR, VERSION, VERSION_INFO

CORE_ERR: _jmexc.JMException = _jmexc.JMException(
    _os.linesep + '  CORE ERROR: An error occurred in core module.')

__all__ = ['PomParser', 'JMRepairer']

class PomParser:
    """
    A class that provides an easy way to parse and retrieve useful
    information from the provided POM file.

    Parameters
    ----------
    soup : BeautifulSoup
        A `bs4.BeautifulSoup` object representing the parsed POM file.

    """

    def __init__(self, soup: _bs4.BeautifulSoup) -> 'PomParser':
        """Create a new instance of ``PomParser`` class."""
        if not isinstance(soup, _bs4.BeautifulSoup):
            # Raise an error
            raise TypeError(f'Invalid instance class: {soup.__class__}') \
                from CORE_ERR

        self.soup: _bs4.BeautifulSoup = soup
        self.project_tag: _bs4.element.Tag = soup.find('project')

    @staticmethod
    def parse(pom_file: str, encoding: str = 'UTF-8') -> 'PomParser':
        """
        Parse the POM file (``pom.xml``) and return an instance of
        this class. Remove comments and blank lines to keep the POM clean.

        Parameters
        ----------
        pom_file : str
            The path of the pom.xml file to be parsed.

        encoding : str, optional
            The encoding used while parsing the pom.xml file. Defaults to UTF-8.

        Returns
        -------
        PomParser :
            An instance of this class.

        """

        try:
            # Read and convert the pom.xml file to BeautifulSoup object
            soup: _bs4.BeautifulSoup = _bs4.BeautifulSoup(
                ''.join(_jmutils.readfile(pom_file, encoding=encoding)), 'xml')

            # Find the comments using lambda, then extract them
            for element in soup(text=lambda t: isinstance(t, _bs4.Comment)):
                element.extract()
        except Exception as exc:
            raise exc from CORE_ERR

        # Return the instance of this class
        return PomParser(soup)

    def printsoup(self, *, pretty: bool = True, file: TextIO = _sys.stdout) -> None:
        """
        Print the ``BeautifulSoup`` object, optionally prettified, for debugging purposes.

        Parameters
        ----------
        pretty : bool, optional
            If True, the BeautifulSoup object will be prettified for better readability.
            Defaults to True.

        file : TextIO, optional
            A file-like object to which the output will be printed.
            Defaults to ``sys.stdout``.

        Notes
        -----
        This method is intended for debugging and allows you to print the
        current state of the ``BeautifulSoup`` object. The output can be customized
        with the `pretty` parameter to control prettification and the `file`
        parameter to redirect the output to a specific file-like object.

        Example
        -------
        # Print the soup to the console standard output
        >>> soup_instance.printsoup()

        # Print and save the soup to the specified file
        >>> with open('output.xml', 'w') as f:
        ...     soup_instance.printsoup(pretty=True, file=f)

        """
        print(self.soup.prettify() if pretty else str(self.soup).strip(), file=file)

    def get(self, key: Union[str, List[str]]) -> Optional[_bs4.element.Tag]:
        """
        Find the element tag based on the provided key, which can be a string
        (separated by dots) or a list of tag names. The result could be a None,
        this means that element are undefined or the users has specified wrong
        element tree path.

        Parameters
        ----------
        key : str or a list of str
            The key representing the element tree path.

        Returns
        -------
        Tag or None :
            A ``bs4.element.Tag`` object representing the desired element tag,
            or ``None`` if the element tag is undefined or cannot be found.

        """

        # Split the key if the key is a string
        keys: List[str] = key.split('.') if isinstance(key, str) else key

        # Find the element according to the first key
        result: _bs4.element.Tag = self.soup.find(keys[0])
        for k in keys[1:]:
            # Break the loop if the result is None
            if not result:
                break
            result = result.find(k)

        return result

    def get_name(self) -> Optional[str]:
        """Return the project name."""
        # => project.name
        name_element: _bs4.element.Tag = self.project_tag.find('name')
        return name_element.text if name_element else name_element

    def get_version(self) -> Optional[str]:
        """Return the project version."""
        # => project.version
        version_element: _bs4.element.Tag = self.project_tag.find('version')
        return version_element.text if version_element else version_element

    def get_id(self) -> Dict[str, Optional[str]]:
        """Return a dictionary with 'groupId' and 'artifactId'."""
        id_element: List[Optional[_bs4.element.Tag]] = [
            self.project_tag.find('groupId'),    # => project.groupId
            self.project_tag.find('artifactId')  # => project.artifactId
        ]

        return {  # Return a dictionary
            'groupId': id_element[0].text if id_element[0] else id_element[0],
            'artifactId': id_element[1].text if id_element[1] else id_element[1]
        }

    def get_url(self) -> Optional[str]:
        """Return the project URL."""
        # => project.url
        url_element: _bs4.element.Tag = self.project_tag.find('url')
        return url_element.text if url_element else url_element

    def get_inception_year(self) -> Optional[str]:
        """Return the project inception year."""
        # => project.inceptionYear
        inc_year_element: _bs4.element.Tag = self.project_tag.find('inceptionYear')
        return inc_year_element.text if inc_year_element else inc_year_element

    def get_author(self) -> Dict[str, Optional[str]]:
        """Return a dictionary with 'id', 'name', and 'url' of the project author."""
        key: str = 'project.developers.developer'
        author_element: List[Optional[_bs4.element.Tag]] = [
            self.get(key + '.id'),    # => project.developers[0].developer.id
            self.get(key + '.name'),  # => project.developers[0].developer.name
            self.get(key + '.url')    # => project.developers[0].developer.url
        ]

        return {  # Return a dictionary
            'id': author_element[0].text if author_element[0] else author_element[0],
            'name': author_element[1].text if author_element[1] else author_element[1],
            'url': author_element[2].text if author_element[2] else author_element[2],
        }

    def get_license(self) -> Dict[str, str]:
        """Return a dictionary with 'name', 'url', and 'distribution' of the project license."""
        key: str = 'project.licenses.license'
        license_element: List[Optional[_bs4.element.Tag]] = [
            self.get(key + '.name'),         # => project.licenses[0].license.name
            self.get(key + '.url'),          # => project.licenses[0].license.url
            self.get(key + '.distribution')  # => project.licenses[0].license.distribution
        ]

        return {
            'name': license_element[0].text if license_element[0] else license_element[0],
            'url': license_element[1].text if license_element[1] else license_element[1],
            'distribution': license_element[2].text if license_element[2] else license_element[2],
        }

    def get_property(self, key: str, dot: bool = True) -> Optional[str]:
        """
        Return the value of the specified property key from the POM properties.

        Parameters
        ----------
        key : str
            The property key.

        dot : bool, optional
            If True, split the key using dots. Defaults to True.

        Returns
        -------
        str or None :
            The property value if found, otherwise, returns None.

        Raises
        ------
        ValueError :
            If the provided key is an empty string or None.

        """
        # Raise an error if the provided key is an empty string or None
        if not (key or len(key)):
            raise ValueError('Key argument cannot be empty.')

        # Remove the 'properties' string tag
        key = key.replace('properties.', '') \
            if key.startswith('properties.') else key

        # Only split the dots if 'dot' argument enabled
        keys: List[str] = key.split('.') if dot else [key]
        # Add the prefix of 'properties' element tag
        if not dot or (dot and keys[0] != 'properties'):
            keys.insert(0, 'properties')  # Append to the first index

        # This way, we can prevent an error due to NoneType use
        result: _bs4.element.Tag = self.get(keys)
        return result.text if result else result


class JMRepairer:
    """
    A class for repairing manifest and properties files using information
    from a POM file.

    Parameters
    ----------
    pom : str, PomParser, or _bs4.BeautifulSoup
        The POM file, either as a path (str), a `PomParser` instance,
        or a `BeautifulSoup` object.

    Raises
    ------
    ValueError
        If the 'pom' argument is empty.

    TypeError
        If the type of 'pom' argument is unknown, neither of str,
        a `PomParser` instance, nor a `BeautifulSoup` object.

    Attributes
    ----------
    _val_pattern : re.Pattern
        Regular expression pattern for extracting values from curly
        braces in strings.

    _soup : PomParser
        Instance of `PomParser` representing the parsed POM file.

    _pom_items : Dict[str, str (could possibly None)]
        Dictionary containing key-value pairs extracted from the POM file.

    """

    def __init__(self, pom: Union[str, PomParser, _bs4.BeautifulSoup]) -> 'JMRepairer':
        """Create a new instance of this class."""
        if not pom:
            raise ValueError("Argument 'pom' cannot be empty") \
                from CORE_ERR

        if not isinstance(pom, (str, PomParser, _bs4.BeautifulSoup)):
            raise TypeError(f"Unknown type of 'pom' argument: {type(pom).__name__}") \
                from CORE_ERR

        self._val_pattern: _re.Pattern = _re.compile(r'\$\{([\w.-\[\]]+)\}')
        self._soup: PomParser = None

        if isinstance(pom, str):
            self._soup = PomParser.parse(pom)  # Need to be parsed first
        elif isinstance(pom, _bs4.BeautifulSoup):
            self._soup = PomParser(pom)        # Pass directly to the constructor
        elif isinstance(pom, PomParser):
            self._soup = pom                   # Already an instance of PomParser

        project_id: Dict[str, Optional[str]] = self._soup.get_id()
        project_author: Dict[str, Optional[str]] = self._soup.get_author()
        project_license: Dict[str, Optional[str]] = self._soup.get_license()

        self._pom_items: Dict[str, Optional[str]] = {
            'project.name': self._soup.get_name(),
            'project.version': self._soup.get_version(),
            'project.url': self._soup.get_url(),
            'project.groupId': project_id['groupId'],
            'project.artifactId': project_id['artifactId'],
            'project.inceptionYear': self._soup.get_inception_year(),
            'project.developers[0].name': project_author['name'],
            'project.developers[0].url': project_author['url'],
            'project.licenses[0].name': project_license['name'],
            'project.licenses[0].url': project_license['url'],
            'package.licenseFile': self._soup.get_property('package.licenseFile', dot=False),
            'package.mainClass': self._soup.get_property('package.mainClass', dot=False),
            'maven.build.timestamp': _dt.now(_tz.utc).strftime('%Y-%m-%dT%H:%M:%SZ')
        }

    @classmethod
    def __write_out(cls, contents: List[str], out: str) -> None:
        """
        Write the given contents to the specified output file.

        Parameters
        ----------
        contents : a list of str
            List of strings to be written to the file.

        out : str
            Path to the output file.

        Raises
        ------
        Exception
            If an error occurs while writing to the output file.
    
        """

        parentdir: str = _os.path.dirname(out)
        if not _os.path.exists(parentdir):
            _os.mkdir(parentdir)

        try:
            with open(out, 'w', encoding='UTF-8') as o_file:
                for line in contents:
                    o_file.write(f'{line}{_os.linesep}')
        except Exception as e:
            raise e from CORE_ERR

    def fix_manifest(self, infile: str, outfile: str = None) -> None:
        """
        Fix the given manifest file by replacing placeholders with values
        from the POM file.

        Parameters
        ----------
        infile : str
            Path to the input manifest file.

        outfile : str, optional
            Path to the output manifest file. If not specified,
            the input file will be overwritten.

        Raises
        ------
        ValueError
            If the 'infile' argument is empty.

        FileNotFoundError
            If the specified input file does not exist.

        """

        if not infile:
            raise ValueError("Argument 'infile' cannot be empty") \
                from CORE_ERR

        if not _os.path.exists(infile):
            raise FileNotFoundError(f'Cannot read non-existing file: {infile!r}') \
                from CORE_ERR

        # When outfile argument not specified, then use infile
        # for the name of output file, which means will overwrite the infile
        outfile = infile if not outfile else outfile

        manifest: _jmutils.JMProperties = _jmutils.JMProperties(infile)

        # Fix the manifest
        for key, val in manifest.items():
            new_val = self._val_pattern.match(val)
            if not new_val:
                continue

            new_val = new_val[1]
            if key == 'ID':
                manifest[key] = f"{self._pom_items['project.groupId']}:" + \
                    f"{self._pom_items['project.artifactId']}"
            elif new_val in self._pom_items:
                manifest[key] = self._pom_items[new_val]

        self.__write_out(
            [f'{key}: {val}' for key, val in manifest.items()] + [''],
            out=outfile
        )

    def fix_properties(self, infile: str, outfile: str = None) -> None:
        """
        Fix the given properties file by replacing placeholders with values
        from the POM file.

        Parameters
        ----------
        infile : str
            Path to the input properties file.

        outfile : str, optional
            Path to the output properties file. If not specified,
            the input file will be overwritten.

        Raises
        ------
        ValueError
            If the 'infile' argument is empty.

        FileNotFoundError
            If the specified input file does not exist.

        """

        if not infile:
            raise ValueError("Argument 'infile' cannot be empty") \
                from CORE_ERR

        if not _os.path.exists(infile):
            raise FileNotFoundError(f'Cannot read non-existing file: {infile!r}') \
                from CORE_ERR

        # If the outfile argument were not specified, then use infile
        # for the name of output file, which means will overwrite the infile
        outfile = infile if not outfile else outfile

        # Parse the properties file
        properties: _jmutils.JMProperties = _jmutils.JMProperties(infile)

        # Fix the properties file
        for key, val in properties.items():
            new_val = self._val_pattern.match(val)
            if not new_val:
                continue

            new_val = new_val[1]
            if new_val in self._pom_items:
                properties[key] = self._pom_items[new_val]

        self.__write_out(
            [f'{key} = {val}' for key, val in properties.items()],
            out=outfile
        )


__author__       = AUTHOR
__version__      = VERSION
__version_info__ = VERSION_INFO

# Delete unused variables
del AUTHOR, VERSION, VERSION_INFO
del Dict, List, Union, Optional, TextIO

if __name__ == '__main__':
    __warn(
'''You are attempting to run this module directly (i.e. as main module), \
which is not permitted. It is designed to be imported, not as main module.''')
    _sys.exit()
