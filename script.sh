#!/bin/sh

# command to run soot
# java -cp sootclasses-trunk-jar-with-dependencies.jar soot.Main

# command to convert .java into .jimp
# java -cp sootclasses-trunk-jar-with-dependencies.jar soot.Main -cp ./tests -pp InputClass -output-format j

# command to run soot API
javac -cp ".:sootclasses-trunk-jar-with-dependencies.jar" Tool.java
java -cp ".:sootclasses-trunk-jar-with-dependencies.jar" Tool
