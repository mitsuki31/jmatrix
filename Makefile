#### ---------------- ================================= ####
####  :: Makefile ::   Main driver of Make for JMatrix  ####
#### ---------------- ================================= ####

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


MAKE_DIR     := $(shell pwd)/make
MAIN_MAKE    := $(MAKE_DIR)/Main.mk
MKFLAGS      := --no-print-directory --silent --file {FILE}
CUSTOMGOALS  := $(MAKECMDGOALS)


# Imports
ifneq ($(wildcard $(MAKE_DIR)),)
include $(MAKE_DIR)/Setup.mk
__HAS_IMPORTED = 1
export __HAS_IMPORTED
else
$(error $(shell printf "\033[1;91mFatal\033[0m"):\
	Cannot import neccessary files from "$(MAKE_DIR)". No such file or directory)
endif


# Enable the linter if '.lint' in command line arguments or
# LINT variable are defined and the value is 'true'.
#
# For example:
#   $ make <TARGET> .lint
#
# It is equivalent with:
#   $ make <TARGET> LINT=true
#
ifeq ($(filter $(CUSTOMGOALS),.lint),.lint)
  CUSTOMGOALS   := $(strip $(subst .lint,,$(CUSTOMGOALS)))

  JCFLAGS       += -Xlint:all -Xdoclint:all
  JDOCFLAGS     += -Xdoclint:all

  LINT          := true
  __intern_LINT := 1
else
  # Enable the linter with all checks if LINT is true
  ifeq ($(LINT),true)
    JCFLAGS       += -Xlint:all -Xdoclint:all
    JDOCFLAGS     += -Xdoclint:all

    __intern_LINT := 1
  else
    JCFLAGS       += -Xlint:deprecation,unchecked,cast -Xdoclint:html,syntax/protected
    JDOCFLAGS     += -Xdoclint:html,syntax/protected
  endif
endif


# Enable the verbose output if '.verbose' in command line arguments or
# VERBOSE variable are defined and the value is 'true'.
#
# For example:
#   $ make <TARGET> .verbose
#
# It is equivalent with:
#   $ make <TARGET> VERBOSE=true
#
ifeq ($(filter $(CUSTOMGOALS),.verbose),.verbose)
  CUSTOMGOALS      := $(strip $(subst .verbose,,$(CUSTOMGOALS)))

  JCFLAGS          += -verbose
  JARFLAGS         += --verbose
  JDOCFLAGS        += -verbose

  VERBOSE          := true
  __intern_VERBOSE := 1
else
  # Enable the verbose if VERBOSE is true
  ifeq ($(VERBOSE),true)
    JCFLAGS           += -verbose
    JARFLAGS          += --verbose
    JDOCFLAGS         += -verbose

    __intern_VERBOSE  := 1
  endif
endif


# Enable option to include the source files get archived if '.include-src' is present
# in command line arguments or INCLUDE_SRC variable are defined and
# the value is 'true'.
#
# For example:
#   $ make <TARGET> .include-src
#
# It is equivalent with:
#   $ make <TARGET> INCLUDE_SRC=true
#
ifeq ($(filter $(CUSTOMGOALS),.include-src),.include-src)
  CUSTOMGOALS        := $(strip $(subst .include-src,,$(CUSTOMGOALS)))

  INCLUDE_SRC        := true
  __intern_INC_SRC   := 1
else
  ifeq ($(INCLUDE_SRC),true)
    __intern_INC_SRC := 1
  endif
endif



# Default target rule; If no target rule specified then display the help message
help:
	@cat $(word 1,$(MAKE_USAGE))

.PHONY: help


# Exports
export LINT VERBOSE INCLUDE_SRC FLAGS
export JC JAR JDOC JCFLAGS JARFLAGS JDOCFLAGS
export __intern_LINT __intern_VERBOSE __intern_INC_SRC
export TARGET_DIR CLASSES_DIR JAVADOC_OUT DOCS_DIR MAKE_DIR JAVA_DIR
export SOURCES CLASSES_FILES RESOURCES JAR_NAMES MANIFEST
export PREFIX CLR_PREFIX
export __is_exist




$(info $(CLR_PREFIX) $(call __clr,6,-------------------------------------------------------------------------))
$(info $(CLR_PREFIX) $(call __bold,Project): $(call __clr,4,$(PROGNAME)-$(VERSION)))
$(info $(CLR_PREFIX) $(call __bold,Author): $(call __clr,4,$(AUTHOR)))
$(info $(CLR_PREFIX) $(call __clr,6,-------------------------------------------------------------------------))


# Accept any target rules (including undefined ones)
# Then send all target rules to another Make file
%:
	@$(if $(findstring $@,$(CUSTOMGOALS)),\
		echo "$(CLR_PREFIX) $(call __bold,JOB START): $(call __clr_br,3,$@)",\
	)

# Leave this code unchecked by if statement and let the custom flags
# flow to MAIN_MAKE ("Main.mk") file. If not, it will prints a little
# annoying message, for example: "make: '.lint' is up to date."
	@+$(MAKE) $(subst {FILE},$(MAIN_MAKE),$(MKFLAGS)) $@

	@$(if $(findstring $@,$(CUSTOMGOALS)),\
		echo "$(CLR_PREFIX) $(call __bold,JOB DONE): $(call __clr_br,3,$@)"; \
		echo "$(CLR_PREFIX) $(call __clr,6,-------------------------------------------------------------------------)",\
	)
