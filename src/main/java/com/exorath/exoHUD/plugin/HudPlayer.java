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

import com.exorath.exoHUD.libs.actionbar.SpigotActionBarHandler;
import com.exorath.exoHUD.libs.title.ReflectionTitleHandler;
import com.exorath.exoHUD.locations.row.ScoreboardLocation;
import com.exorath.exoHUD.locations.simple.ActionBarLocation;
import com.exorath.exoHUD.locations.simple.BossBarLocation;
import com.exorath.exoHUD.locations.simple.TitleLocation;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by toonsev on 4/28/2017.
 */
public class HudPlayer {
    private ActionBarLocation actionBarLocation;
    private TitleLocation titleLocation;
    private ScoreboardLocation scoreboardLocation;
    private BossBarLocation bossBarLocation;

    public HudPlayer(Player player) {
        this.actionBarLocation = new ActionBarLocation(player, new SpigotActionBarHandler());
        this.titleLocation = new TitleLocation(player, new ReflectionTitleHandler(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3]));
        this.scoreboardLocation = new ScoreboardLocation(player);
        this.bossBarLocation = new BossBarLocation(player);
    }

    public BossBarLocation getBossBarLocation() {
        return bossBarLocation;
    }

    public ActionBarLocation getActionBarLocation() {
        return actionBarLocation;
    }

    public TitleLocation getTitleLocation() {
        return titleLocation;
    }

    public ScoreboardLocation getScoreboardLocation() {
        return scoreboardLocation;
    }
}
