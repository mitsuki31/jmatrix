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
INCLUDE_SRC ?= false
LINT        ?= false
VERBOSE     ?= false



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
CLR_PREFIX     :=
EXCLUDE_PKGS   := com.mitsuki.jmatrix.util

# Private and internal constants
__intern_LINT    :=
__intern_VERBOSE :=
__intern_INC_SRC :=


ifdef MAKE_DIR
include $(MAKE_DIR)/Func.mk
else
include $(shell pwd)/Func.mk
endif


# Call the `__get_sources` to initialize `SOURCES` variable
# and retrieve the paths to all Java source files in "src/main/java" directory.
$(eval $(call __get_sources,false))


# Replace the '.java' with '.class' and the directory with "target/classes"
CLASSES_FILES := $(patsubst $(JAVA_DIR)/%.java,$(CLASSES_DIR)/%.class,$(SOURCES))

# Colorize the prefix
CLR_PREFIX    := [$(call __clr_br,6,jmatrix)]

# Get the packages list using string manipulation
PACKAGES_LIST := $(subst /,.,$(basename $(subst $(JAVA_DIR)/,,$(SOURCES))))

JFLAGS        := -encoding UTF-8
