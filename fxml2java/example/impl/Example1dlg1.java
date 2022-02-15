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
  
  public Example1dlg1(ResourceBundle resourceBundle)
  {
    this.resourceBundle = resourceBundle;
    setupFX();
    setTextStrings();
    userConfig();
  }
  
  private void setupFX()
  {
    Insets insetsLINST0001 = new Insets(0.0, 10.0, 0.0, 0.0);
    
    chbChoices = new ChoiceBox<>();
    chbChoices.setPrefHeight(25.0);
    chbChoices.setPrefWidth(169.0);
    
    chkCheck = new CheckBox();
    VBox.setVgrow(chkCheck, Priority.NEVER);
    chkCheck.setMnemonicParsing(false);
    
    VBox vboxLINST0002 = new VBox();
    VBox.setVgrow(vboxLINST0002, Priority.NEVER);
    vboxLINST0002.setFillWidth(false);
    vboxLINST0002.setMaxWidth(1000.0);
    vboxLINST0002.setSpacing(10.0);
    vboxLINST0002.getChildren().addAll(chbChoices, chkCheck);
    
    vboxLINST0000 = new VBox();
    vboxLINST0000.setAlignment(Pos.CENTER);
    vboxLINST0000.setMaxHeight(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setMaxWidth(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setMinHeight(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setMinWidth(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setPadding(insetsLINST0001);
    vboxLINST0000.getChildren().addAll(vboxLINST0002);
    
  }
  
  private void setTextStrings()
  {
    chkCheck.setText(resourceBundle.getString("dlg1.chk.lbl"));
  }
  
  public VBox getMainContainer()
  {
    return vboxLINST0000;
  }
  
  public void resourceUpdate(ResourceBundle resourceBundle)
  {
    this.resourceBundle = resourceBundle;
    setTextStrings();
  }
  
  private void userConfig()
  {
    // Set up the selections in the choice box.
    chbChoices.getItems().addAll(resourceBundle.getString("dlg1.chb.ch1"), resourceBundle.getString("dlg1.chb.ch2"), resourceBundle.getString("dlg1.chb.ch3"));
  }
  
  /**
   * <p>
   * Reset the form.
   */
  public void resetForm()
  {
    chkCheck.selectedProperty().set(false);
    chbChoices.selectionModelProperty().get().clearSelection();
  }
  
  /**
   * <p>
   * Return the dialogue state information.
   *
   * @return
   */
  public Example1Dialogue1Item dialogueState()
  {
    return new Example1Dialogue1Item(chkCheck.selectedProperty().get(), chbChoices.selectionModelProperty().get().getSelectedItem());
  }
  
}
