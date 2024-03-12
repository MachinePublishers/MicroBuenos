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
import com.machinepublishers.microbuenos.score.core.gen.Sine;
import com.machinepublishers.microbuenos.score.core.gen.Square;
import com.machinepublishers.microbuenos.score.core.gen.Tone;
import com.machinepublishers.microbuenos.score.core.gen.Triangle;
import com.machinepublishers.microbuenos.score.core.notes.Chord;
import com.machinepublishers.microbuenos.score.core.notes.Note;
import com.machinepublishers.microbuenos.score.core.Composition;
import com.machinepublishers.microbuenos.score.core.Mix;
import com.machinepublishers.microbuenos.score.core.Patch;

public class ChocolateStout extends Composition {

  private static Patch master = new Patch();
  private double start = 0;
  private double cur = 0;
  private double len = 0;
  private double scale = 20;
  private double grain = 5.;
  private boolean isWild = false;
  private Chord chord1;
  private Chord chord2;
  private Chord chord3;
  private Tone tone1;
  private Tone tone2;
  private Tone tone3;
  private double[] loFreqs = new double[]{Note.TwelveTET.instance().getFreq(0, 2),
      Note.TwelveTET.instance().getFreq(7, 4),
      Note.TwelveTET.instance().getFreq(5, 2),
      Note.TwelveTET.instance().getFreq(7, 2),
      Note.TwelveTET.instance().getFreq(7, 2),
      Note.TwelveTET.instance().getFreq(9, 2),
      Note.TwelveTET.instance().getFreq(0, 2),
      Note.TwelveTET.instance().getFreq(7, 4),
      Note.TwelveTET.instance().getFreq(5, 4)};
  private double[] hiFreqs = new double[]{Note.TwelveTET.instance().getFreq(0, 8),
      Note.TwelveTET.instance().getFreq(5, 8),
      Note.TwelveTET.instance().getFreq(9, 8),
      Note.TwelveTET.instance().getFreq(0, 8),
      Note.TwelveTET.instance().getFreq(9, 8),
      Note.TwelveTET.instance().getFreq(5, 8),
      Note.TwelveTET.instance().getFreq(0, 8),
      Note.TwelveTET.instance().getFreq(4, 8),
      Note.TwelveTET.instance().getFreq(7, 8)};
  private double[] hiFreqs2 = new double[]{Note.TwelveTET.instance().getFreq(0, 16),
      Note.TwelveTET.instance().getFreq(0, 16),
      Note.TwelveTET.instance().getFreq(7, 16),
      Note.TwelveTET.instance().getFreq(5, 16),
      Note.TwelveTET.instance().getFreq(4, 16),
      Note.TwelveTET.instance().getFreq(7, 8),
      Note.TwelveTET.instance().getFreq(0, 16),
      Note.TwelveTET.instance().getFreq(5, 8),
      Note.TwelveTET.instance().getFreq(0, 16),
      Note.TwelveTET.instance().getFreq(0, 8)};

  public ChocolateStout() {
    super(master);
    Patch patcher1 = new Patch();
    chord1 = new Chord(Square.class, "maj", 440d);
    patcher1.add(chord1);
    patcher1.add(new Gain(.04));

    Patch patcher2 = new Patch();
    chord2 = new Chord(Triangle.class, "5", 440d);
    patcher2.add(chord2);
    patcher2.add(new Gain(.065));

    Patch patcher3 = new Patch();
    chord3 = new Chord(Saw.class, "maj 7", 440d);
    patcher3.add(chord3);
    patcher3.add(new Gain(.03));

    Patch patcher4 = new Patch();
    tone1 = new Sine();
    patcher4.add(tone1);
    patcher4.add(new Gain(.035));

    Patch patcher5 = new Patch();
    tone2 = new Saw();
    patcher5.add(tone2);
    patcher5.add(new Gain(.035));

    Patch patcher6 = new Patch();
    tone3 = new Sine();
    patcher6.add(tone3);
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
    cur = getCurTimeInMs(sampleRate);
    if (cur >= 0 && (Math.abs(cur - start) >= len) && (cur != start || cur == 0)) {

      start = cur;
      double base = 0;
      double deviation = rand.nextDouble() * .0001;
      if (true) {
        double noteChange = rand.nextDouble();
        if (noteChange < .99) {
          base = loFreqs[(int) Math.rint(rand.nextDouble() * ((double) loFreqs.length - 1))];
          chord1.setChord((deviation + 1.) * base * getRoot());
        }
        if (noteChange < .6) {
          base = loFreqs[(int) Math.rint(rand.nextDouble() * ((double) loFreqs.length - 1))];
          tone1.setFreq((deviation + 1.) * base * getRoot());
        }
        if (noteChange < .8) {
          base = hiFreqs[(int) Math.rint(rand.nextDouble() * ((double) hiFreqs.length - 1))];
          chord2.setChord((deviation + 1.) * base * getRoot());
        }
        if (noteChange < .3) {
          base = hiFreqs[(int) Math.rint(rand.nextDouble() * ((double) hiFreqs.length - 1))];

          tone2.setFreq((deviation + 1.) * base * getRoot());
        }
        if (noteChange < .5) {
          base = hiFreqs2[(int) Math.rint(rand.nextDouble() * ((double) hiFreqs2.length - 1))];
          chord3.setChord((deviation + 1.) * base * getRoot());
        }
        if (noteChange < .2) {
          base = hiFreqs2[(int) Math.rint(rand.nextDouble() * ((double) hiFreqs2.length - 1))];

          tone3.setFreq((deviation + 1.) * base * getRoot());
        }
      }
      double factor = 1000 * rand.nextDouble();
      isWild = rand.nextDouble() < .2;
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

}
