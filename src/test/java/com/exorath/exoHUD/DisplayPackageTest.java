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

package com.exorath.exoHUD;

import net.md_5.bungee.api.chat.TextComponent;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by toonsev on 9/25/2016.
 */
public class DisplayPackageTest {

    private HUDPackage hudPackage;
    private DisplayProperties displayProperties;

    private DisplayPackage displayPackage;

    @Before
    public void setup() {
        List<HUDText> texts = Arrays.asList(new HUDTextMock("test1", "test2").getMock(), new HUDTextMock("test3", "test4").getMock());

        hudPackage = mock(HUDPackage.class);
        when(hudPackage.getTexts()).thenReturn(texts);

        displayProperties = mock(DisplayProperties.class);
        when(displayProperties.getPriority()).thenReturn(123d);
        when(displayProperties.getRemover()).thenReturn(mock(HUDRemover.class));

        this.displayPackage = new DisplayPackage(hudPackage, displayProperties);
    }

    @Test
    public void getHUDPackageNotNullTest() {
        assertNotNull(displayPackage.getHudPackage());
    }

    @Test
    public void getHUDPackageEqualsHUDPackageTest() {
        assertEquals(hudPackage, displayPackage.getHudPackage());
    }

    @Test
    public void getPropertiesNotNullTest() {
        assertNotNull(displayPackage.getProperties());
    }

    @Test
    public void getPropertiesEqualsPropertiesTest() {
        assertEquals(displayProperties, displayPackage.getProperties());
    }
}
