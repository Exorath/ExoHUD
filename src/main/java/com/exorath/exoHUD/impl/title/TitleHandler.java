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
