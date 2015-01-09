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
 * Created on 09-06-14.
 */
public class DefinitionsAnalytics {

    /*
    AnalyticsByGoogle property key. Replace with a proper key in production mode.
     */
    private static final String PROPERTY_APP = "UA-51752258-1";


    /* AnalyticsByGoogle strategies accesors. They mst mimic the ones in AnalyticsStrategy.java.
    1 for Google analytics, 2 for our analytics API, 3 for Local storage, 4 for Google and API analytics,
    * 5 for Google and Local, 6 for API and Local and 7 for all of them.
    * */

    public static int STRATEGY_GOOGLE = 1;
    public static int STRATEGY_API = 2;
    public static int STRATEGY_LOCAL = 3;
    public static int STRATEGY_GOOGLE_AND_API = 4;
    public static int STRATEGY_GOOGLE_AND_LOCAL = 5;
    public static int STRATEGY_API_AND_LOCAL = 6;
    public static int STRATEGY_ALL = 7;

    public static String getPropertyApp() {
        return PROPERTY_APP;
    }

    /**
     * Evaluates if a given number is a valid strategy.
     * @param strategy the int representing the strategy.
     * @return true or false.
     */
    public static boolean isStrategy(int strategy) {
        if (strategy < STRATEGY_ALL && strategy > STRATEGY_GOOGLE) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * Creates an AnalyticsStrategy enum from an int. It's intended to be used with the AnalyticsStrategy
     * int field of the BaseModel class.
     * @param strategy the int representing the strategy.
     * @return a valid AnalyticsStrategy enum.
     */
    public static AnalyticsStrategy selectStrategy(int strategy) {


        if(!isStrategy(strategy)){
            strategy = STRATEGY_API;
        }

        AnalyticsStrategy analyticsStorage = AnalyticsStrategy.API;

        switch(strategy) {
            case 1:
                analyticsStorage = AnalyticsStrategy.GOOGLE;
                break;
            case 2:
                analyticsStorage = AnalyticsStrategy.API;
                break;
            case 3:
                analyticsStorage = AnalyticsStrategy.LOCAL;
                break;
            case 4:
                analyticsStorage = AnalyticsStrategy.GOOGLE_API;
                break;
            case 5:
                analyticsStorage = AnalyticsStrategy.GOOGLE_LOCAL;
                break;
            case 6:
                analyticsStorage = AnalyticsStrategy.API_LOCAL;
                break;
            case 7:
                analyticsStorage = AnalyticsStrategy.ALL;
                break;
            default:break;
        }
        return analyticsStorage;
    }
}
