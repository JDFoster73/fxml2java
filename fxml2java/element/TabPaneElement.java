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

      //Create the tab instance.
      String tabInstance = createInstanceName(tab);
      createElementInstructions(tab, tabInstance);
      //Add the tab to the instance's tabs property.
      addInstanceInstruction(instanceName + ".getTabs().add(" + tabInstance + ");");
      
    }
  }
  
}
