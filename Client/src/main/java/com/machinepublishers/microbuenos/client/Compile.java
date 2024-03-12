/*
 * Copyright 2022-2023 MicroBuenos committers
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
package com.machinepublishers.microbuenos.client;

import com.github.xpenatan.gdx.backends.teavm.TeaBuildConfiguration;
import com.github.xpenatan.gdx.backends.teavm.TeaBuilder;
import com.github.xpenatan.gdx.backends.web.gen.SkipClass;
import java.io.File;
import org.teavm.tooling.TeaVMTool;
import org.teavm.vm.TeaVMOptimizationLevel;

@SkipClass
public class Compile {

  public static void main(String[] args) {
    deleteDir(new File("../Server/src/main/resources/webapp"));
    TeaBuildConfiguration conf = new TeaBuildConfiguration();

    //the toolkit uses the path <webappPath>/webapp (../Server/src/main/resources/webapp)
    conf.webappPath = new File("../Server/src/main/resources").getAbsolutePath();
    conf.assetsPath.add(new File("./src/main/resources"));
    conf.obfuscate = true; //due to a bug, this is required to be true to work

    TeaVMTool tool = TeaBuilder.config(conf);
    tool.setMainClass(Main.class.getName());
    tool.setOptimizationLevel(TeaVMOptimizationLevel.ADVANCED);
    tool.setObfuscated(true);
    tool.setShortFileNames(true);
    tool.setSourceFilesCopied(false);
    tool.setStrict(true);
    tool.setSourceMapsFileGenerated(false);
    TeaBuilder.build(tool);
  }

  private static void deleteDir(File dir) {
    File[] files = dir.listFiles();
    if (files != null) {
      for (File file : files) {
        deleteDir(file);
      }
    }
    dir.delete();
  }
}
