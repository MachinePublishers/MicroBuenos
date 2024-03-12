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

public class Sine extends Tone {

  @Override
  public void next(double offset, double[] inputVal, double sampleRate) {
    super.next(offset, inputVal, sampleRate);
    double period = sampleRate / getFreq(sampleRate);
    setInc(period / getPrevPeriod() * getInc() + offset);
    setPrevPeriod(period);
    if (getInc() >= period) {
      setInc(0);
    } else if (getInc() < 0) {
      setInc(period - 1);
    }
    double val = Math.sin(Math.PI * 2. * (getInc() / period));
    for (int i = 0; i < inputVal.length; i++) {
      inputVal[i] = val;
    }
  }
}
