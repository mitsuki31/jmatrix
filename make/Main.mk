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
### TODO: Remove this when all necessary variables has correctly
###       imported from the Makefile within the project's root directory.
include $(or $(MAKE_DIR),$(CURDIR)/make)/Setup.mk


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
endef  # __pr_lint


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
endef  # __pr_verbose


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
endef  # __pr_include_src


# __job_msg
#
# This function prints the currently running job.
#
# Usage:
#   $(call __job_msg,<job>)
#
# Arguments:
#   job:
#     The job name.
#
define __job_msg
	$(info $(CLR_PREFIX) $(call __bold,-----[) $(call __clr,2,$(1)) $(call __bold,::) $(call __clr_br,4,$(ARTIFACT_ID)) $(call __bold,]-----))
	$(info $(CLR_PREFIX))
endef  # __job_msg


## :::::::::::::::: ##
##  Builders        ##
## :::::::::::::::: ##


compile: $(CLASSES_FILES) ;

package: compile | $(SOURCES) $(ORIG_MANIFEST) $(ORIG_SETUP_PROPERTIES) $(RESOURCES)
	$(call __job_msg,$@)

	$(call __pr_include_src)
	$(call __pr_verbose)
	$(call __info,)

	$(eval JARFLAGS := $(subst {MANIFEST},$(MANIFEST),$(JARFLAGS)) $(FLAGS))

	$(call __info,Copying $(words $(RESOURCES)) resources to '$(CURDIR)/$(CLASSES_DIR)'...)
	@cp --recursive --preserve=all $(RESOURCES_ONLY_DIR) $(CLASSES_DIR) \
		$(if $(__intern_VERBOSE),--verbose,)
	$(call __info,)

    # Fix 'setup.properties'
	$(call __info,Initiating '$(notdir $(SETUP_PROPERTIES))' file...)
	PYTHONPATH="$(BUILDER_DIR):$PYTHONPATH" $(PY) -m jmbuilder --fix-properties \
		pom.xml $(ORIG_SETUP_PROPERTIES) $(SETUP_PROPERTIES)
    # Overwrite the existing 'setup.properties' file in 'target/classes' directory
	@cp --preserve=all $(SETUP_PROPERTIES) $(CLASSES_DIR)/$(subst $(wildcard $(RESOURCE_DIR)),,$(dir $(ORIG_SETUP_PROPERTIES)))

    # Fix 'MANIFEST.MF'
	$(call __info,Initiating '$(notdir $(MANIFEST))' file...)
	PYTHONPATH="$(BUILDER_DIR):$PYTHONPATH" $(PY) -m jmbuilder --fix-manifest \
		pom.xml $(ORIG_MANIFEST) $(MANIFEST)
    # For MANIFEST file only, no need to copy to 'classes' directory

	$(call __info,$(call __clr,2,All initiated.))

	$(call __info,)
	$(call __info,Generating the JAR file...)
	@$(JAR) $(subst {JAR},$(word 1,$(JAR_NAMES)),$(JARFLAGS)) \
		$(wildcard LICENSE) -C $(CLASSES_DIR) .

# Create the second JAR file containing the source files only,
# if and only if the INCLUDE_SRC option is "true" (enabled)
ifeq ($(__intern_INC_SRC),1)
	$(call __info,)
	@echo "$(CLR_PREFIX) $(call __bold,-----[) $(call __clr,2,$@:sources) $(call __bold,::) $(call __clr_br,4,$(ARTIFACT_ID)) $(call __bold,]-----)"
	$(call __info,)
	$(call __info,Generating the JAR file (sources)...)
	@$(JAR) $(subst {JAR},$(word 2,$(JAR_NAMES)),$(JARFLAGS)) \
		$(wildcard LICENSE) -C $(JAVA_DIR) . \
		-C $(addprefix $(CLASSES_DIR)/,$(notdir $(RESOURCES_ONLY_DIR))) .
endif  # __intern_INC_SRC
	$(call __lines)



build-docs:
	$(call __job_msg,$@)

	$(call __pr_lint)
	$(call __pr_verbose)
	$(call __info,)

	$(eval JDOCFLAGS += $(FLAGS))

	$(call __info,Generating HTML documentations to '$(abspath $(JAVADOC_OUT))'...)
	@$(JDOC) $(JDOCFLAGS) $(addprefix -J,$(MEMFLAGS))

	$(call __lines)

.PHONY: compile package build-docs


## :::::::::::::::: ##
##  Cleaners        ##
## :::::::::::::::: ##


clean:
	$(call __job_msg,$@)

# Clean the "target" (which is the output directory for compiled classes
# and the JAR files) recursively.
ifeq ($(call __is_exist,$(TARGET_DIR)),1)
	$(call __pr_verbose)
	$(call __info,)

	$(call __info,Deleting '$(abspath $(TARGET_DIR))'...)
	@-rm --recursive $(TARGET_DIR) $(if $(__intern_VERBOSE),--verbose,)
endif

	@echo "$(CLR_PREFIX) $(call __clr_br,2,ALL CLEAN)"
	$(call __lines)


cleanbin:
	$(call __job_msg,$@)

ifeq ($(call __is_exist,$(CLASSES_DIR)),1)
	$(call __pr_verbose)
	$(call __info,)

	$(call __info,Deleting '$(abspath $(CLASSES_DIR))'...)
	@-rm --recursive $(CLASSES_DIR) $(if $(__intern_VERBOSE),--verbose,)
endif

	@echo "$(CLR_PREFIX) $(call __clr_br,2,ALL CLEAN)"
	$(call __lines)


cleandocs:
	$(call __job_msg,$@)

ifeq ($(call __is_exist,$(JAVADOC_OUT)),1)
	$(call __pr_verbose)
	$(call __info,)

	$(call __info,Deleting '$(abspath $(JAVADOC_OUT))'...)
	@-rm --recursive $(JAVADOC_OUT) $(if $(__intern_VERBOSE),--verbose,)
endif

	@echo "$(CLR_PREFIX) $(call __clr_br,2,ALL CLEAN)"
	$(call __lines)

.PHONY: clean cleanbin cleandocs


# We cannot do any further from here, when the one of Java source file get changed
# or modified, then it will compile all source files again, even though it is only one of them.
# If you want more efficient and more easier on compilation, consider to use Maven instead.
#
$(CLASSES_FILES): $(SOURCES)
	$(call __job_msg,compile)

	$(call __pr_lint)
	$(call __pr_verbose)
	$(call __info,)

	$(call __info,Compiling $(words $(SOURCES)) source files to '$(CURDIR)/$(CLASSES_DIR)'...)
	@$(foreach src,$^,\
		echo "$(CLR_PREFIX) $(call __bold,$(notdir $(src))) >>\
		$(subst /,.,$(basename $(subst $(JAVA_DIR)/,,$(call __clr,4,$(src)))))$(NORMAL)";\
	)

	@$(JC) -d $(CLASSES_DIR) $^ $(JCFLAGS) $(addprefix -J,$(MEMFLAGS))
	$(call __lines)



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
