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
import java.util.ResourceBundle;

import fxml2java.example.impl.Example1;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * <p>Run the example.
 * 
 * @author James David Foster
 *
 */
public class FXML2JavaExampleRun extends Application
{
  
  public static void main(String[] args)
  {
    launch(args);
  }
  
  @Override
  public void start(Stage primaryStage) throws IOException
  {
    ResourceBundle rb = ResourceBundle.getBundle("fxml2java.example.strings");
    
    Example1 e1 = new Example1(rb);
    
    //Create the scene.
    Scene scene = new Scene(e1.getMainContainer());
    
    //Set the stage scene.
    primaryStage.setScene(scene);
    
    //Show the stage.
    primaryStage.show();
  }
  
}
