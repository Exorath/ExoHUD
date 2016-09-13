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

import com.exorath.versions.api.Version;
import com.exorath.versions.api.VersionAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by toonsev on 8/30/2016.
 */
public class TitleLib {
    private VersionAPI<TitleHandler> versionAPI = VersionAPI.create(Version.create("test"));

    public TitleLib() {
        versionAPI.registerDefault(() -> new ReflectionTitleHandler(Bukkit.getServer().getClass().getName().split("\\.")[3]));
    }

    public void clearTitle(Player player) {
        TitleHandler handler = versionAPI.get();
        if (handler != null)
            handler.clear(player);
    }

    public void send(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        TitleHandler handler = versionAPI.get();
        if (handler != null)
            handler.send(player, fadeIn, stay, fadeOut, title, subtitle);
    }
}
