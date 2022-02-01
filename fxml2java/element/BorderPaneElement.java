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

import fxml2java.util.Util;

/**
 * <p>Extension of the {@link RegionElement} to add borderpane-specific property support.  
 * 
 * @author James David Foster
 *
 */
public class BorderPaneElement extends RegionElement
{  
  /**
   * <p>Create BorderPane element.
   * 
   * @param element
   */
  public BorderPaneElement(Element element, ElementCreator elCreator)
  {
    super(element, elCreator);
  }

  /**
   * <p>Special treatment for border properties.
   */
  @Override
  protected void handleElement(Element element)
  {
    switch(element.getNodeName())
    {
      //Constraints.
      case "left":
      case "right":
      case "center":
      case "top":
      case "bottom":
        handleBorderPane(element);
        break;
      default:
        //Handle all other elements.
        super.handleElement(element);
    }
    
  }

  /**
   * <p>Add the instructions for setting the border property with a newly created node.
   * 
   * @param element
   */
  private void handleBorderPane(Element element)
  {
    //Get the border.
    String border = Util.capitalise(element.getNodeName());
    
    //Create the node and add it to the border specified.
    //Only one node can be added to a border.
    Element borderEl = (Element) Util.getFirstXMLElement(element);
    
    String borderChildInstance = createInstanceName(borderEl);
    createElementInstructions(borderEl, borderChildInstance);
    //Add the tab to the instance's tabs property.
    addInstanceInstruction(instanceName + ".set" + border + "(" + borderChildInstance + ");");
    
  }
}
