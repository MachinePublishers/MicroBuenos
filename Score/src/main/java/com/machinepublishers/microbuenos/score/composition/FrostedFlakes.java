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
package com.machinepublishers.microbuenos.score.composition;

import com.machinepublishers.microbuenos.score.core.effect.Reverb;
import com.machinepublishers.microbuenos.score.core.gen.Gain;
import com.machinepublishers.microbuenos.score.core.gen.Sine;
import com.machinepublishers.microbuenos.score.core.gen.Tone;
import com.machinepublishers.microbuenos.score.core.notes.Note;
import com.machinepublishers.microbuenos.score.core.Composition;
import com.machinepublishers.microbuenos.score.core.Mix;
import com.machinepublishers.microbuenos.score.core.Patch;

public class FrostedFlakes extends Composition {

  private static Patch master = new Patch();
  private final Tone sine1;
  private final Tone sine2;
  private final Tone sine3;
  private final Tone sine4;
  private final Tone saw2;
  private final Tone saw3;
  double[] loFreqs = new double[]{Note.TwelveTET.instance().getFreq(0, 4),
      Note.TwelveTET.instance().getFreq(3, 4),
      Note.TwelveTET.instance().getFreq(5, 4),
      Note.TwelveTET.instance().getFreq(7, 4),
      Note.TwelveTET.instance().getFreq(0, 4)};
  double[] hiFreqs = new double[]{Note.TwelveTET.instance().getFreq(0, 8),
      Note.TwelveTET.instance().getFreq(3, 8),
      Note.TwelveTET.instance().getFreq(5, 8),
      Note.TwelveTET.instance().getFreq(7, 8),
      Note.TwelveTET.instance().getFreq(0, 8)};
  double[] hiFreqs2 = new double[]{Note.TwelveTET.instance().getFreq(3, 8),
      Note.TwelveTET.instance().getFreq(5, 8),
      Note.TwelveTET.instance().getFreq(7, 8),
      Note.TwelveTET.instance().getFreq(8, 8),
      Note.TwelveTET.instance().getFreq(0, 8),
      Note.TwelveTET.instance().getFreq(3, 8),
      Note.TwelveTET.instance().getFreq(5, 8),
      Note.TwelveTET.instance().getFreq(7, 8),
      Note.TwelveTET.instance().getFreq(8, 8),
      Note.TwelveTET.instance().getFreq(0, 16),
      Note.TwelveTET.instance().getFreq(3, 16),
      Note.TwelveTET.instance().getFreq(5, 16),
      Note.TwelveTET.instance().getFreq(7, 16),
      Note.TwelveTET.instance().getFreq(8, 16)};

  public FrostedFlakes() {
    super(master);
    Patch patcher1 = new Patch();
    sine1 = new Sine(); // TODO add freq to ctor
    patcher1.add(sine1);
    patcher1.add(new Gain(.035));

    Patch patcher2 = new Patch();
    sine2 = new Sine();
    patcher2.add(sine2);
    patcher2.add(new Gain(.035));

    Patch patcher3 = new Patch();
    sine3 = new Sine();
    patcher3.add(sine3);
    patcher3.add(new Gain(.035));

    Patch patcher4 = new Patch();
    sine4 = new Sine(); // TODO add freq to ctor
    patcher4.add(sine4);
    patcher4.add(new Gain(.035));

    Patch patcher5 = new Patch();
    saw2 = new Sine();
    patcher5.add(saw2);
    patcher5.add(new Gain(.035));

    Patch patcher6 = new Patch();
    saw3 = new Sine();
    patcher6.add(saw3);
    patcher6.add(new Gain(.035));

    Mix mixer = new Mix();
    mixer.add(patcher1);
    mixer.add(patcher2);
    mixer.add(patcher3);
    mixer.add(patcher4);
    mixer.add(patcher5);
    mixer.add(patcher6);

    Reverb reverb = new Reverb();
    master.add(mixer);
    master.add(reverb);
  }

  @Override
  public void aboutToComputeSample(double sampleRate) {
    double base;
    double octave1 = 4;
    double octave2 = 1;
    double octave3 = 4;
    double octave4 = 2;
    double octave5 = 1;
    if (rand.nextDouble() < .5) {
      octave1 = 2;
      octave2 = 2;
    } else {
      octave1 = 4;
      octave2 = 2;
      octave3 = 4;
      octave4 = 2;
      octave5 = 2;
    }

    base = loFreqs[rand.nextInt(loFreqs.length)];
    sine1.setFreq(getRoot() * base * octave1);

    base = hiFreqs[rand.nextInt(hiFreqs.length)];
    sine2.setFreq(getRoot() * 2 * base * octave2);

    base = hiFreqs2[rand.nextInt(hiFreqs2.length)];
    sine3.setFreq(getRoot() * 2 * base * octave2);

    base = loFreqs[rand.nextInt(loFreqs.length)];
    sine4.setFreq(getRoot() * 2 * base * octave3);

    base = hiFreqs[rand.nextInt(hiFreqs.length)];
    saw2.setFreq(getRoot() * 2 * base * octave4);

    base = hiFreqs2[rand.nextInt(hiFreqs2.length)];
    saw3.setFreq(getRoot() * 2 * base * octave5);

  }

  @Override
  public void next(double offset, double[] inputVal, double sampleRate) {
    super.next(1, inputVal, sampleRate);
  }
}
