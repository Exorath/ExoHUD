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

package com.exorath.exoHUD.texts;

import net.md_5.bungee.api.chat.TextComponent;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by toonsev on 9/11/2016.
 */
public class PlainTextTest {
    private static final TextComponent component1 = new TextComponent("comp1");
    private static final TextComponent component2 = new TextComponent("comp2");
    private static final String TEXT = "test!";

    private PlainText plainTextFromText;
    private PlainText plainTextFromComponent;
    private PlainText plainTextFromComponents;

    @Before
    public void setup(){
        this.plainTextFromText = new PlainText(TEXT);
        this.plainTextFromComponent = new PlainText(component1);
        this.plainTextFromComponents = new PlainText(Arrays.asList(component1, component2));
    }

    //text
    @Test
    public void plainTextFromTextObservableEmitsOneTime(){
        plainTextFromText.getTextObservable().test().assertValueCount(1);
    }
    @Test
    public void plainTextFromTextObservableCompletes(){
        plainTextFromText.getTextObservable().test().assertComplete();
    }

    @Test
    public void plainTextFromTextObservableEmitsListWithSingleElement(){
        List<TextComponent> textComponents = new ArrayList<>();
        textComponents.add(new TextComponent(TEXT));
        assertEquals(1, plainTextFromText.getTextObservable().blockingFirst().size());
    }

    @Test
    public void plainTextFromTextObservableEmitsListContainingTextComponentWithText(){
        assertEquals(TEXT, plainTextFromText.getTextObservable().blockingFirst().get(0).getText());
    }

    //Component
    @Test
    public void plainTextFromComponentObservableEmitsOneTime(){
        plainTextFromComponent.getTextObservable().test().assertValueCount(1);
    }
    @Test
    public void plainTextFromComponentObservableCompletes(){
        plainTextFromComponent.getTextObservable().test().assertComplete();
    }

    @Test
    public void plainTextFromComponentObservableEmitsListWithSingleElement(){
        assertEquals(1, plainTextFromComponent.getTextObservable().blockingFirst().size());
    }

    @Test
    public void plainTextFromComponentObservableEmitsListContainingTextComponentWithText(){
        assertEquals(Arrays.asList(component1), plainTextFromComponent.getTextObservable().blockingFirst());
    }
    //Components

    @Test
    public void plainTextFromComponentsObservableEmitsOneTime(){
        plainTextFromComponents.getTextObservable().test().assertValueCount(1);
    }
    @Test
    public void plainTextFromComponentsObservableCompletes(){
        plainTextFromComponents.getTextObservable().test().assertComplete();
    }

    @Test
    public void plainTextFromComponentsObservableEmitsListWithTwoElements(){
        assertEquals(2, plainTextFromComponents.getTextObservable().blockingFirst().size());
    }

    @Test
    public void plainTextFromComponentsObservableEmitsListContainingTextComponentWithText(){
        assertEquals(Arrays.asList(component1, component2), plainTextFromComponents.getTextObservable().blockingFirst());
    }
}
