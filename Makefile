# MAKEFILE FOR JMATRIX
# Created by Ryuu Mitsuki
VERSION = v1.0.0

SRC_DIR = com/mitsuki/jmatrix/
BIN_DIR = bin/

PY = src/python/
BIN := $(wildcard $(BIN_DIR)$(SRC_DIR)*.class)
SRCTXT = assets/properties/source_files.list

MANIFEST = META-INF/MANIFEST.MF
JAR = jars/jmatrix-$(VERSION).jar


all: $(SRCTXT) $(MANIFEST)
	$(info Creating jar files...)
	mkdir -p jars/
	python $(PY)get_sources.py
	python $(PY)fix_version.py
	javac -d $(BIN_DIR) @$<
	jar cvfm $(JAR) $(MANIFEST) \
	    LICENSE assets/ -C bin/ .


clean-bin:
	rm -r $(BIN_DIR)
	$(info Cleaning "$(BIN_DIR)" only...)

clean:
	rm -r $(JAR) $(BIN_DIR)
	$(info Cleaning "$(JAR)" "$(BIN_DIR)"...)


.PHONY: all clean clean-bin
