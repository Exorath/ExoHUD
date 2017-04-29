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

package com.exorath.exoHUD.libs.title;

import com.exorath.exoHUD.libs.NMSUtil;
import com.exorath.exoHUD.locations.simple.TitleLocation;
import com.exorath.versions.api.Version;
import com.exorath.versions.api.VersionAPI;
import org.bukkit.entity.Player;

/**
 * Created by toonsev on 8/30/2016.
 */
public class TitleLib {
    private VersionAPI<TitleHandler> versionAPI = VersionAPI.create(Version.create(NMSUtil.getVersion()));

    public TitleLib() {
        versionAPI.registerDefault(() -> new ReflectionTitleHandler(NMSUtil.getVersion()));
    }

    public TitleLocation getTitleLocation(Player player){
        return new TitleLocation(player, versionAPI.get());
    }
}
