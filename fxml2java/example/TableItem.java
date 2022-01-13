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

/**
 * <p>Example data for the displaying in the table.
 * 
 * @author James David Foster
 *
 */
public class TableItem
{
  private final String c1;
  
  private final String c2;

  public TableItem(String c1, String c2)
  {
    super();
    this.c1 = c1;
    this.c2 = c2;
  }

  public String getC1()
  {
    return c1;
  }

  public String getC2()
  {
    return c2;
  }
  
  
}
