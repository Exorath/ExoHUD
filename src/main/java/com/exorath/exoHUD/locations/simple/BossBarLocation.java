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
import com.exorath.exoHUD.HUDText;
import com.exorath.exoproperties.Property;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * TODO: Make color and style observable.
 * Created by toonsev on 5/18/2017.
 */
public class BossBarLocation extends SimpleLocation{
    public Property<BarColor> PROPERTY_BAR_COLOR = Property.create(BarColor.GREEN);
    public Property<BarStyle> PROPERTY_BAR_STYLE = Property.create(BarStyle.SOLID);
    private Player player;
    private BossBar bossBar;

    private DisplayPackage current;
    private Disposable subscription;

    public BossBarLocation(Player player) {
        this.player = player;
    }

    @Override
    public void onDisplay(DisplayPackage displayPackage) {
        this.current = displayPackage;
        List<HUDText> hudTexts = displayPackage.getHudPackage().getTexts();
        if (hudTexts.size() == 0)
            return;
        HUDText barText = hudTexts.get(0);
        Observable<List<TextComponent>> titleSubtitleObservable = barText.getTextObservable();

        subscription = titleSubtitleObservable.debounce(50, TimeUnit.MILLISECONDS)
                .subscribe(textComponents ->
                        show(displayPackage, textComponents));

    }

    private void show(DisplayPackage displayPackage, List<TextComponent> components){
        BarColor color = displayPackage.getProperties().getMeta().get(PROPERTY_BAR_COLOR);
        BarStyle style = displayPackage.getProperties().getMeta().get(PROPERTY_BAR_STYLE);
        if(bossBar == null) {
            bossBar = Bukkit.createBossBar(getLegacyText(components), color, style);
            bossBar.addPlayer(player);
        }else
            bossBar.setTitle(getLegacyText(components));
    }



    private static String getLegacyText(List<TextComponent> components) {
        StringBuilder legacyBuilder = new StringBuilder();
        components.forEach(component -> legacyBuilder.append(component.toLegacyText()));
        return legacyBuilder.toString();
    }
    @Override
    public void onDisplayRemove(DisplayPackage displayPackage) {
        if (current != displayPackage)
            return;
        if (subscription != null)
            subscription.dispose();
        bossBar.removeAll();
    }

    @Override
    public void onHide(boolean hidden) {
        bossBar.setVisible(!hidden);
    }
}
