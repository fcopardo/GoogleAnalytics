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
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.grizzly.analytics.DefinitionsAnalytics;
import com.grizzly.analytics.Logging.BaseAnalyticsEventLog;
import com.grizzly.analytics.Logging.BaseAnalyticsLog;

import java.util.HashMap;

/**
 * Class in charge of managing google analytics.
 * Created on 10-06-14.
 */
public class AnalyticsByGoogle {

    /**
     * Class members.
     * mTrackers: The available trackers.
     * googleAnalytics: Singleton instance of google Analytics.
     * myContext: android app context provider.
     */
    private static HashMap<Trackers.TrackerName, Tracker> mTrackers;
    private static com.google.android.gms.analytics.GoogleAnalytics googleAnalytics;
    private static Context myContext;
    private static int myGlobalTracker = 0;
    private static int myEcommerceTracker = 0;

    /**
     * Initializes the analytics singleton. Requires a valid context.
     *
     * @param context
     */
    public static void initializeGoogleAnalytics(Context context, int MyGlobalTracker, int MyEcommerceTracker) {

        myContext = context;
        myGlobalTracker = MyGlobalTracker;
        myEcommerceTracker = MyEcommerceTracker;
        if (googleAnalytics == null) {
            resetGoogleAnalytics(myContext);
        }
    }

    /**
     * Resets the context and analytics singleton.
     *
     * @param context
     */
    public static void resetGoogleAnalytics(Context context) {
        myContext = context;
        googleAnalytics = com.google.android.gms.analytics.GoogleAnalytics.getInstance(context.getApplicationContext());
    }

    /**
     * Tracker provider. Creates and/or returns singleton instances of each tracker.
     *
     * @param trackerId
     * @return
     */
    private static synchronized Tracker getTracker(Trackers.TrackerName trackerId) {

        if (mTrackers == null) {
            mTrackers = new HashMap<Trackers.TrackerName, Tracker>();
        }

        resetGoogleAnalytics(myContext);

        if (!mTrackers.containsKey(trackerId)) {

            Tracker t = (trackerId == Trackers.TrackerName.APP_TRACKER) ? googleAnalytics.newTracker(DefinitionsAnalytics.getPropertyApp())
                    : (trackerId == Trackers.TrackerName.GLOBAL_TRACKER) ? googleAnalytics.newTracker(myGlobalTracker)
                    : googleAnalytics.newTracker(myEcommerceTracker);
            mTrackers.put(trackerId, t);

        }
        return mTrackers.get(trackerId);
    }


    /**
     * Send the desired data to google servers. It requires a subclass of BaseAnalyticsLog as argument.
     *
     * @param logData the data to be sent.
     * @param <T>     any class extending BaseAnalyticsLog
     */
    public static <T extends BaseAnalyticsLog> void track(T logData) {

        Tracker t = AnalyticsByGoogle.getTracker(Trackers.TrackerName.APP_TRACKER);

        t.setScreenName(logData.getWindow());
        t.setAppId(logData.getApp());
        t.setAppInstallerId(logData.getDevice());

        if (logData instanceof BaseAnalyticsEventLog) {

            if (!logData.getFreeValues().isEmpty()) {

                t.send(new HitBuilders.EventBuilder()
                        .setCategory(((BaseAnalyticsEventLog) logData).getCategory())
                        .setAction(((BaseAnalyticsEventLog) logData).getAction())
                        .setLabel(((BaseAnalyticsEventLog) logData).getLabel())
                        .setValue(((BaseAnalyticsEventLog) logData).getValue())
                        .setAll(logData.getFreeValues())
                        .build());
            } else {

                t.send(new HitBuilders.EventBuilder()
                        .setCategory(((BaseAnalyticsEventLog) logData).getCategory())
                        .setAction(((BaseAnalyticsEventLog) logData).getAction())
                        .setLabel(((BaseAnalyticsEventLog) logData).getLabel())
                        .setValue(((BaseAnalyticsEventLog) logData).getValue())
                        .build());
            }

        } else {


            for (String s : logData.loadSendvalues().keySet()) {
                try{
                    t.set(s, logData.loadSendvalues().get(s).toString());
                }
                catch(NullPointerException e){
                    e.printStackTrace();
                }
            }

            if (logData.getFreeValues().isEmpty()) {
                t.send(new HitBuilders.EventBuilder().build());
            } else {
                t.send(new HitBuilders.EventBuilder()
                                .setAll(logData.getFreeValues())
                                .build()
                );
            }

        }
    }
}
