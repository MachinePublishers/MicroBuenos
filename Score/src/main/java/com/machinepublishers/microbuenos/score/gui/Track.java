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
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class Track extends JPanel {

  public Track() {
    setLayout(null);
    setBorder(BorderFactory.createLineBorder(Color.WHITE));
    setSize(new Dimension(Integer.MAX_VALUE, 30));
    setPreferredSize(new Dimension(30, 30));
    setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
    setMinimumSize(new Dimension(30, 30));
    setBackground(Color.LIGHT_GRAY);
    addMouseListener(new MouseListener() {

      public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

      }

      public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

      }

      public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

      }

      public void mousePressed(MouseEvent e) {
        TrackSegment ts = new TrackSegment();
        ts.setLocation(0, e.getX());
        ts.setSize(10, 30);
        add(ts);
        revalidate();
      }

      public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

      }
    });
  }
}
