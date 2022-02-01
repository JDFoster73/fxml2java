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

import org.w3c.dom.Element;

import fxml2java.IDManager;

/**
 * <p>Create an FXML-mapped element for a given element type.
 * 
 * @author James David Foster
 *
 */
public class ElementCreator
{
  /**
   * <p>Manage id numbers for non-field instances.
   */
  private final IDManager idm;
  
  /**
   * <p>Reference to the java descriptor for the generated file.
   */
  private final FXMLJavaDescriptorImpl fjd;
  
  /**
   * <p>Constructor.
   * 
   * @param idm
   * @param fjd
   */
  public ElementCreator(IDManager idm, FXMLJavaDescriptorImpl fjd)
  {
    this.idm = idm;
    this.fjd = fjd;
  }

  /**
   * <p>Create a mapped element handler for the given FXML element.
   * 
   * @param element
   * @return
   */
  public FXMLElement createElement(Element element)
  {
    //Get the element name.
    String elName = element.getNodeName();
    
    //Create an FXMLElement instance depending on the element name (which gives the type).
    switch(elName)
    {
      case "GridPane":
        return new GridPaneElement(element, this);
      case "TableView":
        return new TableViewElement(element, this);
      case "TreeTableView":
        return new TreeTableViewElement(element, this);
      case "TabPane":
        return new TabPaneElement(element, this);
      case "BorderPane":
        return new BorderPaneElement(element, this);
      //ALL GENERIC REGION ELEMENTS HERE.  THESE ARE ELEMENTS THAT HAVE NO EXTENDED FUNCTIONALITY BEYOND THE JAVAFX REGION CLASS 
      //AND ARE COMPLETELY DEALT WITH WITHIN THAT CLASS.
      case "Tab":
        return new TabElement(element, this);
      case "ComboBox":
      case "VBox":
      case "HBox":
      case "StackPane":
      case "ListView":
      case "Separator":
        return new RegionElement(element, this);
      //ALL GENERIC LABELED ELEMENTS HERE.  THESE ARE ELEMENTS THAT HAVE NO EXTENDED FUNCTIONALITY BEYOND THE JAVAFX LABELED CLASS 
      //AND ARE COMPLETELY DEALT WITH WITHIN THAT CLASS.
      case "Label":
      case "RadioButton":
      case "CheckBox":
      case "ChoiceBox":
      case "Button":
        return new LabeledElement(element, this);
      //ALL GENERIC FXML ELEMENTS HERE.  THESE ARE ELEMENTS THAT HAVE NO EXTENDED FUNCTIONALITY AND ARE COMPLETELY DEALT WITH
      //WITHIN THE BASE FXMLElement CLASS.
      case "TableColumn":
      case "TreeTableColumn":
      case "TextField":
      case "TextArea":
      case "PasswordField":
      case "ImageView":
      case "Text":
        return new FXMLElement(element, this);
      default:
        throw new IllegalArgumentException("No processor available for element type [" + elName + "].  Please add a processor for this type.");
    }
  }

  /**
   * <p>Get the next generated id count.
   * 
   * @return
   */
  public int getNextID()
  {
    return idm.getNextID();
  }

  /**
   * <p>Access the java descriptor.
   * 
   * @return
   */
  public FXMLJavaDescriptorImpl getDescriptor()
  {
    return fjd;
  }

  
}
