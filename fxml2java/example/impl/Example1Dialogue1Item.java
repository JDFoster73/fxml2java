package fxml2java.example.impl;

/**
 * @author James David Foster
 *
 */
public class Example1Dialogue1Item
{
  private final boolean isChecked;
  
  private final String chosenItem;

  public Example1Dialogue1Item(boolean isChecked, String chosenItem)
  {
    super();
    this.isChecked = isChecked;
    this.chosenItem = chosenItem;
  }

  public boolean isChecked()
  {
    return isChecked;
  }

  public String getChosenItem()
  {
    return chosenItem;
  }
  
  
}
