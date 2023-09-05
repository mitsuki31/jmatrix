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


# Import: Setup.mk
include $(or $(MAKE_DIR),$(shell pwd)/make)/Setup.mk


## :::::::::::::::: ##
##  Functions       ##
## :::::::::::::::: ##
#
# Note:
# These functions are exclusive for this file only


# __pr_lint Function
#
# This function will displays the condition of LINT option to console.
#
# Usage:
#   $(call __pr_lint)
#
define __pr_lint
	$(info $(CLR_PREFIX) $(call __clr_br,5,LINT):$(if $(__intern_LINT),\
		$(call __clr_br,2,ENABLED),\
		$(call __clr_br,1,DISABLED)\
	))
endef


# __pr_verbose Function
#
# This function will displays the condition of VERBOSE option to console.
#
# Usage:
#   $(call __pr_verbose)
#
define __pr_verbose
	$(info $(CLR_PREFIX) $(call __clr_br,5,VERBOSE):$(if $(__intern_VERBOSE),\
		$(call __clr_br,2,ENABLED),\
		$(call __clr_br,1,DISABLED)\
	))
endef


# __pr_include_src Function
#
# This function will displays the condition of INCLUDE_SRC option to console.
#
# Usage:
#   $(call __pr_include_src)
#
define __pr_include_src
	$(info $(CLR_PREFIX) $(call __clr_br,5,INCLUDE_SRC):$(if $(__intern_INC_SRC),\
		$(call __clr_br,2,ENABLED),\
		$(call __clr_br,1,DISABLED)\
	))
endef



## :::::::::::::::: ##
##  Builders        ##
## :::::::::::::::: ##


compile: $(CLASSES_FILES)
	@:


package: compile
	$(call __pr_include_src)
	$(call __pr_verbose)
	$(call __info,)

	$(eval JARFLAGS := $(subst {MANIFEST},$(MANIFEST),$(JARFLAGS)) $(FLAGS))

	$(call __info,Copying the resources...)
	$(call __info,Got $(words $(shell find $(RESOURCES) -type f)) resources)
	@cp --recursive --preserve=all $(RESOURCES) $(CLASSES_DIR) \
		$(if $(__intern_VERBOSE),--verbose,)

	$(call __info,)
	$(call __info,Creating the JAR file...)
	@$(JAR) $(subst {JAR},$(word 1,$(JAR_NAMES)),$(JARFLAGS)) $(MANIFEST)     \
		$(wildcard LICENSE) -C $(CLASSES_DIR) .

# Create the second JAR file containing the source files only,
# if and only if the INCLUDE_SRC option is "true" (enabled)
ifeq ($(__intern_INC_SRC),1)
	$(call __info,)
	$(call __info,Creating the JAR file (sources)...)
	@$(JAR) $(subst {JAR},$(word 2,$(JAR_NAMES)),$(JARFLAGS)) $(MANIFEST) \
		$(wildcard LICENSE) -C $(JAVA_DIR) . $(addsuffix \ .,$(addprefix -C ,$(foreach f,$(RESOURCES),\
		$(addsuffix $(notdir $(f)),$(CLASSES_DIR)/))))
endif  # __intern_INC_SRC


build-docs:
# TODO: Implement the code for build-docs target
	@:

.PHONY: compile package build-docs



## :::::::::::::::: ##
##  Cleaners        ##
## :::::::::::::::: ##


clean:
	$(call __pr_verbose)
	$(call __info,)

# Clean the "target" (which is the output directory for compiled classes
# and the JAR files) recursively.
ifeq ($(call __is_exist,$(TARGET_DIR)),1)
	$(call __info,Cleaning up \"$(TARGET_DIR)\" directory recursively...)
	@-rm --recursive $(TARGET_DIR) $(if $(__intern_VERBOSE),--verbose,)
else
	$(call __info,Cleaned up \"$(TARGET_DIR)\" directory)
endif

	@echo "$(CLR_PREFIX) $(call __clr_br,2,All clean)"


cleanbin:
	$(call __pr_verbose)
	$(call __info,)

ifeq ($(call __is_exist,$(CLASSES_DIR)),1)
	$(call __info,Cleaning up \"$(CLASSES_DIR)\" directory recursively...)
	@-rm -r $(CLASSES_DIR) $(if $(__intern_VERBOSE),--verbose,)
else
	$(call __info,Cleaned up \"$(CLASSES_DIR)\" directory)
endif

	@echo "$(CLR_PREFIX) $(call __clr_br,2,All clean)"


cleandocs:
# TODO: ...
	@:

.PHONY: clean cleanbin cleandocs


# We cannot do any further from here, when the one of Java source file get changed
# or modified, then it will compile all source files again, even though it is only one of them.
# If you want more efficient and more easier on compilation, consider to use Maven instead.
#
$(CLASSES_FILES): $(SOURCES)
	$(call __pr_lint)
	$(call __pr_verbose)
	$(call __info,)

	$(call __info,Compiling all Java source files...)
	@$(foreach src,$^,\
		echo "$(CLR_PREFIX) $(call __bold,$(notdir $(src))) >>\
		$(subst /,.,$(basename $(subst $(JAVA_DIR)/,,$(call __clr,4,$(src)))))$(NORMAL)";\
	)

	@$(JC) -d $(CLASSES_DIR) $^ $(JCFLAGS) $(addprefix -J,$(MEMFLAGS))



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
