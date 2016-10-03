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

package com.exorath.exoHUD.locations.simple;

import com.exorath.exoHUD.*;
import com.exorath.exoHUD.libs.title.TitleHandler;
import com.exorath.exoHUD.texts.PlainText;
import com.exorath.exoHUD.texts.PlainTextTest;
import io.reactivex.Completable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by toonsev on 10/1/2016.
 */
public class TitleLocationTest {
    private Player playerMock;
    private TitleHandler titleHandlerMock;
    private TitleLocation titleLocation;

    private String legacyTitle, legacySubtitle;
    private TextComponent titleComponent, subtitleComponent;
    private HUDText titleText, subtitleText;
    private DisplayPackage titleDisplayPackage, subtitleDisplayPackage, titleSubtitleDisplayPackage;

    @Before
    public void setup(){
        playerMock = mock(Player.class);
        titleHandlerMock = mock(TitleHandler.class);
        titleLocation = new TitleLocation(playerMock, titleHandlerMock);

        titleComponent = new TextComponent("title");
        subtitleComponent = new TextComponent("subtitle");
        titleComponent.setColor(ChatColor.GREEN);
        titleComponent.setBold(true);
        legacyTitle = titleComponent.toLegacyText();
        legacySubtitle = subtitleComponent.toLegacyText();

        titleText = PlainText.component(titleComponent);
        subtitleText = PlainText.component(subtitleComponent);
        titleDisplayPackage = new DisplayPackage(HUDPackage.create(Arrays.asList(titleText)), DisplayProperties.create(0, () -> Completable.never()));
        subtitleDisplayPackage = new DisplayPackage(HUDPackage.create(Arrays.asList(null, subtitleText)), DisplayProperties.create(0, () -> Completable.never()));
        titleSubtitleDisplayPackage = new DisplayPackage(HUDPackage.create(Arrays.asList(titleText, subtitleText)), DisplayProperties.create(0, () -> Completable.never()));
    }

    @Test
    public void addDisplayPackageWithOneHUDTextCallsSendOnceWithLegacyTextTitleTest(){
        titleLocation.addDisplayPackage(titleDisplayPackage);
        verify(titleHandlerMock, times(1)).send(eq(playerMock), eq(0), anyInt(), anyInt(), eq(legacyTitle), isNull(String.class));
    }
    @Test
    public void addDisplayPackageWithOneNullAndOneHUDTextCallsSendOnceWithLegacyTextSubtitleAndFadeInAsDefinedInDisplayPackagePropertiesMetaTest(){
        int fadeIn = 20;
        subtitleDisplayPackage.getProperties().getMeta().set(TitleLocation.FADE_IN_PROPERTY, fadeIn);
        titleLocation.addDisplayPackage(subtitleDisplayPackage);
        verify(titleHandlerMock, times(1)).send(eq(playerMock), eq(fadeIn), anyInt(), anyInt(), isNull(String.class), eq(legacySubtitle));
    }


    @Test
    public void addDisplayPackageWithOneNullAndOneHUDTextCallsSendOnceWithLegacyTextSubtitleTest(){
        titleLocation.addDisplayPackage(subtitleDisplayPackage);
        verify(titleHandlerMock, times(1)).send(eq(playerMock), eq(0), anyInt(), anyInt(), isNull(String.class), eq(legacySubtitle));
    }

    @Test
    public void addDisplayPackageWithTwoHUDTextsCallsSendOnceWithLegacyTextTitleAndSubtitleTest(){
        titleLocation.addDisplayPackage(titleSubtitleDisplayPackage);
        verify(titleHandlerMock, times(1)).send(eq(playerMock), eq(0), anyInt(), anyInt(), eq(legacyTitle), eq(legacySubtitle));
    }


    @Test
    public void addDisplayPackageThatInstantlyRemovesItselfCallsClearOnce(){
        titleLocation.addDisplayPackage(new DisplayPackage(HUDPackage.create(Arrays.asList(PlainText.component(new TextComponent("title")))), DisplayProperties.create(1, () -> Completable.complete())));
        verify(titleHandlerMock, times(1)).clear(eq(playerMock));
    }

    @Test
    public void addDisplayPackageWithFadeOutMetaThatInstantlyRemovesItselfNeverCallsClear(){
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(Arrays.asList(PlainText.component(new TextComponent("title")))), DisplayProperties.create(1, () -> Completable.complete()));
        displayPackage.getProperties().getMeta().set(TitleLocation.FADE_OUT_PROPERTY, 5);
        titleLocation.addDisplayPackage(displayPackage);
        verify(titleHandlerMock, times(0)).clear(any());
    }

    @Test
    public void addDisplayPackageWithFadeOutMetaThatInstantlyRemovesItselfCallsSendOnceTest(){
        DisplayPackage displayPackage = new DisplayPackage(HUDPackage.create(Arrays.asList(titleText,subtitleText)), DisplayProperties.create(1, () -> Completable.complete()));
        displayPackage.getProperties().getMeta().set(TitleLocation.FADE_OUT_PROPERTY, 5);
        titleLocation.addDisplayPackage(displayPackage);
        verify(titleHandlerMock, times(0)).send(eq(playerMock), eq(0), eq(0), eq(5), eq(legacyTitle), eq(legacySubtitle));
    }
}
