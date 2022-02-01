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

