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
// Java port of the public domain C++ lib Freeverb: https://github.com/sinshu/freeverb

package com.machinepublishers.microbuenos.score.core.effect;

import com.machinepublishers.microbuenos.score.core.UnitGenerator;

public class Comb extends UnitGenerator {

  private double feedback;
  private double filterStore;
  private double dampA;
  private double dampB;
  private double[] buf;
  private int curBuf;

  public Comb() {
    filterStore = 0;
    curBuf = 0;
  }

  public void setBuffer(double[] buf) {
    this.buf = buf;
  }

  public double process(double inputVal) {
    double output;
    output = buf[curBuf];
    filterStore = (output * dampB) + (filterStore * dampA);
    buf[curBuf] = inputVal + (filterStore * feedback);
    if (++curBuf >= buf.length) {
      curBuf = 0;
    }
    return output;
  }

  public void mute() {
    for (int i = 0; i < buf.length; i++) {
      buf[i] = 0;
    }
  }

  public void setDamp(double val) {
    dampA = val;
    dampB = 1 - val;
  }

  public void setFeedback(double val) {
    feedback = val;
  }
}
