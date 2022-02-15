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
//      case "padding":
//        handlePadding(element);
//        break;
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
      //String insetsCreateInstruction = getInsetsCreateInstruction((Element) elementsByTagName.item(0));
      FXMLElement insetsEl = createInternalElementNode((Element) elementsByTagName.item(0));
      
      //Add the instruction.
      addInstanceInstruction(instanceName + ".setPadding(" + insetsEl.instanceName + ");");
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
