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

public class Reverb extends UnitGenerator {

  private int numCombs = 8;
  private int numAllpasses = 4;
  private float muted = 0;
  private float fixedGain = 0.015f;
  private float scaleWet = 3;
  private float scaleDry = 2;
  private float scaleDamp = 0.4f;
  private float scaleRoom = 0.28f;
  private float offsetRoom = 0.7f;
  private float initialRoom = 0.5f;
  private float initialDamp = 0.5f;
  private float initialWet = 1 / scaleWet;
  private float initialDry = 0;
  private float initialWidth = 1;
  private float initialMode = 0;
  private float freezeMode = 0.5f;
  private int stereoSpread = 23;

  //TODO These values assume 44.1KHz sample rate -- need scaling for other sample rates
  private int combtuningL1 = 1116;
  private int combtuningR1 = 1116 + stereoSpread;
  private double[] bufcombR1 = new double[combtuningR1];
  private int combtuningL2 = 1188;
  private int combtuningR2 = 1188 + stereoSpread;
  private double[] bufcombR2 = new double[combtuningR2];
  private int combtuningL3 = 1277;
  private int combtuningR3 = 1277 + stereoSpread;
  private double[] bufcombR3 = new double[combtuningR3];
  private int combtuningL4 = 1356;
  private int combtuningR4 = 1356 + stereoSpread;
  private double[] bufcombR4 = new double[combtuningR4];
  private int combtuningL5 = 1422;
  private int combtuningR5 = 1422 + stereoSpread;
  private double[] bufcombR5 = new double[combtuningR5];
  private int combtuningL6 = 1491;
  private int combtuningR6 = 1491 + stereoSpread;
  private double[] bufcombR6 = new double[combtuningR6];
  private int combtuningL7 = 1557;
  private int combtuningR7 = 1557 + stereoSpread;
  private double[] bufcombR7 = new double[combtuningR7];
  private int combtuningL8 = 1617;
  private int combtuningR8 = 1617 + stereoSpread;
  private double[] bufcombR8 = new double[combtuningR8];
  private int allpasstuningL1 = 556;
  private int allpasstuningR1 = 556 + stereoSpread;
  private double[] bufallpassR1 = new double[allpasstuningR1];
  private int allpasstuningL2 = 441;
  private int allpasstuningR2 = 441 + stereoSpread;
  private double[] bufallpassR2 = new double[allpasstuningR2];
  private int allpasstuningL3 = 341;
  private int allpasstuningR3 = 341 + stereoSpread;
  private double[] bufallpassR3 = new double[allpasstuningR3];
  private int allpasstuningL4 = 225;
  private int allpasstuningR4 = 225 + stereoSpread;
  private double[] bufallpassR4 = new double[allpasstuningR4];
  private double gain;
  private double roomSize, roomsize1;
  private double damp, damp1;
  private double wet, wet1, wet2;
  private double dry;
  private double width;
  private double mode;
  // Buffers for the combs
  private double[] bufcombL1 = new double[combtuningL1];
  private double[] bufcombL2 = new double[combtuningL2];
  private double[] bufcombL3 = new double[combtuningL3];
  private double[] bufcombL4 = new double[combtuningL4];
  private double[] bufcombL5 = new double[combtuningL5];
  private double[] bufcombL6 = new double[combtuningL6];
  private double[] bufcombL7 = new double[combtuningL7];
  private double[] bufcombL8 = new double[combtuningL8];
  // Buffers for the allpasses
  private double[] bufallpassL1 = new double[allpasstuningL1];
  private double[] bufallpassL2 = new double[allpasstuningL2];
  private double[] bufallpassL3 = new double[allpasstuningL3];
  private double[] bufallpassL4 = new double[allpasstuningL4];
  private Comb[] combL = new Comb[numCombs];
  private Comb[] combR = new Comb[numCombs];
  private Allpass[] allpassL = new Allpass[numAllpasses];
  private Allpass[] allpassR = new Allpass[numAllpasses];

  public Reverb() {

    for (int i = 0; i < numCombs; i++) {
      combL[i] = new Comb();
      combR[i] = new Comb();
    }
    for (int i = 0; i < numAllpasses; i++) {
      allpassL[i] = new Allpass();
      allpassR[i] = new Allpass();
    }
    // Tie the components to their buffers
    combL[0].setBuffer(bufcombL1);
    combR[0].setBuffer(bufcombR1);
    combL[1].setBuffer(bufcombL2);
    combR[1].setBuffer(bufcombR2);
    combL[2].setBuffer(bufcombL3);
    combR[2].setBuffer(bufcombR3);
    combL[3].setBuffer(bufcombL4);
    combR[3].setBuffer(bufcombR4);
    combL[4].setBuffer(bufcombL5);
    combR[4].setBuffer(bufcombR5);
    combL[5].setBuffer(bufcombL6);
    combR[5].setBuffer(bufcombR6);
    combL[6].setBuffer(bufcombL7);
    combR[6].setBuffer(bufcombR7);
    combL[7].setBuffer(bufcombL8);
    combR[7].setBuffer(bufcombR8);
    allpassL[0].setBuffer(bufallpassL1);
    allpassR[0].setBuffer(bufallpassR1);
    allpassL[1].setBuffer(bufallpassL2);
    allpassR[1].setBuffer(bufallpassR2);
    allpassL[2].setBuffer(bufallpassL3);
    allpassR[2].setBuffer(bufallpassR3);
    allpassL[3].setBuffer(bufallpassL4);
    allpassR[3].setBuffer(bufallpassR4);

    // Set default values
    allpassL[0].setFeedback(0.5);
    allpassR[0].setFeedback(0.5);
    allpassL[1].setFeedback(0.5);
    allpassR[1].setFeedback(0.5);
    allpassL[2].setFeedback(0.5);
    allpassR[2].setFeedback(0.5);
    allpassL[3].setFeedback(0.5);
    allpassR[3].setFeedback(0.5);
    setWet(initialWet);
    setRoomSize(initialRoom);
    setDry(initialDry);
    setDamp(initialDamp);
    setWidth(initialWidth);
    setMode(initialMode);

    // Buffer will be full of rubbish - so we MUST mute them
    mute();
  }

  @Override
  public void next(double offset, double[] inputVal, double sampleRate) {
    super.next(offset, inputVal, sampleRate);
    double sum = (inputVal[0] + inputVal[1]) * gain;
    double outL = 0, outR = 0;
    // Accumulate comb filters in parallel
    for (int i = 0; i < numCombs; i++) {
      outL += combL[i].process(sum);
      outR += combR[i].process(sum);
    }

    // Feed through allpasses in series
    for (int i = 0; i < numAllpasses; i++) {
      outL = allpassL[i].process(outL);
      outR = allpassR[i].process(outR);
    }

    // Calculate output MIXING with anything already there
    inputVal[0] = outL * wet1 + outR * wet2 + inputVal[0] * dry;
    inputVal[1] = outR * wet1 + outL * wet2 + inputVal[1] * dry;
  }

  public void mute() {
    if (getmode() >= freezeMode) {
      return;
    }

    for (int i = 0; i < numCombs; i++) {
      combL[i].mute();
      combR[i].mute();
    }
    for (int i = 0; i < numAllpasses; i++) {
      allpassL[i].mute();
      allpassR[i].mute();
    }
  }

  private void update() {
    // Recalculate internal values after parameter change

    int i;

    wet1 = wet * (width / 2 + 0.5f);
    wet2 = wet * ((1 - width) / 2);

    if (mode >= freezeMode) {
      roomsize1 = 1;
      damp1 = 0;
      gain = muted;
    } else {
      roomsize1 = roomSize;
      damp1 = damp;
      gain = fixedGain;
    }

    for (i = 0; i < numCombs; i++) {
      combL[i].setFeedback(roomsize1);
      combR[i].setFeedback(roomsize1);
    }

    for (i = 0; i < numCombs; i++) {
      combL[i].setDamp(damp1);
      combR[i].setDamp(damp1);
    }
  }

  public double getRoomSize() {
    return (roomSize - offsetRoom) / scaleRoom;
  }

  public void setRoomSize(double value) {
    roomSize = (value * scaleRoom) + offsetRoom;
    update();
  }

  public double getDamp() {
    return damp / scaleDamp;
  }

  public void setDamp(double value) {
    damp = value * scaleDamp;
    update();
  }

  public double getWet() {
    return wet / scaleWet;
  }

  public void setWet(double value) {
    wet = value * scaleWet;
    update();
  }

  public double getDry() {
    return dry / scaleDry;
  }

  public void setDry(double value) {
    dry = value * scaleDry;
  }

  public double getWidth() {
    return width;
  }

  public void setWidth(double value) {
    width = value;
    update();
  }

  public void setMode(double value) {
    mode = value;
    update();
  }

  public double getmode() {
    if (mode >= freezeMode) {
      return 1;
    }
    return 0;
  }
}
