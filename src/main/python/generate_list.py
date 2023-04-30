"""
Program that generates the list of source files or class files.
"""
__author__ = 'Ryuu Mitsuki'
__all__    = ['GenerateList']

import os
import sys
import platform
from utils import Utils, FileUtils

class GenerateList:
    """
    This class provides methods that generates list of source files and class files.

    For generating list, this class uses command that based on the operating system.

    Working directory:
        : 'src/main/java/' - For searching all of source files (*.java).
        : 'target/classes/' - For searching all of class files (*.class).
        : 'target/generated-list/' - The output directory for generated list.
    """
    __PATH: dict = {
        ('target_path'): (os.sep).join(['src', 'main', 'java']),
        ('class_path'): (os.sep).join(['target', 'classes']) + os.sep,
        ('out_path'): (os.sep).join(['target', 'generated-list']) + os.sep,
        ('source'): (os.sep).join(['target', 'generated-list', 'sourceFiles.lst']),
        ('output'): (os.sep).join(['target', 'generated-list', 'outputFiles.lst'])
    }

    __verbose: bool = False
    __command: dict = {
        'unix': {
            'source':
                f'find {__PATH["target_path"]} -type f -name "*.java" > {__PATH["source"]}',

            'output':
                f'find {__PATH["class_path"]} -type f -name "*.class" > {__PATH["output"]}'
        },

        'windows': {
            'source':
                f'dir "{__PATH["target_path"]}" /b /s *.java > {__PATH["source"]}',

            'output':
                f'dir "{__PATH["class_path"]}" /b /s *.class > {__PATH["output"]}'
        }
    }


    def __init__(self, verbose: bool = False) -> None:
        """
        This constructor will construct new object of `GenerateList`.
        It does nothing, only checks and creates some working directories.

        Parameters:
            - verbose: bool (default = False)
                Boolean value that specifies whether to print verbose output.

        Returns:
            None

        Raises:
            None
        """
        self.__verbose: bool = verbose

        # Checking 'src/main/java' directory
        FileUtils.check_directory(
            self.__PATH['target_path'],
            verbose=self.__verbose
        )

        # Checking 'target/generated-list' directory
        FileUtils.check_directory(
            self.__PATH['out_path'],
            verbose=self.__verbose
        )


    def run(self, generate: str = 'source_list') -> None:
        """
        Runs and generates the list by specifying the type of list
          that want to be generated.

        Parameters:
            - generate: str (default = 'source_list')
                String value that specifies which one list
                  that want to be generated.

                Accepted values:
                    - 'source_list' or 'source'
                    - 'output_list' or 'output'

        Returns:
            None

        Raises:
            - ValueError
                If the value of `generate` parameter is invalid.
        """
        if generate in ('source', 'source_list'):
            self.__generate_sources_list()
        elif generate in ('output', 'output_list'):
            self.__generate_output_list()
        else:
            try:
                msg = f'Unknown list type: "{generate}"'
                raise ValueError(msg)
            except ValueError as val_err:
                Utils.raise_error(val_err, -1, file=__file__)


    def __generate_sources_list(self) -> None:
        """
        This method generates a list of source files from `src/main/java` directory.
        The generated list is sorted and written back to `sourceFiles.lst` file.

        Parameters:
            None

        Returns:
            None

        Raises:
            None
        """
        if platform.system().lower() in ('linux', 'unix'):
            command = self.__command['unix']['source']
        elif platform.system().lower() in ('win', 'win32', 'win64', 'windows'):
            command = self.__command['windows']['source']

        err_code: int = os.system(command)

        if err_code != 0:
            sys.exit(err_code)

        if self.__verbose:
            print(os.linesep + '>>> [ SORT THE SOURCE LIST ] <<<')
            Utils.info_msg(
                f'Sorting "{self.__PATH["source"].rsplit(os.sep, maxsplit=1)[-1]}"...'
            )

        source_lst: list = FileUtils.read_file(
            self.__PATH['source'], verbose=self.__verbose
        )

        for idx, _ in enumerate(source_lst):
            source_lst[idx] = source_lst[idx].strip()
        source_lst.sort()

        if self.__verbose:
            Utils.info_msg('All list sorted.')

        FileUtils.write_to_file(
            self.__PATH['source'], contents=sorted(source_lst), verbose=self.__verbose
        )


    def __generate_output_list(self) -> None:
        """
        This method generates a list of class files from `target/classes/` directory.
        The generated list is sorted and written back to `outputFiles.lst` file.

        Parameters:
            None

        Returns:
            None

        Raises:
            None
        """
        if platform.system().lower() in ('linux', 'unix'):
            cmd = self.__command['unix']['output']
        elif platform.system().lower() in ('win', 'win32', 'win64', 'windows'):
            cmd = self.__command['windows']['output']

        err_code: int = os.system(cmd)

        if err_code != 0:
            sys.exit(err_code)

        if self.__verbose:
            print(os.linesep + '>>> [ SORT THE OUTPUT LIST ] <<<')
            Utils.info_msg(
                f'Sorting "{self.__PATH["output"].rsplit(os.sep, maxsplit=1)[-1]}"...'
            )

        output_lst: list = FileUtils.read_file(
            self.__PATH['output'], verbose=self.__verbose
        )

        new_output_lst: list = [ ]

        for output_file in output_lst:
            for out in output_file.split():
                new_output_lst.append(os.path.join(*(out.split(os.sep)[2:])))
        new_output_lst.sort()

        if self.__verbose:
            Utils.info_msg('All list sorted.')

        FileUtils.write_to_file(
            self.__PATH['output'], contents=new_output_lst, verbose=self.__verbose
        )

    __all__ = ['run']


def main() -> None:
    """
    Main program.
    """
    def check_opt(opt: str, *args) -> bool:
        result: bool = False

        if len(args) == 1 and args[0] == 1:
            result = any(opt in option for option in opts_arg[args[0]])
        elif len(args) == 1 and args[0] == 0:
            result = opt in opts_arg[0]
        elif len(args) == 2 and args[0] == 1:
            result = opt in opts_arg[args[0]][args[1]]

        return result

    # -------------------------------------------------------- #

    opts_arg: list  = [
        ('-v', '--verbose', 'verbose'),
        [
            ('src', 'source'),
            ('cls', 'class')
        ]
    ]

    # Checking the CLI arguments
    try:
        if len(sys.argv) > 3:
            raise RuntimeError('Too many arguments, need (1 or 2) arguments')
        if len(sys.argv) == 2 and not check_opt(sys.argv[1], 1):
            raise ValueError(f'Unknown options value: "{sys.argv[1]}"')
        if len(sys.argv) == 3 and not (check_opt(sys.argv[1], 1) or check_opt(sys.argv[2], 0)):
            raise ValueError(f'Unknown options value: "{sys.argv[1]}" + "{sys.argv[2]}"')
    except ValueError as value_err:
        Utils.raise_error(value_err, 1, file=__file__)
    except RuntimeError as run_err:
        Utils.raise_error(run_err, -1, file=__file__)

    # Run the GenerateList program
    if len(sys.argv) == 2 and \
            (check_opt(sys.argv[1], 1) and check_opt(sys.argv[1], 1, 0)):
        GenerateList().run(generate='source_list')
    elif len(sys.argv) == 2 and \
            (check_opt(sys.argv[1], 1) and check_opt(sys.argv[1], 1, 1)):
        GenerateList().run(generate='output_list')
    elif len(sys.argv) == 3 and (check_opt(sys.argv[1], 1) and check_opt(sys.argv[2], 0)):
        if check_opt(sys.argv[1], 1, 0):
            GenerateList(verbose=True).run(generate='source_list')
        elif check_opt(sys.argv[1], 1, 1):
            GenerateList(verbose=True).run(generate='output_list')
    else:
        try:
            raise ValueError(f'Unknown options value: "{sys.argv[1]}" "{sys.argv[2]}"')
        except ValueError as value_err:
            Utils.raise_error(value_err, 1, file=__file__)


## === DRIVER === ##
if __name__ == '__main__':
    if len(sys.argv) < 2:
        Utils.raise_error(
            RuntimeError('Arguments cannot be empty'),
            -1, file=__file__
        )

    main()
