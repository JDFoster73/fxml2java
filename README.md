# fxml2java
Takes FXML files created in SceneBuilder (for example) and builds (and then rebuilds) a POJO class to achieve the same but which is easier to work with than FXML (in my opinion)

Requires linking to the java fx modules in order to run the example but not just to run the converter.  The converter depends on the JavaParser library to rebuild changed files so the user must acquire this seperately.  It is on GitHub (https://github.com/javaparser/javaparser).

The library is not complete.  If conversion fails because the library doesn't know how to handle a given element or attribute then send me a message and I'll add it in.  Alternatively, feel free to get involved.

The basic workflow is:

-Create a package in the project where the fxml objects are required.  Add fxml files in this package.  The name of the fxml file MUST be the fully-qualified name
 of the *DESTINATION package.package,...,target_classname.fxml*.  This tells the converter where to put the resulting plain Java class file.  It must be in the same
 project so it can resolve relative references to any resources in the project such as i18n files and icons etc.  See the example code for more information.
 
-Run the converter (see example code for explanation of how to do this).  This will initially generate the java files in the source locations specified by the full filename
 of the fxml file.  For example, if an fxml file in source package org/form/basis (the location of the source package doesn't really matter, just that it is in the
 same project as the destination file) named org.pack1.pack2.Result.fxml is run through the converter, then the resulting Java file will be saved in the destination 
 package org/pack1/pack2/Result.java of the same project.
 
-Instantiate the class and access the main form container using the getMainContainer() method.  Again, see example code for more information.

-Update fxml file(s) in SceneBuilder or using the text editor to edit the XML source.  Run the converter and it will update the destination Java files but replicate any 
 non-generated methods, fields and imports. Note that the converter messes up the formatting but all IDEs contain a handy code formatter so I don't find this to be a problem.
 
-If you change the name of an FXML file for which a Java file has already been generated, you *MUST* change the name of the generated Java file for this FXML source file or the  converter will create a brand new file that does not contain any additions that have been made to the destination Java file subsequently.
 
 
