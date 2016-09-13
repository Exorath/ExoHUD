package com.exorath.exoHUD.api.texts;

import com.exorath.exoHUD.api.HUDText;
import io.reactivex.Observable;
import io.reactivex.functions.Function;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by toonsev on 9/7/2016.
 */
public class CompositeText extends CompositeTextBase {
    public CompositeText(HUDText... hudTexts){
        super(hudTexts);
    }
    @Override
    public Observable<List<TextComponent>> getTextObservable() {
        //Check
        return Observable.combineLatest(getCompositeObservables(), new Function<Object[], List<TextComponent>>() {
            @Override
            public List<TextComponent> apply(Object[] componentLists) throws Exception {
                return getComposite(componentLists);
            }
        });
    }

    protected static List<TextComponent> getComposite(Object... componentListObjects) {
        List<TextComponent> composite = new ArrayList<>();
        List<TextComponent>[] componentLists = Arrays.copyOf(componentListObjects, componentListObjects.length, List[].class);
        Arrays.asList(componentLists).forEach((componentList) -> composite.addAll(componentList));
        return composite;
    }

    public static final CompositeText composite(HUDText... texts) {
        return new CompositeText(texts);
    }
}
