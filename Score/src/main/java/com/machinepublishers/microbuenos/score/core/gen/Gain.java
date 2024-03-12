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
package com.machinepublishers.microbuenos.score.core.gen;

import com.machinepublishers.microbuenos.score.core.UnitGenerator;

public class Gain extends UnitGenerator {

  private double gain;

  public Gain(double gain) {
    this.gain = gain;
  }

  @Override
  public void next(double offset, double[] inputVal, double sampleRate) {
    super.next(offset, inputVal, sampleRate);
    for (int i = 0; i < inputVal.length; i++) {
      inputVal[i] *= gain;
    }
  }

  public void setGain(double gain) {
    this.gain = gain;
  }
}