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
