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

package com.exorath.exoHUD.locations.simple;

import com.exorath.exoHUD.*;
import com.exorath.exoHUD.libs.title.TitleHandler;
import com.exorath.exoHUD.locations.simple.SimpleLocation;
import com.exorath.exoproperties.Property;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;


/**
 * Created by toonsev on 9/20/2016.
 */
public class TitleLocation extends SimpleLocation {
    public static final Property<Integer> FADE_IN_PROPERTY = Property.create(0);
    public static final Property<Integer> FADE_OUT_PROPERTY = Property.create(0);
    private static final int stayTime = Integer.MAX_VALUE / 2;

    private Player player;
    private TitleHandler titleHandler;

    private DisplayPackage current;
    private String lastTitle;
    private String lastSubtitle;
    private Disposable subscription;


    public TitleLocation(Player player, TitleHandler titleHandler) {
        this.player = player;
        this.titleHandler = titleHandler;
    }

    @Override
    public void onDisplayRemove(DisplayPackage displayPackage) {
        if (current != displayPackage)
            return;
        if (subscription != null)
            subscription.dispose();
        int fadeOut = displayPackage.getProperties().getMeta().get(FADE_OUT_PROPERTY);
        if (fadeOut != 0 && (lastTitle != null || lastSubtitle != null))
            titleHandler.send(player, 0, 0, 0, lastTitle, lastSubtitle);
        else
            titleHandler.clear(player);
    }

    @Override
    public void onDisplay(DisplayPackage displayPackage) {
        current = displayPackage;
        int fadeIn = displayPackage.getProperties().getMeta().get(FADE_IN_PROPERTY);
        display(displayPackage.getHudPackage(), fadeIn);
    }

    //TODO; MAYBE SPECIFY DEBOUNCE SCHEDULER FOR TESTING?
    private void display(HUDPackage hudPackage, int fadeIn) {
        List<HUDText> hudTexts = hudPackage.getTexts();
        HUDText title = hudTexts.size() >= 1 ? hudTexts.get(0) : null;
        HUDText subtitle = hudTexts.size() >= 2 ? hudTexts.get(1) : null;
        Observable<TitleSubtitle> titleSubtitleObservable = null;
        if (title != null && subtitle != null) {
            titleSubtitleObservable = getWrappedTitleSubtitleObservable(title.getTextObservable(), subtitle.getTextObservable());
        } else if (title != null) {
            titleSubtitleObservable = getWrappedTitleObservable(title.getTextObservable());
        } else if (subtitle != null) {
            titleSubtitleObservable = getWrappedSubtitleObservable(subtitle.getTextObservable());
        } else
            return;
        subscription = titleSubtitleObservable.debounce(50, TimeUnit.MILLISECONDS).subscribe(titleSubtitle -> {
            String legacyTitle = titleSubtitle.getTitleComponents() == null ? null : getLegacyText(titleSubtitle.getTitleComponents());
            String legacySubtitle = titleSubtitle.getSubtitleComponents() == null ? null : getLegacyText(titleSubtitle.getSubtitleComponents());

            lastTitle = legacyTitle;
            lastSubtitle = legacySubtitle;

            titleHandler.send(player, fadeIn, stayTime, 0, legacyTitle, legacySubtitle);
        });
    }

    private static String getLegacyText(List<TextComponent> components) {
        StringBuilder legacyBuilder = new StringBuilder();
        components.forEach(component -> legacyBuilder.append(component.toLegacyText()));
        return legacyBuilder.toString();
    }

    private Observable<TitleSubtitle> getWrappedTitleSubtitleObservable(Observable<List<TextComponent>> titleObservable, Observable<List<TextComponent>> subtitleObservable) {
        return Observable.combineLatest(titleObservable, subtitleObservable, (titleComponents, subtitleComponents) -> new TitleSubtitle(titleComponents, subtitleComponents));
    }

    private Observable<TitleSubtitle> getWrappedTitleObservable(Observable<List<TextComponent>> titleObservable) {
        return titleObservable.map(textComponents -> new TitleSubtitle(textComponents, null));
    }

    private Observable<TitleSubtitle> getWrappedSubtitleObservable(Observable<List<TextComponent>> subtitleObservable) {
        return subtitleObservable.map(textComponents -> new TitleSubtitle(null, textComponents));

    }

    private static Observable<List<TextComponent>> getTitleObservable(HUDPackage hudPackage) {
        return hudPackage.getTexts().get(0).getTextObservable();
    }

    private static Observable<List<TextComponent>> getSubtitleObservable(HUDPackage hudPackage) {
        return hudPackage.getTexts().get(1).getTextObservable();
    }

    @Override
    public void onHide(boolean hidden) {
        if (hidden)
            titleHandler.clear(player);
    }

    private class TitleSubtitle {
        private List<TextComponent> titleComponents;
        private List<TextComponent> subtitleComponents;

        public TitleSubtitle(List<TextComponent> titleComponents, List<TextComponent> subtitleComponents) {
            this.titleComponents = titleComponents;
            this.subtitleComponents = subtitleComponents;
        }

        public List<TextComponent> getTitleComponents() {
            return titleComponents;
        }

        public List<TextComponent> getSubtitleComponents() {
            return subtitleComponents;
        }
    }
}
