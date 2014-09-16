package com.wandoujia.adssdk.sample;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import com.fyzb.gg.sdk.AdListener;
import com.fyzb.gg.sdk.widget.SmartAdView;

/**
 * Created by changyizhang on 7/31/14.
 */
public class SmartAdSample extends Activity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    setContentView(R.layout.activity_smart_ad);

    try {
      new MyCustomSmartAdView(this, "61a56ba5cc2cae85905af9453e26c697").load(new AdListener() {

        @Override
        public void onAdReady() {

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
      }).into((android.view.ViewGroup) findViewById(R.id.wdj_smart_ad_container));
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static class MySmartAdView extends SmartAdView {
    public MySmartAdView(Context context, String id) {
      super(context, id);
    }

    public MySmartAdView(Context context, String appId, String secretKey, String id) throws Exception {
      super(context, appId, secretKey, id);
    }
  }

  static class MyCustomSmartAdView extends SmartAdView {

    public MyCustomSmartAdView(Context context, String id) {
      super(context, id);
    }

    public MyCustomSmartAdView(Context context, String appId, String secretKey, String id) throws Exception {
      super(context, appId, secretKey, id);
    }

    @Override
    public int getLayoutId() {
      return R.layout.custom_smart_ad_view;
    }

    @Override
    public int getBigPictureViewId() {
      return R.id.app_widget_big_picture;
    }

    @Override
    public String getDownloadText() {
      return "点击安装";
    }

    @Override
    public String getDownloadingText() {
      return "下载进行时";
    }
  }
}
