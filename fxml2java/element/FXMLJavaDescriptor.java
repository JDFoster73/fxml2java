/**
Licensed to the Apache Software Foundation (ASF) under one
or more contributor license agreements.  See the NOTICE file
distributed with this work for additional information
regarding copyright ownership.  The ASF licenses this file
to you under the Apache License, Version 2.0 (the
"License"); you may not use this file except in compliance
with the License.  You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing,
software distributed under the License is distributed on an
"AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
KIND, either express or implied.  See the License for the
specific language governing permissions and limitations
under the License.    
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
