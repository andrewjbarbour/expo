// Copyright 2015-present 650 Industries. All rights reserved.

package abi39_0_0.host.exp.exponent.modules.universal;

import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;

import org.json.JSONException;

import abi39_0_0.org.unimodules.interfaces.constants.ConstantsInterface;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import androidx.annotation.Nullable;
import abi39_0_0.expo.modules.constants.ConstantsService;
import expo.modules.manifests.core.Manifest;
import host.exp.exponent.Constants;
import host.exp.exponent.di.NativeModuleDepsProvider;
import host.exp.exponent.kernel.ExpoViewKernel;
import host.exp.exponent.kernel.KernelConstants;
import host.exp.exponent.storage.ExponentSharedPreferences;

public class ConstantsBinding extends ConstantsService implements ConstantsInterface {

  @Inject
  ExponentSharedPreferences mExponentSharedPreferences;

  private final Map<String, Object> mExperienceProperties;
  private Manifest mManifest;

  private static int convertPixelsToDp(float px, Context context) {
    Resources resources = context.getResources();
    DisplayMetrics metrics = resources.getDisplayMetrics();
    float dp = px / (metrics.densityDpi / 160f);
    return (int) dp;
  }

  public ConstantsBinding(Context context, Map<String, Object> experienceProperties, Manifest manifest) {
    super(context);
    NativeModuleDepsProvider.getInstance().inject(ConstantsBinding.class, this);

    mManifest = manifest;
    mExperienceProperties = experienceProperties;

    int resourceId = mContext.getResources().getIdentifier("status_bar_height", "dimen", "android");
    mStatusBarHeight = resourceId > 0 ? convertPixelsToDp(mContext.getResources().getDimensionPixelSize(resourceId), mContext) : 0;
  }

  @Nullable
  @Override
  public Map<String, Object> getConstants() {
    Map<String, Object> constants = super.getConstants();

    constants.put("expoVersion", ExpoViewKernel.getInstance().getVersionName());
    constants.put("manifest", mManifest.toString());
    constants.put("nativeAppVersion", ExpoViewKernel.getInstance().getVersionName());
    constants.put("nativeBuildVersion", Constants.ANDROID_VERSION_CODE);
    constants.put("supportedExpoSdks", Constants.SDK_VERSIONS_LIST);

    String appOwnership = getAppOwnership();

    constants.put("appOwnership", appOwnership);
    constants.putAll(mExperienceProperties);

    Map<String, Object> platform = new HashMap<>();
    Map<String, Object> androidPlatform = new HashMap<>();

    Integer versionCode = appOwnership.equals("expo") ? null : Constants.ANDROID_VERSION_CODE;
    androidPlatform.put("versionCode", versionCode);

    platform.put("android", androidPlatform);
    constants.put("platform", platform);
    constants.put("isDetached", Constants.isStandaloneApp());

    return constants;
  }

  public String getAppScopeKey() {
    try {
      return mManifest.getScopeKey();
    } catch (JSONException e) {
      return null;
    }
  }

  public String getAppOwnership() {
    if (mExperienceProperties.containsKey(KernelConstants.MANIFEST_URL_KEY)) {
      String manifestUrl = (String) mExperienceProperties.get(KernelConstants.MANIFEST_URL_KEY);

      if (Constants.INITIAL_URL == null) {
        return "expo";
      } else if (manifestUrl.equals(Constants.INITIAL_URL)) {
        return "standalone";
      } else {
        return "guest";
      }
    } else {
      return "expo";
    }
  }

  @Override
  public String getOrCreateInstallationId() {
    // Override scoped installationId from ConstantsService with unscoped
    return mExponentSharedPreferences.getOrCreateUUID();
  }
}
