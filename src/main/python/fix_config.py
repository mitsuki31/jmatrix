'''
This program will fix the JMatrix configurations for specified file(s).

File(s):
    - MANIFEST.MF
    - Makefile
    - config.xml

Created by Ryuu Mitsuki
'''

import os, sys, json
from typing import Optional, Union
from bs4 import BeautifulSoup
from bs4.formatter import XMLFormatter
from utils import Utils
from file_utils import FileUtils

class FixConfig:
    __SOURCES_PATH:   str = f'src{os.sep}main{os.sep}'
    __TARGET_PATH:    str = f'target{os.sep}'
    __CLASSES_PATH:   str = __TARGET_PATH + f'classes{os.sep}'
    __CACHE_PATH:     str = __TARGET_PATH + f'.cache{os.sep}'
    __RESOURCES_PATH: str = __SOURCES_PATH + f'resources{os.sep}'

    __TARGET_FILES: tuple = ('config.xml', 'Makefile', 'MANIFEST.MF',)

    def __init__(self):
        pass

    @staticmethod
    def run(verbose: bool = False):
        FixConfig().__get_pom_data(cache=True, verbose=verbose)
        FixConfig().__fix_configuration(cached=f'{FixConfig().__CACHE_PATH}config.json', target='config.xml', verbose=verbose)
#        FixConfig().__fix_configuration(cached=f'{FixConfig().__CACHE_PATH}config.json', target='manifest.mf', verbose=verbose)


    def __create_cache(self, data: Union[dict, str], indent: int = 4, out: str = None, verbose: bool = False) -> None:
        """
        Create the cache of `data` and store it to `target/.cache/` directory with specified file name.
        The function supports creating two types of cache (JSON and string). For JSON cache,
          the `data` parameter should be a dictionary object.

        Parameters:
            - data: Union[dict, str]
                Data object that want to be cached.
                The type is optional, can be 'dict' or 'str'.
                Recommended use 'dict' object for creating JSON cache.

            - indent: int (default = 4)
                An integer value that specifies number of spaces for indentation in the JSON cache.

            - out: str (default = None)
                String representing the name of the output file.
                The output directory is `target/.cache/` directory.

            - verbose: bool (default = False)
                Boolean that specifies whether to print verbose output.

        Returns:
            None

        Raises:
            - RuntimeError
                If `out` parameter is empty.
        """
        FileUtils._FileUtils__check_directory(self.__CACHE_PATH, verbose=verbose)

        if verbose:
            print(os.linesep + '>>> [ CREATE CACHE ] <<<')
            Utils._Utils__info_msg(f'Creating cache for id:<{id(data)}> to "{out}"...')
        try:
            if out is None:
                msg = 'Please insert name for output file'
                raise RuntimeError(msg)
        except RuntimeError as re:
            Utils._Utils__raise_error(re, -1, file=__file__)

        if isinstance(data, str):
            try:
                with open(self.__CACHE_PATH + out, 'w', encoding='utf-8') as cache:
                    if verbose:
                        Utils._Utils__info_msg(os.linesep + f'Writing "{data.strip()}" -> \'{out}\'...')
                    cache.write(data)
            except Exception as e:
                Utils._Utils__raise_error(e, file=__file__)

        elif isinstance(data, dict):
            try:
                with open(self.__CACHE_PATH + out, 'w', encoding='utf-8') as cache:
                    if verbose:
                        print()
                        for k, v in data.items():
                            Utils._Utils__info_msg(f'Writing ("{k}", "{v}") -> \'{out}\'...')
                    json.dump(data, cache, indent=indent)
                    cache.write(os.linesep)
            except Exception as e:
                Utils._Utils__raise_error(e, file=__file__)
            else:
                if verbose:
                    print()
                    Utils._Utils__info_msg(f'Cache created, saved in "{self.__CACHE_PATH}{out}".')


    def __get_pom_data(self, cache: bool = True, verbose: bool = False) -> Optional[dict]:
        """
        This function is used to retrieve configuration data from 'pom.xml'.

        Parameters:
            - cache: bool (default = True)
                Boolean that specifies whether to create a cache file for the retrieved data or not.
                If filename is 'config.xml' or any values that same, would be cached with file name 'config.json'.

            - verbose: bool (default = False)
                Boolean that specifies whether to print verbose output.

        Returns:
            If `cache` is False, the function returns a dictionary containing the configuration data.
            Otherwise, the function returns None.
        """
        pom_xml:     str  = 'pom.xml'
        config_data: dict = { }

        if verbose:
            print(os.linesep + '>>> [ GET CONFIG DATA ] <<<')

        try:
            from bs4 import BeautifulSoup
        except ModuleNotFoundError as me:
            Utils._Utils__info_msg('Please install all the requirements first.' + os.linesep)
            Utils._Utils__raise_error(me, file=__file__)

        FileUtils._FileUtils__check_file(pom_xml)
        bs_data: BeautifulSoup = None
        bs_data = Utils._Utils__convert_to_BS(pom_xml, verbose=verbose)

        if verbose:
            Utils._Utils__info_msg('Filtering contents...')
            print(os.linesep + '>> ' + '-'*30)
            print(os.linesep + '<contents>')

        for element in bs_data.find_all():
            for child in element.find_all():
                if child.name == 'properties':
                    for child_1 in child.find_all():
                        if not child_1.name.startswith('project'):
                            config_data[child_1.name] = child_1.text.strip()
                            if verbose:
                                print(' '*4 + f'<{child_1.name}>', child_1.text.strip(), f'</{child_1.name}>')

        for elm in bs_data.find_all('groupId'):
            if elm.text.strip().split('.')[-1] in config_data['author.name'].lower():
                config_data['project.' + elm.name] = elm.text.strip()
                if verbose:
                    print(' '*4 + f'<{elm.name}>', elm.text.strip(), f'</{elm.name}>')
        for elm in bs_data.find_all('artifactId'):
            if elm.text.strip().split('-')[0] == config_data['package.name'].lower():
                config_data['project.' + elm.name] = elm.text.strip()
                if verbose:
                    print(' '*4 + f'<{elm.name}>', elm.text.strip(), f'</{elm.name}>')

        if verbose:
            print('</contents>' + os.linesep)
            print('>> ' + '-'*30 + os.linesep)
            Utils._Utils__info_msg('All contents filtered.')

        if cache:
            self.__create_cache(config_data, indent=4, out='config.json', verbose=verbose)
        else:
            return config_data


    def __fix_configuration(self, data: dict = None, cached: str = None, target: str = None, verbose: bool = False) -> None:
        """
        Fix the configurations of specified target file.

        Parameters:
            - data: dict
                The dictionary contains new configuration data.

            - cached: str
                File path to the cached new configuration data.

            - target: str
                The target file that want to fix it's configurations.

            - verbose: bool
                Whether to print detailed messages while fixing the configurations.

        Returns:
            None

        Raises:
            - ValueError
                If the file path is empty.

            - FileNotFoundError
                If file path to cached configuration data does not exist.
        """
        with_cache:  bool   = False
        target_path: str    = None
        target_data: object = None
        output_dir:  str    = None

        if target.lower() == self.__TARGET_FILES[0]:
            output_dir = self.__CLASSES_PATH + f'configuration{os.sep}'
        elif target.lower() == self.__TARGET_FILES[1].lower():
            output_dir = self.__TARGET_FILES[1]
        elif target.lower() == self.__TARGET_FILES[2].lower():
            output_dir = f'MANIFEST{os.sep}' + self.__TARGET_FILES[2]

        FileUtils._FileUtils__check_directory(output_dir, verbose=verbose)

        if verbose:
            print(os.linesep + '>>> [ FIX CONFIGURATION ] <<<')
        if data is None or len(data) == 0:
            with_cache = True
            if verbose:
                Utils._Utils__info_msg(f'Using cached configuration data "{cached}".')

        try:
            if (data is None or len(data) == 0) and cached is None:
                msg = 'Given config data is empty, please specify path to cached config'
                raise RuntimeError(msg)
            elif target.lower() is None or not target in [target_file.lower() for target_file in self.__TARGET_FILES]:
                msg = 'Please specify the target file'
                raise RuntimeError(msg)
            elif target.lower() in [target_file.lower() for target_file in self.__TARGET_FILES]:
                # config.xml
                if target.lower() == self.__TARGET_FILES[0]:
                    target_path = self.__RESOURCES_PATH + f'configuration{os.sep}' + self.__TARGET_FILES[0]
                # Makefile
                elif target.lower() == self.__TARGET_FILES[1].lower():
                    target_path = self.__TARGET_FILES[1]
                # MANIFEST.MF
                elif target.lower() == self.__TARGET_FILES[2].lower():
                    target_path = f'META-INF{os.sep}' + self.__TARGET_FILES[2]
        except RuntimeError as re:
            Utils._Utils__raise_error(re, -1, file=__file__)

        if with_cache:
            try:
                if not os.path.exists(cached):
                    msg = 'Path to cached configuration data does not exist'
                    raise FileNotFoundError(msg)
                elif os.path.exists(cached) and not os.path.isfile(cached):
                    msg = 'Path to cached configuration data is a directory'
                    raise FileNotFoundError(msg)

                with open(cached, 'r', encoding='utf-8') as cache:
                    data = json.load(cache)
            except Exception as e:
                Utils._Utils__raise_error(e, 1, file=__file__)

        # Check the target file
        if target.lower() == self.__TARGET_FILES[0]:
            import re

            target_data: dict = { }
            bs_data = repr(Utils._Utils__convert_to_BS(target_path, verbose=verbose))
            pattern: re.Pattern = re.compile(r"\$\{([\w.-]+)\}")

            target_value: list = pattern.findall(bs_data)

            for i in range(len(target_value)):
                target_data[target_value[i]] = data[target_value[i]]

            for var, val in target_data.items():
                bs_data = re.sub(fr'\${{{var}}}', val, bs_data)

            xml_formatter = XMLFormatter(indent=4)
            bs_data = BeautifulSoup(bs_data, 'xml').prettify(formatter=xml_formatter).split(os.linesep)
            bs_data[0] += os.linesep

            fixed_data: list = [ ]
            i: int = 0
            for idx, element in enumerate(bs_data):
                if not element.startswith(('<?', '<conf', '</conf')):
                    if not element.strip().startswith('<'):
                        fixed_data.append(element.strip())
                    elif fixed_data and element.strip().startswith('</'):
                        fixed_data[-1] += element.strip()
                        bs_data[idx - 2] += fixed_data[i]
                        bs_data[idx] = None; bs_data[idx - 1] = None
                        i += 1

            bs_data = [data for data in bs_data if data is not None]

            FileUtils._FileUtils__write_to_file(output_dir + 'config.xml', contents=bs_data, verbose=verbose)

        elif target.lower() == self.__TARGET_FILES[1].lower():
            target_data: list = FileUtils._FileUtils__read_file(target_path, _list=True, verbose=verbose)

            print(target_data)

        elif target.lower() == self.__TARGET_FILES[2].lower():
            target_data: list = FileUtils._FileUtils__read_file(target_path, _list=True, verbose=verbose)

            print(target_data)



## === DRIVER === ##
if __name__ == '__main__':
    verbose:  bool  = False
    opts_arg: tuple = ('-v', '--verbose', 'verbose',)

    # Checking the CLI arguments
    try:
        msg: str = None  # Empty message variable for exception message

        if len(sys.argv) > 2:
            msg = 'Too many arguments, need (1) argument'
            raise IndexError(msg)
        elif len(sys.argv) == 2 and sys.argv[1] not in opts_arg:
            msg = f'Unknown options for value: "{sys.argv[1]}"'
            raise ValueError(msg)
    except Exception as e: # Catch all exceptions that could occurs
        Utils._Utils__raise_error(e, file=__file__)
    else:
        if len(sys.argv) == 2 and sys.argv[1] in opts_arg:
            verbose = True
        else:
            verbose = False

    FixConfig.run(verbose=verbose)
