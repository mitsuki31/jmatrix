'''
This program will fix the JMatrix version for specified file(s),
which the real version is taken from 'config.xml'.

Fix(es):
    - MANIFEST.MF
    - Makefile

Created by Ryuu Mitsuki
'''

import os, sys, traceback
from bs4 import BeautifulSoup

def raise_error(ex: Exception, status: int = 1, tb: bool = True) -> None:
    '''Raise an error message then exit if status not equals to zero'''
    print('[jmatrix]', ex, os.linesep if tb else '')
    traceback.print_exc() if tb else None
    sys.exit(status) if status != 0 else None

def convert_to_BS(fp: str, type: str) -> BeautifulSoup:
    '''Convert given path into BeautifulSoup object'''
    contents = None  # create null object for file contents

    try:
        with open(fp, 'r') as file:
            contents = file.read()
    except FileNotFoundError as fe:
        raise_error(fe, 2)
    except Exception as e:
        raise_error(e, 1)

    return BeautifulSoup(contents, type)

def get_config_data(bs: BeautifulSoup) -> dict:
    '''Get all configuration data from given stream'''
    config_data: dict = { }  # Create empty dictionary

    for element in bs.find_all():
        if len(element.find_all()) != 0:
            child_dict = { }
            for child in element.find_all():
                if child.name == 'version':
                    child_dict_1 = { }
                    if not child.has_attr('type'):
                        try:
                            raise Exception(f"'type' is not defined in config data")
                        except Exception as e:
                            raise_error(e, 1)

                    child_dict_1['value'] = child.text.strip()
                    child_dict_1['type'] = child.get('type').strip()
                    child_dict[child.name] = child_dict_1
                else:
                    child_dict[child.name] = child.text.strip()
            config_data[element.name] = child_dict

    return config_data['configuration']

def fix_version(fp: str, cfg: dict, delim: str) -> None:
    '''Fix the version number in specified file'''
    contents = None           # Create an empty object for file contents
    idx: int = -1             # Index to store which line the version was declared from
    version: list = None      # Version number of given file will stored here as list
    hasChanged: bool = False  # Boolean to check the file has changed
    cfg_version: str = None   # Version from config data stored here

    # Get the file name by spliting the path with delimiter of system file separator
    filename: str = fp.split(os.sep)[-1] if len(fp.split(os.sep)) > 1 else fp


    try:
        # Check the dictionary from given config data
        if not cfg.get('version', None):
            msg: str = "Given config data does not have 'version' attribute"
            raise Exception(msg)
        else:
            if not 'value' in cfg['version']:
                msg: str = "Version does not have 'value' attribute"
                raise Exception(msg)
            elif not 'type' in cfg['version']:
                msg: str = "Version does not have 'type' attribute"
                raise Exception(msg)

            # Get the version number from config data
            if cfg['version']['type'] != 'release':
                cfg_version = '{}-{}.{}'.format(
                        cfg['version']['value'],
                        cfg['version']['type'],
                        cfg['beta_num']
                )
            else:
                cfg_version = cfg['version']['value']


        with open(fp, 'r') as file:
            contents = file.readlines()
    except FileNotFoundError as fe:
        raise_error(fe, 2)
    except Exception as e:
        raise_error(e, 1)
    else:
        # Check whether the declared version name is equals to given delimiter
        # If declared, then continue and get the version number
        for content in contents:
            if content.startswith(delim):
                idx = contents.index(content)
                version = content.split()
                break

        # If "idx" still equals to -1 after searching
        # the desired version delimiter, then it will throw an error
        if idx == -1:
            try:
                raise Exception('There was no "{}" declared in file: "{}"'.format(delim, filename))
            except Exception as e:
                raise_error(e, 1)

    # Check similarities for the version number from given file and
    # the version from given config data
    if version[-1] == cfg_version:
        return False # return False if the version number are same each other
    else:
        version[-1] = cfg_version
        contents[idx] = ' '.join(version) + os.linesep

        try:
            # Overwrite the file with new contents
            with open(fp, 'w') as file:
                [file.write(content) for content in contents]
        except FileNotFoundError as fe:
            raise_error(fe, 2)
        except Exception as e:
            raise_error(e, 1)
        else:
            return True


if __name__ == '__main__':
    # Files path
    config_path: str = (os.sep).join(['assets', 'configuration', 'config.xml'])
    makefile: str = 'Makefile'
    manifest: str = (os.sep).join(['META-INF', 'MANIFEST.MF'])
    delimiter: str = 'version'

    XML_DATA: BeautifulSoup = convert_to_BS(config_path, 'xml')
    CONFIG_DATA: dict = get_config_data(XML_DATA)

    fix_version(manifest, CONFIG_DATA, delimiter.capitalize())

    ## Note: Need restart after fix the version in Makefile
    if (fix_version(makefile, CONFIG_DATA, delimiter.upper())):
        print('[jmatrix] Successfully changing version at %s' %makefile)

        try:
            raise Exception('System aborted.')
        except Exception as e:
            raise_error(e, -1, False)
