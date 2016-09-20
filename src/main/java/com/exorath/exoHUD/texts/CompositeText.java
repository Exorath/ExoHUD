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

package com.exorath.exoHUD.texts;

import com.exorath.exoHUD.HUDText;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by toonsev on 9/7/2016.
 */
public class CompositeText extends CompositeTextBase {
    public CompositeText(HUDText... hudTexts){
        super(hudTexts);
    }
    @Override
    public Observable<List<TextComponent>> getTextObservable() {
        //Check
        return Observable.combineLatest(getCompositeObservables(), new Function<Object[], List<TextComponent>>() {
            @Override
            public List<TextComponent> apply(Object[] componentLists) throws Exception {
                return getComposite(componentLists);
            }
        });
    }

    protected static List<TextComponent> getComposite(Object... componentListObjects) {
        List<TextComponent> composite = new ArrayList<>();
        List<TextComponent>[] componentLists = Arrays.copyOf(componentListObjects, componentListObjects.length, List[].class);
        Arrays.asList(componentLists).forEach((componentList) -> composite.addAll(componentList));
        return composite;
    }

    public static final CompositeText composite(HUDText... texts) {
        return new CompositeText(texts);
    }
}
