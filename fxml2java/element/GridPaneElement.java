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
    switch (element.getNodeName())
    {
      // Constraints.
      case "rowConstraints":
        handleRowConstraints(element);
        break;
      case "columnConstraints":
        handleColumnConstraints(element);
        break;
      default:
        super.handleElement(element);
    }
    
  }
  
  private void handleRowConstraints(Element element)
  {
    // Get each constraint element and add instructions for it.
    NodeList childNodes = element.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++)
    {
      Node item = childNodes.item(i);
      if (item.getNodeType() == Node.ELEMENT_NODE)
      {
        // Constraint.
        Element constraint = (Element) item;
        
        // Create the constraint instance.
        FXMLElement createSubNode = createSubNode(constraint);
        addSubNode(createSubNode);
        
        // Add the constraint to the instance.
        addInstanceInstruction(instanceName + ".getRowConstraints().add(" + createSubNode.instanceName + ");");
      }
    }
  }
  
  private void handleColumnConstraints(Element element)
  {
    // Get each constraint element and add instructions for it.
    NodeList childNodes = element.getChildNodes();
    for (int i = 0; i < childNodes.getLength(); i++)
    {
      Node item = childNodes.item(i);
      if (item.getNodeType() == Node.ELEMENT_NODE)
      {
        // Constraint.
        Element constraint = (Element) item;
        
        // Create the constraint instance.
        FXMLElement createSubNode = createSubNode(constraint);
        addSubNode(createSubNode);
        
        // Add the constraint to the instance.
        addInstanceInstruction(instanceName + ".getColumnConstraints().add(" + createSubNode.instanceName + ");");
      }
    }
  }
  
}
