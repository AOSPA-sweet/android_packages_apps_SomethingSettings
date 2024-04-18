package org.somethingos.somethingsettings.fragments.networktraffic;

import static android.provider.Settings.System.NETWORK_TRAFFIC_AUTOHIDE;
import static android.provider.Settings.System.NETWORK_TRAFFIC_STATE;

import android.content.ContentResolver;
import android.os.Bundle;
import android.os.UserHandle;
import android.provider.Settings;
import android.widget.Switch;

import androidx.preference.Preference;
import androidx.preference.SwitchPreference;

import com.android.settings.R;
import com.android.settings.SettingsPreferenceFragment;
import com.android.settingslib.widget.MainSwitchPreference;
import com.android.settingslib.widget.OnMainSwitchChangeListener;

public class NetworkTrafficSettings extends SettingsPreferenceFragment implements
        Preference.OnPreferenceChangeListener, OnMainSwitchChangeListener {

    private static final String KEY = "network_traffic_state";
    private static final String KEY_AUTOHIDE = "network_traffic_autohide";

    private MainSwitchPreference mMainPref;
    private SwitchPreference mAutoHidePref;

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        addPreferencesFromResource(R.xml.network_traffic_settings);

        final ContentResolver resolver = getActivity().getContentResolver();

        final boolean enabled = Settings.System.getIntForUser(resolver,
                NETWORK_TRAFFIC_STATE, 0, UserHandle.USER_CURRENT) == 1;
        mMainPref = (MainSwitchPreference) findPreference(KEY);
        mMainPref.setChecked(enabled);
        mMainPref.addOnSwitchChangeListener(this);

        final boolean autoHide = Settings.System.getIntForUser(resolver,
                NETWORK_TRAFFIC_AUTOHIDE, 1, UserHandle.USER_CURRENT) == 1;
        mAutoHidePref = (SwitchPreference) findPreference(KEY_AUTOHIDE);
        mAutoHidePref.setChecked(autoHide);
        mAutoHidePref.setOnPreferenceChangeListener(this);
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object objValue) {
        if (preference == mAutoHidePref) {
            final boolean value = (Boolean) objValue;
            Settings.System.putIntForUser(getActivity().getContentResolver(),
                    NETWORK_TRAFFIC_AUTOHIDE, value ? 1 : 0, UserHandle.USER_CURRENT);
            return true;
        }
        return false;
    }

    @Override
    public void onSwitchChanged(Switch switchView, boolean isChecked) {
        Settings.System.putIntForUser(getActivity().getContentResolver(),
                NETWORK_TRAFFIC_STATE, isChecked ? 1 : 0, UserHandle.USER_CURRENT);
    }

    @Override
    public int getMetricsCategory() {
        return -1;
    }
}