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
      case "TabPane":
        return new TabPaneElement(element, this);
      case "BorderPane":
        return new BorderPaneElement(element, this);
      //ALL GENERIC REGION ELEMENTS HERE.  THESE ARE ELEMENTS THAT HAVE NO EXTENDED FUNCTIONALITY BEYOND THE JAVAFX REGION CLASS 
      //AND ARE COMPLETELY DEALT WITH WITHIN THAT CLASS.
      case "ComboBox":
      case "VBox":
      case "HBox":
      case "StackPane":
      case "ListView":
        return new RegionElement(element, this);
      //ALL GENERIC LABELED ELEMENTS HERE.  THESE ARE ELEMENTS THAT HAVE NO EXTENDED FUNCTIONALITY BEYOND THE JAVAFX LABELED CLASS 
      //AND ARE COMPLETELY DEALT WITH WITHIN THAT CLASS.
      case "RadioButton":
      case "CheckBox":
      case "ChoiceBox":
      case "Button":
        return new LabeledElement(element, this);
      //ALL GENERIC FXML ELEMENTS HERE.  THESE ARE ELEMENTS THAT HAVE NO EXTENDED FUNCTIONALITY AND ARE COMPLETELY DEALT WITH
      //WITHIN THE BASE FXMLElement CLASS.
      case "TableColumn":
      case "Label":
      case "TextField":
      case "TextArea":
      case "PasswordField":
      case "ImageView":
      case "Text":
      case "Tab":
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
