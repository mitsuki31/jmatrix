#### ---------------- ====================================== ####
####  :: Setup.mk ::   Setup and utilities file for globals  ####
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


# Import: Func.mk
include $(or $(MAKE_DIR),$(shell pwd)/make)/Func.mk

# Import: Prop.mk
include $(or $(MAKE_DIR),$(shell pwd)/make)/Prop.mk


# Prevent null or empty values for both version and program name
# If these constants undefined, warn user and then use their default values instead
#
ifndef PROGNAME
  $(call __warn,PROGNAME constant are not defined correctly. Overriden by the default value)
  PROGNAME := jmatrix
endif  # PROGNAME

ifndef VERSION
  $(call __warn,VERSION constant are not defined correctly. Overriden by the default value)
  VERSION  := x.x.x-SNAPSHOT
endif  # VERSION



## :::::::::::::::: ##
##  User Options    ##
## :::::::::::::::: ##

# Set the default values to "false" except for FLAGS option
FLAGS       ?=
INCLUDE_SRC ?= false
LINT        ?= false
VERBOSE     ?= false



## :::::::::::::::: ##
##  Constants       ##
## :::::::::::::::: ##

### Java commands
JC             := javac
JAR            := jar
JDOC           := javadoc

### Flags
# Check if these constants has been defined to avoid redefine
# and avoid accidentally overriding by the default value
ifndef JCFLAGS
  JCFLAGS      :=
endif  # JCFLAGS

ifndef JARFLAGS
  # Simple flags: `cfm`
  JARFLAGS     := --create --file={JAR} --manifest={MANIFEST}
endif  # JARFLAGS

ifndef JDOCFLAGS
  JDOCFLAGS    :=
endif  ## JDOCFLAGS

ifndef MEMFLAGS
  # Minimum: 32M  |  Maximum: 64M
  MEMFLAGS     := -Xms32m -Xmx64m
endif  # MEMFLAGS


### ::: Directories paths
SOURCE_DIR     := src/main
TEST_DIR       := src/test
TARGET_DIR     := target
DOCS_DIR       := docs
PYTHON_DIR     := $(SOURCE_DIR)/python
JAVA_DIR       := $(SOURCE_DIR)/java
RESOURCE_DIR   := $(SOURCE_DIR)/resources
CLASSES_DIR    := $(TARGET_DIR)/classes
JAVADOC_OUT    := $(DOCS_DIR)/jmatrix-$(VERSION)

### ::: Files paths
MANIFEST       := META-INF/MANIFEST.MF
MAKE_USAGE     := $(addprefix $(DOCS_DIR)/,makefile-usage.txcc makefile-usage.txt)
SOURCES_LIST   := $(TARGET_DIR)/generated-list/sourceFiles.lst
CLASSES_LIST   := $(TARGET_DIR)/generated-list/outputFiles.lst

### ::: Others
PREFIX         := [$(PROGNAME)]
CLR_PREFIX     := [$(call __clr_br,6,jmatrix)]

# Check whether the current version is release version, zero if false, otherwise non-zero
IS_RELEASE     := $(if $(findstring 1,$(words $(subst -, ,$(VERSION)))),1,0)
EXCLUDE_PKGS   := com.mitsuki.jmatrix.util
JAR_NAMES      := $(addprefix $(TARGET_DIR)/,             \
                      $(PROGNAME)-$(VERSION).jar          \
                      $(PROGNAME)-$(VERSION)-sources.jar  \
                  )
# Retrieve all resources directories in "src/main/resources"
RESOURCES      := $(wildcard $(RESOURCE_DIR)/*)


### Private and internal constants
ifndef __intern_LINT
  __intern_LINT    :=
endif # __intern_LINT

ifndef __intern_VERBOSE
  __intern_VERBOSE :=
endif # __intern_VERBOSE

ifndef __intern_INC_SRC
  __intern_INC_SRC :=
endif # __intern_INC_SRC




# Call the `__get_sources` to initialize `SOURCES` variable
# and retrieve the paths to all Java source files in "src/main/java" directory.
$(eval $(call __get_sources,false))


# Replace the '.java' with '.class' and the directory with "target/classes"
#
# This also can be done using:
#  $(subst $(JAVA_DIR),$(CLASSES_DIR),$(SOURCES:.java=.class))
#
CLASSES_FILES  := $(patsubst $(JAVA_DIR)/%.java,$(CLASSES_DIR)/%.class,$(SOURCES))

# Get the packages list using string manipulation
PACKAGES_LIST  := $(subst /,.,$(basename $(subst $(JAVA_DIR)/,,$(SOURCES))))
