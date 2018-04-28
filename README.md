# Project 1 Part 3: Regular Expressions

* Author: Megan Pierce, Anne Brinegar
* Class: CS361 Section 2
* Semester: Spring 2017

## Overview

This Java application converts a Regular Expression into an NFA

## Compiling and Using

  # Project Structure
  |-- re
  | |-re.java
  | |-REDriver.java
  | |-REInterface.java
  |-- tests
  | |-- p3tc1.txt
  | |-- p3tc2.txt
  | |-- p3tc3.txt
  |-- CS361FA.jar
  |-- CS361FAdocs.zip


To compile, execute the following command in the main project directory:
```
$ javac -cp ".:./CS361FA.jar" re/REDriver.java
```

Run the compiled class with the command:
```
$  java -cp ".:./CS361FA.jar" re.REDriver ./tests/<text file>
```

These commands will run the .jar file as well, which is necessary to conver the RE to an NFA
The run command should include the name of the text file you want to run. 
The first line of the file should contain a RegEx using the characters (a,b,*,|,(,))
The following lines should contain strings that you are testing against the RegEx.
The output will write yes or no on each corresponding line of the strings in the file.


## Discussion

This was another challenging program, from figuring out how to parse the regular expression to what a corresponding NFA would look like and how we were going to create it in code. We created a lot of state machines, partial and complete, to find patterns in the regular expression, and to design what would happen in the program to replicate it. Once we had an idea about what that would look like, we were faced with the harder challenge of actually parsing the regex. The possibilities of one or more sets of parenthesis made it especially difficult to conceptualize how to correctly parse the string. Once we took the aproach building up one NFA instead of creating a lot of mini NFA's, like we originally intended on doing, we had an easier time writing the code. We avoided recursion somewhat by utilizing the continue function in Java and stacks to keep track of our starting and ending points, when we face looping or branching. To us, this was easier to conceptualize, and we were able to make a working RE to NFA conversion.

Overall this program was pretty difficult, but we worked well together from beginning to end, and came up with a good and working approach to solving this problem.

