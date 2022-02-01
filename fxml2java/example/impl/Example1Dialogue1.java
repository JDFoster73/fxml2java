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
package fxml2java.example.impl;

import java.util.ResourceBundle;

import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.stage.Modality;
import javafx.util.Callback;

public class Example1Dialogue1 extends Dialog<Example1Dialogue1Item>
{  
  /**
   * <p>The editor panel - main container.
   */
  private final Example1dlg1 editorPanel;
  
  public Example1Dialogue1(ResourceBundle bundle)
  {
    //Initialise application modal.
    initModality(Modality.APPLICATION_MODAL);
    
    //Set up the buttons.
    getDialogPane().getButtonTypes().add(ButtonType.OK);
    getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

    //Get the OK button.  Can disable by default and only enable in response to a validity property in the editor panel (for example) if required. 
    //final Button okButton = (Button) getDialogPane().lookupButton(ButtonType.OK);

    //Create the dialogue panel main container.
    editorPanel = new Example1dlg1(bundle);
    
    //Set the content of the dialogue.
    getDialogPane().setContent(this.editorPanel.getMainContainer());
    
    //Set up result converter.
    setResultConverter(new Callback<ButtonType, Example1Dialogue1Item>()
    {
      
      @Override
      public Example1Dialogue1Item call(ButtonType param)
      {
        return (param == ButtonType.OK) ? editorPanel.dialogueState() : null;
      }
    });
  }

  /**
   * <p>Set the dialogue up new operation.
   */
  public void resetForm()
  {
    //Clear text fields.
    editorPanel.resetForm();
  }

}
