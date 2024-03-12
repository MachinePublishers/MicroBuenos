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

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;

public class Play extends UnitGenerator implements Runnable {

  private static final double SAMP_SIZE = 2.;
  private static final double BITS_PER_BYTE = 8.;
  private static final double GAIN = 3;
  private static final double MAX =
      ((Math.rint(Math.pow(2., SAMP_SIZE * BITS_PER_BYTE - 1))) - 1.) * GAIN;
  private static final double MIN =
      ((Math.rint(Math.pow(2., SAMP_SIZE * BITS_PER_BYTE - 1))) * (-1.)) * GAIN;
  private static final byte[] convertedDouble = new byte[(int) SAMP_SIZE];
  protected final Segment seg;

  public Play(Segment seg) {
    this.seg = seg;
  }

  private static byte[] getBytes(double p_dSample) {

    double dSample = Math.min(MAX, Math.max(MIN, p_dSample));
    int nSample = (int) Math.rint(dSample);
    for (int i = 0; i < SAMP_SIZE; i++) {
      convertedDouble[i] = (byte) ((nSample >> ((int) BITS_PER_BYTE * ((int) SAMP_SIZE - 1 - i)))
          & 0xFFFFFFFF);
    }
    return convertedDouble;
  }

  @Override
  public void next(double offset, double[] inputVal, double sampleRate) {
    super.next(offset, inputVal, sampleRate);
    seg.next(offset, inputVal, sampleRate); //let them get the prev. sample
  }

  public final void run() {
    final double SAMP_RATE = 44100.;
    final long BUF_MS = 150;
    final double BUF_LEN = BUF_MS / 1000. * SAMP_RATE;
    final double CHANNELS = 2.;
    final AudioFormat af = new AudioFormat((float) SAMP_RATE,
        (int) (SAMP_SIZE * BITS_PER_BYTE),
        (int) CHANNELS,
        true,
        true);
    final DataLine.Info info = new DataLine.Info(SourceDataLine.class, af);
    final byte[] buf = new byte[(int) (BUF_LEN * SAMP_SIZE * CHANNELS)];
    SourceDataLine source = null;
    try {
      source = (SourceDataLine) AudioSystem.getLine(info);
      source.open(af, (int) (BUF_LEN * CHANNELS * SAMP_SIZE));
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }

    source.start();
    double[] input = new double[(int) CHANNELS];
    double[] output = new double[(int) CHANNELS];

    while (!seg.isEnded(SAMP_RATE)) {
      for (int cur = 0; cur < buf.length; ) {
        this.next(1, input, SAMP_RATE);
        for (int i = 0; i < input.length; i++) {
          output[i] = input[i] * MAX;
        }
        for (int chan = 0; chan < CHANNELS; chan++) {
          byte[] bytes = getBytes(output[chan]);
          for (int i = 0; i < SAMP_SIZE; i++) {
            buf[cur++] = bytes[i];
          }
        }
      }

      int pos = 0;
      while (pos < buf.length) {
        pos = source.write(buf, pos, buf.length - pos);
      }
    }

    source.drain();
    source.stop();
    source.close();
  }
}
