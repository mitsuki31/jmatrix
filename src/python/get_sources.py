'''
`get_sources.py` created for searching all source files (.java)
in "src/com/mitsuki/jmatrix" directory.
/!\ Make sure you've Python 3.7+ installed

Created by Ryuu Mitsuki
'''
import os, platform
import re

def raise_error(ex: Exception, status: int = 1) -> None:
    '''Raise an error message then exit if status not equal zero'''
    print('[jmatrix]', e)
    sys.exit(status) if status != 0 else None

cmd: str = None
FILE_DEST: str = 'source_files.list'
LIB: str = (os.sep).join(['src', 'com', 'mitsuki', 'jmatrix']) + os.sep
DEST: str = (os.sep).join(['assets', 'properties', FILE_DEST])

print('Getting all source files in "%s"...' % LIB)

if platform.system() in ('Linux', 'Unix'):
    cmd = f'find {LIB} -type f -name "*.java" > {DEST}'
else:
    cmd = f'dir "{LIB}" /b /s *.java > {DEST}'

err_code: int = os.system(cmd)

if err_code != 0:
    os.sys.exit(err_code)

src_list: list = []
try:
    with open(DEST, 'r') as src:
        src_list = src.readlines()
except FileNotFoundError as fe:
    raise_error(fe, 2)


print('\n' + '-' * 15 + '   SOURCE FILES   ' + '-' * 15)
[
    print(src, end='' if sorted(src_list).index(src) != (len(sorted(src_list)) - 1) else None)
    for src in sorted(src_list)
]

print('Saved to "%s"' % DEST)
try:
    with open(DEST, 'w') as src:
        [src.write(s) for s in sorted(src_list)]
except FileNotFoundError as fe:
    raise_error(fe, 2)
