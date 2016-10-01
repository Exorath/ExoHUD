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

package com.exorath.exoHUD.locations;

import com.exorath.exoHUD.*;
import io.reactivex.Completable;
import io.reactivex.schedulers.TestScheduler;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by toonsev on 9/25/2016.
 */
public class SimpleLocationTest {
    private List<DisplayPackage> onDisplayCalls, onDisplayRemoveCalls;
    private List<Boolean> onHideCalls;
    private SimpleLocation simpleLocation;

    @Before
    public void setup() {
        onDisplayCalls = new ArrayList<>();
        onDisplayRemoveCalls = new ArrayList<>();
        onHideCalls = new ArrayList<>();

        simpleLocation = new SimpleLocation() {
            @Override
            public void onDisplay(DisplayPackage displayPackage) {
                onDisplayCalls.add(displayPackage);
            }

            @Override
            public void onDisplayRemove(DisplayPackage displayPackage) {
                onDisplayRemoveCalls.add(displayPackage);
            }

            @Override
            public void onHide(boolean hidden) {
                onHideCalls.add(hidden);
            }
        };
    }

    //hidden

    @Test
    public void isHiddenReturnsFalseByDefaultTest() {
        assertFalse(simpleLocation.isHidden());
    }

    @Test
    public void setHiddenTrueCallsOnHideMethodOnceTest() {
        simpleLocation.setHidden(true);
        assertEquals(1, onHideCalls.size());
    }

    @Test
    public void setHiddenTrueCallsOnHideTrueMethodTest() {
        simpleLocation.setHidden(true);
        assertTrue(onHideCalls.get(0));
    }

    @Test
    public void setHiddenFalseCallsOnHideMethodOnceTest() {
        simpleLocation.setHidden(true);
        assertEquals(1, onHideCalls.size());
    }

    @Test
    public void setHiddenFalseCallsOnHideFalseMethodTest() {
        simpleLocation.setHidden(true);
        assertTrue(onHideCalls.get(0));
    }

    @Test
    public void HiddenReturnsTrueWhenSetHiddenTrueCalledTest() {
        simpleLocation.setHidden(true);
        assertTrue(simpleLocation.isHidden());
    }

    @Test
    public void HiddenReturnsFalseWhenSetHiddenFalseCalledTest() {
        simpleLocation.setHidden(false);
        assertFalse(simpleLocation.isHidden());
    }

    @Test
    public void addDisplayDoesNotCallOnDisplayWhenSetHiddenTrueCalledTest() {
        simpleLocation.setHidden(true);
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0.9d, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);

        assertEquals(0, onDisplayCalls.size());
    }

    @Test

    public void addDisplayDoesNotCallOnDisplayWhenSetHiddenFalseCalledTest() {
        simpleLocation.setHidden(false);
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0.9d, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);

        assertEquals(1, onDisplayCalls.size());
    }

    @Test
    public void addDisplayDoesNotCallOnDisplayWhenThresholdIsHigherThenDisplayedPackage() {
        simpleLocation.setHideThreshold(1d);
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0.9d, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);

        assertEquals(0, onDisplayCalls.size());
    }

    @Test
    public void addDisplayCallsOnDisplayWhenThresholdIsLowerThenDisplayedPackage() {
        simpleLocation.setHideThreshold(0.9d);
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(1d, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);

        assertEquals(1, onDisplayCalls.size());
    }

    //DisplayPackage
    @Test
    public void addDisplayPackageCallsOnDisplayOnceTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);

        assertEquals(1, onDisplayCalls.size());
    }

    @Test
    public void addDisplayPackageCallsOnDisplayWithSameDisplayPackageParamTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);

        assertEquals(displayPackage, onDisplayCalls.get(0));
    }

    @Test
    public void addDisplayPackageRemoveDisplayPackageOnDisplayRemoveCalledOnceTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);
        simpleLocation.removeDisplayPackage(displayPackage);

        assertEquals(1, onDisplayRemoveCalls.size());
    }

    @Test
    public void addDisplayPackageRemoveDisplayPackageOnDisplayRemoveSameDisplayPackageTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);
        simpleLocation.removeDisplayPackage(displayPackage);

        assertEquals(displayPackage, onDisplayRemoveCalls.get(0));
    }

    @Test
    public void removeDisplayPackageWithNotAddedPackageReturnsFalseTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        boolean returned = simpleLocation.removeDisplayPackage(displayPackage);
        assertFalse(returned);
    }

    @Test
    public void removeDisplayPackageWithNotAddedPackageDoesNotCallOnDisplayRemoveTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        simpleLocation.removeDisplayPackage(displayPackage);
        assertEquals(0, onDisplayRemoveCalls.size());
    }
    //Text

    @Test
    public void addTextCallsOnDisplayOnceTest() {
        HUDText hudText = new HUDTextMock("test1", "test2").getMock();
        DisplayProperties properties = DisplayProperties.create(0, () -> Completable.never());
        simpleLocation.addText(hudText, properties);

        assertEquals(1, onDisplayCalls.size());
    }

    @Test
    public void addTextReturnsDisplayPackageWithOneHUDTextTest() {
        HUDText hudText = new HUDTextMock("test1", "test2").getMock();
        DisplayProperties properties = DisplayProperties.create(0, () -> Completable.never());
        DisplayPackage displayPackage = simpleLocation.addText(hudText, properties);

        assertEquals(1, displayPackage.getHudPackage().getTexts().size());
    }

    @Test
    public void addTextReturnsDisplayPackageWithSameHUDTextTest() {
        HUDText hudText = new HUDTextMock("test1", "test2").getMock();
        DisplayProperties properties = DisplayProperties.create(0, () -> Completable.never());
        DisplayPackage displayPackage = simpleLocation.addText(hudText, properties);

        assertEquals(hudText, displayPackage.getHudPackage().getTexts().get(0));
    }

    @Test
    public void addTextReturnsDisplayPackageWithSameHUDPropertiesTest() {
        HUDText hudText = new HUDTextMock("test1", "test2").getMock();
        DisplayProperties properties = DisplayProperties.create(0, () -> Completable.never());
        DisplayPackage displayPackage = simpleLocation.addText(hudText, properties);

        assertEquals(properties, displayPackage.getProperties());
    }


    @Test
    public void removeTextCallsOnDisplayRemoveOnceTest() {
        HUDText hudText = new HUDTextMock("test1", "test2").getMock();
        DisplayProperties properties = DisplayProperties.create(0, () -> Completable.never());
        DisplayPackage displayPackage = simpleLocation.addText(hudText, properties);
        simpleLocation.removeDisplayPackage(displayPackage);

        assertEquals(1, onDisplayRemoveCalls.size());
    }

    @Test
    public void removeTextCallsOnDisplayRemoveWithReturnedDisplayPackageTest() {
        HUDText hudText = new HUDTextMock("test1", "test2").getMock();
        DisplayProperties properties = DisplayProperties.create(0, () -> Completable.never());
        DisplayPackage displayPackage = simpleLocation.addText(hudText, properties);
        simpleLocation.removeDisplayPackage(displayPackage);

        assertEquals(displayPackage, onDisplayRemoveCalls.get(0));
    }

    //Removeable test
    @Test
    public void addDisplayWithInstantRemoveableCallsOnDisplayRemoveOnceTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.complete()));
        simpleLocation.addDisplayPackage(displayPackage);
        assertEquals(1, onDisplayRemoveCalls.size());
    }

    //Removeable test
    @Test
    public void addDisplayWithInstantRemoveableCallsOnDisplayRemoveWithDisplayPackageParameterTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.complete()));
        simpleLocation.addDisplayPackage(displayPackage);
        assertEquals(displayPackage, onDisplayRemoveCalls.get(0));
    }

    @Test
    public void addDisplayWithNeverRemoveableDoesNotCallOnDisplayRemoveTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);
        assertEquals(0, onDisplayRemoveCalls.size());
    }

    @Test
    public void addDisplayWithTimedRemoveableDoesNotCallOnDisplayRemoveWhileTimeNotExpiredTest() {
        TestScheduler scheduler = new TestScheduler();
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.timer(2, TimeUnit.HOURS, scheduler)));
        simpleLocation.addDisplayPackage(displayPackage);

        scheduler.advanceTimeBy(1, TimeUnit.HOURS);

        assertEquals(0, onDisplayRemoveCalls.size());
    }

    @Test
    public void addDisplayWithTimedRemoveableCallsOnDisplayRemoveWhenTimeExpiredOnceTest() {
        TestScheduler scheduler = new TestScheduler();
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.timer(2, TimeUnit.HOURS, scheduler)));
        simpleLocation.addDisplayPackage(displayPackage);

        scheduler.advanceTimeBy(2, TimeUnit.HOURS);

        assertEquals(1, onDisplayRemoveCalls.size());
    }

    @Test
    public void addDisplayWithTimedRemoveableCallsOnDisplayRemoveWhenTimeExpiredWithDisplayPackageParameterTest() {
        TestScheduler scheduler = new TestScheduler();
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.timer(2, TimeUnit.HOURS, scheduler)));
        simpleLocation.addDisplayPackage(displayPackage);

        scheduler.advanceTimeBy(2, TimeUnit.HOURS);

        assertEquals(displayPackage, onDisplayRemoveCalls.get(0));
    }


    @Test
    public void onDisplayRemoveCalledOnceWhenDisplayPackageAddedAndDisplayPackageAddedWithHigherPriorityTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        DisplayPackage displayPackage2 = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(1, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);
        simpleLocation.addDisplayPackage(displayPackage2);


        assertEquals(1, onDisplayRemoveCalls.size());
    }
    @Test
    public void onDisplayRemoveCalledWithFirstDisplayPackageWhenDisplayPackageAddedAndDisplayPackageAddedWithHigherPriorityTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        DisplayPackage displayPackage2 = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(1, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);
        simpleLocation.addDisplayPackage(displayPackage2);


        assertEquals(displayPackage, onDisplayRemoveCalls.get(0));
    }
    @Test
    public void onDisplayRemoveNotCalledWhenDisplayPackageAddedAndDisplayPackageAddedWithLowerPriorityTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        DisplayPackage displayPackage2 = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(-0.1, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);
        simpleLocation.addDisplayPackage(displayPackage2);


        assertEquals(0, onDisplayRemoveCalls.size());
    }

    @Test
    public void onDisplayRemoveNotCalledWhenDisplayPackageAddedAndDisplayPackageAddedWithSamePriorityTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        DisplayPackage displayPackage2 = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);
        simpleLocation.addDisplayPackage(displayPackage2);


        assertEquals(0, onDisplayRemoveCalls.size());
    }

    @Test
    public void onDisplayCalledTwiceWhenDisplayPackageAddedAndDisplayPackageAddedWithHigherPriorityTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        DisplayPackage displayPackage2 = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(1, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);
        simpleLocation.addDisplayPackage(displayPackage2);


        assertEquals(2, onDisplayCalls.size());
    }

    @Test
    public void onDisplaySecondCallArgumentEqualsSecondDisplayPackageWhenDisplayPackageAddedAndDisplayPackageAddedWithHigherPriorityTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        DisplayPackage displayPackage2 = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(1, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);
        simpleLocation.addDisplayPackage(displayPackage2);


        assertEquals(displayPackage2, onDisplayCalls.get(1));
    }


    @Test
    public void onDisplayCalledOnceWhenDisplayPackageAddedAndDisplayPackageAddedWithLowerPriorityTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        DisplayPackage displayPackage2 = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(-0.1, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);
        simpleLocation.addDisplayPackage(displayPackage2);


        assertEquals(1, onDisplayCalls.size());
    }

    @Test
    public void onDisplayCalledOnceWhenDisplayPackageAddedAndDisplayPackageAddedWithSamePriorityTest() {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        DisplayPackage displayPackage2 = new DisplayPackage(HUDPackage.create(new ArrayList<>()), DisplayProperties.create(0, () -> Completable.never()));
        simpleLocation.addDisplayPackage(displayPackage);
        simpleLocation.addDisplayPackage(displayPackage2);


        assertEquals(1, onDisplayCalls.size());
    }
}
