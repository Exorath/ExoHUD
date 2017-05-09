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

package com.exorath.exoHUD.libs.scoreboard;

/**
 * Created by toonsev on 5/9/2017.
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.BiMap;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;

import com.google.common.collect.HashBiMap;

/**
 * Created by TOON on 8/12/2015.
 */
public class ScoreboardBase {

    private org.bukkit.scoreboard.Scoreboard scoreboard;
    private Objective objective;//Bukkit objective
    private Set<ScoreboardEntry> entries = new HashSet<>();

    private int teamId;

    public ScoreboardBase(String title) {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        objective = scoreboard.registerNewObjective("scoreobj", "dummy");//Register new dummy objective on the scoreboard
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);
        setTitle(title);

        teamId = 1;
    }

    public org.bukkit.scoreboard.Scoreboard getScoreboard() {
        return scoreboard;
    }

    public Objective getObjective() {
        return objective;
    }

    public void setTitle(String title) {
        add();
        objective.setDisplayName(title);
    }

    public Set<ScoreboardEntry> getEntries() {
        return entries;
    }

    public ScoreboardEntry add(String name, int value) {
        return add(name, value, false);
    }

    public ScoreboardEntry add(String name, int value, boolean overwrite) {
        add();
//        if (overwrite && contains(name)) {
//            ScoreboardEntry entry = getEntryByName(name);
//            if (key != null && entries.get(key) != entry)
//                throw new IllegalArgumentException(
//                        "Key references different score than one to be overwritten");
//
//            entry.setValue(value);
//            return entry;
//        }
        int count = 0;
        String origName = name;
        if (!overwrite) {
            Map<Integer, String> created = create(name);
            for (Map.Entry<Integer, String> entry : created.entrySet()) {
                count = entry.getKey();
                name = entry.getValue();
            }
        }

        ScoreboardEntry entry = new ScoreboardEntry(this, value, origName, count);
        entry.update(name);
        entries.add(entry);
        return entry;
    }


    public void remove(ScoreboardEntry entry) {
        add();
        if (entry.getScoreboard() != this)
            throw new IllegalArgumentException("Entry doesn't belong to this scoreboard");
        entries.remove(entry);

        entry.remove();
    }

    private Map<Integer, String> create(String name) {
        int count = 0;
        while (contains(name)) {
            name = ChatColor.RESET + name;
            count++;
        }

        if (name.length() > 48)
            name = name.substring(0, 47);

        if (contains(name))
            throw new IllegalArgumentException("Could not find a suitable replacement name for '" + name + "'");

        Map<Integer, String> created = new HashMap<Integer, String>();
        created.put(count, name);
        return created;
    }

    public int getTeamId() {
        return teamId++;
    }

    public ScoreboardEntry getEntryByName(String name) {
        for (ScoreboardEntry entry : entries)
            if (entry.getName().equals(name))
                return entry;

        return null;
    }

    public boolean contains(String name) {
        for (ScoreboardEntry entry : entries)
            if (entry.getName().equals(name))
                return true;

        return false;
    }

    private boolean enabled = false;
    private Player player;

    public boolean isEnabled() {
        return enabled;
    }

    public void add(Player player) {
        this.player = player;
        add();
    }

    public void remove(Player player) {
        player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
    }

    public void add() {
        if (player != null)
            return;
        enabled = true;
        player.setScoreboard(scoreboard);
    }
}
