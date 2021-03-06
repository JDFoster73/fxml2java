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
    //REPLACE THIS WITH THE LOCATION OF THE BASIS DIRECTORY ON YOUR LOCAL INSTALLATION
    f2j.convertDirectory("D:\\Code\\Tools\\fxml2java\\fxml2java\\example\\basis", "D:\\Code\\Tools\\fxml2java");
  }
  
}
