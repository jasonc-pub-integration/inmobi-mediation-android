package com.vartyr.inmobi_mediation_android;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.aerserv.sdk.AerServBanner;
import com.aerserv.sdk.AerServConfig;
import com.aerserv.sdk.AerServEvent;
import com.aerserv.sdk.AerServEventListener;
import com.aerserv.sdk.AerServInterstitial;
import com.aerserv.sdk.AerServSdk;
import com.aerserv.sdk.AerServTransactionInformation;
import com.aerserv.sdk.utils.UrlBuilder;

import java.util.List;

public class MainActivity extends Activity {

    private AerServBanner banner;               // AS Banner
    private AerServInterstitial interstitial;   // AS Interstitial
    // private List<DTBAdResponse> responses;      // A9 AD responses TODO: For a later workshop on A9 integration


    private Boolean preloadBannerReady = false;
    private Boolean preloadInterstitialReady = false;


    private final String LOG_TAG = "[INMOBIEXAMPLE]";
    private final String APP_ID = "380000";
    private final String AS_BANNER_PLC = "380000";
    private final String AS_INTERSTITIAL_PLC = "380003";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initSDK();                      // Init all SDKs
        drawView();                     // Draw all necessary views

//        preloadBanner();                // Config and preload a banner
//        preloadInterstitial();          // Config and preload interstitial

    }


    //
    protected void initSDK(){

        AerServSdk.init(this, APP_ID);      // Initialize SDK (AerServ)


        // TODO: Init other SDK (ie: A9, etc) as required

    }


    protected void drawView(){

        // TODO: Do something with the view
        TextView SDKlabel = findViewById(R.id.coreSDKVersionLabel);
        SDKlabel.setText("Core SDK Version: " + getString(R.string.aerserv_sdk_version, UrlBuilder.VERSION));

    }


    // "Preload" a banner
    public void preloadBanner(View view) {

        if (banner != null) {
            banner.kill();          // Kill the existing banner
        }


        final AerServConfig config = new AerServConfig(this, AS_BANNER_PLC)
                .setDebug(true)
                .setEventListener(bannerListener)
                .setRefreshInterval(30)
                .setA9AdResponses(null)
                .setPreload(true);
        banner = findViewById(R.id.banner);
        banner.configure(config);

    }


    // Preload an interstitial
    public void preloadInterstitial(View view) {

        final AerServConfig config = new AerServConfig(this, AS_INTERSTITIAL_PLC)
                .setDebug(true)
                .setEventListener(interstitialListener)
                .setA9AdResponses(null)
                .setPreload(true);
        interstitial = new AerServInterstitial(config);

    }


    // Show the banner only if the flag is set to true
    public void showPreloadedBanner(View view) {
        if (banner != null && preloadBannerReady) {
            banner.show();
            preloadBannerReady = false;
            Log.d(LOG_TAG, "Banner shown");
        } else {
            Log.d(LOG_TAG, "Banner is null or not ready");
        }
    }



    // Show the interstitial only if the flag is set to true
    public void showPreloadedInterstitial(View view) {
        if (interstitial != null && preloadInterstitialReady) {
            interstitial.show();
            preloadInterstitialReady = false;
            Log.d(LOG_TAG, "Interstitial shown");
        } else {
            Log.d(LOG_TAG, "Interstitial is null or not ready");
        }
    }




    // Set up a listener to listen to incoming AS events from the banner
    protected AerServEventListener bannerListener = new AerServEventListener() {
        @Override
        public void onAerServEvent(final AerServEvent event, final List<Object> args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AerServTransactionInformation ti;
                    switch (event) {
                        case PRELOAD_READY:
                            preloadBannerReady = true;
                            Log.d(LOG_TAG, "Preload ready for banner!");
                            break;
                        case AD_FAILED:
                            if (args.size() > 0) {
                                Log.d(LOG_TAG, "AD FAILED"
                                        + " Error code: " + AerServEventListener.AD_FAILED_CODE
                                        + ", reason=" + AerServEventListener.AD_FAILED_REASON);
                            } else {
                                Log.d(LOG_TAG, "AD FAILED, no error code / reason");
                            }
                            break;
                        case LOAD_TRANSACTION:
                            if (args.size() >= 1) {
                                Log.d(LOG_TAG, "Load Transaction Information PLC has:" + args.get(0));
                            }
                            else {
                                Log.d(LOG_TAG, "Load Transaction Information PLC has no information");
                            }
                            break;
                        case AD_IMPRESSION:
                            Log.d(LOG_TAG, "AD IMPRESSION");
                            break;
                        case AD_LOADED:
                            Log.d(LOG_TAG, "AD LOADED");
                            break;
                        case AD_DISMISSED:
                            Log.d(LOG_TAG, "AD DISMISSED");
                            break;
                    }
                }
            });
        }
    };




    // Set up a listener to listen to incoming AS events from the banner
    protected AerServEventListener interstitialListener = new AerServEventListener() {
        @Override
        public void onAerServEvent(final AerServEvent event, final List<Object> args) {
            MainActivity.this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    AerServTransactionInformation ti;
                    switch (event) {
                        case PRELOAD_READY:
                            preloadInterstitialReady = true;
                            Log.d(LOG_TAG, "Preload ready for interstitial!");
                            break;
                        case AD_FAILED:
                            if (args.size() > 0) {
                                Log.d(LOG_TAG, "AD FAILED"
                                        + " Error code: " + AerServEventListener.AD_FAILED_CODE
                                        + ", reason=" + AerServEventListener.AD_FAILED_REASON);
                            } else {
                                Log.d(LOG_TAG, "AD FAILED, no error code / reason");
                            }
                            break;
                        case LOAD_TRANSACTION:
                            if (args.size() >= 1) {
                                Log.d(LOG_TAG, "Load Transaction Information PLC has:" + args.get(0));
                            }
                            else {
                                Log.d(LOG_TAG, "Load Transaction Information PLC has no information");
                            }
                            break;
                        case AD_IMPRESSION:
                            Log.d(LOG_TAG, "AD IMPRESSION");
                            break;
                        case AD_LOADED:
                            Log.d(LOG_TAG, "AD LOADED");
                            break;
                        case AD_DISMISSED:
                            Log.d(LOG_TAG, "AD DISMISSED");
                            break;
                    }
                }
            });
        }
    };


}
