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
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by toonsev on 9/13/2016.
 */
public class IterateText extends CompositeTextBase {
    private long interval;
    private TimeUnit intervalUnit;
    private Scheduler scheduler;

    public IterateText(int interval, TimeUnit intervalUnit, Scheduler scheduler, HUDText... hudTexts) {
        super(hudTexts);
        this.interval = interval;
        this.intervalUnit = intervalUnit;
        this.scheduler = scheduler;
    }

    @Override
    public Observable<List<TextComponent>> getTextObservable() {
        return Observable.interval(0, interval, intervalUnit, scheduler)
                .flatMap(iteration -> getHudTexts().get((int) (iteration % getHudTexts().size())).getTextObservable());
    }

    public static final IterateText iterate(int interval, TimeUnit intervalUnit, HUDText... hudTexts) {
        return new IterateText(interval, intervalUnit, Schedulers.computation(), hudTexts);
    }
}
