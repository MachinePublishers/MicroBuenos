/*
 * Copyright 2023 MicroBuenos committers
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
package com.machinepublishers.microbuenos.conductor;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Prices {

  private static final Random rand = new SecureRandom();
  private final int bufferLen;
  private final int[][] training;
  private final int[][] alt;
  private final int[] oddsTraining;
  private final int[] oddsAlt;

  public Prices(int bufferLen) {
    this.bufferLen = bufferLen;
    new File("./prices/training").mkdirs();
    new File("./prices/alt").mkdirs();
    for (File file : Objects.requireNonNull(
        new File("./prices/training").listFiles((dir, name) -> !name.startsWith(".")))) {
      for (File f : Objects.requireNonNull(file.listFiles((dir, name) -> !name.startsWith(".")))) {
        process(Integer.parseInt(file.getName()), f);
      }
    }
    for (File file : Objects.requireNonNull(
        new File("./prices/alt").listFiles((dir, name) -> !name.startsWith(".")))) {
      for (File f : Objects.requireNonNull(file.listFiles((dir, name) -> !name.startsWith(".")))) {
        process(Integer.parseInt(file.getName()), f);
      }
    }
    List<int[]> data = Stream.of(new File("./prices/training-dist").listFiles())
        .filter(f -> !f.getName().startsWith(".")).map(Prices::load).collect(Collectors.toList());
    training = data.toArray(new int[0][]);
    data = Stream.of(new File("./prices/alt-dist").listFiles())
        .filter(f -> !f.getName().startsWith(".")).map(Prices::load).collect(Collectors.toList());
    alt = data.toArray(new int[0][]);

    oddsTraining = initOdds(training);
    oddsAlt = initOdds(alt);
  }

  private static int[] initOdds(int[][] data) {
    int[] odds = new int[100_000];
    double k =
        (double) odds.length / (double) Stream.of(data).map(a -> a.length).reduce(0, Integer::sum);
    int pos = 0;
    for (int i = 0; i < data.length; i++) {
      int chances = (int) Math.rint((double) data[i].length * k);
      if (chances < 1) {
        throw new IllegalStateException();
      }
      for (int j = 0; j < chances && pos < odds.length; j++, pos++) {
        odds[pos] = i;
      }
    }
    return Arrays.copyOf(odds, pos);
  }

  private static void process(int priceCol, File file) {
    File dist = new File(file.getParentFile().getParentFile() + "-dist", file.getName());
    if (!dist.exists()) {
      List<Integer> prices = new ArrayList<>();
      try (BufferedReader reader = new BufferedReader(
          new InputStreamReader(new FileInputStream(file)))) {
        reader.readLine();
        double prevPrice = -1;
        for (String line = reader.readLine(); line != null; line = reader.readLine()) {
          String[] tokens = line.split(",");
          double priceTmp = Double.parseDouble(tokens[priceCol]);
          if (Double.isFinite(priceTmp)) {
            priceTmp = Math.rint(priceTmp * 100d);
            if (priceTmp > Integer.MAX_VALUE || priceTmp < Integer.MIN_VALUE) {
              throw new IllegalStateException(
                  String.format("Price overflow '%s' (%f) in %s", tokens[priceCol], priceTmp,
                      file.getAbsolutePath()));
            }
            if (priceTmp == 0) {
              throw new IllegalStateException(
                  String.format("Unusual price in %s: %s", file.getAbsolutePath(), line));
            }
            if (prevPrice != -1 && Math.abs(prevPrice - priceTmp) / priceTmp > .5) {
              throw new IllegalStateException(
                  String.format("Unusual price change in %s: %s", file.getAbsolutePath(), line));
            }
            prevPrice = priceTmp;
            prices.add((int) priceTmp);
          }
        }
        int[] data = new int[prices.size()];
        for (int i = 0; i < prices.size(); i++) {
          data[i] = prices.get(i);
        }
        dist.getParentFile().mkdirs();
        try (ByteArrayOutputStream bytesOut = new ByteArrayOutputStream(); DataOutputStream dataOut = new DataOutputStream(
            bytesOut); FileOutputStream fileOut = new FileOutputStream(dist, false)) {
          for (int i = 0; i < data.length; i++) {
            dataOut.writeInt(data[i]);
          }
          fileOut.write(bytesOut.toByteArray());
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static int[] load(File file) {
    List<Integer> data = new ArrayList<>();
    try (FileInputStream fileIn = new FileInputStream(
        file); ByteArrayInputStream bytesIn = new ByteArrayInputStream(
        fileIn.readAllBytes()); DataInputStream dataIn = new DataInputStream(bytesIn)) {
      while (true) {
        data.add(dataIn.readInt());
      }
    } catch (EOFException e) {
      return data.stream().mapToInt(i -> i).toArray();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  public Marker rand(boolean training) {
    int[][] data = training ? this.training : this.alt;
    int[] odds = training ? this.oddsTraining : this.oddsAlt;
    int dataset = odds[rand.nextInt(odds.length)];
    int offset = rand.nextInt(data[dataset].length - bufferLen - 1);
    return new Marker(training, dataset, offset);
  }

  public int[] getData(Marker marker) {
    int[][] data = marker.training() ? this.training : this.alt;
    return data[marker.dataset()];
  }

  public record Marker(boolean training, int dataset, int offset) implements Serializable {

  }
}