#### ---------------- ======================== ####
####  :: Func.mk  ::   Contains all functions  ####
#### ---------------- ======================== ####

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
ifndef __MAKE_FUNC_MK
__MAKE_FUNC_MK = 1


NORMAL         := \033[0m
BOLD           := \033[1m
ITALIC         := \033[3m
COLORS         := \033[31m \033[32m \033[33m \033[34m \033[35m \
                  \033[36m \033[37m
COLORS_BR      := \033[1;91m \033[1;92m \033[1;93m \033[1;94m \033[1;95m \
                  \033[1;96m \033[1;97m


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
    SOURCES := $(or $(shell                                   \
      find $(JAVA_DIR) -type f -name '*.java' 2>/dev/null |   \
      sed 's/^$(subst /,\/,$(JAVA_DIR)/)//'                   \
    ), $(call __raise_err,Fatal,Cannot find the source files  \
      in "$(JAVA_DIR)". No such file or directory)            \
    )
  else
    SOURCES := $(or $(shell                                   \
      find $(JAVA_DIR) -type f -name '*.java' 2>/dev/null     \
    ), $(call __raise_err,Fatal,Cannot find the source files  \
      in "$(JAVA_DIR)". No such file or directory)            \
    )
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
$(shell printf "$(word $(1),$(COLORS))$(2)$(NORMAL)")
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
$(shell printf "$(word $(1),$(COLORS_BR))$(2)$(NORMAL)")
endef  # __clr_br


# __bold Function
#
# This function prints the given message with a bold version of the current color.
# It uses ANSI escape codes to apply it to the message.
#
# Usage:
#   @echo $(call __bold,<message>)
#
# Arguments:
#   message:
#     The message to be printed.
#
define __bold
$(shell printf "$(BOLD)$(1)$(NORMAL)")
endef  # __bold


# __raise_err Function
#
# This function raises a customized Make error and prints the error message to the console.
# It allows you to specify a title and a detailed error message.
# With custom colors and prefixes applied to the error message.
#
# Usage:
#   $(call __raise_err,<title>,<message>)
#
# Arguments:
#   title:
#     The title for the error message.
#
#   message:
#     The detailed error message.
#
define __raise_err
$(error $(or $(CLR_PREFIX),[$(call __clr_br,6,jmatrix)])     \
  $(call __clr_br,1,$(1)): $(2))
endef  # __raise_err


# __warn Function
#
# This function raises a customized Make warning and prints the warning message to the console.
# It allows you to specify a detailed warning message.
# With custom colors and prefixes applied to the warning message.
#
# Usage:
#   $(call __warn,<message>)
#
# Arguments:
#   message:
#     The detailed warning message.
#
define __warn
$(warning $(or $(CLR_PREFIX),[$(call __clr_br,6,jmatrix)])   \
  $(call __clr_br,3,Warning): $(1))
endef  # __warn


# __info Function
#
# This function prints the given info message to the console.
# With custom colors and prefixes applied to the info message.
#
# Usage:
#   $(call __info,<message>)
#
# Arguments:
#   message:
#     The detailed info message.
#
define __info
	@echo "$(or $(CLR_PREFIX),[$(call __clr_br,6,jmatrix)]) $(call __clr,6,$(1))"
endef  # __info

endif  # __MAKE_FUNC_MK
