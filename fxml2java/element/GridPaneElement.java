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
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>Special handling for the GridPane datatype.  Handle column and row constraint directives.
 * 
 * @author James David Foster
 *
 */
public class GridPaneElement extends RegionElement
{  
  /**
   * <p>Create GridPane element.
   * 
   * @param element
   */
  public GridPaneElement(Element element, ElementCreator elCreator)
  {
    super(element, elCreator);
  }

  @Override
  protected void handleElement(Element element)
  {
    switch(element.getNodeName())
    {
      //Constraints.
      case "columnConstraints":
        handleColumnConstraints(element);
        break;
      case "rowConstraints":
        handleRowConstraints(element);
        break;
      default:
        super.handleElement(element);
    }
    
  }

  private void handleRowConstraints(Element element)
  {
    //Get each constraint element and add instructions for it.
    NodeList childNodes = element.getChildNodes();
    for(int i = 0; i < childNodes.getLength(); i++)
    {
      Node item = childNodes.item(i);
      if(item.getNodeType() == Node.ELEMENT_NODE)
      {
        //Constraint.
        Element constraint = (Element) item;
        
        //Create the constraint instance.    
        String constraintInstance = createInstanceName(constraint);
        createElementInstructions(constraint, constraintInstance);
        //Add the constraint to the instance.
        addInstanceInstruction(instanceName + ".getRowConstraints().add(" + constraintInstance + ");");
      }
    }
  }

  private void handleColumnConstraints(Element element)
  {
    //Get each constraint element and add instructions for it.
    NodeList childNodes = element.getChildNodes();
    for(int i = 0; i < childNodes.getLength(); i++)
    {
      Node item = childNodes.item(i);
      if(item.getNodeType() == Node.ELEMENT_NODE)
      {
        //Constraint.
        Element constraint = (Element) item;
        
        //Create the constraint instance.
        String constraintInstance = createInstanceName(constraint);
        createElementInstructions(constraint, constraintInstance);
        //Add the constraint to the instance.
        addInstanceInstruction(instanceName + ".getColumnConstraints().add(" + constraintInstance + ");");
      }
    }
  }
  
}
