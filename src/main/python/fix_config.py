'''
This program will fix the JMatrix configurations for specified file(s).

File(s):
    - MANIFEST.MF
    - Makefile
    - config.xml

Created by Ryuu Mitsuki
'''

import os, sys, json
from typing import Optional
from bs4 import BeautifulSoup
from bs4.formatter import XMLFormatter
from utils import raise_error, info_msg, convert_to_BS

# Global Variables #
__SOURCES_PATH:   str = f'src{os.sep}main{os.sep}'
__TARGET_PATH:    str = f'target{os.sep}'
__CLASSES_PATH:   str = __TARGET_PATH + f'classes{os.sep}'
__CACHE_PATH:     str = __TARGET_PATH + f'.cache{os.sep}'
__RESOURCES_PATH: str = __SOURCES_PATH + f'resources{os.sep}'

__TARGET_FILES: tuple = ('config.xml', 'Makefile', 'MANIFEST.MF',)

def __check_directory(dir: str, verbose: bool = False) -> None:
    """
    Check if a directory exists and create it if it does not.

    Parameters:
        - dir: str
            Directory path to be checked.

        - verbose: bool
            Whether to print detailed messages while checking or creating the directory.

    Returns:
        None
    """
    if verbose:
        print(os.linesep + '>>> [ CHECK DIRECTORY ] <<<')
    if not os.path.exists(dir):
        if verbose:
            info_msg(f'Creating new directory... (\'{dir}\')')
        os.makedirs(dir)
        if verbose:
            info_msg(f'Successfully create "{dir}" directory')
    else:
        if verbose:
            info_msg(f'"{dir}" directory is already exists')


def __create_cache(data: Optional[dict], indent: int = 4, out: str = None, verbose: bool = False) -> None:
    """
    Create the cache of `data` and store it to `target/.cache/` directory with specified file name.
    The function supports creating two types of cache (JSON and string). For JSON cache,
      the `data` parameter should be a dictionary object.

    Parameters:
        - data: Optional[dict]
            Data object that want to be cached.
            The type is optional, can be 'dict' or 'str'.
            Recommended use 'dict' object for creating JSON cache.

        - indent: int (default = 4)
            An integer value that specifies number of spaces for indentation in the JSON cache.

        - out: str (default = None)
            String representing the name of the output file.
            The output directory is `target/.cache/` directory.

        - verbose: bool (default = False)
            Boolean that specifies whether to print verbose output.

    Returns:
        None

    Raises:
        - RuntimeError
            If `out` parameter is empty.
    """
    __check_directory(__CACHE_PATH, verbose=verbose)

    if verbose:
        print(os.linesep + '>>> [ CREATE CACHE ] <<<')
        info_msg(f'Creating cache for id:<{id(data)}> to "{out}"...')
    try:
        if out is None:
            msg = 'Please insert name for output file'
            raise RuntimeError(msg)
    except RuntimeError as re:
        raise_error(re, -1, file=__file__)

    if isinstance(data, str):
        try:
            with open(__CACHE_PATH + out, 'w', encoding='utf-8') as cache:
                if verbose:
                    info_msg(os.linesep + f'Writing "{data.strip()}" -> \'{out}\'...')
                cache.write(data)
        except Exception as e:
            raise_error(e, file=__file__)

    elif isinstance(data, dict):
        try:
            with open(__CACHE_PATH + out, 'w', encoding='utf-8') as cache:
                if verbose:
                    print()
                    for k, v in data.items():
                        info_msg(f'Writing ("{k}", "{v}") -> \'{out}\'...')
                json.dump(data, cache, indent=indent)
                cache.write(os.linesep)
        except Exception as e:
            raise_error(e, file=__file__)
        else:
            if verbose:
                print()
                info_msg(f'Cache created, saved in "{__CACHE_PATH}{out}"')


def __get_pom_data(cache: bool = True, verbose: bool = False) -> Optional[dict]:
    """
    This function is used to retrieve configuration data from 'pom.xml'.

    Parameters:
        - cache: bool (default = True)
            Boolean that specifies whether to create a cache file for the retrieved data or not.
            If filename is 'config.xml' or any values that same, would be cached with file name 'config.json'.

        - verbose: bool (default = False)
            Boolean that specifies whether to print verbose output.

    Returns:
        If `cache` is False, the function returns a dictionary containing the configuration data.
        Otherwise, the function returns None.
    """
    def __check_existence(fp: str, verbose: bool = False) -> None:
        """
        Check if a file exists.

        Parameters:
            - fp: str
                The file path to be checked.

            - verbose: bool (default = False)
                If True, print verbose output.

        Returns:
            None

        Raises:
            - FileNotFoundError
                If the file does not exist or cannot be accessed.
        """
        if verbose:
            info_msg(f'Check existence for file: "{fp}"...')

        if os.path.exists(fp):
            if verbose:
                info_msg(f'File "{fp}" is exist.')
            return
        else:
            info_msg(f'File "{fp}" does not exist, error raised!' + os.linesep)
            try:
                msg = f'File "{fp}" does not exist or cannot be accessed'
                raise FileNotFoundError(msg)
            except FileNotFoundError as fe:
                raise_error(fe, 2, file=__file__)

    # ------------------------------------------- #

    pom_xml:     str  = 'pom.xml'
    config_data: dict = { }

    if verbose:
        print(os.linesep + '>>> [ GET CONFIG DATA ] <<<')

    try:
        from bs4 import BeautifulSoup
    except ModuleNotFoundError as me:
        info_msg('Please install all the requirements first.' + os.linesep)
        raise_error(me, file=__file__)

    __check_existence(pom_xml)
    bs_data: BeautifulSoup = None
    bs_data = convert_to_BS(pom_xml, verbose=verbose)

    if verbose:
        info_msg('Filtering contents...')
        print(os.linesep + '>> ' + '-'*30)
        print(os.linesep + '<contents>')

    for element in bs_data.find_all():
        for child in element.find_all():
            if child.name == 'properties':
                for child_1 in child.find_all():
                    if not child_1.name.startswith('project'):
                        config_data[child_1.name] = child_1.text.strip()
                        if verbose:
                            print(' '*4 + f'<{child_1.name}>', child_1.text.strip(), f'</{child_1.name}>')

    for elm in bs_data.find_all('groupId'):
        if elm.text.strip().split('.')[-1] in config_data['author.name'].lower():
            config_data['project.' + elm.name] = elm.text.strip()
            if verbose:
                print(' '*4 + f'<{elm.name}>', elm.text.strip(), f'</{elm.name}>')
    for elm in bs_data.find_all('artifactId'):
        if elm.text.strip().split('-')[0] == config_data['package.name'].lower():
            config_data['project.' + elm.name] = elm.text.strip()
            if verbose:
                print(' '*4 + f'<{elm.name}>', elm.text.strip(), f'</{elm.name}>')

    if verbose:
        print('</contents>' + os.linesep)
        print('>> ' + '-'*30 + os.linesep)
        info_msg('All contents filtered.')

    if cache:
        __create_cache(config_data, indent=4, out='config.json', verbose=verbose)
    else:
        return config_data


def __fix_configuration(data: dict = None, cached: str = None, target: str = None, verbose: bool = False) -> None:
    """
    Fix the configurations of specified target file.

    Parameters:
        - data: dict
            The dictionary contains new configuration data.

        - cached: str
            File path to the cached new configuration data.

        - target: str
            The target file that want to fix it's configurations.

        - verbose: bool
            Whether to print detailed messages while fixing the configurations.

    Returns:
        None

    Raises:
        - ValueError
            If the file path is empty.

        - FileNotFoundError
            If file path to cached configuration data does not exist.
    """
    with_cache:  bool = False
    target_path: str  = None
    target_data: object = None

    def __read_file(fp: str, _list: bool = True, verbose: bool = False) -> Optional[list]:
        """
        Read all contents from specified file, and specify the return to list or str

        Parameters:
            - fp: str
                The file path to read the contents.

            - _list: bool (default = True)
                If True it will returns the contents with type of list,
                if False it will returns the contents with type of str.

            - verbose: bool (default = False)
                Whether to print detailed messages while read the file contents.

        Returns:
            None

        Raises:
            - ValueError
                If the file path is empty.

            - FileNotFoundError
                If file path to read it's contents does not exist.
        """
        if verbose:
            info_msg(f'Retrieving all contents from "{fp}"...')
        try:
            if fp is None:
                msg = 'File path cannot be empty'
                raise ValueError(msg)
            elif not os.path.exists(fp):
                msg = f'File "{fp}" does not exist or cannot be accessed'
                raise FileNotFoundError(msg)

            with open(fp, 'r', encoding='utf-8') as file:
                if _list:
                    return file.readlines()
                else:
                    return file.read()
        except Exception as e:
            raise_error(e)
        else:
            if verbose:
                info_msg('Successfully retrieve all contents')

        return None


    def __write_to_file(fp: str, contents: object = None, verbose: bool = False) -> None:
        """
        Write the contents to specified file.

        Parameters:
            - fp: str
                The file path to write to.

            - contents: object (default = None)
                The contents to write to the file. Can be a `list`, `str`, or `dict`.

            - verbose: bool
                Whether to print detailed messages while writing to the file.

        Returns:
            None

        Raises:
            - ValueError
                If the file path or contents is empty.
        """
        try:
            if fp is None:
                msg = 'File path cannot be empty'
                raise ValueError(msg)
            elif contents is None:
                msg = 'Contents cannot be empty'
                raise ValueError(msg)

            with open(fp, 'w', encoding='utf-8') as file:
                if isinstance(contents, list):
                    for content in contents:
                        if verbose:
                            info_msg('Writing "{content}" -> \'{fp}\'')
                        file.write(content + os.linesep)
                elif isinstance(contents, str):
                    if verbose:
                        info_msg('Type: str')
                        info_msg(os.linesep + f'Writing "{contents}" -> \'{fp}\'')
                    file.write(contents)
                elif isinstance(contents, dict):
                    if verbose:
                        info_msg('Type: dict' + os.linesep)
                    file.write(json.dumps(contents, indent=4))
                    if verbose:
                        for var, val in contents.items():
                           info_msg(f'Writing ("{var}", "{val}") -> \'{fp}\'')

        except Exception as e:
            raise_error(e, file=__file__)
        else:
            if verbose:
                info_msg(f'Successfully write contents to file: "{fp}"')


    if verbose:
        print(os.linesep + '>>> [ FIX CONFIGURATION ] <<<')
    if data is None or len(data) == 0:
        with_cache = True
        if verbose:
            info_msg(f'Using cached configuration data "{cached}"')

    try:
        if (data is None or len(data) == 0) and cached is None:
            msg = 'Given config data is empty, please specify path to cached config'
            raise RuntimeError(msg)
        elif target is None or not target in __TARGET_FILES:
            msg = 'Please specify the target file'
            raise RuntimeError(msg)
        elif target in __TARGET_FILES:
            # config.xml
            if target == __TARGET_FILES[0]:
                target_path = __RESOURCES_PATH + f'configuration{os.sep}' + __TARGET_FILES[0]
            # Makefile
            elif target == __TARGET_FILES[1]:
                target_path = __TARGET_FILES[1]
            # MANIFEST.MF
            elif target == __TARGET_FILES[2]:
                target_path = f'META-INF{os.sep}' + __TARGET_FILES[2]
    except RuntimeError as re:
        raise_error(re, -1, file=__file__)

    if with_cache:
        try:
            if not os.path.exists(cached):
                msg = 'Path to cached configuration data does not exist'
                raise FileNotFoundError(msg)
            elif os.path.exists(cached) and not os.path.isfile(cached):
                msg = 'Path to cached configuration data is a directory'
                raise FileNotFoundError(msg)

            with open(cached, 'r', encoding='utf-8') as cache:
                data = json.load(cache)
        except Exception as e:
            raise_error(e, 1, file=__file__)

    output_dir: str = __CLASSES_PATH + f'resources{os.sep}'

    __check_directory(output_dir, verbose=verbose)
    if target in ('config_xml', 'config.xml'):
        import re

        target_data: dict = { }
        bs_data = repr(convert_to_BS(target_path, verbose=verbose))
        pattern: re.Pattern = re.compile(r"\$\{([\w.-]+)\}")

        target_value: list = pattern.findall(bs_data)

        for i in range(len(target_value)):
            target_data[target_value[i]] = data[target_value[i]]

        for var, val in target_data.items():
            bs_data = re.sub(fr'\${{{var}}}', val, bs_data)

        xml_formatter = XMLFormatter(indent=4)
        bs_data = BeautifulSoup(bs_data, 'xml').prettify(formatter=xml_formatter)

        with open(output_dir + 'config.xml', 'w', encoding='utf-8') as config:
            config.write(bs_data + os.linesep)

    elif target in ('Make', 'make', 'Makefile', 'makefile'):
        target_data = __read_file(target_path, verbose=verbose)
    


if __name__ == '__main__':
    verbose : bool  = False
    opts_arg: tuple = ('-v', '--verbose', 'verbose',)

    # Checking the CLI arguments
    try:
        msg: str = None  # Empty message variable for exception message

        if len(sys.argv) > 2:
            msg = 'Too many arguments, need (1) argument'
            raise IndexError(msg)
        elif len(sys.argv) == 2 and sys.argv[1] not in opts_arg:
            msg = f'Unknown options for value: "{sys.argv[1]}"'
            raise ValueError(msg)
    except Exception as e: # Catch all exceptions that could occurs
        raise_error(e, file=__file__)
    else:
        if len(sys.argv) == 2 and sys.argv[1] in opts_arg:
            verbose = True
        else:
            verbose = False

    __get_pom_data(cache=True, verbose=verbose)
    __fix_configuration(cached=f'{__CACHE_PATH}config.json', target='config.xml', verbose=verbose)
