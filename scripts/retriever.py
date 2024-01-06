"""
Retrieve and manipulate information from a parsed POM
(*Project Object Model*) file.

This module includes functions to calculate MD5 hashes, verify hashes,
set up and retrieve information from a parsed POM file, and handle the main
execution of the script to retrieve and print values based on user input.

Copyright (c) 2023-2024 Ryuu Mitsuki.

Available Constants
-------------------
ROOT_DIR : os.PathLike
    The absolute path to the root directory.

TARGET_DIR : os.PathLike
    The path to the target directory.

OUTPUT_DIR : os.PathLike
    The path to the output directory for retrieved information.

Available Functions
-------------------
md5_get
    Calculate the MD5 hash of the specified file.

md5_verify
    Verify the MD5 hash of a file against the original hash.

setup
    Set up and retrieve information from the parsed POM file.

main
    Main function to retrieve POM information and print values
    based on user input.

"""


import os
import sys
import json
import hashlib
from typing import Dict, Tuple, Optional

ROOT_DIR: os.PathLike = os.path.abspath(
    os.path.join(os.path.dirname(__file__), '..'))
TARGET_DIR: os.PathLike = os.path.join(ROOT_DIR, 'target')
OUTPUT_DIR: os.PathLike = os.path.join(TARGET_DIR, '_retriever')


def md5_get(file: os.PathLike, block_size: int = -1) -> str:
    """
    Calculate the MD5 hash of the specified file.

    Parameters
    ----------
    file : path-like string
        The path to the file.
    block_size : int, optional
        The block size for reading the file, by default -1.

    Returns
    -------
    str
        The MD5 hash of the file.
    """
    # Prevent an error when specifying lower than -1
    block_size = -1 if block_size < -1 else block_size
    with open(file, 'rb') as file_obj:  # Must use read binary mode
        return hashlib.md5(file_obj.read(block_size)).hexdigest()

def md5_verify(orig: str, file: os.PathLike) -> bool:
    """
    Verify the MD5 hash of a file against the original hash.

    Parameters
    ----------
    orig : str
        A string representing the original MD5 hash.
    file : path-like string
        The path to the file.

    Returns
    -------
    bool
        ``True`` if the file's MD5 hash matches the original hash,
        ``False`` otherwise.
    """
    return md5_get(file) == orig

def setup() -> str:
    """
    Set up and retrieve information from the parsed POM file.

    Returns
    -------
    str
        The base name of the output file, which stored inside
        directory ``OUTPUT_DIR``.
    """
    # Insert the JMBuilder path to PYTHONPATH environment
    sys.path.insert(0, os.path.join(ROOT_DIR, 'tools', 'JMBuilder'))

    # DO NOT TRY TO MOVE THIS IMPORTS TO GLOBAL!! Keep on this function only
    # for speed and performance when commands in Makefile run this
    # module multiple times.
    try:
        # Try to import the package
        import jmbuilder
    except (ValueError, ImportError, ModuleNotFoundError) as import_err:
        print(f'{os.path.basename(__file__)}: {type(import_err).__name__}:',
            import_err, file=sys.stderr)
        print('>>> sys.path', file=sys.stderr)
        _ = [print(path, file=sys.stderr) for path in sys.path]
        sys.exit(1)

    curpom_file: os.PathLike = os.path.join(OUTPUT_DIR, '.CURRENT_POM')

    def write_checkpoint(md5_hash: str) -> None:
        with open(curpom_file, 'w') as curpom_obj:
            curpom_obj.write(md5_hash)

    # Retrieve several information (not all) from the parsed POM file,
    # including the project name, version, group ID, etc.
    pom_items: Dict[str, Optional[str]] = jmbuilder.JMRepairer(
        os.path.join(ROOT_DIR, 'pom.xml'))._pom_items
    del pom_items['maven.build.timestamp']  # No need for this

    # Create the temporary directory, ignoring existence
    os.makedirs(OUTPUT_DIR, exist_ok=True)

    # Get the MD5 hash of POM file
    pom_md5: str = md5_get(os.path.join(ROOT_DIR, 'pom.xml'))
    write_checkpoint(pom_md5)  # Write the checkpoint for later check

    # Create the file name from MD5 hash of POM file
    output_file: os.PathLike = os.path.join(
        OUTPUT_DIR, f'pom-{pom_md5}')

    with open(output_file, 'w') as out:
        out.write(json.dumps(pom_items, sort_keys=True))

    return os.path.basename(output_file)

def main() -> None:
    """
    Main function to retrieve POM information and return the values
    based on user input.

    First it will checks the '.CURRENT_POM' file within 'target'
    directory, if not exist it will try to setup and retrieve the
    information from the POM file, if exist then read the MD5 hash
    in that file to be used to find previously retrieved information
    with JSON format in the same directory.

    Returns
    -------
    str
        The value of the given key which from the first argument.
    """
    curpom_file: os.PathLike = os.path.join(
        OUTPUT_DIR, '.CURRENT_POM')
    pom_file: os.PathLike = os.path.join(ROOT_DIR, 'pom.xml')

    has_setup: bool = False
    out_file: str = None
    if not (os.path.isdir(OUTPUT_DIR) and os.path.exists(curpom_file)):
        out_file = setup()
        has_setup = True

    md5_hash: str = ''
    if not has_setup:
        with open(curpom_file, 'r') as curpom_obj:
            md5_hash = curpom_obj.read()
            if not md5_verify(md5_hash, pom_file):
                setup()
    else:
        # Simply remove the prefix
        md5_hash = out_file.removeprefix('pom-')

    json_file: os.PathLike = os.path.join(OUTPUT_DIR, f'pom-{md5_hash}')
    if not os.path.exists(json_file):
        json_file = setup()

    pom_items: Dict[str, Optional[str]] = {}
    with open(json_file) as file_obj:
        pom_items = json.load(file_obj)

    args: Tuple[str] = tuple(sys.argv[1:])
    if len(args) == 0:
        return

    return pom_items.get(args[0], '')

del Dict, Optional, Tuple

if __name__ == '__main__':
    print(main())  # This to make the value can be piped to Makefile
