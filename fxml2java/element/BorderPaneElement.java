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
