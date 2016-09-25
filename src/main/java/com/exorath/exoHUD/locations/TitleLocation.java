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

package com.exorath.exoHUD.locations;

import com.exorath.exoHUD.*;
import com.exorath.exoHUD.libs.title.TitleHandler;
import com.exorath.exoHUD.removers.NeverRemover;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * Created by toonsev on 9/20/2016.
 */
public class TitleLocation extends LocationBase {
    private TitleHandler titleHandler;


    public TitleLocation(TitleHandler titleHandler){
        this.titleHandler = titleHandler;
    }

    @Override
    public void onDisplayRemove(DisplayPackage displayPackage) {

    }

    @Override
    public void onDisplay(DisplayPackage displayPackage) {

    }

    @Override
    public void onHide(boolean hidden) {

    }
}
