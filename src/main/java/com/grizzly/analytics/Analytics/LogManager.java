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

package com.grizzly.analytics.Analytics;

import android.content.Context;


import com.grizzly.analytics.DefinitionsAnalytics;
import com.grizzly.analytics.Logging.BaseAnalyticsLog;
import com.grizzly.analytics.SpecificMemoryStorage;

import java.util.HashMap;

/**
 * Analytics management class.
 * Created on 11-06-14.
 */
public class LogManager {

    /**
     * Class members.
     * context: Android application context.
     */
    private Context context;
    private SpecificMemoryStorage logMemoryStorage = new SpecificMemoryStorage();
    private int myGlobalTracker = 0;
    private int myEcommerceTracker = 0;


    public LogManager(Context Context, int MyGlobalTracker, int MyEcommerceTracker) {
        context = Context.getApplicationContext();
        myEcommerceTracker = MyEcommerceTracker;
        myGlobalTracker = MyGlobalTracker;
    }

    /**
     * Creates a event log in any of the recognized analytics providers.
     *
     * @param logObject the event to be logged.
     * @param <T>       A subclass of BaseAnalyticsLog.
     */
    public <T extends BaseAnalyticsLog> void createEventLog(T logObject) {

        if(logObject.getAnalyticsStrategies().contains(DefinitionsAnalytics.STRATEGY_GOOGLE)){
            doGoogleAnalytics(logObject);
        }

        if(logObject.getAnalyticsStrategies().contains(DefinitionsAnalytics.STRATEGY_API)){

        }

        if(logObject.getAnalyticsStrategies().contains(DefinitionsAnalytics.STRATEGY_LOCAL)){
            doStoreLog(logObject);
        }

    }

    /**
     * Wrapper method for google analytics.
     *
     * @param logObject the event to be logged.
     * @param <T>       A subclass of BaseAnalytics log.
     */
    private <T extends BaseAnalyticsLog> void doGoogleAnalytics(T logObject) {

        AnalyticsByGoogle.initializeGoogleAnalytics(context.getApplicationContext(), myGlobalTracker, myEcommerceTracker);
        AnalyticsByGoogle.track(logObject);
    }

    /**
     * Wrapper method for local storage of events.
     *
     * @param logObject the event to be logged.
     * @param <T>       A subclass of BaseAnalyticsLog.
     */
    private <T extends BaseAnalyticsLog> void doStoreLog(T logObject) {
        logMemoryStorage.create(logObject.getId(), logObject);
    }

    /**
     * Returns all the events of certain type from the local storage.
     *
     * @param logClass The type of event to be returned.
     * @param <T>      A valid subclass of BaseAnalyticsLog.
     * @return a HashMap with all the events of the desired type.
     */
    private <T extends BaseAnalyticsLog> HashMap<String, T> getTypedLogs(Class<T> logClass) {
        HashMap<String, T> map = new HashMap<>();
        try {
            T contrastObject = logClass.newInstance();

            for(String s: logMemoryStorage.getEntrySet()) {

                if (logMemoryStorage.getEntity(s, logClass).getClass().isInstance(contrastObject)) {
                    map.put(s, logMemoryStorage.getEntity(s, logClass));
                }
            }
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Deletes all the logs of the desired type from the local storage.
     *
     * @param logClass the type of event to be deleted.
     * @param <T>      a valid subclass of BaseAnalyticsLog.
     */
    private <T extends BaseAnalyticsLog> void deleteTypedLogs(Class<T> logClass) {

        HashMap<String, T> map = getTypedLogs(logClass);

        for (String s : map.keySet()) {
            logMemoryStorage.delete(s);
        }
    }
}
