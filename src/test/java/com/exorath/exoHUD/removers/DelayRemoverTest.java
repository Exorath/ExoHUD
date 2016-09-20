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

package com.exorath.exoHUD.removers;

import io.reactivex.schedulers.TestScheduler;
import io.reactivex.subscribers.TestSubscriber;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

/**
 * Created by toonsev on 9/17/2016.
 */
public class DelayRemoverTest {
    private static final long DELAY = 10;
    private static final TimeUnit TIME_UNIT = TimeUnit.SECONDS;
    private TestScheduler scheduler;
    private DelayRemover delayRemover, displayingDelayRemover;

    @Before
    public void setup() {
        scheduler = new TestScheduler();
        this.delayRemover = new DelayRemover(DELAY, TIME_UNIT, false, scheduler);
        this.displayingDelayRemover = new DelayRemover(DELAY, TIME_UNIT, true, scheduler);
    }

    @Test
    public void delayRemoverNotCompletedAtSubscriptionTest() {
        TestSubscriber subscriber = delayRemover.getRemoveCompletable().test();
        subscriber.assertNotComplete();
    }

    @Test
    public void delayRemoverCompletesAfterDelayTest() {
        TestSubscriber subscriber = delayRemover.getRemoveCompletable().test();
        scheduler.advanceTimeBy(DELAY, TIME_UNIT);
        subscriber.assertComplete();
    }

    @Test
    public void delayRemoverDoesNotCompletesAfterDelayMinusOneTest() {
        TestSubscriber subscriber = delayRemover.getRemoveCompletable().test();
        scheduler.advanceTimeBy(DELAY - 1, TIME_UNIT);
        subscriber.assertNotComplete();
    }

    @Test
    public void delayRemoverCompleteAfterDelayWhenOnDisplayAndOnDisplayRemovedCalledTest() {
        TestSubscriber subscriber = delayRemover.getRemoveCompletable().test();
        delayRemover.onDisplay();
        delayRemover.onDisplayRemoved();
        scheduler.advanceTimeBy(DELAY, TIME_UNIT);
        subscriber.assertComplete();
    }

    @Test
    public void delayRemoverCompleteAfterDelayWhenOnDisplayCalledTest() {
        TestSubscriber subscriber = delayRemover.getRemoveCompletable().test();
        delayRemover.onDisplay();
        scheduler.advanceTimeBy(DELAY, TIME_UNIT);
        subscriber.assertComplete();
    }

    @Test
    public void delayRemoverCompleteAfterDelayWhenOnDisplayRemovedCalledTest() {
        TestSubscriber subscriber = delayRemover.getRemoveCompletable().test();
        delayRemover.onDisplayRemoved();
        scheduler.advanceTimeBy(DELAY, TIME_UNIT);
        subscriber.assertComplete();
    }

    @Test
    public void delayRemoverCompleteAfterDelayWhenOnCompleteCalledTest() {
        TestSubscriber subscriber = delayRemover.getRemoveCompletable().test();
        delayRemover.onComplete();
        scheduler.advanceTimeBy(DELAY, TIME_UNIT);
        subscriber.assertComplete();
    }

    //displayingDelayRemover

    @Test
    public void displayingDelayRemoverNotCompletedAtSubscriptionTest() {
        TestSubscriber subscriber = displayingDelayRemover.getRemoveCompletable().test();
        subscriber.assertNotComplete();
    }

    @Test
    public void displayingDelayRemoverDoesNotCompleteAfterDelayTest() {
        TestSubscriber subscriber = displayingDelayRemover.getRemoveCompletable().test();
        scheduler.advanceTimeBy(DELAY, TIME_UNIT);
        subscriber.assertNotComplete();
    }

    @Test
    public void displayingDelayRemoverDoesNotCompleteAfterDelayMinusOneTest() {
        TestSubscriber subscriber = displayingDelayRemover.getRemoveCompletable().test();
        scheduler.advanceTimeBy(DELAY - 1, TIME_UNIT);
        subscriber.assertNotComplete();
    }

    @Test
    public void displayingDelayRemoverDoesNotCompleteAfterDelayPlusOneTest() {
        TestSubscriber subscriber = displayingDelayRemover.getRemoveCompletable().test();
        scheduler.advanceTimeBy(DELAY + 1, TIME_UNIT);
        subscriber.assertNotComplete();
    }

    @Test
    public void displayingDelayRemoverCompleteAfterDelayWhenOnDisplayCalledTest() {
        TestSubscriber subscriber = displayingDelayRemover.getRemoveCompletable().test();
        displayingDelayRemover.onDisplay();
        scheduler.advanceTimeBy(DELAY, TIME_UNIT);
        subscriber.assertComplete();
    }

    @Test
    public void displayingDelayRemoverDoesNotCompleteAfterDelayWhenOnDisplayAndOnDisplayRemovedCalledTest() {
        TestSubscriber subscriber = displayingDelayRemover.getRemoveCompletable().test();
        displayingDelayRemover.onDisplay();
        displayingDelayRemover.onDisplayRemoved();
        scheduler.advanceTimeBy(DELAY, TIME_UNIT);
        subscriber.assertNotComplete();
    }

    @Test
    public void displayingDelayRemoverDoesNotCompleteAfterDelayWhenOnDisplayCalledThenTimeAdvancedByDelayMinusOneThenOnDisplayRemovedThenTimeAdvancedByOneTest() {
        TestSubscriber subscriber = displayingDelayRemover.getRemoveCompletable().test();
        displayingDelayRemover.onDisplay();
        scheduler.advanceTimeBy(DELAY - 1, TIME_UNIT);
        displayingDelayRemover.onDisplayRemoved();
        scheduler.advanceTimeBy(1, TIME_UNIT);
        subscriber.assertNotComplete();
    }

    @Test
    public void displayingDelayRemoverDoesNotCompleteAfterDelayWhenOnDisplayCalledThenTimeAdvancedByDelayMinusOneThenOnDisplayRemovedThenTimeAdvancedByOneThenOnDisplayCalledThenTimeAdvancedByOneTest() {
        TestSubscriber subscriber = displayingDelayRemover.getRemoveCompletable().test();
        displayingDelayRemover.onDisplay();
        scheduler.advanceTimeBy(DELAY - 1, TIME_UNIT);
        displayingDelayRemover.onDisplayRemoved();
        scheduler.advanceTimeBy(1, TIME_UNIT);
        displayingDelayRemover.onDisplay();
        scheduler.advanceTimeBy(1, TIME_UNIT);
        subscriber.assertComplete();
    }

    @Test
    public void displayingDelayRemoverCompletesWhenOnDisplayIsCalledTwiceWithSufficientDelayBetween() {
        TestSubscriber subscriber = displayingDelayRemover.getRemoveCompletable().test();
        displayingDelayRemover.onDisplay();
        scheduler.advanceTimeBy(DELAY - 1, TIME_UNIT);
        displayingDelayRemover.onDisplay();
        scheduler.advanceTimeBy(1, TIME_UNIT);
        subscriber.assertComplete();
    }

    @Test
    public void displayingDelayRemoverDoesNotCompleteWhenOnDisplayIsCalledTwiceWithInsufficientDelayBetweenTest() {
        TestSubscriber subscriber = displayingDelayRemover.getRemoveCompletable().test();
        displayingDelayRemover.onDisplay();
        scheduler.advanceTimeBy(DELAY - 2, TIME_UNIT);
        displayingDelayRemover.onDisplay();
        scheduler.advanceTimeBy(1, TIME_UNIT);
        subscriber.assertNotComplete();
    }

    @Test
    public void displayingDelayCompletesOnDisplayOnDisplayRemovedOnDisplayRemovedOnDisplaySufficientTimeTest() {
        TestSubscriber subscriber = displayingDelayRemover.getRemoveCompletable().test();
        displayingDelayRemover.onDisplay();
        scheduler.advanceTimeBy(DELAY - 2, TIME_UNIT);
        displayingDelayRemover.onDisplayRemoved();
        scheduler.advanceTimeBy(DELAY, TIME_UNIT);
        displayingDelayRemover.onDisplayRemoved();
        scheduler.advanceTimeBy(DELAY, TIME_UNIT);
        displayingDelayRemover.onDisplay();
        scheduler.advanceTimeBy(2, TIME_UNIT);
        subscriber.assertComplete();
    }
    @Test
    public void displayingDelayDoesNotCompleteOnDisplayOnDisplayRemovedOnDisplayRemovedOnDisplayInsufficientTimeTest() {
        TestSubscriber subscriber = displayingDelayRemover.getRemoveCompletable().test();
        displayingDelayRemover.onDisplay();
        scheduler.advanceTimeBy(DELAY - 2, TIME_UNIT);
        displayingDelayRemover.onDisplayRemoved();
        scheduler.advanceTimeBy(DELAY, TIME_UNIT);
        displayingDelayRemover.onDisplayRemoved();
        scheduler.advanceTimeBy(DELAY, TIME_UNIT);
        displayingDelayRemover.onDisplay();
        scheduler.advanceTimeBy(1, TIME_UNIT);
        subscriber.assertNotComplete();
    }
}
