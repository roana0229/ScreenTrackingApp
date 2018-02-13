package app.roana0229.org.screentrackingapp.utility;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.UUID;

import app.roana0229.org.screentrackingapp.tracking.Event;

public class TrackingLogger {

    private static final String TAG = TrackingLogger.class.getSimpleName();

    private static final TrackingLogger instance = new TrackingLogger();
    private String session;
    private int pvCount;
    private String prevScreenName;

    public static TrackingLogger getInstance() {
        return instance;
    }

    private TrackingLogger() {
        init();
    }

    public void init() {
        session = UUID.randomUUID().toString().replaceAll("-", "");
        pvCount = 0;
        prevScreenName = null;
    }

    public void sendScreen(@NonNull String screenName, @Nullable HashMap<String, Object> params, long exposureTime) {
        pvCount += 1;

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(prevScreenName != null ? screenLogString(prevScreenName, pvCount - 1) : "");
        stringBuilder.append(" -> ");
        stringBuilder.append(screenLogString(screenName, pvCount));
        stringBuilder.append(screenExposureLogString(exposureTime));
        stringBuilder.append(paramsLogString(params));

        log(stringBuilder.toString());

        prevScreenName = screenName;
    }

    public void sendEvent(@NonNull String screenName, @NonNull Event event) {
        sendEvent(screenName, event, null);
    }

    // TODO: Eventをこのフレームワークで持たないようにするために、interfaceで受け取る
    public void sendEvent(@NonNull String screenName, @NonNull Event event, @Nullable HashMap<String, Object> params) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(eventLogString(screenName, event));
        stringBuilder.append(paramsLogString(params));
        log(stringBuilder.toString());
    }

    private void log(@NonNull String string) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(">");
        stringBuilder.append(divide());
        stringBuilder.append("\n");
        stringBuilder.append(sessionLogString());
        stringBuilder.append("\n");
        stringBuilder.append(string);
        stringBuilder.append("\n");
        stringBuilder.append("<");
        stringBuilder.append(divide());
        Log.i(TAG, stringBuilder.toString());
    }

    private String divide() {
        return "================================================================";
    }

    private String sessionLogString() {
        return String.format("session: %s", session);
    }

    private String screenLogString(@NonNull String screenName, int pvCount) {
        return String.format("%s(pv:%d)", screenName, pvCount);
    }

    private String screenExposureLogString(long exposureTime) {
        return String.format(" exposureTime: %dms", exposureTime);
    }

    private String eventLogString(@NonNull String screenName, @NonNull Event event) {
        return String.format("%s(event:%s)", screenName, event.name());
    }

    private String paramsLogString(@Nullable HashMap<String, Object> hashMap) {
        if (hashMap != null) {
            try {
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append("\n");
                stringBuilder.append(String.format("params:%s", new JSONObject(hashMap).toString(2)));
                return stringBuilder.toString();
            } catch (JSONException e) {
                Log.e(TAG, "Tracking Params ParseError", e);
            }
        }
        return "";
    }

}