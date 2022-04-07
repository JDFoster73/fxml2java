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

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
   * <p>Elememnt creation map.  Contains one FXMLElementResponse per FXML element name.  The FXMLElementResponse
   * instance can be used to create an FXML element of the requested type.
   */
  private final Map<String, FXMLElementResponse> elementCreationMap = new HashMap<>();
  
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
    
    //Set up all FXML element responders.
    //
    //Standard ones.
    FXMLElementResponse stdFXMLEle = (e) -> {return new FXMLElement(e, this);};
    FXMLElementResponse labelledFXMLEle = (e) -> {return new LabeledElement(e, this);};
    FXMLElementResponse regionFXMLEle = (e) -> {return new RegionElement(e, this);};
    
    //Mappings for element creation.
    //Specific implementations.
    elementCreationMap.put("GridPane", (e) -> {return new GridPaneElement(e, this);});
    elementCreationMap.put("TableView", (e) -> {return new TableViewElement(e, this);});
    elementCreationMap.put("TreeTableView", (e) -> {return new TreeTableViewElement(e, this);});
    elementCreationMap.put("TabPane", (e) -> {return new TabPaneElement(e, this);});
    elementCreationMap.put("BorderPane", (e) -> {return new BorderPaneElement(e, this);});
    elementCreationMap.put("Tab", (e) -> {return new TabElement(e, this);});
    //elementCreationMap.put("RadioButton", (e) -> {return new RadioButtonElement(e, this);});
    elementCreationMap.put("Color", (e) -> {return new ColorElement(e, this);});
    elementCreationMap.put("Insets", (e) -> {return new InsetsElement(e, this);});
    elementCreationMap.put("Font", (e) -> {return new FontElement(e, this);});
    elementCreationMap.put("Image", (e) -> {return new ImageElement(e, this);});
    
    //Standard implementations.
    elementCreationMap.put("ComboBox", regionFXMLEle);
    elementCreationMap.put("VBox", regionFXMLEle);
    elementCreationMap.put("HBox", regionFXMLEle);
    elementCreationMap.put("StackPane", regionFXMLEle);
    elementCreationMap.put("AnchorPane", regionFXMLEle);
    elementCreationMap.put("ListView", regionFXMLEle);
    elementCreationMap.put("Separator", regionFXMLEle);

    elementCreationMap.put("Label", labelledFXMLEle);
    elementCreationMap.put("RadioButton", labelledFXMLEle);
    elementCreationMap.put("CheckBox", labelledFXMLEle);
    elementCreationMap.put("ChoiceBox", labelledFXMLEle);
    elementCreationMap.put("Button", labelledFXMLEle);

    elementCreationMap.put("TableColumn", stdFXMLEle);
    elementCreationMap.put("TreeTableColumn", stdFXMLEle);
    elementCreationMap.put("TextField", stdFXMLEle);
    elementCreationMap.put("TextArea", stdFXMLEle);
    elementCreationMap.put("PasswordField", stdFXMLEle);
    elementCreationMap.put("ImageView", stdFXMLEle);
    elementCreationMap.put("Text", stdFXMLEle);
    
    elementCreationMap.put("ColumnConstraints", stdFXMLEle);
    elementCreationMap.put("RowConstraints", stdFXMLEle);
    
    elementCreationMap.put("ToggleGroup", stdFXMLEle);
//    elementCreationMap.put("Font", stdFXMLEle);
//    elementCreationMap.put("Insets", stdFXMLEle);
//    elementCreationMap.put("Image", stdFXMLEle);
    
    
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
    
    //Check for fx:root.  This needs to be handled differently.
    if("fx:root".equals(elName))
    {
      //SPECIAL CASE FOR <fx:root ...> elements.  This element will contain a type attribute which specifies the fx object type name.
      String typeAttr = element.getAttribute("type");
      //Replace the fx:root with an element with the given type name.
      fjd.addImport(typeAttr);
      //Element name is only last segment of fully-qualified name.
      String elementName = typeAttr.substring(typeAttr.lastIndexOf('.') + 1);
      //Create the element.
      Element createdElement = element.getOwnerDocument().createElement(elementName);
      //Add all attributes of the fx:root element, apart from the type attribute.
      NamedNodeMap attributes = element.getAttributes();
      for(int i = 0; i < attributes.getLength(); i++)
      {
        Node attr = attributes.item(i);
        if(!"type".equals(attr.getNodeName())) createdElement.getAttributes().setNamedItem(attr.cloneNode(true));
      }
      
      //Deep copy all sub elements of original.
      NodeList subelements = element.getChildNodes();
      for(int i = 0; i < subelements.getLength(); i++)
      {
        Node subnode = subelements.item(i);
        createdElement.appendChild(subnode.cloneNode(true));
      }
      
      
      //Set the element to create from the fx:root of the parameter to the one we have created.
      element = createdElement;
      
      //Update the element name.
      elName = element.getNodeName();
    }
      
    //Look up the element from the map.
    FXMLElementResponse fxmlElementResponse = elementCreationMap.get(elName);
    
    //Check for not null - if null then a handler for the element doesn't exist and needs to be added!
    if(fxmlElementResponse != null) return fxmlElementResponse.createElement(element);
    
    //Still here - no element handler for the requested element type.
    throw new IllegalArgumentException("No processor available for element type [" + elName + "].  Please add a processor for this type.");
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

  private interface FXMLElementResponse
  {
    FXMLElement createElement(Element element);
  }
  
  /**
   * <p>Returns true if the given FXML element describes a javafx.scene.Node descendant that can be handled by this ElementCreator.
   * 
   * @param el
   * @return
   */
  public boolean isJavaFXElement(Element el)
  {
    return elementCreationMap.containsKey(el.getNodeName());
  }
}
