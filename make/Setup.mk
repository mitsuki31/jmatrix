# Copyright (c) 2023 Ryuu Mitsuki
#
# Licensed under the Apache License, Version 2.0 (the "License");
# you may not use this file except in compliance with the License.
# You may obtain a copy of the License at
#
#     http://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing, software
# distributed under the License is distributed on an "AS IS" BASIS,
# WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
# See the License for the specific language governing permissions and
# limitations under the License.
#

####  :: Setup.mk :: Setup and utilities file for Main.mk  ####


## :::::::::::::::: ##
##  User Options    ##
## :::::::::::::::: ##

FLAGS       ?=
INCLUDE-SRC ?=
LINT        ?=
VERBOSE     ?=



## :::::::::::::::: ##
##  Constants       ##
## :::::::::::::::: ##

# Directories paths
SOURCE_DIR     := src/main
TEST_DIR       := src/test
TARGET_DIR     := target
DOCS_DIR       := docs
PYTHON_DIR     := $(SOURCE_DIR)/python
JAVA_DIR       := $(SOURCE_DIR)/java
RESOURCE_DIR   := $(SOURCE_DIR)/resources
CLASSES_DIR    := $(TARGET_DIR)/classes
JAVADOC_OUT    := $(DOCS_DIR)/jmatrix-$(VERSION)

# Files paths
MANIFEST       := META-INF/MANIFEST.MF
JAR_FILE       := $(TARGET_DIR)/jmatrix-$(VERSION).jar
MAKE_USAGE_TXC := $(DOCS_DIR)/makefile-usage.txcc
MAKE_USAGE_TXT := $(DOCS_DIR)/makefile-usage.txt
SOURCES_LIST   := $(TARGET_DIR)/generated-list/sourceFiles.lst
CLASSES_LIST   := $(TARGET_DIR)/generated-list/outputFiles.lst



## :::::::::::::::: ##
##  Functions       ##
## :::::::::::::::: ##


# __get_sources Function
#
# This function retrieves the Java source files located in the "src/main/java" directory
# and stores them in the `SOURCES` variable. The behavior of this function can be controlled
# using the first argument.
#
# Usage:
#   $(eval $(call get_sources[,<boolean>]))
#
# Arguments:
#   boolean (optional):
#     If set to "true", the function will trim the path prefix "src/main/java"
#     from each source file and retain only the full package paths.
#
define __get_sources
  ifeq "$(1)" "true"
    SOURCES := $(shell                              \
      find $(SOURCE_DIR) -type f -name '*.java' |   \
      sed 's/$(subst /,\/,$(SOURCE_DIR)//'          \
    )
  else
    SOURCES := $(shell find $(SOURCE_DIR) -type -f name '*.java')
  endif
endef  # __get_sources


# __is_exist Function
#
# This function checks the existence of the specified directory or file path
# and returns a binary number representing whether the path exists (1) or not (0).
#
# Usage:
#   <COMMAND> $(call __is_exist,<path>)
#
# Arguments:
#   path:
#     The directory or file path to check for existence.
#
# Returns:
#   1: Indicates that the specified path exists.
#   0: Indicates that the specified path does not exist.
#
define __is_exist
$(if $(wildcard $(1)),1,0)
endef  # __is_exist
