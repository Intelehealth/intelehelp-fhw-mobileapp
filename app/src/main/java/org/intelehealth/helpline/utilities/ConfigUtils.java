package org.intelehealth.helpline.utilities;

import android.content.Context;
import android.widget.Toast;


import com.google.firebase.crashlytics.FirebaseCrashlytics;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

import org.intelehealth.helpline.app.IntelehealthApplication;

import org.intelehealth.helpline.app.AppConstants;

public class ConfigUtils {
    public static String TAG = ConfigUtils.class.getSimpleName();
    SessionManager sessionManager = null;
    Context context;

    public ConfigUtils(Context context) {
        this.context = context;
    }

    private JSONObject jsonreader() {
        sessionManager = new SessionManager(IntelehealthApplication.getAppContext());
        JSONObject obj = null;
        try {
//            if (sessionManager.valueContains("licensekey")) {
            //NonNull added to handle null values in case of downloaded mm's.
            //Load the config file
            if (!sessionManager.getLicenseKey().isEmpty())
                obj = new JSONObject(Objects.requireNonNullElse
                        (FileUtils.readFileRoot(AppConstants.CONFIG_FILE_NAME, context),
                                String.valueOf(FileUtils.encodeJSON(context, AppConstants.CONFIG_FILE_NAME))));
            else
                obj = new JSONObject(String.valueOf(FileUtils.encodeJSON(context, AppConstants.CONFIG_FILE_NAME)));

        } catch (JSONException e) {
            Logger.logE(TAG, "Exception", e);
            Toast.makeText(context, "JsonException" + e, Toast.LENGTH_LONG).show();
        }
        return obj;
    }


    public boolean height() {
        boolean view = false;

        JSONObject object = jsonreader();
        try {
            view = object.getBoolean("mHeight");
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return view;
    }

    public boolean weight() {
        boolean view = false;

        JSONObject object = jsonreader();
        try {
            view = object.getBoolean("mWeight");
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return view;
    }


    public boolean temperature() {
        boolean view = false;

        JSONObject object = jsonreader();
        try {
            view = object.getBoolean("mTemperature");
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return view;
    }

    public boolean celsius() {
        boolean view = false;

        JSONObject object = jsonreader();
        try {
            view = object.getBoolean("mCelsius");
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return view;
    }

    public boolean fahrenheit() {
        boolean view = false;

        JSONObject object = jsonreader();
        try {
            view = object.getBoolean("mFahrenheit");
            Logger.logD(TAG, String.valueOf(view));
        } catch (JSONException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return view;
    }

    public boolean privacy_notice() {
        boolean view = false;

        JSONObject object = jsonreader();
        try {
            view = object.getBoolean("privacyNotice");
        } catch (JSONException | NullPointerException e) {
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return view;
    }

}
