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
