package net.incredibles.brtaquiz.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;

/**
 * @author sharafat
 * @Created 2/16/12 5:11 PM
 */
public class SharedPreferencesWrapper {
    private SharedPreferences sharedPreferences;

    public SharedPreferencesWrapper(Context context, String fileName) {
        this(context, fileName, Context.MODE_PRIVATE);
    }

    public SharedPreferencesWrapper(Context context, String fileName, int mode) {
        sharedPreferences = context.getSharedPreferences(fileName, mode);
    }

    public boolean clear() {
        return sharedPreferences.edit().clear().commit();
    }

    public boolean remove(String key) {
        return sharedPreferences.edit().remove(key).commit();
    }

    public boolean contains(String key) {
        return sharedPreferences.contains(key);
    }

    public Map<String, ?> getAll() {
        return sharedPreferences.getAll();
    }

    public void registerChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener);
    }

    public void unregisterChangeListener(SharedPreferences.OnSharedPreferenceChangeListener listener) {
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(listener);
    }

    public boolean get(String key, boolean defaultValue) {
        return sharedPreferences.getBoolean(key, defaultValue);
    }

    public String get(String key, String defaultValue) {
        return sharedPreferences.getString(key, defaultValue);
    }

    public int get(String key, int defaultValue) {
        return sharedPreferences.getInt(key, defaultValue);
    }

    public long get(String key, long defaultValue) {
        return sharedPreferences.getLong(key, defaultValue);
    }

    public float get(String key, float defaultValue) {
        return sharedPreferences.getFloat(key, defaultValue);
    }

    public boolean set(String key, boolean value) {
        return setPreference(key, value);
    }

    public boolean set(String key, String value) {
        return setPreference(key, value);
    }

    public boolean set(String key, int value) {
        return setPreference(key, value);
    }

    public boolean set(String key, long value) {
        return setPreference(key, value);
    }

    public boolean set(String key, float value) {
        return setPreference(key, value);
    }

    private boolean setPreference(String key, Object value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();

        if (value instanceof String) {
            editor.putString(key, (String) value);
        } else if (value instanceof Boolean) {
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Integer) {
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Float) {
            editor.putFloat(key, (Float) value);
        } else if (value instanceof Long) {
            editor.putLong(key, (Long) value);
        }

        return editor.commit();
    }
}
