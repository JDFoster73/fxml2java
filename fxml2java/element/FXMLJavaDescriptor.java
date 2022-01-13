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
package fxml2java.element;

import java.io.IOException;

import fxml2java.Handler;
import fxml2java.StringReceiver;

/**
 * <p>The Java descriptor contains the parsed information of the source FXML file that can be used for constructing the
 * plain Java equivalent.
 * 
 * @author James David Foster
 *
 */
public interface FXMLJavaDescriptor
{
  /**
   * <p>Add a java class import to the generated file.
   * 
   * @param import_
   */
  public void addImport(String import_);
  
  /**
   * <p>Add a field descriptor to the generated file.
   * 
   * @param string
   */
  public void addField(String string);

  /**
   * <p>Output the java commands for building the root element container and all of its child elements.
   * 
   * @param receiver the string receiver to send the Java commands to.
   * @throws IOException if an exception is thrown by the receiver.
   */
  public void outputElementInstructions(StringReceiver receiver) throws IOException;

  /**
   * <p>Return the generics diamond operator for the main container, if it has generic arguments.
   * 
   * @return
   */
  public String getRootInstanceGenericDecl();

  /**
   * <p>Get the java instance name for the root container.  The root instance will always be created as a field of the generated Java class.
   * 
   * @return
   */
  public String getRootInstanceName();

  /**
   * <p>Get the data type of the root instance (non-qualified).
   * 
   * @return
   */
  public String getRootInstanceDataType();

  /**
   * <p>Get a list of fully-qualified java Class names that should be imported into the generated Java class.
   * 
   * @return
   */
  public String[] getImportList();

  /**
   * <p>Return a list of Handlers which are specified to handle events that are specified in the FXML source.
   * 
   * @return
   */
  public Handler[] getHandlerList();

  /**
   * <p>Get the list of field definition commands for the generated source.
   * 
   * @return
   */
  public String[] getFieldList();

  /**
   * <p>Output instructions that set the text content of fx objects from resource bundles.
   * 
   * @param object
   */
  public String[] getTextStringUpdateInstructionList();
}
