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
