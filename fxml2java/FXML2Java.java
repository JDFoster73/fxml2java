/**
 * Copyright 2021 James David Foster jdfoster73@gmail.com
 * 
 * This file is part of fxml2java.
 * 
 * fxml2java is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * 
 * fxml2java is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty 
 * of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along with fxml2java. If not, see <https://www.gnu.org/licenses/>. 
 */

package fxml2java;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.ImportDeclaration;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;

import fxml2java.element.FXMLDOMResolver;
import fxml2java.element.FXMLJavaDescriptor;


/**
 * <p>Convert either a single source fxml file or a directory of fxml files to a plain Java object.  Correct conversion of the FXML files requires
 * managing the project in a set format.
 * 
 * <p>All FXML files to be converted <b>MUST</b> reside in the same Java project as the destination class files.  SceneBuilder allows for the referencing
 * of resources such as images and i18n text which must be in the same project directory or jar file unless resources are referenced in absolute paths.
 * 
 * <p>Generic arguments can be specified by using the id attribute of an FXML element:
 * <ul>
 * <li>id = "element_id<java.net.URL,String" calls setId("element_id") on the element and declares generic arguments of URL and String.  Generic classes which are not in java.lang must be fully-qualified.
 * <li>id = "<java.net.URL,String" there is no id required for the element but generic arguments of URL and String are required.
 * </ul>
 * 
 * 
 * @author James David Foster jdfoster73@gmail.com
 *
 */
public class FXML2Java
{
  /**
   * <p>Convert all .fxml files in the given source directory to plain Java objects.  The source fxml files <b>MUST</b> be in the
   * same source project as the destination files, and the source filename <b>MUST</b> be the fully-qualified class name of the
   * FXML>Java source class file that will be generated. 
   * 
   * @param sourceDirectory
   * @param destDir
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public void convertDirectory(String sourceDirectory, String destDir) throws IOException, ParserConfigurationException, SAXException
  {
    //List the files in the source directory and process all that end in .fxml.
    File directory = new File(sourceDirectory);
    
    if(!directory.isDirectory()) throw new IllegalArgumentException();
    
    //List .fxml files in the source directory.
    File[] listFiles = directory.listFiles((f) -> {return f.getName().toLowerCase().endsWith(".fxml");});
    
    //Process each file.
    for(File f : listFiles)
    {
      System.out.println();
      System.out.println();
      
      convert(f.getAbsolutePath(), destDir);
    }
  }
  
  /**
   * <p>Convert a single .fxml file to a plain Java object.  The source fxml file <b>MUST</b> be in the
   * same source project as the destination files so that resources can be referenced correctly.
   * 
   * @param sourceDirectory
   * @param destDir
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public void convert(String sourceFile, String destDir) throws IOException, ParserConfigurationException, SAXException
  {
    // Check the existence of destination file. Create if required.
    //
    // Calculate the dest file. Get last path entry and replace ".fxml" with ".java"
    File srcFile = new File(sourceFile);
    Path srcPath = srcFile.toPath();
    String srcName = srcPath.getFileName().toString();
    // Convert source name to path.
    char[] srcFileName = srcName.toCharArray();

    int inst = 0;
    int extensionLoc = 0;
    for (int i = srcFileName.length - 1; i >= 0; i--)
    {
      //
      if ((srcFileName[i] == '.'))
      {
        if((inst++ > 0))
        {
          srcFileName[i] = '/';
        }
        else
        {
          extensionLoc = i;
        }
      }
    }

    //Replace the 'fxml' with '.java' for the generated filename.
    String destName = new String(srcFileName).substring(0, extensionLoc);
    srcName = destName + ".java"; //new String(srcFileName).replace("fxml", "java");

    File destFile = new File(destDir + "/" + srcName);
    Path packageFile = new File(srcName).toPath();

    String packge = packageFile.getParent().toString().replaceAll("\\\\", ".");

    String className = packageFile.getName(packageFile.getNameCount() - 1).toString();

    //Get resource bundle.
    ResourceBundle rb = ResourceBundle.getBundle("fxml2java.strings");
    
    //Output status.
    System.out.println(String.format(rb.getString("startconv"), sourceFile));
    
    if (!destFile.exists())
    {
      //Output status.
      System.out.println(String.format(rb.getString("create"), destFile.getAbsolutePath()));
      
      //Create a resolver.
      FXMLDOMResolver fxmldomResolver = new FXMLDOMResolver(sourceFile);
      
      //Get the descriptor from the resolver.
      FXMLJavaDescriptor fjd = fxmldomResolver.getDescriptor();

      //Add the required imports.  These are:
      //ResourceBundle
      //FXML annotation.
      fjd.addImport("javafx.fxml.FXML");
      fjd.addImport("java.util.ResourceBundle");
      
      //Add required fields.  Generated class requires a ResourceBundle instance.  If there are no i18n strings then this can be 
      //initialised to null by the owner.
      fjd.addField("ResourceBundle resourceBundle");
      
      // Make sure the parent directory exists.
      Files.createDirectories(destFile.toPath().getParent());

      // Create the file.
      if (destFile.createNewFile())
      {
        // Created new file - can open and populate.
        createAndPopulate(fjd, destFile, packge, className.substring(0, className.indexOf('.')));
      }
      else
      {
        System.out.println("PANIC - CAN'T CREATE NEW FILE!!!");
      }
    }
    else
    {
      //Output status.
      System.out.println(String.format(rb.getString("update"), destFile.getAbsolutePath()));

      //Compare dates for src and dest.  Only update if they are different.
      //if(srcFile.lastModified() > destFile.lastModified())
      {
        System.out.println(String.format(rb.getString("update.diff"), srcFile.getName(), new Date(srcFile.lastModified()), new Date(destFile.lastModified())));

        //Create a resolver.
        FXMLDOMResolver fxmldomResolver = new FXMLDOMResolver(sourceFile);
        
        //Create java processor.
        FXMLJavaDescriptor fjd = fxmldomResolver.getDescriptor();

        //Add required fields.  Generated class requires a ResourceBundle instance.  If there are no i18n strings then this can be 
        //initialised to null by the owner.
        fjd.addField("ResourceBundle resourceBundle");

        // Update the file.
        updateFile(fjd, destFile, packge, className.substring(0, className.indexOf('.')));
      }
      //else
      {
        //System.out.println(String.format(rb.getString("update.nodiff"), srcFile.getName()));
      }
    }
  }

  /**
   * <p>The Java counterpart to the fxml file already exists so we are going to update it.  We will replace import declarations and
   * everything in the setupFX() method.  Also we will replace all field declarations that are annotated with the FXML annotation.
   * <p>New handlers will be created but existing handlers will not be altered.  The userConfig() method will not be altered.
   * 
   * @param fxmlr
   * @param destFile
   * @param packge
   * @param className
   * @throws IOException
   */
  private void updateFile(FXMLJavaDescriptor fxmlr, File destFile, String packge, String className) throws IOException
  {
    CompilationUnit cu = StaticJavaParser.parse(destFile);
    Optional<ClassOrInterfaceDeclaration> classA = cu.getClassByName(className);
    
    //Create indentation manager.
    IndentManager idm = new IndentManager(2);
    
    //REPLACE DEST FILE WITH UPDATE.
    // Open buffered writer.
    FileWriter writer = new FileWriter(destFile);
    BufferedWriter fw = new BufferedWriter(writer);

    //PACKAGE NAME DECLARATION.
    FXML2JavaOutput.outputPackageDecl(fw, packge);
    
    //IMPORTS.  
    //Merge existing imports and FXML imports.
    NodeList<ImportDeclaration> imports = cu.getImports();    //Imports from existing converted file.
    for(ImportDeclaration id : imports) fxmlr.addImport(id.getName().asString());  //Add existing import to descriptor. 
    Set<String> importList = new HashSet<>(Arrays.asList(fxmlr.getImportList())); //All imports.
    FXML2JavaOutput.outputImportList(fw, importList);

    //CLASS COMMENT IF EXISTS.
    FXML2JavaOutput.outputClassComment(fw, classA.get());
    
    //CLASS DECLARATION
    // Declare the class.
    fw.append("public class " + className + "\n");

    //Open class definition brace.
    FXML2JavaOutput.openBracket(fw, idm);
    
    //FIELDS.  
    //
    //GENERATE FXML FIELDS.
    FXML2JavaOutput.outputFXMLFields(fw, fxmlr, idm);
    //(RE-)GENERATE EXISTING FIELDS.
    FXML2JavaOutput.outputExistingNonGeneratedFields(fw, classA.get(), idm);
 
    
    //EXISTING CONSTRUCTORS.
    FXML2JavaOutput.outputExistingNonDefaultConstructors(fw, classA.get().getConstructors(), idm);

    //SETUPFX() ROUTINE.  THIS IS REBUILT ON EVERY UPDATE!!!
    // Declare setup routine that can be called by no-args or user-defined
    // constructor.
    FXML2JavaOutput.outputSetupFX(fw, fxmlr, idm);

    
    //SET i18n TEXT.  ANY INTERNATIONALISED STRING WILL BE SET HERE USING THE CURRENT RESOURCE BUNDLE.
    FXML2JavaOutput.outputInitSetTextStrings(fw, fxmlr, idm);
    
    //GET EXISTING METHODS FROM PARSER WHICH ARE NOT SETUPFX() OR SETTEXTSTRINGS() AND WRITE THEM INTO THE SOURCE FILE.
    //SETUPFX() AND SETTEXTSTRINGS() ARE ALWAYS REBUILT ON UPDATE.
    //Existing methods.
    List<MethodDeclaration> methods = classA.get().getMethods();
    Set<String> existingMethodSet = new HashSet<>();
    
    for(MethodDeclaration md : methods)
    {
      String nm = md.getName().asString();
      if(!"setupFX".equals(nm) && !"setTextStrings".equals(nm))
      {
        //Add into the class.
        FXML2JavaOutput.outputExistingMethodLines(fw, md.toString().split("\n"), idm);
        //Existing method set.
        existingMethodSet.add(md.getSignature().asString());
        fw.newLine();
      }
    }
    
    //ADD HANDLERS WHICH HAVE NOT BEEN ADDED ABOVE.  NEW HANDLER METHODS CAN BE DECLARED WHEN UPDATING THE FXML FILE.
    //Create any newly-added handlers.
    for(Handler h : fxmlr.getHandlerList())
    {
      //Get the method signature for the handler.  If it's not in the existing class then add it.
      if(!existingMethodSet.contains(h.getMethodSignature())) FXML2JavaOutput.outputInitHandler(fw, idm, h);
    }
    
    fw.newLine();
    
    //Inner classes.
    //classA.get().get
    List<ClassOrInterfaceDeclaration> findAll = classA.get().findAll(ClassOrInterfaceDeclaration.class);
    for(int i = 1; i < findAll.size(); i++)
    {
      fw.append(findAll.get(i).toString());
      fw.newLine();
    }
    
    //Clsoe class definition brace.
    FXML2JavaOutput.closeBracket(fw, idm);
    fw.newLine();

    // Finally - close the buffered writer.
    fw.close();
  }

  /**
   * <p>The Java counterpart of the fxml file does not exist yet.  It will be created here.
   * 
   * @param fxmlr
   * @param destFile
   * @param packge
   * @param className
   * @throws IOException
   */
  private void createAndPopulate(FXMLJavaDescriptor fxmlr, File destFile, String packge, String className) throws IOException
  {
    // Open buffered writer.
    BufferedWriter fw = new BufferedWriter(new FileWriter(destFile));

    //Create indentation manager.
    IndentManager idm = new IndentManager(2);
    
    //PACKAGE NAME DECLARATION.
    FXML2JavaOutput.outputPackageDecl(fw, packge);
    
    //OUTPUT IMPORTS LIST.
    //This is for a newly-generated file so will have all imports for the generated file classes and
    //the FXML annotation and resource bundle, which we add to the newly-generated file but may not
    //be present in the FXML converted descriptor.
    List<String> importList = Arrays.asList(fxmlr.getImportList());
    FXML2JavaOutput.outputImportList(fw, importList);
    
    // Declare the class.
    fw.append("public class " + className + "\n");
    
    //Open class definition brace.
    FXML2JavaOutput.openBracket(fw, idm);
    
    //GENERATE FXML FIELDS.
    FXML2JavaOutput.outputFXMLFields(fw, fxmlr, idm);
    
    //GENERATE INITIAL CONSTRUCTOR.
    // Declare constructor with resource bundle.  Can be null if there are no i18n strings.
    FXML2JavaOutput.outputInitConstructor(fw, className, idm);

    //INITIAL SETUPFX ROUTINE.
    FXML2JavaOutput.outputSetupFX(fw, fxmlr, idm);

    //GETMAINCONTAINER ROUTINE.
    // Declare main parent retriever.
    FXML2JavaOutput.outputGetMainContainer(fw, fxmlr, idm);
    
    //SET i18n TEXT.  ANY INTERNATIONALISED STRING WILL BE SET HERE USING THE CURRENT RESOURCE BUNDLE.
    FXML2JavaOutput.outputInitSetTextStrings(fw, fxmlr, idm);
    
    //i18n resources update - language change etc.
    FXML2JavaOutput.outputInitResourceUpdate(fw, idm);

    //USER CONFIG METHOD.
    FXML2JavaOutput.outputInitUserConfig(fw, idm);
    
    //DO THE HANDLERS.
    //Create stubs for each handler.
    for(Handler h : fxmlr.getHandlerList()) FXML2JavaOutput.outputInitHandler(fw, idm, h);
    
    //Close brace.
    FXML2JavaOutput.closeBracket(fw, idm);
    fw.newLine();

    // Finally - close the buffered writer.
    fw.close();
  }
  
  
}
