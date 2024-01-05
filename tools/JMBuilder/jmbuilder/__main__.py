"""Main Module for JMBuilder

Copyright (c) 2023-2024 Ryuu Mitsuki.

"""

import os as __os
import sys as __sys
import re as __re
from pathlib import Path as __Path
from typing import (
    Any,
    Iterable,
    Union,
    Set,
    List,
    Dict,
    Tuple,
    TextIO
)


try:
    from . import utils as __jmutils
    from . import exception as __jmexc
    from ._globals import AUTHOR, VERSION, VERSION_INFO, __jmsetup__
    from .core import PomParser as __core_PomParser, JMRepairer as __core_JMRepairer
except (ImportError, ModuleNotFoundError, ValueError):
    # Add a new Python search path to the first index
    __sys.path.insert(0, str(__Path(__sys.path[0]).parent))

    from . import utils as __jmutils
    from jmbuilder._globals import AUTHOR, VERSION, VERSION_INFO, __jmsetup__
    from jmbuilder.core import PomParser as __core_PomParser, JMRepairer as __core_JMRepairer
finally:
    del __Path  # This no longer being used

CLEAN_ARGS: Tuple[str] = __jmutils.remove_duplicates(__sys.argv[1:])

def __print_version(_exit: bool = False, *,
                    only_ver: bool = False,
                    file: TextIO = __sys.stdout) -> None:
    """
    Print the version info to specific opened file.

    Parameters
    ----------
    exit : bool, optional
        Whether to exit and terminate the Python after printed the version.
        Defaults to False (disabled).

    only_ver: bool, optional
        Whether to print the version only. By activating this option,
        other information like program name, license, and copyright
        will not be printed. Defaults to False.

    file : TextIO, optional
        The file to print the version info.
        Defaults to console standard output (`sys.stdout`).

    """

    if not file:
        raise ValueError(f"File must be a file object, got {type(file).__name__!r}")

    program_name: str = __jmsetup__.progname
    version:      str = f"v{'.'.join(map(str, __jmsetup__.version))}"
    author:       str = __jmsetup__.author

    # Check if only_ver is False (or not specified)
    if not only_ver:
        print(
            program_name, version, f'- {__jmsetup__.license}',  # Program name and version
            __os.linesep + \
            f'Copyright (C) 2023-2024 by {author}.',                 # Copyright notice
            file=file
        )
    else:
        print(version, file=file)

    if _exit:
        __sys.exit(0)


def __argchck(targets: Union[str, Iterable], args: Union[List[str], Tuple[str]]) -> bool:
    """
    Check whether specified argument are presented in `args`.

    Paramaters
    ----------
    targets : str or iterable
        An argument or a list of arguments (must iterable) to searched for.

    args : list or tuple of str
        A list of arguments.

    Returns
    -------
    bool :
        Returns True if the specified argument are presented in `args`,
        otherwise returns False.

    """
    if isinstance(targets, str):
        return targets in args

    found: bool = False
    for target in targets:
        if str(target) in args:
            found = True

    return found


def __find_arg(val: Union[str, __re.Pattern]) -> int:
    """
    Find the index of specified argument from the command-line arguments.

    Parameters
    ----------
    val : str or re.Pattern
        A regular expression pattern used to search for the argument within
        the command-line arguments. Accepts a string literal representing
        the regular expression or a compiled regular expression.

    Returns
    -------
    int :
        The index of the specified argument in the command-line arguments.
        Returns -1 if the argument cannot be found or if the command-line
        arguments are empty.

    Notes
    -----
    This function utilizes the global constant ``CLEAN_ARGS``, ensuring that
    it searches for the desired argument within the command-line arguments
    with all duplicate arguments omitted.

    """
    # Use the fixed arguments; global constant
    if len(CLEAN_ARGS) == 0:
        return -1

    # Convert to regular expression
    val = __re.compile(val) if isinstance(val, str) else val

    res: __re.Match = None
    for arg in CLEAN_ARGS:
        res = val.search(arg)
        if res:
            break

    return CLEAN_ARGS.index(res.group()) if res else -1


def __print_help() -> None:
    """Print the help message to the standard output."""

    program_name: str = __jmsetup__.progname
    version:      str = f"v{'.'.join(map(str, __jmsetup__.version))}"
    author:       str = __jmsetup__.author

    header: str = f'{program_name} {version}'

    print(f"""\
{header}
{''.join(['-' for _ in range(len(header))])}

USAGE:
   python -m {__package__} [OPTIONS]

OPTIONS:
   --fix-mf <pom> <in> [out],
   --fix-manifest <pom> <in> [out]
        Run the builder to correct the specified manifest file containing
        Maven's variables. Utilizes information from the provided POM file.
        The output will be written to the given output file, if provided;
        otherwise, it will overwrite the input file.

   --fix-prop <pom> <in> [out],
   --fix-properties <pom> <in> [out]
        Run the builder to rectify the specified properties file with
        Maven's variables. Incorporates information from the provided POM file.
        The output will be written to the given output file, if provided;
        otherwise, it will overwrite the input file.

   -V, --version, -version
        Print the version and copyright information. All details will be printed
        directly to the standard output, except for '-version', it goes
        to the standard error.

   -VV, --only-ver, --only-version
        Print the version number only.

   -h, --help
        Print this help message.

ISSUES:
   Report some issues and help us improve this builder.
   <https://github.com/mitsuki31/JMBuilder/issues/new>

AUTHOR:
   {author}\
""")


#::#  Main Driver  #::#
def main() -> None:
    """Main function for JMBuilder."""
    help_args: Tuple[str] = ('-h', '--help')
    version_args: Tuple[str] = ('-V', '--version', '-version')
    only_version_args: Tuple[str] = ('-VV', '--only-ver', '--only-version')
    fix_mf_args: Tuple[str] = ('--fix-manifest', '--fix-mf')
    fix_prop_args: Tuple[str] = ('--fix-properties', '--fix-prop')
    all_known_args: Set[str] = {
        *help_args, *version_args, *only_version_args,
        *fix_mf_args, *fix_prop_args
    }

    if len(CLEAN_ARGS) == 0:
        print(
            f'Nothing to run.{__os.linesep * 2}' +
            f'USAGE: python -m {__package__} [-h | -V | -VV]{__os.linesep}' +
            '\t\t[--fix-mf <pom> <in> [out] | --fix-prop <pom> <in> [out]]'
        )
        __sys.exit(0)

    # Check for `-V` or `--version` in the arguments
    # If found, print the version info then exit with exit code zero (success)
    #
    if __argchck(version_args[:-1], CLEAN_ARGS):
        __print_version(True)

    # For `-version` argument, the output will be redirected
    # to the standard error (`sys.stderr`)
    #   `-V`, `--version` -> sys.stdout
    #   `-version`        -> sys.stderr
    #
    elif __argchck(version_args[-1], CLEAN_ARGS):
        __print_version(True, file=__sys.stderr)

    # To print the version only, user can use several arguments. See 'only_version_args'
    elif __argchck(only_version_args, CLEAN_ARGS):
        __print_version(True, only_ver=True)

    # Print the help message
    elif __argchck(help_args, CLEAN_ARGS):
        __print_help()

    # Only executed if and only if both options not specified simultaneously
    elif __argchck(fix_mf_args, CLEAN_ARGS) ^ __argchck(fix_prop_args, CLEAN_ARGS):
        fix_args = fix_mf_args if __argchck(fix_mf_args, CLEAN_ARGS) else fix_prop_args
        opt_idx = __find_arg('(' + '|'.join(fix_args) + ')') \
            if __argchck(fix_args, CLEAN_ARGS) else -1
        del fix_args

        # Keep the unformatted brackets, with this we can format and
        # write the actual error message on later.
        option_err_msg: str = \
            '{}' + f'.{__os.linesep * 2}' + \
            f'USAGE: python -m {__package__} {CLEAN_ARGS[opt_idx]} <pom> <in> [out]'

        # This dictionary will holds the builder arguments
        option_args: Dict[str, Any] = {
            'pom': None,     # 1
            'infile': None,  # 2
            'outfile': None  # 3
        }
        # Get the builder method name
        method_name: Any = 'fix_manifest' \
            if CLEAN_ARGS[opt_idx] in fix_mf_args else 'fix_properties'

        if not (len(CLEAN_ARGS) - 1) >= (opt_idx + 1):
            raise __jmexc.JMException(
                option_err_msg.format('No POM file were specified'))
        if not (len(CLEAN_ARGS) - 1) >= (opt_idx + 2):
            raise __jmexc.JMException(
                option_err_msg.format('No input file were specified'))

        # Get the argument right after the option argument, and treat it
        # as a path to specific POM file, otherwise an error raised if not specified.
        option_args['pom'] = __core_PomParser.parse(CLEAN_ARGS[opt_idx + 1])

        # Get the input file (infile) argument,
        # otherwise raise an error if not specified
        option_args['infile'] = CLEAN_ARGS[opt_idx + 2]

        # Get the outfile argument, otherwise use the infile argument
        # if not specified (i.e., overwrite the input file)
        if (len(CLEAN_ARGS) - 1) >= (opt_idx + 3):
            option_args['outfile'] = CLEAN_ARGS[opt_idx + 3]
        else:
            option_args['outfile'] = option_args.get('infile')

        repairer: __core_JMRepairer = __core_JMRepairer(option_args.pop('pom'))
        # Call the method dynamically
        args_vals: Tuple[str] = (*option_args.values(),)
        getattr(repairer, method_name)(args_vals[0], outfile=args_vals[1])

    # This for causing the error when both options are specified
    elif __argchck(fix_mf_args, CLEAN_ARGS) and __argchck(fix_prop_args, CLEAN_ARGS):
        raise __jmexc.JMException(
            'Program was unable to run both options simultaneously')
    elif not __argchck(CLEAN_ARGS, all_known_args):
        err: str = "Unknown argument detected: '{}'" + (__os.linesep * 2) + \
            'For more details, type argument `-h` or `--help`.'

        arg_idx: int = next((idx for idx, arg in enumerate(CLEAN_ARGS)
            if not __argchck(arg, all_known_args)), -1)

        raise __jmexc.JMException(err.format(CLEAN_ARGS[arg_idx]))


__author__       = AUTHOR
__version__      = VERSION
__version_info__ = VERSION_INFO


# Delete unused imported objects
del AUTHOR, VERSION, VERSION_INFO
del Iterable, Union, Any, TextIO, List, Tuple, Dict, Set


if __name__ == '__main__':
    main()
else:
    # Remove the `main` function when this module is being imported,
    # but it's a bit silly if there someone imports this module.
    # The reason is the function implementation are strongly depends on
    # the command-line arguments (`sys.argv`). However, if this is imported
    # users can still modify the `sys.argv` manually, right?
    # But we restricted that, we want the function to run as main driver only.
    del main
