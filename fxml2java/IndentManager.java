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

import java.util.Arrays;

/**
 * <p>Track indentation of code and allow caller to set the number of spaces in the indentation.
 * 
 * @author James David Foster
 *
 */
public class IndentManager implements CharSequence
{
  private String indent;
  
  private String baseIndent;
  
  private int indentationLevel = 0;
  
  public IndentManager(int indentSpaces)
  {
    char[] spaceArray = new char[indentSpaces];
    Arrays.fill(spaceArray, ' ');
    baseIndent = new String(spaceArray);
    
    //Set indent to "" initially.
    indent = "";
  }
  
  /**
   * <p>Increase the indentation level.
   */
  public void inc()
  {
    indentationLevel++;
    
    //Update the indent string.
    indent = "";
    for(int i = 0; i < indentationLevel; i++) indent += baseIndent;
  }

  /**
   * <p>Decrease the indentation level.
   */
  public void dec()
  {
    if(indentationLevel == 0) throw new IllegalStateException();
    indentationLevel--;

    //Update the indent string.
    indent = "";
    for(int i = 0; i < indentationLevel; i++) indent += baseIndent;
  }
  
  public String toString()
  {
    return indent;
  }

  @Override
  public int length()
  {
    return indent.length();
  }

  @Override
  public char charAt(int index)
  {
    return indent.charAt(index);
  }

  @Override
  public CharSequence subSequence(int start, int end)
  {
    return indent.subSequence(start, end);
  }
}
