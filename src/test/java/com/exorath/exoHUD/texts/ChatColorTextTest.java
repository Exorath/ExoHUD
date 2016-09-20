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

package com.exorath.exoHUD.texts;

import com.exorath.exoHUD.HUDText;
import io.reactivex.Observable;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by toonsev on 9/11/2016.
 */
public class ChatColorTextTest {
    private  TextComponent component;
    private HUDText mockedText;
    private ChatColorText chatColorText;

    @Before
    public void setup(){
        component = new TextComponent("Sampletext");
        mockedText = mock(HUDText.class);
        when(mockedText.getTextObservable()).thenReturn(Observable.just(Arrays.asList(component)));
        chatColorText = new ChatColorText(mockedText);
    }

    @Test
    public void getTextObservableCompletesTest(){
        chatColorText.getTextObservable().test().assertComplete();
    }

    @Test
    public void getTextObservableEmitsSingleTest(){
        chatColorText.getTextObservable().test().assertValueCount(1);
    }

    @Test
    public void getTextObservableEmitsValueTest(){
        chatColorText.getTextObservable().test().assertValue(Arrays.asList(component));
    }

    //bold
    @Test
    public void getTextObservableEmitsNullBoldedValueWhenNotBoldSetTest(){
        assertNull(chatColorText.getTextObservable().blockingFirst().get(0).isBoldRaw());
    }

    @Test
    public void getTextObservableEmitsTrueBoldedValueWhenBoldSetTrueTest(){
        assertTrue(chatColorText.bold(true).getTextObservable().blockingFirst().get(0).isBoldRaw());
    }

    @Test
    public void getTextObservableEmitsFalseBoldedValueWhenBoldSetFalseTest(){
        assertFalse(chatColorText.bold(false).getTextObservable().blockingFirst().get(0).isBoldRaw());
    }
    //italic
    @Test
    public void getTextObservableEmitsNullItalicValueWhenNotItalicSetTest(){
        assertNull(chatColorText.getTextObservable().blockingFirst().get(0).isItalicRaw());
    }

    @Test
    public void getTextObservableEmitsTrueItalicValueWhenItalicSetTrueTest(){
        assertTrue(chatColorText.italic(true).getTextObservable().blockingFirst().get(0).isItalicRaw());
    }

    @Test
    public void getTextObservableEmitsFalseItalicValueWhenItalicSetFalseTest(){
        assertFalse(chatColorText.italic(false).getTextObservable().blockingFirst().get(0).isItalicRaw());
    }

    //obfuscated
    @Test
    public void getTextObservableEmitsNullObfuscatedValueWhenNotObfuscatedSetTest(){
        assertNull(chatColorText.getTextObservable().blockingFirst().get(0).isObfuscatedRaw());
    }

    @Test
    public void getTextObservableEmitsTrueObfuscatedValueWhenObfuscatedSetTrueTest(){
        assertTrue(chatColorText.obfuscated(true).getTextObservable().blockingFirst().get(0).isObfuscatedRaw());
    }

    @Test
    public void getTextObservableEmitsFalseObfuscatedValueWhenObfuscatedSetFalseTest(){
        assertFalse(chatColorText.obfuscated(false).getTextObservable().blockingFirst().get(0).isObfuscatedRaw());
    }

    //Underlined
    @Test
    public void getTextObservableEmitsNullUnderlinedValueWhenNotUnderlinedSetTest(){
        assertNull(chatColorText.getTextObservable().blockingFirst().get(0).isUnderlinedRaw());
    }

    @Test
    public void getTextObservableEmitsTrueUnderlinedValueWhenUnderlinedSetTrueTest(){
        assertTrue(chatColorText.underlined(true).getTextObservable().blockingFirst().get(0).isUnderlinedRaw());
    }

    @Test
    public void getTextObservableEmitsFalseUnderlinedValueWhenUnderlinedSetFalseTest(){
        assertFalse(chatColorText.underlined(false).getTextObservable().blockingFirst().get(0).isUnderlinedRaw());
    }

    //StrikeThrough
    @Test
    public void getTextObservableEmitsNullStrikethroughValueWhenNotStrikethroughSetTest(){
        assertNull(chatColorText.getTextObservable().blockingFirst().get(0).isStrikethroughRaw());
    }

    @Test
    public void getTextObservableEmitsTrueStrikethroughValueWhenStrikethroughSetTrueTest(){
        assertTrue(chatColorText.strikethrough(true).getTextObservable().blockingFirst().get(0).isStrikethroughRaw());
    }

    @Test
    public void getTextObservableEmitsFalseStrikethroughValueWhenStrikethroughSetFalseTest(){
        assertFalse(chatColorText.strikethrough(false).getTextObservable().blockingFirst().get(0).isStrikethroughRaw());
    }

    //ChatColor
    @Test
    public void getTextObservableEmitsNullChatColorValueWhenNotChatColorSetTest(){
        assertNull(chatColorText.getTextObservable().blockingFirst().get(0).getColorRaw());
    }

    @Test
    public void getTextObservableEmitsAquaChatColorValueWhenChatColorSetAquaTest(){
        assertEquals(ChatColor.AQUA, chatColorText.color(ChatColor.AQUA).getTextObservable().blockingFirst().get(0).getColorRaw());
    }

    @Test
    public void getTextObservableEmitsNotAquaChatColorValueWhenChatColorNotSetAquaTest(){
        assertNotEquals(ChatColor.AQUA, chatColorText.color(ChatColor.BLACK).getTextObservable().blockingFirst().get(0).getColorRaw());
    }


    //All effects test
    @Test
    public void getTextObservableWithAllAppliedEffectsEmitsValueWithAllAppliedEffectsTest(){
        chatColorText.obfuscated(true).italic(true).strikethrough(true).bold(true).underlined(true).color(ChatColor.AQUA);
        TextComponent c = chatColorText.getTextObservable().blockingFirst().get(0);
        assertTrue(c.isObfuscated() && c.isItalic() && c.isStrikethrough() && c.isBold() && c.isUnderlined() && c.getColor() == ChatColor.AQUA);
    }

    @Test
    public void getTextObservableWithAllAppliedEffectsEmitsValueWithAllAppliedEffectsToComponentsExtraTest(){
        chatColorText.obfuscated(true).italic(true).strikethrough(true).bold(true).underlined(true).color(ChatColor.AQUA);
        component.addExtra(new TextComponent("Test"));
        BaseComponent c = chatColorText.getTextObservable().blockingFirst().get(0).getExtra().get(0);
        assertTrue(c.isObfuscated() && c.isItalic() && c.isStrikethrough() && c.isBold() && c.isUnderlined() && c.getColor() == ChatColor.AQUA);
    }

    @Test
    public void getTextObservableWithAllAppliedEffectsEmitsValueWithAllAppliedEffectsToComponentsExtraExtraTest(){
        chatColorText.obfuscated(true).italic(true).strikethrough(true).bold(true).underlined(true).color(ChatColor.AQUA);
        TextComponent extra = new TextComponent("Test1");
        extra.addExtra(new TextComponent("Test2"));
        component.addExtra(extra);
        BaseComponent c = chatColorText.getTextObservable().blockingFirst().get(0).getExtra().get(0).getExtra().get(0);
        assertTrue(c.isObfuscated() && c.isItalic() && c.isStrikethrough() && c.isBold() && c.isUnderlined() && c.getColor() == ChatColor.AQUA);
    }
}
