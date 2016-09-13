package com.exorath.exoHUD.api.texts;

import com.exorath.exoHUD.api.HUDText;
import io.reactivex.Observable;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by toonsev on 9/13/2016.
 */
public abstract class CompositeTextBase implements HUDText {
    private List<HUDText> hudTexts;

    public CompositeTextBase(HUDText... hudTexts){
        this.hudTexts = Arrays.asList(hudTexts);
    }

    public CompositeTextBase(List<HUDText> hudTexts){
        this.hudTexts = hudTexts;
    }
    public List<HUDText> getHudTexts() {
        return hudTexts;
    }

    public List<Observable<List<TextComponent>>> getCompositeObservables() {
        return hudTexts.stream().map(hudText -> hudText.getTextObservable()).collect(Collectors.toList());
    }
}
