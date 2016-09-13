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

package com.exorath.exoHUD.impl.title;

import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

/**
 * Created by toonsev on 9/4/2016.
 */
public class ReflectionTitleHandler implements TitleHandler {
    private String nmsPackage;
    public ReflectionTitleHandler(String nmsPackage){
        this.nmsPackage = nmsPackage;
    }

    @Override
    public void send(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        try {
            if (title != null)
                sendTitle(player, title, fadeIn, stay, fadeOut);
            if (subtitle != null)
                sendSubtitle(player, subtitle, fadeIn, stay, fadeOut);
        } catch (Exception e) {e.printStackTrace();}
    }

    @Override
    public void clear(Player player) {
        send(player, 0, 1, 0, "", "");
    }

    private void sendTitle(Player player, String title, Integer fadeIn, Integer stay, Integer fadeOut) throws Exception {
        Object enumTitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
        Object chatTitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                .getMethod("a", new Class[]{String.class})
                .invoke(null, new Object[]{"{\"text\":\"" + title + "\"}"});
        Constructor<?> titleConstructor = getNMSClass("PacketPlayOutTitle")
                .getConstructor(new Class[]{getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                        getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE});
        Object titlePacket = titleConstructor
                .newInstance(new Object[]{enumTitle, chatTitle, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut)});
        sendPacket(player, titlePacket);
    }

    private void sendSubtitle(Player player, String subtitle, Integer fadeIn, Integer stay, Integer fadeOut) throws Exception {
        Object enumSubtitle = getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE")
                .get(null);
        Object chatSubtitle = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0]
                .getMethod("a", new Class[]{String.class})
                .invoke(null, new Object[]{"{\"text\":\"" + subtitle + "\"}"});
        Constructor<?> subtitleConstructor = getNMSClass("PacketPlayOutTitle")
                .getConstructor(new Class[]{getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0],
                        getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE});
        Object subtitlePacket = subtitleConstructor
                .newInstance(new Object[]{enumSubtitle, chatSubtitle, Integer.valueOf(fadeIn), Integer.valueOf(stay), Integer.valueOf(fadeOut)});
        sendPacket(player, subtitlePacket);
    }

    public Class<?> getNMSClass(String name) {
        try {
            return Class.forName(nmsPackage + "." + name);
        } catch (ClassNotFoundException e) {e.printStackTrace();}
        return null;
    }

    public void sendPacket(Player player, Object packet) {
        try {
            Object handle = player.getClass().getMethod("getHandle", new Class[0]).invoke(player, new Object[0]);
            Object playerConnection = handle.getClass().getField("playerConnection").get(handle);
            playerConnection.getClass().getMethod("sendPacket", new Class[]{getNMSClass("Packet")})
                    .invoke(playerConnection, new Object[]{packet});
        } catch (Exception e) {e.printStackTrace();}
    }
}
