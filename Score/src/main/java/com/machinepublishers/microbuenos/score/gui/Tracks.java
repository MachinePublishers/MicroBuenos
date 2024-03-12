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
package com.machinepublishers.microbuenos.score.gui;

import java.awt.Color;
import javax.swing.BoxLayout;
import javax.swing.JPanel;

public class Tracks extends JPanel {

  private static Tracks instance = new Tracks();

  private Tracks() {
    setBackground(Color.BLACK);
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
  }

  public static Tracks instance() {
    return instance;
  }

  public void addTrack() {
    add(new Track());
    revalidate();
  }
}
