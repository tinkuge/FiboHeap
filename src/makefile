JC = javac

.SUFFIXES: .java .class

.java.class:
	$(JC) $*.java
		
CLASSES = \
	keywordcounter.java \
	InitializeFibHeap.java \
	Node.java
		
default: classes

classes: $(CLASSES:.java=.class)

clean:
	$(RM) *.class
