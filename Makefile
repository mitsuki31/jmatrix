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

# It is NOT RECOMMENDED to use the absolute path, Make are ineffectively
# handles directories and files with spaces, it will treats them as a list of values
# rather than a single path of directory.
MAKE_DIR     := make
SETUP_MAKE   := $(MAKE_DIR)/Setup.mk
MAIN_MAKE    := $(MAKE_DIR)/Main.mk
MAKE_USAGE   := $(addprefix docs/make/,makefile-usage.txcc makefile-usage.txt)
MKFLAGS      := --no-print-directory --silent --file {FILE}
CUSTOMGOALS  := $(MAKECMDGOALS)

ifeq ($(wildcard $(MAKE_DIR)),)
  $(error Cannot import internal modules from '$(abspath $(MAKE_DIR))')
endif  # wildcard : $(MAKE_DIR)

# Import: Func.mk
include $(MAKE_DIR)/Func.mk

### ---------------- NOTE ---------------- ###
# Load 'Setup.mk' only when the user specifies targets, excluding 'help'
# or when nothing is specified (triggering the default 'help' rule).
# This approach optimizes performance, preventing unnecessary module imports,
# notably the time-consuming 'Setup.mk' with its extensive property
# initialization via the builder, especially crucial when users only seek
# to display the help message.
ifneq ($(words $(CUSTOMGOALS)),0)
  ifneq ($(firstword $(CUSTOMGOALS)),help)
    include $(SETUP_MAKE)
  endif  # filter
endif  # words

# Enable the linter if '.lint' in command line arguments or
# LINT variable are defined and the value is 'true'.
#
# For example:
#   $ make <TARGET> .lint
#
# It is equivalent with:
#   $ make <TARGET> LINT=true
#
ifeq ($(findstring .lint,$(CUSTOMGOALS)),.lint)
  CUSTOMGOALS   := $(filter-out .lint,$(CUSTOMGOALS))

  JCFLAGS       += -Xlint:all -Xdoclint:all
  JDOCFLAGS     += -Xdoclint:all

  LINT          := true
  __intern_LINT = 1
else
  # Enable the linter with all checks if LINT is true
  ifeq ($(LINT),true)
    JCFLAGS       += -Xlint:all -Xdoclint:all
    JDOCFLAGS     += -Xdoclint:all

    __intern_LINT = 1
  else
    JCFLAGS       += -Xlint:deprecation,unchecked,cast -Xdoclint:html,syntax/protected
    JDOCFLAGS     += -Xdoclint:html,syntax
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
ifeq ($(findstring .verbose,$(CUSTOMGOALS)),.verbose)
  CUSTOMGOALS      := $(filter-out .verbose,$(CUSTOMGOALS))

  JCFLAGS          += -verbose
  JARFLAGS         += --verbose
  JDOCFLAGS        += -verbose

  VERBOSE          := true
  __intern_VERBOSE = 1
else
  # Enable the verbose if VERBOSE is true
  ifeq ($(VERBOSE),true)
    JCFLAGS           += -verbose
    JARFLAGS          += --verbose
    JDOCFLAGS         += -verbose

    __intern_VERBOSE  = 1
  else
    JDOCFLAGS         += -quiet
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
ifeq ($(findstring .include-src,$(CUSTOMGOALS)),.include-src)
  CUSTOMGOALS        := $(filter-out .include-src,$(CUSTOMGOALS))

  INCLUDE_SRC        := true
  __intern_INC_SRC   = 1
else
  ifeq ($(INCLUDE_SRC),true)
    __intern_INC_SRC = 1
  endif
endif

# Default target rule; If no target rule specified then display the help message.
.DEFAULT_GOAL: help
help:
ifneq ($(or $(RAW),$(NOCC)),true)
    # User could pipe this (`cat`) command to `less` command with:
    #   $ make [help] | less -r
    #
    # And must specify `-r` or `--raw` flag to output the raw control-characters.
    #
	$(call __help,1)
else
    # There is an exception for this. If user defined a variable called `RAW`,
    # from the command line then the raw or original version without raw control-characters
    # will be displayed, and it does not needs the `-r` or `--raw` flag.
    #
    # The command will looks like this:
    #   $ make [help] RAW=true | less
    #
	$(call __help,2)
endif

.PHONY: Makefile help

### TODO: Import all necessary variables from the Setup module
###       so that increasing performance when run the Main module
###       and no need to import the Setup module again in the Main module.
# Exports
export LINT VERBOSE INCLUDE_SRC FLAGS
export JCFLAGS JARFLAGS JDOCFLAGS
export __intern_LINT __intern_VERBOSE __intern_INC_SRC
export MAKE_DIR  # Suppress the warning


# A variable used to signal whether the Make has been initialized and
# currently on running stat. This variable also prevent some messages being printed
# when user not specified any target rules or targetting only the `help` target.
__mk_init__ =

# Accept any target rules (including undefined ones)
# Then send all target rules to another Make file
#
%:
    # These messages will be printed when __init__ still an empty variable
    # i.e., does not have any value yet.
	$(if $(__mk_init__),,                                                                                                                     \
		$(eval __mk_init__ = 1)                                                                                                               \
		$(info $(CLR_PREFIX) $(call __clr,6,------------------------------------------------------------------------))                        \
		$(info $(CLR_PREFIX) $(call __bold,Project): $(call __clr,4,$(ARTIFACT_ID)-$(VERSION) $(call __clr,3,($(GROUP_ID):$(ARTIFACT_ID)))))  \
		$(info $(CLR_PREFIX) $(call __bold,Author): $(call __clr,4,$(AUTHOR) $(call __clr,3,($(notdir $(AUTHOR_URL))))))                      \
		$(info $(CLR_PREFIX) $(call __bold,License): $(call __clr,4,$(LICENSE)))                                                              \
		$(info $(CLR_PREFIX) $(call __clr,6,------------------------------------------------------------------------))                        \
	)

    # Skip this code below from the conditional check and allow the custom flags
    # to pass through to the "Main.mk" file. Otherwise, it will display a potentially
    # distracting message like: "make: '.lint' is up to date."
	@+$(MAKE) $(subst {FILE},$(MAIN_MAKE),$(MKFLAGS)) $@
