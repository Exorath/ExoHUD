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
import java.util.stream.Collectors;

/**
 * Created by toonsev on 9/13/2016.
 */
public abstract class CompositeTextBase implements HUDText {
    private List<HUDText> hudTexts;

    public CompositeTextBase(HUDText... hudTexts){
        this.hudTexts = Arrays.asList(hudTexts);
    }

    public CompositeTextBase(List<HUDText> hudTexts){
        this.hudTexts = hudTexts;
    }
    public List<HUDText> getHudTexts() {
        return hudTexts;
    }

    public List<Observable<List<TextComponent>>> getCompositeObservables() {
        return hudTexts.stream().map(hudText -> hudText.getTextObservable()).collect(Collectors.toList());
    }
}
