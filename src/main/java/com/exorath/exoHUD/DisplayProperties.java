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
public class DisplayProperties {
    public static final PriorityComparator COMPARATOR_PRIORITY = new PriorityComparator();

    private double priority;
    private HUDRemover remover;

    private DisplayProperties(double priority, HUDRemover remover){
        this.priority = priority;
        this.remover = remover;
    }

    public double getPriority() {
        return priority;
    }

    public HUDRemover getRemover() {
        return remover;
    }


    public static DisplayProperties create(double priority, HUDRemover remover){
        return new DisplayProperties(priority, remover);
    }

    /**
     * Note: this comparator imposes orderings that are inconsistent with equals.
     */
    static class PriorityComparator implements Comparator<DisplayProperties> {
        @Override
        public int compare(DisplayProperties first, DisplayProperties second) {
            if(first.getPriority() < second.getPriority())
                return -1;
            if(first.getPriority() == second.getPriority())
                return 0;
            return 1;
        }
    }
}
