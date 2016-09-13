package com.exorath.exoHUD.api.texts;

import com.exorath.exoHUD.api.HUDText;
import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import net.md_5.bungee.api.chat.TextComponent;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by toonsev on 9/13/2016.
 */
public class IterateText extends CompositeTextBase {
    private long interval;
    private TimeUnit intervalUnit;
    private Scheduler scheduler;

    public IterateText(int interval, TimeUnit intervalUnit, Scheduler scheduler, HUDText... hudTexts) {
        super(hudTexts);
        this.interval = interval;
        this.intervalUnit = intervalUnit;
        this.scheduler = scheduler;
    }

    @Override
    public Observable<List<TextComponent>> getTextObservable() {
        return Observable.interval(0, interval, intervalUnit, scheduler)
                .flatMap(iteration -> getHudTexts().get((int) (iteration % getHudTexts().size())).getTextObservable());
    }

    public static final IterateText iterate(int interval, TimeUnit intervalUnit, HUDText... hudTexts) {
        return new IterateText(interval, intervalUnit, Schedulers.computation(), hudTexts);
    }
}
