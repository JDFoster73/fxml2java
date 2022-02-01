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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

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
  private final boolean isField;
  
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
    
    //Create the element id.
    instanceName = createInstanceName(element);
    
    //Is field?
    isField = !element.getAttribute("fx:id").isBlank();
    
    //Create element instructions from attributes.
    createElementInstructions(element, instanceName);
    
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

    //Look for an "fx:id".
    Node namedItem = attributes.getNamedItem("fx:id");
    String subElementID;
    
    //Set the generic argument to an empty diamond operator if generic arguments exist.
    String genericArg = (!instanceGenericType.isBlank()) ? "<>" : "";
    
    if(namedItem != null)
    {
      //This instance has an fx:id.
      subElementID = namedItem.getNodeValue();

      //Add field instruction.
      elementCreator.getDescriptor().addField(element.getNodeName() + instanceGenericType + " " + subElementID);
      
      //First instruction - create the instance.
      addInstanceInstruction(subElementID + " = new " + element.getNodeName() + genericArg + "();");
    }
    else
    {
      //Next ID number.
      int idNum = elementCreator.getNextID();
      
      //Element id.
      subElementID = element.getNodeName().toLowerCase() + "LINST" + String.format("%1$04d", idNum);
      
      //Make sure there is a field if the id number is 0.
      if(idNum == 0) elementCreator.getDescriptor().addField(element.getNodeName() + instanceGenericType + " " + subElementID);// + " = new " + element.getNodeName() + genericArg + "();");
      
      //First instruction - create the instance.
      addInstanceInstruction( ((idNum != 0) ? element.getNodeName() + instanceGenericType + " " : "") + subElementID + " = new " + element.getNodeName() + genericArg + "();");
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
  protected final void createElementInstructions(Element element, String instanceNameToUse)
  {
    //Attribute map.
    NamedNodeMap attributes = element.getAttributes();
    
    //Create attribute instructions.
    for(int i = 0; i < attributes.getLength(); i++)
    {
      //Get the next attribute.
      Attr item = (Attr) attributes.item(i);
      
      //Add instructions for the attribute.
      handleAttribute(item, instanceNameToUse);
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
   * <p>Handle all attribute types in the base FXML element class.  Attributes can be present in different elements so it makes some sense
   * to have all attributes handled centrally to avoid duplicating code in the subclasses of this class.  Also, attributes that do not
   * relate to a given class can be present when a static method needs to be called using an instance as an argument.  For example, any
   * container FX object can be used as an argument in GridPane.setVgrow(...).  In order to keep things simple, all attributes will be
   * processed here in one place.
   *  
   * @param attribute
   * @param subElementID 
   */
  private void handleAttribute(Attr attribute, String subElementID)
  {
    //Get the attribute name.
    String qName = attribute.getName();
    
    // Ignore anything prefixed "fx" or "xml".
    if (qName.startsWith("fx") || qName.startsWith("xml")) return;

    //Get the attribute value.
    String attrVal = attribute.getValue();
    
    // Act only on the attribute's name - this declaration will specify the
    // datatype.
    switch (qName)
    {
      // Anything with a Pos parameter.
      case "alignment":
        // Check import list for Pos.
        checkImport("javafx.geometry.Pos");
        addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(Pos." + attrVal + ");");
        break;
      case "StackPane.alignment":
      case "BorderPane.alignment":
        //Make sure Pos class has been imported.
        checkImport("javafx.geometry.Pos");
        //Add the alignment
        //Split around '.'.
        String[] gpComps = qName.split("\\.");
        String gpIxInstruction = gpComps[0] + ".set" + Util.capitalise(gpComps[1]) + "(" + instanceName + ", " + "Pos." + attrVal + ");";
        addInstanceInstruction(gpIxInstruction);
        break;
      case "halignment":
      case "columnHalignment":
        // Check import list for HPos.
        checkImport("javafx.geometry.HPos");
        addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(HPos." + attrVal + ");");
        break;
      case "valignment":
        // Check import list for HPos.
        checkImport("javafx.geometry.VPos");
        addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(VPos." + attrVal + ");");
        break;
      case "orientation":
        // Check import list for HPos.
        checkImport("javafx.geometry.Orientation");
        addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(Orientation." + attrVal + ");");
        break;
      // Anything with 1 boolean parameter.
      case "mnemonicParsing":
      case "editable":
      case "resizable":
      case "sortable":
      case "pickOnBounds":
      case "preserveRatio":
      case "disable":
      case "selected":
      case "visible":
      case "fillHeight":
      case "fillWidth":
      case "closable":
        addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(" + attrVal + ");");
        break;
      // Anything with 1 double parameter.
      case "maxHeight":
      case "maxWidth":
      case "minHeight":
      case "minWidth":
      case "prefHeight":
      case "prefWidth":
      case "prefWrapLength":
      case "percentHeight":
      case "percentWidth":
      case "spacing":
      case "fitHeight":
      case "fitWidth":
      case "hgap":
      case "vgap":
      case "opacity":
      case "strokeWidth":
      case "wrappingWidth":
      case "fixedCellSize":
      case "size":
        switch(attrVal)
        {
          case "-Infinity":
            addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(Double.NEGATIVE_INFINITY);");
            break;
          case "Infinity":
            addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(Double.POSITIVE_INFINITY);");
            break;
          default:
            addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(" + attrVal + ");");
        }
        break;
      //Anything with 1 string parameter
      case "style":
      case "name":
        addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(\"" + attrVal + "\");");
        break;
      case "stylesheets":
        //Single style sheet.  Seems to be prefixed with @.
        if(attrVal.startsWith("@")) attrVal = attrVal.substring(1);
        //Set the property.
        addInstanceInstruction(subElementID + ".getStylesheets().add(Util.resolveRelativeResourceFilePath(getClass(), \"" + attrVal + "\"));");
        break;
      case "styleClass":
        addInstanceInstruction(subElementID + ".getStyleClass().add(\"" + attrVal + "\");");
        break;
      case "toggleGroup":
        //These seem to be prefixed with '$' for some reason.  Strip if so.
        if(attrVal.startsWith("$")) attrVal = attrVal.substring(1);
        //Add the toggle group reference.
        addInstanceInstruction(subElementID + ".setToggleGroup(" + attrVal + ");");
        break;
      // Anything with a single String parameter.
      case "text":
        // Is the string prefixed with a %? This is an i18n string.
        if (attrVal.startsWith("%"))
        {
          if(isField)
          {
            elementCreator.getDescriptor().setI18NTextCommandList.add(subElementID + ".set" + Util.capitalise(qName) + "(resourceBundle.getString(\"" + attrVal.substring(1) + "\"));");
          }
          else
          {
            addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(resourceBundle.getString(\"" + attrVal.substring(1) + "\"));");
            System.out.println(String.format(ResourceBundle.getBundle("fxml2java.element.strings").getString("textwarn"), subElementID));
          }
        }
        else
        {
          addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(\"" + attrVal + "\");");
        }
        break;
      case "strokeType":
        //Make sure we import the StrokeType enum.
        checkImport("javafx.scene.shape.StrokeType");
        //Add the attribute.
        addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(" + Util.capitalise(qName) + "." + attrVal + ");");        
        break;
      case "textAlignment":
        //Make sure we import the StrokeType enum.
        checkImport("javafx.scene.text.TextAlignment");
        //Add the attribute.
        addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(" + Util.capitalise(qName) + "." + attrVal + ");");        
        break;
      case "id":
        //Make sure any generic information has been stripped off.
        String[] rawID = attrVal.split("\\<");//[0];//Util.fromHTML(attrVal).split("\\<")[0];
        if(!rawID[0].isBlank()) addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(\"" + rawID + "\");");
        break;
      // ContentDisplay parameter.
      case "contentDisplay":
        // Check import list for ContentDisplay.
        checkImport("javafx.scene.control.ContentDisplay");
        addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(ContentDisplay." + attrVal + ");");
        break;
      // Grow parameters.
      case "hgrow":
      case "vgrow":
        // Check import list for Pos.
        checkImport("javafx.scene.layout.Priority");
        addInstanceInstruction(subElementID + ".set" + Util.capitalise(qName) + "(Priority." + attrVal + ");");
        break;
      case "VBox.vgrow":
      case "HBox.hgrow":
      case "GridPane.vgrow":
      case "GridPane.hgrow":
        //
        String staticClass = qName.substring(0, qName.indexOf('.')) + ".";
        addInstanceInstruction(
            staticClass + "set" + Util.capitalise(qName.substring(qName.indexOf('.') + 1)) + "(" + subElementID + ", Priority." + attrVal + ");");
        //Make sure ActionEvent class has been imported.
        checkImport("javafx.scene.layout.Priority");
        break;
      case "GridPane.rowIndex":
      case "GridPane.columnIndex":
        //Split around '.'.
        gpComps = qName.split("\\.");
        gpIxInstruction = gpComps[0] + ".set" + Util.capitalise(gpComps[1]) + "(" + subElementID + ", " + attrVal + ");";
        addInstanceInstruction(gpIxInstruction);
        break;
      case "GridPane.halignment":
      case "GridPane.valignment":
        //Attribute value type prefix.
        checkImport(("GridPane.halignment".equals(qName)) ? "javafx.geometry.HPos": "javafx.geometry.VPos");
        String alignmentAttrValPrefix = ("GridPane.halignment".equals(qName)) ? "HPos." : "VPos.";
        
        //Split around '.'.
        gpComps = qName.split("\\.");
        gpIxInstruction = gpComps[0] + ".set" + Util.capitalise(gpComps[1]) + "(" + subElementID + ", " + alignmentAttrValPrefix + attrVal + ");";
        addInstanceInstruction(gpIxInstruction);
        break;
      case "GridPane.rowSpan":
      case "GridPane.columnSpan":
        //Attribute value type prefix.
        
        //Split around '.'.
        gpComps = qName.split("\\.");
        gpIxInstruction = gpComps[0] + ".set" + Util.capitalise(gpComps[1]) + "(" + subElementID + ", " + attrVal + ");";
        addInstanceInstruction(gpIxInstruction);
        break;
      // TAB PANE.
      case "tabClosingPolicy":
        //Make sure the import is present.
        checkImport("javafx.scene.control.TabPane.TabClosingPolicy");
        //Add the instruction.
        addInstanceInstruction(subElementID + ".setTabClosingPolicy(TabClosingPolicy." + attrVal + ");");
        break;
      case "side":
        //Make sure the import is present.
        checkImport("javafx.geometry.Side");
        //Add the instruction.
        addInstanceInstruction(subElementID + ".setSide(Side." + attrVal + ");");        
        break;
        // ACTION HANDLERS.
      case "onAction":
        elementCreator.getDescriptor().handlerList.add(new Handler(qName, attrVal.substring(1)));  //Remove leading #.
        //Create a lambda to call the method.
        addInstanceInstruction(subElementID + ".setOnAction((e) -> {" + attrVal.substring(1) + "(e);});");
        checkImport("javafx.event.ActionEvent");
        break;
      case "url":
        //Don't do anything.  This needs to be dealt with in all cases by the element handler.
        break;
      default:
        throw new IllegalStateException("Unknown attribute: " + qName + " - can't process.");
    }
  }

  /**
   * <p>Process general elements here.
   * 
   * @param element
   */
  protected void handleElement(Element element)
  {
    //Get the sub-element name.
    String subElementName = element.getNodeName();
    
    //Handle sub elements depending on their name.
    switch(subElementName)
    {
      //Static insets.
      case "VBox.margin":
      case "HBox.margin":
      case "GridPane.margin":
        //Get type - subElementName upto '.'
        String typ = subElementName.substring(0, subElementName.indexOf("."));
        
        //Get the <Insets>
        Element insetsEl = (Element) element.getElementsByTagName("Insets").item(0);
        
        //handleStaticInsets(instanceName, typ, el);
        addInstanceInstruction(typ + ".setMargin(" + instanceName + ", " + getInsetsCreateInstruction(insetsEl) + ");");
        break;
      case "image":
        //Get the <Image> subelement.
        Element imageEl = (Element) element.getElementsByTagName("Image").item(0);
        
        //Get the 'url' attribute from the element.
        String urlString = imageEl.getAttribute("url");
        //Strip off leading '@'
        if(urlString.startsWith("@")) urlString = urlString.substring(1);
        
        //Check to see if the path is absolute.  If relative then inject the relative path calculator.
        if(!urlString.startsWith(File.pathSeparator))
        {
          //Find the resource in the classpath.
          urlString = Util.findRelativeResourceLocation(elementCreator.getDescriptor().sourceLocation, urlString);
        }
        
        //Constructor argument.
        String cArg = "new Image(getClass().getResourceAsStream(\"" + urlString + "\"))";

        //Add to the container.
        //Add declaration.
        addInstanceInstruction(instanceName + ".setImage(" + cArg + ");");
        
        break;
      case "opaqueInsets":
        //Get the insets creation subcommand.
        String opaqueInsets = getInsetsCreateInstruction((Element) element.getElementsByTagName("Insets").item(0));
        //Add the set insets command.
        addInstanceInstruction(instanceName + ".setOpaqueInsets(" + opaqueInsets + ");");
        break;
      case "font":
        //Contains a <Font> element.
        Element fontEl = Util.getFirstXMLElement(element);
        //Font instance name.
        String fontName;
        String fontSize;
        //Get the font parameters.  These are not properties; each Font instance is immutable and is created with the constructor only.
        if(!"".equals(fontName = fontEl.getAttribute("name")) && !"".equals(fontSize = fontEl.getAttribute("size")))
        {
          //Create the Font instance and set the containing instance's setFont(...) method.
          //createElementInstructions(fontEl, fontInst = createInstanceName(fontEl));
          //Add the prefix instruction.
          addInstanceInstruction(instanceName + ".setFont(new Font(\"" + fontName + "\", " + fontSize + "));");
        }
        else
        {
          //Currently we can only handle a Font declaration with a name and size attribute.
          throw new IllegalStateException("Don't know how to process font with the given attributes.  An attribute handler is required.");
        }
        break;
      case "toggleGroup":
        //Handle the toggle group declaration.
        //The first child element is <ToggleGroup>, which contains the group name as an fx:id attribute.
        String groupString = ( (Element) element.getElementsByTagName("ToggleGroup").item(0)).getAttribute("fx:id");
        //Toggle groups are always fields because they have an fx:id and there's no way to specify if they should be fields or not
        //in SceneBuilder.
        elementCreator.getDescriptor().addField("ToggleGroup " + groupString);
        //Create the group.
        addInstanceInstruction(groupString + " = new ToggleGroup();");
        //Check the import for ToggleGroup exists.
        elementCreator.getDescriptor().addImport("javafx.scene.control.ToggleGroup");
        //Set the toggle group property of the owning instance.
        addInstanceInstruction(instanceName + ".setToggleGroup(" + groupString + ");");
        break;
      default:
        throw new IllegalStateException("Don't know how to process child node of type " + subElementName);
    }
  }
  
  /**
   * <p>Handle a set insets declaration for a node instance.  Like [inst of Button].setPadding(...
   * 
   * @param parentTagName
   * @param insetsParentDecl
   * @param insetsElement
   */
  protected String getInsetsCreateInstruction(Element insetsEl)
  {
    String val;
    String topPar = (val = insetsEl.getAttribute("top")).equals("") ? "0.0" : val;
    String bottomPar = (val = insetsEl.getAttribute("bottom")).equals("") ? "0.0" : val;
    String leftPar = (val = insetsEl.getAttribute("left")).equals("") ? "0.0" : val;
    String rightPar = (val = insetsEl.getAttribute("right")).equals("") ? "0.0" : val;
    return "new Insets(Double.parseDouble(\"" + topPar
        + "\"), Double.parseDouble(\"" + rightPar + "\"), Double.parseDouble(\"" + bottomPar
        + "\"), Double.parseDouble(\"" + leftPar + "\"))";
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
}
