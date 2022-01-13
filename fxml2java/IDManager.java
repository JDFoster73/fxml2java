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
package fxml2java;

/**
 * <p>Persistent integer parameter for passing up and down a recursive call chain.
 * 
 * @author jdf19
 *
 */
public class IDManager
{
  /**
   * <p>Next ID - will be returned postincremented.
   */
  private int nextID = 0;
  
  /**
   * <p>Initialise the parameter.
   * 
   * @param start initial value.
   */
  public IDManager(int start)
  {
    //Initialise the instance with the given start value.
    nextID = start;
  }
  
  /**
   * <p>Return and postincrement the next ID value.
   * @return
   */
  public int getNextID()
  {
    int ret = nextID++;
    return ret;
  }
}
