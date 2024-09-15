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


# Define a guard variable to prevent and protect from import recursion
ifndef __MAKE_SETUP_MK
__MAKE_SETUP_MK = 1

# Import: Func.mk
include $(or $(MAKE_DIR),make)/Func.mk

## :::::::::::::::: ##
##  User Options    ##
## :::::::::::::::: ##

# Set the default values to "false" except for FLAGS option
FLAGS       ?=
INCLUDE_SRC ?= false
LINT        ?= false
VERBOSE     ?= false


## :::::::::::::::: ##
##  Properties      ##
## :::::::::::::::: ##

# User can overrides these properties but with caution

ENCODING    ?= UTF-8
SOURCE_JDK  ?= 11
TARGET_JDK  ?= 11
MIN_MEMORY  ?= 32m
MAX_MEMORY  ?= 64m


## :::::::::::::::: ##
##  Constants       ##
## :::::::::::::::: ##

# Check whether JAVA_HOME exists then get neccessary Java commands
# from its "bin" directory.
ifdef JAVA_HOME
  JC           := $(JAVA_HOME)/bin/javac
  JDOC         := $(JAVA_HOME)/bin/javadoc
  JAR          := $(JAVA_HOME)/bin/jar
else
# Otherwise, use `type` command to search the Java commands,
# raise an error if the command not found.
  JC           := $(or $(shell command -v javac 2> /dev/null),\
                       $(call __raise_err,Fatal,Cannot find the `javac' command.\
                       Please ensure that JDK is installed correctly)\
                  )
  JDOC         := $(or $(shell command -v javadoc 2> /dev/null),\
                       $(call __raise_err,Fatal,Cannot find the `javadoc' command.\
                       Please ensure that JDK is installed correctly)\
                  )
  JAR          := $(or $(shell command -v jar 2> /dev/null),\
                       $(call __raise_err,Fatal,Cannot find the `jar' command.\
                       Please ensure that JDK is installed correctly)\
                  )
endif  # JAVA_HOME

# Test the Python command and check its existence
PY ?= $(or $(shell command -v python 2> /dev/null),\
		$(call __raise_err,Fatal,Cannot find the `python' command.\
    	Please ensure the Python is installed correctly)\
      )

# Import: Prop.mk
# Import this module only after the Python command checker
include $(or $(MAKE_DIR),make)/Prop.mk

### Flags
# Check if these constants has been defined to avoid redefine
# and avoid accidentally overriding by the default value
ifndef JCFLAGS
  JCFLAGS      := -encoding $(ENCODING) -source $(SOURCE_JDK) -target $(TARGET_JDK)
endif  # JCFLAGS

ifndef JARFLAGS
  # Simple flags: `cfm`
  JARFLAGS     := --create --file={JAR} --manifest={MANIFEST}
endif  # JARFLAGS

ifndef JDOCFLAGS
  JDOCFLAGS    := -author -version -protected -keywords -sourcepath {SRCPATH} -subpackages com.mitsuki.jmatrix \
                  -d {OUTPATH} -locale en_US -exclude {EXCLUDE} -docencoding $(ENCODING) \
                  -windowtitle {WIN_TITLE} {TAGS} -bottom {BOTTOM}
endif  ## JDOCFLAGS

ifndef MEMFLAGS
  # Minimum: 32M  |  Maximum: 64M. See MIN_MEMORY and MAX_MEMORY
  MEMFLAGS     := -Xms$(MIN_MEMORY) -Xmx$(MAX_MEMORY)
endif  # MEMFLAGS

### ::: Directories paths
SOURCE_DIR     := src/main
TEST_DIR       := src/test
TARGET_DIR     := target
DOCS_DIR       := docs
BUILDER_DIR    := tools/JMBuilder
JAVA_DIR       := $(SOURCE_DIR)/java
RESOURCE_DIR   := $(SOURCE_DIR)/resources
CLASSES_DIR    := $(TARGET_DIR)/classes
JAVADOC_OUT    := $(DOCS_DIR)/jmatrix-$(VERSION)
BUILDER_OUT    := $(TARGET_DIR)/_builder

### ::: Files paths
ORIG_MANIFEST  := META-INF/MANIFEST.MF
MANIFEST       := $(BUILDER_OUT)/$(ORIG_MANIFEST)
SOURCES_LIST   := $(TARGET_DIR)/generated-list/sourceFiles.lst
CLASSES_LIST   := $(TARGET_DIR)/generated-list/outputFiles.lst

### ::: Others
PREFIX         := [$(ARTIFACT_ID)]
CLR_PREFIX     := [$(call __clr_br,6,$(ARTIFACT_ID))]

# Check whether the current version is release version, zero if false, otherwise non-zero
IS_RELEASE     := $(if $(findstring 1,$(words $(subst -, ,$(VERSION)))),1,0)
EXCLUDE_PKGS   := com.mitsuki.jmatrix.internal
JAR_NAMES      := $(addprefix $(TARGET_DIR)/,                \
                      $(ARTIFACT_ID)-$(VERSION).jar          \
                      $(ARTIFACT_ID)-$(VERSION)-sources.jar  \
                  )

# Get all resources in "src/main/resources"
RESOURCES             := $(wildcard $(RESOURCE_DIR)/**/*)
RESOURCES_ONLY_DIR    := $(wildcard $(RESOURCE_DIR)/*)
ORIG_SETUP_PROPERTIES := $(filter %/setup.properties,$(RESOURCES))
SETUP_PROPERTIES      := $(BUILDER_OUT)/$(notdir $(ORIG_SETUP_PROPERTIES))

### Private and internal constants

# Lint option
ifndef __intern_LINT
  __intern_LINT    =
endif  # __intern_LINT

# Verbose option
ifndef __intern_VERBOSE
  __intern_VERBOSE =
endif  # __intern_VERBOSE

# Include sources option
ifndef __intern_INC_SRC
  __intern_INC_SRC =
endif  # __intern_INC_SRC

# Get current year using Python datetime module
__current_year     := $(shell $(PY) -c "import datetime as date; print(date.datetime.now().year)")

# Window title for HTML docs "JMatrix <VERSION> API"
__jdoc_WIN_TITLE   := "$(PROGNAME) $(VERSION) API"

# Custom Javadoc tags
__jdoc_CUSTOM_TAGS := -tag param                                    \
                      -tag return                                   \
                      -tag throws                                   \
                      -tag warning:a:"Warning:"                     \
                      -tag apiNote:ptcmf:"API Note:"                \
                      -tag implNote:ptcmf:"Implementation Note:"    \
                      -tag author                                   \
                      -tag license:pt:"License:"                    \
                      -tag see


# Custom bottom page; Must be closed with quotes!
__jdoc_BOTTOM      := 'Copyright &copy; $(INCEPTION_YEAR) - $(__current_year) <a href="$(AUTHOR_URL)">$(AUTHOR)</a>. All rights reserved.'


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


# Change neccessary values on JDOCFLAGS
JDOCFLAGS := $(subst {SRCPATH},$(JAVA_DIR),$(JDOCFLAGS))
JDOCFLAGS := $(subst {OUTPATH},$(JAVADOC_OUT),$(JDOCFLAGS))
JDOCFLAGS := $(subst {EXCLUDE},$(EXCLUDE_PKGS),$(JDOCFLAGS))
JDOCFLAGS := $(subst {WIN_TITLE},$(__jdoc_WIN_TITLE),$(JDOCFLAGS))
JDOCFLAGS := $(subst {TAGS},$(__jdoc_CUSTOM_TAGS),$(JDOCFLAGS))
JDOCFLAGS := $(subst {BOTTOM},$(__jdoc_BOTTOM),$(JDOCFLAGS))


endif  # __MAKE_SETUP_MK
