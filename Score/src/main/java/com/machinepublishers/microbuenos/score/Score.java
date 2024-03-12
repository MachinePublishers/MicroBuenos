/*
 * Copyright 2009-2023 MicroBuenos committers
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.machinepublishers.microbuenos.score;

import com.machinepublishers.microbuenos.score.gui.Main;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class Score extends JFrame {

  private Score() {
    Main view = new Main();
    getContentPane().add(view);
    setTitle("");
    this.setSize(600, 80);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }

  public static void main(String[] args) {
    try {
      SwingUtilities.invokeAndWait(() -> new Score());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
