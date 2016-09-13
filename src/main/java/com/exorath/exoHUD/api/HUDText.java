package com.exorath.exoHUD.api;

import io.reactivex.Observable;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;

/**
 * Created by toonsev on 9/6/2016.
 */
public interface HUDText {
    Observable<List<TextComponent>> getTextObservable();
}
