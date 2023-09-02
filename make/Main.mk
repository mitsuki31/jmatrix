#### --------------- ================================= ####
####  :: Main.mk ::   Main configuration for Makefile  ####
#### --------------- ================================= ####

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

include $(shell pwd)/make/Setup.mk


# Enable the linter if '.lint' is present in command line arguments or
# LINT variable are defined and the value is 'true'.
#
# For example:
#   $ make <TARGET> .lint
#
# It is equivalent with:
#   $ make <TARGET> LINT=true
#
ifeq ($(filter $(MAKECMDGOALS),.lint),.lint)
  MAKECMDGOALS  := $(strip $(subst .lint,,$(MAKECMDGOALS)))

  JFLAGS        += -Xlint:all -Xdoclint:all
  __intern_LINT := 1
  LINT          := true
else
  # Enable the linter with all checks if LINT is true
  ifeq ($(LINT),true)
    JFLAGS        += -Xlint:all -Xdoclint:all
    __intern_LINT := 1
  else
    JFLAGS        += -Xlint:deprecation,unchecked,cast -Xdoclint:html,syntax/protected
  endif
endif


# Enable the verbose output if '.verbose' is present in command line arguments or
# VERBOSE variable are defined and the value is 'true'.
#
# For example:
#   $ make <TARGET> .verbose
#
# It is equivalent with:
#   $ make <TARGET> VERBOSE=true
#
ifeq ($(filter $(MAKECMDGOALS),.verbose),.verbose)
  MAKECMDGOALS     := $(strip $(subst .verbose,,$(MAKECMDGOALS)))

  JFLAGS           += -verbose
  VERBOSE          := true
  __intern_VERBOSE := 1
else
  # Enable the verbose if VERBOSE is true
  ifeq ($(VERBOSE),true)
    JFLAGS           += -verbose
    __intern_VERBOSE := 1
  endif
endif


# Enable option to include the source files get archived if '.include-src' is present
# in command line arguments or INCLUDE_SRC variable are defined and
# the value is 'true'.
#
ifeq ($(filter $(MAKECMDGOALS),.include-src),.include-src)
  MAKECMDGOALS       := $(strip $(subst .include-src,,$(MAKECMDGOALS)))

  __intern_INC_SRC   := 1
else
  ifeq ($(INCLUDE_SRC),true)
    __intern_INC_SRC := 1
  endif
endif


## :::::::::::::::: ##
##  Target Rules    ##
## :::::::::::::::: ##


# Default target rule; If no target rule specified then display the help message
help:
	@cat $(word 1,$(MAKE_USAGE))

.PHONY: help


# We cannot do more further here, when the one of Java source file get changed
# or modified, then it will compile all source files again, even though it is only one of them.
# If you want more efficient and more easier on compilation, consider to use Maven instead.
$(CLASSES_FILES): $(SOURCES)
	$(eval JFLAGS += $(FLAGS) $(addprefix -J,$(MEMFLAGS)))

	$(info $(CLR_PREFIX) $(call __clr_br,5,LINT):$(if $(__intern_LINT),\
		$(call __clr_br,2,ENABLED),\
		$(call __clr_br,1,DISABLED)\
	))

	$(info $(CLR_PREFIX) Compiling all Java source files...)
	$(foreach src,$^,\
		$(info $(call __clr_br,3,   *) $(call __bold,$(notdir $(src))):\
		$(subst /,.,$(basename $(subst $(JAVA_DIR)/,,$(call __clr,6,$(src))))))\
	)

	@$(JC) -d $(CLASSES_DIR) $^ $(JFLAGS)



## :::::::::::::::: ##
##  Custom Flags    ##
## :::::::::::::::: ##

# WARNING
# -------
# Please be cautious when using these custom flags, as they are experimental and
# may lead to unexpected behaviors and errors. It is recommended to use the
# `<FLAGS>=<value>` format for a more reliable and predictable experience.
#
#
# DISADVANTAGES
# -------------
# When using these custom flags as the first target or as standalone rules, they will override
# the default rule (top rule). Make will attempt to treat the first argument (the custom flag)
# as a target rule and execute it. In such cases, Make may not build anything, resulting in
# no actions being taken, as the first argument is not actually a target rule.
#


# Enables the linter during compilation
#
# Usage:
#   make <TARGET> .lint
#
# Alternative usage:
#   make <TARGET> LINT=true
#
.lint:
# The `@:` does nothing, but to prevent the message from Make get printed
# Message: "Nothing to be done for '<TARGET>'"
	@:


# Enables the verbose output
#
# Usage:
#   make <TARGET> .verbose
#
# Alternative usage:
#   make <TARGET> VERBOSE=true
#
.verbose:
# The `@:` does nothing, but to prevent the message from Make get printed
# Message: "Nothing to be done for '<TARGET>'"
	@:


# Includes the source files to be archived
#
# It will generates two JAR files simultaneously, one containing the compiled classes
# and the other containing only source files. The JAR file that contains
# only source files will have the `-sources` suffix added to its name.
#
# Generated JAR files:
#   - jmatrix-<VERSION>.jar
#   - jmatrix-<VERSION>-sources.jar
#
# Usage:
#   make <TARGET> .include-src
#
# Alternative usage:
#   make <TARGET> INCLUDE_SRC=true
#
.include-src:
# The `@:` does nothing, but to prevent the message from Make get printed
# Message: "Nothing to be done for '<TARGET>'"
	@:
