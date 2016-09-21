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

import io.reactivex.Completable;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * Created by toonsev on 9/21/2016.
 */
public class NeverRemoverTest {
    private NeverRemover neverRemover;

    @Before
    public void setup(){
        neverRemover = new NeverRemover();
    }

    @Test
    public void staticNeverNotNullTest(){
        assertNotNull(NeverRemover.never());
    }

    @Test
    public void doesNotInstantlyTerminateTest(){
        neverRemover.getRemoveCompletable().test().assertNotTerminated();
    }

    @Test
    public void doesNotTerminateAfterOnCompleteCalledTest(){
        Completable removeCompletable = neverRemover.getRemoveCompletable();
        neverRemover.onComplete();
        removeCompletable.test().assertNotComplete();
    }    @Test
    public void doesNotTerminateAfterOneHundredMillisTest(){
        Completable removeCompletable = neverRemover.getRemoveCompletable();
        assertFalse(removeCompletable.test().awaitTerminalEvent(100, TimeUnit.MILLISECONDS));
    }
}
