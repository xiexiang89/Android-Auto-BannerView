/*
 * Copyright (C) 2016 The Edgar
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.edgar.transformers;

/**
 * Created by Edgar on 2016/12/15.
 */
public enum  TransformerType {
    Accordion(1),
    BackgroundToForeground(2),
    CubeIn(3),
    CubeOut(4),
    DepthPage(5),
    Fade(6),
    FlipHorizontal(7),
    FlipVertical(8),
    FlipPageView(9),
    ForegroundToBackground(10),
    RotateDown(11),
    RotateUp(12),
    Stack(13),
    Tablet(14),
    ZoomIn(15),
    ZoomOutSlide(16),
    ZoomOut(17);

    private int id;

    TransformerType(int id){
        this.id = id;
    }

    public static TransformerType convert(int id){
        TransformerType[] values = TransformerType.values();
        for (TransformerType type:values) {
            if (type.id == id){
                return type;
            }
        }
        return null;
    }
}