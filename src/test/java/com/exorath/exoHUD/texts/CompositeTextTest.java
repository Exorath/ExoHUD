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
import net.md_5.bungee.api.chat.TextComponent;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by toonsev on 9/11/2016.
 */
public class CompositeTextTest {
    private static final String TEXT1 = "sampletext1";
    private static final String TEXT2 = "sampletext2";

    private TextComponent component1;
    private HUDText hudText1;
    private TextComponent component2;
    private HUDText hudText2;
    private CompositeText compositeText1c, compositeText2c;

    @Before
    public void setup(){
        component1 = new TextComponent(TEXT1);
        hudText1 = mock(HUDText.class);
        when(hudText1.getTextObservable()).thenReturn(Observable.just(Arrays.asList(component1)));

        component2 = new TextComponent(TEXT2);
        hudText2 = mock(HUDText.class);
        when(hudText2.getTextObservable()).thenReturn(Observable.just(Arrays.asList(component2)));

        compositeText1c = new CompositeText(hudText1);
        compositeText2c = new CompositeText(hudText1, hudText2);
    }

    //One value
    @Test
    public void compositeTextWithOneComponentCompletesTest(){
        compositeText1c.getTextObservable().test().assertComplete();
    }

    @Test
    public void compositeTextWithOneComponentEmitsOneValueTest(){
        compositeText1c.getTextObservable().test().assertValueCount(1);
    }

    @Test
    public void compositeTextWithOneComponentEmitsValueWithOneComponentTest(){
        assertEquals(1, compositeText1c.getTextObservable().blockingFirst().size());
    }

    @Test
    public void compositeTextWithOneComponentEmitsValueWithComponentTest(){
        assertEquals(component1, compositeText1c.getTextObservable().blockingFirst().get(0));
    }
    //Two components
    @Test
    public void compositeTextWithTwoComponentsCompletesTest(){
        compositeText2c.getTextObservable().test().assertComplete();
    }

    @Test
    public void compositeTextWithTwoComponentEmitsOneValueTest(){
        compositeText2c.getTextObservable().test().assertValueCount(1);
    }

    @Test
    public void compositeTextWithTwoComponentEmitsValueWithTwoComponentTest(){
        assertEquals(2, compositeText2c.getTextObservable().blockingFirst().size());
    }

    @Test
    public void compositeTextWithTwoComponentEmitsValueWithComponent1FirstTest(){
        assertEquals(component1, compositeText2c.getTextObservable().blockingFirst().get(0));
    }

    @Test
    public void compositeTextWithTwoComponentEmitsValueWithComponent2SecondTest(){
        assertEquals(component2, compositeText2c.getTextObservable().blockingFirst().get(1));
    }
}
