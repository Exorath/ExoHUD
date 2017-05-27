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
import com.exorath.exoHolograms.api.Hologram;
import com.exorath.exoHolograms.api.HologramsAPI;
import com.exorath.exoHolograms.api.lines.HologramLine;
import com.exorath.exoHolograms.api.lines.TextLine;
import io.reactivex.disposables.Disposable;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;

import java.util.*;

/**
 * Created by toonsev on 5/9/2017.
 */
public class HologramLocation implements HUDLocation {
    private Hologram hologram;
    private boolean hidden = false;
    private Double hideThreshold = null;//still requiers implementation


    private TreeSet<DisplayPackage> displayPackages = new TreeSet<>(Collections.reverseOrder(DisplayPackage.COMPARATOR_PRIORITY));
    private HashMap<HUDText, HologramLine> linesByHUDText = new HashMap<>();
    private List<DisplayPackage> visiblePackages = new ArrayList<>();

    private HashMap<DisplayPackage, Disposable> removeSubscriptionByDisplayPackage = new HashMap<>();

    public HologramLocation(Location location) {
        this.hologram = HologramsAPI.getInstance().addHologram(location);
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
        for (int i = 0; i < visiblePackages.size(); i++) {
            if (visiblePackages.get(i).getProperties().getPriority() >= displayPackage.getProperties().getPriority())
                continue;
            displayPackage(i, displayPackage);
            return;
        }
        displayPackage(null, displayPackage);
    }

    private void displayPackage(Integer index, DisplayPackage displayPackage) {
        if (index != null)
            visiblePackages.add(index, displayPackage);
        else
            visiblePackages.add(displayPackage);
        ListIterator<HUDText> listIterator = displayPackage.getHudPackage().getTexts().listIterator(displayPackage.getHudPackage().getTexts().size());
        while (listIterator.hasPrevious()) {
            HUDText text = listIterator.previous();
            TextLine line = index != null ? hologram.insertTextLine(index, "") : hologram.appendTextLine("");
            linesByHUDText.put(text, line);
            display(text, line);
        }
        setRemover(displayPackage);
    }

    @Override
    public boolean removeDisplayPackage(DisplayPackage displayPackage) {
        boolean removed = displayPackages.remove(displayPackage);
        visiblePackages.remove(displayPackage);
        if (removed) {
            disposeRemoveSubscription(displayPackage);
            tryStopDisplaying(displayPackage);
            for (HUDText hudText : displayPackage.getHudPackage().getTexts()) {
                HologramLine line = linesByHUDText.remove(hudText);
                if (line != null)
                    hologram.removeLine(line);
            }
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
        hologram.setVisible(!hidden);


    }

    @Override
    public boolean isHidden() {
        return hidden;
    }

    private void setRemover(DisplayPackage displayPackage) {
        removeSubscriptionByDisplayPackage.put(displayPackage, displayPackage.getProperties().getRemover().getRemoveCompletable()
                .subscribe(() -> removeDisplayPackage(displayPackage)));
    }


    private void removeText(HUDText hudText) {
        hologram.removeLine(linesByHUDText.get(hudText));
        linesByHUDText.remove(hudText);
    }

    private void display(HUDText hudText, TextLine textLine) {
        hudText.getTextObservable().subscribe(textComponents -> textLine.setText(getLegacyText(textComponents)));
    }

    private static String getLegacyText(List<TextComponent> components) {
        StringBuilder legacyBuilder = new StringBuilder();
        components.forEach(component -> legacyBuilder.append(component.toLegacyText()));
        return legacyBuilder.toString();
    }

    public void teleport(Location location){
        hologram.teleport(location);
    }

    public Location getLocation(){
        return hologram.getLocation();
    }
}
