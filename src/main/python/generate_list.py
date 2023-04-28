import os, sys, platform
from utils import Utils
from file_utils import FileUtils

class GenerateList:
    __PATH: dict = {
        ('target_path'): f'src{os.sep}main{os.sep}java{os.sep}',
        ('class_path'): f'target{os.sep}classes{os.sep}',
        ('out_path'): f'target{os.sep}generated-list{os.sep}',
        ('source'): f'target{os.sep}generated-list{os.sep}sourceFiles.lst',
        ('output'): f'target{os.sep}generated-list{os.sep}outputFiles.lst'
    }

    __verbose: bool = False
    __command: dict = {
        'unix': {
            'source': 'find {_in} -type f -name "*.java" > {_out}'.format(
                _in=__PATH['target_path'], _out=__PATH['source']
            ),

            'output': 'find {_in} -type f -name "*.class" > {_out}'.format(
                _in=__PATH['class_path'], _out=__PATH['output']
            )
        },

        'windows': {
            'source': 'dir "{_in}" /b /s *.java > {_out}'.format(
                _in=__PATH['target_path'], _out=__PATH['source']
            ),

            'output': 'dir "{_in}" /b /s *.class > {_out}'.format(
                _in=__PATH['class_path'], _out=__PATH['output']
            )
        }
    }

    def __init__(self, verbose: bool = False) -> None:
        self.__verbose: bool = verbose

        # Checking 'src/main/java' directory
        FileUtils._FileUtils__check_directory(
            self.__PATH['target_path'],
            verbose=self.__verbose
        )

        # Checking 'target/generated-list' directory
        FileUtils._FileUtils__check_directory(
            self.__PATH['out_path'],
            verbose=self.__verbose
        )


    def run(self, generate: str = 'source_list') -> None:
        if generate in ('source', 'source_list'):
            self.__generate_sources_list()
        elif generate in ('output', 'output_list'):
            self.__generate_output_list()
        else:
            try:
                msg = f'Unknown argument value: "{generate}"'
                raise ValueError(msg)
            except ValueError as ve:
                Utils._Utils__raise_error(ve, -1, file=__file__)

    def __generate_sources_list(self) -> None:
        if platform.system().lower() in ('linux', 'unix'):
            cmd = self.__command['unix']['source']
        elif platform.system().lower() in ('win', 'win32', 'win64', 'windows'):
            cmd = self.__command['windows']['source']

        err_code: int = os.system(cmd)

        if err_code != 0:
            sys.exit(err_code)


    def __generate_output_list(self) -> None:
        if platform.system().lower() in ('linux', 'unix'):
            cmd = self.__command['unix']['output']
        elif platform.system().lower() in ('win', 'win32', 'win64', 'windows'):
            cmd = self.__command['windows']['output']

        err_code: int = os.system(cmd)

        if err_code != 0:
            sys.exit(err_code)

        outputFiles_lst: list = FileUtils._FileUtils__read_file(
            self.__PATH['output'], verbose=self.__verbose
        )

        new_outputFiles_lst: list = [ ]

        for output_file in outputFiles_lst:
            for out in output_file.split():
                new_outputFiles_lst.append(os.path.join(*(out.split(os.sep)[2:])))
        new_outputFiles_lst.sort()

        FileUtils._FileUtils__write_to_file(
            self.__PATH['output'], contents=new_outputFiles_lst, verbose=self.__verbose
        )


## === DRIVER === ##
if __name__ == '__main__':
    verbose:       bool  = False
    generate_type: str   = None
    opts_arg:      list  = [
        ('-v', '--verbose', 'verbose'),
        [
            ('src', 'source'),
            ('cls', 'class')
        ]
    ]


    check_opt_1  = lambda arg : any(arg in opt for opt in opts_arg[1])
    check_opt_1x = lambda arg, i : arg in opts_arg[1][i]
    check_opt_2  = lambda arg : arg in opts_arg[0]

    # Checking the CLI arguments
    try:
        msg: str = None  # Empty message variable for exception message

        if len(sys.argv) < 2:
            msg = 'Arguments cannot be empty'
            raise IndexError(msg)
        elif len(sys.argv) > 3:
            msg = 'Too many arguments, need (1 or 2) arguments'
            raise IndexError(msg)
        elif len(sys.argv) == 2 and not (check_opt_1(sys.argv[1])):
            msg = f'Unknown options value: "{sys.argv[1]}"'
            raise ValueError(msg)
        elif len(sys.argv) == 3 and not (check_opt_1(sys.argv[1]) or check_opt_2(sys.argv[2])):
            msg = f'Unknown options value: "{sys.argv[1]}" + "{sys.argv[2]}"'
            raise ValueError(msg)
    except Exception as e:
        Utils._Utils__raise_error(e, 1, file=__file__)
    else:
        if len(sys.argv) == 2 and check_opt_1(sys.argv[1]):
            if check_opt_1x(sys.argv[1], 0):
                generate_type = 'source_list'
            elif check_opt_1x(sys.argv[1], 1):
                generate_type = 'output_list'
        elif len(sys.argv) == 3 and (check_opt_1(sys.argv[1]) and check_opt_2(sys.argv[2])):
            if check_opt_1x(sys.argv[1], 0):
                generate_type = 'source_list'
            elif check_opt_1x(sys.argv[1], 1):
                generate_type = 'output_list'
            verbose = True
        else:
            try:
                msg = f'Unknown options value: "{sys.argv[1]}" "{sys.argv[2]}"'
                raise ValueError(msg)
            except ValueError as ve:
                Utils._Utils__raise_error(ve, 1, file=__file__)


    # Run the generator
    GenerateList(verbose=verbose).run(generate=generate_type)
