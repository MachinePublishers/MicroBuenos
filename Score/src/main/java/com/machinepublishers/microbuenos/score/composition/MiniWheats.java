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
import com.machinepublishers.microbuenos.score.core.gen.Saw;
import com.machinepublishers.microbuenos.score.core.gen.Tone;
import com.machinepublishers.microbuenos.score.core.gen.Triangle;
import com.machinepublishers.microbuenos.score.core.notes.Note;
import com.machinepublishers.microbuenos.score.core.Composition;
import com.machinepublishers.microbuenos.score.core.Mix;
import com.machinepublishers.microbuenos.score.core.Patch;

public class MiniWheats extends Composition {

  private static Patch master = new Patch();
  private final Tone tone1;
  private final Tone tone2;
  private final Tone tone3;
  double[] loFreqs = Note.TwelveTET.instance().getFreqs("maj", 2);
  double[] hiFreqs = Note.TwelveTET.instance().getFreqs("5", 4);
  double[] hiFreqs2 = Note.TwelveTET.instance().getFreqs("maj 7", 8);
  private double start = 0;
  private double len = 0;
  private double scale = 300.;
  private double grain = 200.;
  private boolean isWild = false;

  public MiniWheats() {
    super(master);
    Patch patcher1 = new Patch();
    tone1 = new Saw(); //TODO add freq to ctor
    patcher1.add(tone1);
    patcher1.add(new Gain(.05));
    patcher1.add(new Reverb());

    Patch patcher2 = new Patch();
    tone2 = new Saw();
    patcher2.add(tone2);
    patcher2.add(new Gain(.03));
    patcher2.add(new Reverb());

    Patch patcher3 = new Patch();
    tone3 = new Triangle();
    patcher3.add(tone3);
    patcher3.add(new Gain(.3));
    patcher3.add(new Reverb());
    patcher3.add(new Gain(.3));
    patcher3.add(new Reverb());
    patcher3.add(new Gain(.5));
    patcher3.add(new Reverb());

    Mix mixer = new Mix();
    mixer.add(patcher1);
    mixer.add(patcher2);
    mixer.add(patcher3);

    Reverb reverb = new Reverb();
    master.add(mixer);
    master.add(reverb);
  }

  @Override
  public void aboutToComputeSample(double sampleRate) {
    double cur = getCurTimeInMs(sampleRate);
    if (cur >= 0 && (Math.abs(cur - start) >= len) && (cur != start || cur == 0)) {

      start = cur;
      double base = 0;
      double deviation = rand.nextDouble() * .008 * (rand.nextBoolean() ? 1 : -1);
      double noteChange = rand.nextDouble();
      if (noteChange < .5) {
        base = loFreqs[(int) Math.rint(rand.nextDouble() * ((double) loFreqs.length - 1))];
        tone1.setFreq((deviation + 1.) * base * getRoot());
      }
      if (noteChange < .1) {
        base = hiFreqs[(int) Math.rint(rand.nextDouble() * ((double) hiFreqs.length - 1))];
        tone2.setFreq((deviation + 1.) * base * getRoot());
      }
      if (noteChange < .8) {
        base = hiFreqs2[(int) Math.rint(rand.nextDouble() * ((double) hiFreqs2.length - 1))];
        tone3.setFreq((deviation + 1.) * base * getRoot());
      }
      double factor = 1000 * rand.nextDouble();
      isWild = rand.nextDouble() < .5;
      len = (((rand.nextDouble() * factor)) * factor);

      if (!isWild) {
        len %= 32452843;
        len = len == 0 ? 7001 : len;
      } else {
        len %= 15485863;
        len = len == 0 ? 4001 : len;
      }
      len %= scale;
      len -= len % grain;
      len -= cur % grain; //error correction... try to stay on beat
      len += grain;
    }
  }

  @Override
  public void next(double offset, double[] inputVal, double sampleRate) {
    super.next(
        offset < sampleRate
            ? 1
            : (Math.random() < .5
                ? 108
                : (Math.random() < .5 ? -200 : 100)),
        inputVal, sampleRate);
  }

}
