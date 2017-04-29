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

package com.exorath.exoHUD.removers;

import io.reactivex.subscribers.TestSubscriber;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by toonsev on 9/19/2016.
 */
public class CompleteRemoverTest {
    private CompleteRemover completeRemover;

    @Before
    public void setup(){
        this.completeRemover = new CompleteRemover();
    }

    @Test
    public void getRemoveCompletableDoesNotCompleteByDefaultTest(){
       completeRemover.getRemoveCompletable().test().assertNotComplete();
    }

    @Test
    public void getRemoveCompletableCompletesWhenOnCompleteIsCalledBeforeSubscriptionTest(){
        completeRemover.onComplete();
        completeRemover.getRemoveCompletable().test().assertComplete();
    }

    @Test
    public void getRemoveCompletableCompletesWhenOnCompleteIsCalledAfterSubscriptionTest(){
        TestSubscriber testSubscriber = completeRemover.getRemoveCompletable().test();
        completeRemover.onComplete();
        testSubscriber.assertComplete();
    }

    @Test
    public void getRemoveCompletableDoesNotCompleteWhenOnDisplayIsCalledTest(){
        TestSubscriber testSubscriber = completeRemover.getRemoveCompletable().test();
        completeRemover.onDisplay();
        testSubscriber.assertNotComplete();
    }

    @Test
    public void getRemoveCompletableDoesNotCompleteWhenOnDisplayRemovedIsCalledTest(){
        TestSubscriber testSubscriber = completeRemover.getRemoveCompletable().test();
        completeRemover.onDisplayRemoved();
        testSubscriber.assertNotComplete();
    }

    @Test
    public void getRemoveCompletableDoesNotCompleteWhenOnDisplayOnDisplayRemovedAndOnCompleteIsCalledTest(){
        TestSubscriber testSubscriber = completeRemover.getRemoveCompletable().test();
        completeRemover.onDisplay();
        completeRemover.onDisplayRemoved();
        completeRemover.onComplete();
        testSubscriber.assertComplete();
    }
}
