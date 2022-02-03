package fxml2java.example.impl;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import java.util.ResourceBundle;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.ChoiceBox;

public class Example1dlg1
{
  @FXML
  private VBox vboxLINST0000;
  @FXML
  private ChoiceBox<String> chbChoices;
  @FXML
  private CheckBox chkCheck;
  @FXML
  private ResourceBundle resourceBundle;


  public Example1dlg1(ResourceBundle resourceBundle) {
    this.resourceBundle = resourceBundle;
    setupFX();
    setTextStrings();
    userConfig();
    }


  private void setupFX()
  {
    chbChoices = new ChoiceBox<>();
    chbChoices.setPrefHeight(25.0);
    chbChoices.setPrefWidth(169.0);
    
    chkCheck = new CheckBox();
    VBox.setVgrow(chkCheck, Priority.NEVER);
    chkCheck.setMnemonicParsing(false);
    
    VBox vboxLINST0001 = new VBox();
    VBox.setVgrow(vboxLINST0001, Priority.NEVER);
    vboxLINST0001.setFillWidth(false);
    vboxLINST0001.setMaxWidth(1000.0);
    vboxLINST0001.setSpacing(10.0);
    vboxLINST0001.getChildren().addAll(chbChoices, chkCheck);
    
    vboxLINST0000 = new VBox();
    vboxLINST0000.setAlignment(Pos.CENTER);
    vboxLINST0000.setMaxHeight(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setMaxWidth(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setMinHeight(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setMinWidth(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setPadding(new Insets(Double.parseDouble("0.0"), Double.parseDouble("10.0"), Double.parseDouble("0.0"), Double.parseDouble("0.0")));
    vboxLINST0000.getChildren().addAll(vboxLINST0001);
    
  }

  private void setTextStrings()
  {
    chkCheck.setText(resourceBundle.getString("dlg1.chk.lbl"));
  }

  public VBox getMainContainer()
  {
    return vboxLINST0000;
  }

  public void resourceUpdate(ResourceBundle resourceBundle) {
    this.resourceBundle = resourceBundle;
    setTextStrings();
    }


  private void userConfig() {
    // Set up the selections in the choice box.
    chbChoices.getItems().addAll(resourceBundle.getString("dlg1.chb.ch1"), resourceBundle.getString("dlg1.chb.ch2"), resourceBundle.getString("dlg1.chb.ch3"));
    }


  /**
  * <p>
  * Reset the form.
  */
  public void resetForm() {
    chkCheck.selectedProperty().set(false);
    chbChoices.selectionModelProperty().get().clearSelection();
    }


  /**
  * <p>
  * Return the dialogue state information.
  *
  * @return
  */
  public Example1Dialogue1Item dialogueState() {
    return new Example1Dialogue1Item(chkCheck.selectedProperty().get(), chbChoices.selectionModelProperty().get().getSelectedItem());
    }



}

