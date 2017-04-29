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

package com.exorath.exoHUD;

import java.util.List;

/**
 * Created by toonsev on 9/21/2016.
 */
public class SimpleHUDPackage implements HUDPackage {
    private List<HUDText> texts;

    public SimpleHUDPackage(List<HUDText> texts) {
        this.texts = texts;
    }

    @Override
    public List<HUDText> getTexts() {
        return texts;
    }

}
