package com.wandoujia.adssdk.sample;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.fyzb.gg.sdk.AdListener;
import com.fyzb.gg.sdk.Ads;
import com.fyzb.gg.sdk.InterstitialAd;
import com.fyzb.gg.sdk.loader.Fetcher;
import com.fyzb.gg.sdk.widget.AdBanner;

public class MainActivity extends Activity {

  private static final String TAG = "Ads-Sample";

  /*private static final String ADS_APP_ID = "100010385";
  private static final String ADS_SECRET_KEY = "770756af60b0cab6a278fcb6677adc8e";

  private static final String TAG_LIST = "d9fd4146d1ba23f3eb783cd986fa156e";
  private static final String TAG_INTERSTITIAL_FULLSCREEN = "0a6da2a473924007e4d520ddaca4f572";
  private static final String TAG_INTERSTITIAL_WIDGET = "0a6da2a473924007e4d520ddaca4f572";
  private static final String TAG_BANNER = "d60e08d4371dc29aed88512cd4b2634f";
  // private static final String TAG_INTERSTITIAL_FULLSCREEN = "Ads_show_in_fullScreen";
  // private static final String TAG_INTERSTITIAL_WIDGET = "Ads_show_as_widget";*/

  private static final String ADS_APP_ID = "100012357";
  private static final String ADS_SECRET_KEY = "6f1fa5e237fb4bf0212d398816b581bc";

  private static final String TAG_LIST = "244b0ea24d0c53326d4f85c0f996f6f4";
  private static final String TAG_INTERSTITIAL_FULLSCREEN = "35a652c746f35580134ba4aa702b154d";
  private static final String TAG_INTERSTITIAL_WIDGET = "35a652c746f35580134ba4aa702b154d";
  private static final String TAG_BANNER = "35a4ff2f4cddc106d3f77cae24e543db";

  private Button showAppWallButton;

  private AdBanner adBanner;
  private View adBannerView;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    showAppWallButton = (Button) findViewById(R.id.show_apps_button);

    findViewById(R.id.show_apps_button).setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        Ads.showAppWall(MainActivity.this, TAG_LIST);

      }

    });

    final InterstitialAd interstitialAd = new InterstitialAd(this, TAG_INTERSTITIAL_WIDGET);
    interstitialAd.setAdListener(new AdListener() {
      @Override
      public void onAdReady() {
        interstitialAd.show();
      }

      @Override
      public void onLoadFailure() {

      }

      @Override
      public void onAdPresent() {

      }

      @Override
      public void onAdDismiss() {

      }
    });


    findViewById(R.id.show_app_widget_button).setOnClickListener(new OnClickListener() {

      @Override
      public void onClick(View v) {
        interstitialAd.load();
      }

    });

    /*findViewById(R.id.show_app_widget_button_exception).setOnClickListener(
        new View.OnClickListener() {

          @Override
          public void onClick(View v) {
            Ads.showAppWidget(MainActivity.this, null, "A-TAG", Ads.ShowMode.FULL_SCREEN);

          }

        });*/

    findViewById(R.id.show_smart_ad_button).setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(MainActivity.this, SmartAdSample.class);
        startActivity(intent);
      }
    });

    try {
      Ads.init(this, ADS_APP_ID, ADS_SECRET_KEY);
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    Ads.preLoad(this, Fetcher.AdFormat.appwall, "APP", TAG_LIST, new AdListener() {

      @Override
      public void onAdReady() {
        if (Ads.getUpdateAdCount("APP") > 0) {
          drawUpdateIndicator(Color.RED, true);
        }
      }

      @Override
      public void onLoadFailure() {

      }

      @Override
      public void onAdPresent() {

      }

      @Override
      public void onAdDismiss() {

      }
    });

    Ads.preLoad(this, Fetcher.AdFormat.appwall, "GAME", TAG_LIST, new AdListener() {

      @Override
      public void onAdReady() {
        if (Ads.getUpdateAdCount("GAME") > 0) {
          drawUpdateIndicator(Color.GREEN, false);
        }
      }

      @Override
      public void onLoadFailure() {
        Toast.makeText(MainActivity.this, "网络异常，广告加载失败！", Toast.LENGTH_SHORT).show();
      }

      @Override
      public void onAdPresent() {

      }

      @Override
      public void onAdDismiss() {

      }
    });

    final ViewGroup adsWidgetContainer = (ViewGroup) findViewById(R.id.ads_widget_container);

    final Fetcher.AdFormat adFormat = Fetcher.AdFormat.interstitial;
    if (Ads.isLoaded(adFormat, TAG_INTERSTITIAL_WIDGET)) {
      showAppWidget(adsWidgetContainer);
    } else {
      adsWidgetContainer.setVisibility(View.GONE);
      Log.d(TAG, "Preload data for interstitial Ads.");
      Ads.preLoad(this, adFormat, TAG_INTERSTITIAL_WIDGET);
      new Thread() {
        @Override
        public void run() {
          try {
            while (!Ads.isLoaded(adFormat, TAG_INTERSTITIAL_WIDGET)) {
              Log.d(TAG, "Wait loading for a while...");
              Thread.sleep(2000);
            }
            Log.d(TAG, "Ads data had been loaded.");
            new Handler(Looper.getMainLooper()).post(new Runnable() {
              @Override
              public void run() {
                adsWidgetContainer.setVisibility(View.VISIBLE);
                showAppWidget(adsWidgetContainer);
              }
            });
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }.start();
    }

    showBannerAd();
  }

  @Override
  protected void onStart() {
    super.onStart();
    // This calling is not mandatory since SDK had been in control for most of case when view
    // becomes visible or on attached to window or SCREEN ON.
    // But StartingAutoScroll on BannerAd manually is also a definite way to make sure of it.
    // adBanner.startAutoScroll();
  }

  @Override
  protected void onStop() {
    // This calling is not mandatory since SDK had been in control for most of case when view
    // becomes invisible or on detached to window or SCREEN OFF.
    // But StoppingAutoScroll on BannerAd manually is also a definite way to make sure of it.
    // adBanner.stopAutoScroll();
    super.onStop();
  }

  private void drawUpdateIndicator(int color, boolean drawLeftOrRight) {
    ShapeDrawable smallerCircle = new ShapeDrawable(new OvalShape());
    smallerCircle.setIntrinsicHeight(60);
    smallerCircle.setIntrinsicWidth(60);
    smallerCircle.setBounds(new Rect(0, 0, 60, 60));
    smallerCircle.getPaint().setColor(color);
    smallerCircle.setPadding(50, 50, 50, 100);

    Drawable drawableleft = null;
    Drawable drawableRight = null;
    if (drawLeftOrRight) {
      drawableleft = smallerCircle;
    } else {
      drawableRight = smallerCircle;
    }
    showAppWallButton.setCompoundDrawables(drawableleft, null, drawableRight, null);
  }

  void showAppWidget(final ViewGroup container) {
    container.addView(Ads.showAppWidget(this, null, TAG_INTERSTITIAL_WIDGET, Ads.ShowMode.WIDGET,
        new OnClickListener() {
          @Override
          public void onClick(View v) {
            container.setVisibility(View.GONE);
          }
        }
    ));
  }

  void showBannerAd() {
    ViewGroup containerView = (ViewGroup) findViewById(R.id.banner_ad_container);
    if (adBannerView != null && containerView.indexOfChild(adBannerView) >= 0) {
      containerView.removeView(adBannerView);
    }
    adBanner = Ads.showBannerAd(this, (ViewGroup) findViewById(R.id.banner_ad_container),
        TAG_BANNER);
    adBannerView = adBanner.getView();
  }


  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
    getMenuInflater().inflate(R.menu.main, menu);
    return true;
  }

}
