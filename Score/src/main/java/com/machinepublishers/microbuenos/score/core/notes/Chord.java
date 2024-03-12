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
package com.machinepublishers.microbuenos.score.core.notes;

import com.machinepublishers.microbuenos.score.core.UnitGenerator;
import com.machinepublishers.microbuenos.score.core.gen.Tone;

public class Chord extends UnitGenerator {

  private Tone[] tones = null;
  private String chordName = null;

  public Chord(Class<? extends Tone> tone, String chordName, double rootFreq) {
    setChord(tone, chordName, rootFreq);
  }

  public synchronized void setChord(double rootFreq) {
    setChord(null, null, rootFreq);
  }

  public synchronized void setChord(String chordName, double rootFreq) {
    setChord(null, chordName, rootFreq);
  }

  @Override
  public void next(double offset, double[] inputVal, double sampleRate) {
    super.next(offset, inputVal, sampleRate);
    double val = 0;

    for (int i = 0; i < tones.length; i++) {
      tones[i].next(offset, inputVal, sampleRate);
      double d = 0;
      for (int j = 0; j < inputVal.length; j++) {
        d += inputVal[j];
      }
      val += d / inputVal.length;
    }
    val /= tones.length;

    for (int i = 0; i < inputVal.length; i++) {
      inputVal[i] = val;
    }
  }

  private void setChord(Class<? extends Tone> tone, String chordName, double rootFreq) {
    if (chordName != null) {
      this.chordName = chordName;
    }
    double[] freqs = Note.TwelveTET.instance()
        .getFreqs(this.chordName, rootFreq);
    if (tone != null) {
      tones = new Tone[freqs.length];
    }
    for (int i = 0; i < freqs.length; i++) {
      try {
        if (tone != null) {
          tones[i] = tone.newInstance();
        }
        tones[i].setFreq(freqs[i]); //TODO set freq by force?
      } catch (InstantiationException e) {
        //TODO
      } catch (IllegalAccessException e) {
        //TODO
      }
    }
  }
}
