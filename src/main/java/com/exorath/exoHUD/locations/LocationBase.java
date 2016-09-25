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

package com.exorath.exoHUD.locations;

import com.exorath.exoHUD.*;
import com.exorath.exoHUD.removers.NeverRemover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by toonsev on 9/24/2016.
 */
public abstract class LocationBase implements HUDLocation {
    private boolean hidden = false;
    private Double hideThreshold = null;

    private DisplayPackage currentDisplayPackage;
    private List<DisplayPackage> displayPackages = new ArrayList<>();


    @Override
    public DisplayPackage addText(HUDText text, DisplayProperties displayProperties) {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(Arrays.asList(text)), displayProperties);
        addDisplayPackage(displayPackage);
        return displayPackage;
    }

    @Override
    public DisplayPackage addHUDPackage(HUDPackage hudPackage) {
        DisplayPackage displayPackage = new DisplayPackage(hudPackage, DisplayProperties.create(0, NeverRemover.never()));
        addDisplayPackage(displayPackage);
        return displayPackage;
    }

    @Override
    public void addDisplayPackage(DisplayPackage displayPackage) {
        displayPackages.add(displayPackage);
        updateDisplay();
    }

    @Override
    public boolean removeDisplayPackage(DisplayPackage displayPackage) {
        boolean removed = displayPackages.remove(displayPackage);
        if (removed) {
            if (currentDisplayPackage == displayPackage) {
                currentDisplayPackage = null;
                updateDisplay();
            }
        }
        return removed;
    }

    private void updateDisplay() {
        displayPackages.sort(DisplayPackage.COMPARATOR_PRIORITY);
        if (displayShouldUpdate()) {
            currentDisplayPackage = displayPackages.get(0);
            updateCurrent();
        }
    }

    private void updateCurrent(){
        displayPackage(displayPackages.get(0));
    }

    private void displayPackage(DisplayPackage displayPackage) {
        if (currentDisplayPackage != null)
            removePackageFromDisplay(currentDisplayPackage);
        displayPackage.getProperties().getRemover().onDisplay();
        if (!isHidden(displayPackage))
            onDisplay(displayPackage);
    }

    private void removePackageFromDisplay(DisplayPackage displayPackage) {
        displayPackage.getProperties().getRemover().onDisplayRemoved();
        onDisplayRemove(displayPackage);

    }

    public abstract void onDisplayRemove(DisplayPackage displayPackage);

    public abstract void onDisplay(DisplayPackage displayPackage);

    public abstract void onHide(boolean hidden);

    private boolean isHidden(DisplayPackage displayPackage) {
        if (hidden)
            return true;
        if (hideThreshold == null)
            return false;
        return hideThreshold >= displayPackage.getProperties().getPriority();
    }

    private boolean displayShouldUpdate() {
        if (displayPackages.size() == 0)
            return false;
        if (currentDisplayPackage == null)
            return true;
        return displayPackages.get(0) != currentDisplayPackage;

    }

    @Override
    public void setHideThreshold(Double hideThreshold) {
        this.hideThreshold = hideThreshold;
        //update hidden state
    }

    @Override
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
        onHide(hidden);
        //update hidden state
    }

}
