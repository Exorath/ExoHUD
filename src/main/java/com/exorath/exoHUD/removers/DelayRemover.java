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

import com.exorath.exoHUD.HUDRemover;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.Timed;
import io.reactivex.subjects.BehaviorSubject;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by toonsev on 9/15/2016.
 */
public class DelayRemover implements HUDRemover {
    private long delay;
    private TimeUnit timeUnit;
    private boolean whenDisplaying;
    private Scheduler scheduler;


    private BehaviorSubject<Boolean> displayingSubject;

    private AtomicBoolean displaying;

    public DelayRemover(long delay, TimeUnit timeUnit, boolean whenDisplaying) {
        this(delay, timeUnit, whenDisplaying, Schedulers.computation());
    }

    public DelayRemover(long delay, TimeUnit timeUnit, boolean whenDisplaying, Scheduler scheduler) {
        this.delay = delay;
        this.timeUnit = timeUnit;
        this.whenDisplaying = whenDisplaying;
        this.scheduler = scheduler;

        if (whenDisplaying) {
            this.displayingSubject = BehaviorSubject.createDefault(false);
            displaying = new AtomicBoolean(false);
        }
    }

    @Override
    public void onDisplay() {
        setDisplaying(true);
    }

    @Override
    public void onDisplayRemoved() {
        setDisplaying(false);
    }

    private void setDisplaying(boolean displaying){
        if(whenDisplaying){
            displayingSubject.onNext(displaying);
            this.displaying.set(displaying);
        }
    }

    @Override
    public Completable getRemoveCompletable() {
        return whenDisplaying ? getWhenDisplayingCompletable() : getSimpleDelayCompletable();
    }

    protected Completable getWhenDisplayingCompletable() {
        return displayingSubject.timestamp(scheduler)
                .timeout(new TimeoutFunc(timeUnit.toMillis(delay)), Observable.empty())
                .toCompletable();
    }
    protected Completable getSimpleDelayCompletable() {
        return Completable.timer(delay, timeUnit, scheduler);
    }
    private class TimeoutFunc implements Function<Timed<Boolean>, Observable<Object>> {
        private long lastEmitTime = scheduler.now(TimeUnit.MILLISECONDS);
        private long millisRemaining;
        private boolean previous = false;

        public TimeoutFunc(long millisRemaining) {
            this.millisRemaining = millisRemaining;
        }

        @Override
        public Observable apply(Timed<Boolean> booleanTimed) throws Exception {
            ifPreviousIsTrueDecreaseRemaining(booleanTimed.time());
            setLastEmitTime(booleanTimed.time());
            setPrevious(booleanTimed.value());
            return booleanTimed.value() ? Observable.timer(millisRemaining, TimeUnit.MILLISECONDS, scheduler) : Observable.never();
        }

        private void setLastEmitTime(long time) {
            this.lastEmitTime = time;
        }

        private void setPrevious(boolean previous) {
            this.previous = previous;
        }

        private void ifPreviousIsTrueDecreaseRemaining(long emitTime) {
            if (previous)
                millisRemaining -= emitTime - lastEmitTime;
        }
    }
}
