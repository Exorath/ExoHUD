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
     * Adds a text to this
     * @param text
     * @param priority
     */
    void addText(HUDText text, double priority);
    /**
     * Adds a text to this
     * @param text
     * @param priority
     */
    void addText(HUDText text, double priority, HUDRemover remover);

    void addPackage(HUDPackage hudPackage);

    void removeText(HUDText text);

    void removePackage(HUDPackage hudPackage);

    void ignore(double limit);

    boolean setActive(boolean active);

}
