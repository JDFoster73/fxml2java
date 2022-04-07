package fxml2java.element;

import java.util.ResourceBundle;

import fxml2java.Handler;
import fxml2java.util.Util;

public class PropertyHandler
{
  
  /*
   * StringBuilder instance for building property setting instruction.
   */
  private final StringBuilder instructionBuilder = new StringBuilder();
  
  /**
   * <p>
   * Create an instruction in the parent instance to manage the setting of the
   * property value in that instance.
   * 
   * @param propName
   * @param propValue
   * @param parentInstance
   */
  public void handleProperty(String propName, String propValue, FXMLElement parentInstance)
  {
    //Ensure import type for property parameter.
    ensurePropertyParamImport(propName, parentInstance);
    
    // Clear the instruction builder.
    instructionBuilder.setLength(0);
    
    //Don't process the following properties!  They are set in the constructor.
    //Image
    //Font
    if("Image".equals(parentInstance.instanceDataType) || "Font".equals(parentInstance.instanceDataType)) return;
    
    // Get the setter method prefix.
    getSetterMethod(propName, propValue, parentInstance, instructionBuilder);
    
    // Instruction built. Add to instance's instruction list.
    parentInstance.addInstanceInstruction(instructionBuilder.toString());
  }
  
  private void getSetterMethod(String propName, String propVal, FXMLElement parentInstance, StringBuilder instBuilder)
  {
    // PROPERTY NAME.
    // Handle instance-based and static properties differently.
    if (propName.contains("."))
    {
      // STATIC PROPERTIES ALWAYS USE THE .set(...) METHOD.
      
      // Split around '.'
      String[] gpComps = propName.split("\\.");
      
      // static attribute.
      instBuilder.append(gpComps[0]); // Class name to invoke static method of.
      instBuilder.append(".set"); // .set static text. Static methods are ALWAYS a setProperty(...) method.
      instBuilder.append(Util.capitalise(gpComps[1])); // Static method of this class name.
      
      // Start the function call.
      instBuilder.append("(");
      // Append the instance name.
      instBuilder.append(parentInstance.instanceName);
      // Append ', '
      instBuilder.append(", ");
      instBuilder.append(getPropertyValue(propName, propVal, parentInstance));
      // Add value and complete
      instBuilder.append(");");
    }
    else
    {
      //Check for event handler.  These properties start with "on...".
      if(propName.startsWith("on"))
      {
        parentInstance.addHandlerMethod(new Handler(propName, propVal.substring(1)));//Remove leading #.
        
        //Remove the leading # from the property value.
        propVal = propVal.substring(1);
        
        //
        instBuilder.append(parentInstance.instanceName);
        instBuilder.append(".set");
        instBuilder.append(Util.capitalise(propName));
        instBuilder.append("((e) -> {" + propVal + "(e);});");
        
        //Ensure the event class
        switch(propName)
        {
          //EVENT HANDLERS - ENSURE IMPORT OF EVENT CLASS
          case "onAction":
            parentInstance.checkImport("javafx.event.ActionEvent");
            break;
        }
      }
      else
      {
        // The setter.
        String setter = "";
        
        // INSTANCE PROPERTES MOSTLY USE THE .setXXX(...) method apart from a few
        // exceptions such as stylesheets.
        // Some attributes require special handling and import of related classes.
        switch (propName)
        {
          case "text":
            // Special support for "text" property. If the element is an i18n string
            // (prefixed with %) then the text is set by creating an
            // instruction in the setTextStrings(...) method.
            if (propVal.startsWith("%"))
            {
              // Adjust prop val - remove the leading % symbol.
              propVal = propVal.substring(1);
              
              if (parentInstance.isField)
              {
                parentInstance.addI18NInstruction(parentInstance.instanceName + ".setText(resourceBundle.getString(\"" + propVal + "\"));");
              }
              else
              {
                setter = "set" + Util.capitalise(propName);
                System.out.println(String.format(ResourceBundle.getBundle("fxml2java.element.strings").getString("textwarn"), parentInstance.instanceName));
              }
            }
            else
            {
              // static text.
              setter = "set" + Util.capitalise(propName);
            }
            break;
          case "stylesheets":
            setter = "getStylesheets().add";
            parentInstance.checkImport("fxml2java.util.Util");
            break;
          case "styleClass":
            setter = "getStyleClass().add";
            break;
          default:
            setter = "set" + Util.capitalise(propName);
        }
        
        // Don't add prop for a blank setter.
        if (!setter.isBlank())
        {
          instBuilder.append(parentInstance.instanceName);
          instBuilder.append('.');
          instBuilder.append(setter);
          instBuilder.append("(");
          instBuilder.append(getPropertyValue(propName, propVal, parentInstance));
          instBuilder.append(");");
        }
      }
    }
  }
  
  public void ensurePropertyParamImport(String propName, FXMLElement parentElement)
  {
    switch (propName)
    {
      // Properties of enum type javafx.geometry.Pos:
      case "alignment":
      case "StackPane.alignment":
      case "BorderPane.alignment":
        parentElement.checkImport("javafx.geometry.Pos");
        break;
      // Properties of enum type javafx.geometry.HPos:
      case "halignment":
      case "GridPane.halignment":
      case "columnHalignment":
        parentElement.checkImport("javafx.geometry.HPos");
        break;
      // Properties of enum type javafx.geometry.VPos:
      case "valignment":
      case "GridPane.valignment":
        parentElement.checkImport("javafx.geometry.VPos");
        break;
      // Properties of enum type javafx.geometry.Orientation:
      case "orientation":
        parentElement.checkImport("javafx.geometry.Orientation");
        break;
      // Properties of enum type javafx.geometry.Side:
      case "side":
        parentElement.checkImport("javafx.geometry.Side");
        break;
      // Properties of enum type javafx.scene.shape.StrokeType:
      case "strokeType":
        parentElement.checkImport("javafx.scene.shape.StrokeType");
        break;
      // Properties of enum type javafx.scene.text.TextAlignment:
      case "textAlignment":
        parentElement.checkImport("javafx.scene.text.TextAlignment");
        break;
      // Properties of enum type javafx.scene.control.ContentDisplay:
      case "contentDisplay":
        parentElement.checkImport("javafx.scene.control.ContentDisplay");
        break;
      // Properties of enum type javafx.scene.layout.Priority:
      case "vgrow":
      case "hgrow":
      case "VBox.vgrow":
      case "HBox.hgrow":
      case "GridPane.vgrow":
      case "GridPane.hgrow":
        parentElement.checkImport("javafx.scene.layout.Priority");
        break;
      // Properties of enum type javafx.scene.control.TabPane.TabClosingPolicy:
      case "tabClosingPolicy":
        parentElement.checkImport("javafx.scene.control.TabPane.TabClosingPolicy");
        break;
      // Color parameter.
      case "textFill":
        parentElement.checkImport("javafx.scene.paint.Color");
        break;
    }    
  }
  
  /**
   * <p>Get the property value given the raw value string and the property type.  Some properties need formatting, for example
   * a text property needs wrapping in quotes, and an enum property needs to have the enum type prefix.
   * 
   * @param propName
   * @param propVal
   * @param parentElement
   * @return
   */
  public String getPropertyValue(String propName, String propVal, FXMLElement parentElement)
  {
    switch (propName)
    {
      // Properties of enum type javafx.geometry.Pos:
      case "alignment":
      case "StackPane.alignment":
      case "BorderPane.alignment":
        parentElement.checkImport("javafx.geometry.Pos");
        return "Pos." + propVal;
      // Properties of enum type javafx.geometry.HPos:
      case "halignment":
      case "GridPane.halignment":
      case "columnHalignment":
        parentElement.checkImport("javafx.geometry.HPos");
        return "HPos." + propVal;
      // Properties of enum type javafx.geometry.VPos:
      case "valignment":
      case "GridPane.valignment":
        parentElement.checkImport("javafx.geometry.VPos");
        return "VPos." + propVal;
      // Properties of enum type javafx.geometry.Orientation:
      case "orientation":
        parentElement.checkImport("javafx.geometry.Orientation");
        return "Orientation." + propVal;
      // Properties of enum type javafx.geometry.Side:
      case "side":
        parentElement.checkImport("javafx.geometry.Side");
        return "Side." + propVal;
      // Properties of enum type javafx.scene.shape.StrokeType:
      case "strokeType":
        parentElement.checkImport("javafx.scene.shape.StrokeType");
        return "StrokeType." + propVal;
      // Properties of enum type javafx.scene.text.TextAlignment:
      case "textAlignment":
        parentElement.checkImport("javafx.scene.text.TextAlignment");
        return "TextAlignment." + propVal;
      // Properties of enum type javafx.scene.control.ContentDisplay:
      case "contentDisplay":
        parentElement.checkImport("javafx.scene.control.ContentDisplay");
        return "ContentDisplay." + propVal;
      // Properties of enum type javafx.scene.layout.Priority:
      case "vgrow":
      case "hgrow":
      case "VBox.vgrow":
      case "HBox.hgrow":
      case "GridPane.vgrow":
      case "GridPane.hgrow":
        parentElement.checkImport("javafx.scene.layout.Priority");
        return "Priority." + propVal;
      // Properties of enum type javafx.scene.control.TabPane.TabClosingPolicy:
      case "tabClosingPolicy":
        parentElement.checkImport("javafx.scene.control.TabPane.TabClosingPolicy");
        return "TabClosingPolicy." + propVal;
      // Color parameter.
      case "textFill":
        parentElement.checkImport("javafx.scene.paint.Color");
        return "Color.web(\"" + propVal + "\")";
      //Boolean parameter.
      case "mnemonicParsing":
      case "selected":
      case "pickOnBounds":
      case "preserveRatio":
      case "resizable":
      case "editable":
      case "fillWidth":
      case "fillHeight":
      case "disable":
      case "visible":
      case "closable":
      case "sortable":
      //Integer parameter.
      case "GridPane.columnIndex":
      case "GridPane.rowIndex":
      case "GridPane.columnSpan":
      case "GridPane.rowSpan":
        return propVal;
      // Double parameter.
      //{AnchorPane constraints}.
      case "AnchorPane.topAnchor":  
      case "AnchorPane.leftAnchor":  
      case "AnchorPane.rightAnchor":  
      case "AnchorPane.bottomAnchor":  
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
      case "top":
      case "right":
      case "bottom":
      case "left":
      case "layoutX":
      case "layoutY":
      case "_double": //Dummy property name.
        switch (propVal)
        {
          case "-Infinity":
            propVal = "Double.NEGATIVE_INFINITY";
            break;
          case "Infinity":
            propVal = "Double.NEGATIVE_INFINITY";
            break;
        }
        return propVal;
      // String parameter.
      case "id":
      case "style":
      case "name":
      case "styleClass":
      case "text":
        return "\"" + propVal + "\"";
      case "stylesheets":
        //Strip leading @
        if(propVal.startsWith("@")) propVal = propVal.substring(1);
        return "Util.resolveRelativeResourceFilePath(getClass(), \"" + propVal + "\")";
      case "toggleGroup":
        //Make sure leading $ is removed.
        if(propVal.startsWith("$")) propVal = propVal.substring(1);
        return propVal;
      default:
        //return propVal;
        throw new IllegalArgumentException("Don't know how to process property value for property " + propName);
    }
    
  }  
}
