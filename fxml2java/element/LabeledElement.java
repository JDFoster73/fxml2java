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

/**
 * <p>Special support for labeled graphic property.  Support the creation of an ImageView with an image.
 * 
 * @author James David Foster
 *
 */
public class LabeledElement extends RegionElement
{
  /**
   * <p>Create VBox element.
   * 
   * @param element
   */
  public LabeledElement(Element element, ElementCreator elCreator)
  {
    super(element, elCreator);
  }

  @Override
  protected void handleElement(Element element)
  {
    //Handle inner elements.
    switch(element.getNodeName())
    {
      //Graphic property.
      case "graphic":
        handleGraphic(element);
        break;
      default:
        super.handleElement(element);
    }
  }

  private void handleGraphic(Element element)
  {
    //The graphic element refers to the graphic property of the Labeled instance.  This is a Node instance.
    //Graphic element has one child which is an <ImageView> element.
    Element item = (Element) element.getElementsByTagName("ImageView").item(0);
    
    //Create the image.
    FXMLElement createSubNode = createSubNode(item);
   
    //Set the graphic property.
    //
    //SPECIAL CASE: the inner workings of the <ImageView> are non-standard.  To make sure the call happens in the right place, attach it to the
    //sub node.  This element's instructions will be output first followed by sub nodes.
    addInstanceInstruction(instanceName + ".setGraphic(" + createSubNode.instanceName + ");");
    
    //Add to sub nodes.
    addSubNode(createSubNode);
  }
}
