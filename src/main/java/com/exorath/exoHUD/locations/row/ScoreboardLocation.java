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

package com.exorath.exoHUD.locations.row;

import com.exorath.exoHUD.*;
import com.exorath.exoHUD.libs.scoreboard.ScoreboardBase;
import com.exorath.exoHUD.libs.scoreboard.ScoreboardEntry;
import com.exorath.exoHUD.locations.simple.SimpleLocation;
import io.reactivex.*;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * Created by toonsev on 5/9/2017.
 */
public class ScoreboardLocation implements HUDLocation {
    private Player player;
    private boolean hidden = false;
    private Double hideThreshold = null;//still requiers implementation

    private ScoreboardBase scoreboardBase;
    private ScoreboardTitleLocation sbTitleLocation = new ScoreboardTitleLocation();


    private TreeSet<DisplayPackage> displayPackages = new TreeSet<>(Collections.reverseOrder(DisplayPackage.COMPARATOR_PRIORITY));
    private HashMap<HUDText, ScoreboardEntry> entriesByHudText = new HashMap<>();

    private HashMap<DisplayPackage, Disposable> removeSubscriptionByDisplayPackage = new HashMap<>();

    public ScoreboardLocation(Player player) {
        this.player = player;
        this.scoreboardBase = new ScoreboardBase("");
        scoreboardBase.addPlayer(player);
    }

    @Override
    public DisplayPackage addText(HUDText text, DisplayProperties displayProperties) {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(Arrays.asList(text)), displayProperties);
        addDisplayPackage(displayPackage);
        return displayPackage;
    }

    @Override
    public void addDisplayPackage(DisplayPackage displayPackage) {
        displayPackages.add(displayPackage);
        updateListing();
        setRemover(displayPackage);
    }

    @Override
    public boolean removeDisplayPackage(DisplayPackage displayPackage) {
        boolean removed = displayPackages.remove(displayPackage);
        if (removed) {
            disposeRemoveSubscription(displayPackage);
            tryStopDisplaying(displayPackage);
            updateListing();
        }
        return removed;
    }

    private void tryStopDisplaying(DisplayPackage displayPackage) {
        displayPackage.getHudPackage().getTexts().forEach(hudText -> removeText(hudText));
    }

    private boolean disposeRemoveSubscription(DisplayPackage displayPackage) {
        Disposable disposable = removeSubscriptionByDisplayPackage.remove(displayPackage);
        if (disposable != null)
            disposable.dispose();
        return disposable != null;
    }

    @Override
    public void setHideThreshold(Double hideThreshold) {
        this.hideThreshold = hideThreshold;
        //TODO: Remove/add all valid HUD's
    }

    @Override
    public void setHidden(boolean hidden) {
        if (this.hidden == hidden)
            return;
        this.hidden = hidden;
        if (hidden)
            scoreboardBase.remove(player);
        else
            scoreboardBase.addPlayer(player);
    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    public DisplayPackage addTitle(HUDText hudText, DisplayProperties displayProperties){
        return sbTitleLocation.addText(hudText, displayProperties);
    }

    public boolean removeTitle(DisplayPackage displayPackage){
         return sbTitleLocation.removeDisplayPackage(displayPackage);
    }

    private void setRemover(DisplayPackage displayPackage) {
        removeSubscriptionByDisplayPackage.put(displayPackage, displayPackage.getProperties().getRemover().getRemoveCompletable()
                .subscribe(() -> removeDisplayPackage(displayPackage)));
    }

    private void updateListing() {
        int remainingLines = 15;
        Set<HUDText> newTexts = new HashSet<>();
        for (DisplayPackage displayPackage : displayPackages) {
            if (remainingLines <= 0)
                return;
            if (displayPackage.getHudPackage().getTexts().size() > remainingLines)//The package does not fit, try another
                continue;
            for (HUDText hudText : displayPackage.getHudPackage().getTexts()) {
                newTexts.add(hudText);
                if (entriesByHudText.containsKey(hudText))
                    entriesByHudText.get(hudText).setValue(remainingLines);
                else
                    display(hudText, remainingLines);
            }
        }
        for (HUDText hudText : entriesByHudText.keySet())//Remove all obsolete entries
            if (!newTexts.contains(hudText))
                removeText(hudText);
    }

    private void removeText(HUDText hudText) {
        scoreboardBase.remove(entriesByHudText.get(hudText));
        entriesByHudText.remove(hudText);
    }

    private void display(HUDText hudText, int priority) {
        ScoreboardEntry entry = scoreboardBase.add("To be determined", priority);
        hudText.getTextObservable().subscribe(textComponents -> entry.update(getLegacyText(textComponents)));
    }

    private static String getLegacyText(List<TextComponent> components) {
        StringBuilder legacyBuilder = new StringBuilder();
        components.forEach(component -> legacyBuilder.append(component.toLegacyText()));
        return legacyBuilder.toString();
    }

    private class ScoreboardTitleLocation extends SimpleLocation {
        private DisplayPackage current;
        private Disposable subscription;

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
                            scoreboardBase.setTitle(getLegacyText(textComponents)));

        }

        @Override
        public void onDisplayRemove(DisplayPackage displayPackage) {
            if (current != displayPackage)
                return;
            if (subscription != null)
                subscription.dispose();
            scoreboardBase.setTitle("");
        }

        @Override
        public void onHide(boolean hidden) {

        }
    }

}
