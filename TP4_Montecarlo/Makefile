SOURCES := $(wildcard *.java)
CLASSES := $(SOURCES:.java=.class)

all: pi $(CLASSES)
pi:
	javac pi.java
%.class: %.java
	javac $<

clean:
	rm -f *.class
