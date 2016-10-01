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

package com.exorath.exoHUD;

/**
 * Created by toonsev on 8/30/2016.
 */
public interface HUDLocation {

    /**
    DisplayPackage addText(HUDText text);
    /**
     * Adds a text to this
     * @param text
     */
    DisplayPackage addText(HUDText text, DisplayProperties properties);

    void addDisplayPackage(DisplayPackage displayPackage);

    boolean removeDisplayPackage(DisplayPackage displayPackage);

    /**
     * Hides HUDTexts and packages from displaying if their priority is under the provided maxPriority.
     * Note that as soon as this threshold is lowered the hidden texts will display again.
     *
     * @param hideThreshold any text or package with a priority lower then this will not display.
     */
    void setHideThreshold(Double hideThreshold);

    /**
     * Hides this location completely.
     * Note that as soon as this threshold is lowered the hidden texts will display again.
     */
    void setHidden(boolean hidden);

    boolean isHidden();

}
