# About Propositions LIVE
Propositions LIVE is a Java program designed to parse and interpret varieties of logical propositions, utilizing a custom interpreter and parser to evaluate user input while also utilizing standard order of precedence for propositions.

# Getting Started
Propositions LIVE requires Java 17 at the minimum to be installed on the user-system in order to run. The JAR will not run on a JRE purposed for lower versions. Once installed, install the Propositions LIVE .jar file, and run it. 

Upon first launch, it will create a `config.properties` file with properties assigned. Leave this in the same directory as your Propositions LIVE JAR.

## Propositions
Users can evaluate propositions either using PL's *Simple* method, or the *Complex* method. 

To the left of the input-window are a series of buttons highlighting the different operations possible on propositions. Clicking any one of them will input the text-form of that operation into the input field. 

The *Simple* evaluation method assumes that, by default, all propositions evaluate to true. This means that evaluating "P & Q" will evaluate to "TRUE", as P and Q are both assumed to be true. If you want this to return "FALSE", you'd have to give Propositions LIVE the *Simple* evaluation of "!P & Q".

The *Complex* evaluation method assumes that each proposition has their own unique truth-value. This means that evaluating "P & Q" could evaluate to different things, depending on what truth values are given to P and Q individually. The user can specifiy the truth values for P and Q respectively, and then hit the "INTERPRET" button to obtain the new interpretation.

To the left of any proposition lies a remove button, which removes any proposition from the view at any current moment. 

A read-only console is available as a separate tab, next to the user-input tab, allowing the user to see a preliminary debugging view of Propositions LIVE. Any errors are in red text. Normal outputs are in black text.

## Truth Tables
Under Development

## Tree Method
Under Development

# Getting Started (Development)
Propositions LIVE was developed in IntelliJ IDEA, using Oracle's Java SDK 17.0.2, AtlantaFX, JavaFX 20, and Gluon SceneBuilder. The repo itself is usable as an IntelliJ project.

## Running inside the IDE
Ensure the content inside your project matches the following configurations.

### Project Structure > Modules > Dependencies
![image](https://github.com/alexanderjalexander/propositionslive/assets/129627021/696d0470-dd06-49b3-89bf-b5dd51f4665d)

### Project Structure > Libraries
- MAVEN: io.github.mkpaz:atlantafx.base
- javafx 17.0.6
- Other Maven dependencies for JUnit, Opentest4J, etc.

![image](https://github.com/alexanderjalexander/propositionslive/assets/129627021/765034e9-db78-4f3b-ae6b-6ca8353aaf53)

### Project Structure > Artifacts
Ensure your output layout is the same. This current build works on Windows 10. Both JavaFX 17.0.6 and JavaFX 20 are required, the former for the main GUI, and the latter for AtlantaFX.

![image](https://github.com/alexanderjalexander/propositionslive/assets/129627021/0abecc1a-e028-49a6-b79f-5d5cb7336eb1)


# Credits
- To my Discrete Structures professor, for being an amazing professor and inspiring me to start this project
- To my friend AJW, who helped massively with AST's when I was learning them
- To Robert Nystrom, author of Crafting interpreters
- To the people over at AtlantaFX, for creating a fantastic JavaFX Dark/Light mode theme.
