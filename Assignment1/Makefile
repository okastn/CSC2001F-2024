# Source files directory
SRCDIR = src
# .class folder
CLASSDIR = bin
# Output JAR file
JARFILE = output.jar

CLASSPATH = ./bin
SRCPATH = ./src

# List of Java source files
SRCS = $(wildcard $(SRCDIR)/*.java)
# List of class files corresponding to source files
CLASSFILES = $(patsubst $(SRCDIR)/%.java,$(CLASSDIR)/%.class,$(SRCS))

# Ensure that 'bin' directory is created before compiling
$(CLASSDIR):
	mkdir -p $@

# Rule to compile .java files into .class files
$(CLASSDIR)/%.class: $(SRCDIR)/%.java | $(CLASSDIR)
	javac -d $(CLASSDIR) -cp $(SRCPATH):$(CLASSPATH) $<

# Rule to create the output JAR file
$(JARFILE): $(CLASSFILES)
	jar cfe $(JARFILE) Test -C $(CLASSDIR) .

# Rule to run the program
run: $(JARFILE)
	java -jar $(JARFILE)

# Clean up generated files
clean:
	rm -rf $(CLASSDIR) $(JARFILE)

# Declare 'run' and 'clean' as phony targets
.PHONY: run clean
