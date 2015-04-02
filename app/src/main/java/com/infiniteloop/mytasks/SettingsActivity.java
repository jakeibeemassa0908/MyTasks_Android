package com.infiniteloop.mytasks;

import android.os.Bundle;
import android.preference.PreferenceActivity;

/**
 * Created by theotherside on 02/04/15.
 */
public class SettingsActivity extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}