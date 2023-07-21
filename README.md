# About Propositions LIVE
Propositions LIVE is a Java program designed to parse and interpret varieties of logical propositions, utilizing a custom interpreter and parser to evaluate user input while also utilizing standard order of precedence for propositions.

# Usage Instructions
Propositions LIVE requires Java to be installed on the user-system. Once installed, install the Propositions LIVE .jar file, and run it. To use Propositions LIVE, head over to any of the included tabs and start evaluating!

## Propositions
Users can evaluate propositions either using PL's *Simple* method, or the *Complex* method. 

To the left of the input-window are a series of buttons highlighting the different operations possible on propositions. Clicking any one of them will input the text-form of that operation into the input field. 

The *Simple* evaluation method assumes that, by default, all propositions evaluate to true. This means that evaluating "P & Q" will evaluate to "TRUE", as P and Q are both assumed to be true. If you want this to return "FALSE", you'd have to give Propositions LIVE the *Simple* evaluation of "!P & Q".

The *Complex* evaluation method assumes that each proposition has their own unique truth-value. This means that evaluating "P & Q" could evaluate to different things, depending on what truth values are given to P and Q individually. The user can specifiy the truth values for P and Q respectively, and then hit the "INTERPRET" button to obtain the new interpretation.

To the left of any proposition lies a remove button, which removes any proposition from the view at any current moment. 

A read-only console is available as a separate tab, next to the user-input tab, allowing the user to see a preliminary debugging view of Propositions LIVE!

## Truth Tables
Under Development

## Tree Method
Under Development

# Development
Propositions LIVE was developed in IntelliJ IDEA, using Oracle's Java SDK 17.0.2, JavaFX 17.0.6, and Gluon SceneBuilder 19.0.0. This GitHub repo can be used as an entire standalone project, ready to download for use by IntelliJ. Simply download and import the project!

## Running inside the IDE
As mentioned before, JavaFX is required to run any of these. To ensure it runs properly inside your IDE, ensure that your project structure contains javafx as an available library.

JUnit is also used for unit testing, so ensure that is also installed and added to the proejct structure.

Example Project Structure for IntelliJ: 
![image](https://user-images.githubusercontent.com/129627021/234083199-a63d4c52-8c3b-42da-bb89-a4af91245aa7.png)


## Artifacts
When building your own version of Propositions LIVE, be sure to include all the binaries/.dll's located in your JavaFX SDK's bin folder (e.g. C:\Program Files\JavaFX\javafx-sdk-17.0.6\bin). This will supposedly fix any runtime errors related to JavaFX runtime images. 

E.g. In the image below, the files within the red square are the JavaFX binaries.
![image](https://user-images.githubusercontent.com/129627021/234083625-d357ec22-b753-4411-a56c-8d1c59d26259.png)


# Credits
I would like to give extensive credit to my friend AJW for helping me out with the parser and interpreter, as well as the extensive funky Java bugs encountered during parse testing.
I would also like to thank Robert Nystrom and his book "Crafting Interpreters", which helped out significantly during my work for Proposition LIVE's interpreter and parser.
