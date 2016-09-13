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
