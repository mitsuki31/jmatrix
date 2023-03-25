# MAKEFILE FOR JMATRIX
# Created by Ryuu Mitsuki

SRC_DIR = lib/matrix/
MANIFEST = META-INF/MANIFEST.MF

SRC = $(SRC_DIR)*.java
BIN = $(SRC_DIR)*.class

JAR = Matrix.jar

all: $(SRC)
	$(info Creating jar files...)
	javac $(SRC)
	jar cvfm $(JAR) $(MANIFEST) $(BIN)

run: $(JAR)
	java -jar $(JAR)

clean:
	rm -r $(BIN) $(JAR)


.PHONY: all run clean
