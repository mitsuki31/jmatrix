class FileUtils:
    """
    File utilities for all Python programs that supports `JMatrix`.

    Notes:
        All methods from this class is private.
    """
    from typing import Optional, Union

    @staticmethod
    def __check_directory(dir: str, verbose: bool = False) -> None:
        """
        Check if a directory exists and create it if it does not exists.

        Parameters:
            - dir: str
                Directory path to be checked.

            - verbose: bool
                Whether to print detailed messages while checking or creating the directory.

        Returns:
            None

        Raises:
            None
        """
        import os
        from utils import Utils

        if verbose:
            print(os.linesep + '>>> [ CHECK DIRECTORY ] <<<')
            Utils._Utils__info_msg(f'Checking directory "{dir}"...')
        if not os.path.exists(dir):
            if verbose:
                Utils._Utils__info_msg(f'Given directory path does not exist.')
                Utils._Utils__info_msg(f'Creating new directory (\'{dir}\')...')
            os.makedirs(dir)
            if verbose:
                Utils._Utils__info_msg(f'Successfully create "{dir}" directory.')
        else:
            if verbose:
                Utils._Utils__info_msg(f'"{dir}" directory is already exist.')


    @staticmethod
    def __check_file(fp: str, verbose: bool = False) -> None:
        """
        Check if a file exists, if file does not exists it will raises an error.

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
        import os
        from utils import Utils

        if verbose:
            print(os.linesep + '>>> [ CHECK FILE ] <<<')
            Utils._Utils__info_msg(f'Check existence for file: "{fp}"...')

        if os.path.exists(fp):
            if verbose:
                Utils._Utils__info_msg(f'File "{fp}" is exist.')
            return
        else:
            Utils._Utils__info_msg(f'File "{fp}" does not exist, error raised!' + os.linesep)
            try:
                msg = f'File "{fp}" does not exist or cannot be accessed'
                raise FileNotFoundError(msg)
            except FileNotFoundError as fe:
                Utils._Utils__raise_error(fe, 2, file=__file__)


    @staticmethod
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
        import os
        from utils import Utils

        if verbose:
            print(os.linesep + '>>> [ GET FILE CONTENTS ] <<<')
            Utils._Utils__info_msg(f'Retrieving all contents from "{fp}"...')
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
            Utils._Utils__raise_error(e)
        else:
            if verbose:
                Utils._Utils__info_msg('Successfully retrieve all contents.')

        return None


    @staticmethod
    def __write_to_file(fp: str, contents: Union[Union[list, dict], str] = None, verbose: bool = False) -> None:
        """
        Write the contents to specified file.

        Parameters:
            - fp: str
                The file path to write the contents.

            - contents: Union[Union[list, dict], str] (default = None)
                The contents to write to the file.
                Supported type of contents:
                    - `list` (recommended)
                    - `str`
                    - `dict`

            - verbose: bool
                Whether to print detailed messages while writing to the file.

        Returns:
            None

        Raises:
            - ValueError
                If the file path or contents is empty.

            - RuntimeError
                If the contents type is not valid or unsupported.
        """
        import os, json
        from utils import Utils

        if verbose:
            print(os.linesep + '>>> [ WRITE FILE ] <<<')
            Utils._Utils__info_msg(f'Writing contents id:<{id(contents)}> to "{fp}"...')

        try:
            if fp is None:
                msg = 'File path cannot be empty'
                raise ValueError(msg)
            elif contents is None:
                msg = 'Contents cannot be empty'
                raise ValueError(msg)

            with open(fp, 'w', encoding='utf-8') as file:
                if isinstance(contents, list):
                    if verbose:
                        print()
                    for content in contents:
                        if verbose:
                            Utils._Utils__info_msg(f'Writing "{content.strip()}" -> \'{fp}\'...')
                        file.write(content + os.linesep)
                elif isinstance(contents, str):
                    if verbose:
                        print()
                        Utils._Utils__info_msg(f'Writing "{contents}" -> \'{fp}\'...')
                    file.write(contents + os.linesep)
                elif isinstance(contents, dict):
                    file.write(json.dumps(contents, indent=4) + os.linesep)
                    if verbose:
                        print()
                        for var, val in contents.items():
                           Utils._Utils__info_msg(f'Writing ("{var}", "{val}") -> \'{fp}\'...')
                else:
                    try:
                        msg = f'Unsupported contents type for type: "{type(contents)}"'
                        raise RuntimeError(msg)
                    except RuntimeError as re:
                        Utils._Utils__raise_error(re, -1, file=__file__)

        except Exception as e:
            Utils._Utils__raise_error(e, file=__file__)
        else:
            if verbose:
                print()
                Utils._Utils__info_msg(f'Successfully write the contents to file: "{fp}".')
