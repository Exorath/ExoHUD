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

package com.exorath.exoHUD;

import io.reactivex.Completable;

/**
 * Created by toonsev on 9/15/2016.
 */
public interface HUDRemover {

    default void onDisplay(){}
    default void onDisplayRemoved(){}
    default void onComplete(){}

    /**
     * Gets a completable that completes when the HUD should be removed.
     * @return a completable that completes when the HUD should be removed
     */
    Completable getRemoveCompletable();
}
