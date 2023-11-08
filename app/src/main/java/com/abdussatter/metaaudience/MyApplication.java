package com.abdussatter.metaaudience;

import android.app.Application;

import com.facebook.ads.AdSettings;
import com.facebook.ads.AudienceNetworkAds;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        AudienceNetworkAds.initialize(this);
        AdSettings.addTestDevice("71d275dd-bbfc-4a39-b326-4fc42f3e2121");
        AdSettings.addTestDevice("c802ce3d-a873-43a2-9d48-73e586875f0e");
        AdSettings.addTestDevice("e70b456a-c9ed-481f-948d-eb4971ccaf84");
        super.onCreate();
    }
}
