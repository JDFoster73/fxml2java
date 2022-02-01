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
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import fxml2java.Handler;
import fxml2java.StringReceiver;

/**
 * <p>The Java descriptor contains the parsed information of the source FXML file that can be used for constructing the
 * plain Java equivalent.
 * 
 * @author James David Foster
 *
 */
class FXMLJavaDescriptorImpl implements FXMLJavaDescriptor
{
  /**
   * <p>The source location.  This may be required for resource finding.
   */
  public final String sourceLocation;
  
  /**
   * <p>Construct with the given source file location.
   * 
   * @param sourceLocation
   */
  public FXMLJavaDescriptorImpl(String sourceLocation)
  {
    this.sourceLocation = sourceLocation;
  }

  /**
   * <p>List of Java import statements.
   */
  private Set<String> importList = new HashSet<>();

  /**
   * <p>List of class fields so that internal software can access visual components if required.
   */
  private List<String> fieldList = new ArrayList<>();

  /**
   * <p>List of event handler callbacks required.  These will be created as stubs if they don't exist but not overwritten if they do.
   */
  public List<Handler> handlerList = new ArrayList<>();
  
  /**
   * <p>List of commands for setting i18n text.
   */
  public List<String> setI18NTextCommandList = new ArrayList<>();
  
  /**
   * <p>To get round the lack of relative path support in the java resource finding services, inject a custom relative resource finder.
   * @deprecated not used any more.
   */
  public boolean injectResourceFinder = false;
  
  /**
   * <p>The root fxml element containing the instructions.
   */
  private FXMLElement rootElement;
  
  /**
   * {@inheritDoc}
   */
  @Override
  public void outputElementInstructions(StringReceiver receiver) throws IOException
  {
    rootElement.outputInstructions(receiver);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRootInstanceGenericDecl()
  {
    return rootElement.instanceGenericType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRootInstanceName()
  {
    return rootElement.instanceName;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getRootInstanceDataType()
  {
    return rootElement.instanceDataType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String[] getImportList()
  {
    // TODO Auto-generated method stub
    //List<String> copyList = new ArrayList<>(importList);
    return importList.toArray(new String[] {});
  }

  /**
   * <p>Set the root element of the descriptor.  This contains all of the java instructions necessary to build the form from the FXML description.
   * @param root
   */
  public void setRootElement(FXMLElement root)
  {
    rootElement = root;
  }
  
  /**
   * <p>Add an import declaration to the import list.
   * @param import_
   */
  @Override
  public void addImport(String import_)
  {
    importList.add(import_);
  }

  /**
   * <p>Add the given handler instruction.  This instruction will be used to create an action handler in the generated object.
   * 
   * @param handler
   */
  public void addHandlerInstruction(Handler handler)
  {
    handlerList.add(handler);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addField(String string)
  {
    fieldList.add(string);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Handler[] getHandlerList()
  {
    //
    return handlerList.toArray(new Handler[] {});
  }

  @Override
  public String[] getFieldList()
  {
    return fieldList.toArray(new String[] {});
  }

  /**
   * <p>Text string update instruction output.  These instructions are anything that sets a text value using
   * an i18n source.
   * 
   */
  @Override
  public String[] getTextStringUpdateInstructionList()
  {
    return setI18NTextCommandList.toArray(new String[] {});
  }
}
