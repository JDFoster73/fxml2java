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

import javafx.scene.control.TabPane;

/**
 * <p>Support for a {@link TabPane} which contains tabs.
 * 
 * @author James David Foster
 *
 */
public class TabPaneElement extends RegionElement
{  
  /**
   * <p>Create GridPane element.
   * 
   * @param element
   */
  public TabPaneElement(Element element, ElementCreator elCreator)
  {
    super(element, elCreator);
  }

  @Override
  protected void handleElement(Element element)
  {
    switch(element.getNodeName())
    {
      //Constraints.
      case "tabs":
        handleTabs(element);
        break;
      default:
        super.handleElement(element);
    }
    
  }

  private void handleTabs(Element element)
  {
    //Get each constraint element and add instructions for it.
    NodeList childNodes = element.getElementsByTagName("Tab");
    for(int i = 0; i < childNodes.getLength(); i++)
    {
      //Get the tab fxml element and create a tab with it.
      Element tab = (Element) childNodes.item(i);

//      //Create the tab instance.
//      String tabInstance = createInstanceName(tab);
//      createElementInstructions(tab, tabInstance);
      //Handle the Tab's children element(s).
      FXMLElement subNode = createSubNode(tab);
      addSubNode(subNode);
      
      //Add the tab to the instance's tabs property.
      addInstanceInstruction(instanceName + ".getTabs().add(" + subNode.instanceName + ");");
      
    }
  }
  
}
