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

import com.exorath.exoproperties.Property;
import io.reactivex.Completable;
import org.bukkit.material.Comparator;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by toonsev on 9/25/2016.
 */
public class DisplayPropertiesTest {
    private double priority = 3.35;
    private HUDRemover remover;

    private DisplayProperties.PriorityComparator priorityComparator;
    private DisplayProperties displayProperties;

    @Before
    public void setup(){
        this.remover = mock(HUDRemover.class);
        this.displayProperties = DisplayProperties.create(priority, remover);

        this.priorityComparator = new DisplayProperties.PriorityComparator();
    }

    @Test
    public void getPriorityNotNullTest(){
        assertNotNull(displayProperties.getPriority());
    }

    @Test
    public void getPriorityEqualsPriorityTest(){
        assertEquals(priority, displayProperties.getPriority(), 0);
    }

    @Test
    public void getRemoverNotNullTest(){
        assertNotNull(displayProperties.getRemover());
    }

    @Test
    public void getRemoverEqualsRemoverTest(){
        assertEquals(remover, displayProperties.getRemover());
    }


    @Test
    public void priorityComparatorComparesDifferentPrioritiesSameAsDoubleFirstHigherThenSecondTestComparator(){
        assertEquals(Double.compare(priority, 1), priorityComparator.compare(displayProperties, DisplayProperties.create(1, () -> Completable.never())));
    }

    @Test
    public void priorityComparatorComparesDifferentPrioritiesSameAsDoubleFirstLowerThenSecondTestComparator(){
        assertEquals(Double.compare(priority, 4), priorityComparator.compare(displayProperties, DisplayProperties.create(4, () -> Completable.never())));
    }

    @Test
    public void priorityComparatorComparesDifferentPrioritiesSameAsDoubleFirstSameAsSecondTestComparator(){
        assertEquals(Double.compare(priority, 3.35), priorityComparator.compare(displayProperties, DisplayProperties.create(3.35, () -> Completable.never())));
    }

    @Test
    public void getMetaNotNullByDefault(){
        assertNotNull(displayProperties.getMeta());
    }

    @Test
    public void getOnGetMetaReturnsSameValueThatWasSetOnGetMeta(){
        Property key = Property.create();
        Object value = "testobj";
        displayProperties.getMeta().set(key, value);
        assertEquals(value, displayProperties.getMeta().get(key));
    }
}
