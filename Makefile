# ======================= #
#         Makefile        #
# ======================= #
# >> Created by Ryuu Mitsuki

# WARNING! Don't change this version manually, it's autogenerated!
VERSION := 1.0.0-beta.x
PREFIX  := [jmatrix]

CC := javac

# Path variables
PYTHON_PATH    := ./src/main/python/
SOURCES_PATH   := ./src/main/java/
RESOURCES_PATH := ./src/main/resources/
OUTPUT_PATH    := ./target/
CLASSES_PATH   := ./target/classes/
PACKAGE_PATH   := com/mitsuki/jmatrix/
MANIFEST       := META-INF/MANIFEST.MF

SOURCES_LIST   := target/generated-list/sourceFiles.lst
CLASSES_LIST   := target/generated-list/outputFiles.lst

SRCFILES       := $(shell find $(SOURCES_PATH) -type f -name '*.java')
ifneq "$(wildcard $(CLASSES_PATH))" ""
	CLSFILES   := $(shell find $(CLASSES_PATH) -type f -name '*.class')
endif

jar      := $(OUTPUT_PATH)jmatrix-$(VERSION).jar
jar_name := $(word 3,$(subst /, , $(jar)))


# Check whether the program has been compiled
HAS_OUTPUT   := $(wildcard $(OUTPUT_PATH))
HAS_COMPILED := $(wildcard $(CLASSES_PATH))

ARG1 := $(word 1,$(MAKECMDGOALS))
ifeq ($(words $(MAKECMDGOALS)),2)
    ARG2 := $(word 2,$(MAKECMDGOALS))
endif


# Check VERBOSE variable from shell
ifndef VERBOSE
	MAKE_VERBOSE :=
else
ifeq ($(VERBOSE),true)
	MAKE_VERBOSE := true
else
	MAKE_VERBOSE :=
endif
endif

ifeq "$(ARG1)" "clean"
ifeq "$(strip $(HAS_OUTPUT))" ""
$(error $(PREFIX) Program is uncompiled, failed to clean working directory)
endif
endif

ifeq "$(ARG1)" "cleanbin"
ifeq "$(strip $(HAS_COMPILED))" ""
$(error $(PREFIX) Program is uncompiled, failed to clean classes directory)
endif
endif


# This to avoid 'Make' options treated as file
.PHONY: all compile package clean


all:
	$(info [Makefile-jmatrix])
	@echo "Options:"
	@echo "   * compile       - Compile the program. \
		\n   * package       - Create archived package (jar) of compiled program.\
		\n                        WARNING: Program need to be compiled first! \
		\n   * clean         - Clean all of compiled program and created jar. \
		\n   * cleanbin      - Clean all generated class files only. \
		\n   * check-verbose - Check the verbose status. \
		\n   * usage         - Print the example usages for build the project."

	@echo "\nUsage:"
	@echo "     $$ make [option1] [option2] [...]"
	@echo "     $$ make compile package"

	@echo "\nTips:"
	@echo "   - Combine the options, Makefile can understand multiple rules."
	@echo "   - Use command: 'export VERBOSE=true' for activating verbose output, \
	and check the verbose with 'check-verbose' option."

	@echo "\nCreated by Ryuu Mitsuki"


check-verbose:
ifneq "$(MAKE_VERBOSE)" "true"
	@echo "Verbose output is DEACTIVATE."
else
	@echo "Verbose output is ACTIVATE."
endif



compile: $(SOURCES_LIST) $(SRCFILES)
	@echo "\n>> [ COMPILE PROGRAM ] <<"

	@echo "$(PREFIX) Compiling all source files..."
	@$(CC) -d $(CLASSES_PATH) @$<
	@echo "$(PREFIX) Successfully compile all source files."

	$(eval HAS_COMPILED := $(wildcard $(CLASSES_PATH)))
	$(eval HAS_OUTPUT := $(wildcard $(OUTPUT_PATH)))

	@echo "\n>> [ GENERATE LIST ] <<"
	@echo "$(PREFIX) Generating list of class files..."

ifeq "$(MAKE_VERBOSE)" "true"
	@python $(PYTHON_PATH)generate_list.py cls -v
else
	@python $(PYTHON_PATH)generate_list.py cls
endif

	@echo "$(PREFIX) List file generated."


package: $(CLSFILES)
	$(if $(shell [ ! -d $(CLASSES_PATH) ] && echo "1"),\
		$(error $(PREFIX) Program is uncompiled, compile it with `make compile` command)\
	)

	@echo "\n>> [ CREATE JAR ] <<"

	@echo "$(PREFIX) Copying all program resources to output directory..."
	@cp -r $(RESOURCES_PATH)* $(CLASSES_PATH)
	@echo "$(PREFIX) All resources have been copied.\n"

	@echo "$(PREFIX) Creating jar for compiled classes..."

ifeq "$(MAKE_VERBOSE)" "true"
	@python $(PYTHON_PATH)fix_config.py -v
	@jar cvfm $(jar) $(MANIFEST) \
	    LICENSE -C $(CLASSES_PATH) .
else
	@python $(PYTHON_PATH)fix_config.py
	@jar cfm $(jar) $(MANIFEST) \
		LICENSE -C $(CLASSES_PATH) .
endif

	@echo "$(PREFIX) Successfully create jar file."
	@echo "\nSAVED IN: \"$(jar)\""


clean:
	@echo "\n>> [ CLEAN WORKING DIRECTORY ] <<"
	@echo "$(PREFIX) Cleaning the \"$(OUTPUT_PATH)\" directory recursively..."
	@-rm -r $(OUTPUT_PATH)
	@echo "\n$(PREFIX) All cleaned up."


cleanbin:
	@echo "\n>> [ CLEAN ONLY CLASS OBJECTS ] <<"
	@echo "$(PREFIX) Cleaning the class files only..."
	@-rm -r $(CLASSES_PATH)
	@echo "\n$(PREFIX) All cleaned up."

	$(if $(shell test -e $(jar) && echo "1"),\
		@echo 'File "$(jar:/=)" is still exists.',\
		$(warning File "$(jar_name)" is missing or has been deleted.)\
	)


$(SOURCES_LIST): $(wildcard $(PYTHON_PATH)*.py)
	@echo "\n>> [ GENERATE LIST ] <<"
	@echo "$(PREFIX) Generating list of source files..."

ifeq "$(MAKE_VERBOSE)" "true"
	@python $(PYTHON_PATH)generate_list.py src -v
else
	@python $(PYTHON_PATH)generate_list.py src
endif

	@echo "$(PREFIX) List file generated."


usage:
	@echo "[Makefile Usage]\n"

	@echo "Parameters:\n    $$ make [option1] [option2] [...]\n"

	@echo "Generate \"jar\" file (simple)"
	@echo "\
	make\n\
	  └── compile\n\
	          └── package\n\
	                 └── cleanbin (optional)\n"

	@echo "Generate \"jar\" file (complex)"
	@echo "\
	make\n\
	  └── compile\n\
	         └── package\n\
	                └── && mv target/*.jar\n\
	                       └── && make\n\
	                              └── clean\n"
