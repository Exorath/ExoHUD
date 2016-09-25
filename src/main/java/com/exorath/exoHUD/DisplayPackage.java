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

import java.util.Comparator;

/**
 * Created by toonsev on 9/24/2016.
 */
public class DisplayPackage {
    public static final Comparator<DisplayPackage> COMPARATOR_PRIORITY = new DisplayPackageComparator(DisplayProperties.COMPARATOR_PRIORITY);
    private HUDPackage hudPackage;
    private DisplayProperties properties;

    public DisplayPackage(HUDPackage hudPackage, DisplayProperties properties) {
        this.hudPackage = hudPackage;
        this.properties = properties;
    }

    public HUDPackage getHudPackage() {
        return hudPackage;
    }

    public DisplayProperties getProperties() {
        return properties;
    }

    public static class DisplayPackageComparator implements Comparator<DisplayPackage> {
        private Comparator<DisplayProperties> comparator;

        public DisplayPackageComparator(Comparator<DisplayProperties> comparator) {
            this.comparator = comparator;
        }

        @Override
        public int compare(DisplayPackage first, DisplayPackage second) {
            return comparator.compare(first.getProperties(), second.getProperties());
        }
    }
}
