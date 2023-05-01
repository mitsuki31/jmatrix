"""
Utilities for JMatrix's Python programs
"""
__author__ = 'Ryuu Mitsuki'
__all__ = ['Utils', 'FileUtils']

import os
import sys
import json
from typing import Optional, Union
from traceback import extract_stack

try:
    from bs4 import BeautifulSoup
except ModuleNotFoundError as bs_not_found:
    print('[jmatrix] Please install all the requirements first.')
    raise bs_not_found


class Utils:
    """
    Other utilities for all Python programs that supports `JMatrix`.

    All methods from this class is static.
    """
    @staticmethod
    def raise_error(ex: Exception, status: int = 1, file: str = None) -> None:
        """
        This function is used to print the error message, traceback,
          and exit the program with a given error code.

        Parameters:
            - ex: Exception
                The exception object to be printed.

            - status: int (default = 1)
                The exit status code of the program.

            - file: str (default = None)
                The file name where the error occurred.

        Returns:
            None

        Raises:
            None
        """
        if file is not None and len(file.split(os.sep)) != 1:
            file = file.split(os.sep)[-1]

        sys.stderr.write(
            os.linesep + \
            fr'/!\ ERROR{os.linesep}{">"*9}{os.linesep}'
        )

        if file is not None:
            sys.stderr.write(f'[jmatrix] An error occured in "{file}".{os.linesep}')
        else:
            sys.stderr.write(f'[jmatrix] An error occured.{os.linesep}')

        sys.stderr.write(f"{'>'*9} {ex} {'<'*9}{os.linesep*2}")

        tb_stack = extract_stack()
        tb_stack.pop(-1)

        sys.stderr.write(fr'/!\ TRACEBACK{os.linesep}{">"*13}{os.linesep}')
        for frame in tb_stack:
            sys.stderr.write(
                f'File "...{os.sep}' + \
                f'{(os.sep).join(frame.filename.split(os.sep)[-4:])}"{os.linesep}'
            )
            sys.stderr.write(
                '>' * 4 + ' ' * 6 + \
                f'at "{frame.name}", line {frame.lineno}' + \
                f'{os.linesep * 2 if tb_stack.index(frame) != len(tb_stack) - 1 else os.linesep}'
            )

            if tb_stack.index(frame) == len(tb_stack) - 1:
                sys.stderr.write(f'{type(ex).__name__}: {ex}{os.linesep}')

        sys.stderr.write(f'{os.linesep}Exited with error code: {status}{os.linesep}')

        if status != 0:
            sys.exit(status)


    @staticmethod
    def info_msg(message: str = None, prefix: str = 'jmatrix') -> None:
        """
        Print the message to standard output (stdout)
          with specified prefix.

        Parameters:
            - message: str (default = None)
                A string that want to be printed.

            - prefix: str (default = 'jmatrix')
                A string that specifies prefix.

        Returns:
            None

        Raises:
            None
        """
        print(f'[{prefix}] {message}')


    @staticmethod
    def convert_to_bs(filepath: str, _type: str = 'xml', verbose: bool = False) -> BeautifulSoup:
        """
        Takes a file path as input and returns the contents of
          the file in BeautifulSoup object form.

        Parameters:
            - filepath: str
                The file path of the file to be parsed.

            - _type: str (default = 'xml')
                The type of parser to be used.

            - verbose: bool (default = False)
                A boolean value that specifies whether
                  to print verbose output.

        Returns:
            A `BeautifulSoup` object containing the contents of the file.

        Raises:
            None
        """
        contents: str = FileUtils.read_file(filepath, _list=False, verbose=verbose)

        if verbose:
            Utils.info_msg('Contents converted to \'BeautifulSoup\' object.')

        return BeautifulSoup(contents, _type)


    @staticmethod
    def pr_banner(title: str, **kwargs) -> None:
        """
        Create and print the banner to standard output (stdout).

        Parameters:
            - title: str
                String to specifies the banner title.

        Returns:
            None

        Raises:
            None
        """
        print(os.linesep * kwargs.get('newline', 2) + \
            '-' * kwargs.get('lines', 80))
        print(f'>>> [ {title} ] <<< '.center(kwargs.get('center', 78)))
        print('-' * kwargs.get('lines', 80))


    # List all of public methods, it can be imported all with wildcard '*'
    __all__ = ['check_directory', 'check_file', 'read_file', 'write_to_file', 'pr_banner']



class FileUtils:
    """
    File utilities for all Python programs that supports `JMatrix`.

    All methods from this class is static.
    """
    @staticmethod
    def check_directory(dirpath: str, verbose: bool = False) -> None:
        """
        Check if a directory exists and create it if it does not exists.

        Parameters:
            - dirpath: str
                Directory path to be checked.

            - verbose: bool
                Whether to print detailed messages while checking or creating the directory.

        Returns:
            None

        Raises:
            None
        """
        if verbose:
            Utils.pr_banner(
                'CHECK DIRECTORY', lines=40, center=39, newline=1
            )
            Utils.info_msg(f'Checking directory "{dirpath}"...')
        if not os.path.exists(dirpath):
            if verbose:
                Utils.info_msg('Given directory path does not exist.')
                Utils.info_msg(f'Creating new directory (\'{dirpath}\')...')
            os.makedirs(dirpath)
            if verbose:
                Utils.info_msg(f'Successfully create "{dirpath}" directory.')
        else:
            if verbose:
                Utils.info_msg(f'"{dirpath}" directory is already exist.')
        if verbose:
            Utils.pr_banner(
                '(END) CHECK DIRECTORY',
                lines=40, center=39, newline=0
            )
            print()


    @staticmethod
    def check_file(filepath: str, verbose: bool = False) -> None:
        """
        Check if a file exists, if file does not exists it will raises an error.

        Parameters:
            - filepath: str
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
            Utils.pr_banner(
                'CHECK FILE', lines=40, center=39, newline=1
            )
            Utils.info_msg(f'Check existence for file: "{filepath}"...')

        if os.path.exists(filepath):
            if verbose:
                Utils.info_msg(f'File "{filepath}" is exist.')
            return

        Utils.info_msg(f'File "{filepath}" does not exist, error raised!' + os.linesep)
        try:
            msg = f'File "{filepath}" does not exist or cannot be accessed'
            raise FileNotFoundError(msg)
        except FileNotFoundError as not_found_err:
            Utils.raise_error(not_found_err, 2, file=__file__)

        if verbose:
            Utils.pr_banner(
                '(END) CHECK FILE',
                lines=40, center=39, newline=0
            )
            print()


    @staticmethod
    def read_file(filepath: str, _list: bool = True, verbose: bool = False) -> Optional[list]:
        """
        Read all contents from specified file,
          and specify the return to list or str.

        Parameters:
            - filepath: str
                The file path to read the contents.

            - _list: bool (default = True)
                If True it will returns the contents with type of list,
                  otherwise it will returns the contents with type of str.

            - verbose: bool (default = False)
                Whether to print detailed messages while
                  getting the file contents.

        Returns:
            None

        Raises:
            - ValueError
                If the file path is empty.

            - FileNotFoundError
                If file path to read it's contents does not exist.

            - PermissionError
                If the directory path of file is restricted and cannot
                  be accessed by program.
        """
        contents: Union[list, str] = None

        if verbose:
            Utils.pr_banner(
                'GET FILE CONTENTS', lines=40, center=39, newline=1
            )
            Utils.info_msg(f'Retrieving all contents from "{filepath}"...')

        try:
            if filepath is None:
                msg = 'File path cannot be empty'
                raise ValueError(msg)
            if not os.path.exists(filepath):
                msg = f'File "{filepath}" does not exist or cannot be accessed'
                raise FileNotFoundError(msg)

            with open(filepath, 'r', encoding='utf-8') as file:
                if _list:
                    contents = file.readlines()
                else:
                    contents = file.read()
        except ValueError as empty_path:
            Utils.raise_error(empty_path, 1, file=__file__)
        except FileNotFoundError as file_not_found:
            Utils.raise_error(file_not_found, 2, file=__file__)
        except PermissionError as perm_err:
            Utils.raise_error(perm_err, -1, file=__file__)

        if verbose:
            Utils.info_msg('Successfully retrieve all contents.')
            Utils.pr_banner(
                '(END) GET FILE CONTENTS',
                lines=40, center=39, newline=0
            )
            print()

        return contents


    @staticmethod
    def write_to_file(filepath: str,
            contents: Union[Union[list, dict], str] = None,
            verbose: bool = False, **kwargs) -> None:
        """
        Write the contents to specified file.
        If file has a contents inside, this method won't overwrites it
          instead creates with a new contents.

        Parameters:
            - filepath: str
                The file path to write the contents.

            - contents: Union[Union[list, dict], str] (default = None)
                The contents to write to the file.
                Supported type of contents:
                    - `list` (recommended)
                    - `str`
                    - `dict`

            - verbose: bool (default = False)
                Whether to print detailed messages while
                  writing to the file.

        Returns:
            None

        Raises:
            - ValueError
                If the file path or contents is empty.

            - TypeError
                If the contents type is not valid or unsupported.

            - IsADirectoryError
                If given file path is exists but a directory.

            - PermissionError
                If the directory path is restricted and cannot be
                  accessed by program.
        """
        def check_arguments(**kwargs) -> None:
            contents = kwargs.get('contents', None)
            filepath = kwargs.get('filepath', None)

            try:
                if not filepath:
                    msg = 'File path cannot be empty'
                    raise ValueError(msg)
                if not contents:
                    msg = 'Contents cannot be empty'
                    raise ValueError(msg)
                if os.path.exists(filepath) and os.path.isdir(filepath):
                    msg = 'Given file path is a directory'
                    raise IsADirectoryError(msg)
                if not isinstance(contents, (list, dict, str)):
                    msg = f'Unsupported contents type: "{type(contents)}"'
                    raise TypeError(msg)

            except (ValueError, TypeError) as val_type_err:
                Utils.raise_error(val_type_err, 1, file=__file__)
            except IsADirectoryError as dir_err:
                Utils.raise_error(dir_err, 2, file=__file__)

        # -------------------------------------------------- #

        def pr_msg(message: str, *args) -> None:
            if not verbose:
                return
            if len(args) == 0 or args is None:
                Utils.info_msg(message)
            elif len(args) != 0 and isinstance(args[0], dict):
                for key, val in args[0].items():
                    message.format(key=key, val=val)
                    Utils.info_msg(message)

        # -------------------------------------------------- #

        check_arguments(
            filepath=filepath, contents=contents
        )

        if kwargs.get('newline', True):
            newline = os.linesep
        else:
            newline = ''

        if verbose:
            Utils.pr_banner(
                'WRITE FILE', lines=40, center=39, newline=1
            )
            Utils.info_msg(
                f'Writing contents id:<{id(contents)}> to "{filepath}"...'
            )

        try:
            with open(filepath, 'w', encoding='utf-8') as file:
                if isinstance(contents, list):
                    for content in contents:
                        file.write(content + newline)
                        pr_msg(
                            f'Writing "{content.strip()}" -> \'{filepath}\'...'
                        )
                elif isinstance(contents, str):
                    pr_msg(f'Writing "{contents}" -> \'{filepath}\'...')
                    file.write(contents + newline)
                elif isinstance(contents, dict):
                    file.write(json.dumps(contents, indent=4) + newline)
                    pr_msg(
                        'Writing ("{key}", "{val}") ->' + f'\'{filepath}\'...',
                        contents
                    )

        except PermissionError as perm_err:
            Utils.raise_error(perm_err, -1, file=__file__)

        if verbose:
            print()
            Utils.info_msg(
                f'Successfully write the contents to file: "{filepath}".'
            )
            Utils.pr_banner(
                '(END) WRITE FILE', lines=40, center=39, newline=0
            )
            print()


    # List all of public methods, it can be imported all with wildcard '*'
    __all__ = ['raise_error', 'info_msg', 'convert_to_bs']
