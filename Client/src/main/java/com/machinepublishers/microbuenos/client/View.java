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

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class View implements ApplicationListener {

  private Skin skin;
  private Stage stage;

  @Override
  public void create() {
    skin = new Skin(Gdx.files.internal("skin-holo-dark-hdpi/Holo-dark-hdpi.json"));

    stage = new Stage(new ScreenViewport());
    Gdx.input.setInputProcessor(stage);

    TextButton textButton = new TextButton("Hello", skin);
    stage.addActor(textButton);
  }

  @Override
  public void render() {
    Gdx.gl.glClearColor(.9f, .9f, .9f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.act();
    stage.draw();
  }

  @Override
  public void pause() {

  }

  @Override
  public void resume() {

  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void dispose() {
    skin.dispose();
    stage.dispose();
  }
}