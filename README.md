# JavaToUML

Design Document for JavaToUML project

1.	Project Goal and Requirements
1.1.	Agile engineering process: Kanban style
A weekly Kanban board is maintained to monitor project progress. https://waffle.io/Wei2015/JavaToUML

1.2.	UML Parser requirements
•	Dependencies & Uses Relationships for Interfaces Only
•	Class Declarations with optional Extends and Implements 
•	Only Include Public Methods (ignore private, package and protected scope)
•	Only Include Private and Public Attributes (ignore package and protected scope)
•	Java Setters and Getters:  Must Support also Java Style Public Attributes as "setters and getters"
•	Must Include Types for Attributes, Parameters and Return types on Methods
•	Classifier vs Attributes Compartment

2.	Tools and API used in Implementation
2.1.	Java parse to String API: JavaParser http://javaparser.org/
I have used JavaParser to parse java code in my first part of project. JavaParser can get an Abstract Syntax Tree (AST) from Java code. By using this data structure, all components in a piece of Java code can be retrieved, like attribute declaration name, type, method name, return type and so on. 
The Javadoc of JavaParser can be found here. http://www.javadoc.io/doc/com.github.javaparser/javaparser-core/3.1.3
Some helpful information on how to use JavaParser can be found here: https://github.com/javaparser/javaparser/wiki/Manual
Download the javaparser-core-3.1.0-beta.2.jar to Intellj IDE as external library.

2.2.	String parse to UML diagram API: plantUML http://plantuml.com 
Downloaded plantuml.jar and added it to Intellj IDE as external library.
All string inputs for plantuml must start with @startuml and end with @enduml. I choose output image format is SVG, which is supported by all modern browsers and can be zoomed in and out without losing resolution. 

3.	Command line execution
3.1.	Build jar file in Intellij:
•	File > Project Settings > Artifacts > Add Name: JavaToUML.jar, Type JAR, > Apply and OK
•	Build > Build Artifacts

3.2.	Run command line execution on Amazon EC2 
•	Update Java to 1.8 on EC2
•	Install graphviz on EC2 for plantUML library to be used. http://www.graphviz.org/Download_linux_fedora.php
o	download the graphviz-fedora.repo file and save it (as root) in /etc/yum.repos.d/
o	$sudo yum list available ‘graphviz*’
o	$sudo yum ‘graphviz’
•	FTP transfer JavaToUML.jar and Test Case folder files to EC2
•	$java -jar JavaToUML.jar “path/Test_Case” “pat/graph_name.svg”


