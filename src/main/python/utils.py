class Utils:
    """
    Other utilities for all Python programs that supports `JMatrix`.

    Notes:
        All methods from this class is private.
    """
    @staticmethod
    def __raise_error(ex: Exception, status: int = 1, file: str = __file__) -> None:
        """
        This function is used to print the error message, traceback, and exit the program with a given error code.

        Parameters:
            - ex: Exception
                The exception object to be printed.

            - status: int (default = 1)
                The exit status code of the program.

            - file: str (default = __file__)
                The file name where the error occurred.

        Returns:
            None

        Raises:
            None
        """
        import os, sys, traceback

        if len(file.split(os.sep)) != 1:
            file = file.split(os.sep)[-1]

        sys.stderr.write(os.linesep + f'/!\\ ERROR{os.linesep}{">"*9}{os.linesep}')
        sys.stderr.write(f'[jmatrix] An error occured in "{file}".{os.linesep}')
        sys.stderr.write(f"{'>'*9} {ex} {'<'*9}{os.linesep*2}")

        tb = traceback.extract_stack()
        tb.pop(-1)

        sys.stderr.write(f'/!\\ TRACEBACK{os.linesep}{">"*13}{os.linesep}')
        for frame in tb:
            sys.stderr.write(f'File "...{os.sep}{(os.sep).join(frame.filename.split(os.sep)[-4:])}"{os.linesep}')
            sys.stderr.write('>'*4 + ' '*6 + f'at "{frame.name}", line {frame.lineno}{os.linesep*2 if tb.index(frame) != len(tb) - 1 else os.linesep}')

            if tb.index(frame) == len(tb) - 1:
                sys.stderr.write(f'{type(ex).__name__}: {ex}{os.linesep}')

        sys.stderr.write(f'{os.linesep}Exited with error code: {status}{os.linesep}')
        sys.exit(status) if status != 0 else None


    @staticmethod
    def __info_msg(s: str = None, prefix: str = 'jmatrix') -> None:
        """
        Print the message to standard output (stdout) with specified prefix.

        Parameters:
            - s: str (default = None)
                A string that want to be printed.

            - prefix: str (default = 'jmatrix')
                A string that specifies prefix.

        Returns:
            None

        Raises:
            None
        """
        print(f'[{prefix}] {s}')


    @staticmethod
    def __convert_to_BS(fp: str, _type: str = 'xml', verbose: bool = False):
        """
        Takes a file path as input and returns the contents of the file in BeautifulSoup object form.

        Parameters:
            - fp: str
                The file path of the file to be parsed.

            - _type: str (default = 'xml')
                The type of parser to be used.

            - verbose: bool (default = False)
                A boolean value that specifies whether to print verbose output.

        Returns:
            A `BeautifulSoup` object containing the contents of the file.

        Raises:
            - FileNotFoundError:
                If the given file path does not exist or cannot be accessed.

            - ModuleNotFoundError:
                If the `BeautifulSoup` module cannot be imported or found.

            - Exception:
                If any other error occurs while parsing the file.
        """
        import os

        contents = None  # create null object for file contents

        try:
            from bs4 import BeautifulSoup

            if verbose:
                Utils._Utils__info_msg(f'Retrieving all contents from "{fp}"...')

            with open(fp, 'r', encoding='utf-8') as file:
                contents = file.read()
        except FileNotFoundError as fe:
            Utils._Utils__raise_error(fe, 2)
        except ModuleNotFoundError as me:
            Utils._Utils__info_msg('Please install all the requirements first.' + os.linesep)
            Utils._Utils__raise_error(me)
        except Exception as e:
            Utils._Utils__raise_error(e, 1)
        else:
            if verbose:
                Utils._Utils__info_msg(f'Successfully retrieve all contents from "{fp}".')
                Utils._Utils__info_msg('Contents converted to \'BeautifulSoup\' object.')

            return BeautifulSoup(contents, _type)
