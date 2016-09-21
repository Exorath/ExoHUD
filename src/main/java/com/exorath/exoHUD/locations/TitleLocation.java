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

import com.exorath.exoHUD.HUDLocation;
import com.exorath.exoHUD.HUDPackage;
import com.exorath.exoHUD.HUDRemover;
import com.exorath.exoHUD.HUDText;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * Created by toonsev on 9/20/2016.
 */
public class TitleLocation implements HUDLocation {
    private TreeSet<HUDPackage> packageQueue = new TreeSet<>();
    @Override
    public void addText(HUDText text, double priority) {
        packageQueue.add(HUDPackage.create(Arrays.asList(text), priority));
    }

    @Override
    public void addText(HUDText text, double priority, HUDRemover remover) {
        packageQueue.add(HUDPackage.create(Arrays.asList(text), priority));
        //Add remover
    }

    @Override
    public void addPackage(HUDPackage hudPackage) {
        packageQueue.add(hudPackage);
    }

    @Override
    public boolean removeText(HUDText text) {
        for(HUDPackage hudPackage : packageQueue)
            if(hudPackage.getTexts().contains(text))
                return removePackage(hudPackage);
        return false;
    }

    @Override
    public boolean removePackage(HUDPackage hudPackage) {
        boolean removed = packageQueue.remove(hudPackage);
        if(removed){

        }
        return removed;
    }

    @Override
    public void hide(double limit) {

    }

    @Override
    public void hide(){

    }
}
