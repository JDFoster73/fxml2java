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
import java.util.ResourceBundle;
import javafx.scene.shape.StrokeType;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
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
import java.util.Optional;
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
  
  private final Example1Dialogue1 e1d1;
  
  public Example1(ResourceBundle resourceBundle)
  {
    this.resourceBundle = resourceBundle;
    this.e1d1 = new Example1Dialogue1(resourceBundle);
    setupFX();
    setTextStrings();
    userConfig();
  }
  
  private void setupFX()
  {
    Font fontLINST0003 = Font.font("Segoe UI Bold Italic", 19.0);
    
    Text textLINST0002 = new Text();
    textLINST0002.setStrokeType(StrokeType.OUTSIDE);
    textLINST0002.setStrokeWidth(0.0);
    textLINST0002.setText("title");
    textLINST0002.setFont(fontLINST0003);
    
    Insets insetsLINST0004 = new Insets(10.0, 10.0, 10.0, 10.0);
    
    Insets insetsLINST0005 = new Insets(0.0, 0.0, 0.0, 0.0);
    
    HBox hboxLINST0001 = new HBox();
    VBox.setVgrow(hboxLINST0001, Priority.NEVER);
    hboxLINST0001.setAlignment(Pos.CENTER);
    hboxLINST0001.setPrefWidth(200.0);
    hboxLINST0001.getChildren().addAll(textLINST0002);
    hboxLINST0001.setPadding(insetsLINST0004);
    VBox.setMargin(hboxLINST0001, insetsLINST0005);
    
    Insets insetsLINST0008 = new Insets(10.0, 10.0, 10.0, 10.0);
    
    Font fontLINST0009 = Font.font("Artifakt Element Bold Italic", 11.0);
    
    btnLeftPanel1 = new Button();
    btnLeftPanel1.setMnemonicParsing(false);
    btnLeftPanel1.setOnAction((e) -> {
      onBtnLeft1(e);
    });
    btnLeftPanel1.setPrefWidth(10000.0);
    
    btnLeftPanel1.setTextFill(Color.web("#d33f3f"));
    btnLeftPanel1.setFont(fontLINST0009);
    
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
    
    tg1 = new ToggleGroup();
    
    RadioButton radiobuttonLINST0011 = new RadioButton();
    radiobuttonLINST0011.setMnemonicParsing(false);
    radiobuttonLINST0011.setSelected(true);
    radiobuttonLINST0011.setText("radio1.txt");
    radiobuttonLINST0011.setToggleGroup(tg1);
    
    RadioButton radiobuttonLINST0012 = new RadioButton();
    radiobuttonLINST0012.setMnemonicParsing(false);
    radiobuttonLINST0012.setText("radio2.txt");
    radiobuttonLINST0012.setToggleGroup(tg1);
    
    RadioButton radiobuttonLINST0013 = new RadioButton();
    radiobuttonLINST0013.setMnemonicParsing(false);
    radiobuttonLINST0013.setText("radio3.txt");
    radiobuttonLINST0013.setToggleGroup(tg1);
    
    Insets insetsLINST0014 = new Insets(0.0, 0.0, 0.0, 0.0);
    
    VBox vboxLINST0010 = new VBox();
    VBox.setVgrow(vboxLINST0010, Priority.NEVER);
    vboxLINST0010.setPrefWidth(100.0);
    vboxLINST0010.setSpacing(10.0);
    vboxLINST0010.getStyleClass().add("ex-class-1");
    vboxLINST0010.getStylesheets().add(Util.resolveRelativeResourceFilePath(getClass(), "../style1.css"));
    vboxLINST0010.getChildren().addAll(radiobuttonLINST0011, radiobuttonLINST0012, radiobuttonLINST0013);
    VBox.setMargin(vboxLINST0010, insetsLINST0014);
    
    VBox vboxLINST0007 = new VBox();
    vboxLINST0007.setAlignment(Pos.CENTER);
    vboxLINST0007.setPrefHeight(480.0);
    vboxLINST0007.setPrefWidth(194.0);
    vboxLINST0007.setSpacing(5.0);
    HBox.setMargin(vboxLINST0007, insetsLINST0008);
    vboxLINST0007.getChildren().addAll(btnLeftPanel1, btnLeftPanel2, btnLeftPanel3, vboxLINST0010);
    
    Image imageLINST0017 = new Image(getClass().getResourceAsStream("/fxml2java/example/New.png"));
    
    ImageView imageviewLINST0016 = new ImageView();
    imageviewLINST0016.setFitHeight(150.0);
    imageviewLINST0016.setFitWidth(32.0);
    imageviewLINST0016.setPickOnBounds(true);
    imageviewLINST0016.setPreserveRatio(true);
    imageviewLINST0016.setImage(imageLINST0017);
    
    btnRHSAddTItem = new Button();
    btnRHSAddTItem.setMnemonicParsing(false);
    btnRHSAddTItem.setOnAction((e) -> {
      onRHSAddTItem(e);
    });
    
    btnRHSAddTItem.setGraphic(imageviewLINST0016);
    
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
    
    ColumnConstraints columnconstraintsLINST0019 = new ColumnConstraints();
    columnconstraintsLINST0019.setHgrow(Priority.SOMETIMES);
    columnconstraintsLINST0019.setMaxWidth(300.0);
    columnconstraintsLINST0019.setMinWidth(10.0);
    columnconstraintsLINST0019.setPrefWidth(155.0);
    
    ColumnConstraints columnconstraintsLINST0020 = new ColumnConstraints();
    columnconstraintsLINST0020.setHgrow(Priority.SOMETIMES);
    columnconstraintsLINST0020.setMaxWidth(488.0);
    columnconstraintsLINST0020.setMinWidth(10.0);
    columnconstraintsLINST0020.setPrefWidth(454.0);
    
    RowConstraints rowconstraintsLINST0021 = new RowConstraints();
    rowconstraintsLINST0021.setMinHeight(10.0);
    rowconstraintsLINST0021.setPrefHeight(30.0);
    rowconstraintsLINST0021.setVgrow(Priority.SOMETIMES);
    
    RowConstraints rowconstraintsLINST0022 = new RowConstraints();
    rowconstraintsLINST0022.setMinHeight(10.0);
    rowconstraintsLINST0022.setPrefHeight(30.0);
    rowconstraintsLINST0022.setVgrow(Priority.SOMETIMES);
    
    RowConstraints rowconstraintsLINST0023 = new RowConstraints();
    rowconstraintsLINST0023.setMinHeight(10.0);
    rowconstraintsLINST0023.setPrefHeight(30.0);
    rowconstraintsLINST0023.setVgrow(Priority.SOMETIMES);
    
    Label labelLINST0024 = new Label();
    labelLINST0024.setText("gpane.row1.label");
    
    Label labelLINST0025 = new Label();
    GridPane.setRowIndex(labelLINST0025, 1);
    labelLINST0025.setText("gpane.row2.label");
    
    Label labelLINST0026 = new Label();
    GridPane.setRowIndex(labelLINST0026, 2);
    labelLINST0026.setText("gpane.row3.label");
    
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
    
    GridPane gridpaneLINST0018 = new GridPane();
    gridpaneLINST0018.getColumnConstraints().add(columnconstraintsLINST0019);
    gridpaneLINST0018.getColumnConstraints().add(columnconstraintsLINST0020);
    gridpaneLINST0018.getRowConstraints().add(rowconstraintsLINST0021);
    gridpaneLINST0018.getRowConstraints().add(rowconstraintsLINST0022);
    gridpaneLINST0018.getRowConstraints().add(rowconstraintsLINST0023);
    gridpaneLINST0018.getChildren().addAll(labelLINST0024, labelLINST0025, labelLINST0026, txtRHBTxt1, txtRHBTxt2, txtRHBTxt3);
    
    btnShowDlg = new Button();
    btnShowDlg.setMnemonicParsing(false);
    btnShowDlg.setOnAction((e) -> {
      actShowDialogue(e);
    });
    
    HBox hboxLINST0027 = new HBox();
    hboxLINST0027.setAlignment(Pos.CENTER);
    hboxLINST0027.setPrefHeight(100.0);
    hboxLINST0027.setPrefWidth(200.0);
    hboxLINST0027.getChildren().addAll(btnShowDlg);
    
    Insets insetsLINST0028 = new Insets(0.0, 0.0, 0.0, 0.0);
    
    Insets insetsLINST0029 = new Insets(3.0, 3.0, 3.0, 3.0);
    
    VBox vboxLINST0015 = new VBox();
    HBox.setHgrow(vboxLINST0015, Priority.ALWAYS);
    vboxLINST0015.setPrefHeight(200.0);
    vboxLINST0015.setPrefWidth(100.0);
    vboxLINST0015.setSpacing(5.0);
    vboxLINST0015.getChildren().addAll(btnRHSAddTItem, tblItemTable, gridpaneLINST0018, hboxLINST0027);
    HBox.setMargin(vboxLINST0015, insetsLINST0028);
    vboxLINST0015.setPadding(insetsLINST0029);
    
    HBox hboxLINST0006 = new HBox();
    VBox.setVgrow(hboxLINST0006, Priority.ALWAYS);
    hboxLINST0006.setPrefHeight(100.0);
    hboxLINST0006.setPrefWidth(200.0);
    hboxLINST0006.getChildren().addAll(vboxLINST0007, vboxLINST0015);
    
    Insets insetsLINST0030 = new Insets(0.0, 10.0, 0.0, 0.0);
    
    vboxLINST0000 = new VBox();
    vboxLINST0000.setMaxHeight(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setMaxWidth(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setMinHeight(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setMinWidth(Double.NEGATIVE_INFINITY);
    vboxLINST0000.setPrefHeight(536.0);
    vboxLINST0000.setPrefWidth(829.0);
    vboxLINST0000.getChildren().add(hboxLINST0001);
    vboxLINST0000.getChildren().add(hboxLINST0006);
    vboxLINST0000.setPadding(insetsLINST0030);
    
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
    // Create dialogue box.
    // e1d1 = new Example1Dialogue1(resourceBundle);
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
    int tableItemCount = tblItemTable.getItems().size();
    tblItemTable.getItems().add(new TableItem("Col 1#" + tableItemCount, "Col 2#" + tableItemCount++));
  }
  
  /**
   * <p>
   * Show the dialogue box and report what happened.
   *
   * @param evt
   */
  private void actShowDialogue(ActionEvent evt)
  {
    // Reset the dialogue and show it.
    e1d1.resetForm();
    Optional<Example1Dialogue1Item> showAndWait = e1d1.showAndWait();
    // Action from dialogue.
    if (showAndWait.isPresent())
    {
      // Get the result.
      Example1Dialogue1Item res = showAndWait.get();
      // Build the result.
      String s1 = resourceBundle.getString((res.isChecked()) ? "dlg1.res.chk" : "dlg1.res.nochk");
      String s2 = (res.getChosenItem() != null) ? resourceBundle.getString("dlg1.res.chb") + res.getChosenItem() : resourceBundle.getString("dlg1.res.nochb");
      Alert alt = new Alert(AlertType.INFORMATION, s1 + "\n" + s2);
      alt.show();
    }
    else
    {
      // Cancelled.
      Alert alt = new Alert(AlertType.ERROR, resourceBundle.getString("dlg1.res.cancel"));
      alt.show();
    }
  }
  
}
