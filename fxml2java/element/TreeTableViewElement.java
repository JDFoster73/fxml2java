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
import org.w3c.dom.NodeList;

public class TreeTableViewElement extends RegionElement
{
  /**
   * <p>Create VBox element.
   * 
   * @param element
   */
  public TreeTableViewElement(Element element, ElementCreator elCreator)
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
    //Get the <TableColumn>s
    NodeList elementsByTagName = element.getElementsByTagName("TreeTableColumn");
    //This should return a node list with column elements in it.
    for(int i = 0; i < elementsByTagName.getLength(); i++)
    {
      //Get the table column element.
      Element elTCol = (Element) elementsByTagName.item(i);
      
      //Add the instruction.
      FXMLElement createSubNode = createSubNode(elTCol);
      
      //Add the table column to the table view's sub-node list.
      addSubNode(createSubNode);

      //Create a postfix instruction to add the children to the regions children property.
      addInstanceInstruction(instanceName + ".getColumns().add(" + createSubNode.instanceName + ");");
    }
  }  
}
