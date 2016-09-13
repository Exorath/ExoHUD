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
import io.reactivex.observers.TestObserver;
import io.reactivex.schedulers.TestScheduler;
import net.md_5.bungee.api.chat.TextComponent;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by toonsev on 9/13/2016.
 */
public class IterateTextTest {
    private static final int INTERVAL = 10;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private TestScheduler scheduler;

    private static final String TEXT1 = "sample1";
    private static final String TEXT2 = "sample2";
    private TextComponent component1;
    private TextComponent component2;
    private HUDText hudText1;
    private HUDText hudText2;
    private IterateText iterateText;

    @Before
    public void setup(){
        scheduler = new TestScheduler();
        component1 = new TextComponent(TEXT1);
        hudText1 = mock(HUDText.class);
        when(hudText1.getTextObservable()).thenReturn(Observable.just(Arrays.asList(component1)));

        hudText2 = mock(HUDText.class);
        component2 = new TextComponent(TEXT2);
        when(hudText2.getTextObservable()).thenReturn(Observable.just(Arrays.asList(component2)));

        this.iterateText = new IterateText(INTERVAL, TIME_UNIT, scheduler, hudText1, hudText2);
    }

    @Test
    public void observerDoesNotTerminateTest(){
        TestObserver<List<TextComponent>> observer = iterateText.getTextObservable().test();
        scheduler.advanceTimeBy(1, TimeUnit.HOURS);
        observer.assertNotTerminated();
    }
    @Test
    public void observerEmitsOneItemAfterOneMicroSecondTest(){
        TestObserver<List<TextComponent>> observer = iterateText.getTextObservable().test();
        scheduler.advanceTimeBy(1, TimeUnit.MICROSECONDS);
        observer.assertValueCount(1);
    }
    @Test
    public void observerEmitsFirstComponentAfterOneMicroSecondTest(){
        TestObserver<List<TextComponent>> observer = iterateText.getTextObservable().test();
        scheduler.advanceTimeBy(1, TimeUnit.MICROSECONDS);
        observer.assertValue(Arrays.asList(component1));
    }

    @Test
    public void observerEmitsTwoItemsAfterOneIntervalTest(){
        TestObserver<List<TextComponent>> observer = iterateText.getTextObservable().test();
        scheduler.advanceTimeBy(INTERVAL, TIME_UNIT);
        observer.assertValueCount(2);
    }

    @Test
    public void observerEmitsFirstAndSecondComponentAfterOneIntervalTest(){
        TestObserver<List<TextComponent>> observer = iterateText.getTextObservable().test();
        scheduler.advanceTimeBy(INTERVAL, TIME_UNIT);
        observer.assertValueSequence(Arrays.asList(Arrays.asList(component1), Arrays.asList(component2)));
    }

    @Test
    public void observerEmitsThreeItemsAfterTwoIntervalTest(){
        TestObserver<List<TextComponent>> observer = iterateText.getTextObservable().test();
        scheduler.advanceTimeBy(INTERVAL * 2, TIME_UNIT);
        observer.assertValueCount(3);
    }

    @Test
    public void observerEmitsFirstSecondAndFirstComponentAfterTwoIntervalTest(){
        TestObserver<List<TextComponent>> observer = iterateText.getTextObservable().test();
        scheduler.advanceTimeBy(INTERVAL * 2, TIME_UNIT);
        observer.assertValueSequence(Arrays.asList(Arrays.asList(component1), Arrays.asList(component2),Arrays.asList(component1)));
    }
}
