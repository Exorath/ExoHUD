/*
 * Copyright 2016 Exorath
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package com.exorath.exoHUD.api.texts;

import com.exorath.exoHUD.api.HUDText;
import io.reactivex.Observable;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by toonsev on 9/7/2016.
 */
public class PlainText implements HUDText {
    private List<TextComponent> components;

    public PlainText(String text) {
        this(new TextComponent(text));
    }

    public PlainText(TextComponent component) {
        this(Arrays.asList(component));
    }

    public PlainText(List<TextComponent> components) {
        this.components = components;
    }

    @Override
    public Observable<List<TextComponent>> getTextObservable() {
        return Observable.just(components);
    }

    public static final PlainText plain(String text) {
        return new PlainText(text);
    }

    public static final PlainText component(TextComponent textComponent) {
        return new PlainText(textComponent);
    }
    public static final PlainText components(List<TextComponent> textComponents) {
        return new PlainText(textComponents);
    }
}
