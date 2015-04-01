package com.infiniteloop.mytasks;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by theotherside on 31/03/15.
 */
public class SettingFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

    }
}
