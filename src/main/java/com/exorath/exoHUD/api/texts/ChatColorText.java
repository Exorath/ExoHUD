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

package com.exorath.exoHUD.api.texts;

import com.exorath.exoHUD.api.HUDText;
import io.reactivex.Observable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

/**
 * Created by toonsev on 9/7/2016.
 */
public class ChatColorText implements HUDText {
    private HUDText hudText;
    private ChatColor chatColor;

    private Boolean bold;
    private Boolean italic;
    private Boolean obfuscated;
    private Boolean strikethrough;
    private Boolean underlined;

    public ChatColorText(HUDText hudText) {
        this.hudText = hudText;
    }
    @Override
    public Observable<List<TextComponent>> getTextObservable() {
        return hudText.getTextObservable().doOnNext(componentList -> update(componentList));
    }

    /**
     * Applies all the effects to the components and their extra's (and their extra's..).
     * @param components components to apply effects to
     */
    protected void update(List<TextComponent> components){
        components.forEach(component -> update(component));
    }
    protected void update(BaseComponent component){
        applyEffects(component);
        updateExtra(component);
    }

    /**
     * Updates the extras of a specific component (if it has extras).
     * @param component component to apply extras of
     */
    protected void updateExtra(BaseComponent component){
        if(component.getExtra() != null && component.getExtra().size() > 0)
            component.getExtra().forEach(extra -> update(extra));
    }

    /**
     * Applies all the effects to a specific component.
     * @param component component to apply effects to
     */
    protected void applyEffects(BaseComponent component){
        if(chatColor != null)
            component.setColor(chatColor);
        if(bold != null)
            component.setBold(bold);
        if(italic != null)
            component.setItalic(italic);
        if(obfuscated != null)
            component.setObfuscated(obfuscated);
        if(strikethrough != null)
            component.setStrikethrough(strikethrough);
        if(underlined != null)
            component.setUnderlined(underlined);
    }

    public ChatColorText color(ChatColor color){
        this.chatColor = color;
        return this;
    }

    public ChatColorText bold(boolean bold){
        this.bold = bold;
        return this;
    }

    public ChatColorText italic(boolean italic){
        this.italic = italic;
        return this;
    }
    public ChatColorText obfuscated(boolean obfuscated){
        this.obfuscated = obfuscated;
        return this;
    }

    public ChatColorText strikethrough(boolean strikethrough){
        this.strikethrough = strikethrough;
        return this;
    }

    public ChatColorText underlined(boolean underlined){
        this.underlined = underlined;
        return this;
    }
    public static final ChatColorText markup(HUDText hudText) {
        return new ChatColorText(hudText);
    }

}
