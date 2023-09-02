#### ---------------- ====================================== ####
####  :: Setup.mk ::   Setup and utilities file for Main.mk  ####
#### ---------------- ====================================== ####

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


## :::::::::::::::: ##
##  User Options    ##
## :::::::::::::::: ##

FLAGS       ?=
INCLUDE_SRC ?=
LINT        ?=
VERBOSE     ?=



## :::::::::::::::: ##
##  Constants       ##
## :::::::::::::::: ##

# Compiler and flags
JC             := javac
JFLAGS         :=
MEMFLAGS       := -Xms32m -Xmx128m

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
MAKE_USAGE     := $(DOCS_DIR)/makefile-usage.txcc $(DOCS_DIR)/makefile-usage.txt
SOURCES_LIST   := $(TARGET_DIR)/generated-list/sourceFiles.lst
CLASSES_LIST   := $(TARGET_DIR)/generated-list/outputFiles.lst

# Others
PREFIX         := [jmatrix]
EXCLUDE_PKGS   := com.mitsuki.jmatrix.util
COLORS         := \033[31m \033[32m \033[33m \033[34m \033[35m \
                  \033[36m \033[37m
COLORS_BR      := \033[1;91m \033[1;92m \033[1;93m \033[1;94m \033[1;95m \
                  \033[1;96m \033[1;97m

# Private and internal constants
__intern_LINT    :=
__intern_VERBOSE :=



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
      find $(JAVA_DIR) -type f -name '*.java' |     \
      sed 's/^$(subst /,\/,$(JAVA_DIR)/)//'         \
    )
  else
    SOURCES := $(shell find $(JAVA_DIR) -type f -name '*.java')
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


# __help Function
#
# This function prints a help message based on the provided argument, where
# 0 < N < 3 (N is the given number). The help message is read from a list
# called `MAKE_USAGE`. Users can optionally pipe the output of this function
# into another command (e.g., `less`) for easier viewing.
#
# Usage:
#   $(call __help,1) [ | <COMMAND>]
#
# Arguments:
#   integer:
#     An integer value specifying which help message to display.
#
define __help
	@cat $(word $(1),$(MAKE_USAGE))
endef  # __help


# __clr Function
#
# This function prints the given message with the specified color. It uses ANSI escape
# codes to apply the color to the message. The color is selected based on the argument
# provided.
#
# Expected colors with their indices:
#
#   1: Red
#   2: Green
#   3: Yellow
#   4: Blue
#   5: Purple
#   6: Cyan
#   7: White
#
# Usage:
#   @echo $(call __clr,<index>,<message>)
#
# Arguments:
#   index:
#     An integer value representing the color index (0 < index < 8).
#
#   message:
#     The message to be printed with the specified color.
#
define __clr
$(shell printf "$(word $(1),$(COLORS))$(2)\033[0m")
endef  # __clr


# __clr_br Function
#
# This function prints the given message with a brighter version of the specified color.
# It uses ANSI escape codes to apply the color to the message. The color is selected based
# on the argument provided.
#
# Expected colors with their indices:
#
#   1: Red
#   2: Green
#   3: Yellow
#   4: Blue
#   5: Purple
#   6: Cyan
#   7: White
#
# Usage:
#   @echo $(call __clr_br,<index>,<message>)
#
# Arguments:
#   index:
#     An integer value representing the color index (0 < index < 8).
#
#   message:
#     The message to be printed with the specified color.
#
define __clr_br
$(shell printf "$(word $(1),$(COLORS_BR))$(2)\033[0m")
endef  # __clr_br
