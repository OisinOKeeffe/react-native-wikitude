package com.brave.wikitudebridge;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Toast;

import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.wikitude.architect.ArchitectView;
import com.wikitude.common.permission.PermissionManager;

import com.facebook.react.bridge.ActivityEventListener;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.facebook.react.bridge.WritableMap;

import java.io.File;
import java.util.Arrays;
import android.util.Log;

public class WikitudeModule extends ReactContextBaseJavaModule implements ActivityEventListener {
  private ReactContext mReactContext;
  protected static final String TAG = "WikitudeModule";
  private static final int PICK_IMAGE = 1;

  public WikitudeModule(ReactApplicationContext reactContext) {
    super(reactContext);
    mReactContext = reactContext;
    reactContext.addActivityEventListener(this);
  }

  @Override
  public void onActivityResult(Activity activity, final int requestCode, final int resultCode, final Intent intent) {

    if( intent != null && WikitudeActivity.WIKITUDE_RESULT != null)
    {
      Log.i(TAG, "Override-onActivityResult "+ intent.getStringExtra(WikitudeActivity.WIKITUDE_RESULT) + "");
      mReactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit("WikitudeResult", intent.getStringExtra(WikitudeActivity.WIKITUDE_RESULT));
    }
     
  }

  @Override
  public void onNewIntent(Intent intent) {

  }

  @Override
  public String getName() {
    return "RNWikitude";
  }

  /*@ReactMethod
  public void alert(String message) {
    Toast.makeText(getReactApplicationContext(), message, Toast.LENGTH_LONG).show();
  }*/

  @ReactMethod
  public void startAR(String architectWorldURL, boolean hasGeolocation, boolean hasImageRecognition, boolean hasInstantTracking, String wikitudeSDKKey)
  {
    final Activity currentActivity = getCurrentActivity();

	  final Intent intent = new Intent(currentActivity, WikitudePrecheck.class);

	  intent.putExtra(WikitudeActivity.EXTRAS_KEY_AR_URL, architectWorldURL);
	  intent.putExtra(WikitudeActivity.EXTRAS_KEY_HAS_GEO, hasGeolocation);
	  intent.putExtra(WikitudeActivity.EXTRAS_KEY_HAS_IR, hasImageRecognition);
    intent.putExtra(WikitudeActivity.EXTRAS_KEY_HAS_INSTANT, hasInstantTracking);
    intent.putExtra(WikitudeActivity.EXTRAS_KEY_SDK_KEY, wikitudeSDKKey);

	  //launch activity
	  currentActivity.startActivityForResult(intent, PICK_IMAGE);
  }

}