package com.rninstallreferrer;

import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeMap;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Promise;

import android.os.RemoteException;
import java.lang.IllegalStateException;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;

public class RNInstallReferrerModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;
  InstallReferrerClient mReferrerClient = null;

  public RNInstallReferrerModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "RNInstallReferrer";
  }

  @ReactMethod
  public void getReferrer(final Promise promise) {
    try {
      mReferrerClient = InstallReferrerClient.newBuilder(reactContext).build();
      mReferrerClient.startConnection(new InstallReferrerStateListener() {
        @Override
        public void onInstallReferrerSetupFinished(int responseCode) {
          WritableMap result = new WritableNativeMap();
          switch (responseCode) {
          case InstallReferrerClient.InstallReferrerResponse.OK:
            try {
              ReferrerDetails response = mReferrerClient.getInstallReferrer();
              result.putString("installTimestamp", String.valueOf(response.getInstallBeginTimestampSeconds()));
              result.putString("installReferrer", response.getInstallReferrer());
              result.putString("clickTimestamp", String.valueOf(response.getReferrerClickTimestampSeconds()));
              mReferrerClient.endConnection();
            } catch (RemoteException e) {
              result.putString("error", e.getMessage());
              e.printStackTrace();
            } catch (IllegalStateException e) {
              result.putString("error", e.getMessage());
              e.printStackTrace();
            }
            break;
          case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
            result.putString("message", "FEATURE_NOT_SUPPORTED");
            break;
          case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
            result.putString("message", "SERVICE_UNAVAILABLE");
            break;
          default:
            result.putString("message", "UNKNOWN_RESPONSE_CODE: " + responseCode);
          }
          promise.resolve(result);
        }

        @Override
        public void onInstallReferrerServiceDisconnected() {
        }
      });
    } catch (Exception  e) {
      promise.reject(e);
    }
  }
}
