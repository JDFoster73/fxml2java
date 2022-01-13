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

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ProcessingInstruction;
import org.xml.sax.SAXException;

import fxml2java.IDManager;

/**
 * <p>Resolve the given FXML source file into a java descriptor which will be used to build the Java source code for the given FXML input.
 * 
 * @author James David Foster
 *
 */
public class FXMLDOMResolver
{
    
  /**
   * <p>The source xml file location.
   */
  private final String sourceFile;
    
  /**
   * <p>The java descriptor used to build a Java source file.
   */
  private final FXMLJavaDescriptorImpl jdescriptor;
  
  /**
   * <p>Resolve the specified file into a java descriptor.
   * 
   * @param sourceFile
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws SAXException
   */
  public FXMLDOMResolver(String sourceFile) throws IOException, ParserConfigurationException, SAXException
  {
    //Save the xml source file location.
    this.sourceFile = sourceFile;
    this.jdescriptor = new FXMLJavaDescriptorImpl(sourceFile);
    doParsing();
  }
  
  /**
   * <p>Get the java file descriptor for the given FXML source file.
   * 
   * @return java descriptor.
   */
  public FXMLJavaDescriptor getDescriptor()
  {
    return jdescriptor;
  }

  private void doParsing() throws ParserConfigurationException, SAXException, IOException
  {
    //Do the resolving.
    InputStream resourceAsStream = new BufferedInputStream(new FileInputStream(sourceFile));

    // DOM parser.
    DocumentBuilder docBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
    Document fxmlDocument = docBuilder.parse(resourceAsStream);
    
    //Import statements in the FXML file - include these as imports in the generated Java source code.
    NodeList childNodes = fxmlDocument.getChildNodes();
    for(int i = 0; i < childNodes.getLength(); i++)
    {
      Node item = childNodes.item(i);
      if(item.getNodeType() == Node.PROCESSING_INSTRUCTION_NODE)
      {
        ProcessingInstruction pi = (ProcessingInstruction) item;
        //Check for import.
        if(pi.getTarget().equals("import")) jdescriptor.addImport(pi.getData());
      }
    }
    
    //Get the root document element - is the form's main container.
    Element element = fxmlDocument.getDocumentElement();
    
    //Determine if the root element has an fx:id.  If not then we still need a reference to it as a field because it is
    //needed to return to the owner.
    //To simplify the element structure, it is a rule that only the root element can be ID 0.  If root already has an fx:id then
    //make sure the id manager starts at 1.
    //ID manager.
    IDManager idm = element.hasAttribute("fx:id") ? new IDManager(1) : new IDManager(0);
    
    //Element creator.
    ElementCreator ec = new ElementCreator(idm, jdescriptor);
    
    //
    FXMLElement root = ec.createElement(element);

    //Set the root node in the descriptor.
    jdescriptor.setRootElement(root);
  }
}
