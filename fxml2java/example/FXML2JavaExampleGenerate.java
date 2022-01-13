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
package fxml2java.example;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import fxml2java.FXML2Java;

/**
 * <p>Run the main(...) method of this class to generate Java files from the FXML example files provided in the fxml2java.example.basis package.
 * 
 * @author James David Foster
 *
 */
public class FXML2JavaExampleGenerate
{
  
  public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException
  {
    //Create converter instance.
    FXML2Java f2j = new FXML2Java();
    
    //Convert the basis directory.
    f2j.convertDirectory("D:\\Repository\\Tools\\FXML2Java\\src\\fxml2java\\example\\basis", "D:\\Repository\\Tools\\FXML2Java\\src\\");
  }
  
}
