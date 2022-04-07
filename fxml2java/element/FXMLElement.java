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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import fxml2java.Handler;
import fxml2java.StringReceiver;
import fxml2java.util.Util;

/**
 * <p>This is the base class for turning FXML into Java source code.  Upon creation, the instance will read its FXML directive and create Java statements
 * for creating itself in a POJO environment.  It will recursively build all sub elements of itself.
 * 
 * @author James David Foster
 *
 */
public class FXMLElement
{
  /**
   * <p>The instance name - will be the fx:id attribute if it exists or an internally-generated instance name if not.
   */
  public final String instanceName;
  
  /**
   * <p>The generic types of the instance if applicable.  For example, a TableView will require a generic argument for the item data type.
   */
  public final String instanceGenericType;
  
  /**
   * <p>The Java datatype class name of the instance.
   */
  public final String instanceDataType;
  
  /**
   * <p>True if this instance is specified as a field. 
   */
  public final boolean isField;
  
  /**
   * <p>The element creator for accessing the descriptor and creating nested sub-elements.
   */
  private final ElementCreator elementCreator;
  
  /**
   * Java instructions for creating this instance.
   */
  private final List<String> instanceInstructionList = new ArrayList<>();

  /**
   * <p>Sub nodes of this node.
   */
  private final List<FXMLElement> subNodeList = new ArrayList<>();

  /**
   * <p>Handler for attribute name and value.
   */
  protected final PropertyHandler propHandler = new PropertyHandler();
  
  /**
   * <p>Create instance instructions for the given element and process all of the element's sub-nodes.
   * 
   * @param element
   * @param elCreator
   */
  protected FXMLElement(Element element, ElementCreator elCreator)
  {
    //Store ref to element creator.
    this.elementCreator = elCreator;

    //Instance data type is the element node name.
    instanceDataType = element.getNodeName();
    
    //Create the diamond operator for the declaration.
    instanceGenericType = createGenericArgs(element);
    
    //Is field?
    isField = !element.getAttribute("fx:id").isBlank();
    
    //Create the element id.
    instanceName = createInstanceName(element);
    
    //Create element instructions from attributes.
    createElementInstructions(element);
    
    //Process sub-elements.
    //
    NodeList childNodes = element.getChildNodes();
    for(int i = 0; i < childNodes.getLength(); i++)
    {
      Node item = childNodes.item(i);
      if(item.getNodeType() == Node.ELEMENT_NODE)
      {
        handleElement((Element) item);
      }
    }
  }
  
  /**
   * <p>Simply store the element creator and let the subclass set up its instructions.  Helpful for implementations which must set
   * their state in a constructor (immutable) such as Color, Insets, Font and others.
   * <p>The implementations that will call this constructor are ancillary classes and not descendants of java.scene.Node.  As such,
   * they can not be fields (no fx:id) nor have generic arguments.
   * 
   * 
   * @param element
   * @param elCreator
   */
  protected FXMLElement(ElementCreator elCreator, String instanceDataType)
  {
    //Store ref to element creator.
    this.elementCreator = elCreator;

    //Instance data type is the element node name.
    this.instanceDataType = instanceDataType;
    
    //Not a field.
    isField = false;
    
    //No generic information.
    this.instanceGenericType = "";
    
    //Node name.
    this.instanceName = instanceDataType.toLowerCase() + "LINST" + String.format("%1$04d", elementCreator.getNextID());
  }

  /**
   * <p>Create an instance name for an element.  If the element does not have an fx:id then a temporary instance name will be created.
   * 
   * @param element
   * @return
   */
  protected final String createInstanceName(Element element)
  {
    //Attribute map.
    NamedNodeMap attributes = element.getAttributes();

    String subElementID;
    
    //Set the generic argument to an empty diamond operator if generic arguments exist.
    String genericArg = (!instanceGenericType.isBlank()) ? "<>" : "";
    
    //Look for an "fx:id".
    Node namedItem = attributes.getNamedItem("fx:id");

    if(namedItem != null)
    {
      //This instance has an fx:id.
      subElementID = namedItem.getNodeValue();

      //Add field instruction.
      elementCreator.getDescriptor().addField(element.getNodeName() + instanceGenericType + " " + subElementID);
      
      //First instruction - create the instance.
      addInstanceInstruction(subElementID + " = new " + element.getNodeName() + genericArg + "(" + "" + ");");
    }
    else
    {
      //Next ID number.
      int idNum = elementCreator.getNextID();
      
      //Element id.
      subElementID = element.getNodeName().toLowerCase() + "LINST" + String.format("%1$04d", idNum);
      
      //Make sure there is a field if the id number is 0.
      if(idNum == 0) elementCreator.getDescriptor().addField(element.getNodeName() + instanceGenericType + " " + subElementID);
      
      //First instruction - create the instance.
      addInstanceInstruction( ((idNum != 0) ? element.getNodeName() + instanceGenericType + " " : "") + subElementID + " = new " + element.getNodeName() + genericArg + "(" + "" + ");");//constructorArgs
    }
    
    //Return the sub-element id to the caller.
    return subElementID;
  }
  
  
  /**
   * <p>Create element instructions, which handles attributes of the instance's fxml element and creates Java statements for them.
   * 
   * @param element
   * @param instanceNameToUse
   */
  protected final void createElementInstructions(Element element)
  {
    //Attribute map.
    NamedNodeMap attributes = element.getAttributes();
    
    //Create attribute instructions.
    for(int i = 0; i < attributes.getLength(); i++)
    {
      //Get the next attribute.
      Attr attribute = (Attr) attributes.item(i);
      
      //handle attrib.
      handleAttribute(attribute.getName(), attribute.getValue());
      
    }
  }
  
  /**
   * <p>Create generic arguments for the instance.  Generic arguments are specified using a trick which utilises the fxml id attribute.  In scenebuilder,
   * set the id field using [id][<gen_arg,gen_arg,...].  If the element does not need an id field then simply (for example) set the field to be "<String,String" in
   * the case of an object which requires two generic arguments.
   * 
   * @param element
   * @return
   */
  protected final String createGenericArgs(Element element)
  {
    //Attribute map.
    NamedNodeMap attributes = element.getAttributes();

    //Look at generic arguments.
    //
    //This is a special case as there isn't any support for generics in SceneBuilder.  We use the id attribute and if there is a '<' character
    //then we'll treat everything afterwards as comma-separated generic arguments.
    Node idAttr = attributes.getNamedItem("id");
    //String genericArg = "";
    String genericField = "";
    
    if(idAttr != null)
    {
      //Attribute value for id.
      String idAttrValue = idAttr.getNodeValue();
      
      //Check for '<'.
      if(idAttrValue.contains("<"))
      {
        //Get the generic portion of the id attribute.
        String genargs = idAttrValue.substring(idAttrValue.indexOf('<') + 1);
        
        //Generic argument.
        //
        //
        String[] genArgs = genargs.split(",");
        
        //Start the gen args.
        StringBuilder sb = new StringBuilder();
        sb.append("<");
        
        //For each generic argument.
        for(String genArg : genArgs)
        {
          //Add to the field generics.
          //
          //Only include the class name and not the fully-qualified name.
          int gendotix = genArg.lastIndexOf('.');
          if(gendotix != -1) 
          {
            //Add import for the generic parameter ONLY IF THE CLASS NAME IS FULLY-QUALIFIED.  Non-qualified classnames belong to java.lang
            //and do not need to be imported.
            elementCreator.getDescriptor().addImport(genArg);
            //Strip package qualifier from generic argument.
            genArg = genArg.substring(gendotix + 1);
          }
          
          //Append a separator if not first arg.
          if(sb.length() > 1) sb.append(", ");
          sb.append(genArg);
        }
        
        sb.append(">");
        //Set the generic field.
        genericField = sb.toString();
        
      }
    }
    
    //Return the generic field.
    return genericField;
  }
  
  private void handleAttribute(String attrName, String attrVal)
  {
    //Special handling.
    //
    //don't process "fx:id" or anything prefixed with "xml".
    //if the attrName is "id" then make sure any generic information is stripped first, and if blank then don't call.
    if(!"fx:id".equals(attrName) && !attrName.startsWith("xml"))
    {
      //id.
      if("id".equals(attrName))
      {
        //Split generic information.
        String[] idFields = attrVal.split("\\<");
        if(!idFields[0].isBlank())
        {
          //Add instructions for the attribute.
          propHandler.handleProperty(attrName, attrVal, this);
        }
      }
      else
      {
        //Add instructions for the attribute.
        propHandler.handleProperty(attrName, attrVal, this);
      }

    }
  }
  
  /**
   * <p>Add a Java instruction for this instance.
   * 
   * @param instruction
   */
  protected final void addInstanceInstruction(String instruction)
  {
    instanceInstructionList.add(instruction);
  }

  /**
   * <p>Add a sub node of this element.
   * 
   * @param node
   */
  protected final void addSubNode(FXMLElement node)
  {
    //Add to the subnode list.
    subNodeList.add(node);
  }

  /**
   * <p>Create a node instance that is linked to the instance in some way that is defined by the calling subclass.  An example
   * is a TableColumn, which is not a child node of the TableView owner but should be added to its columns collection.
   *  
   * @param node
   */
  protected final FXMLElement createSubNode(Element node)
  {
    return elementCreator.createElement(node);
  }
  
  /**
   * <p>Add an instruction to to the i18n list.
   * 
   * @param instruction
   */
  final void addI18NInstruction(String instruction)
  {
    elementCreator.getDescriptor().setI18NTextCommandList.add(instruction);
  }

  /**
   * <p>Process general elements here.
   * 
   * @param element
   */
  protected void handleElement(Element element)
  {    
    //REJIG THIS!!!
    //WE REALLY NEED TO TAKE IT CASE-BY-CASE OR TRYING TO FIND A UNIFYING METHOD WILL BE CRAZY.
    //HANDLE SEMANTICALLY - ALL ELEMENTS HANDLED IN SUBCLASSES BY THEIR TYPE AND UNKNOWNS WILL 
    //NEED ADDING.
    
    ///////////////////////////////////////////////////////////////////////// FXLM ELEMENTS.
    //FXML MAIN ELEMENTS
    if(elementCreator.isJavaFXElement(element))
    {
      createInternalElementNode(element);
    }
    else
    {
      //Get the sub-element name.
      String subElementName = element.getNodeName();
      
      //Static or instance property?
      if(subElementName.contains("."))
      {
        //Static property.
        //Split sub element name around the '.'
        String[] parts = subElementName.split("\\.");
        
        //Get an iterator for all sub elements.  These must be properties that can be instanced by the element creator.
        Iterator<Element> elIt = Util.getSubElementIterator(element);
        
        //Set all properties.
        for( ; elIt.hasNext(); )
        {
          //Get the next element.
          Element nextXMLElement = elIt.next();

          //Create the sub-node.
          FXMLElement subElement = elementCreator.createElement(nextXMLElement);

          //Add to node as sub node.  The instructions for the property must be created before they can be referenced in the instruction below.
          addSubNode(subElement);
          
          //Create the instruction.
          addInstanceInstruction(parts[0] + ".set" + Util.capitalise(parts[1]) + "(" + instanceName + ", " + subElement.instanceName + ");");       
        }
      }
      else
      {
        //Instance property.

        //Single, settable property.
        Element firstXMLElement = Util.getFirstXMLElement(element);
        
        //Create it.
        FXMLElement subElement = elementCreator.createElement(firstXMLElement);
        
        //Add to node as sub node.  The instructions for the property must be created before they can be referenced in the instruction below.
        addSubNode(subElement);
        
        //Create the instruction.
        addInstanceInstruction(instanceName + ".set" + Util.capitalise(subElementName) + "(" + subElement.instanceName + ");");       
      }
    }
  }
  
  /*
   * As a result of processing child elements of this FXML element, create sub node instances and add them to the subnode list
   * in one handy operation. 
   */
  protected FXMLElement createInternalElementNode(Element element)
  {
    //Use the element creator to create the element and add as a child.
    FXMLElement createElement = elementCreator.createElement(element);
    
    //Add element instructions and register element as a child.
    subNodeList.add(createElement);
    
    //Create an add child instruction.
    //JavaFX elements which contain other JavaFX elements are inherently Parents; <children> is optional.
    addInstanceInstruction(this.instanceName + ".getChildren().add(" + createElement.instanceName + ");");
    
    //Return the created element.
    return createElement;
  }
  
  /**
   * <p>Add the given import if it is not already present.
   * 
   * @param qualifiedName
   */
  protected void checkImport(String qualifiedName)
  {
    elementCreator.getDescriptor().addImport(qualifiedName);
  }

  /**
   * <p>Output the Java instructions for this element.  Output sub node instructions first and then this instances instructions.
   * 
   * @param receiver
   * @throws IOException
   */
  public void outputInstructions(StringReceiver receiver) throws IOException
  {    
    //Sub nodes.
    for(FXMLElement fe : subNodeList) fe.outputInstructions(receiver);

    //
    for(String s : instanceInstructionList) receiver.receive(s);
    
    //Line break after instructions.
    receiver.receive("");
  }
  
  /**
   * <p>Add the field command.  This will create the given field in the generated Java object.
   * 
   * @param fieldCommand
   */
  protected void addFieldCommand(String fieldCommand)
  {
    elementCreator.getDescriptor().addField(fieldCommand);
  }

  /**
   * <p>Add the handler method.
   * @param handler
   */
  public void addHandlerMethod(Handler handler)
  {
    elementCreator.getDescriptor().addHandlerInstruction(handler); 
  }

}
