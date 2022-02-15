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
package fxml2java.util;

import java.io.File;
import java.net.URL;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * <p>This class contains simple utility methods for assisting in converting FXML text files to plain Java components.
 * 
 * @author jdf19
 *
 */
public class Util
{
  /**
   * <p>Capitalise the first letter of the string.  A lower case string can be prepended to create a camel case identifier (for example).
   * 
   * @param qName
   * @return
   */
  public static String capitalise(String qName)
  {
    String capStr = qName.substring(0, 1).toUpperCase() + qName.substring(1);
    return capStr;
  }

  /**
   * <p>Build the generic diamond identifier with the comma-separated generic arguments inside. 
   * 
   * @param generics
   * @return
   */
  public static String genericsString(String[] generics)
  {
    if(generics.length == 0) return "";
    
    StringBuilder sb = new StringBuilder();
    sb.append('<');
    sb.append(generics[0]);
    for(int i = 1; i < generics.length; i++)
    {
      sb.append(',');
      sb.append(generics[i]);
    }
    sb.append('>');
    return sb.toString();
  }

  /**
   * <p>Return true if the given generic class array contains any generic arguments (i.e. has length > 0).
   * 
   * @param generics string array containing generic classes.
   * 
   * @return true if the array length is > 0.
   */
  public static String genericsDiamond(String[] generics)
  {
    return (generics.length > 0) ? "<>" : "";
  }
  
  /**
   * <p>Use the absolute path of the source fxml file and the relative file path to the resource using the source
   * filename to determine the package where the generated class will reside.  The <b>relative</b> path to the resource is from the
   * <b>generated file location</b>.
   * 
   * @param sourceFXMLFilePath the source FXML file.  The filename <b>MUST BE</b> the fully-qualified class name of the generated class. 
   * @param relativePathToResource the relative path to the resource from the <b>generated</b> FXML>Java file.
   * @return the relative path calculated.
   */
  public static String findRelativeResourceLocation(String sourceFXMLFilePath, String relativePathToResource)
  {
    //Replace any backslash separators with fwdslash.
    sourceFXMLFilePath = sourceFXMLFilePath.replaceAll("\\\\", "/");
    
    //Get the filename.
    File f = new File(sourceFXMLFilePath);
    
    //Get the dest file name.
    String destName = f.getName();
    //Split around ".".  We are interested in everything before the last 2 entries: filename and ".fxml"
    String[] destPathCalc = destName.split("\\.");
    //First entry.
    File srcParent = f.getParentFile();
    boolean found = false;
    while(!found)
    {
      if(destPathCalc[0].equalsIgnoreCase(srcParent.getName()))
      {
        found = true;
      }
      srcParent = srcParent.getParentFile();
    }
    
    //Got the source parent absolute file.
    //
    //Get the path for the given source
    //Replace any backslash separators with fwdslash.
    relativePathToResource = relativePathToResource.replaceAll("\\\\", "/");
    File rscFile = f.getParentFile();
    
    destPathCalc = relativePathToResource.split("/");
    
    for(String s : destPathCalc)
    {
      if("..".equals(s))
      {
        rscFile = rscFile.getParentFile();
      }
      else
      {
        rscFile = new File(rscFile, s);
      }
    }
    
    //Finally - we've got the source directory.  Strip that off the front.
    String dstRsc = rscFile.toString().substring(srcParent.toString().length());

    //
    return dstRsc.replaceAll("\\\\", "/");
  }

  /**
   * 
   * @param u
   * @param relativePath
   * @return
   */
  public static String resolveRelativeResourceFilePath(Class<?> u, String relativePath)
  {
    //Get class name.
    String clsName = u.getSimpleName() + ".class";
    
    //Get the resource location for the class.
    URL resLoc = u.getResource(clsName);
    
    //If the protocol indicates that the codebase is in a file or jar file, resolve the relative path.
    if("file".equals(resLoc.getProtocol()) || "jar".equals(resLoc.getProtocol()))
    {
      //This is a file.  Start by getting the URL file string and converting any \ chars to /
      String sourceFile = resLoc.toString().replaceAll("\\\\", "/");
      
      //The URL points to the location of the codebase of the class specified.  The classname.class must therefore
      //be present at the end of the file name and so we will strip off text from the last / char inclusive.
      sourceFile = sourceFile.substring(0, sourceFile.lastIndexOf('/'));
      
      //We will go through the relative path string, winding back the source file directories while we have the '..' operator (move back one directory).
      //Once we run out of backward operators, we can append the rest of the relative path information to the rewound source file path.
      //LinkedList<String> srcFileComponents = new LinkedList<>(Arrays.asList(sourceFile.split("/")));
      String[] relPathComponents = relativePath.replaceAll("\\\\", "/").split("/");
      
      for(int i = 0; i < relPathComponents.length; i++)
      {
        //Get the next path component.
        String pathComponent = relPathComponents[i];
        
        //Wind back or not?
        if("..".equals(pathComponent))
        {
          //Wind back.
          sourceFile = sourceFile.substring(0, sourceFile.lastIndexOf('/'));
        }
        else
        {
          //Add component.
          sourceFile += "/" + pathComponent;
        }
      }
      
      //OK - finished relativising path.  Rebuild and return.
      return sourceFile;
    }
    else
    {
      return u.toString();
    }
  }
  
  /**
   * <p>Simple utility method to get the first XML sub element of the given element.  Returns null if none available.
   *  
   * @param element
   * @return
   */
  public static Element getFirstXMLElement(Element element)
  {
    //Get element child nodes.
    NodeList childNodes = element.getChildNodes();
    
    //Cycle through until first element is found.
    for(int i = 0; i < childNodes.getLength(); i++)
    {
      Node node = childNodes.item(i);
      if(node.getNodeType() == Node.ELEMENT_NODE) return (Element) node;
    }
    
    //Return null by default - no element found.
    return null;
  }

  public static Iterator<Element> getSubElementIterator(Element element)
  {
    return new SubElementIterator(element);
  }
  
  private static class SubElementIterator implements Iterator<Element>
  {
    /*
     * The parent element.
     */
    final Element parent;
    
    /*
     * Index of next sub element in the parent element.
     */
    int nextIX = 0;
    
    private SubElementIterator(Element el)
    {
      //Store parent ref.
      this.parent = el;

      //Find the first element if it exists.
      findNext();
    }

    private final void findNext()
    {
      //Find the next element if it exists.
      NodeList childNodes = parent.getChildNodes();
      for(int i = ++nextIX; i < childNodes.getLength(); i++)
      {
        //Any Element node.
        if(childNodes.item(i).getNodeType() == Node.ELEMENT_NODE) 
        {
          //Update next index.
          nextIX = i;
          
          //Complete - return.
          return;
        }
      }
      
      //Nothing found.  Set next ix to -1.
      nextIX = -1;
    }
    
    @Override
    public boolean hasNext()
    {
      // nextIX is not -1.
      return nextIX != -1;
    }

    @Override
    public Element next()
    {
      //Next element exists?
      if(nextIX == -1) throw new NoSuchElementException();
      
      //Store the index to return.
      int retIX = nextIX;
      
      //Find the next element if it exists.
      findNext();
      
      // Return the element at the return index.
      return (Element) parent.getChildNodes().item(retIX);
    }
    
  }
}
