'''
This program will fix the JMatrix version for specified file(s),
which the real version is taken from 'config.xml'.

Fix(es):
    - MANIFEST.MF
    - Makefile

Created by Ryuu Mitsuki
'''

import os, sys
from bs4 import BeautifulSoup

def raise_error(ex: Exception, status: int = 1) -> None:
    '''Raise an error message then exit if status not equal zero'''
    print('[jmatrix]', ex)
    sys.exit(status) if status != 0 else None


# Files path
pathXML: str = (os.sep).join(['assets', 'configuration', 'config.xml'])
file: str = 'Makefile'
pathManifest: str = (os.sep).join(['META-INF', 'MANIFEST.MF'])


'''Get a version number from config.xml'''
data = None
idx: int = 0

try:
    with open(pathXML, 'r') as XML:
        data = XML.read()
except FileNotFoundError as fe:
    raise_error(fe, 2)

BS_dat: BeautifulSoup = BeautifulSoup(data, 'xml')
version: str = BS_dat.find('version').text
release: str = BS_dat.find('version')['type']


'''Fix version for Makefile'''
try:
    with open(file, 'r') as make:
        data = make.readlines()
except FileNotFoundError as fe:
    raise_error(fe, 2)

Make_ver: str = None
for line in data:
    if line.startswith('VERSION'):
        idx = data.index(line)
        Make_ver = line.split()
        break

if Make_ver[2] != version:
    Make_ver[2] = version
    data[idx] = ' '.join(Make_ver) + '\n'

    try:
        with open(file, 'w') as make:
            [make.write(line) for line in data]
    except FileNotFoundError as fe:
        raise_error(fe, 2)
    else:
        print('[jmatrix] Successfully changing version at %s' %(file))
        try:
            raise Exception('System aborted\n')
        except Exception as e:
            print('[jmatrix]', e)
            sys.exit(-1)


'''Fix version for MANIFEST.MF'''
try:
    with open(pathManifest, 'r') as manifest:
        data = manifest.readlines()
except FileNotFoundError as fe:
    raise_error(fe, 2)

mf_ver: str = None
for line in data:
    if line.startswith('Version'):
        idx = data.index(line)
        mf_ver = line.split()
        break

if mf_ver[1] != version:
    mf_ver[1] = version
    data[idx] = ' '.join(mf_ver) + '\n'

    try:
        with open(pathManifest, 'w') as manifest:
            [manifest.write(line) for line in data]
    except FileNotFoundError as fe:
        raise_error(fe, 2)
