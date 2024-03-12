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

public class Patch extends UnitGenerator {

  private final ArrayList<UnitGenerator> units = new ArrayList<>();

  @Override
  public void next(double offset, double[] inputVal, double sampleRate) {
    super.next(offset, inputVal, sampleRate);
    for (int i = 0; i < units.size(); i++) {
      UnitGenerator curUnit = units.get(i);
      curUnit.next(offset, inputVal, sampleRate);
    }
  }

  public void add(UnitGenerator unit) {
    units.add(unit);
  }
}