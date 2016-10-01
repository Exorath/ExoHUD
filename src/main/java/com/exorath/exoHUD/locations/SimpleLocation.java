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
import io.reactivex.disposables.Disposable;

import java.util.*;

/**
 * Created by toonsev on 9/24/2016.
 */
public abstract class SimpleLocation implements HUDLocation {
    private boolean hidden = false;
    private Double hideThreshold = null;

    private DisplayPackage currentDisplayPackage;

    private List<DisplayPackage> displayPackages = new ArrayList<>();
    private HashMap<DisplayPackage, Disposable> removeSubscriptionByDisplayPackage = new HashMap<>();

    @Override
    public DisplayPackage addText(HUDText text, DisplayProperties displayProperties) {
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(Arrays.asList(text)), displayProperties);
        addDisplayPackage(displayPackage);
        return displayPackage;
    }

    @Override
    public void addDisplayPackage(DisplayPackage displayPackage) {
        displayPackages.add(displayPackage);
        sortAndUpdateDisplay();
        setRemover(displayPackage);
    }

    @Override
    public boolean removeDisplayPackage(DisplayPackage displayPackage) {
        boolean removed = displayPackages.remove(displayPackage);
        if (removed) {
            disposeRemoveSubscription(displayPackage);
            tryStopDisplaying(displayPackage);
            sortAndUpdateDisplay();
        }
        return removed;
    }

    private boolean disposeRemoveSubscription(DisplayPackage displayPackage) {
        Disposable disposable = removeSubscriptionByDisplayPackage.remove(displayPackage);
        if (disposable != null)
            disposable.dispose();
        return disposable != null;
    }

    private void sortAndUpdateDisplay() {

        displayPackages.sort(Collections.reverseOrder(DisplayPackage.COMPARATOR_PRIORITY));
        if (displayShouldUpdate()) {
            if (currentDisplayPackage != displayPackages.get(0))
                stopDisplayingCurrent();
            currentDisplayPackage = displayPackages.get(0);
            if (!isHidden(currentDisplayPackage))
                startDisplayingCurrent();
        }
    }

    private void setRemover(DisplayPackage displayPackage) {
        removeSubscriptionByDisplayPackage.put(displayPackage, displayPackage.getProperties().getRemover().getRemoveCompletable()
                .subscribe(() -> removeDisplayPackage(displayPackage)));
    }

    public boolean isHidden() {
        return hidden;
    }

    private void tryStopDisplaying(DisplayPackage displayPackage) {
        if (currentDisplayPackage == displayPackage)
            stopDisplayingCurrent();
    }

    private void stopDisplayingCurrent() {
        DisplayPackage current = currentDisplayPackage;
        if (current == null)
            return;
        currentDisplayPackage = null;
        onDisplayRemove(current);
    }

    private void startDisplayingCurrent() {
        onDisplay(currentDisplayPackage);
    }

    public abstract void onDisplay(DisplayPackage displayPackage);

    public abstract void onDisplayRemove(DisplayPackage displayPackage);

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
        if(currentDisplayPackage.getProperties().getPriority() < hideThreshold)
            stopDisplayingCurrent();
    }

    @Override
    public void setHidden(boolean hidden) {
        this.hidden = hidden;
        onHide(hidden);
        //update hidden state
    }

}
