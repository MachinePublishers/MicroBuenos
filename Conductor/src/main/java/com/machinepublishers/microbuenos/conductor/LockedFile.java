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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.charset.StandardCharsets;

public class LockedFile {

  public static String read(File f) {
    synchronized (f.getName().intern()) {
      try (RandomAccessFile file = new RandomAccessFile(f,
          "rws"); FileChannel channel = file.getChannel(); FileLock ignored = channel.lock()) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(512);
        StringBuilder builder = new StringBuilder();
        while (channel.read(byteBuffer) != -1) {
          byteBuffer.rewind();
          builder.append(StandardCharsets.UTF_8.decode(byteBuffer));
          byteBuffer.flip();
        }
        if (builder.isEmpty()) {
          throw new RuntimeException("Empty " + f.getAbsolutePath());
        }
        return builder.toString();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void write(File f, String string) {
    synchronized (f.getName().intern()) {
      try (RandomAccessFile file = new RandomAccessFile(f,
          "rws"); FileChannel channel = file.getChannel(); FileLock ignored = channel.lock()) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(string.getBytes(StandardCharsets.UTF_8));
        while (byteBuffer.hasRemaining()) {
          channel.write(byteBuffer);
        }
        file.setLength(byteBuffer.position());
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static boolean exists(File f) {
    synchronized (f.getName().intern()) {
      try (RandomAccessFile file = new RandomAccessFile(f,
          "rws"); FileChannel channel = file.getChannel(); FileLock ignored = channel.lock()) {
        return channel.size() > 0;
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }
}