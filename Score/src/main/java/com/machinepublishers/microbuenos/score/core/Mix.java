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
import java.util.Arrays;

public class Mix extends UnitGenerator {

  private final ArrayList<UnitGenerator> patches = new ArrayList<>();

  @Override
  public void next(double offset, double[] inputVal, double sampleRate) {
    super.next(offset, inputVal, sampleRate);
    double[] runningVal = new double[inputVal.length];
    Arrays.fill(runningVal, 0.);
    for (int i = 0; i < patches.size(); i++) {

      //don't let the patches being mixed affect each other,
      //so always use original input to start
      double[] workingInputVal = inputVal.clone();

      UnitGenerator curUnit = patches.get(i);
      curUnit.next(offset, workingInputVal, sampleRate);

      for (int j = 0; j < workingInputVal.length; j++) {
        runningVal[j] += workingInputVal[j];
      }
    }
    for (int j = 0; j < runningVal.length; j++) {
      inputVal[j] = runningVal[j] / patches.size();
    }
  }

  public void add(UnitGenerator unit) {
    patches.add(unit);
  }
}
