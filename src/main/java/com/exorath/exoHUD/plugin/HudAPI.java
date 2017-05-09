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

package com.exorath.exoHUD.plugin;

import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * Created by toonsev on 4/28/2017.
 */
public class HudAPI {
    private HashMap<Player, HudPlayer>  players = new HashMap<>();
    public HudPlayer getHudPlayer(Player player){
        return players.get(player);
    }

    protected void onJoin(Player player){
        players.put(player, new HudPlayer(player));
    }

    protected void onLeave(Player player){
        players.remove(player);
    }

    public static HudAPI getInstance(){
        return HudPlugin.getInstance().getHudAPI();
    }
}
