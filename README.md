# Soot-Tool

## Key Features

Soot-Tool is a tool that takes another Java program in the form of jar file as input. It locates all the "main" methods in all the classes and computes the dependences of local variables.

For each local variable v in such method, the tool should print other variables that v "depends" on. If there is an assignment statement v = <some_expression>, then v depends on every variable that occurs in <some_expression>, and also transitively depends on all variables on which the variables in <some_expression> depends.

## How To Use
To clone and run this application, you'll need Git and Java installed on your machine. From your command line:

```bash
# Clone this repository
$ git clone https://github.com/harishpb26/Soot-Tool

# Go into the repository
$ cd Soot-Tool

# Add permission to script.sh
$ chmod +x ./script.sh

# Run script
$ ./script.sh
```
