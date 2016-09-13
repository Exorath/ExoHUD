package com.exorath.exoHUD.api.texts;

import com.exorath.exoHUD.api.HUDText;
import io.reactivex.Observable;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.Arrays;
import java.util.List;

/**
 * Created by toonsev on 9/7/2016.
 */
public class PlainText implements HUDText {
    private List<TextComponent> components;

    public PlainText(String text) {
        this(new TextComponent(text));
    }

    public PlainText(TextComponent component) {
        this(Arrays.asList(component));
    }

    public PlainText(List<TextComponent> components) {
        this.components = components;
    }

    @Override
    public Observable<List<TextComponent>> getTextObservable() {
        return Observable.just(components);
    }

    public static final PlainText plain(String text) {
        return new PlainText(text);
    }

    public static final PlainText component(TextComponent textComponent) {
        return new PlainText(textComponent);
    }
    public static final PlainText components(List<TextComponent> textComponents) {
        return new PlainText(textComponents);
    }
}
