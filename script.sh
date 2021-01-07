#!/bin/sh

# command to convert .java source code into .JAR
javac tests/InputClass1.java
echo "Main-Class: tests.InputClass1\n" > myfile.mf
jar -cvmf myfile.mf tests/InputClass1.jar

# extract .class executable from .JAR
jar xf tests/InputClass1.jar InputClass1.class

# command to run soot
# java -cp sootclasses-trunk-jar-with-dependencies.jar soot.Main

# command to convert .java into .jimp
# java -cp sootclasses-trunk-jar-with-dependencies.jar soot.Main -cp ./tests -pp InputClass -output-format j

# command to run soot API
javac -cp ".:sootclasses-trunk-jar-with-dependencies.jar" Tool.java
java -cp ".:sootclasses-trunk-jar-with-dependencies.jar" Tool InputClass1
