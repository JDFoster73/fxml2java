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
 * <p>Special support for Color.  This is immutable and arguments must be specified in the constructor.  Arguments can be specified
 * by attributes or sub-elements.
 * 
 * @author James David Foster
 *
 */
public class ColorElement extends FXMLElement
{
  /**
   * <p>Create Color.
   * 
   * @param element
   */
  public ColorElement(Element element, ElementCreator elCreator)
  {
    //Create skeleton superclass with no instructions.
    super(elCreator, element.getNodeName());
    
    //Check the import for javafx.scene.paint.Color.
    checkImport("javafx.scene.paint.Color");
    
    //Insets values:
    String red = "0.0";
    String green = "0.0";
    String blue = "0.0";
    String opacity = "1.0";
    
    //Attempt to set from attributes.
    String attrString = "";
    if( !"".equals(attrString = element.getAttribute("red"))) red = propHandler.getPropertyValue("_double", attrString, this);
    if( !"".equals(attrString = element.getAttribute("green"))) green =  propHandler.getPropertyValue("_double", attrString, this);
    if( !"".equals(attrString = element.getAttribute("blue"))) blue = propHandler.getPropertyValue("_double", attrString, this);
    if( !"".equals(attrString = element.getAttribute("opacity"))) opacity = propHandler.getPropertyValue("_double", attrString, this);
    
    //Unlikely but allow for sub-element property values.
    NodeList childNodes = element.getChildNodes();
    for(int i = 0; i < childNodes.getLength(); i++)
    {
      //Only process element nodes.
      Node nd = childNodes.item(i);
      if(nd.getNodeType() == Node.ELEMENT_NODE)
      {
        //Cast to element.
        Element el = (Element) nd;
        
        //Check element name.
        String tagname = el.getTagName();
        //If element tag reflects a known constructor argument then set the argument value.
        switch(tagname)
        {
          case "red":
            red = propHandler.getPropertyValue("_double", el.getTextContent(), this);
            break;
          case "green":
            green = propHandler.getPropertyValue("_double", el.getTextContent(), this);
            break;
          case "blue":
            blue = propHandler.getPropertyValue("_double", el.getTextContent(), this);
            break;
          case "opacity":
            opacity = propHandler.getPropertyValue("_double", el.getTextContent(), this);
            break;
        }
      }
    }
    
    //OK - finished processing constructor arguments.  Declare the constructor instruction.
    addInstanceInstruction("Color" + instanceName + " = Color.color(" + red + ", " + green + ", " + blue + ", " + opacity);    
  }
}
