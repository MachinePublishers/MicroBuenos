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
package com.machinepublishers.microbuenos.score.core;

import java.util.Random;

public abstract class Composition extends Segment {

  protected final Random rand = RandRandom.rand;
  private double root = 440d / 16d;

  public Composition(UnitGenerator master) {
    super(master, 0, -1);
    master.addListener(this);
  }

  public abstract void aboutToComputeSample(double sampleRate);

  public synchronized final double getRoot() {
    return root;
  }

  public synchronized final void setRoot(double freq) {
    root = freq;
  }
}
