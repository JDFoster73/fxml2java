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

import javafx.scene.layout.Region;

/**
 * <p>Add support for {@link Region} subclasses - they contain children and padding insets.
 * 
 * @author James David Foster
 *
 */
public class RegionElement extends FXMLElement
{
  protected RegionElement(Element element, ElementCreator elCreator)
  {
    super(element, elCreator);
  }

  @Override
  protected void handleElement(Element element)
  {
    //Constraints.
    switch(element.getNodeName())
    {
      case "children":
        handleChildren(element);
        break;
      case "padding":
        handlePadding(element);
        break;
      default:
        super.handleElement(element);
    }
  }

  /**
   * <p>Create the insets for this node.
   * 
   * @param element
   */
  private void handlePadding(Element element)
  {
    //Get the <Insets>
    NodeList elementsByTagName = element.getElementsByTagName("Insets");
    //This should return a node list with one element in it.
    if(elementsByTagName.getLength() > 0)
    {
      //Get the insets new (sub-)instruction.
      //ASSUME that the first item is an element of <Insets> 
      String insetsCreateInstruction = getInsetsCreateInstruction((Element) elementsByTagName.item(0));
      
      //Add the instruction.
      addInstanceInstruction(instanceName + ".setPadding(" + insetsCreateInstruction + ");");
    }
  }

  /**
   * <p>Create child nodes of this node.
   * 
   * @param element
   */
  private void handleChildren(Element element)
  {
    //Create a string builder for the add children postfix instruction.
    StringBuilder sb = new StringBuilder();
    sb.append(instanceName + ".getChildren().addAll(");
    boolean sep = false;
    
    //Create JFX objects for FXML elements.
    NodeList childNodes = element.getChildNodes();
    for(int i = 0; i < childNodes.getLength(); i++)
    {
      Node item = childNodes.item(i);
      if(item.getNodeType() == Node.ELEMENT_NODE)
      {
        //Add the element to the children of this node.
        FXMLElement createSubNode = createSubNode((Element) item);
        
        //Add sub node to this node's list.
        addSubNode(createSubNode);
        
        //Separator.
        if(sep) sb.append(", ");
        //Add to child instances.
        sb.append(createSubNode.instanceName);
        //Set sep.
        sep = true;
      }
    }
    
    //Finish postfix create children.
    sb.append(");");
    
    //Create a postfix instruction to add the children to the regions children property.
    addInstanceInstruction(sb.toString());
  }
}
