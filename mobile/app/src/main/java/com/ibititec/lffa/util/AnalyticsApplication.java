/*
 * Copyright Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ibititec.lffa.util;

import android.app.Activity;
import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.ibititec.lffa.MainActivity;
import com.ibititec.lffa.R;

/**
 * This is a subclass of {@link Application} used to provide shared objects for this app, such as
 * the {@link Tracker}.
 */
public class AnalyticsApplication extends Application {
    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        try {
            if (mTracker == null) {
                GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
                // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
                mTracker = analytics.newTracker(R.xml.global_tracker);
            }
            return mTracker;
        }catch (Exception ex){
            Log.i(MainActivity.TAG,"Erro: getDefaultTracker: "+ ex.getLocalizedMessage());
            return  mTracker;
        }
    }

    public static void enviarGoogleAnalitcs(Activity activity) {
        try {
            AnalyticsApplication application = (AnalyticsApplication) activity.getApplication();
            Tracker mTracker = application.getDefaultTracker();
            Log.i(MainActivity.TAG, "Setting screen name: " + activity.getTitle());
            mTracker.setScreenName("Tela: " + activity.getTitle());
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        } catch (Exception e) {
            Log.i(MainActivity.TAG, "Erro: Setting screen name: " + activity.getTitle() + " - " + e.getMessage());
        }
    }
}