"""
Program that fixes all JMatrix configurations.
"""
__author__ = 'Ryuu Mitsuki'
__all__    = ['FixConfig']

import os
import sys
import json
import re
from typing import Union, Optional
from utils import Utils, FileUtils

try:
    from bs4 import BeautifulSoup
    from bs4.formatter import XMLFormatter
except ModuleNotFoundError as bs_not_found:
    Utils.info_msg('Please install all the requirements first.' + os.linesep)
    Utils.raise_error(bs_not_found, -1, file=__file__)


class FixConfig:
    '''
    This class will fix the JMatrix configurations for specified file(s).

    File(s) to be fixed:
        - MANIFEST.MF
        - Makefile
        - config.xml
    '''
    __SOURCES_PATH:   str = (os.sep).join(['src', 'main']) + os.sep
    __TARGET_PATH:    str = f'target{os.sep}'
    __CLASSES_PATH:   str = __TARGET_PATH + f'classes{os.sep}'
    __CACHE_PATH:     str = __TARGET_PATH + f'.cache{os.sep}'
    __RESOURCES_PATH: str = __SOURCES_PATH + f'resources{os.sep}'

    __TARGET_FILES: tuple = ('config.xml', 'Makefile', 'MANIFEST.MF',)
    __verbose:      bool  = False

    def __init__(self, verbose: bool = False) -> None:
        """
        This constructor will construct new object of `FixConfig`.

        Parameters:
            - verbose: bool (default = False)
                Boolean value that specifies whether to print verbose output.

        Returns:
            None

        Raises:
            None
        """
        self.__verbose = verbose

    def run(self) -> None:
        """
        Runs this program and fixes all JMatrix configurations data.

        Parameters:
            None

        Returns:
            None

        Raises:
            None
        """
        self.__get_pom_data(cache=True, out='config.json')

        # [config.xml]
        self.__fix_configuration(
            None,
            cached=f'{self.__CACHE_PATH}config.json',
            target='config.xml'
        )

        # [Makefile]
        self.__fix_configuration(
            None,
            cached=f'{self.__CACHE_PATH}config.json',
            target='makefile'
        )

        # [MANIFEST.MF]
        self.__fix_configuration(
            None,
            cached=f'{self.__CACHE_PATH}config.json',
            target='manifest.mf'
        )

    def __create_cache(self,
            data: Union[dict, str], indent: int = 4, out: str = None) -> None:
        """
        Create the cache of `data` and store it to `target/.cache/` directory
          with specified file name.
        The function supports creating two types of cache (JSON and string).
        For JSON cache, the `data` parameter should be a dictionary object.

        Parameters:
            - data: Union[dict, str]
                Data object that want to be cached.
                The type is optional, can be 'dict' or 'str'.
                Recommended use 'dict' object for creating JSON cache.

            - indent: int (default = 4)
                An integer value that specifies number of spaces for
                  indentation in the JSON cache.

            - out: str (default = None)
                String representing the name of the output file.
                The output directory is `target/.cache/` directory.

        Returns:
            None

        Raises:
            - RuntimeError
                If `out` parameter is empty.

            - PermissionError
                If the directory path for output file is restricted
                  and cannot be accessed by program.

        """
        FileUtils.check_directory(self.__CACHE_PATH, verbose=self.__verbose)

        if self.__verbose:
            Utils.pr_banner('CREATE CACHE')
            Utils.info_msg(f'Creating cache for id:<{id(data)}> to "{out}"...')

        try:
            if out is None:
                msg = 'Please insert name for output file'
                raise RuntimeError(msg)
        except RuntimeError as run_err:
            Utils.raise_error(run_err, -1, file=__file__)

        if isinstance(data, str):
            try:
                if self.__verbose:
                    Utils.info_msg(os.linesep + \
                        f'Writing "{data.strip()}" -> \'{out}\'...')

                with open(self.__CACHE_PATH + out, 'w', encoding='utf-8') as cache:
                    cache.write(data)
            except PermissionError as perm_err:
                Utils.raise_error(perm_err, -1, file=__file__)

            if self.__verbose:
                print()
                Utils.info_msg(f'Cache created, saved in "{self.__CACHE_PATH}{out}".')

        elif isinstance(data, dict):
            try:
                with open(self.__CACHE_PATH + out, 'w', encoding='utf-8') as cache:
                    cache.write(json.dumps(data, indent=indent) + os.linesep)

                if self.__verbose:
                    print()
                    for key, val in data.items():
                        Utils.info_msg(f'Writing ("{key}", "{val}") -> \'{out}\'...')
            except PermissionError as perm_err:
                Utils.raise_error(perm_err, -1, file=__file__)

            if self.__verbose:
                print()
                Utils.info_msg(f'Cache created, saved in "{self.__CACHE_PATH}{out}".')
                Utils.pr_banner('(END) CREATE CACHE', newline=1)


    def __get_cache(self, cache_path: str) -> dict:
        """
        Method that gets cached configurations data from specified path.

        Parameters:
            - cache_path: str
                Path to cached configurations data

        Returns:
            A dictionary containing all cached configurations data.

        Raises:
            - FileNotFoundError
                If the given cache path is does not exist.

            - IsADirectoryError
                If the given cache path is exist but a directory.
        """
        cache_data: dict = { }

        try:
            if not os.path.exists(cache_path):
                msg = 'Path to cached configuration data does not exist'
                raise FileNotFoundError(msg)
            if os.path.exists(cache_path) and not os.path.isfile(cache_path):
                msg = 'Path to cached configuration data is a directory'
                raise IsADirectoryError(msg)

            cache_data = json.loads(FileUtils.read_file(
                cache_path, _list=False,
                verbose=self.__verbose
            ))

        except FileNotFoundError as nf_err:
            Utils.raise_error(nf_err, 2, file=__file__)
        except IsADirectoryError as dir_err:
            Utils.raise_error(dir_err, 1, file=__file__)

        return cache_data


    def __get_output_path(self, target: str) -> str:
        """
        Gets the correct output path for targeted file.

        Parameters:
            - target: str
                The targeted file to gets it's correct output path.

        Returns:
            A string representing output path for targeted file.

        Raises:
            None
        """
        path: str = None

        if target.lower() == self.__TARGET_FILES[0]:
            path = self.__CLASSES_PATH + f'configuration{os.sep}'
        if target.lower() == self.__TARGET_FILES[1].lower():
            path = '.' + os.sep
        if target.lower() == self.__TARGET_FILES[2].lower():
            path = 'MANIFEST' + os.sep

        return path


    def __get_version(self, list_data: list = None, **kwargs) -> Union[tuple, str]:
        """
        Search for a version string in a list of data
          using a regex pattern and return version information.

        Parameters:
            - list_data: list (default = None)
                A list of data to be searched.

            - **kwargs
                Additional keyword arguments.

                + regex: str
                    A regex pattern to match against the list elements.

                + config_data: dict
                    Dictionary containing the configuration data and version.

                + fixed_only: bool (optional)
                    If True, returns only the fixed version string.

        Returns:
            If a match is found, a tuple containing the index of the
              matching element, the new version string,
              and the original element content.
            If 'fixed_only' is True, returns the fixed version string only.

        Raises:
            None
        """
        config_data:   dict = kwargs.get('config_data', {})
        fixed_version: str  = config_data.get('package.version.core', 'null')
        if config_data.get('package.releaseType', 'null').lower() != 'release':
            fixed_version += '-' + config_data.get('package.releaseType', 'null') + \
                             '.' + config_data.get('package.betaNum', 'null')

        if kwargs.get('fixed_only', False):
            return fixed_version

        if not list_data or len(list_data) == 0:
            Utils.raise_error(
                ValueError('List data cannot be empty'),
                1, file=__file__
            )

        regex:         str  = kwargs.get('regex')
        new_version:   str  = None
        index:         int  = -99

        for i, __data in enumerate(list_data):
            match = re.compile(regex).match(__data.lower().strip())
            if match:
                index = i
                new_version = ' '.join(
                    match.group(0).upper().split()[:2]
                ) + ' ' + fixed_version
                break

        return (list_data[index].strip(), new_version, index)


    def __get_pom_data(self, cache: bool = True, out: str = None) -> Optional[dict]:
        """
        This function is used to retrieve configuration data from 'pom.xml'.

        Parameters:
            - cache: bool (default = True)
                Boolean that specifies whether to create a cache file
                  for the retrieved data or not.
            - out: str (default = None)
                A string that specifies file name of output cache.
                If None it will be overwrited with 'config.json'

        Returns:
            If `cache` is False, the function returns a dictionary
              containing the configuration data. Otherwise, returns None.

        Raises:
            None
        """
        def filter_data(bs_data: BeautifulSoup) -> dict:
            def filter_group_id(data: dict) -> None:
                for elm in bs_data.find_all('groupId'):
                    if elm.text.strip().split('.')[-1] not in data['author.name'].lower():
                        continue
                    data['project.' + elm.name] = elm.text.strip()
                    if self.__verbose:
                        print(' ' * 4 + f'<{elm.name}>', elm.text.strip(), f'</{elm.name}>')

            # --------------------------------------------------- #

            def filter_artifact_id(data: dict) -> None:
                for elm in bs_data.find_all('artifactId'):
                    if not elm.text.strip().split('-')[0] == data['package.name'].lower():
                        continue
                    data['project.' + elm.name] = elm.text.strip()
                    if self.__verbose:
                        print(' ' * 4 + f'<{elm.name}>', elm.text.strip(), f'</{elm.name}>')

            # --------------------------------------------------- #

            data: dict = { }

            if self.__verbose:
                Utils.info_msg('Filtering contents...')
                print(os.linesep + '>> ' + '-' * 50 + ' <<')
                print(os.linesep + '<contents>')

            for element in bs_data.find_all():
                for child in element.find_all():
                    if child.name != 'properties':
                        continue
                    for child_1 in child.find_all():
                        if child_1.name.startswith('project'):
                            continue
                        data[child_1.name] = child_1.text.strip()
                        if self.__verbose:
                            print(' ' * 4 + f'<{child_1.name}>',
                                child_1.text.strip(), f'</{child_1.name}>'
                            )

            filter_group_id(data)
            filter_artifact_id(data)

            if self.__verbose:
                print('</contents>' + os.linesep)
                print('>> ' + '-' * 50 + ' <<' + os.linesep)
                Utils.info_msg('All contents filtered.')

            return data

        # ------------------------------------------------------- #

        if out is None or out == '':
            out = 'config.json'

        if self.__verbose:
            Utils.pr_banner('GET CONFIG DATA', newline=1)

        FileUtils.check_file('pom.xml', verbose=self.__verbose)
        bs_data: BeautifulSoup = Utils.convert_to_bs(
            'pom.xml',
            verbose=self.__verbose
        )

        config_data: dict = filter_data(bs_data)

        if cache:
            self.__create_cache(config_data, indent=4, out=out)
            config_data = None

        if self.__verbose:
            Utils.pr_banner('(END) GET CONFIG DATA', newline=1)

        return config_data


    def __fix_configuration(self,
            data: dict, cached: str = None, target: str = None) -> None:
        """
        Fix the configurations of specified target file.

        Parameters:
            - data: dict
                The dictionary contains new configuration data.

            - cached: str (default = None)
                File path to the cached new configuration data.

            - target: str (default = None)
                The target file that want to fix it's configurations.

        Returns:
            None

        Raises:
            - RuntimeError
                If `data` is empty and path to cached
                  configuration also empty.

            - FileNotFoundError
                If file path to cached configuration data does not exist.
        """
        target_path: str = None
        target_data: Union[list, dict] = None

        try:
            if not data and not cached:
                raise RuntimeError(
                    'Given config data is empty, please specify path to cached config'
                )
            if target.lower() is None or \
                    not target in [
                        target_file.lower()
                        for target_file in self.__TARGET_FILES
                    ]:
                raise RuntimeError('Please specify the target file')

            if target.lower() in [
                    target_file.lower()
                    for target_file in self.__TARGET_FILES
                ]:
                # config.xml
                if target.lower() == self.__TARGET_FILES[0]:
                    target_path = self.__RESOURCES_PATH + \
                        f'configuration{os.sep}' + self.__TARGET_FILES[0]

                # Makefile
                elif target.lower() == self.__TARGET_FILES[1].lower():
                    target_path = self.__TARGET_FILES[1]

                # MANIFEST.MF
                elif target.lower() == self.__TARGET_FILES[2].lower():
                    target_path = f'META-INF{os.sep}' + self.__TARGET_FILES[2]

        except RuntimeError as run_err:
            Utils.raise_error(run_err, -1, file=__file__)

        if self.__verbose:
            Utils.pr_banner('FIX CONFIGURATION')
            print(f'>>>>> FILE: "{target_path}"')

        if target.lower() != self.__TARGET_FILES[1].lower():
            FileUtils.check_directory(
                self.__get_output_path(target), verbose=self.__verbose
            )

        data = self.__get_cache(cached) if not data else data

        # Check the target file
        # [config.xml]
        if target.lower() == self.__TARGET_FILES[0]:
            bs_data = repr(Utils.convert_to_bs(target_path, verbose=self.__verbose))

            target_data: dict = { }
            target_value: list = re.compile(r"\$\{([\w.-]+)\}").findall(bs_data)

            for i, _ in enumerate(target_value):
                target_data[target_value[i]] = data[target_value[i]]

            for key, val in target_data.items():
                bs_data = re.sub(fr'\${{{key}}}', val, bs_data)

            bs_data = BeautifulSoup(bs_data, 'xml').prettify(
                formatter=XMLFormatter(indent=4)
            ).split(os.linesep)
            bs_data[0] += os.linesep

            fixed_data: list = [ ]
            i: int = 0
            for idx, element in enumerate(bs_data):
                if element.startswith(('<?', '<conf', '</conf')):
                    continue
                if not element.strip().startswith('<'):
                    fixed_data.append(element.strip())
                elif fixed_data and element.strip().startswith('</'):
                    fixed_data[-1] += element.strip()
                    bs_data[idx - 2] += fixed_data[i]
                    bs_data[idx] = None
                    bs_data[idx - 1] = None
                    i += 1

            bs_data = [__data for __data in bs_data if __data]

            FileUtils.write_to_file(
                self.__get_output_path(target) + 'config.xml',
                contents=bs_data, verbose=self.__verbose
            )

        # [Makefile]
        elif target.lower() == self.__TARGET_FILES[1].lower():
            target_data: list = FileUtils.read_file(
                target_path, verbose=self.__verbose
            )

            # Get the version number using regex
            old_version, new_version, idx = self.__get_version(
                list_data=target_data,
                regex=r'^version\s*[:=|=]+\s*(.*?)\s*$',
                config_data=data
            )

            if old_version == new_version:
                if self.__verbose:
                    Utils.info_msg(f'Version in "{target_path}" is up-to-date')
                return

            target_data[idx] = new_version + os.linesep
            FileUtils.write_to_file(
                target_path, contents=target_data,
                newline=False,
                verbose=self.__verbose
            )

            # After fix version in Makefile, this program will exit immediately.
            # If this program exit, it will send exit signal to Makefile.
            # In that way, Makefile will recognize the signal,
            #   and then will be terminated immediately.
            print(os.linesep * 2 + r'/!\ WARNING' + os.linesep + '-' * 70)
            Utils.info_msg(f'Version in "{target_path}" have been fixed, need restart!')
            print(
                f'Old version: "{old_version.split()[-1].strip()}"' + os.linesep + \
                f'New version: "{new_version.split()[-1].strip()}"' + os.linesep + \
                '-' * 70
            )
            sys.exit(-1)  # this should not be zero

        # [MANIFEST.MF]
        elif target.lower() == self.__TARGET_FILES[2].lower():
            contents: str = f'''
            Manifest-Version: 1.2
            Built-By: {data.get("author.name")}
            License-File: LICENSE
            Main-Class: {data.get("package.mainClass")}
            Program-Name: {data.get("package.name")}
            Program-Version: v%s
            ''' % self.__get_version(config_data=data, fixed_only=True)
            contents = [ct.strip() for ct in contents.splitlines() if ct][:-1]

            manifest_contents: list = FileUtils.read_file(
                target_path, verbose=self.__verbose
            )
            manifest_contents = [mf.strip() for mf in manifest_contents]

            if contents == manifest_contents:
                if self.__verbose:
                    Utils.info_msg(f'File "{self.__TARGET_FILES[2]}" is up-to-date')
                return

            FileUtils.write_to_file(
                target_path, contents=contents,
                verbose=self.__verbose
            )

            if self.__verbose:
                Utils.info_msg(f'File "{self.__TARGET_FILES[2]}" updated.')


        if self.__verbose:
            Utils.pr_banner('(END) FIX CONFIGURATION', newline=1)


    __all__ = ['run']



def main() -> None:
    """
    Main program.
    """
    opts_arg: tuple = ('-v', '--verbose', 'verbose',)

    # Checking the CLI arguments
    try:
        if len(sys.argv) > 2:
            raise RuntimeError('Too many arguments, need (1) argument')
        if len(sys.argv) == 2 and sys.argv[1] not in opts_arg:
            raise ValueError(f'Unknown options for value: "{sys.argv[1]}"')
    except RuntimeError as runtime_err:
        Utils.raise_error(runtime_err, -1, file=__file__)
    except ValueError as val_err:
        Utils.raise_error(val_err, 1, file=__file__)


    # Run the FixConfig program
    if len(sys.argv) == 2 and sys.argv[1] in opts_arg:
        FixConfig(verbose=True).run()
    else:
        FixConfig().run()



## === DRIVER === ##
if __name__ == '__main__':
    main()
