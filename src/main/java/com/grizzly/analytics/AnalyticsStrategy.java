/*
 * Copyright (c) 2014.  Fco Pardo Baeza
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package com.grizzly.analytics;

/**
 * Created on 11-06-14.
 * An enum type. Useful for Switch operations. Represents the available analytics strategies.
 */
public enum AnalyticsStrategy {

    /**
     * The current strategies.
     */
    GOOGLE(1), API(2), LOCAL(3),
    GOOGLE_API(4), GOOGLE_LOCAL(5),
    API_LOCAL(6), ALL(7);

    private int value = 1;

    private AnalyticsStrategy(int value) {
        if (value > 0 && value < 8) {
            this.value = value;
        } else {
            this.value = 1;
        }
    }

    public void setValue(int value) {
        if (value > 0 && value < 8) {
            this.value = value;
        } else {
            this.value = 1;
        }
    }

    public int getValue() {
        return value;
    }
}
