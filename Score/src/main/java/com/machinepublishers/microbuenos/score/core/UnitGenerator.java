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

import java.util.ArrayList;

public abstract class UnitGenerator {

  private ArrayList<Composition> listeners = null;

  public UnitGenerator() {
  }

  /**
   * If you override this (which is generally what you'll want to do), just be sure to call super.
   *
   * @param offset   TODO
   * @param inputVal
   * @param conf
   */
  public void next(double offset, double[] inputVal, double sampleRate) {
    if (listeners != null) {
      fireNext(inputVal, sampleRate);
    }
  }

  private void fireNext(double[] inputVal, double sampleRate) {
    for (int i = 0; i < listeners.size(); i++) {
      Composition listener = listeners.get(i);
      listener.aboutToComputeSample(sampleRate);
    }
  }

  public final void addListener(Composition listener) {
    listeners =
        listeners == null ? new ArrayList<>()
            : listeners;
    listeners.add(listener);
  }
}
