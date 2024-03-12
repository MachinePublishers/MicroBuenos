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

public abstract class Segment extends UnitGenerator {

  final static double FRAC_POS = .001;
  final static double FRAC_NEG = -1. * FRAC_POS;
  private double start = 0;
  private double dur = 10;
  private double curSamp = -1;
  private final UnitGenerator unit;
  private boolean silent = false;

  public Segment(final UnitGenerator unit, double startInMs,
      double durInMs) {
    this.unit = unit;
    setStart(startInMs / 1000.);
    setDur(durInMs / 1000.);
  }

  @Override
  public void next(double offset, double[] inputVal, double sampleRate) {
    curSamp += offset;
    super.next(offset, inputVal, sampleRate);

    if (isPlaying(sampleRate)) {
      unit.next(offset, inputVal, sampleRate);
    } else if (isEnded(sampleRate) && !silent) {//do a slight fade out to avoid popping
      silent = true;
      for (int i = 0; i < inputVal.length; i++) {
        double change = inputVal[i] > 0. ? FRAC_NEG : inputVal[i] < 0. ? FRAC_POS : 0.;
        inputVal[i] += change;
        inputVal[i] = Math.abs(inputVal[i]) < 2 * FRAC_POS ? 0. : inputVal[i];
        if (inputVal[i] != 0.) {
          silent = false;
        }
      }
    } else {
      for (int i = 0; i < inputVal.length; i++) {
        inputVal[i] = 0.;
      }
    }

  }

  private synchronized double getStart() {
    return start;
  }

  private synchronized void setStart(double d) {
    start = d;
  }

  private synchronized double getDur() {
    return dur;
  }

  private synchronized void setDur(double d) {
    dur = d;
  }

  public void startPlaying() {
    setStart(0);
    setDur(-1);
  }

  public void stopPlaying() {
    setStart(0);
    setDur(.5);
  }

  public boolean isPlaying(double sampleRate) {
    return dur < 0 || (curSamp / sampleRate >= getStart()
        && curSamp / sampleRate - getStart() < getDur());
  }

  public boolean isEnded(double sampleRate) {
    return dur >= 0 && curSamp / sampleRate - getStart() >= getDur();
  }

  /**
   * Might be fractional.  But will be in milliseconds.
   *
   * @param audioConf
   * @return MS
   */
  public double getCurTimeInMs(double sampleRate) {
    return curSamp / sampleRate * 1000d;
  }
}
