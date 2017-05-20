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

import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Score;

import com.google.common.base.Splitter;

/**
 * Created by TOON on 8/12/2015.
 */
public class ScoreboardEntry {
    private ScoreboardBase scoreboard;
    private String name;
    private org.bukkit.scoreboard.Team team;
    private Score score;
    private int value;

    private String origName;
    private int count;

    public ScoreboardEntry(ScoreboardBase scoreboard, int value) {
        this.scoreboard = scoreboard;
        this.value = value;
        count = 0;
    }

    public ScoreboardEntry(ScoreboardBase scoreboard, int value, String origName, int count) {
        this.scoreboard = scoreboard;
        this.value = value;
        this.origName = origName;
        this.count = count;
    }

    public ScoreboardBase getScoreboard() {
        return scoreboard;
    }

    public String getName() {
        return name;
    }

    public org.bukkit.scoreboard.Team getTeam() {
        return team;
    }

    public Score getScore() {
        return score;
    }

    public int getValue() {
        return score != null ? (value = score.getScore()) : value;
    }

    public void setValue(int value) {
        if (!score.isScoreSet())
            score.setScore(-1);
        score.setScore(value);
    }

    public void update(String newName) {
       update(newName, getValue());
    }

    public void update(String newName, int value){
        if (origName != null && newName.equals(origName))
            for (int i = 0; i < count; i++)
                newName = ChatColor.RESET + newName;
        else if (newName.equals(name))
            return;

        create(newName);
        setValue(value);
    }

    void remove() {
        if (score != null)
            score.getScoreboard().resetScores(score.getEntry());

        if (team != null && scoreboard.getScoreboard().getTeams().contains(team))
            team.unregister();
    }

    private void create(String name) {
        this.name = name;
        remove();

        if (name.length() <= 16) {
            int value = getValue();
            score = scoreboard.getObjective().getScore(name);
            score.setScore(value);
            return;
        }

        team = scoreboard.getScoreboard().registerNewTeam("scoreboard-" + scoreboard.getTeamId());
        Iterator<String> iterator = Splitter.fixedLength(16).split(name).iterator();
        if (name.length() > 16)
            team.setPrefix(iterator.next());
        String entry = iterator.next();
        score = scoreboard.getObjective().getScore(entry);
        if (name.length() > 32)
            team.setSuffix(iterator.next());

        team.addEntry(entry);
    }
}