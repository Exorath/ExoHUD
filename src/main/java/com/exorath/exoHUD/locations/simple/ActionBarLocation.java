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

package com.exorath.exoHUD.locations.simple;

import com.exorath.exoHUD.DisplayPackage;
import com.exorath.exoHUD.HUDPackage;
import com.exorath.exoHUD.HUDText;
import com.exorath.exoHUD.libs.actionbar.ActionBarHandler;
import com.exorath.exoproperties.Property;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by toonsev on 10/3/2016.
 */
public class ActionBarLocation extends SimpleLocation {
    public static final Property<Boolean> FADE_OUT_PROPERTY = Property.create(false);
    private static final int REFRESH_TIME = 3;
    private static final TimeUnit REFRESH_TIME_UNIT = TimeUnit.SECONDS;

    private Player player;
    private ActionBarHandler actionBarHandler;
    private Scheduler refreshScheduler;

    private TextComponent[] lastMessage;
    private DisplayPackage current;
    private Disposable subscription;
    private Disposable scheduledRefresh;

    public ActionBarLocation(Player player, ActionBarHandler actionBarHandler) {
        this(player, actionBarHandler, Schedulers.computation());
    }

    public ActionBarLocation(Player player, ActionBarHandler actionBarHandler, Scheduler refreshScheduler) {
        this.player = player;
        this.actionBarHandler = actionBarHandler;
        this.refreshScheduler = refreshScheduler;
    }


    @Override
    public void onDisplayRemove(DisplayPackage displayPackage) {
        if (current != displayPackage)
            return;
        if (subscription != null)
            subscription.dispose();
        if(scheduledRefresh != null)
            scheduledRefresh.dispose();

        Boolean fadeOut = displayPackage.getProperties().getMeta().get(FADE_OUT_PROPERTY);
        if (fadeOut && lastMessage != null)
            actionBarHandler.send(player, lastMessage);
        else
            actionBarHandler.clear(player);
    }
    @Override
    public void onDisplay(DisplayPackage displayPackage) {
        current = displayPackage;
        display(displayPackage.getHudPackage());
    }


    //TODO; MAYBE SPECIFY DEBOUNCE SCHEDULER FOR TESTING?
    private void display(HUDPackage hudPackage) {
        List<HUDText> hudTexts = hudPackage.getTexts();
        if (hudTexts.size() == 0)
            return;
        HUDText barText = hudTexts.get(0);
        Observable<List<TextComponent>> titleSubtitleObservable = barText.getTextObservable();

        subscription = titleSubtitleObservable.debounce(50, TimeUnit.MILLISECONDS)
                .subscribe(textComponents -> {
                    lastMessage = textComponents.toArray(new TextComponent[textComponents.size()]);
                    actionBarHandler.send(player, lastMessage);
                    if(scheduledRefresh != null)
                        scheduledRefresh.dispose();
                    sendUpdate(current);
                });

    }

    public void sendUpdate(DisplayPackage toUpdate) {
        scheduledRefresh = refreshScheduler.createWorker().schedule(() -> {
            if (toUpdate != current)
                return;
            if (lastMessage != null)
                actionBarHandler.send(player, lastMessage);
            sendUpdate(toUpdate);
        }, REFRESH_TIME, REFRESH_TIME_UNIT);
    }

    @Override
    public void onHide(boolean hidden) {
        actionBarHandler.clear(player);
    }
}
