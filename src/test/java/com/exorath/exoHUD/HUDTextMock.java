/*
 * Copyright 2017 Exorath
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

package com.exorath.exoHUD;


import io.reactivex.Observable;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by toonsev on 9/25/2016.
 */
public class HUDTextMock {
    private HUDText mocked = mock(HUDText.class);

    public HUDTextMock(TextComponent... components){
        setComponents(components);
    }
    public HUDTextMock(String... componentStrings){
        List<TextComponent> textComponents = Arrays.asList(componentStrings).stream().map(componentString -> new TextComponent(componentString)).collect(Collectors.toList());
        setComponents(textComponents.toArray(new TextComponent[textComponents.size()]));
    }


    public void setComponents(TextComponent... components){
        when(mocked.getTextObservable()).thenAnswer(invocation -> Observable.just(Arrays.asList(components)));
    }
    public HUDText getMock(){
        return mocked;
    }
}
