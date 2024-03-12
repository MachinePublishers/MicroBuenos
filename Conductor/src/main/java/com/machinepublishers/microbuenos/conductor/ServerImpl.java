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

import com.machinepublishers.microbuenos.conductor.Prices.Marker;
import java.rmi.RemoteException;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ServerImpl implements Server {

  private static final Random rand = new SecureRandom();
  private final Prices prices;
  private final Map<Long, Marker> markers = new HashMap<>();
  private Marker marker1 = null;
  private Marker marker2 = null;

  public ServerImpl(Prices prices) {
    this.prices = prices;
  }

  @Override
  public void upload(String key, NeuralNet cur) throws RemoteException {
    if (Main.getKey().equals(key)) {
      cur.save();
    }
  }

  @Override
  public NeuralNet download(String key, int indexFrom, int indexTo) throws RemoteException {
    return Main.getKey().equals(key) ? NeuralNet.create(indexFrom, indexTo) : null;
  }

  @Override
  public Marker randPriceMarker(String key, boolean training, long sync) throws RemoteException {
    if (Main.getKey().equals(key)) {
      synchronized (ServerImpl.class) {
        if (markers.containsKey(sync)) {
          return markers.get(sync);
        }
        Marker toRemove = marker2;
        marker2 = marker1;
        marker1 = prices.rand(training);
        if (toRemove != null) {
          markers.remove(toRemove);
        }
        markers.put(sync, marker1);
        return marker1;
      }
    }
    return null;
  }
}