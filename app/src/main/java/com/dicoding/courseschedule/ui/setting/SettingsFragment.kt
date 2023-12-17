package com.dicoding.courseschedule.ui.setting

import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.NightMode
import androidx.preference.ListPreference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.notification.DailyReminder

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        //TODO 10 : Update theme based on value in ListPreference

        val settingTheme : ListPreference? = findPreference(getString(R.string.pref_key_dark))
        settingTheme?.setOnPreferenceChangeListener { preference, newValue ->
            val string = newValue.toString()
            if (string == getString(R.string.pref_dark_auto)){
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                    updateTheme(com.dicoding.courseschedule.util.NightMode.AUTO.value)
                else updateTheme(com.dicoding.courseschedule.util.NightMode.ON.value)
            } else
                if (string == getString(R.string.dark_mode_off)) updateTheme(com.dicoding.courseschedule.util.NightMode.OFF.value)
                else updateTheme(com.dicoding.courseschedule.util.NightMode.ON.value)

            true
        }

        //TODO 11 : Schedule and cancel notification in DailyReminder based on SwitchPreference

        val changedNotification : SwitchPreference? = findPreference(getString(R.string.pref_key_notify))

        changedNotification?.setOnPreferenceChangeListener { preference, newValue ->
            val reminder = DailyReminder()

            if (newValue == true) {
                reminder.setDailyReminder(requireContext())
                Toast.makeText(activity, "ON", Toast.LENGTH_SHORT).show()
            } else {
                reminder.cancelAlarm(requireContext())
                Toast.makeText(activity, "OFF", Toast.LENGTH_SHORT).show()
            }

            true

        }

    }

    private fun updateTheme(nightMode: Int): Boolean {
        AppCompatDelegate.setDefaultNightMode(nightMode)
        requireActivity().recreate()
        return true
    }
}