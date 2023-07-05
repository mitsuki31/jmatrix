# ======================= #
#         Makefile        #
# ======================= #
# >> Created by Ryuu Mitsuki

# WARNING! Don't change this version manually, it's autogenerated!
VERSION := 1.1.0
PREFIX  := [jmatrix]

CC := javac

# Define null variable, let user to initialize with any Java options or flags
FLAGS ?=

# Assign "true" into this variable would trigger the Java linter
# and the `FLAGS` variable automatically added linter flags "-Xlint"
LINT ?=

ifeq "$(LINT)" "true"
	FLAGS := -Xlint -Xdoclint
endif


# Path variables
PYTHON_PATH    := ./src/main/python/
SOURCES_PATH   := ./src/main/java/
RESOURCES_PATH := ./src/main/resources/
OUTPUT_PATH    := ./target/
CLASSES_PATH   := ./target/classes/
PACKAGE_PATH   := com/mitsuki/jmatrix/
MANIFEST       := META-INF/MANIFEST.MF
DOCS_PATH      := docs/

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


# Define null variable, user should defines it on CLI arguments
# if want to activate it (the value should "true")
INCLUDE-SRC ?=


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
	@echo "   * compile       - Compile the program."
	@echo "   * package       - Create archived package (jar) of compiled program."
	@echo "                        WARNING: Program need to be compiled first!"
	@echo "   * clean         - Clean all of compiled program and created jar."
	@echo "   * cleanbin      - Clean all generated class files only."
	@echo "   * check-verbose - Check the verbose status."
	@echo "   * usage         - Print the example usages for build the project."
	@echo ""
	@echo "Additional Options:"
	@echo "   * Activating verbose output"
	@echo ""
	@echo "       \`export VERBOSE=true\`"
	@echo ""
	@echo "     Or:"
	@echo ""
	@echo "       \`make [options] VERBOSE=true\`"
	@echo ""
	@echo ""
	@echo "   * Include source files while packaging"
	@echo ""
	@echo "       \`make [options] INCLUDE-SRC=true\`"
	@echo ""
	@echo ""
	@echo "   * Invoke the linter"
	@echo ""
	@echo "       \`make [options] LINT=true\`"
	@echo ""
	@echo ""
	@echo "   * Add some options or flags to compiler"
	@echo ""
	@echo "       \`make [options] FLAGS[=<flags>]\`"
	@echo ""
	@echo "Usage:"
	@echo "     $$ make [options] [...] [arguments]"
	@echo "     $$ make [options] VERBOSE[=<bool>] INCLUDE-SRC[=<bool>]"
	@echo "     $$ make [options] (LINT[=<bool>] | FLAGS[=<flags>])"
	@echo ""
	@echo "Tips:"
	@echo "   - Combine the options, Makefile can understand multiple rules."
	@echo ""
	@echo "Author:"
	@echo "   Ryuu Mitsuki"


check-verbose:
ifneq "$(MAKE_VERBOSE)" "true"
	@echo "Verbose output is DEACTIVATED."
else
	@echo "Verbose output is ACTIVATED."
endif



compile: $(SOURCES_LIST) $(SRCFILES)
	@echo ""
	@echo ">> [ COMPILE PROGRAM ] <<"

	$(if $(shell [ $(LINT) = "true" ] && echo 1),\
		@echo "$(PREFIX) Linter is ACTIVATED."\
	)
	@echo "$(PREFIX) Compiling all source files..."
	@$(CC) -d $(CLASSES_PATH) @$< $(FLAGS)
	@echo "$(PREFIX) Successfully compiled all source files."

	$(eval HAS_COMPILED := $(wildcard $(CLASSES_PATH)))
	$(eval HAS_OUTPUT := $(wildcard $(OUTPUT_PATH)))

	@echo ""
	@echo ">> [ GENERATE LIST ] <<"
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

	@echo ""
	@echo ">> [ CREATE JAR ] <<"

	@echo "$(PREFIX) Copying all program resources to output directory..."
	@cp -r --preserve=all $(RESOURCES_PATH)* $(CLASSES_PATH)
	@echo "$(PREFIX) All resources have been copied."

	@echo ""
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

ifeq "$(INCLUDE-SRC)" "true"
	@echo ""
	@echo "$(PREFIX) INCLUDE-SRC option is ACTIVATED"
	@echo "$(PREFIX) Adding the source files into jar..."
ifeq "$(MAKE_VERBOSE)" "true"
	@jar uvf $(jar) -C $(SOURCES_PATH) .
else
	@jar uf $(jar) -C $(SOURCES_PATH) .
endif
endif

	@echo "$(PREFIX) Successfully created the jar file."
	@echo ""
	@echo "SAVED IN: \"$(jar)\""


build-docs: $(SOURCES_LIST)
	@echo
	@echo ">> [ BUILD DOCS ] <<"
	@echo "$(PREFIX) Build the JMatrix docs..."
	@javadoc -author -version -quiet -d $(DOCS_PATH)jmatrix -Xdoclint \
		@$^ --release 11 -windowtitle "JMatrix" -doctitle "<b>JMatrix</b><br>v$(VERSION)" \
		-tag param -tag return -tag throws -tag warning:a:"Warning:" -tag author -tag license:a:"License:" -tag see \
		-Xdoclint/package:-com.mitsuki.jmatrix.core \
		-bottom "<font size="-1">Copyright (c) 2023 <a href="https://github.com/mitsuki31">Ryuu Mitsuki</a>.</font>" \
		-group "Core Packages" "com.mitsuki.jmatrix*:com.mitsuki.jmatrix.core" \
		-group "Utilities Packages" "com.mitsuki.jmatrix.util"

	@echo "$(PREFIX) Successfully build the JMatrix docs."
	@echo
	@echo "SAVED IN: \"$(DOCS_PATH)jmatrix/\""

clean:
	@echo ""
	@echo ">> [ CLEAN WORKING DIRECTORY ] <<"
	@echo "$(PREFIX) Cleaning the \"$(OUTPUT_PATH)\" directory recursively..."
	@-rm -r $(OUTPUT_PATH)
	@echo ""
	@echo "$(PREFIX) All cleaned up."


cleanbin:
	@echo ""
	@echo ">> [ CLEAN ONLY CLASS OBJECTS ] <<"
	@echo "$(PREFIX) Cleaning the class files only..."
	@-rm -r $(CLASSES_PATH)
	@echo ""
	@echo "$(PREFIX) All cleaned up."

	$(if $(shell test -e $(jar) && echo "1"),\
		@echo 'File "$(subst ./,,$(jar))" is still exists.',\
		@echo 'File "$(subst ./,,$(jar))" is missing or has been deleted.'\
	)


$(SOURCES_LIST): $(wildcard $(PYTHON_PATH)*.py)
	@echo ""
	@echo ">> [ GENERATE LIST ] <<"
	@echo "$(PREFIX) Generating list of source files..."

ifeq "$(MAKE_VERBOSE)" "true"
	@python $(PYTHON_PATH)generate_list.py src -v
else
	@python $(PYTHON_PATH)generate_list.py src
endif

	@echo "$(PREFIX) List file generated."


usage:
	@echo "[Makefile Usage]"

	@echo ""
	@echo "Parameters:"
	@echo "    $$ make [option1] [option2] [...]"

	@echo ""
	@echo "Generate \"jar\" file (simple)"
	@echo ""
	@echo "make"
	@echo "  └── compile"
	@echo "          └── package"
	@echo "                 └── cleanbin (optional)"

	@echo ""
	@echo "Generate \"jar\" file (complex)"
	@echo ""
	@echo "make"
	@echo "  └── compile"
	@echo "         └── package"
	@echo "                └── && mv target/*.jar ."
	@echo "                       └── && make"
	@echo "                                └── clean"
