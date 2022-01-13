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
 * 
 * @author James David Foster
 *
 */
public class Handler
{
  private final String type;

  private final String handlerName;

  public Handler(String type, String handlerName)
  {
    this.type = type;
    this.handlerName = handlerName;
  }

  @Override
  public String toString()
  {
    return "[" + handlerName + "::" + type + "]";
  }

  public String getType()
  {
    return type;
  }

  public String getHandlerName()
  {
    return handlerName;
  }

  public String getMethodSignature()
  {
    switch(type)
    {
      case "onAction":
        return handlerName + "(ActionEvent)";
      default:
        throw new IllegalArgumentException("The handler type " + type + " is not known.");
    }
  }

  public String getMethodDeclaration()
  {
    switch(type)
    {
      case "onAction":
        return "(ActionEvent evt)";
      default:
        throw new IllegalArgumentException("The handler type " + type + " is not known.");
    }
  }
}

