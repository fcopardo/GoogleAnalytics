/*
 * Copyright (c) 2014. Fco Pardo Baeza.
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


import com.grizzly.analytics.Analytics.AnalyticsByGoogle;
import com.grizzly.analytics.Analytics.LogManager;
import com.grizzly.analytics.Logging.BaseAnalyticsEventLog;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created on 10-06-14.
 */
@RunWith(RobolectricTestRunner.class)
@Config(manifest= Config.NONE, emulateSdk = 18, shadows = {MyShadowApplication.class})
public class TestAnalytics extends BaseAndroidTestClass {


    /**
     * Tests the access to the Google Analytics services.
     */
    @Test
    public void test() {

        BaseAnalyticsEventLog testLog = new BaseAnalyticsEventLog();
        testLog.setAction("Testing");
        testLog.setCategory("Test");
        testLog.setLabel("Test operation");
        testLog.setApp("Demo app");
        testLog.setWindow(this.getClass().getCanonicalName());
        testLog.addAnalyticsStrategy(DefinitionsAnalytics.STRATEGY_GOOGLE);

        try{
            AnalyticsByGoogle.initializeGoogleAnalytics(this.getContext(), 0, 0);
            AnalyticsByGoogle.track(testLog);
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    /**
     * Same test, but through the analytics manager.
     */
    @Test
    public void testManager() {

        BaseAnalyticsEventLog testLog = new BaseAnalyticsEventLog();
        testLog.setAction("Testing");
        testLog.setCategory("Test");
        testLog.setLabel("Test operation");
        testLog.setApp("Demo app");
        testLog.setWindow(this.getClass().getCanonicalName());
        testLog.addAnalyticsStrategy(DefinitionsAnalytics.STRATEGY_GOOGLE);

        try{
            LogManager logManager = new LogManager(this.getContext(), 0, 0);
            logManager.createEventLog(testLog);
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }

}
