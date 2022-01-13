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
import org.w3c.dom.NodeList;

public class TableViewElement extends RegionElement
{
  /**
   * <p>Create VBox element.
   * 
   * @param element
   */
  public TableViewElement(Element element, ElementCreator elCreator)
  {
    super(element, elCreator);
  }
  
  @Override
  protected void handleElement(Element element)
  {
    //Columns.
    switch(element.getNodeName())
    {
      case "columns":
        handleColumms(element);
        break;
      default:
        super.handleElement(element);
    }
  }

  private void handleColumms(Element element)
  {
    //Create a string builder for the add children postfix instruction.
//    StringBuilder sb = new StringBuilder();
//    sb.append(instanceName + ".getColumns().addAll(");
//    boolean sep = false;
    
    //Get the <TableColumn>s
    NodeList elementsByTagName = element.getElementsByTagName("TableColumn");
    //This should return a node list with column elements in it.
    for(int i = 0; i < elementsByTagName.getLength(); i++)
    {
      //Get the table column element.
      Element elTCol = (Element) elementsByTagName.item(i);
      
      //Add the instruction.
      FXMLElement createSubNode = createSubNode(elTCol);
      
      //Add the table column to the table view's sub-node list.
      addSubNode(createSubNode);

      //Separator.
  //    if(sep) sb.append(", ");
      //Add to child instances.
  //    sb.append(createSubNode.instanceName);
      //Set sep.
  //    sep = true;

      //Create a postfix instruction to add the children to the regions children property.
      addInstanceInstruction(instanceName + ".getColumns().add(" + createSubNode.instanceName + ");");
      
      //Add the field reference if the element has an fx:id attribute.
      //String tcolID = elTCol.getAttribute("fx:id");
      //if(!tcolID.isBlank()) 
    }
    
    
    //Finish postfix create children.
  //  sb.append(");");
  }  
}
