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

package com.exorath.exoHUD.libs.actionbar;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

/**
 * Created by toonsev on 10/3/2016.
 */
public class SpigotActionBarHandler implements ActionBarHandler {
    @Override
    public void send(Player player, TextComponent... textComponents) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, textComponents);
    }

    @Override
    public void clear(Player player) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent());
    }
}
