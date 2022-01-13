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
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.ConstructorDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.comments.JavadocComment;
import com.github.javaparser.ast.expr.AnnotationExpr;

import fxml2java.element.FXMLJavaDescriptor;

/**
 * <p>This class contains output methods for writing code statements and methods out.
 * 
 * @author James David Foster
 *
 */
class FXML2JavaOutput
{
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //HELPER METHODS.
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * <p>Open a control brace and then increment the indent.
   * 
   * @param fw
   * @param idm
   * @throws IOException
   */
  static void openBracket(BufferedWriter fw, IndentManager idm) throws IOException
  {
    //Output the open bracket.
    outputIndentedString(fw, idm, "{");
    //Increment the indent.
    idm.inc();
  }
  
  /**
   * <p>Close a control brace and then decrement the indent.
   * 
   * @param fw
   * @param idm
   * @throws IOException
   */
  static void closeBracket(BufferedWriter fw, IndentManager idm) throws IOException
  {
    //Decrement the indent.
    idm.dec();
    //Output the close bracket.
    outputIndentedString(fw, idm, "}");
  }

  /**
   * <p>Convenience method to output an indented string to the writer.
   * 
   * @param fw
   * @param idm
   * @param string
   * @throws IOException
   */
  static void outputIndentedString(BufferedWriter fw, IndentManager idm, String string) throws IOException
  {
    //Output the indented string requested.
    fw.append(idm).append(string).append('\n');
  }
  
  /**
   * 
   * @param fw
   * @param constructors
   * @param idm
   * @throws IOException
   */
  static void outputExistingMethodLines(BufferedWriter fw, String[] lines, IndentManager idm) throws IOException
  {
      //Add the lines.
      for(String line : lines)
      {
        //Trim the string.
        line = line.trim();
        
        //Don't replicate blank lines.
        if(!line.isBlank())
        {
          //First line.  If ends with '{' then set indent.
          outputIndentedString(fw, idm, line);
          
          if(line.endsWith("{")) idm.inc();
          if(line.endsWith("}")) idm.dec();
        }
        
      }
      
      //End with new line.
      fw.newLine();
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //SHARED GENERATE AND REGENERATE HELPER METHODS.
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * 
   * @param fw
   * @param fxmlr
   * @param idm
   * @throws IOException
   */
  static void outputSetupFX(BufferedWriter fw, FXMLJavaDescriptor fxmlr, IndentManager idm) throws IOException
  {
    // Method is called to set up the fx objects.
    // constructor.
    outputIndentedString(fw, idm, "private void setupFX()");

    //Open brace.
    openBracket(fw, idm);
    
    // Setup declarations.
    //for (String decl : fxmlr.instructionList)
    fxmlr.outputElementInstructions((decl) ->
    {
      outputIndentedString(fw, idm, decl);
    });

    // Close brace.
    closeBracket(fw, idm);
    fw.newLine();
  }

  /**
   * <p>Output the getMainContainer() method.  This allows the owner to retrieve the main container which contains all of the
   * sub containers and functionality.
   * 
   * @param fw
   * @param fxmlr
   * @param idm
   * @throws IOException
   */
  static void outputGetMainContainer(BufferedWriter fw, FXMLJavaDescriptor fxmlr, IndentManager idm) throws IOException
  {
    //Output the method signature.
    outputIndentedString(fw, idm, "public " + fxmlr.getRootInstanceDataType() + fxmlr.getRootInstanceGenericDecl() + " getMainContainer()");

    //Open brace.
    openBracket(fw, idm);
    
    // Return the main container.
    outputIndentedString(fw, idm, "return " + fxmlr.getRootInstanceName() + ";");

    //Close brace.
    closeBracket(fw, idm);
    fw.newLine();
  }
  
  /**
   * <p>Output the package declaration to the generated file.
   * 
   * @param fw
   * @param packageDecl
   * @throws IOException
   */
  static void outputPackageDecl(BufferedWriter fw, String packageDecl) throws IOException
  {
    // Start with the package name.
    fw.append("package " + packageDecl + ";");
    fw.newLine();
    fw.newLine();
  }
  
  /**
   * <p>Output the list of java imports to the generated file.
   * 
   * @param fw
   * @param importList
   * @throws IOException 
   */
  static void outputImportList(BufferedWriter fw, Collection<String> importList) throws IOException
  {
    for (String imp : importList)
    {
      fw.append("import " + imp + ";");
      fw.newLine();
    }

    //Finish with a newline.
    fw.newLine();
  }
  
  /**
   * <p>Output fields annotated with @FXML.  These are fields that have been generated by the FXML2Java parser.
   * 
   * @param fw
   * @param fxmlr
   * @param idm
   * @throws IOException
   */
  static void outputFXMLFields(BufferedWriter fw, FXMLJavaDescriptor fxmlr, IndentManager idm) throws IOException
  {
    for (String field : fxmlr.getFieldList())
    {
      outputIndentedString(fw, idm, "@FXML");
      outputIndentedString(fw, idm, "private " + field + ";");
    }    

    //End with new line.
    fw.newLine();
  }
  

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //UPDATE EXISTING FILE HELPER METHODS ONLY.
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  
  /**
   * <p>Include the existing class comment if regenerating the file in response to new FXML.
   * 
   * @param fw
   * @param classA
   * @throws IOException
   */
  static void outputClassComment(BufferedWriter fw, ClassOrInterfaceDeclaration classA) throws IOException
  {
    //CLASS COMMENT IF EXISTS.
    if(!classA.getComment().isEmpty())
    {
      //Get the comment.
      JavadocComment asJavadocComment = classA.getComment().get().asJavadocComment();
      
      //Append the comment.
      fw.append(asJavadocComment.toString());
    }
  }
  
  /**
   * <p>When regenerating the plain Java file, make sure the fields that have been added by the user (that were not originally generated
   * by FXML2Java) are added to the regenerated file.
   * 
   * @param fw
   * @param classA
   * @throws IOException
   */
  static void outputExistingNonGeneratedFields(BufferedWriter fw, ClassOrInterfaceDeclaration classA, IndentManager idm) throws IOException
  {
    //Retain only existing fields without an @FXML annotation. 
    List<FieldDeclaration> fields = classA.getFields();
    for(FieldDeclaration fd : fields)
    {
      Optional<AnnotationExpr> annotationByName = fd.getAnnotationByName("FXML");
      if(!annotationByName.isPresent())
      {
        outputIndentedString(fw, idm, fd.toString());
      }
    }
    
    //End with new line.
    fw.newLine();
  }
  
  /**
   * 
   * @param fw
   * @param constructors
   * @param idm
   * @throws IOException
   */
  static void outputExistingNonDefaultConstructors(BufferedWriter fw, List<ConstructorDeclaration> constructors, IndentManager idm) throws IOException
  {
    for(ConstructorDeclaration cs : constructors)
    {
      //Split constructor lines up and ident them correctly.
      String[] constLines = cs.toString().split("\n");

      //Output method lines.
      outputExistingMethodLines(fw, constLines, idm);
    }
    
    //End with new line.
    fw.newLine();
  }
  
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
  //GENERATE NEW JAVA FILE METHODS ONLY.
  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
  /**
   * <p>
   * 
   * @param fw
   * @param fxmlr
   * @param idm
   * @throws IOException 
   */
  static void outputInitConstructor(BufferedWriter fw, String className, IndentManager idm) throws IOException
  {
    // Declare constructor with resource bundle.  Can be null if there are no i18n strings.
    outputIndentedString(fw, idm, "public " + className + "(ResourceBundle resourceBundle)");

    //Open brace.
    openBracket(fw, idm);
    
    //Assign the resource bundle.
    outputIndentedString(fw, idm, "this.resourceBundle = resourceBundle;");
    
    // Call the setup routine.
    outputIndentedString(fw, idm, "setupFX();");

    // Call the set text strings routine.
    outputIndentedString(fw, idm, "setTextStrings();");
    
    // Call the user configuration routine.
    outputIndentedString(fw, idm, "userConfig();");

    // Close constructor definition.
    closeBracket(fw, idm);
    fw.newLine();
  }
  
  /**
   * 
   * @param fw
   * @param idm
   * @throws IOException
   */
  static void outputInitUserConfig(BufferedWriter fw, IndentManager idm) throws IOException
  {
    // Declare constructor with resource bundle.  Can be null if there are no i18n strings.
    outputIndentedString(fw, idm, "private void userConfig()");

    //Open brace.
    openBracket(fw, idm);

    //Empty method initially.  User fills in configuration here.
    
    // Close constructor definition.
    closeBracket(fw, idm);
    fw.newLine();
  }
  
  /**
   * 
   * @param fw
   * @param idm
   * @throws IOException
   */
  static void outputInitSetTextStrings(BufferedWriter fw, FXMLJavaDescriptor fxmlr, IndentManager idm) throws IOException
  {
    //Output method signature.
    outputIndentedString(fw, idm, "private void setTextStrings()");

    //Open brace.
    openBracket(fw, idm);

    // Setup declarations.
//    fxmlr.outputTextStringUpdateInstructions((decl) ->
//    {
//      outputIndentedString(fw, idm, decl);
//    });
    for(String s : fxmlr.getTextStringUpdateInstructionList())
    {
      outputIndentedString(fw, idm, s);
    }
    
    // Close constructor definition.
    closeBracket(fw, idm);
    fw.newLine();
  }

  /**
   * 
   * @param fw
   * @param idm
   * @throws IOException
   */
  static void outputInitResourceUpdate(BufferedWriter fw, IndentManager idm) throws IOException
  {
    //Method signature.
    outputIndentedString(fw, idm, "public void resourceUpdate(ResourceBundle resourceBundle)");

    //Open brace.
    openBracket(fw, idm);

    outputIndentedString(fw, idm, "this.resourceBundle = resourceBundle;");
    outputIndentedString(fw, idm, "setTextStrings();");

    // Close constructor definition.
    closeBracket(fw, idm);
    fw.newLine();
  }

  /**
   * 
   * @param fw
   * @param idm
   * @param handlerList
   * @throws IOException
   */
  public static void outputInitHandler(BufferedWriter fw, IndentManager idm, Handler h) throws IOException
  {
//    for(Handler h : handlerList)
    {
      //Append stub method.
      outputIndentedString(fw, idm, "private void " + h.getHandlerName() + h.getMethodDeclaration());
      // Open handler stub definition.
      openBracket(fw, idm);
      // Close handler stub definition.
      closeBracket(fw, idm);
      fw.newLine();
    }
    
  }

  //DISABLED.  THIS IS BEING HANDLED STATICALLY IN THE DOM RESOLVER.  LEFT IN HERE IN CASE IT BECOMES NECESSARY IN FUTURE.
////INJECT RELATIVE RESOURCE FINDER IF REQUIRED.
//if(fxmlr.injectResourceFinder)
//{
//  // Declare helper code.
//  fw.append("\tprivate String findRelativeResource(String relPath)");
//  fw.newLine();
//  fw.append("\t{");
//  fw.newLine();
//
//  // Return the main container.
//  fw.append("\t\t//Get the class package name.").append('\n');
//  fw.append("\t\tString pckg = getClass().getPackageName().replaceAll(\"\\\\.\", \"/\");").append('\n');
//  fw.append("\t\t//Find the number of separators.").append('\n');
//  fw.append("\t\tint seps = 0;").append('\n');
//  fw.append("\t\tfor(int i = 0; i < pckg.length(); i++) if(pckg.charAt(i) == '/') seps++;").append('\n');
//  fw.append("\t\t//Log the separator positions.").append('\n');
//  fw.append("\t\tint seppos[] = new int[seps];").append('\n');
//  fw.append("\t\tint sepposIX = 0;").append('\n');
//  fw.append("\t\tfor(int i = 0; i < pckg.length(); i++) if(pckg.charAt(i) == '/') seppos[sepposIX++] = i;").append('\n');
//  
//  fw.append("\t\t//Calculate the number of relative directories to regress.").append('\n');
//  fw.append("\t\tint normStart = 0;").append('\n');
//  fw.append("\t\tfor(normStart = 0; normStart < relPath.length(); normStart++)").append('\n');
//  fw.append("\t\t{").append('\n');
//  fw.append("\t\t\tif(relPath.charAt(normStart) == '.') continue;").append('\n');
//  fw.append("\t\t\telse if(relPath.charAt(normStart) == '/')").append('\n');
//  fw.append("\t\t\t{").append('\n');
//  fw.append("\t\t\tsepposIX--;").append('\n');
//  fw.append("\t\t\t}").append('\n');
//  fw.append("\t\t\telse").append('\n');
//  fw.append("\t\t\t{").append('\n');
//  fw.append("\t\t\t//Normal char - break.").append('\n');
//  fw.append("\t\t\tbreak;").append('\n');
//  fw.append("\t\t\t}").append('\n');
//  fw.append("\t\t}").append('\n');
//  
//  fw.append("\t\treturn '/' + pckg.substring(0, seppos[sepposIX]) + '/' + relPath.substring(normStart);").append('\n');
//
//  // Close retriever.
//  fw.append("\t}");
//  fw.newLine();
//  fw.newLine();
//  
//}


}
