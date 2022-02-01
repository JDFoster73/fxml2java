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

import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.scene.layout.ColumnConstraints;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import fxml2java.util.Util;
import javafx.scene.control.TableColumn;
import javafx.geometry.Insets;
import javafx.scene.control.Alert.AlertType;

import java.util.Optional;
import java.util.ResourceBundle;
import javafx.scene.shape.StrokeType;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import fxml2java.example.TableItem;
import javafx.scene.text.Font;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.text.Text;
import javafx.fxml.FXML;
import javafx.scene.layout.Priority;
import javafx.scene.control.ToggleGroup;
import javafx.event.ActionEvent;
import javafx.scene.control.RadioButton;
import javafx.scene.image.ImageView;
import javafx.beans.value.ObservableValue;
import javafx.scene.image.Image;

public class Example1
{
  @FXML
  private VBox vboxLINST0000;
  @FXML
  private Button btnLeftPanel1;
  @FXML
  private Button btnLeftPanel2;
  @FXML
  private Button btnLeftPanel3;
  @FXML
  private ToggleGroup tg1;
  @FXML
  private Button btnRHSAddTItem;
  @FXML
  private TableView<TableItem> tblItemTable;
  @FXML
  private TableColumn<TableItem, String> tclCol1;
  @FXML
  private TableColumn<TableItem, String> tclCol2;
  @FXML
  private TextField txtRHBTxt1;
  @FXML
  private TextField txtRHBTxt2;
  @FXML
  private TextField txtRHBTxt3;
  @FXML
  private Button btnShowDlg;
  @FXML
  private ResourceBundle resourceBundle;
  
  private int tableItemCount = 0;
  
  //Create dialogue box.
  private Example1Dialogue1 e1d1;

  public Example1(ResourceBundle resourceBundle)
  {
    this.resourceBundle = resourceBundle;
    setupFX();
    setTextStrings();
    userConfig();
  }
  
  private void setupFX()
  {
    Text textLINST0002 = new Text();
    textLINST0002.setStrokeType(StrokeType.OUTSIDE);
    textLINST0002.setStrokeWidth(0.0);
    textLINST0002.setText(resourceBundle.getString("title"));
    textLINST0002.setFont(new Font("Segoe UI Bold Italic", 19.0));
    
    HBox hboxLINST0001 = new HBox();
    VBox.setVgrow(hboxLINST0001, Priority.NEVER);
    hboxLINST0001.setAlignment(Pos.CENTER);
    hboxLINST0001.setPrefWidth(200.0);
    hboxLINST0001.getChildren().addAll(textLINST0002);
    hboxLINST0001.setPadding(new Insets(Double.parseDouble("10.0"), Double.parseDouble("10.0"), Double.parseDouble("10.0"), Double.parseDouble("10.0")));
    VBox.setMargin(hboxLINST0001, new Insets(Double.parseDouble("0.0"), Double.parseDouble("0.0"), Double.parseDouble("0.0"), Double.parseDouble("0.0")));
    
    btnLeftPanel1 = new Button();
    btnLeftPanel1.setMnemonicParsing(false);
    btnLeftPanel1.setOnAction((e) -> {
      onBtnLeft1(e);
    });
    btnLeftPanel1.setPrefWidth(10000.0);
    
    btnLeftPanel2 = new Button();
    btnLeftPanel2.setMnemonicParsing(false);
    btnLeftPanel2.setOnAction((e) -> {
      onBtnLeft2(e);
    });
    btnLeftPanel2.setPrefWidth(10000.0);
    
    btnLeftPanel3 = new Button();
    btnLeftPanel3.setMnemonicParsing(false);
    btnLeftPanel3.setOnAction((e) -> {
      onBtnLeft3(e);
    });
    btnLeftPanel3.setPrefWidth(10000.0);
    
    RadioButton radiobuttonLINST0006 = new RadioButton();
    radiobuttonLINST0006.setMnemonicParsing(false);
    radiobuttonLINST0006.setSelected(true);
    radiobuttonLINST0006.setText(resourceBundle.getString("radio1.txt"));
    tg1 = new ToggleGroup();
    radiobuttonLINST0006.setToggleGroup(tg1);
    
    RadioButton radiobuttonLINST0007 = new RadioButton();
    radiobuttonLINST0007.setMnemonicParsing(false);
    radiobuttonLINST0007.setText(resourceBundle.getString("radio2.txt"));
    radiobuttonLINST0007.setToggleGroup(tg1);
    
    RadioButton radiobuttonLINST0008 = new RadioButton();
    radiobuttonLINST0008.setMnemonicParsing(false);
    radiobuttonLINST0008.setText(resourceBundle.getString("radio3.txt"));
    radiobuttonLINST0008.setToggleGroup(tg1);
    
    VBox vboxLINST0005 = new VBox();
    VBox.setVgrow(vboxLINST0005, Priority.NEVER);
    vboxLINST0005.setPrefWidth(100.0);
    vboxLINST0005.setSpacing(10.0);
    vboxLINST0005.getStyleClass().add("ex-class-1");
    vboxLINST0005.getStylesheets().add(Util.resolveRelativeResourceFilePath(getClass(), "../style1.css"));
    vboxLINST0005.getChildren().addAll(radiobuttonLINST0006, radiobuttonLINST0007, radiobuttonLINST0008);
    VBox.setMargin(vboxLINST0005, new Insets(Double.parseDouble("0.0"), Double.parseDouble("0.0"), Double.parseDouble("0.0"), Double.parseDouble("0.0")));
    
    VBox vboxLINST0004 = new VBox();
    vboxLINST0004.setAlignment(Pos.CENTER);
    vboxLINST0004.setPrefHeight(480.0);
    vboxLINST0004.setPrefWidth(194.0);
    vboxLINST0004.setSpacing(5.0);
    HBox.setMargin(vboxLINST0004, new Insets(Double.parseDouble("10.0"), Double.parseDouble("10.0"), Double.parseDouble("10.0"), Double.parseDouble("10.0")));
    vboxLINST0004.getChildren().addAll(btnLeftPanel1, btnLeftPanel2, btnLeftPanel3, vboxLINST0005);
    
    ImageView imageviewLINST0010 = new ImageView();
    imageviewLINST0010.setFitHeight(150.0);
    imageviewLINST0010.setFitWidth(32.0);
    imageviewLINST0010.setPickOnBounds(true);
    imageviewLINST0010.setPreserveRatio(true);
    imageviewLINST0010.setImage(new Image(getClass().getResourceAsStream("/fxml2java/example/New.png")));
    
    btnRHSAddTItem = new Button();
    btnRHSAddTItem.setMnemonicParsing(false);
    btnRHSAddTItem.setOnAction((e) -> {
      onRHSAddTItem(e);
    });
    btnRHSAddTItem.setGraphic(imageviewLINST0010);
    
    tclCol1 = new TableColumn<>();
    tclCol1.setPrefWidth(233.0);
    tclCol1.setResizable(false);
    
    tclCol2 = new TableColumn<>();
    tclCol2.setPrefWidth(282.0);
    tclCol2.setResizable(false);
    
    tblItemTable = new TableView<>();
    tblItemTable.setPrefHeight(264.0);
    tblItemTable.setPrefWidth(609.0);
    tblItemTable.getColumns().add(tclCol1);
    tblItemTable.getColumns().add(tclCol2);
    
    Label labelLINST0017 = new Label();
    labelLINST0017.setText(resourceBundle.getString("gpane.row1.label"));
    
    Label labelLINST0018 = new Label();
    GridPane.setRowIndex(labelLINST0018, 1);
    labelLINST0018.setText(resourceBundle.getString("gpane.row2.label"));
    
    Label labelLINST0019 = new Label();
    GridPane.setRowIndex(labelLINST0019, 2);
    labelLINST0019.setText(resourceBundle.getString("gpane.row3.label"));
    
    txtRHBTxt1 = new TextField();
    GridPane.setColumnIndex(txtRHBTxt1, 1);
    txtRHBTxt1.setEditable(false);
    
    txtRHBTxt2 = new TextField();
    GridPane.setColumnIndex(txtRHBTxt2, 1);
    GridPane.setRowIndex(txtRHBTxt2, 1);
    txtRHBTxt2.setEditable(false);
    
    txtRHBTxt3 = new TextField();
    GridPane.setColumnIndex(txtRHBTxt3, 1);
    GridPane.setRowIndex(txtRHBTxt3, 2);
    txtRHBTxt3.setEditable(false);
    
    GridPane gridpaneLINST0011 = new GridPane();
    ColumnConstraints columnconstraintsLINST0012 = new ColumnConstraints();
    columnconstraintsLINST0012.setHgrow(Priority.SOMETIMES);
    columnconstraintsLINST0012.setMaxWidth(300.0);
    columnconstraintsLINST0012.setMinWidth(10.0);
    columnconstraintsLINST0012.setPrefWidth(155.0);
    gridpaneLINST0011.getColumnConstraints().add(columnconstraintsLINST0012);
    ColumnConstraints columnconstraintsLINST0013 = new ColumnConstraints();
    columnconstraintsLINST0013.setHgrow(Priority.SOMETIMES);
    columnconstraintsLINST0013.setMaxWidth(488.0);
    columnconstraintsLINST0013.setMinWidth(10.0);
    columnconstraintsLINST0013.setPrefWidth(454.0);
    gridpaneLINST0011.getColumnConstraints().add(columnconstraintsLINST0013);
    RowConstraints rowconstraintsLINST0014 = new RowConstraints();
    rowconstraintsLINST0014.setMinHeight(10.0);
    rowconstraintsLINST0014.setPrefHeight(30.0);
    rowconstraintsLINST0014.setVgrow(Priority.SOMETIMES);
    gridpaneLINST0011.getRowConstraints().add(rowconstraintsLINST0014);
    RowConstraints rowconstraintsLINST0015 = new RowConstraints();
    rowconstraintsLINST0015.setMinHeight(10.0);
    rowconstraintsLINST0015.setPrefHeight(30.0);
    rowconstraintsLINST0015.setVgrow(Priority.SOMETIMES);
    gridpaneLINST0011.getRowConstraints().add(rowconstraintsLINST0015);
    RowConstraints rowconstraintsLINST0016 = new RowConstraints();
    rowconstraintsLINST0016.setMinHeight(10.0);
    rowconstraintsLINST0016.setPrefHeight(30.0);
    rowconstraintsLINST0016.setVgrow(Priority.SOMETIMES);
    gridpaneLINST0011.getRowConstraints().add(rowconstraintsLINST0016);
    gridpaneLINST0011.getChildren().addAll(labelLINST0017, labelLINST0018, labelLINST0019, txtRHBTxt1, txtRHBTxt2, txtRHBTxt3);
    
    btnShowDlg = new Button();
    btnShowDlg.setMnemonicParsing(false);
    btnShowDlg.setOnAction((e) -> {
      actShowDialogue(e);
    });
    
    HBox hboxLINST0020 = new HBox();
    hboxLINST0020.setAlignment(Pos.CENTER);
    hboxLINST0020.setPrefHeight(100.0);
    hboxLINST0020.setPrefWidth(200.0);
    hboxLINST0020.getChildren().addAll(btnShowDlg);
    
    VBox vboxLINST0009 = new VBox();
    HBox.setHgrow(vboxLINST0009, Priority.ALWAYS);
    vboxLINST0009.setPrefHeight(200.0);
    vboxLINST0009.setPrefWidth(100.0);
    vboxLINST0009.setSpacing(5.0);
    vboxLINST0009.getChildren().addAll(btnRHSAddTItem, tblItemTable, gridpaneLINST0011, hboxLINST0020);
    HBox.setMargin(vboxLINST0009, new Insets(Double.parseDouble("0.0"), Double.parseDouble("0.0"), Double.parseDouble("0.0"), Double.parseDouble("0.0")));
    vboxLINST0009.setPadding(new Insets(Double.parseDouble("3.0"), Double.parseDouble("3.0"), Double.parseDouble("3.0"), Double.parseDouble("3.0")));
    
    HBox hboxLINST0003 = new HBox();
    VBox.setVgrow(hboxLINST0003, Priority.ALWAYS);
    hboxLINST0003.setPrefHeight(100.0);
    hboxLINST0003.setPrefWidth(200.0);
    hboxLINST0003.getChildren().addAll(vboxLINST0004, vboxLINST0009);
    
    vboxLINST0000 = new VBox();
    vboxLINST0000.setMaxHeight(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setMaxWidth(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setMinHeight(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setMinWidth(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setPrefHeight(536.0);
    vboxLINST0000.setPrefWidth(829.0);
    vboxLINST0000.getChildren().addAll(hboxLINST0001, hboxLINST0003);
    vboxLINST0000.setPadding(new Insets(Double.parseDouble("0.0"), Double.parseDouble("10.0"), Double.parseDouble("0.0"), Double.parseDouble("0.0")));
    
  }
  
  private void setTextStrings()
  {
    btnLeftPanel1.setText(resourceBundle.getString("button1.txt"));
    btnLeftPanel2.setText(resourceBundle.getString("button2.txt"));
    btnLeftPanel3.setText(resourceBundle.getString("button3.txt"));
    btnRHSAddTItem.setText(resourceBundle.getString("button.add"));
    tclCol1.setText(resourceBundle.getString("table.column1.title"));
    tclCol2.setText(resourceBundle.getString("table.column2.title"));
    btnShowDlg.setText(resourceBundle.getString("button.dlg.lbl"));
  }
  
  public void resourceUpdate(ResourceBundle resourceBundle)
  {
    this.resourceBundle = resourceBundle;
    setTextStrings();
  }
  
  public VBox getMainContainer()
  {
    return vboxLINST0000;
  }
  
  private void userConfig()
  {
    //Create dialogue box.
    e1d1 = new Example1Dialogue1(resourceBundle);
        
    // Set up the table column value factory.
    tclCol1.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TableItem, String>, ObservableValue<String>>()
    {
      @Override
      public ObservableValue<String> call(CellDataFeatures<TableItem, String> arg0)
      {
        // TODO Auto-generated method stub
        return new SimpleStringProperty(arg0.getValue().getC1());
      }
    });
    tclCol2.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<TableItem, String>, ObservableValue<String>>()
    {
      @Override
      public ObservableValue<String> call(CellDataFeatures<TableItem, String> arg0)
      {
        // TODO Auto-generated method stub
        return new SimpleStringProperty(arg0.getValue().getC2());
      }
    });
    // Use the toggle group to set the text of the non-editable text fields.
    tg1.selectedToggleProperty().addListener((a, b, c) -> {
      txtRHBTxt1.setText(((RadioButton) c).getText());
      txtRHBTxt2.setText(((RadioButton) c).getText());
      txtRHBTxt3.setText(((RadioButton) c).getText());
    });
  }
  
  private void onBtnLeft1(ActionEvent evt)
  {
    Alert a1 = new Alert(AlertType.CONFIRMATION, resourceBundle.getString("button1.lrt"));
    a1.showAndWait();
  }
  
  private void onBtnLeft2(ActionEvent evt)
  {
    Alert a1 = new Alert(AlertType.ERROR, resourceBundle.getString("button2.lrt"));
    a1.showAndWait();
  }
  
  private void onBtnLeft3(ActionEvent evt)
  {
    Alert a1 = new Alert(AlertType.WARNING, resourceBundle.getString("button3.lrt"));
    a1.showAndWait();
  }
  
  private void onRHSAddTItem(ActionEvent evt)
  {
    tblItemTable.getItems().add(new TableItem("Col 1#" + tableItemCount, "Col 2#" + tableItemCount++));
  }
  
  /**
   * <p>Show the dialogue box and report what happened.
   * 
   * @param evt
   */
  private void actShowDialogue(ActionEvent evt)
  {
    //Reset the dialogue and show it.
    e1d1.resetForm();
    Optional<Example1Dialogue1Item> showAndWait = e1d1.showAndWait();
    //Action from dialogue.
    if(showAndWait.isPresent())
    {
      //Get the result.
      Example1Dialogue1Item res = showAndWait.get();
      
      //Build the result.
      String s1 = resourceBundle.getString( (res.isChecked()) ? "dlg1.res.chk" : "dlg1.res.nochk");
      String s2 = (res.getChosenItem() != null) ? resourceBundle.getString("dlg1.res.chb") + res.getChosenItem() :  resourceBundle.getString("dlg1.res.nochb");
      Alert alt = new Alert(AlertType.INFORMATION, s1 + "\n" + s2);
      alt.show();
    }
    else
    {
      //Cancelled.
      Alert alt = new Alert(AlertType.ERROR, resourceBundle.getString("dlg1.res.cancel"));
      alt.show();
    }
  }
  
}
