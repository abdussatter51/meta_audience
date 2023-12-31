package com.abdussatter.metaaudience;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import java.util.Date;

public class MainActivity extends AppCompatActivity {

    TextView tvDisplay;
    int bannerAdClicked = 0;
    int fullScreenAdClicked = 0;
    private AdView adView;
    private InterstitialAd interstitialAd;
    private final String TAG = "FullScreenAd";
    int onOff = 0;
    Button showAdBtn;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvDisplay = findViewById(R.id.tvDisplay);
        showAdBtn = findViewById(R.id.showAdBtn);


        //=======================================================================================


        sharedPreferences = getSharedPreferences(""+getString(R.string.app_name),MODE_PRIVATE);
        editor = sharedPreferences.edit();
        long minute=1000*60*3;
        long bOldTime = sharedPreferences.getLong("bTime",0);
        long iOldTime = sharedPreferences.getLong("iTime",0);
        long bDifference = (System.currentTimeMillis()-bOldTime);
        long iDifference = (System.currentTimeMillis()-iOldTime);
        if(bDifference>=minute && onOff == 0){
            tvDisplay.setText("Banner "+Long.toString(bDifference));
            loadFBBannerAd();
        }
        if(iDifference>=minute && onOff == 0){
            tvDisplay.append("\nInterstitial "+ Long.toString(iDifference));
            loadFBFullScreenAd();
        }

        //=======================================================================================



        //loadBannerAd();
        //sharedPreferences();
        //loadBannerAd();
        //loadFullScreenAd();

        showAdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(interstitialAd != null && interstitialAd.isAdLoaded())
                    interstitialAd.show();
            }
        });
    }
    //End onCreate Bundle

    //============================================================================================
    private void loadFBFullScreenAd() {
        interstitialAd = new InterstitialAd(this, "YOUR_PLACEMENT_ID");
        sharedPreferences = getSharedPreferences(""+getString(R.string.app_name),MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Date date = new Date(System.currentTimeMillis());
        long millis = date.getTime();
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
                loadFBFullScreenAd();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad

            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
                editor.putLong("iTime",millis).apply();

                fullScreenAdClicked++;
                if(fullScreenAdClicked>=2){
                    if(interstitialAd!=null) interstitialAd.destroy();
                }

            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }


    //============================================================================================

    private void loadFBBannerAd(){
        adView = new AdView(this, "IMG_16_9_APP_INSTALL#YOUR_PLACEMENT_ID",AdSize.BANNER_HEIGHT_50);
        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
        adContainer.addView(adView);
        sharedPreferences = getSharedPreferences(""+getString(R.string.app_name),MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Date date = new Date(System.currentTimeMillis());
        long millis = date.getTime();
        //adView.loadAd();
        AdListener bannerAdListener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                //tvDisplay.append("\n"+adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                tvDisplay.append("\n"+"Add Loaded");

            }

            @Override
            public void onAdClicked(Ad ad) {

                bannerAdClicked++;
                //tvDisplay.append("\n"+"Add Clicked"+bannerAdClicked);
                editor.putLong("bTime",millis).apply();


                if(bannerAdClicked>=2){
                    if (adView != null) adView.destroy();
                    adContainer.setVisibility(View.GONE);
                }


            }
            @Override
            public void onLoggingImpression(Ad ad) {
            }
        };

        adView.loadAd(adView.buildLoadAdConfig().withAdListener(bannerAdListener).build());

    }
//=======================================================================================
    /*
    private int sharedPreferences(){
        sharedPreferences = getSharedPreferences(""+getString(R.string.app_name),MODE_PRIVATE);
        editor = sharedPreferences.edit();
        Date date = new Date(System.currentTimeMillis());
        long millis = date.getTime();
        long hour=1000*60*30;
        long oldTime = sharedPreferences.getLong("time",0);
        long difference = (System.currentTimeMillis()-oldTime);
        if(difference>=hour){
            return 0;
        }
        else
            return 1;

    }

     */

//=======================================================================================
    @Override
    protected void onDestroy() {
        if (adView != null) {
            adView.destroy();
        }
        if(interstitialAd != null){
            interstitialAd.destroy();
        }
        super.onDestroy();
    }

}