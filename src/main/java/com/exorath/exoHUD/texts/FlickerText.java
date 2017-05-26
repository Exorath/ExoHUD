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

import com.exorath.exoHUD.HUDText;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by toonsev on 5/26/2017.
 */
public class FlickerText implements HUDText{
    private HUDText hudText;
    private int interval;
    private TimeUnit timeUnit;
    private Scheduler scheduler;
    private ChatColor replaceColor;

    public FlickerText(int interval, TimeUnit timeUnit, ChatColor replaceColor,HUDText hudText) {
        this(interval, timeUnit, Schedulers.computation(), replaceColor, hudText);
    }

    public FlickerText(int interval, TimeUnit timeUnit, Scheduler scheduler, ChatColor replaceColor, HUDText hudText) {
        this.interval = interval;
        this.timeUnit = timeUnit;
        this.scheduler = scheduler;
        this.replaceColor = replaceColor;
        this.hudText = hudText;
    }

    @Override
    public Observable<List<TextComponent>> getTextObservable() {
        return Observable.combineLatest(hudText.getTextObservable(), Observable.interval(0, interval, timeUnit, scheduler), (textComponents, aLong) -> {
            if(aLong % 2 == 0)
                textComponents.forEach(component -> component.setColor(replaceColor));
            return textComponents;
        });
    }
}
