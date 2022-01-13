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
