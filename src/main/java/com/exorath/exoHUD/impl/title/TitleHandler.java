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

import com.exorath.versions.api.VersionHandler;
import org.bukkit.entity.Player;

/**
 * Created by toonsev on 9/3/2016.
 */
public interface TitleHandler extends VersionHandler {

    void send(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle);

    void clear(Player player);
}
