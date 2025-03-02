
SOURCES := $(wildcard *.java)
CLASSES := $(SOURCES:.java=.class)

all: $(CLASSES)

%.class: %.java
	javac $<

clean:
	rm -f *.class
