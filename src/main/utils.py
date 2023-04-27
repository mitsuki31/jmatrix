import os, sys, traceback

def raise_error(ex: Exception, status: int = 1, file: str = __file__) -> None:
    '''Raise an error message then exit if status not equals to zero'''
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


def info_msg(s: str = None, prefix: str = 'jmatrix') -> None:
    print(f'[{prefix}] {s}')

try:
    from bs4 import BeautifulSoup
except ModuleNotFoundError as me:
    info_msg('Please install all the requirements first.' + os.linesep)
    raise_error(me)

def convert_to_BS(fp: str, type: str = 'xml', verbose: bool = False) -> BeautifulSoup:
    '''Convert given file path into `BeautifulSoup` object'''

    contents = None  # create null object for file contents

    try:
        if verbose:
            info_msg(f'Retrieving all contents from "{fp}"...')

        with open(fp, 'r', encoding='utf-8') as file:
            contents = file.read()
    except FileNotFoundError as fe:
        raise_error(fe, 2)
    except Exception as e:
        raise_error(e, 1)
    else:
        if verbose:
            info_msg(f'Successfully retrieve all contents from "{fp}".')
            info_msg('Contents converted to \'BeautifulSoup\' object')

    return BeautifulSoup(contents, type)
