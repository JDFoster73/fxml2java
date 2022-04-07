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

import javafx.scene.control.TabPane;

/**
 * <p>Support for a {@link TabPane} which contains tabs.
 * 
 * @author James David Foster
 *
 */
public class TabElement extends RegionElement
{  
  /**
   * <p>Create GridPane element.
   * 
   * @param element
   */
  public TabElement(Element element, ElementCreator elCreator)
  {
    super(element, elCreator);
  }

  @Override
  protected void handleElement(Element element)
  {
    switch(element.getNodeName())
    {
      //Constraints.
      case "content":
        handleTab(element);
        break;
      default:
        super.handleElement(element);
    }
    
  }

  private void handleTab(Element element)
  {
    //Get each constraint element and add instructions for it.
    NodeList childNodes = element.getChildNodes();
    for(int i = 0; i < childNodes.getLength(); i++)
    {
      //Only process Element type nodes.
      if(childNodes.item(i).getNodeType() == Node.ELEMENT_NODE)
      {
        //Get the tab fxml element and create a tab with it.
        Element tabContent = (Element) childNodes.item(i);

        //Create the element.
        FXMLElement content = createSubNode(tabContent);
        //Add to the tab.
        addSubNode(content);
        
        //Add the tab to the instance's tabs property.
        addInstanceInstruction(instanceName + ".setContent(" + content.instanceName + ");");
        
        //Only one content node allowed - break.
        break;
      }
    }
  }
  
}
